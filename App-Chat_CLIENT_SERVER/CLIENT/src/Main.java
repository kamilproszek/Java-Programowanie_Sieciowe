public class Main {
    public static void main(String[] args) {

        Client c=new Client("localhost", 5001);
        c.Connect();
        c.sendMessage();
        //c.disconnect();

    }
}