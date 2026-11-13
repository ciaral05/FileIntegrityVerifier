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
import java.io.IOException;

/**
 *tests : unmodified, modified, not registered, and file is missing
 * @author amelia maxwell
 * x23471092
 */
public class FileVerifierTest {
    
    public FileVerifierTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    

 /**
 * testing that the app knows when that a file is unregistered
 */
    @Test
    public void testUnregisteredFile() throws Exception {
        System.out.println("testing unregistered file");
        //creates temp unregistered file
        File file = File.createTempFile("unregisteredTest", ".txt");
        //trying to verify it without saving a hash
        String result = FileVerifier.verifyFiles(file);
        assertEquals("not registered", result);//checks that it is unregistered
        file.delete();//dleetes file after test
    }


    /**
     * tests that verifyFiles() returns unmodified when a file hasnt been changed since the hash was saved
     */
    @Test
    public void testUnmodifiedFile() throws Exception {
        System.out.println("testing unmodified file");
        // creates temp txt file and adds text to it 
        File file = File.createTempFile("unmodifiedTest", ".txt");
        FileWriter writer = new FileWriter(file);
        writer.write("this testing is testing my will to live");
        writer.close();
        // calc files hash and saves it to our db
        String hash = FileIntegrityApp.calculateHash(file);
        hashStorage.saveHash(file.getAbsolutePath(), hash);
        // Verify the file without changing its contents
        String result = FileVerifier.verifyFiles(file);
        assertEquals("unmodified", result);//checks its = unmodified
        file.delete();
    }

    
    /**
     * tests that verifyFiles() returns modified when a file has been changed since the hash was saved
     */
    @Test
    public void testModifiedFile() throws Exception {
        System.out.println("testing modified file");
        // temp file with text
        File file = File.createTempFile("modifiedTest", ".txt");
        FileWriter writer = new FileWriter(file);
        writer.write("files original text");
        writer.close();
        // calc the hash and save to our db
        String hash = FileIntegrityApp.calculateHash(file);
        String saveResult = hashStorage.saveHash(file.getAbsolutePath(), hash);
        assertEquals("saved", saveResult);
        // then we changed the ocntents of the file
        writer = new FileWriter(file);
        writer.write("oops i modified the text");
        writer.close();
        //verifiy if its been modified and checks it = modified
        String result = FileVerifier.verifyFiles(file);
        assertEquals("modified", result);
        file.delete();
    }
    

    /**
     tests that verify file throws an exception when a file doesnt exists anymore ie its deleted 
     */
    @Test
    public void testMissingFile() throws Exception {
        System.out.println("testing missing file");
        // temporary file for testing then deleting it
        File file = File.createTempFile("missingTest", ".txt");
        file.delete();
        // check its deleted
        assertFalse(file.exists());
        //then we try to verify the missing file
        try {
            FileVerifier.verifyFiles(file);
            // this is a fail for if no exception was thrown 
            fail("Expected an IOException for a missing file");
        } catch (IOException e) {//test passes as expected exception was thrown
            
        }
    }

}
