package preDataMiningModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Preprocessor {
	static String FILEPATH = "C:\\Users\\Adam Rains\\DataMining\\DataMining\\LetterRecognitionDataMining\\resources\\";
	static String FILENAME = "letter-recognition.data";
	
	static ArrayList<ArrayList<String>> characterData;
	static int LINE_LENGTH = 17;
	static int ATTRIBUTE_LOW = 0;
	static int ATTRIBUTE_HIGH = 15;
	static String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	static PrintWriter logger;
	
	/**
	 * ========================================================================
	 * readFile Method
	 * 
	 * @return The cleaned character data array with invalid entries removed
	 */
	@SuppressWarnings("resource")
	public static ArrayList<ArrayList<String>> preprocessData(){
		//Create logger
			try {
				logger = new PrintWriter(new File(FILEPATH + "Preprocessing.log"));
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		//Import data
		characterData = readFile();
		
		//Ensure first character on each line is the letter representing the data
		checkForLetters();
		
		//Ensure attribute list only contains numbers and they are within the correct range
		checkAttributes();
		
		//Print cleaning results
		logger.println("Total entries remaining after cleaning: " + characterData.size());
		logger.flush();
		
		//Split data into 26 files, one for each letter
		splitByLetter();
		
		//Data has been cleaned, return

		return characterData;
	}
	
	/**
	 * ========================================================================
	 * readFile Method
	 * 
	 * Reads the raw data and adds it to a 2D ArrayList: lines and entries for the line
	 * 
	 * @return An ArrayList format of the raw data separated by line and ,
	 */
	private static ArrayList<ArrayList<String>> readFile(){
		//Array to be returned
		ArrayList<ArrayList<String>> characterDataArray = new ArrayList<ArrayList<String>>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILEPATH + FILENAME));
			
			//Read each line, separate at commas and store
			String line = null;
			String[] splitLineComma;
			
			ArrayList<String> tempArray = new ArrayList<String>();
			
			//Read 
			while ((line = reader.readLine()) != null){
				splitLineComma = line.split(",");
				
				//Add each entry to temp array for the line
				for (int count = 0; count < splitLineComma.length; count++){
					tempArray.add(splitLineComma[count]);
				}
				
				//Add the line data to the main array and refresh the temp array
				characterDataArray.add(tempArray);
				tempArray = new ArrayList<String>();
			}
			reader.close();	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Give information about array
		logger.println("Number of character entries: " + characterDataArray.size());
		logger.flush();
		
		//Return the character data array
		return characterDataArray;
	}

	
	
	/**
	 * ========================================================================
	 * checkForLetters Method
	 * 
	 * Checks the character data for invalid first characters representing the letter (Only upper case accepted)
	 * Removes any line that fails the test.
	 */
	private static void checkForLetters(){
		int startingLineCount = characterData.size();
		int finalLineCount;
		
		//Loop through each line, checking only the first character
		for (int lineCount = 0; lineCount < characterData.size(); lineCount++){
			//Check if first character is a letter, if not, erase line entry
			if (!(characterData.get(lineCount).get(0).matches("[A-Z]+"))){
				//Character missing, remove line data
				characterData.remove(lineCount);
				
				//Decrement lineCount due to missing line
				lineCount--;
			}
							
		}
		
		//All lines complete, display results
		finalLineCount = characterData.size();
		logger.println("Lines with letters: " + finalLineCount + "/" + startingLineCount);
		logger.flush();
	}

	/**
	 * ========================================================================
	 * checkAttributes Method
	 * 
	 * Checks the character data for invalid attributes.
	 * Checks the number of attributes, whether the attribute is an integer and whether it is within the specified bounds.
	 * Removes any line that fails the test.
	 */
	private static void checkAttributes(){
		int startingLineCount = characterData.size();
		int finalLineCount;
		int attribute;
		
		//Loop through each line, checking only the first character
		for (int lineCount = 0; lineCount < characterData.size(); lineCount++){
			//Check each line, if not correct length discard immediately
			if ((characterData.get(lineCount).size() != LINE_LENGTH)){
				//Character missing, remove line data
				characterData.remove(lineCount);
				
				//Decrement lineCount due to missing line
				lineCount--;
				
			} else {
				//Line is correct length, now check each attribute is an integer between the required bounds
				for (int attributeCount = 1; attributeCount < LINE_LENGTH; attributeCount++){
					//Check attribute
					try	{
						attribute = Integer.parseInt(characterData.get(lineCount).get(attributeCount));
					} catch (NumberFormatException e) {
						//Attribute isn't an integer, remove the line and break
						characterData.remove(lineCount);
						
						//Decrement lineCount due to missing line
						lineCount--;
						
						break;
					}
					//Passed catch statement, attribute is an integer
					
					//Check bounds
					if (!((attribute >= ATTRIBUTE_LOW) && (attribute <= ATTRIBUTE_HIGH))){
						//Attribute is not within bounds, remove line
						characterData.remove(lineCount);
						
						//Decrement lineCount due to missing line
						lineCount--;
					}
					
				}
			}
			
		}
		
		//All lines complete, display results
		finalLineCount = characterData.size();
		logger.println("Lines with correct number of integer attributes within bounds: " + finalLineCount + "/" + startingLineCount);
		logger.flush();
	}

	/**
	 * ========================================================================
	 * splitByLetter Method
	 * 
	 * Saves each line to one of 26 different files depending on which letter it represents.
	 */
	private static void splitByLetter(){
		String currentLetter;
		int totalLetters = LETTERS.length();
		int totalLinesForLetter = 0;
		ArrayList<ArrayList<String>> letterLineArray = new ArrayList<ArrayList<String>>();
		
		logger.println("Total instances per letter: ");
		logger.flush();
		
		//For each letter in the list of letters, loop
		for (int letterCount = 0; letterCount < totalLetters; letterCount++){
			//Get the current letter
			currentLetter = LETTERS.substring(letterCount, letterCount + 1);
			
			//Loop through line array and add each line representing the current letter to a new array
			//Once complete, save the created list to a file
			for (int lineCount = 0; lineCount < characterData.size(); lineCount++){
				//Check if letter matches
				if (currentLetter.equals(characterData.get(lineCount).get(0))){
					//Add line
					letterLineArray.add(characterData.get(lineCount));
					
					//Increment counter (To tell how many instances per letter
					totalLinesForLetter++;
				}
			}
			
			//All lines checked, print to file
			saveToFile(letterLineArray, currentLetter);
			
			//Print total instances of character and reset counters
			logger.println(currentLetter + ": " + totalLinesForLetter);
			logger.flush();
			totalLinesForLetter = 0;
			
			//Reset array
			letterLineArray = new ArrayList<ArrayList<String>>();
		}
	}
	
	/**
	 * ========================================================================
	 * saveToFile Method
	 * 
	 * Saves each line to one of 26 different files depending on which letter it represents.
	 * 
	 * @param lineList The list of lines to print
	 * @param newFilename The name of the file to be created
	 */
	private static void saveToFile(ArrayList<ArrayList<String>> lineList, String newFilename){
		try {
			PrintWriter writer = new PrintWriter(FILEPATH + newFilename + ".data", "UTF-8");
			
			//Write each line to the file
			for (int lineCount = 0; lineCount < lineList.size(); lineCount++){
				for (int attributeCount = 0; attributeCount < LINE_LENGTH; attributeCount++){
					writer.write(lineList.get(lineCount).get(attributeCount));
					
					//If at the last attribute, go to the next line, otherwise add a comma (Don't add new line if at last line)
					if ((lineCount < lineList.size() - 1) && (attributeCount >= LINE_LENGTH - 1)){
						writer.println();
						writer.flush();
					} else if ((attributeCount < LINE_LENGTH - 1)){
						writer.write(",");
						writer.flush();
					}
				}
		}
			
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}


