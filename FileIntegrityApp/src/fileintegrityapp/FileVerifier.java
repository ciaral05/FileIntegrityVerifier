package fileintegrityapp;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 *This class compares the original hash from the db with the current 
 * SHA-256 hash  to see whether a file has been modified
 * 
 * Amelia maxwell
 * x23471092
 */

public class FileVerifier {
    
    
    public static String verifyFiles(File file) throws IOException, NoSuchAlgorithmException, SQLException{
       //first we check file exists and can be read
       if(!file.exists()){
           throw new IOException("the selected file no longer exists");
       }
       if(!file.isFile()){ //eg checks path points to file not a folder
           throw new IOException ("the path selected is not a valid file");
       }
       if(!file.canRead()){//checks that the program can read the file eg it doesnt have permission
           throw new IOException("the selected file cannot be read");
       }
       
        //retrives orginal hash from sqlite
        String OgHash = hashStorage.loadHash(file.getAbsolutePath());  //tgetabsoluetpath() gives loadhash() filepath to get hash
            if(OgHash == null){
                return "not registered";
            }
            
         //calcs current hash
         String currentHash = FileIntegrityApp.calculateHash(file);//calcuate files current hash
         
         //compares currenthash to original hash
         if(OgHash.equals(currentHash)){
             return "unmodified";
         }else{
              return "modified";
          }
      }

    
    
    }
    

