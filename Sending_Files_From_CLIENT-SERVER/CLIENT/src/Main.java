public class Main {
    public static void main(String[] args) throws Exception {
//        Client c = new Client("localhost", 5501);
//        c.connection();
//        c.sendMessage();
//        c.disconnect();
    FileClient fc=new FileClient("localhost",5201,"CLIEN\\pliki\\");

    fc.connection();
    fc.sendFileToServer();
        //fc.getFileFromServer();
    fc.disconnect();

    }
}