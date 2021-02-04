# Dungeon-Of-Doom-Coursework
Dungeon Of Doom was a piece of coursework we had to complete for our Principles of Programming unit in first-year undergraduate Computer Science at the University of Bath. I received a final score of 82/100 for this game, which is a First.


Read Me - Dungeon of Doom
-------------------------

There are four classes used in this game: Map, HumanPlayer, BotPlayer & GameLogic. Please run the GameLogic class; this serves as the main class to run the whole game.
GameLogic calls instances of the Map constructor created in the Map class, and also calls methods specific to the Map and player classes. These should all be placed in the same directory.
Developed in Eclipse IDE.

***** IF THE MAP DOES NOT LOAD THE FIRST TIME, PLEASE RESTART THE PROGRAM AND TRY AGAIN.*****
For some reason the program is not consistent with loading maps in - keep trying until it loads a map.

Game Overview
-----------------------
You are a fortune-hunter seeking to enter the dungeon and search for some rare gold. You are allowed to select from a variety of commands to aid your traversal in the dungeon, including MOVE, which allows your player to move.
The aim is to get enough gold and exit the dungeon without being caught by the bot villain, which will be trying to chase you.

The game is turn-based, and the human player starts first. On each turn, the player enters a command and if the command is successful, the corresponding action occurs. The game is over when one of the following conditions is met:

- when the human player has enough gold to meet the requirement and calls the QUIT command on an exit tile, which is a WIN.

- the bot lands onto the same square as the player (or vice versa), which results in the bot catching you and therefore a LOSE.

The commands used to play the game are further below.


Game Setup
--------------------
The player starts with 0 gold and must obtain the required value of gold to be able to exit the dungeon successfully. Both the player and the bot start the game at a random location within the dungeon on an empty floor tile. The board representation details are further below.


Loading the Map
-------------------------
On running the program, you will be given a set of four options to pick a map. The first three options use files in the SAME directory as the code classes (relative file paths) for convenient map inputs. However, if you do not put the maps into the same file directory and select options 1, 2 or 3, the default package will be selected.

Option 4 allows you to enter a custom file name - this can be used to read in absolute file paths. Please enter a file name that does exist or the default map will be selected. An absolute file path would resemble something to this: C:\dungeon_map. Option 4 can be selected if there are no map files in the same directory as the classes.

Please ensure any maps you want to input are valid. The program will check if there are any problems with the map (such as no exits or invalid values of gold) and if there are, the default map will be selected.


Navigating the Map
----------------------------
The dungeon is made up of floor tiles, with each square containing a different character; one of the following:

'P' - this represents the human player's position.

'B' - this represents the bot player's position.

'.' - an empty floor tile. Players can move onto these squares.

'G' - a tile with gold on it. The human player can move onto this tile and pickup the gold. If the pickup is successful, this tile will revert back to an empty floor tile, '.'

'E' - an exit tile. The human player can use the QUIT command whilst on this tile and exit the dungeon to win the game (only if the gold required condition is met).

'#' - a wall tile. Players cannot move through this tile.


Command Protocol
----------------------------
The human player will be requested to input one of the following commands into the command line when it is their turn.

'HELLO' - displays the total amount of gold required for the human player to be eligible to win. This number does not decrease as gold is 	  collected.

'GOLD' - displays the current amount of gold owned.

'MOVE <direction>' - where direction is a character from N, S, E or W. This moves the player one square in the indicated direction, such as MOVE S. Players cannot move into walls. The response is either a 'Success' or 'Fail' depending on whether the move is valid or not.

'PICKUP' - Picks up the gold on the player’s current location. The response is 'Success' and the amount of gold that the player has after picking up the gold on the tile. If there  is no gold on the square, the response is 'Fail' and the amount of gold that the player had before attempting PICKUP. 

'LOOK' - The response is a 5x5 grid, showing the map around the player. Walls, exit tiles and gold are displayed as well. Any areas outside the map visible in the grid are displayed as a '#' symbol.

'QUIT' - Quits the game. If the player is standing on the exit tile 'E' and has enough gold to win, the response is WIN. Otherwise, the response is LOSE and the game quits.

All commands take up a player's turn regardless of whether they were successful or not.


Bot Implementation
----------------------------
The bot uses a simple distance calculator to try and find the human player. The bot uses the command LOOK after every two moves. It only sees a five by five grid, the same as the player.

When the player is not in the bot's five by five grid, the bot will move randomly until the player is in view. Then the bot will calculate the horizontal and vertical distances between it and the player and compare them. It will then move in the direction of the player and chase it down until the player is out of sight.
