package preDataMiningModule;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Preprocessor {
	static String filepath = "F:\\Data Mining\\LetterRecognitionDataMining\\resources\\";
	static String filename = "letter-recognition.data";
	
	static ArrayList<ArrayList<String>> characterData;
	static int LINE_LENGTH = 17;
	static int ATTRIBUTE_LOW = 0;
	static int ATTRIBUTE_HIGH = 15;
	
	/**
	 * ========================================================================
	 * readFile Method
	 * 
	 * @return The cleaned character data array with invalid entries removed
	 */
	public static ArrayList<ArrayList<String>> preprocessData(){
		//Import data
		characterData = readFile();
		
		//Ensure first character on each line is the letter representing the data
		checkForLetters();
		
		//Ensure attribute list only contains numbers and they are within the correct range
		checkAttributes();
		
		//Data has been cleaned, return
		System.out.println("Total entries remaining after cleaning: " + characterData.size());
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
			BufferedReader reader = new BufferedReader(new FileReader(filepath + filename));
			
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
		System.out.println("Number of character entries: " + characterDataArray.size());
		
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
		System.out.println("Lines with letters: " + finalLineCount + "/" + startingLineCount);
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
		System.out.println("Lines with correct number of integer attributes within bounds: " + finalLineCount + "/" + startingLineCount);
	}
}


