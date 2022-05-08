# BakeryApp

Introduction
	For our project, we created a baking app for bakers and everyday people to use to discover new recipes they can bake. As people who enjoy baking themselves, we found it difficult to find good recipes to follow so we wanted to provide a database of recipes where users can upload their own recipes or view other user’s recipes that they’ve written. Local bakeries or bakery companies can also use the database to upload official recipes for users to enjoy. Our app will keep track of the users inventory or amounts of ingredients they have and double check for them if it’s possible to bake a recipe they find for convenience. Users also can select dessert types that they enjoy such as cakes or pastries, and new recipes will be recommended to them based on their preferences, and if they enjoy the recipe, they can save it to look at it again later on. 

## Technical Descriptions
For our project submission, we wrote our Java code using Eclipse software, our SQL code using MySQL, and connected them using the JDBC connector for Java 8.0.28. These codes are the only things needed to run the application. Output for the application and user interaction occurs in the java console. If the software you’re using to run our java code doesn’t have a console, the link for eclipse is provided here: https://eclipseide.org/release/ 

## Application Runthrough 

https://www.youtube.com/watch?v=SDrPWiDJAWk 

## User Activity Description

Firstly, the user can log into the system or create a new account. To make a new account, the user types “2” and hits enter into the console. If they make a new account, they can enter their new password and username which must be unique and not yet taken. To enter them, the user can type their username and hit enter. Afterwards, they’ll be prompted for a password and can type their password then hit enter. If they are taken, a message will appear to retry and the user will be prompted to enter a different password or username. If the user already has an account, they can type “1” and hit enter. Then they’ll be prompted for their username which they can type and hit enter. After this, they’re prompted for their password which they can type and hit enter. After the user has logged in, they have a list of options from at the homepage. They can view their profile, view recommended recipes, view saved recipes, or log out. To view profile, they can type “p” and hit enter. To view recommended recipes, they can type “r” and hit enter. To view saved recipes, they can type “s” and hit enter. To log out, they can type “o” and hit enter. 
If they select the hompages option to view their profile, a new prompt of options will appear and the user can either update their account info, update the types of desserts they’re interested in, write a new recipe or view written recipes, view their inventory, or return to the homepage. From here, when they select update account by typing “a” and hitting enter, they can choose the option to update their account to be bakery owned instead of an account for an individual user by typing “i” and hitting enter. Otherwise, they can return to the profile page by typing “b” and hitting enter. The next option on the profile page is to update the user’s dessert type preferences by typing “d” and hitting enter. If they select this option, the user’s current dessert types they enjoy is displayed and a list of available dessert types they can add to the list is displayed. Then the user is prompted to either type in one of the valid dessert types and hit enter to add to their list, or they can return back to the profile page by typing b and hitting enter. The next option on the profile page is to write your own recipe or view recipes that you’ve written previously by typing “r” and hitting enter. If they select this option, they are taken to another selection page to write a recipe by typing “w” and hitting enter, view written recipes by typing “v” and hitting enter, or return to the profile page by typing “p” and hitting enter. If they choose to write a new recipe, the user is prompted to enter all information for the recipe one item at a time and hit enter, and the recipe is saved to the database and the user can return to the profile page. Once the user gets to the point where they can enter ingredients, the user can enter an ingredient from the available ingredient list that is printed out. On the other hand, if the user selects to view their written recipes, the written recipes are displayed and they’re given the option to edit a recipe by typing that recipe’s ID and hitting enter or return to the profile page by typing “p” and hitting enter. When editing the recipe, they can update the name or instructions by typing “e” and hitting enter, or they can delete the recipe from their written recipes by typing “d” and hitting enter. If they choose to edit their recipe, they can type “n” and hit enter to update the name, type “i” and hit enter to update the instructions, or type “v” and hit enter to return to viewing recipes. If they choose to update the instructions or the name of the written recipe, they are prompted to type the new name or instructions respectively and hit enter to complete the update. After this they can return to viewing their recipes by typing “v” and hitting enter or go back to the profile page by typing “p” and hitting enter. The next option on the profile page is to view inventory by typing “i” and hitting enter. This displays the current amounts of ingredients the user has available to use. The user can update the amounts they have by typing the ingredient they want to update and hitting enter. Then once prompted, entering the new amount as a number and hitting enter. Finally, the last option on the profile page allows the user to return back to the homepage by typing “h” and hitting enter. 
Our next option on the homepage is to view recommended recipes. When the user selects this option, a number of recipes are displayed based on the user’s dessert types that they enjoy. This is similar to a ‘for-you page’. From here they can choose a specific recipe to view more detailed info and instructions for by typing the ID associated with each recipe and hitting enter or return to the homepage by typing “h” and hitting enter. After viewing the recipe, they can choose to add it to their saved recipes by typing “s” and hitting enter or go back to browsing recipes by typing “b” and hitting enter. If a user saves a recipe that they themselves wrote, it will tell the user that it’s saved, but not appear under saved recipes. This is because the recipe is already viewable under the user’s written recipes and is kept there so as to not have redundancy. If they save the recipe, they are prompted to return to browsing recipes by typing “b” and hitting enter. 
The next homepage option is to view any recipes the user has saved. Once they are viewing saved recipes, they can select a specific recipe to view more instructions and see required ingredients by typing the recipe ID and hitting enter or return back to the homepage by typing “h” and hitting enter. Once they’ve selected to view a recipe, they can unsave the recipe by hitting “U” and enter or go back to their saved recipes by typing “b” and hitting enter. 
The final option on the homepage is for the user to log out. Once the user has selected to log out by typing “o” and hitting enter, they are returned back to the login page to either login again or create a new account. 

