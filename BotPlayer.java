import java.util.Random;

public class BotPlayer {
	
	/* 
	 * Value to track how often the bot 'looks'.
	 * Set to 2 to start with to ensure the bot's first action is to look.
	 *  Before this global value reaches 3 it is reset to 0.
	 */
	protected static int lookEveryTwoTurns = 2;
	
	protected static String botMoveCommandString = null;
	/**
     * Calculates the bot's distance from the player.
     * 
     * @param : a five by five grid of the map view with the bot at the centre.
     * This is used to calculate the horizontal and vertical distances between the bot and the
     * player (if present in the grid) and move in the direction with the smallest distance.
     * 
     * If the player is not present in the grid, the bot will select a random direction to move.
     * 
     * The bot's first command is to look, then move two spaces in the same direction before looking again.
     * 
     * @return : An integer of the Manhattan distance between the players.
     */
	public static String manhattanDistance(char[][] fiveByFiveGrid) {
		// Initialising the coordinate variables of the bot and player in the 5x5 grid
		// Looping through the grid to find the indexes of the char array[][] and assigning these to the coordinates
		int xCoordinateOfBot = 0;
		int yCoordinateOfBot = 0;
		boolean isPlayerPresentInBotGrid = false;
		int xCoordinateOfPlayer = 0;
		int yCoordinateOfPlayer = 0;
		for(int i = 0; i < 5; i++) {
    	    for(int j = 0; j < 5; j++) {
    	        if(fiveByFiveGrid[i][j] == 'P') {
    	        isPlayerPresentInBotGrid = true;
    	    	xCoordinateOfPlayer = i;
    	    	yCoordinateOfPlayer = j;
    	        }
    	        else if(fiveByFiveGrid[i][j] == 'B') {
        	    	xCoordinateOfBot = i;
        	    	yCoordinateOfBot = j;
        	    }
    	    }
		}
		/* 
		 * If the player is present in the 5x5 grid, calculate the distance to the bot.
		 * The vertical and horizontal distances are compared, and the greatest distance of the two is found.
		 * Move in the direction of the greatest distance to close the gap between the bot and the player.
		 * 
		 * If the vertical or horizontal distance is positive, the bot moves south or east respectively.
		 * If the vertical or horizontal distance is negative, the bot moves north or west respectively.
		 * 
		 */
		if (isPlayerPresentInBotGrid == true) {
			int verticalDistance = xCoordinateOfPlayer - xCoordinateOfBot;
			int horizontalDistance = yCoordinateOfPlayer - yCoordinateOfBot;
			int greatestDistanceDirection = Math.max(Math.abs(verticalDistance), Math.abs(horizontalDistance));
			if (greatestDistanceDirection == Math.abs(verticalDistance)) {
				if (verticalDistance < 0) {
					botMoveCommandString = "move n";
				}
				else if (verticalDistance > 0) {
					botMoveCommandString = "move s";
				}
			}
			else if (greatestDistanceDirection == Math.abs(horizontalDistance)) {
				if (horizontalDistance < 0) {
					botMoveCommandString = "move w";
				}
				else if (horizontalDistance > 0) {
					botMoveCommandString = "move e";
				}
			}
		}
		/*
		 * If the player is not present in the 5x5 grid, the bot randomly selects a direction to move.
		 */
		else {
			String[] randomDirectionArray = {"move n", "move s", "move e", "move w"};
			int randomValue = new Random().nextInt(randomDirectionArray.length);
		    botMoveCommandString = randomDirectionArray[randomValue];
		}
	return botMoveCommandString;
	}
	/**
     * Outputs the command string for the bot's turn.
     * @return : A string with the specified command.
     */
	public static String getBotCommand() {
		System.out.println("\nBot's turn.");
		String outputBotCommand = null;
		if (lookEveryTwoTurns == 2) {
			outputBotCommand = "look";
			lookEveryTwoTurns = 0;
		}
		else {
			lookEveryTwoTurns++;
			outputBotCommand = botMoveCommandString;
		}
	return outputBotCommand;
	}
}
