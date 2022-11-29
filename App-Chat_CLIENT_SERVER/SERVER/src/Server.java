import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Server {

    //1-inicjalizacja serewa, 2 obiekt tworzenie polaczeniz  klientem
    private ServerSocket serverSocket;
    private Socket socket;

    public Server(int port){
        try {
            serverSocket=new ServerSocket(port);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }


    public void serverConnection() {

        while (true) {
            try {
                System.out.println("oczekuje na klienta .... ");
                socket = serverSocket.accept();
                System.out.println("nawiazalem polaczenie");
                sendMessageToClient();
                disconnectClient();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessageToClient() {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(),
                    true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), StandardCharsets.UTF_8
            ));
            String text;
            while(true){
                writer.println("Witaj podaj imię:");
                text = reader.readLine();
                if (text.equals("e")){
                    System.out.println("klient zakończył połączenie");
                    writer.close();
                    reader.close();
                    break;
                }
                System.out.println(text);
                writer.println(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectClient(){
        try{
            socket.close();
            System.out.println("zakonczenie polaczenia z klientem");

        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
