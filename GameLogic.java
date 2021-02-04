	/**
	 * Contains the main logic part of the game, as it processes.
	 *
	 */
public class GameLogic {
		
	/**
	 * Required global variables that require constant updating or accessibility in more than one method.
	 * These are mostly to do with the map state, and are returned from various functions.
	 */
	
	// An instance of the Map constructor which is initialised when the game is run.
	private Map map;
		
	// Integer variables to keep track of the required gold values of the game.
	protected int goldRequired;
	protected int goldOwned = 0;
		
	// Boolean that checks if the game is running. When it is false, the game quits.
	protected boolean isRunning = true;
		
	// Char global variables that keep track of the previous floor tiles of the player and bot.
	// These are used to replace the previous character of the map array after a player has moved.
	protected char playerPreviousFloorTile = '.';
	protected char botPreviousFloorTile = '.';
		
	// Variable to keep track of who's turn it is - 'P' for the human player and 'B' for the bot.
	protected char playerTurn;
		
	/**
	* Procedure that runs the game while it is playing. It alternates between turns and calls
	* 	methods from this class and the player classes.
	*/
	public GameLogic() {
		// Initialise the instance of the map constructor.
		map = new Map();
		// While the game is running and has not quit:
		while (isRunning == true) {
			// The human player goes first and the user enters a command.
			playerTurn = 'P';
			String humanCommand = HumanPlayer.getHumanPlayerCommand();
			// Process the human command.
			getNextAction(humanCommand);
			// Bot's turn - process bot command.
			playerTurn = 'B';
			String botCommand = BotPlayer.getBotCommand();
			getNextAction(botCommand);
			// Repeat cycle.
		}
	}

	 /**
	 * 'hello' command input by the user outputs the gold required to win.
	 */
	  protected void hello() {
	  goldRequired = map.returnGoldRequired();
	  System.out.println("Gold to win: " + goldRequired);
	  }
		
	  /**
	   * 'gold' command input by the user outputs how much gold the user has successfully picked up.
	   */
	    protected void gold() {
	    	System.out.println("Gold owned: " + goldOwned);
	    }
	    
	   /**
		* Function to increment the number of gold the human player owns after a successful 'pickup' command.
		*/
	    protected int incrementGold() {
	    	goldOwned += 1;
	        return goldOwned;
	    }
	    
	    /**
	     * Processes the player's 'pickup' command, updating the map and the player's gold amount.
	     *
	     * @return If the player successfully picked-up gold or not.
	     */
	    protected int pickup() {
	    	// Check if the tile the player landed on is a 'G' tile.
	    	if (playerPreviousFloorTile == 'G') {
	    		// Call the method to add 1 to the amount of gold owned.
	    		incrementGold();
	    		System.out.println("Success. Gold owned: " + goldOwned);
	    		// Replace gold tile to an empty floor tile upon successful pickup.
	    		playerPreviousFloorTile = '.';
	    	}
	    	else {
	    		System.out.println("Fail. Gold owned: " + goldOwned);
	    	}
	        return goldOwned;
	    }

