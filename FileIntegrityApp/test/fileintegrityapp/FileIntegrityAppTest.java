/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package fileintegrityapp;

import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.FileWriter;


/**
 *
 * @author amelia maxwell
 * x23471092
 */
public class FileIntegrityAppTest {
    
    public FileIntegrityAppTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }


    /**
     * Test of calculateHash method, of class FileIntegrityApp.
     */
    @Test
        public void testCalculateHash() throws Exception {
        System.out.println("testing calculateHash()");
        File file = File.createTempFile("hashTest", ".txt");//here we are creating a tempory txt file
        //then giving the file some data
        FileWriter writer = new FileWriter(file);
        writer.write("Hello World");
        writer.close();
        // calling on our method to calculate the files SHA-256 hash
        String result = FileIntegrityApp.calculateHash(file);
        assertEquals(64, result.length());//checking its 64 char long as it should be 
        file.delete();//deletes file after the test
    }
    
        
        
/**
 * not a method but tests an unchanged file produces the same hash every time
 */
@Test
public void testHashConsistency() throws Exception {
    System.out.println("testing hash calc consistency");
    // creating the temporary file and write some text into it
    File file = File.createTempFile("hashTest", ".txt");
    FileWriter writer = new FileWriter(file);
    writer.write("Hello World");
    writer.close();
    // calculating the hash twice without changing the file
    String hash1 = FileIntegrityApp.calculateHash(file);
    String hash2 = FileIntegrityApp.calculateHash(file);
    assertEquals(hash1, hash2);//checks their the same
    file.delete();
}


/**
 * also not a method but to check a modified files hash changes
 */
@Test
public void testHashChanges() throws Exception {
    System.out.println("testing hash changes");
    //temp file
    File file = File.createTempFile("hashTest", ".txt");
    FileWriter writer = new FileWriter(file);
    writer.write("Hello World");
    writer.close();
    // calc OG hash
    String originalHash = FileIntegrityApp.calculateHash(file);
    //change the writing in the file 
    writer = new FileWriter(file);
    writer.write("Hello World!");
    writer.close();
    // calc the hash again and chceks their not the same
    String newHash = FileIntegrityApp.calculateHash(file);
    assertNotEquals(originalHash, newHash);
    file.delete();
}


}
