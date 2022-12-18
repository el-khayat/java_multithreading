import java.awt.image.Kernel;
import java.io.File;
import java.io.Serializable;

public class Data implements Serializable {

    byte[] f;
    int id ;
    public Data() {
    }

    public Data(byte[] f, Kernel kernel) {
        this.f = f;
    }

    public byte[] getF() {
        return f;
    }

    public void setF(byte[] f) {
        this.f = f;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}