	    /**
	     * Checks if movement is legal and updates player's or bot's location on the map.
	     *
	     * @param direction : The direction of the movement.
	     * @return : Protocol if success or not.
	     */
	    protected void move(char direction) {
	    	// Initialise two coordinate variables - these will store the position of the player in the map.
	    	int xCoordinate = 0; 
	    	int yCoordinate = 0;
	    	// Loop through the map to find the position of the character tile 'P'. 
	    	// When found, assign these index values to the coordinate variables.
	    	for(int i = 0; i < map.heightOfMap(); i++) {
	    	    for(int j = 0; j < (map.widthOfMap()); j++) {
	    	      if(map.accessMapPosition(i, j) == playerTurn) {
	    	    	  xCoordinate = i;
	    	    	  yCoordinate = j;
	    	    	  break;  
	    	      }
	    	   }
    	    }	
	    	/** Assign current coordinates to new coordinate variables.
	    	 * This will be used to check if the move is valid (doesn't go into walls) or
	    	 * 		if the player is caught by the bot.
	    	 */
	    	int nextXCoordinate = xCoordinate;
	    	int nextYCoordinate = yCoordinate;
	    	// IF statement to alter the corresponding coordinate depending on the direction entered.
	    	if (direction == ('n') || direction == ('N')) {
	    		nextXCoordinate --;
	    	}
	    	else if (direction == ('s') || direction == ('S')) {
	    		nextXCoordinate ++;
	    	}
	    	else if (direction == ('e') || direction == ('E')) {
	    		nextYCoordinate ++;
	    	}
	    	else if (direction == ('w') || direction == ('W')) {
	    		nextYCoordinate --;
	    	}
	    	
	    	// The variable nextFloorTile is assigned the value of the map using the next coordinates.
	    	// This value is the 'possible' move that the user or bot wants to move to.
	    	char nextFloorTile = map.accessMapPosition(nextXCoordinate, nextYCoordinate);
	    	
	    	// If the next floor tile contains a wall, the move is classed as invalid and the turn is over.
	    	if (nextFloorTile == '#') {
	    		if (playerTurn == 'P') {
	    			System.out.println("Fail.");	
	    		}
	    		else if (playerTurn == 'B') {
	    			System.out.println("Bot move failed.");
	    		}
	    	}
	    	else {
	    		// If it is the bot's turn, replace the previous floor tile in the map with a few exceptions.
	    		// Do not replace a gold or exit square with an empty tile! 
	    		// If the bot lands on the player the game ends.
	    		if (playerTurn == 'B') {
	    			if (botPreviousFloorTile == 'P') {
	    				botCaughtPlayer();
	    			}
	    			else if (botPreviousFloorTile == 'G') {
	    				map.replaceFloorTile(xCoordinate, yCoordinate, 'G');		    			
	    			}
	    			else if (botPreviousFloorTile == 'E') {
	    				map.replaceFloorTile(xCoordinate, yCoordinate, 'E');
	    			}
	    			else {
	    				map.replaceFloorTile(xCoordinate, yCoordinate, botPreviousFloorTile);	
	    			}
	    		
	    		// Update the bot's previous floor tile and replace the next tile with 'B'.
	    		botPreviousFloorTile = nextFloorTile;
	    		map.replaceFloorTile(nextXCoordinate, nextYCoordinate, playerTurn);	
	    		System.out.println("Bot has moved.");
	        	}
	    		// Repeat the same checks but with the player's previous tile when it is their turn.
	    		else if (playerTurn == 'P') {
	    			if (playerPreviousFloorTile == 'B') {
	    				botCaughtPlayer();
	    			}
	    			else if (playerPreviousFloorTile == 'G') {
	    				map.replaceFloorTile(xCoordinate, yCoordinate, 'G');		    			
	    			}
	    			else if (playerPreviousFloorTile == 'E') {
	    				map.replaceFloorTile(xCoordinate, yCoordinate, 'E');
	    			}
	    			else {
	    				map.replaceFloorTile(xCoordinate, yCoordinate, playerPreviousFloorTile);	
	    			}
	    			// Update the player's previous floor tile and replace the next tile with 'P'.
	    			playerPreviousFloorTile = nextFloorTile;
		    		map.replaceFloorTile(nextXCoordinate, nextYCoordinate, playerTurn);
		    		System.out.println("Success.");
	    		}
	    	}   	
	    }

	    /**
	     * Converts the map from a 2D char array to a single string to be output in a 5x5 grid to be displayed
	     * 		to the user upon entering the 'look' command.
	     * This command is also used by the bot to try and catch the player.
	     *
	     * Displays the 5x5 grid to the human player and calls other subroutines in the Bot class if the bot
	     * 		uses this command.
	     */
	    protected void look() {	    
	    	// Find and assign the index positions of the bot/human in the grid to integer coordinate variables.
	    	int xCoordinate = 0, yCoordinate = 0;
	    	for(int i = 0; i < map.heightOfMap(); i++) {
	    	    for(int j = 0; j < (map.widthOfMap()); j++) {
	    	      if(map.accessMapPosition(i, j) == playerTurn) {
	    	    	  xCoordinate = i;
	    	    	  yCoordinate = j;
	    	      }
	    	   }
	    	}
	    	// Create the boundaries of the five by five grid to be displayed.
	    	// As the player is in the centre, the square is two squares up, down, left and right of them.
	    	int startOfRows = xCoordinate - 2;
	    	int endOfRows = xCoordinate + 2;
	    	int startOfColumns = yCoordinate - 2;
	    	int endOfColumns = yCoordinate + 2;    
	    	// If it is the player's turn, display the five by five grid.
	    	if (playerTurn == 'P') {
	    		for (int k = startOfRows; k <= endOfRows ; k++) {
	    			for(int l = startOfColumns; l <= endOfColumns; l++) {
	    				try {
	    					System.out.print(map.accessMapPosition(k, l));
	    				}
	    				// Avoid ArrayIndexOutOfBounds Exception by using a try-catch block.
	    		    	// If the five by five square goes outside the range of the map array, then display a # sign.
	    				catch (ArrayIndexOutOfBoundsException e) {
	    					System.out.print("#");
	    				}
	    			}
	    		System.out.print("\n");	
	    		}
	    	}
	    	// If it is the bot's turn, create a new 2D array just containing the five by grid.
	    	// This will be used as a parameter in the distance calculator method in the Bot class.
	    	else if (playerTurn == 'B'){
	    		// Check to ensure that proposed bot grid is within the boundaries of the map.
	    		if (startOfRows < 0) {
	    			startOfRows = 0;
	    		}
	    		if (endOfRows > map.heightOfMap()) {
	    			endOfRows = map.heightOfMap();
	    		}
	    		if (startOfColumns < 0) {
	    			startOfColumns = 0;
	    		}
	    		if (endOfColumns > map.widthOfMap()) {
	    			endOfColumns = map.widthOfMap();
	    		}
		    	char[][] fiveByFiveGrid = new char[5][5];
		    	int columnCount = 0;
		    	int rowCount = 0;
		    		for (int k = startOfRows; k <= endOfRows ; k++) {
		    			for(int l = startOfColumns; l <= endOfColumns; l++) {
		    				fiveByFiveGrid[rowCount][columnCount] = (map.accessMapPosition(k, l));
		    				columnCount++;
		    			}
		    		rowCount++;
		    		columnCount = 0;
		    		}
		    	BotPlayer.manhattanDistance(fiveByFiveGrid);
	    	}
	    }

