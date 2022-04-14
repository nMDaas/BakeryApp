/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 
db.mysql.url="jdbc:mysql://localhost:3306/db?characterEncoding=UTF-8&useSSL=false"
*/
package javamysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author kath
 */
public class bakingapp {

  /** The name of the MySQL account to use (or empty for anonymous) */
  private final String userName = "root";

  /** The password for the MySQL account (or empty for anonymous) */
  private final String password = "password";

  /** The name of the computer running MySQL */
  private final String serverName = "localhost";

  /** The port of the MySQL server (default is 3306) */
  private final int portNumber = 3306;

  /**
   * The name of the database we are testing with (this default is installed with
   * MySQL)
   */
  private final String dbName = "bakingApp";

  /** The name of the table we are testing with */
  private final String tableName = "test";
  private final boolean useSSL = false;

  /**
   * Get a new database connection
   * 
   * @return
   * @throws SQLException
   */
  public Connection getConnection() throws SQLException {
    Connection conn = null;
    Properties connectionProps = new Properties();
    connectionProps.put("user", this.userName);
    connectionProps.put("password", this.password);

    conn = DriverManager.getConnection("jdbc:mysql://" + this.serverName + ":" + this.portNumber
        + "/" + this.dbName + "?characterEncoding=UTF-8", connectionProps);

    return conn;
  }

