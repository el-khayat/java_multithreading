import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Util {

    public static BufferedImage filter(byte[] file,Kernel kernel) throws IOException {
        BufferedImage bi =  byteToBuffered(file) ;
        ConvolveOp convolution = new ConvolveOp(kernel,ConvolveOp.EDGE_NO_OP,null);
        BufferedImage resultat=convolution.filter(bi, null);
        return resultat;
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

    public static BufferedImage byteToBuffered(byte[] b) throws IOException{
        BufferedImage bufferedImage = null;
        InputStream is = new ByteArrayInputStream(b);
        bufferedImage = ImageIO.read(is);
         return bufferedImage ;
    }
}