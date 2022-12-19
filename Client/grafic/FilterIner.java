
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
// Creating Remote interface for our application
public interface FilterIner extends Remote {
    String printMsg() throws RemoteException;
    int add(int x,int y) throws RemoteException;
    public File Grayscale(byte file[] ) throws RemoteException ;
    public byte[] negative(byte file[] ) throws RemoteException ;
    public File red(byte file[] ) throws RemoteException ;
    public File green(byte file[] ) throws RemoteException ;
    public File blue(byte file[] ) throws RemoteException ;
    public File sepia(byte file[] ) throws RemoteException ;
    public File merge(byte file[] ) throws RemoteException ;
}
