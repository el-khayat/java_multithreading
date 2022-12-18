import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Slaver extends Thread {
    ServerSocket serverSocket ;

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(3334);
            System.out.println("server is running at port 3334");
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

                System.out.println("id is "+data.id);
                int id = data.id;
                Kernel kernel = new Kernel(3, 3, new float[] {0f, -1f,0f, -1f, 5f,-1f, 0f, -1f, 0f});
                BufferedImage res = Util.filter(data.f,kernel);
                File f = new File("retImage.jpeg");
                ImageIO.write(res,"jpeg",f);


                FileInputStream inf = new FileInputStream(f);
                byte b[] = new byte[inf.available()];
                inf.read(b);

                data = new Data();
                data.setF(b);
                data.setId(id);
                out.writeObject(data);
                out.flush();
                

                long now = System.currentTimeMillis();
                System.out.println(" duree est ");
                System.out.println(now-start);
                inf.close();
                //=======
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}