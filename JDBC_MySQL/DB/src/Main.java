import up.DBConnection;
import up.DBOperation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner=new Scanner(System.in);

        DBConnection conn=new DBConnection();
        Connection c= conn.connectToMySql();
        //conn.createTable();
        DBOperation operation=new DBOperation(c);

        //operation.insertPersoon("Jan","Nowak",22);
        //operation.insertPersoon("Pawel","Kowalcze",19);
        //operation.insertPersoon("Gabriela","Ptak",39);
        //operation.insertPersoon("Krzysztof","Pan",39);

        //operation.wypiszRekordyMalejaco();

        //procedury
        //operation.getPersonProc(1);
        //operation.getAllPersonProc();
        //operation.getCountPersonProc();

        //System.out.println("podaj id rekordu ktory chcesz usunac: ");
        operation.wypiszRekordy();
        //int id=scanner.nextInt();
        //operation.usunRekord(id);
        //operation.wypiszRekordy();




        conn.disconnect();

    }
}