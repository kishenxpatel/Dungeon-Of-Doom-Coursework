import java.util.*;
/**
 * Contains code needed to read inputs.
 *
 */
public class HumanPlayer {
    /**
     * Reads player's input from the console.
     * <p>
     * return : A string containing the input the player entered.
     */
    public static String getHumanPlayerCommand() {
    	System.out.println("\nEnter a command: ");
    	// Read user input from command line using an instance of the Scanner class
        Scanner input = new Scanner(System.in);
        String userInput = input.nextLine();
        // Return the command string to be used a parameter for the getNextAction subroutine in class GameLogic.
		return userInput;
    }
    
}