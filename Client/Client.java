import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {

        try {


            Socket socket;
            ObjectInputStream in;
            ObjectOutputStream out;

            socket = new Socket("localhost", 3336);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Data data = new Data();
            File imagef = new File("D:\\img.jpeg");
            FileInputStream fileInputStream = new FileInputStream(imagef);
            byte[] b = new byte[fileInputStream.available()];
            fileInputStream.read(b);
            fileInputStream.close();
            data.setF(b);
            out.writeObject(data);
            out.flush();
            

            data=(Data)in.readObject();
            File file = new File("D:\\imgTestFinal ga3.jpeg");
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