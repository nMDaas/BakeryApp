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

  public boolean loggedIn(Connection conn) {
    boolean repeat = false;
    try {
      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("Enter username: ");
      String username = myObj.nextLine(); // Read user input
      System.out.println("Enter password: ");
      String userPassword = myObj.nextLine(); // Read user input
      
      Statement stmtLogIn = conn.createStatement();
      ResultSet rsLogIn = stmtLogIn.executeQuery("SELECT * FROM user WHERE username = \"" + username + "\" AND password = \"" + userPassword + "\";");
      
      String user = null;
      String password = null;
      
      if (rsLogIn.next()) {
        user = rsLogIn.getString("username");
        password = rsLogIn.getString("password");
      }
      
      if (username.equals(user) && userPassword.equals(password)) {
        repeat = false;
      }
      else {
        repeat = true;
        System.out.println("ERROR: Incorrect username or password. Please re-enter your username and password.");
        //throw new SQLException();
      }


    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return repeat;
  }

  public Boolean makeAcc(Boolean exists, Connection conn) {
    try {

      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("Enter a new username: ");
      String username = myObj.nextLine(); // Read user input
      System.out.println("Enter a new password: ");
      String userPassword = myObj.nextLine(); // Read user input

      
      String createUser = "INSERT INTO user(username, password) VALUES ('" + username + "', '"
          + userPassword + "');";

      this.executeUpdate(this.getConnection(), createUser);
      return false;
    }
    catch (SQLException e) {
      System.out.println(
          "ERROR: Unable to create new account. Make sure your username is unique, and please try again.");
      return true;
    }
  }

  /**
   * Connect to MySQL and do some stuff.
   */
  public void run() {

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
        Boolean repeat = true;
        while (repeat) {
          repeat = this.loggedIn(conn);
        }
        System.out.println("User logged in.");
      }
      
      if (choice == 2) {
        Boolean exists = true;
        while (exists) {
          exists = this.makeAcc(exists, conn);
        }
        System.out.println("User signed up.");
      }
    }
    finally {
      System.out.println("User logged in.");
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
