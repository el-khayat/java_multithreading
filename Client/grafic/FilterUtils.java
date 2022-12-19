
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dell
 */
public  class FilterUtils {
    
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in ;
    private Server server ;
    private static String fileConfigpath = "C:\\Users\\dell\\Desktop\\servers.txt";
                
    public static File applyFilter(File image, float[] arrayKirnel ) throws IOException{
    
    image  = applyFilterTCP(image,arrayKirnel);       
    
    return image ;
    }


    public static class Server{
    String host ;
    String type ;
    String name ;
    int port ;

        public Server(String host, String type, String name, int port) {
            this.host = host;
            this.type = type;
            this.name = name;
            this.port = port;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public int getPort() {
            return port;
        }
        
    
    }
    
    static Server getServer(String srvr) throws FileNotFoundException, IOException{
    Server sr =null;
        File serversFile = new File(fileConfigpath);
        BufferedReader br = new BufferedReader(new FileReader(serversFile)); 
        String line="";
         while(line!=null ){
          line =  br.readLine();
          if(line == null) break;
         String[] serverInfo = line.split(";");
         if(srvr.equals(serverInfo[0])){
            int port = Integer.parseInt(serverInfo[3]);
            String host = serverInfo[2];
            String type = serverInfo[1];
            sr = new Server(host, type, srvr, port);
                     
         }
         }
         return sr;
    }
    static File   applyFilterRMI(Server server,File image,String filter){
        byte[] br = new byte[100];
            try{
                
        Registry registry = LocateRegistry.getRegistry(server.host,server.port); 
        FilterIner stub = (FilterIner) registry.lookup("Test"); 

         // Looking up the registry for the remote object   File image = new File("imqge.jpg")                                 
                                FileInputStream inf = new FileInputStream(image);
                                 byte b[] = new byte[inf.available()];
                                 inf.read(b);
                                switch(filter){
                                    case "gray"  :
                                     image = stub.Grayscale(b);

                                        break;
                                case "negative"  :
                                    br = stub.negative(b);
                                    FileOutputStream fileOutputStream = new FileOutputStream(image);
                                    fileOutputStream.write(br);
                                    return image;
                                    
                                case "red"  :
                                     image = stub.green(b);
                                        break;                                
                                case "sepia"  :
                                     image = stub.sepia(b);
                                        break;
                                case "blue"  :
                                     image = stub.sepia(b);
                                        break;
                                case "green"  :
                                     image = stub.sepia(b);
                                        break;
                                        
                                }
                                
        }catch (Exception e) {
         System.err.println("Client exception: " + e.toString()); 
         e.printStackTrace(); 
      }
         return image;   
     }
    static File   applyFilterTCP(File image,float[] arrayKirnel ){
     Socket  socket ;
     ObjectOutputStream out ;
     ObjectInputStream in ;
     File ff = null ;
     String src = "../assets/img.jpeg" ;
     String dis = "../assets/imgFiltred.jpeg" ;
     //float[] arrayKirnel = {0f, -1f,0f, -1f, 5f,-1f, 0f, -1f, 0f};
          try {
            socket = new Socket("localhost", 3336);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Data data = new Data();
            File imagef = new File(src);
            FileInputStream fileInputStream = new FileInputStream(image);
            byte[] b = new byte[fileInputStream.available()];
            fileInputStream.read(b);
            fileInputStream.close();
            data.setF(b);
            data.setArrayKirnel(arrayKirnel);
            data.setHegth(3);
            data.setWidth(3);
            out.writeObject(data);
            out.flush();
            // start timer 
            long start = System.currentTimeMillis();

            
            data=(Data)in.readObject();
            File file = new File(dis);
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            fileOutputStream.write(data.getF());
            fileOutputStream.close();
            socket.close();
            long now = System.currentTimeMillis();
                System.out.println(" duree est ");
                System.out.println(now-start);

        return file;

        } catch (Exception e) {
              System.out.println("FilterUtils.applyFilterTCP()");
        }
         return ff;   
     }
    
}
