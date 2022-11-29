import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class FileServer {

    private DataOutputStream outputStream;
    private DataInputStream inputStream;
   private DataOutputStream dataOutputStream ;
    String defaultLocation;

    private ServerSocket serverSocket;
    private Socket socket;

    public FileServer(int port, String location) {
        try {
            defaultLocation=location;
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }

    public void serverConnection() {

        while (true) {
            try {

                System.out.println("Oczekuje na klienta ...");
                socket = serverSocket.accept();

                inputStream=new DataInputStream(socket.getInputStream());
                outputStream=new DataOutputStream(socket.getOutputStream());


                System.out.println("Nawiązałem połączenie z adresem" +
                        socket.getInetAddress().getHostAddress());

                getFileFromClient();
                //sendFileToClient();
                //disconnectServer();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void sendFileToClient(){
        File file = new File(defaultLocation + "test.txt");
        // pobranie nazwy i konwersja na tablice najtów
        System.out.println("przygotowywanie pliku do wysłania");
        int fileNameLength = file.getName().length();
        byte[] fileNameBytes = file.getName().getBytes(StandardCharsets.UTF_8);
        byte[] fileContentBytes = new byte[(int) file.length()];
        try {
            FileInputStream fileIn = new FileInputStream(file);
            fileIn.read(fileContentBytes);
            fileIn.close();
            // wysłanie nazwy pliku

            dataOutputStream.writeInt(fileNameLength);
            dataOutputStream.write(fileNameBytes);
            // wysyłanie zawartości pliku
            dataOutputStream.writeLong(file.length());
            dataOutputStream.write(fileContentBytes);
            dataOutputStream.flush();
            System.out.println("Wysłano plik");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getFileFromClient(){
        try {
            int fileNameLength=inputStream.readInt();
            if(fileNameLength>0){
                byte[] fileNameBytes=new byte[fileNameLength];
                inputStream.readFully(fileNameBytes);
                String fileName=new String(fileNameBytes, StandardCharsets.UTF_8);
                long fileContentLength=inputStream.readLong();
                if(fileContentLength>0){
                    byte[] fileContentBytes=new byte[(int) fileContentLength];
                    inputStream.readFully(fileContentBytes);
                    FileOutputStream fileOut=new FileOutputStream(defaultLocation+fileName);
                    fileOut.write(fileContentBytes);
                    fileOut.flush();
                    fileOut.close();
                    System.out.println("Zapisalem plik"+fileName);

                }else {
                    System.err.println("Plik jest pusty! nie zaposuje go");
                }

            }else {
                System.out.println("nie przekazano pliku");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void disconnectServer() {
        if (!socket.isClosed()) {
            try {
                outputStream.close();
                inputStream.close();
                socket.close();
                System.out.println("Zakończyłem połączenie z klientem");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}