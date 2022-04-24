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
      ResultSet rsLogIn = stmtLogIn.executeQuery("SELECT * FROM user WHERE username = \"" + username
          + "\" AND password = \"" + userPassword + "\";");

      if (rsLogIn.next()) {
        userName = rsLogIn.getString("username");
        password = rsLogIn.getString("password");
      }

      if (username.equals(userName) && userPassword.equals(password)) {
        userName = userName;
      }
      else {
        userName = null;
        System.out.println(
            "ERROR: Incorrect username or password. Please re-enter your username and password.");
        // throw new SQLException();
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
  
  public void updateBakery(Connection conn, String username, boolean check) throws SQLException {
    while (check == false) {
    try {
      
      java.sql.CallableStatement cStmtBakeryInfo = conn.prepareCall("{CALL aBakery(?)}");
      cStmtBakeryInfo.setString(1, username);
      ResultSet reBakery = cStmtBakeryInfo.executeQuery();
      
      while (reBakery.next()) {
        String phone = reBakery.getString("phoneNo");
        String address = reBakery.getString("address");
        
        System.out.println("Phone No:   " + phone + "(Type p to update)");
        System.out.println("Address:    " + address + "(Type a to update)");
      }
      
      System.out.println("Type b to go back");
      
      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      String doWhat = myObj.nextLine(); // Read user input
      
      if (doWhat.equals("p")) {
        System.out.println("Enter phone number:");
        String phone = myObj.nextLine(); // Read user input
        java.sql.CallableStatement cStmtUpdatePhone = conn.prepareCall("{CALL updatePhone(?, ?)}");
        cStmtUpdatePhone.setString(1, username);
        cStmtUpdatePhone.setInt(2, Integer.parseInt(phone));
        ResultSet reUpdatePhone = cStmtUpdatePhone.executeQuery();
        System.out.println("Phone number updated!");
        this.bakery(conn, username, true, false);
        check = true;
      }
      else if (doWhat.equals("a") ) {
        System.out.println("Enter your address:");
        String address = myObj.nextLine(); // Read user input
        java.sql.CallableStatement cStmtUpdateAddress = conn.prepareCall("{CALL updateAddress(?, ?)}");
        cStmtUpdateAddress.setString(1, username);
        cStmtUpdateAddress.setString(2, address);
        ResultSet reUpdateAddress = cStmtUpdateAddress.executeQuery();
        System.out.println("Address updated!");
        this.bakery(conn, username, true, false);
        check = true;
      }
      else if (doWhat.equals("b")) {
        this.bakery(conn, username, true, false);
        check = true;
      }
      
      else if (check == false) {
        System.out.println(".");
        this.updateBakery(conn, username, check);
      }
    }
    finally {}
    }
  }
  
  public void bakery(Connection conn, String username, boolean bakery, boolean check) throws SQLException {
    try {
      if (bakery) {
        System.out.println("Switch to personal account - Type 's'");
        System.out.println("Update bakery details - Type 'a'");
      }
      else {
        System.out.println("Switch to a business account - Type 's'");
      }
      System.out.println("Type 'b' to back to your account");

      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      String doWhat = myObj.nextLine(); // Read user input
      
      if (doWhat.equals("s") && bakery) {
        String deleteBakery = "DELETE FROM bakery WHERE bakery.bakeryId = \"" + username + ";";
        this.executeUpdate(conn, deleteBakery);
        this.account(conn, username, false);
        check = true;

      }
      else if (doWhat.equals("s")  && !bakery) {
        try {
          String createString = "INSERT INTO bakery(bakeryId, phoneNo, address) VALUES (\"" + username + "\", NULL, NULL)";
          this.executeUpdate(conn, createString);
          this.updateBakery(conn, username, false);
         // System.out.println("Type 'b' to back to your account");
          String doNext = myObj.nextLine(); // Read user input
          check = true;
        }
        catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      else if (doWhat.equals("b")) {
        try {
          this.account(conn, username, false);
          check = true;
        }
        catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      else if (doWhat.equals("a")) {
        try {
          this.updateBakery(conn, username, false);
          check = true;
        }
        catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      else if (check == false) {
        System.out.println("Invalid input. Please enter again.");
        this.bakery(conn, username, bakery, check);
      }
    }
    finally {}
  }
  
  public void account(Connection conn, String username, boolean check) throws SQLException {
    try {
      System.out.println("username: @" + username);
      System.out.println("");
      
      java.sql.CallableStatement cStmt = conn.prepareCall("{CALL aBakery(?)}");
      cStmt.setString(1, username);
      ResultSet reBakery = cStmt.executeQuery();
      boolean bakery = false;
      if (reBakery.next()) {
        bakery = true;
        System.out.println("Bakery   - Type i to change account type to be individually owned");
      }
      else {
        System.out.println("Not A Bakery   - Type i to change account type to be bakery owned");
      }
      
      System.out.println("");
      System.out.println("Type 'b' to back to your profile");

      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("What would you like to do?");
      String doWhat = myObj.nextLine(); // Read user input
      
      if (doWhat.equals("b")) {
        this.profile(conn, username, false);
        check = true;
      }
      else if (doWhat.equals("i")) {
        this.bakery(conn, username, bakery, false);
        check = true;
      }
      else if (check == false) {
        this.account(conn, username, check);
      }
    }
    finally {}
  }
  
  public void dessertTypes(Connection conn, String username) throws SQLException {
    try {
      java.sql.CallableStatement cStmt = conn.prepareCall("{CALL userTypes(?)}");
      cStmt.setString(1, username);
      ResultSet reTypes = cStmt.executeQuery();
      System.out.println("Your Dessert Types: ");
      while (reTypes.next()) {
        String type = reTypes.getString("typeName");
        System.out.println(type);
      }
      System.out.println("");
      System.out.println("Available dessert types: ");
      
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM dessertType;");
      while (rs.next()) {
        String type = rs.getString("typeName");
        System.out.println(type);
      }
      
      System.out.println("Type a dessert type to add or remove it from your dessert types.");
      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      String type = myObj.nextLine(); // Read user input
      
      java.sql.CallableStatement cStmtAType = conn.prepareCall("{CALL aUserType(?, ?)}");
      cStmtAType.setString(1, username);
      cStmtAType.setString(2, type);
      ResultSet reAType = cStmtAType.executeQuery();
      if (reAType.next()) {
        int typeId = reAType.getInt("dessertType");
        String removeType = "DELETE FROM userDessertType WHERE userDessertType.user = \"" + username + "\" AND userDessertType.dessertType = " + typeId + ";";
        this.executeUpdate(conn, removeType);
        this.dessertTypes(conn, username);
      }
      else {
        Statement stmtId = conn.createStatement();
        ResultSet rsId = stmtId.executeQuery("SELECT dessertType.typeId FROM dessertType WHERE dessertType.typeName = \"" + type + "\";");
        while (rsId.next()) {
          int id = rsId.getInt("typeId");
          String addType = "INSERT INTO userDessertType(dessertType, user) VALUES (" + id + ", \"" + username + "\");";
          this.executeUpdate(conn, addType);
          this.dessertTypes(conn, username);
        }
        
        
      }
    }
    finally {}
  }
  
  public void inventory(Connection conn, String username) throws SQLException {
    try {
      
      java.sql.CallableStatement cStmt = conn.prepareCall("{CALL getInventory(?)}");
      cStmt.setString(1, username);
      ResultSet reTypes = cStmt.executeQuery();
      
      while(reTypes.next()){
        String ingredient = reTypes.getString("ingredientName");
        double amount = reTypes.getDouble("amount");
        String units = reTypes.getString("units");
        System.out.println(ingredient + "          :" + amount + ", " + units);
      }
      
      System.out.println("Press p to go back to profile or enter the name of an ingredient to update its amount."); // ---------- add option for adding new ingredient

      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("What would you like to do?");
      String doWhat = myObj.nextLine(); // Read user input

      if (doWhat.equals("p")) {
        this.profile(conn, username, false);
      }
      
      
      else {
      boolean ingredientExists = false;
      
      java.sql.CallableStatement cStmt3 = conn.prepareCall("{CALL getInventory(?)}");
      cStmt3.setString(1, username);
      ResultSet reTypes2 = cStmt3.executeQuery();
      while(reTypes2.next()) {
        String ingredientName = reTypes2.getString("ingredientName");
        if (doWhat.equals(ingredientName)) {
          ingredientExists = true;
        }
      }
      
      if(ingredientExists) {
        
      java.sql.CallableStatement cStmt2 = conn.prepareCall("{CALL updateInventory(?, ?, ?)}");
      cStmt2.setString(1, username);
      cStmt2.setString(2, doWhat);
      System.out.println("Enter your updated amount of " + doWhat + ".");
      String newAmount = myObj.nextLine();
      cStmt2.setString(3, newAmount);
      
      ResultSet update = cStmt2.executeQuery();
      
      System.out.println("Ingredient updated!");
      this.inventory(conn, username);
      
      
      } else {
        System.out.println("Ingredient entered does not exist, please try again!");
        this.inventory(conn,  username);
      }
      }

    }
    finally {}
      
  }
  public boolean writeIngredient(int recipeId, Connection conn, String username) throws SQLException {
    try {
      
      
      System.out.println("");
      System.out.println("Available ingredients: ");
      
      
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Ingredient;");
      while (rs.next()) {
        String ingredientId = rs.getString("ingredientId");
        String ingredientName = rs.getString("ingredientName");
        String units = rs.getString("units");
        System.out.println(ingredientName + ", " + units+ ":         " + ingredientId);
      }
      
      System.out.println("Enter an available ingredient id: ");
      Scanner myObj = new Scanner(System.in);
      String ingredientId = myObj.nextLine(); // Read user inputrofile(conn, username);
      //myObj.close();

      System.out.println("Enter your ingredient amount: ");
      Scanner myObj2 = new Scanner(System.in);
      String amount = myObj2.nextLine(); // Read user inputrofile(conn, username);
      //myObj2.close();
      
      Statement stmt5 = conn.createStatement();
      stmt5.executeUpdate("INSERT INTO recipeIngredient(recipe, ingredient, amount) VALUES (" 
          + recipeId + ", \"" + ingredientId + "\", " + amount + ");");
      
      
      Scanner myObj3 = new Scanner(System.in); // Create a Scanner object
      System.out.println("Press w to finish entering ingredients or press i to continue entering ingredients.");
      String doWhat = myObj3.nextLine(); // Read user input
      // myObj3.close();
      
      if (doWhat.equals("w")) {
        return false;
      }
      else if (doWhat.equals("i")) {
        return true;
      } 
      else {
        return true;
      }
      
      
    }
    finally {}
  }
  
  // recipeId, recipeName, instructions, type, writer (Recipe)
  public void edit(int recipeId, Connection conn, String username, boolean check) throws SQLException {
    try {
      System.out.println("Update Recipe Name             : Type n");
      System.out.println("Update Recipe Instructions     : Type i");
      System.out.println("Go back to viewing recipes     : Type v");
      System.out.println("Press p to return to Profile or v to view recipes again");
      System.out.println("What would you like to do?");
      Scanner myObj5 = new Scanner(System.in);
      String doWhat = myObj5.nextLine();
      
      if (doWhat.equals("n")) {
        Scanner myObj = new Scanner(System.in); // Create a Scanner object              
        System.out.println("Enter your recipe's new name: ");
        String name = myObj.nextLine(); // Read user inputrofile(conn, username);
        String updateName = "UPDATE Recipe SET recipeName = \"" + name + "\" WHERE recipeId = " + recipeId + ";";
        this.executeUpdate(conn, updateName);
        System.out.println("Recipe name updated!");
        check = true;
        this.edit(recipeId, conn, username, false);
        
      }
      else if(doWhat.equals("i")){
        Scanner myObj2 = new Scanner(System.in); // Create a Scanner object
        System.out.println("Enter your recipe's new instructions: ");
        String instructions = myObj2.nextLine(); // Read user inputrofile(conn, username);
        String updateInstructions = "UPDATE Recipe SET instructions = \"" + instructions + "\" WHERE recipeId = " + recipeId + ";";
        this.executeUpdate(conn, updateInstructions);
        System.out.println("Recipe instructions updated!");
        check = true;
        this.edit(recipeId, conn, username, false);
      }
      else if(doWhat.equals("v")){
        check = true;
        this.viewRecipes(conn, username);
      }
      
      else if (doWhat.equals("p")) {
        check = true;
        this.profile(conn, username, false);
      }
      else if(doWhat.equals("v")) {
        check = true;
        this.viewRecipes(conn, username);
      }
      else if (check == false) {
        System.out.println("Invalid input. Please enter again.");
        this.edit(recipeId, conn, username, check);
      }
      
    }
    finally {} 
  }
  
  public void editRecipe(int recipeId, Connection conn, String username, boolean check) throws SQLException {
    try {
      System.out.println("Press e to edit your recipe or press d to delete your recipe.");
      System.out.println("Press p to return to Profile or v to view recipes again");
      Scanner myObj5 = new Scanner(System.in);
      String doWhat = myObj5.nextLine();
      
      if (doWhat.equals("e")) {
        this.edit(recipeId, conn, username, false);
        check = true;
      } else if(doWhat.equals("d")){
        String deleteWrittenRecipe = "DELETE FROM writtenRecipe WHERE writtenRecipe.recipe = " + recipeId + ";";
        this.executeUpdate(conn, deleteWrittenRecipe);
        String deleteRecipeIngredient = "DELETE FROM recipeIngredient WHERE recipeIngredient.recipe = " + recipeId + ";";
        this.executeUpdate(conn, deleteRecipeIngredient);
        String deleteRecipe = "DELETE FROM Recipe WHERE Recipe.recipeId = " + recipeId + ";";
        this.executeUpdate(conn, deleteRecipe);
        System.out.println("Recipe deleted!");
        this.viewRecipes(conn, username);
        check = true;
      }
      
      else if (doWhat.equals("p")) {
        this.profile(conn, username, false);
        check = true;
      }
      else if(doWhat.equals("v")) {
        this.viewRecipes(conn, username);
        check = true;
      }
      else if (check == false) {
        System.out.println("Invalid input. Please enter again.");
        this.editRecipe(recipeId, conn, username, check);
      }
      
    }
    finally {} 
  }
  
  public void viewRecipes(Connection conn, String username) throws SQLException {
    try {
      System.out.println("Here are your written recipes: ");
      System.out.println("");
      
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Recipe HAVING writer = \"" + username + "\";");
      while (rs.next()) {
        String instructions = rs.getString("instructions");
        String RecipeName = rs.getString("recipeName");
        String RecipeId = rs.getString("recipeId");
        System.out.println(RecipeName + ", " + RecipeId + ": ");
        System.out.println(instructions);
        System.out.println("");
      }
    
      System.out.println("Press p to return to profile or enter a recipe ID to edit that recipe.");
      Scanner myObj5 = new Scanner(System.in);
      String doWhat = myObj5.nextLine();
      
      if (doWhat.equals("p")) {
        this.profile(conn, username, false);
        
      } else {
        this.editRecipe(Integer.parseInt(doWhat), conn, username, false);
      }
    }
    finally {}
      
  }
  
  
  // recipeId, recipeName, instructions, type, writer (Recipe)
  // recipe, writer, dateWritten (writtenRecipe)
  // recipe, ingredient, amount (recipeIngredient)
  public void write(Connection conn, String username, boolean check) throws SQLException {
    try {
   
      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("Name of recipe: ");
      String recipeName = myObj.nextLine(); // Read user inputrofile(conn, username);
     
      
      System.out.println("");
      
      Scanner myObj2 = new Scanner(System.in); // Create a Scanner object
      System.out.println("Enter your recipe's instructions: ");
      String Instructions = myObj2.nextLine(); // Read user inputrofile(conn, username);
      //myObj2.close();
      
      System.out.println("");
      System.out.println("Available dessert types: ");
      
      
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM dessertType;");
      while (rs.next()) {
        String type = rs.getString("typeName");
        String typeId = rs.getString("typeId");
        System.out.println(type + ":         " + typeId);
      }
      
      System.out.println("Enter one of the available dessert type IDs: ");
      Scanner myObj3 = new Scanner(System.in);
      String dessertType = myObj3.nextLine(); // Read user inputrofile(conn, username);
      //myObj3.close();
      
      System.out.println("Enter date in YYYY-MM-DD format: ");
      Scanner myObj4 = new Scanner(System.in);
      String date = myObj4.nextLine(); // Read user inputrofile(conn, username);
      //myObj4.close();
      
     
      Statement stmt2 = conn.createStatement();
      ResultSet rs2 = stmt2.executeQuery("SELECT COUNT(*) FROM Recipe;");
      int newRecipeId = 0;
      while(rs2.next()){
      newRecipeId = rs2.getInt("COUNT(*)") + 1;
      }
      
      Statement stmt3 = conn.createStatement();
      stmt3.executeUpdate("INSERT INTO Recipe(recipeId, recipeName, instructions, type, writer) VALUES (" 
          + newRecipeId + ", \"" + recipeName + "\", \"" + Instructions + "\", " + dessertType + ", \"" + username + "\");");
      
      
      Statement stmt4 = conn.createStatement();
      stmt4.executeUpdate("INSERT INTO writtenRecipe(recipe, writer, dateWritten) VALUES (" 
          + newRecipeId + ", \"" + username + "\", \"" + date + "\");");
      
      boolean repeat = true;
      
      while(repeat) {
        repeat = this.writeIngredient(newRecipeId, conn, username);
      }
      
      System.out.println("All ingredients added and recipe entered! Press p to return to Profile.");
      Scanner myObj5 = new Scanner(System.in);
      String doWhat = myObj5.nextLine();
      
      if (doWhat.equals("p")) {
        this.profile(conn, username, false);
        check = true;
      }
      else if (check = false) {
        System.out.println("invalid input. Please enter again.");
        this.write(conn, username, check);
      }
    }
    finally {}
      
  }
  
  public void writeView(Connection conn, String username, boolean check) throws SQLException {
    try {
      System.out.println("Write a New Recipe                    : Type w");
      System.out.println("View Written Recipes                  : Type v");
      System.out.println("Go Back to Profile                    : Type p");

      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("What would you like to do?");
      String doWhat = myObj.nextLine(); // Read user input
      
      
      if (doWhat.equals("w")) {
        this.write(conn, username, false);
        check = true;
      }
      else if (doWhat.equals("v")) {
        this.viewRecipes(conn, username);
        check = true;
      }
      else if (doWhat.equals("p")) {
        this.profile(conn, username, false);
        check = true;
      }
      else if (check == false) {
        System.out.println("invalid input. Please enter again.");
        this.writeView(conn, username, check);
      }
    }
    finally {}
      
  }
  
  
  public void profile(Connection conn, String username, boolean check) throws SQLException {
    try {
      System.out.println("Update Account                        : Type a");
      System.out.println("Update Dessert Types                  : Type d");
      System.out.println("write a Recipe/view Recipes by you    : Type r");
      System.out.println("View Inventory                        : Type i");
      System.out.println("Back to Home                          : Type h");

      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("What would you like to do?");
      String doWhat = myObj.nextLine(); // Read user input

      if (doWhat.equals("a")) {
        this.account(conn, username, false);
        check = true;
      }
      else if (doWhat.equals("d")) {
        this.dessertTypes(conn, username);
        check = true;
      }
      else if (doWhat.equals("i")) {
        this.inventory(conn, username);
        check = true;
      }
      else if (doWhat.equals("r")) {
        this.writeView(conn, username, false);
        check = true;
      }
      else if (check == false) {
        System.out.println("invalid input. Please enter again.");
        this.profile(conn, username, check);
      }
    }
    finally {}
      
  }

  public void home(Connection conn, String username, boolean check) throws SQLException {
    try {
      System.out.println("Welcome to Bake It Till You Make It!");
      System.out.println("    View your PROFILE                        : Type p");
      System.out.println("    View RECOMMENDED RECIPES                 : Type r");
      System.out.println("    View your SAVED recipes                  : Type s ");
      System.out.println("    Log Out                                  : Type o ");


      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println("What would you like to do?");
      String doWhat = myObj.nextLine(); // Read user input

      if (doWhat.equals("p")) {
        this.profile(conn, username, false);
        check = true;
      }
      if (doWhat.equals("r")) {
        try {
          this.Recommended(conn, username);
          check = true;
        }
        catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      if (doWhat.equals("s")) {
        try {
          this.Saved(conn, username);
          check = true;
        }
        catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      if (doWhat.equals("o")) {
        try {
          this.login(null, conn);
          check = true;
        }
        catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      else if (check == false) {
        System.out.println("invalid input. Please enter again.");
        this.home(conn, username, check);
      }
    }
    finally {
    }
  }
  
  public void savedRecipe(Connection conn, int num, String user, boolean check) {
    try {
      java.sql.CallableStatement cStmt = conn.prepareCall("{CALL viewRecipe(?)}");
      cStmt.setInt(1, num);
      ResultSet reRecipe = cStmt.executeQuery();
      while (reRecipe.next()) {
        String name = reRecipe.getString("recipeName");
        String instructions = reRecipe.getString("instructions");
        
        System.out.println(name);
        System.out.println("");
        System.out.println(instructions);
        System.out.println("");
        System.out.println("");
        
        java.sql.CallableStatement cStmtIngredReq = conn.prepareCall("{CALL missingIngredientAmounts(?, ?)}");
        cStmtIngredReq.setString(1, user);
        cStmtIngredReq.setInt(2, num);
        ResultSet reIngredReq = cStmtIngredReq.executeQuery();
        System.out.println("Ingredients required: ");
        while (reIngredReq.next()) {
          String ingredient = reIngredReq.getString("ingredientName");
          String amtRequired = reIngredReq.getString("amtOfIngredientRequired");
          String units = reIngredReq.getString("units");
          
          if (amtRequired == null) {
            amtRequired = "We don't know how much " + ingredient + " you have! Update your inventory!";
          }
          
          System.out.println("        " + ingredient + ": " + amtRequired + " " + units);
        }
        
        Scanner myObj = new Scanner(System.in); // Create a Scanner object
        System.out
            .println("Type 'u' if you would like to unsave this recipe. Type 'b' to go back to see your saved recipes.");
        String rChoice = myObj.nextLine(); // Read user input
        
        if (rChoice.equals("b")) {
          this.Saved(conn, user);
          check = true;
        }
        else if (rChoice.equals("u")) {
          String unsaveRecipe = "DELETE FROM saves WHERE saves.user = \"" + user + "\" AND saves.recipe = " + num + ";";
          this.executeUpdate(conn, unsaveRecipe);
          System.out.println("Recipe unsaved");
          System.out.println("");
          this.Saved(conn, user);
          check = true;
        }
        else if (check == false) {
          System.out.println("invalid input. Please enter again.");
          this.savedRecipe(conn, num, user, check);
        }
        
      }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    finally {
    }
    
    
  }
  
  public void aRecipe(Connection conn, int num, String user) {
    try {
      java.sql.CallableStatement cStmt = conn.prepareCall("{CALL viewRecipe(?)}");
      cStmt.setInt(1, num);
      ResultSet reRecipe = cStmt.executeQuery();
      while (reRecipe.next()) {
        String name = reRecipe.getString("recipeName");
        String instructions = reRecipe.getString("instructions");
        
        System.out.println(name);
        System.out.println("");
        System.out.println(instructions);
      }
      
      java.sql.CallableStatement cStmtSaved = conn.prepareCall("{CALL aSavedRecipe(?, ?)}");
      cStmtSaved.setInt(1, num);
      cStmtSaved.setString(2, user);
      ResultSet savedRecipe = cStmtSaved.executeQuery();
      if (savedRecipe.next()) {
      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out
          .println("This recipe is saved! Type 'b' to go back to browse recipes.");
      String rChoice = myObj.nextLine(); // Read user input
      
      if (rChoice.equals("b")) {
        this.Recommended(conn, user);
      }
      }
      else {
        Scanner myObj = new Scanner(System.in); // Create a Scanner object
        System.out
            .println("Type 's' to save this recipe or type 'b' to go back to browse recipes.");
        String rChoice = myObj.nextLine(); // Read user input
        
        if (rChoice.equals("b")) {
          this.Recommended(conn, user);
        }
        if (rChoice.equals("s"))  {
          java.sql.CallableStatement cStmtInsertRecipe = conn.prepareCall("{CALL saveRecipe(?, ?)}");
          cStmtInsertRecipe.setInt(1, num);
          cStmtInsertRecipe.setString(2, user);
          ResultSet saveRecipe = cStmtInsertRecipe.executeQuery();
          System.out.println("");
          System.out
              .println("Recipe saved! Type 'b' to go back to browse recipes.");
          String rNextChoice = myObj.nextLine(); // Read user input
          
          if (rNextChoice.equals("b")) {
            this.Recommended(conn, user);
          }
          
        }
      
    }
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    finally {
    }
  }

  public void Recommended(Connection conn, String username) throws SQLException {
    java.sql.CallableStatement cStmt = conn.prepareCall("{CALL viewRecommendedRecipes(?)}");
    cStmt.setString(1, username);
    ResultSet rsRecommended = cStmt.executeQuery();
    if (rsRecommended.next() == false) {
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
    System.out.println(
        "These are ALL the available recipes. Update your dessert type preferences in your Profile to find recipes for YOU.");
    System.out.println("");
    System.out
        .println("Enter the # of a recipe to view it or type 'h' to go back to the Home page");
    String rChoice = myObj.nextLine(); // Read user input

    if (rChoice.equals("h")) {
      this.home(conn, username, false);
    }
    else {
      this.aRecipe(conn, Integer.parseInt(rChoice), username);
    }

  }

  public void Saved(Connection conn, String username) throws SQLException {
    java.sql.CallableStatement cStmtSaved = conn.prepareCall("{CALL viewSavedRecipes(?)}");
    cStmtSaved.setString(1, username);
    ResultSet rsSaved = cStmtSaved.executeQuery();

    try {
      int count = 0;
      while (rsSaved.next()) {
        count ++;
        String id = rsSaved.getString("recipeId");
        String name = rsSaved.getString("recipeName");
        String type = rsSaved.getString("typeName");
        String writer = rsSaved.getString("writer");
        System.out.println(id + ": " + name);
        System.out.println("        dessert type: " + type);
        System.out.println("        writer: " + writer);
        System.out.println("");
      }
      
      if (count > 0) {
        Scanner myObj = new Scanner(System.in); // Create a Scanner object
        System.out.println("Type 'h' to go back to the Home page or type the recipe number to view recipe.");
        String rChoice = myObj.nextLine(); // Read user input
        
        if (rChoice.equals("h")) {
          this.home(conn, username, false);
        }
        else {
          this.savedRecipe(conn, Integer.parseInt(rChoice), username, false);
        }
      }

      if (count == 0) {
        System.out.println("You have no saved recipes. Save recipes from Recommended Recipes.");
        Scanner myObj = new Scanner(System.in); // Create a Scanner object
        System.out.println("Type 'h' to go back to the Home page.");
        String rChoice = myObj.nextLine(); // Read user input
        
        if (rChoice.equals("h")) {
          this.home(conn, username, false);
        }
      }


    }
    finally

    {

    }

  }
  
  public void login(String username, Connection conn) throws SQLException {
    try {
      Scanner myObj = new Scanner(System.in); // Create a Scanner object
      System.out.println(
          "Enter the number 1 if you have an account,or enter 2 if you want to make an account:");
      String logChoice = myObj.nextLine(); // Read user input
      int choice = Integer.parseInt(logChoice);

      // here im making user input that will then go to an if statement with two trys
      // one for loggin in and one for making an account,
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
      // System.out.println("User logged in.");
    }

    try {
     this.home(conn, username, false);
    }
    finally {
    }
  }

  /**
   * Connect to MySQL and do some stuff.
   * @throws SQLException 
   */
  public void run() throws SQLException {
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

    this.login(username, conn);

  }

  /**
   * Connect to the DB and do some stuff
   * 
   * @param args
   * @throws SQLException 
   */
  public static void main(String[] args) throws SQLException {
    bakingapp app = new bakingapp();
    app.run();
    
  }
}
