import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {

    public static BufferedImage filter(byte[] file,Kernel kernel) throws IOException {
        File image = new File("tmpFile.jpeg");
        FileOutputStream fileOutputStream = new FileOutputStream(image);
        fileOutputStream.write(file);
        fileOutputStream.close();

        BufferedImage image2 = ImageIO.read(image);
        ConvolveOp convolution = new ConvolveOp(kernel,ConvolveOp.EDGE_NO_OP,null);
        BufferedImage resultat=convolution.filter(image2, null);
               

        return resultat;
    }
}