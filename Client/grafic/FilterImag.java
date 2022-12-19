/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JLabel;

/**
 *
 * @author Dell
 */
public class FilterImag {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Filter f = new Filter();
        f.setVisible(true);
    }
}
