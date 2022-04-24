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
   bakeryId VARCHAR(64),  
   phoneNo INT,
   address VARCHAR(200),
   PRIMARY KEY(bakeryId), 
   CONSTRAINT `fk_bakeryId` FOREIGN KEY (`bakeryId`) REFERENCES `user` (`username`)
);

INSERT INTO     
bakery(bakeryId, phoneNo, address)    
VALUES 
("Sweet Magnolias", 1234567891, "123 Huntington Avenue, Boston, 510203");

DROP TABLE dessertType;
CREATE TABLE dessertType (
   typeId INT AUTO_INCREMENT,   
   typeName enum('cakes', 'cupcakes', 'pudding', 'biscuits', 'pastry', 'bread'),
   typeDescription VARCHAR (1500),
   PRIMARY KEY(typeId) 
);

INSERT INTO     
dessertType(typeId, typeName, typeDescription)    
VALUES 
(1, 'cakes', "An item of soft, sweet food made from a mixture of flour, shortening, eggs, sugar, and other ingredients, baked and often decorated."), 
(2, 'cupcakes', "A small cake baked in a cup-shaped container and typically iced."),
(3, 'pudding', "A flavored, custard-like dessert made of milk, sugar, and a thickening agent such as egg yolks or corn starch."),
(4, 'biscuits', "A small, typically round cake of bread leavened with baking powder, baking soda, or sometimes yeast."),
(5, 'pastry', "An item of food consisting of sweet pastry with a cream, jam, or fruit filling."),
(6, 'bread', "Food made of flour, water, and yeast or another leavening agent, mixed together and baked");

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
Layer pastry, cream and 4 oz. strawberries; repeat. Dust with confectioners' sugar. Serve.", 5, "Sweet Magnolias"),
(2, "Cake & Berry Campfire Cobbler", "Prepare grill or campfire for low heat, using 16-20 charcoal briquettes or large wood chips.
Line an ovenproof Dutch oven with heavy-duty aluminum foil; add pie filling. In a large bowl, combine the cake mix, water and oil. Spread over pie filling.
Cover Dutch oven. When briquettes or wood chips are covered with white ash, place Dutch oven directly on top of 8-10 of them. Using long-handled tongs, place remaining briquettes on pan cover.
Cook until filling is bubbly and a toothpick inserted in the topping comes out clean, 30-40 minutes. To check for doneness, use the tongs to carefully lift the cover. If desired, serve with ice cream.", 
5, "Sweet Magnolias"), 
(3, "Microwave Chocolate Mug Cake", "Mix flour, sugar, cocoa powder, baking soda, and salt in a large microwave-safe mug; stir in milk, canola oil, water, and vanilla extract.
Cook in microwave until cake is done in the middle, about 1 minute 45 seconds.", 
1, "Sweet Magnolias"), 
(4, "Chocolate Cupcakes", "Preheat oven to 350 degrees F (175 degrees C). Grease two muffin pans or line with 20 paper baking cups.
In a medium bowl, beat the butter and sugar with an electric mixer until light and fluffy. Mix in the eggs, almond extract and vanilla. Combine the flour, cocoa, baking powder and salt; stir into the batter, alternating with the milk, just until blended. Spoon the batter into the prepared cups, dividing evenly.
Bake in the preheated oven until the tops spring back when lightly pressed, 20 to 25 minutes. Cool in the pan set over a wire rack. When cool, arrange the cupcakes on a serving platter. Frost with your favorite frosting.", 
2, "Sweet Magnolias"), 
(5, "Vanilla Pudding", "Mix sugar, cornstarch, and salt into a saucepan. Gradully stir in milk.
Bring to a boil over medium heat. Boil 1 minute, sitrring constantly.
Remove from heat. Stir in butter and vanilla. Chill.", 
3, "Sweet Magnolias"), 
(6, "3-Ingredient Peanut Butter Cookies", "Preheat oven to 350 degrees F (175 degrees C).
Mix peanut butter, sugar, and egg together in a bowl using an electric mixer until smooth and creamy. Roll mixture into small balls and arrange on a baking sheet; flatten each with a fork, making a criss-cross pattern.
Bake in the preheated oven for 10 minutes. Cool cookies on the baking sheet for 2 minutes before moving to a plate.", 
4, "Sweet Magnolias");

