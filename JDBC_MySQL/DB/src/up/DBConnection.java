package up;

import java.sql.*;

public class DBConnection {
    private Connection conn;

    public DBConnection(){
        conn=null;
    }

    //sqlite
    public Connection connectToSqlite(){
        try {
            conn=DriverManager.getConnection("jdbc:sqlite:baza.db");
            System.out.println("Nazwiązano połączenie");
            return conn;
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return null;
    }

    //mysql
    public Connection connectToMySql() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test",
                    "root",
                    ""
            );

            System.out.println("Połączono z bazą");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    public void createTable() throws SQLException {
      try {
          PreparedStatement preper = conn.prepareStatement(
                  "CREATE TABLE Person " +
                  "(Id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                  "Name varchar(60)," +
                  "LastName varchar(60)," +
                  "Age INTEGER);");

          preper.execute();
      }catch (SQLException e){
          e.printStackTrace();
      }
    }

    public void disconnect(){
        try {
            if(!conn.isClosed()){
                System.out.println("połaczenie zakonczone");
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Savepoint getPoint(){
        try {
            Savepoint s=conn.setSavepoint();
            conn.setAutoCommit(false);
            System.out.println("Utworzono punkt przywracania");
            return s;

        } catch (SQLException e) {
            return null;
        }
    }

    public void getRollback(Savepoint savepoint){
        try {
            conn.rollback(savepoint);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
