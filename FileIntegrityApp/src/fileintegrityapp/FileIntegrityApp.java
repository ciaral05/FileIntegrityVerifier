/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/**
 @author ciara lynch
 * x23461962
 * last edited on 11/11/26
 * 
 * this class calculates the SHA-256 hash of a file in the calculateHash() method
 * this method is used by the GUI and the fileVerifier classes
 */
package fileintegrityapp;

import javax.swing.JFileChooser;
import java.io.File;
import java.security.MessageDigest;//provides access to the SHA-256 hashing algorithm we use to finc changes in the digital ifngerprint 
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;

public class FileIntegrityApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        JFileChooser fileChooser = new JFileChooser();//user selects file
        int result = fileChooser.showOpenDialog(null);
        
        if(result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected File: "+selectedFile.getName());
            
            try {
                String hash = calculateHash(selectedFile);//passes selected file as arugment to calc its sha-256 hash and then returns it for the next line 
                System.out.println("SHA-256 Hash: " + hash);
                } catch (NoSuchAlgorithmException | IOException e) { //handles two exceptions: 1- if hashing aglorithm isnt available, and 2 - if there was a problem reading the file 
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    
    //this method takes a file object as its paramter and and returns a string of the files sha-256 hash 
    public static String calculateHash(File file) throws NoSuchAlgorithmException, IOException { 
        MessageDigest digest= MessageDigest.getInstance("SHA-256");
                try(FileInputStream fis = new FileInputStream(file)){//reads files contents, we used try with resources instead of try catch for this file stream  as it automatically closes the stream even if error accurs due(code calling deals with errors)
                                     
                    // this byte array temporarily stores 4096 byte chunks of the file instead of loading the entire file into RAM, making it more memory-efficient
                    byte[] buffer = new byte[4096];
                   int bytesRead;//stores the number of bytes read from a file
                
                //repeatedly reads chunks of data  from file until done(done = read() returning -1 as this is EOF)
                 while ((bytesRead = fis.read(buffer))!= -1) { 
                     digest.update(buffer, 0, bytesRead);// since we are reading in chunks .update() is needed to process each chunk so .digest() can produce theSHA-256 hash 
                 }
            }
            byte[] hashBytes= digest.digest();//this digest method finishes the process to produce a hash value, its stored  in an array that will contain 32 bytes as this value is 256 bits
            StringBuilder hexString = new StringBuilder();

            for(byte b : hashBytes){
            hexString.append(String.format("%02x", b));//each current byte will be changed into a 2 character hexadecimal number and then will be added to our string builder. once finished = 64 character hexadecimal hash
                }
            return hexString.toString();//this returns our stringbuiulder as a string
            
        
       
    }
}







/* REFERENCES / LEARNING RESOURCES
 *
 * 1 : Max O Didily- youtube
 *  "how to select a file using java JFileChooser (simple)"
 *  https://www.youtube.com/watch?v=gMVkp8108f0
 *
 * 2 : tutorial plus - youtube
 *  "coding a message digest algorithms aka hashing algorithm in java: SHA-256, MD5 and MD2"
 *  https://www.youtube.com/watch?v=bv9bKaQu5CY&t=14s
 *
 * 3 : Daniel Smidstrup  - youtube
 *  "java - simple setup for hashing algorithms: MD5, SHA-1 and SHA-265"
 *  https://www.youtube.com/watch?v=ef3kenC4xa0&t=5s
 */

