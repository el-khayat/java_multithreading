import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class Util {

    public static BufferedImage filter(byte[] file,Kernel kernel) throws IOException {
        File image = new File("D:\\tmpFile.jpeg");
        FileOutputStream fileOutputStream = new FileOutputStream(image);
        fileOutputStream.write(file);

        BufferedImage image2 = ImageIO.read(image);
        ConvolveOp convolution = new ConvolveOp(kernel);
        BufferedImage resultat=convolution.filter(image2, null);

        return resultat;
    }
    
    public static Stack Decouper(File image ,int n) throws IOException {
        Stack imageDivs = new Stack<BufferedImage>();
        BufferedImage bufferedImage =ImageIO.read(image);
        int he = bufferedImage.getHeight();
        int wi = bufferedImage.getWidth();
        Server.He = he ;
        Server.Wi = wi ;
        for(int i = 0; i < n; i++){
            BufferedImage tmp_Recorte = ((BufferedImage) bufferedImage).getSubimage(0, i * (he / n) , wi , he / n) ;
            imageDivs.push(tmp_Recorte);

        }
        return imageDivs ;
    }
    
    public static byte[] Merge(List paries) throws IOException {

        int x=0,y=0;
        BufferedImage result = new BufferedImage(
                Server.Wi, Server.He, //work these out
                BufferedImage.TYPE_INT_RGB);

        Data data ;
        for (int i = paries.size() ; i >=1  ;--i){

            data = Util.getItemById(i,paries);

            BufferedImage bi = byteToBuffered(data.getF());
            
            result.createGraphics().drawImage(bi,x,y,null);
            y+= bi.getHeight();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(result, "jpeg", baos);
        return baos.toByteArray();
    }
    
    public static void DistToSlavers(Stack st , Stack slavers,float[] kernel,int h,int w ){
        BufferedImage bufferedImage ;
        Stack slaversClone =  (Stack) slavers.clone();

        Iterator<BufferedImage> itr = st.iterator();
        int x=0,y=0;
        while (itr.hasNext())
        {
            BufferedImage bi = itr.next();
            Server.Slaver slaver =(Server.Slaver) slaversClone.pop();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this){
                        try {
                            Socket socket = new Socket(slaver.host,slaver.port);
                            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                            File f = new File("./assets/uImagePart"+slaver.id+".jpeg");
                            ImageIO.write(bi,"jpeg",f);

                            FileInputStream fileInputStream = new FileInputStream(f);
                            byte[] b = new byte[fileInputStream.available()];
                            fileInputStream.read(b);
                            Data data = new Data();

                            data.setId(slaver.id);
                            data.setF(b);
                            data.setHegth(h);
                            data.setWidth(w);
                            data.setArrayKirnel(kernel);
                            out.writeObject(data);

                            out.flush();

                            data = (Data) in.readObject();
                            Server.filtredPartey.add(data);
                            System.out.println(Thread.currentThread().getName()+" id is "+data.id);
                            System.out.println(" stack size after slaver response ..."+Server.filtredPartey.size());
                            socket.close();


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }
            },"distrub").start();
        }



    }
    
    public static  void getAvailabelSlavers(File file) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line ="";
        int nbrLine = 0 ;
        while (line != null){
            line = br.readLine();
            if (line ==null)break;
            String [] slaverline = line.split(";");
            Server.Slaver slaver = new Server.Slaver(slaverline[1],Integer.parseInt(slaverline[2]),Integer.parseInt(slaverline[0]));
            System.out.println("---------------------- slavers---------------");
            System.out.println(slaverline[1]+" @"+Integer.parseInt(slaverline[2])+"@"+Integer.parseInt(slaverline[0]));
            Server.slevers.push(slaver);
            nbrLine++;

        }
        Server.numberS = nbrLine ;
        System.out.println("there are "+Server.numberS+" slaver");

    }
    
    public static Data getItemById(int id,List list){
        Data data = null;
        for (int i = 0 ; i < list.size() ; i++){
           data = (Data)  list.get(i);
          if(data.id == id)
              return data;
        }
        return data ;
    }
    
    public static BufferedImage byteToBuffered(byte[] b) throws IOException{
        BufferedImage bufferedImage = null;
        InputStream is = new ByteArrayInputStream(b);
        bufferedImage = ImageIO.read(is);
         return bufferedImage ;
    }
    
    public static byte[] bufferedToByte(BufferedImage bi){
        byte[] bytes  =null ;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "jpg", baos);
             bytes = baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes ; 
    }
}