	    /**
	     * Quits the game, shutting down the application.
	     */
	    protected void quitGame() {
	    	// If the player is on an exit tile and has the minimum amount of gold owned to win:
	    	// The game is over and the player has won.
	    	if (playerPreviousFloorTile == 'E') {
	    		if (goldOwned == goldRequired) {
	    			System.out.println("WIN");
	    			System.out.println("Congratulations!");
	    			System.out.println("Game quit.");
	    			isRunning = false;
	    			System.exit(0);
	    		}
	    		else {
	    			System.out.println("LOSE");
	    			System.out.println("Not on an exit tile.");
	    			System.out.println("Game quit.");
		    		isRunning = false;
		    		System.exit(0);
	    		}
	    	}
	    	// If the player inputs a 'quit' command without being on an exit tile AND not having enough gold, they lose.
	    	else {
	    		System.out.println("LOSE");
	    		System.out.println("You have lost.");
	    		System.out.println("Game quit.");
	    		isRunning = false;
	    		System.exit(0);
	    	}
	    }
	    
	    /**
	     * Quits the game when the bot has caught the player.
	     */
	    protected void botCaughtPlayer() {
	    	System.out.println("LOSE");
    		System.out.println("The bot has caught you!");
    		// The game is over, so system quits.
    		isRunning = false;
    		System.exit(0);
	    }
	    
	    /**
	     * Processes the command. It returns a reply in form of a String, as the protocol dictates.
	     * Otherwise it should return the string "Invalid".
	     *
	     * @param : string command input by the user.
	     * @return : Processed output or Invalid if the @param command is wrong.
	     */
	    public void getNextAction(String userCommand) {
	    	// IF statement to check if the command matches one of the accepted inputs.
	        if (userCommand.equalsIgnoreCase("hello") == true) {
	        	hello();
	        }
	        else if (userCommand.equalsIgnoreCase("gold") == true) {
	        	gold();
	        }
	        else if ((userCommand.equalsIgnoreCase("move n")) ||
	        		 (userCommand.equalsIgnoreCase("move s")) ||
	        		 (userCommand.equalsIgnoreCase("move e")) ||
	        		 (userCommand.equalsIgnoreCase("move w"))) {      	
	        	// If the command is a move command, process the direction initial input as a character.
	        	// Call the move() method with the direction character as a parameter.
	        	char direction = userCommand.charAt(userCommand.length() - 1);
	        	move(direction);
	        }
	        else if (userCommand.equalsIgnoreCase("look")) {
	        	look();
	        	if (playerTurn == 'B') {
	        		System.out.println("Bot has looked.");
	        	}
	        }
	        else if (userCommand.equalsIgnoreCase("pickup")) {
	        	pickup();
	        }
	        else if (userCommand.equalsIgnoreCase("quit")) {
	        	quitGame();	
	        }
	        else {
	        	System.out.println("Invalid.");
	        }
	    }	
		public static void main(String[] args) {
			GameLogic logic = new GameLogic();
	    }
	}
