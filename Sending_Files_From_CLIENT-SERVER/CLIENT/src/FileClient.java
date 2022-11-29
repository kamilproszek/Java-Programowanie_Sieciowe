import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class FileClient {

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    String defaultLocation;
    private Socket socket;
    private String host;
    private int port;

    public FileClient(String h, int p, String l){
        host = h;
        port = p;
        defaultLocation = l;
    }

    public  void connection(){
        try{
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            if(socket.isConnected()){
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                System.out.println("Nawiązano połączenie z serverem");
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void sendFileToServer(){
        System.out.println("Wysylanie plikow");
        File file = new File(defaultLocation + "test.txt");
        int fileNameLength = file.getName().length();
        byte[] fileNameBytes = file.getName().getBytes(StandardCharsets.UTF_8);

        long fileContentLength = file.length();
        byte[] fileContentBytes = new byte[(int) fileContentLength];

        try {
            FileInputStream filein = new FileInputStream(file);
            filein.read(fileContentBytes);
            filein.close();
            // przesłanie nazwy  pliku
            dataOutputStream.writeInt(fileNameLength);
            dataOutputStream.write(fileNameBytes);
            // przesłanie zawartości pliku
            dataOutputStream.writeLong(fileContentLength);
            dataOutputStream.write(fileContentBytes);
            dataOutputStream.flush();
            System.out.println("Wyslano plik do serwera");
        }catch(FileNotFoundException e){
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getFileFromServer(){
        try {
            int fileNameLength = dataInputStream.readInt();
            if (fileNameLength > 0){
                byte[] fileNameBytes = new byte[fileNameLength];
                dataInputStream.readFully(fileNameBytes);
                String fileName = new String(fileNameBytes);
                long fileContentLength = dataInputStream.readLong();
                if(fileContentLength > 0){
                    byte[] fileContentBytes = new byte[(int) fileContentLength];
                    dataInputStream.readFully(fileContentBytes);
                    FileOutputStream fileOut =
                            new FileOutputStream(defaultLocation + fileName);
                    fileOut.write(fileContentBytes);
                    fileOut.flush();
                    fileOut.close();
                    System.out.println("Zapisano przekazany plik " + fileName);
                }else{
                    System.out.println("Plik pusty nie tworzę go");
                }
            }else{
                System.out.println("nie przekazano plku");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        if (!socket.isClosed()){
            try {
                dataOutputStream.close();
                dataInputStream.close();
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
