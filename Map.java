import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Reads and contains in memory the map of the game.
 * Contains accessors and other methods to manipulate the map as required.
 *
 */
public class Map {
	public static void main(String[] args) throws FileNotFoundException {
		// Creates an instance of the map constructor
		Map createMap = new Map();
	}
	
	/* Representation of the map */
	private char[][] map;
	
	/* Map name */
	private String mapName;
	
	/* Gold required for the human player to win */
	public int goldRequired;
	
	/* Booleans to check if the given map is found and valid, therefore being suitable for gameplay */
	private boolean fileFound = true;
	
	private boolean validMap = true;
	
	/**
	 * Default constructor, creates the default map "Very small Labyrinth of doom".
	 * @return : map name
	 * @return : the amount of gold required to win
	 * @return : the map two-dimensional char array
	 */
	protected String defaultMapName() {
		mapName = "Default Dungeon of Doom";
		return mapName;
	}
	protected int defaultMapGold() {
		goldRequired = 2;
		return goldRequired;
	}
	protected char[][] defaultMapGrid() {
		map = new char[][]{
			{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','.','.','.','.','.','.','G','.','.','.','.','.','.','.','.','.','E','.','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','.','.','E','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','G','.','.','.','.','.','.','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
			{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
			};
			return map;
	}
	
	/**
	 * Method that uses the default map settings and sets a random location for the player and bot in the grid.
	 * This is used if the file name or directory is not found, or if the map itself is not valid.
	 */
	protected void setDefaultMapSettings() {
		mapName = defaultMapName();
		goldRequired = defaultMapGold();
		map = defaultMapGrid();
		map = setRandomLocation(map, 'P');
		map = setRandomLocation(map, 'B');
	}
	
	/**
	 * The main method that initialises a map when the game first runs.
	 * If the map file cannot be found, the default map is used.
	 * If the map is not valid (for example, does not contain an exit tile) then the default map is used.
	 * Otherwise, the map file is read in and the name, gold required and map array are assigned to variables.
	 */
	public Map() {
		String fileName = selectMap();
		if (fileFound == false) {
			setDefaultMapSettings();
			System.out.println("\nMap Name: " + mapName);
		}
		else {
			List<String> mapStore = readMap(fileName);
			mapName = getMapName(mapStore);
			goldRequired = getGoldRequired(mapStore);
			map = getMap(mapStore);
			
			validMap = validateMap(map);
			if (validMap == true) {
				map = setRandomLocation(map, 'P');
				map = setRandomLocation(map, 'B');
				System.out.println("\nMap Name: " + mapName);	
			}
			else if (validMap == false){
				System.out.println("Invalid map grid; the default map has been selected.");
				setDefaultMapSettings();
				System.out.println("\nMap Name: " + mapName);
			}				
		}
	}
	
    /**
     * Allows user to select a map option or enter a map file name/directory.
     * The first three options use a pre-loaded map.
     * The last option allows the user to specify their own map file location. If this location is invalid,
     *		the default map is used.
     *
     * @return : the selected file name of the map to be read in,
     */
    protected String selectMap() {
    	String fileName = null;
    	// Displaying the options available to the user
    	System.out.println("Please select an map option:");
    	System.out.println("1 - Small Map");
    	System.out.println("2 - Medium Map");
    	System.out.println("3 - Large Map");
    	System.out.println("4 - Custom Map File");
    	
    	// Reading in the user's input using the Scanner class
    	// An IF statement assigns the corresponding file name
    	Scanner input = new Scanner(System.in);
    	String option = input.nextLine();
		if (option.equals("1")) {
    		fileName = "small_example_map.txt";
    	}
    	else if (option.equals("2")) {
    		fileName = "medium_example_map.txt";
    	}
    	else if (option.equals("3")) {
    		fileName = "large_example_map.txt";
    	}
    	else if (option.equals("4")) {
    		System.out.println("Please enter a file name and its root:");
    		fileName = input.nextLine();
    		// If the file name does not contain the filename extension '.txt', it is appended.
    		// This makes the filename valid in case the user forgot to type in '.txt' at the end.
    		if (fileName.indexOf(".txt") == -1) {
    			fileName = (fileName + ".txt");
             	System.out.println("The file name has been appended with .txt\n");
             	// Check to see if the appended file name exists using the built-in method .exists().
             	// If the file does not exist, the default map is selected.
             	File tempMapFileChecker = new File(fileName);
        		fileFound = tempMapFileChecker.exists();
        		if (fileFound == false) {
        			System.out.println("File not found; the default map has been selected.");
        		}
    		}
    	}
		// Default map selected if an invalid option (i.e. anything other than 1, 2, 3, 4) was input.
    	else {
    		System.out.println("Invalid option; the default map has been selected.");
    		fileFound = false;
    	}
    return fileName;
    }

    /**
     * Validates the map to ensure it is fit to be played with.
     * Checks to see if there is an appropriate amount of gold required, and at least one exit tile.
     * Otherwise, the map cannot be used and so the default map is selected.
     *
     * @param : the map grid read in from the user-specified file name.
     * @return : boolean value to denote if the map is valid or not.
     */
    protected boolean validateMap(char[][] map) {
    	int goldCount = 0;
    	boolean exitExists = false;
    	boolean negativeGold = false;
    	// Checks to see if gold required is negative.
    	if (goldRequired < 0) {
    		negativeGold = true;
    	}
    	// Loop through map array to check that gold exists and obtain the amount of gold on the map.
    	// Check to see if there is at least one exit tile for a possible win for the player.
    	for (int i = 0; i < heightOfMap(); i++) {
    	    for (int j = 0; j < (widthOfMap()); j++) {
    	    	if (accessMapPosition(i, j) == 'G') {
    	    		goldCount ++;
    	    	}
    	    	else if (accessMapPosition(i, j) == 'E') {
    	    		exitExists = true;
    	    	}
    	     }
    	 }
    	// If the amount of gold is NOT negative, and greater than or equal to the amount of gold required,
    	// 		and there is at least one exit tile, the map is valid and can be played with.
    	if (negativeGold == false) {
    		if (exitExists == true) {
    			if (goldRequired <= goldCount) {
    				validMap = true;
    			}
    			else {
    				validMap = false;
    			}
    		}
    		else {
    			validMap = false;
    		}
    	}
    	else {
    		validMap = false;
    	}
    	return validMap;
    }
    
    /**
     * Reads the map from the specified filename.
     *
     * @param : name of the map's file.
     * @return : a two-dimensional character array of the map grid 
     * @throws : FileNotFoundException 
     */
    public List<String> readMap(String fileName) {
    		String line;
    		BufferedReader inputMap = null;
    		// Using an ArrayList which has the benefit of being able to expand in size and be appended to.
    		List<String> mapArray = new ArrayList<String>();
    		
    		// Try to read in the file and add each row to the ArrayList mapArray while the file is NOT empty.
    		// However, raise an exception is the file is not found and display an error message,
    		// Set boolean fileFound to false to indicate the default map settings should be used.
    	try {
    		inputMap = new BufferedReader(new FileReader(fileName));
    		while ((line = inputMap.readLine()) != null) {
    				mapArray.add(line);
    		}
    	} catch (FileNotFoundException e) {
    		fileFound = false;
    		System.out.println("The file cannot be found.");
    	} catch (IOException e) {
    		System.out.println("An exception occured while reading the file.");
    	}
    	// Close the map if the file has been found and read in, and return another exception in case of any other errors.
    	finally {
    		if(inputMap != null) {
				try {
					inputMap.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}
	return mapArray;
    }
    
    /**
     * To get the map name, take the first line of the map file and remove the prefix 'name'.
     * The actual map name should follow this prefix and be assigned to mapName.
     * 
     * @param : the ArrayList that stores the contents of the map file.
     * @return : The name of the current map.
     */
    protected String getMapName(List<String> mapStore) {
    	String nameLine = mapStore.get(0);
    	mapName = nameLine.replace("name ", "");
        return mapName;
    }
	
    /**
     * To get the gold required, take the second line of the map file and remove the prefix 'win'.
     * The amount of gold needed to win should follow this prefix and be assigned to goldRequired.
     * To avoid any invalid values such as letters, the substring will be tried to convert to an integer
     * 		and an exception raised if it is not an integer. The default map will be selected in this case.
     * 
     * @param : the ArrayList that stores the contents of the map file.
     * @return : Gold required to exit the current map.
     */
    protected int getGoldRequired(List<String> mapStore) {
        String goldLine = mapStore.get(1);
    	String goldString = goldLine.replace("win ", "");
    	// Check to see if value is numeric, otherwise raise an exception and display an error message.
    	try {
    		goldRequired = Integer.parseInt(goldString);
    	} catch (NumberFormatException e) {
    		System.out.println("The map specifies an invalid amount of gold; the default map has been selected.");
            goldRequired = 0;
            validMap = false;
    	}
        return goldRequired;
    }
    
	/**
     * @return : the amount of gold required. Accessor used in other classes.
     */
    public int returnGoldRequired() {
		return goldRequired;
	}	
    
    /**
     * This converts the string array of the map to a character array.
     * It removes the first two elements of the array, which are the name and gold required.
     * It then converts each string element to a char array and adds that to another array.
     * This results in a 2D char array.
     * 
     * @param : the ArrayList that stores the contents of the map file.
     * @return : a 2D character array of the map grid, so that each floor tile can be accessed with two indexes
     * 			 like map[][].
     */
    protected char[][] getMap(List<String> mapStore) {
    	mapStore.remove(1);
    	mapStore.remove(0);
    	int lengthOfMap = mapStore.size();
    	char[][] mapGrid = new char[lengthOfMap][];
    	for (int i = 0; i < lengthOfMap; i++) { 
    	    	char[] charElement = mapStore.get(i).toCharArray();
    	    	mapGrid[i] = (charElement);	
    	}
        return mapGrid;
    }
    
    /**
     * Method to assign random starting locations for the player and bot and display them.
     * Starts by using the width and height of the map as boundaries to generate two random coordinates.
     * One is the X coordinate and the other is the Y coordinate.
     * The player and bot are only placed on the map if the floor tile of the random coordinates is empty,
     * 		i.e. it is not a wall (#), a gold tile or an exit tile. They also cannot be placed on the same tile.
     * 
     * @param : 2D char array of the map and the initial of either the player (P) or the bot (B).
     * @return : the map with the player and the bot positions.
     */
    protected char[][] setRandomLocation(char[][] map, char initial) {
    	boolean isPlaced = false;
		int widthOfMap = map.length;
    	int heightOfMap = map[0].length;
    	// Generate random numbers between 0 and map boundaries
    	Random rand = new Random();
    	int randomXCoordinate = rand.nextInt(widthOfMap);
    	int randomYCoordinate = rand.nextInt(heightOfMap);
    	while (isPlaced == false) {
    		// Only place player and bot on empty floor tiles
    		if (map[randomXCoordinate][randomYCoordinate] == '.') {
    			map[randomXCoordinate][randomYCoordinate] = initial;
    			isPlaced = true;
    		}
    	}
    	return map; 	
    }	
    
    /* Accessors used in other methods and classes: */

	/**
     * @return : The height of the map - how many rows there are. 
     */
	public int heightOfMap() {
		int mapHeight = map.length;
		return mapHeight;
	}
	
	/**
     * @return : The width of the map - how many columns there are.
     */
	public int widthOfMap() {
		int mapWidth = map[0].length;
		return mapWidth;
	}
	
	/**
	 * @param : two integer coordinates
     * @return : the map floor tile at the specified index - i is the X coordinate and j is the Y coordinate.
     */
	public char accessMapPosition(int i, int j) {
		return map[i][j];
	}
	
	/**
	 * @param : two integer coordinates and a floor tile character to replace the existing tile.
     * @return : modifies the specified floor tile to a new character and returns the new map array.
     */
	public char[][] replaceFloorTile(int i, int j, char floorTile) {
		map[i][j] = floorTile;
		return map;
	}

    
    }
