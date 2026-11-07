/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package fileintegrityapp;

import javax.swing.JFileChooser;
import java.io.File;
import java.security.MessageDigest;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
/**
 *
 * @author ciara
 */
public class FileIntegrityApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected File: "+selectedFile.getName());
            try{
                MessageDigest digest= MessageDigest.getInstance("SHA-256");
                try(FileInputStream fis = new FileInputStream(selectedFile)){
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer))!= -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }
            byte[] hashBytes= digest.digest();
            StringBuilder hexString = new StringBuilder();

            for(byte b : hashBytes){
            hexString.append(String.format("%02x", b));
                }
            System.out.println("SHA-256 Hash: " + hexString.toString());
            } catch (NoSuchAlgorithmException | IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
}