  /**
   * Run a SQL command which does not return a recordset:
   * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
   * 
   * @throws SQLException If something goes wrong
   */
  public boolean executeUpdate(Connection conn, String command) throws SQLException {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      stmt.executeUpdate(command); // This will throw a SQLException if it fails
      return true;
    }
    finally {

      // This will run whether we throw an exception or not
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  public String loggedIn(Connection conn) {
    String userName = null;
    String password = null;
    try {
      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("Enter username: ");
      String username = myObj.nextLine(); // Read user input
      System.out.println("Enter password: ");
      String userPassword = myObj.nextLine(); // Read user input
      
      Statement stmtLogIn = conn.createStatement();
      ResultSet rsLogIn = stmtLogIn.executeQuery("SELECT * FROM user WHERE username = \"" + username + "\" AND password = \"" + userPassword + "\";");
      
     
      
      if (rsLogIn.next()) {
        userName = rsLogIn.getString("username");
        password = rsLogIn.getString("password");
      }
      
      if (username.equals(userName) && userPassword.equals(password)) {
        userName = userName;
      }
      else {
        userName = null;
        System.out.println("ERROR: Incorrect username or password. Please re-enter your username and password.");
        //throw new SQLException();
      }


    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return userName;
  }

  public String makeAcc(Connection conn) {
    try {

      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("Enter a new username: ");
      String username = myObj.nextLine(); // Read user input
      System.out.println("Enter a new password: ");
      String userPassword = myObj.nextLine(); // Read user input

      
      String createUser = "INSERT INTO user(username, password) VALUES ('" + username + "', '"
          + userPassword + "');";

      this.executeUpdate(this.getConnection(), createUser);
      return username;
    }
    catch (SQLException e) {
      System.out.println(
          "ERROR: Unable to create new account. Make sure your username is unique, and please try again.");
      return null;
    }
  }
  
  public String directory() {
    try {
      System.out.println("Welcome to Bake It Till You Make It!");
      System.out.println("    To update your PROFILE                    : Type p");
      System.out.println("    To update your INVENTORY                  : Type i");
      System.out.println("    To check out RECOMMENDED RECIPES for you  : Type r");
      System.out.println("    To view your SAVED recipes                : Type s ");
      
      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("What would you like to do?");
      String doWhat = myObj.nextLine(); // Read user input
     
      return doWhat;
    }
    finally {}
  }
  
  public void Recommended(Connection conn, String username) throws SQLException {
      java.sql.CallableStatement cStmt = conn.prepareCall("{CALL viewRecommendedRecipes(?)}");
      cStmt.setString(1, username);
      ResultSet rsRecommended = cStmt.executeQuery();
      if (rsRecommended.next() == false) {
        System.out.println("These are ALL the available recipes. Update your dessert type preferences in your Profile to find recipes for YOU.");
        System.out.println("");
        java.sql.CallableStatement cStmtAll = conn.prepareCall("{CALL viewAllRecipes()}");
        ResultSet rs = cStmtAll.executeQuery();
        while (rs.next()) {
          String id = rs.getString("recipeId");
          String name = rs.getString("recipeName");
          String type = rs.getString("typeName");
          String writer = rs.getString("writer");
          
          System.out.println(id + ": " + name);
          System.out.println("        dessert type: " + type);
          System.out.println("        writer: " + writer);
          System.out.println("");
        }
      }
      while (rsRecommended.next()) {
        String id = rsRecommended.getString("recipeId");
        String name = rsRecommended.getString("recipeName");
        String type = rsRecommended.getString("typeName");
        String writer = rsRecommended.getString("writer");
        
        System.out.println(id + ": " + name);
        System.out.println("        dessert type: " + type);
        System.out.println("        writer: " + writer);
        System.out.println("");
      }
      
      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("Enter the # of a recipe to select it or type 'd' to go back to your directory");
      String rChoice = myObj.nextLine(); // Read user input
      
      if (rChoice.equals("d")) {
        this.directory();
      }

  }
  
  
  public void Saved(Connection conn, String username) throws SQLException {
    try {
    java.sql.CallableStatement cStmtSaved = conn.prepareCall("{CALL viewSavedRecipes(?)}");
    cStmtSaved.setString(1, username);
    ResultSet rsSaved = cStmtSaved.executeQuery();
//    System.out.println("query = " + rsSaved.next());
//    if (rsSaved.next() == false) {
//    }
      while (rsSaved.next() == false) {
      System.out.println("You have no saved recipes. Save recipes from Recommended Recipes.");
      }
    
      while (rsSaved.next()) {
        String id = rsSaved.getString("recipeId");
        String name = rsSaved.getString("recipeName");
        String type = rsSaved.getString("typeName");
        String writer = rsSaved.getString("writer");
        
        System.out.println(id + ": " + name);
        System.out.println("        dessert type: " + type);
        System.out.println("        writer: " + writer);
        System.out.println("");
      }
      
      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("Enter the # of a recipe to select it or type 'd' to go back to your directory");
      String rChoice = myObj.nextLine(); // Read user input
      
      if (rChoice.equals("d")) {
        this.directory();
      }
    
    }
    finally {}
    

}

  /**
   * Connect to MySQL and do some stuff.
   */
  public void run() {
    String username = null;
    // Connect to MySQL
    Connection conn = null;
    try {
      conn = this.getConnection();
      System.out.println("Connected to database");
    }
    catch (SQLException e) {
      System.out.println("ERROR: Could not connect to the database");
      e.printStackTrace();
      return;
    }

    try {
      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("Enter the number 1 if you have an account,or enter 2 if you want to make an account:");
      String logChoice = myObj.nextLine(); // Read user input
      int choice = Integer.parseInt(logChoice); 

      // here im making user input that will then go to an if statement with two trys one for loggin in and one for making an account,
      // then being redirected to login

      if (choice == 1) {
        while (username == null) {
          username = this.loggedIn(conn);
        }
        System.out.println("User logged in.");
      }
      
      if (choice == 2) {
        while (username == null) {
          username = this.makeAcc(conn);
        }
        System.out.println("User signed up.");
      }
    }
    finally {
      //System.out.println("User logged in.");
    }
    
    try {
      String doThis = this.directory();

      if (doThis.equals("r")) {
        this.Recommended(conn, username);
        }
      if (doThis.equals("s")) {
        this.Saved(conn, username);
        }
      }
    catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } 
    finally {
      //System.out.println("User logged in.");
    }
//    catch (SQLException e) {
//      System.out.println("ERROR: cannot create new account");
//      e.printStackTrace();
//    }



//    // Create a table
//    try {
//        String createString =
//              "CREATE TABLE " + this.tableName + " ( " +
//              "ID INTEGER NOT NULL, " +
//              "NAME varchar(40) NOT NULL, " +
//              "STREET varchar(40) NOT NULL, " +
//              "CITY varchar(20) NOT NULL, " +
//              "STATE char(2) NOT NULL, " +
//              "ZIP char(5), " +
//              "PRIMARY KEY (ID))";
//      this.executeUpdate(conn, createString);
//      System.out.println("Created a table");
//      } catch (SQLException e) {
//      System.out.println("ERROR: Could not create the table");
//      e.printStackTrace();
//      return;
//    }
//    
//    // Drop the table
//    try {
//        String dropString = "DROP TABLE " + this.tableName;
//      this.executeUpdate(conn, dropString);
//      System.out.println("Dropped the table");
//      } catch (SQLException e) {
//      System.out.println("ERROR: Could not drop the table");
//      e.printStackTrace();
//      return;
//    }


  }

  /**
   * Connect to the DB and do some stuff
   * 
   * @param args
   */
  public static void main(String[] args) {
    bakingapp app = new bakingapp();
    app.run();
  }
}
