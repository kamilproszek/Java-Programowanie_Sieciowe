public class Main {
    public static void main(String[] args) throws Exception
    {
      FileServer fs=new FileServer(5201,"SERWER\\pliki_serwer\\");
      fs.serverConnection();
      //fs.sendFileToClient();
      fs.getFileFromClient();

    }
}