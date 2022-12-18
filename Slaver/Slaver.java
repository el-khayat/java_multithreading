import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Slaver extends Thread {
    ServerSocket serverSocket ;
    public int port = 3334;

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("server is running at port "+port);
            while (true)
                new MTClient(serverSocket.accept()).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        new Slaver().start();
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
                long start = System.currentTimeMillis();

                int id = data.id;
                Kernel kernel = new Kernel(data.getWidth(), data.getHegth(),data.getArrayKirnel());
                BufferedImage res = Util.filter(data.f,kernel);

                byte[] b = Util.bufferedToByte(res);

                data = new Data();
                data.setF(b);
                data.setId(id);
                out.writeObject(data);
                out.flush();
                
                long now = System.currentTimeMillis();
                System.out.println(" duree est ");
                System.out.println(now-start);
                //=======
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}