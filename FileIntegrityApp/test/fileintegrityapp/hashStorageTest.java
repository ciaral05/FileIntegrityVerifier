/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package fileintegrityapp;

import java.sql.Connection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;

/**
 * @author amelia maxwell
 * x23471092
 */
public class hashStorageTest {
    
    public hashStorageTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Tests that our app can connect to the sqlite database successfully 
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("Testing database connection");
        Connection result = hashStorage.connect();//calls on connect() to establish db connection
        assertNotNull(result);//checks a conection was returned and is not null
        assertFalse(result.isClosed());//checks connection is open
        result.close();//closes connection after test is complete
    }

    /**
     * Test of createTable method, of class hashStorage.
     */
    @Test
        public void testCreateTable() throws SQLException{
        System.out.println("testing database table creation");
        hashStorage.createTable();//calls our create method incase table doesnt exist
        try (Connection conn = hashStorage.connect();//connected to db so we can check if table exists
             java.sql.PreparedStatement stmt = conn.prepareStatement(
                 "select name from sqlite_master where type = 'table' and name = ?")) {
            stmt.setString(1, "file_hashes");//searching for the table named file hashes
            try (java.sql.ResultSet result = stmt.executeQuery()) {  // executes  or query and check whether a matching table was found
                assertTrue(result.next());
            }
        }
    }

    /**
     * Test of saveHash method, of class hashStorage.
     */
    @Test
    public void testSaveHash() throws SQLException{
        System.out.println("testing save hash");
        // the file path and hash used for this testing
        String filePath = "test_file.txt";
        String hash = "abc123";//sample hash instead of a real sha hash as we want to test this method in isolation 
        // deleting any previous test data for fresh test
        Connection conn = hashStorage.connect();//connect to db
        java.sql.PreparedStatement stmt = conn.prepareStatement(//sql to delete matching file
            "delete from file_hashes where file_path = ?");
        stmt.setString(1, filePath);
        stmt.executeUpdate();//run dlete statemnet
        stmt.close();
        conn.close();

        String result = hashStorage.saveHash(filePath, hash);//save hash to db
        assertEquals("saved", result);//checks hash was saved 
        String savedHash = hashStorage.loadHash(filePath);//retrieves hash we just saved so we can get it matches the saved hash
        assertEquals(hash, savedHash);
    }

    /**
     * Test of loadHash method, of class hashStorage.
     */
    @Test
    public void testLoadHash() throws SQLException{
        System.out.println("Testing loadHash");
        String filePath = "unregistered_test_" + System.nanoTime() + ".txt";// use a unique file path that has not been registered
        String result = hashStorage.loadHash(filePath);// attempts to retrieve the original hash for this file
        assertNull(result);// checks that null is returned because no hash was saved
    }
    
    
/**
 * We dont have a duplicate hash method in our hash storage but our our savehash method deals with duplications and we wanted to test that
 */
@Test
public void testDuplicateHash() throws SQLException {
    System.out.println("Testing duplicate registration");
    String filePath = "duplicate_test_file.txt";//a test file path and original hash
    String hash = "abc123";
    // removs  any previous test record
    Connection conn = hashStorage.connect();
    java.sql.PreparedStatement stmt = conn.prepareStatement(
        "delete from file_hashes where file_path = ?");
    stmt.setString(1, filePath);
    stmt.executeUpdate();
    stmt.close();
    conn.close();

    hashStorage.saveHash(filePath, hash);//save 1st time
    String result = hashStorage.saveHash(filePath, "xyz789");//try saving the same again
    assertEquals("already registered", result);//checks it was rejected
    assertEquals(hash, hashStorage.loadHash(filePath));//then finally we check that our original hash wasnt overwritten
}
}