CREATE TABLE Ingredient (
   ingredientId INT AUTO_INCREMENT,   
   ingredientName VARCHAR (120),
   units VARCHAR (50), 
   PRIMARY KEY(ingredientId) 
);

INSERT INTO     
Ingredient(ingredientId, ingredientName, units)    
VALUES 
(1, "puff pastry", "sheets"),
(2, "crème fraîche", "cups"),
(3, "confectioners' sugar", "tablespoons"),
(4, "strawberries", "ounces"),
(5, "sugar", "cups"),
(6, "cornstarch", "tablespoons"),
(7, "salt", "teaspoon"),
(8, "milk", "cups"),
(9, "eggs", "count"),
(10, "butter", "ounces"),
(11, "vanilla", "teaspoons"), 
(12, "baking powder", "teaspoons");

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

INSERT INTO     
userDessertType(dessertType, user)    
VALUES 
(1, "Caroline"),
(3, "Caroline"),
(5, "Caroline"),
(2, "Natasha"),
(4, "Natasha"),
(5, "Natasha");

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

INSERT INTO     
recipeIngredient(recipe, ingredient, amount)    
VALUES 
(1, 1, 2.0),
(1, 2, 1.0),
(1, 3, 2.0),
(1, 4, 4.0),
(5, 5, 0.33),
(5, 6, 2.0),
(5, 7, 0.125),
(5, 8, 2.0),
(5, 9, 2.0),
(5, 10, 2.0),
(5, 11, 2.0);


DROP TABLE inventoryIngredient;
CREATE TABLE inventoryIngredient (
   ingredient INT,
   inventory INT,
   amount DOUBLE,
   CONSTRAINT `fk_inventory_ingredient` FOREIGN KEY (`ingredient`) REFERENCES `Ingredient` (`ingredientId`),
   CONSTRAINT `fk_inventory` FOREIGN KEY (`inventory`) REFERENCES `Inventory` (`inventoryId`)
);

INSERT INTO     
inventoryIngredient(ingredient, inventory, amount)    
VALUES 
(7, 1, 0.125),
(8, 1, 1.0),
(9, 1, 1.0),
(10, 1, 1.5),
(11, 1, 1.5);


DROP TABLE saves;
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
("Natasha", 2, 1, false),
("Natasha", 2, 6, false),
("Caroline", 2, 1, false);

Use bakingApp;
SELECT * FROM saves;

-- FUCTION: Check if a username exists
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

-- TESTS: testing function
SELECT usernameExists("xyz");

-- PROCEDURE: view saved recipes 
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

-- TESTS: testing procedure
CALL viewSavedRecipes("Natasha");

-- PROCEDURE: view recommended recipes
DROP PROCEDURE viewRecommendedRecipes;
DELIMITER //
CREATE PROCEDURE viewRecommendedRecipes(user VARCHAR(64))
BEGIN
	 SELECT DISTINCT(recipeName), Recipe.recipeId, instructions, dessertType.typeName, writer
		FROM Recipe LEFT OUTER JOIN userDessertType  
        ON userDessertType.dessertType = Recipe.type
        JOIN dessertType
        ON dessertType.typeId = Recipe.type
        WHERE userDessertType.user = user;
        
END //
DELIMITER ;

-- TESTS: testing procedure
CALL viewRecommendedRecipes("Caroline");

-- PROCEDURE: view all recipes
DROP PROCEDURE viewAllRecipes;
DELIMITER //
CREATE PROCEDURE viewAllRecipes()
BEGIN
	 SELECT DISTINCT(recipeName), Recipe.recipeId, instructions, dessertType.typeName, writer
		FROM Recipe LEFT OUTER JOIN userDessertType  
        ON userDessertType.dessertType = Recipe.type
        JOIN dessertType
        ON dessertType.typeId = Recipe.type;
        
END //
DELIMITER ;

