DROP DATABASE IF EXISTS bakingApp;
CREATE DATABASE bakingApp;

USE bakingApp;

CREATE TABLE user (
   username VARCHAR(64)	UNIQUE,
   password VARCHAR(64),
   PRIMARY KEY(username, password)
   
);

SELECT * FROM user;

CREATE TABLE bakery (
   name VARCHAR(120),
   bakeryId VARCHAR(64),  
   phoneNo INT,
   address VARCHAR(200),
   PRIMARY KEY(bakeryId), 
   CONSTRAINT `fk_bakeryId` FOREIGN KEY (`bakeryId`) REFERENCES `user` (`username`)
);

CREATE TABLE dessertType (
   typeId INT AUTO_INCREMENT,   
   typeName enum('cakes, cupcakes, pudding, biscuits, pastry, bread'),
   typeDescription VARCHAR (120),
   PRIMARY KEY(typeId) 
);

CREATE TABLE Recipe (
   recipeId INT AUTO_INCREMENT,   
   recipeName VARCHAR (120),
   instructions VARCHAR (120), 
   type INT,
   writer VARCHAR(64),
   PRIMARY KEY(recipeId), 
   CONSTRAINT `fk_recipe_type` FOREIGN KEY (`type`) REFERENCES `dessertType` (`typeId`)
);

CREATE TABLE Ingredient (
   ingredientId INT AUTO_INCREMENT,   
   ingredientName VARCHAR (120),
   units VARCHAR (50), 
   PRIMARY KEY(ingredientId) 
);

CREATE TABLE Inventory (
   inventoryId INT AUTO_INCREMENT,   
   user VARCHAR(64), 
   PRIMARY KEY(inventoryId), 
   CONSTRAINT `fk_user_inventory` FOREIGN KEY (`user`) REFERENCES `user` (`username`)
);

CREATE TABLE userDessertType (
   dessertType INT,
   user VARCHAR(64), 
   CONSTRAINT `fk_user_dessert_type` FOREIGN KEY (`user`) REFERENCES `user` (`username`),
   CONSTRAINT `fk_dessert_type` FOREIGN KEY (`dessertType`) REFERENCES `dessertType` (`typeId`)
);

CREATE TABLE writtenRecipe (
   recipe INT,
   writer VARCHAR(64),
   dateWritten DATE,
   CONSTRAINT `fk_written_recipe` FOREIGN KEY (`recipe`) REFERENCES `Recipe` (`recipeId`),
   CONSTRAINT `fk_written_writer` FOREIGN KEY (`writer`) REFERENCES `user` (`username`)
);

CREATE TABLE recipeIngredient (
   recipe INT,
   ingredient INT,
   amount DOUBLE,
   CONSTRAINT `fk_recipe_ingredient` FOREIGN KEY (`recipe`) REFERENCES `Recipe` (`recipeId`),
   CONSTRAINT `fk_ingredient` FOREIGN KEY (`ingredient`) REFERENCES `Ingredient` (`ingredientId`)
);

CREATE TABLE inventoryIngredient (
   ingredient INT,
   inventory INT,
   amount DOUBLE,
   CONSTRAINT `fk_inventory_ingredient` FOREIGN KEY (`ingredient`) REFERENCES `Ingredient` (`ingredientId`),
   CONSTRAINT `fk_inventory` FOREIGN KEY (`inventory`) REFERENCES `Inventory` (`inventoryId`)
);

CREATE TABLE saves (
   user VARCHAR(64),
   inventory INT,
   recipe INT,
   canBeMade boolean,
   CONSTRAINT `fk_user_saver` FOREIGN KEY (`user`) REFERENCES `user` (`username`),
   CONSTRAINT `fk_inventory_save` FOREIGN KEY (`inventory`) REFERENCES `Inventory` (`inventoryId`),
   CONSTRAINT `fk_recipe_saved` FOREIGN KEY (`recipe`) REFERENCES `Recipe` (`recipeId`)
);


