DROP DATABASE IF EXISTS bakingApp;
CREATE DATABASE bakingApp;

USE bakingApp;

CREATE TABLE user (
   username VARCHAR(64)	UNIQUE,
   password VARCHAR(64),
   PRIMARY KEY(username, password)
   
);

INSERT INTO     
user(username, password)    
VALUES 
("Natasha", "123@456"),
("Caroline", "abc$def"), 
("Sweet Magnolias", "iAmABakery123");

CREATE TABLE bakery (
   name VARCHAR(120),
   bakeryId VARCHAR(64),  
   phoneNo INT,
   address VARCHAR(200),
   PRIMARY KEY(bakeryId), 
   CONSTRAINT `fk_bakeryId` FOREIGN KEY (`bakeryId`) REFERENCES `user` (`username`)
);

DROP TABLE dessertType;
CREATE TABLE dessertType (
   typeId INT AUTO_INCREMENT,   
   typeName enum('cakes', 'cupcakes', 'pudding', 'biscuits', 'pastry', 'bread'),
   typeDescription VARCHAR (120),
   PRIMARY KEY(typeId) 
);

INSERT INTO     
dessertType(typeId, typeName, typeDescription)    
VALUES 
(1, 'pastry', "An item of food consisting of sweet pastry with a cream, jam, or fruit filling");

DROP TABLE Recipe;
CREATE TABLE Recipe (
   recipeId INT AUTO_INCREMENT,   
   recipeName VARCHAR (120),
   instructions VARCHAR (2500), 
   type INT,
   writer VARCHAR(64),
   PRIMARY KEY(recipeId), 
   CONSTRAINT `fk_recipe_type` FOREIGN KEY (`type`) REFERENCES `dessertType` (`typeId`),
   CONSTRAINT `fk_written_by` FOREIGN KEY (`writer`) REFERENCES `user` (`username`)
);

INSERT INTO     
Recipe(recipeId, recipeName, instructions, type, writer)    
VALUES 
(1, "3-Step Strawberry Dessert", "Cut 2 sheets of puff pastry in half. Bake 3 of them at 400°F Just 4 ingredients! for 18 minutes. 
In a medium bowl, whip 1 cup crème fraîche and 2 Tbsp. confectioners' sugar until fluffy.
Layer pastry, cream and 4 oz. strawberries; repeat. Dust with confectioners' sugar. Serve.", 1, "Sweet Magnolias"),
(2, "Cake & Berry Campfire Cobbler", "Prepare grill or campfire for low heat, using 16-20 charcoal briquettes or large wood chips.
Line an ovenproof Dutch oven with heavy-duty aluminum foil; add pie filling. In a large bowl, combine the cake mix, water and oil. Spread over pie filling.
Cover Dutch oven. When briquettes or wood chips are covered with white ash, place Dutch oven directly on top of 8-10 of them. Using long-handled tongs, place remaining briquettes on pan cover.
Cook until filling is bubbly and a toothpick inserted in the topping comes out clean, 30-40 minutes. To check for doneness, use the tongs to carefully lift the cover. If desired, serve with ice cream.", 
1, "Sweet Magnolias");

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

INSERT INTO     
Inventory(inventoryId, user)    
VALUES 
(1, "Caroline"),
(2, "Natasha");

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

INSERT INTO     
saves(user, inventory, recipe, canBeMade)    
VALUES 
("Natasha", 1, 2, false), 
("Caroline", 2, 1, false);


-- Check if a username exists
DROP FUNCTION IF EXISTS usernameExists;
DELIMITER //
CREATE FUNCTION usernameExists
(
	nameEntry VARCHAR(64)
)
RETURNS VARCHAR(64)
DETERMINISTIC READS SQL DATA
BEGIN
	DECLARE name VARCHAR(64);

	SELECT username 
	INTO name
    FROM user WHERE user.username = nameEntry;
    
    
RETURN(name) ; 
END//
DELIMITER ;

-- testing function
SELECT usernameExists("xyz");

-- view saved recipes 
DROP PROCEDURE viewSavedRecipes;
DELIMITER //
CREATE PROCEDURE viewSavedRecipes(user VARCHAR(64))
BEGIN
	 SELECT Recipe.recipeId, Recipe.recipeName, Recipe.instructions, dessertType.typeName, Recipe.writer, canBeMade 
		FROM saves JOIN Recipe
        ON saves.recipe = Recipe.recipeId
        JOIN dessertType
        ON Recipe.recipeId = dessertType.typeId
        WHERE saves.user = user;
        
END //
DELIMITER ;

-- testing procedure
CALL viewSavedRecipes("Caroline");