-- TESTS: testing procedure
CALL viewAllRecipes();
-- PROCEDURE: finding which ingredients are missing from the inventory
-- any value that is null or > 0 means that a certain amt of ingredient is required 
-- should be considered when calling from Eclipse ^^
DROP PROCEDURE missingIngredientAmounts;
DELIMITER //
CREATE PROCEDURE missingIngredientAmounts(user VARCHAR(64), recipe INT)
BEGIN
	SELECT DISTINCT(Ingredient.ingredientName), (recipe.AmountRequired - inventory.amountInStock) as amtOfIngredientRequired, recipe.units FROM
	 (SELECT ingredient, amount as amountInStock FROM Inventory
		JOIN InventoryIngredient
		ON InventoryIngredient.inventory = Inventory.inventoryId
		WHERE Inventory.user = user) as inventory
	RIGHT OUTER JOIN
	(SELECT ingredient, amount as AmountRequired, units FROM Recipe JOIN RecipeIngredient
		ON Recipe.recipeId = RecipeIngredient.recipe
        JOIN Ingredient 
        ON RecipeIngredient.ingredient = Ingredient.ingredientId
        WHERE Recipe.recipeId = recipe) as recipe
	ON recipe.ingredient = inventory.ingredient
    JOIN Ingredient 
    ON recipe.ingredient = Ingredient.ingredientId;
    
        
END //
DELIMITER ;

-- TESTS: testing procedure
CALL missingIngredientAmounts("Caroline", 5);

SELECT * FROM saves;

-- PROCEDURE: Update amount of ingredients in inventory
DROP PROCEDURE updateInventory;
DELIMITER //
CREATE PROCEDURE updateInventory(user VARCHAR(64), ingredient VARCHAR(120), amount INT)
BEGIN
	 DECLARE inventory INT;
     DECLARE ingredientId INT;
     
     SELECT Ingredient.ingredientId 
		INTO IngredientId
	FROM Ingredient
    WHERE Ingredient.ingredientName = ingredient;
     
     SELECT inventoryId 
		INTO inventory
	FROM inventory
    WHERE inventory.user = user;
     
     DELETE FROM inventoryIngredient WHERE inventoryIngredient.ingredient = ingredientId AND inventoryIngredient.inventory = inventory;
	 INSERT INTO inventoryIngredient (inventoryIngredient.ingredient, inventoryIngredient.inventory, inventoryIngredient.amount) VALUES (ingredientId, inventory, amount);
END //
DELIMITER ;

-- TESTS: testing procedure
CALL updateInventory("Caroline", "salt", 5.0);

-- PROCEDURE: Delete Ingredient in inventory
DROP PROCEDURE deleteFromInventory;
DELIMITER //
CREATE PROCEDURE deleteFromInventory(user VARCHAR(64), ingredient VARCHAR(120))
BEGIN
	 DECLARE inventory INT;
     DECLARE ingredientId INT;
     
     SELECT Ingredient.ingredientId 
		INTO IngredientId
	FROM Ingredient
    WHERE Ingredient.ingredientName = ingredient;
     
     SELECT inventoryId 
		INTO inventory
	FROM inventory
    WHERE inventory.user = user;
     
     DELETE FROM inventoryIngredient
     WHERE inventoryIngredient.ingredient = ingredientId AND inventoryIngredient.inventory = inventory;
END //
DELIMITER ;

-- TESTS: testing procedure
CALL deleteFromInventory("Caroline", "salt");

-- PROCEDURE: Delete a dessert type interest
DROP PROCEDURE deleteDessertType;
DELIMITER //
CREATE PROCEDURE deleteDessertType(user VARCHAR(64), dessert VARCHAR(10))
BEGIN
	 DECLARE dessertId INT;
     
	SELECT dessertType.typeId 
		INTO dessertId
	FROM dessertType
    WHERE dessertType.typeName = dessert;
     
     DELETE FROM userDessertType
     WHERE userDessertType.user = user AND userDessertType.dessertType = dessertId;
END //
DELIMITER ;

-- TESTS: testing procedure
CALL deleteDessertType("Natasha", "biscuits");

-- PROCEDURE: View a recipe
DROP PROCEDURE viewRecipe;
DELIMITER //
CREATE PROCEDURE viewRecipe(recipeNum INT)
BEGIN
	SELECT Recipe.recipeName, Recipe.instructions
	FROM Recipe
    WHERE Recipe.recipeId = recipeNum;
END //
DELIMITER ;

-- TESTS: testing procedure
CALL viewRecipe(1);

SELECT * FROM saves;

