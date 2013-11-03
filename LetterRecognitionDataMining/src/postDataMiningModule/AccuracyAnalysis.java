package postDataMiningModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import miningRules.Data;
import miningRules.Rule;

public class AccuracyAnalysis {
	private static final String RESOURCE_PATH = "F:\\Data Mining\\LetterRecognitionDataMining\\resources\\";
	private static final String DATA = RESOURCE_PATH
			+ "Data\\letter-recognition.data";
	
	// Used to convert characters into array indices
	private final int MOD_VALUE = 'A';

	private ArrayList<Rule> ruleSet = new ArrayList<Rule>();

	private void loadRules(String rulePath) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(rulePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scanner.hasNext()) {
			ruleSet.add(new Rule(scanner.next()));
		}

		scanner.close();
	}

	/**
	 * Determines the subsection of rules that match the presented data.
	 * 
	 * @param data
	 *            The piece of data being checked
	 * @return An ArrayList of rules which are eligible classifiers
	 */
	private ArrayList<Rule> getEligibleRules(Data data) {
		ArrayList<Rule> eligibleRules = new ArrayList<Rule>();

		// Check every rule present to see if it is an eligible classifier for
		// the data
		for (Rule rule : ruleSet) {
			if (rule.compareRule(data))
				eligibleRules.add(rule);
		}
		return eligibleRules;
	}

	private int findMaxIndex(int[] values) {
		int maxVal = 0;
		int maxIndex = 0;
		
		for(int i = 0; i < values.length; i++) {
			if (maxVal < values[i]) {
				maxVal = values[i];
				maxIndex = i;
			}
		}
		
		return maxIndex;
	}
	
	private String findWeightedFavourite(ArrayList<Rule> ruleSubset) {
		int[] results = new int[26];
		
		for (Rule rule : ruleSubset) {
			results[rule.getRuleCategory().charAt(0) % MOD_VALUE] += rule.getSpecificity();
		}
		
		char result = (char) (findMaxIndex(results) + MOD_VALUE);
		
		return Character.toString(result);
	}
	
	public void determineAccuracy(String rulePath) {
		loadRules(rulePath);

		Scanner scanner = null;

		try {
			scanner = new Scanner(new File(DATA));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		int total = 0;
		int correctClassifications = 0;
		
		while(scanner.hasNext()) {
			ArrayList<Rule> accurateRules = new ArrayList<Rule>();
			Data data = new Data(scanner.next());
			
			accurateRules = getEligibleRules(data);
			
			String favouriteResult = findWeightedFavourite(accurateRules);
			
			if (favouriteResult.equals(data.getDataCategory()))
				correctClassifications++;
			
			total++;
			double percent = (double) correctClassifications / (double) total * 100.0;
			
			
			System.out.println(String.format("Total classifications: %4d\tCorrect classifications: %4d\tAccuracy: %2.2f", total, correctClassifications, percent));
		}
	}
	
	public static void main(String[] args) {
		String rulePath = "F:\\Mining Results\\Verification\\Hybrid.results";
		
		AccuracyAnalysis test = new AccuracyAnalysis();
		
		test.determineAccuracy(rulePath);
	}
}
