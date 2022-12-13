These files are the final products for the lessons before the Computer Programming with Java Final Project.

Here's a summary of what each file is for:
 - The Methods file contains the methods used to detect collisions and to draw the HUD.

 - The Movement_and_Collisions_Template is a blank file with no code for user input.
     - The following files use this template:
         - Movement
         - Walls
         - Collisions

 - The Movement file contains code a method for moving the player and for drawing the player.
     - talk about the scope of a variable again and make sure students know that we can use global variables in methods.
     - talk about how we should split our program up into methods to make it easier to read.
     - talk about how to create an array to store info on the player (x, y, w, h) and to create an array for velocity.
 
 - The Walls file contains code for creating a 2D array of walls and a method for drawing them.
 
 - The Collisions file updates the movePlayer method so that the player will move in the opposite direction if they hit a wall.
     - talk to students about how to use the collided method in the Methods file. 
     - talk to students about why we need to check for vertical and horizontal collisions.
     - talk about why we would use the break command to get out of the for loop.
     
 - The Final_Project_Template is a blank file that has a mouse and key listener.
     - The following files use this template:
         - Movement_with_Inputs
         - Game_Mechanics
         
 - The Movement_with_Inputs file has pretty much the same code as the Collisions file, but it uses user input and updates velocity
   based on that input.
     - talk about how we use these mouse and key listeners to get user input and store them in a HashMap
         - all they really need to know is to use inputs.get(...) to see if a button is being pressed or not.
     - talk about how we want the velocity to be 0 if the user is about to run into something.

 - The Game_Mechanics file adds on to the Movement_with_Inputs file by including screens and game mechanics
     - talk about how to draw screens and switch between them
     - talk about creating a game mechanic (like coin collecting) to know when a user has won the game
     - talk about keeping track of the overall score
     - talk about resetting the game when the user restarts from the game over screen
     
     