import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class Server extends Thread {

    public static int He;
    public static int Wi;
    public static int numberS = 3 ;
    public int port = 3336;
    static Stack  slevers = new Stack<Slaver>();
    ServerSocket serverSocket ;
    static List filtredPartey =new ArrayList<Data>();
    public Server() throws IOException {
        Util.getAvailabelSlavers(new File("./config.txt"));
    }

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("server is running at port "+port);
            while (true)
                new MTClient(serverSocket.accept()).start();
        } catch (IOException e) {
           System.out.println(" Error : Client disconnect !");
        }

    }

    public static void main(String[] args) throws IOException {
        new Server().start();
    }

    class MTClient extends Thread{
        Socket socket ;
        ObjectInputStream in ;
        ObjectOutputStream out ;
        public MTClient(Socket socket){
            this.socket = socket;
            System.out.println("client coneccted");
        }

        @Override
        public void run() {
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out= new ObjectOutputStream(socket.getOutputStream()) ;
                Data data = (Data) in.readObject();
                //========
                File image = new File("./assets/ImageServerinit.jpeg");
                FileOutputStream outf = new FileOutputStream(image);
                outf.write(data.f);
                outf.close();
                Stack st = Util.Decouper(image,Server.numberS);
                Util.DistToSlavers(st,slevers);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                           //sleep(5000);
                            while (true){
                                if (filtredPartey.size() < Server.numberS){
                                    System.out.println(" waiting for slavers ... ");
                                    continue;
                                }
                                data.setF(Util.Merge(Server.filtredPartey));
                                out.writeObject(data);
                                out.flush();
                                break;
                            }
                        }catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        }

                }).start();
                //=======
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    public static class Slaver{
        String host ;
        int port ;
        int id ;

        public Slaver(String host, int port, int id) {
            this.host = host;
            this.port = port;
            this.id = id;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public int getId() {
            return id;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
