package up;
import com.mysql.cj.MysqlType;
import java.sql.*;

public class DBOperation {
    private Connection conn;

    public DBOperation(Connection c){
        conn=c;
    }

    public void insertPersoon(String name, String lastname, int age){
        PreparedStatement preper= null;
        try {
            preper = conn.prepareStatement(
                    "INSERT INTO Person (Name, Lastname, Age) Values (?,?,?)"
            );
            preper.setString(1, name.toString());
            preper.setString(2, lastname.toString());
            preper.setInt(3,age);
            int resoult=preper.executeUpdate();     //liczba wstawionych rekordow/ lub usunietych

            //System.out.println("liczba wstawionych elementow: "+resoult);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void wypiszRekordy(){

        try {
            PreparedStatement preper=conn.prepareStatement(
                    "Select * from Person",
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY
            );

            ResultSet resultSet=preper.executeQuery();
            while (resultSet.next()){
                System.out.println(String.format("rekord: %d, %s, %s, %d ",
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void wypiszRekordyMalejaco(){

        try {
            PreparedStatement preper=conn.prepareStatement(
                    "Select * from Person order by ID DESC",
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY
            );

            ResultSet resultSet=preper.executeQuery();
            while (resultSet.next()){
                System.out.println(String.format("rekord: %d, %s, %s, %d ",
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void usunRekord(int id){
        PreparedStatement preper;
        String zapytanie= String.format("DELETE from Person where ID=%d",id);
        try {
            preper = conn.prepareStatement(zapytanie);
            preper.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getPersonProc(int id){
        CallableStatement callableStatement= null;
        try {
            callableStatement = conn.prepareCall("{call GetPersons(?,?,?,?)}");

        callableStatement.setInt(1,id);
        callableStatement.registerOutParameter(2, MysqlType.VARCHAR);
        callableStatement.registerOutParameter(3, MysqlType.VARCHAR);
        callableStatement.registerOutParameter(4, MysqlType.INT);

        callableStatement.executeUpdate();

        String name=callableStatement.getString(2);
        String lastName=callableStatement.getString(3);
        int age =callableStatement.getInt(4);
            System.out.println(String.format("rekordy z bazy danych: %s %s %d",name,lastName,age));


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getCountPersonProc() throws SQLException{
        try {
            CallableStatement callStm = conn.prepareCall(
                    "{call GetCountPerson(?)}");
            callStm.registerOutParameter(1, MysqlType.INT);
            callStm.executeUpdate();
            int number = callStm.getInt(1);
            System.out.println("Liczba rekordwo w bazie " + number);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void getAllPersonProc(){
        try {
            CallableStatement callableStatement=conn.prepareCall("{call GetAllPersons() }");
            ResultSet resultSet=callableStatement.executeQuery();

            while (resultSet.next()){
                System.out.printf("user: (%d) %s %s %d \n",
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4)
                        );
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
