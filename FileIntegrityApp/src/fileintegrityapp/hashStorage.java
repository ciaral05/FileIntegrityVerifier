/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fileintegrityapp;

/**
 *x23471092
 * @author amelia
 * this class handles our database operations such as save retrieve create etc
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class hashStorage {
    private static final String db_url = "jdbc:sqlite:file_hashes.db"; // a constant storing the location of our sqlite database, its created automatically when we connect if it doesn't already exist

    
    //CONNECT() : connect to database, and returns connection other methods can use
    public static Connection connect() throws SQLException{
        return DriverManager.getConnection(db_url);//find jdbc driver and establishes connection 
    }
    
    
    
    public static void createTable(){
        String sql ="""
                    create table if not exists file_hashes(
                    id integer primary key autoincrement,
                    file_path text not null unique,
                    original_hash text not null,
                    created_at text default current_timestamp
                                )
              """;
        Connection con = null;//delaring database variables(con to hold database connection, stated to hold sql statement for java to send to sql lite)
        Statement stated = null;
        try{
            con = connect();//calling on method to establish connection to db storing it on object con 
            stated= con.createStatement();
            stated.execute(sql);
            System.out.println("table sucessfully built");
        }catch(SQLException e){
            System.out.println("database failure"+ e.getMessage());
        }finally{
            try{//closing connection
                if(stated !=null){
                    stated.close();
                }
                if(con != null){
                    con.close();
                }
            }catch(SQLException e){
                System.out.println("error closing the database"+ e.getMessage());
            }
        }
    }
    
    
     //SAVEHASH() : this method saves a files original hash and filepath to db, it returns string to show if it was saved, already registered, or an error happened
    public static String saveHash(String filePath, String Hash){
        String sql = """ 
                 insert into file_hashes (file_path, original_hash) values(?,?)
               """;        
        Connection con = null;
        PreparedStatement stated = null;//prepared statement : represent our sql statements for excecution 
         try{     
              // Check if this file already has a saved hash
            String existingHash = loadHash(filePath);
            if (existingHash != null) {
                return "already registered";
            }
            
            con = connect();
            stated = con.prepareStatement(sql);
            stated.setString(1,filePath);
            stated.setString(2, Hash);
            int rows = stated.executeUpdate();//executeUpdate() instead of execute as we want the number of insertions given bk to us instead of a boolean
            if(rows > 0){//checks if a row got succesfully inserted
                return "saved";
            }
            return "error";
          }
         catch(SQLException e){
                System.out.println("error saving hash"+ e.getMessage());
                return "error";
        }finally{ 
            try{
                if(stated != null){
                    stated.close();
                }
                if (con != null){
                    con.close();
                }
                }catch(SQLException e){
                        System.out.println("error closing database"+ e.getMessage());
                        }
            }
        }
    
    
    
    //LOADHASH() : retrives a files original hash from db using its file path
    public static String loadHash(String filePath) throws SQLException{
        String sql ="select original_hash from file_hashes where file_path = ?";
        Connection con = null;
        PreparedStatement stated = null;
        ResultSet results = null;  //result set type lets us store rows returned from sql query so we can acces their saved hashes
        try{
            con = connect();
            stated = con.prepareStatement(sql);
            stated.setString(1, filePath);//the ? is 1st/only placeholder for filepath in the sql search, we fill it with 1, filepath
            results = stated.executeQuery();
                if (results.next()){ //we use .next() to move curser to first result (think slot machine)
                    return results.getString("original_hash");
                    //"return original hash as string from results "
                }
                
                return null;
                
        }catch (SQLException e){
            System.out.println("error when retrieving hash"+ e.getMessage());
            throw e;
        }finally{
            try{             
                if (results != null){
                    results.close();
                }
                if(stated !=null){
                    stated.close();
                }
                if(con != null){
                    con.close();
                }

            }catch(SQLException e){
                System.out.println("error closing db" + e.getMessage());
            }
        }  
    }
        
}



/*REFERENCES /LEARNING RESOURCES 

1: JobME Tech Solutions- youtube
    "JAVA SQLite Database | Create Database, Table, List and Insert Data in SQLite using JAVA in Netbean"
    https://www.youtube.com/watch?v=OFb6muySmxs

2: Logic Lambda - youtube
    "Working with Databases in Java: JDBC & SQLite"
    https://www.youtube.com/watch?v=0beocykXUag
*/
