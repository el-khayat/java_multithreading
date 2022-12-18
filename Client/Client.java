import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        int port = 3334 ;
        String host = "localhost" ;
        String src = "./assets/img.jpeg" ;
        String dis = "./assets/imgFiltred.jpeg" ;
        float[] arrayKirnel = {0f, -1f,0f, -1f, 5f,-1f, 0f, -1f, 0f};

        try {


            Socket socket;
            ObjectInputStream in;
            ObjectOutputStream out;

            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Data data = new Data();
            File imagef = new File(src);
            FileInputStream fileInputStream = new FileInputStream(imagef);
            byte[] b = new byte[fileInputStream.available()];
            fileInputStream.read(b);
            fileInputStream.close();
            data.setF(b);
            data.setArrayKirnel(arrayKirnel);
            data.setHegth(3);
            data.setWidth(3);
            out.writeObject(data);
            out.flush();
            
            data=(Data)in.readObject();
            File file = new File(dis);
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            fileOutputStream.write(data.getF());
            fileOutputStream.close();
            socket.close();

        }catch(IOException e){
            throw new RuntimeException(e);
        }catch (RuntimeException e){

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



}