-- PROCEDURE: Is this recipe already saved?
DROP PROCEDURE aSavedRecipe;
DELIMITER //
CREATE PROCEDURE aSavedRecipe(recipeNum INT, user VARCHAR(64))
BEGIN
	SELECT *
	FROM saves
    WHERE saves.recipe = recipeNum AND saves.user = user;
END //
DELIMITER ;

-- TESTS: testing procedure
CALL aSavedRecipe(1, "Natasha");

-- PROCEDURE: save a recipe
DROP PROCEDURE saveRecipe;
DELIMITER //
CREATE PROCEDURE saveRecipe(recipeNum INT, user VARCHAR(64))
BEGIN
	DECLARE inventory INT;
     
	SELECT inventory.inventoryId 
		INTO inventory
	FROM inventory
    WHERE inventory.user = user;
    
	INSERT INTO     
		saves(user, inventory, recipe, canBeMade)    
		VALUES 
		(user, inventory, recipeNum, false);
END //
DELIMITER ;

-- TESTS: testing procedure
CALL saveRecipe(5, "Caroline");

-- PROCEDURE: checks whether a user is a bakery
DROP PROCEDURE aBakery;
DELIMITER //
CREATE PROCEDURE aBakery(user VARCHAR(64))
BEGIN
	SELECT * FROM bakery 
		WHERE bakeryId = user;
END //
DELIMITER ;

-- TESTS: testing procedure
CALL aBakery("Sweet Magnolias");

-- PROCEDURE: updates phone number of bakery
DROP PROCEDURE updatePhone;
DELIMITER //
CREATE PROCEDURE updatePhone(user VARCHAR(64), newNum INT)
BEGIN
	UPDATE bakery SET bakery.phoneNo = newNum WHERE bakery.bakeryId = user;
END //
DELIMITER ;

-- TESTS: testing procedure
CALL updatePhone("Sweet Magnolias", 1231231231);

-- PROCEDURE: updates address of bakery
DROP PROCEDURE updateAddress;
DELIMITER //
CREATE PROCEDURE updateAddress(user VARCHAR(64), address VARCHAR(100))
BEGIN
	UPDATE bakery SET bakery.address = address WHERE bakery.bakeryId = user;
END //
DELIMITER ;

-- TESTS: testing procedure
CALL updateAddress("Sweet Magnolias", "test");

SELECT * FROM userDessertType;
SELECT * FROM dessertType;

-- PROCEDURE: find a user's dessert types
DROP PROCEDURE userTypes;
DELIMITER //
CREATE PROCEDURE userTypes(user VARCHAR(64))
BEGIN
	SELECT dessertType.typeName FROM userDessertType JOIN dessertType
		ON userDessertType.dessertType = dessertType.typeId
        WHERE userDessertType.user = user;
END //
DELIMITER ;

-- TESTS: testing procedure
CALL userTypes("Natasha");

-- PROCEDURE: find whether a dessert type is a user's dessert type
DROP PROCEDURE aUserType;
DELIMITER //
CREATE PROCEDURE aUserType(user VARCHAR(64), type VARCHAR(64))
BEGIN
	DECLARE typeId INT; 
    
    SELECT dessertType.typeId 
		INTO typeId
        FROM dessertType
        WHERE dessertType.typeName = type;
        
	SELECT * FROM userDessertType 
        WHERE userDessertType.user = user AND userDessertType.dessertType = typeId;
END //
DELIMITER ;

-- TESTS: testing procedure
CALL aUserType("Natasha", "pastry");

SELECT * FROM inventory;
SELECT * FROM inventoryIngredient;
-- PROCEDURE: returns a table of inventory values and ingredient names
DROP PROCEDURE getInventory;
DELIMITER //
CREATE PROCEDURE getInventory(puser VARCHAR(64))
BEGIN
DECLARE inventoryInt INT;

SELECT inventoryId
	INTO inventoryInt
    FROM inventory 
    WHERE inventory.user = puser;

SELECT * FROM inventoryIngredient JOIN (SELECT ingredientId AS id, ingredientName, units FROM ingredient) AS T
ON T.id = inventoryIngredient.ingredient
WHERE inventory = inventoryInt;

END //
DELIMITER ;

-- TESTS: testing procedure
CALL getInventory("Caroline");
CALL getInventory("Natasha");

