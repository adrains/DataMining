package dataMiningModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import miningRules.Data;
import miningRules.ExemplarRuleGenerator;
import miningRules.HybridRuleGenerator;
import miningRules.Rule;

public class TrainingModule {

	public enum AuctionType {
		WINNER_TAKES_ALL, SHARED
	}

	public enum RuleType {
		EXEMPLAR, HYBRID, VERIFICATION
	}

	private static final String RESOURCE_PATH = "F:\\Data Mining\\LetterRecognitionDataMining\\resources\\";
	private static final String TRAINING_DATA = RESOURCE_PATH
			+ "Data\\training.data";

	private ArrayList<Bidder> allBidders = new ArrayList<Bidder>();
	private ArrayList<Bidder> currentBidders = new ArrayList<Bidder>();

	private int initialBidAmount = 1000;

	private final int NUMBER_OF_ITERATIONS = 50;

	private AuctionType currentAuction = AuctionType.SHARED;
	private RuleType currentRuleType = RuleType.HYBRID;

	private PrintWriter logger;

	{
		try {
			logger = new PrintWriter(new File(RESOURCE_PATH
					+ "trainingOutput.log"));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Adds a Bidder to the AuctionHouse's bidder list.
	 * 
	 * @param bidder
	 *            The Bidder to be added to the AH list
	 */
	public void addBidder(Bidder bidder) {
		allBidders.add(bidder);
	}

	/**
	 * Sets the current auction type.
	 * 
	 * @param auctionType
	 *            The new auction type to be used
	 */
	public void setAuctionType(AuctionType auctionType) {
		this.currentAuction = auctionType;
	}

	/**
	 * Sets the current rule type.
	 * 
	 * @param auctionType
	 *            The new rule type to be used
	 */
	public void setRuleType(RuleType ruleType) {
		this.currentRuleType = ruleType;
	}

	/**
	 * Check every bidder to see if any have fallen below the performance
	 * threshold. If a bidder has fallen below, it is removed and a new bidder
	 * generated in its place.
	 */
	private void assessAllBidders() {
		if (allBidders.size() < 1)
			return;

		// Sort bidders into descending order of strength
		Collections.sort(allBidders);
		Collections.reverse(allBidders);

		ArrayList<Bidder> brokeBidders = new ArrayList<Bidder>();

		for (Bidder bidder : allBidders) {
			if (bidder.getStrength() <= 1)
				brokeBidders.add(bidder);
		}

		for (Bidder bidder : brokeBidders) {
			allBidders.remove(bidder);
			switch (currentRuleType) {
			case EXEMPLAR:
			case VERIFICATION:
				break;
			case HYBRID:
			default:
				int i = 0;

				// Get the current highest bidder
				Bidder highBidder = allBidders.get(i);

				// Get the broke bidders rule
				Rule brokeBidderRule = bidder.getRule();

				// Get the high bidders rule
				Rule highBidderRule = highBidder.getRule();

				// Loop through all the bidders in descending order until a
				// bidder with a matching category is found
				while (!highBidderRule.getRuleCategory().equals(
						brokeBidderRule.getRuleCategory())) {
					highBidder = allBidders.get(++i);
					highBidderRule = highBidder.getRule();
				}

				// Do the same for the second highest bidder

				// Get the current highest bidder
				Bidder secondHighBidder = allBidders.get(++i);

				// Get the high bidders rule
				Rule secondHighBidderRule = highBidder.getRule();

				// Loop through all the bidders in descending order until a
				// bidder with a matching category is found
				while (!secondHighBidderRule.getRuleCategory().equals(
						brokeBidderRule.getRuleCategory())) {
					secondHighBidder = allBidders.get(++i);
					secondHighBidderRule = secondHighBidder.getRule();
				}

				// If the highest bidder found has a strength lower than the
				// "threshold" disregard
				if (secondHighBidder.getBid() <= initialBidAmount)
					secondHighBidderRule = null;

				// Get parameters from existing and highest bidder
				String category = bidder.getRule().getRuleCategory();
				Rule parent1 = highBidderRule;
				Rule parent2 = secondHighBidderRule;

				// Create a new rule using aforementioned parameters
				Rule newRule = HybridRuleGenerator.generateRule(category,
						parent1, parent2);

				// Create a new bidder using the new rule. Retains old bidders
				// BidType
				Bidder replacement = new Bidder(newRule, initialBidAmount,
						bidder.getBidType());

				// Place the replacement bidder back into the pool
				allBidders.add(replacement);
			}
		}

	}

	/**
	 * Determines the subsection of bidders that have rules matching the
	 * presented data.
	 * 
	 * @param data
	 *            The piece of data being checked
	 * @return An ArrayList of Bidders who have eligible classifiers
	 */
	private ArrayList<Bidder> getEligibleBidders(Data data) {
		ArrayList<Bidder> eligibleBidders = new ArrayList<Bidder>();

		// Check every bidder present to see if it is eligible to bid on the
		// data
		for (Bidder bidder : allBidders) {
			if (bidder.isValidBidder(data))
				eligibleBidders.add(bidder);
		}
		return eligibleBidders;
	}

	/**
	 * 
	 * @param data
	 */
	private void bidOn(Data data) {
		ArrayList<Bidder> winningBids = determineWinningBids(data);

		if (currentRuleType == RuleType.EXEMPLAR) {
			// If none of the winners advocated the correct category, generate
			// new rules.
			if (winningBids.size() < 1) {
				Rule[] newRules = ExemplarRuleGenerator.generateRule(data);
				for (Rule rule : newRules) {
					allBidders.add(new Bidder(rule, initialBidAmount));
				}
				return;
			}
		}

		payWinners(winningBids);

		taxSpecificBidders(currentBidders);
		taxAllBidders();
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	private ArrayList<Bidder> determineWinningBids(Data data) {
		// An ArrayList for the winning bids, the winning criterion determined
		// by the auction type
		ArrayList<Bidder> winningBids = new ArrayList<Bidder>();

		currentBidders = getEligibleBidders(data);

		// TODO: Remove log line
		logger.write(String.format("%s eligible bidders\n",
				currentBidders.size()));
		logger.flush();

		// If there are no eligible bidders, act based on the current rule type.
		if (currentBidders.size() < 1) {
			switch (currentRuleType) {
			case EXEMPLAR:
				// If Exemplar, generate new rules
				Rule[] newRules = ExemplarRuleGenerator.generateRule(data);
				for (Rule rule : newRules) {
					allBidders.add(new Bidder(rule, initialBidAmount));
				}
				break;

			case HYBRID:
			case VERIFICATION:
				// If hybrid or otherwise, tax all the bidders
			default:
				taxAllBidders();
			}

			return winningBids;
		}

		// Sort the eligible bidders into descending order
		Collections.sort(currentBidders);
		Collections.reverse(currentBidders);

		switch (currentAuction) {
		case WINNER_TAKES_ALL:
			if (currentBidders.size() > 1) {

				// Winning bidder is the bidder with the highest bid
				Bidder winningBidder = currentBidders.get(0);
				int winningBid = winningBidder.getBid();

				// Get details for the second highest bidder
				Bidder secondHighestBidder = currentBidders.get(1);
				int secondBid = secondHighestBidder.getBid();

				if (winningBid == secondBid) {
					// If the highest bid is shared/matched, find all matching
					// bids that are correct
					for (Bidder bidder : currentBidders) {
						if (winningBid == bidder.getBid()
								&& bidder.isCorrectBid(data))
							winningBids.add(bidder);
					}

					// TODO: Remove log line
					logger.write(String.format(
							"Winner-Takes-All\nsShared max bid: %s\n",
							winningBids.size()));
					logger.flush();

				} else {
					// Otherwise check that the winning bidder made a correct
					// bid
					if (winningBidder.isCorrectBid(data)) {
						winningBids.add(winningBidder);
					}

					// TODO: Remove log line
					logger.write(String.format(
							"Winner-Takes-All\nSingle Bid, was correct: %s\n",
							winningBidder.isCorrectBid(data)));
					logger.flush();
				}
			} else {
				// Winning bidder is the only bidder
				Bidder winningBidder = currentBidders.get(0);

				// Check that the winning bidder made a correct bid
				if (winningBidder.isCorrectBid(data)) {
					winningBids.add(winningBidder);
				}

				// TODO: Remove log line
				logger.write(String
						.format("Winner-Takes-All\nOne eligible bidder, was correct: %s\n",
								winningBidder.isCorrectBid(data)));
				logger.flush();
			}
			break;
		case SHARED:
			for (Bidder bidder : currentBidders) {
				if (bidder.isCorrectBid(data))
					winningBids.add(bidder);
			}

			// TODO: Remove log line
			logger.write(String.format("Reward Sharing\n%s correct bidders\n",
					winningBids.size()));
			logger.flush();
		default:

		}

		return winningBids;
	}

	/**
	 * Loops through the list of bidders and pays them according to the payout
	 * scheme.
	 * 
	 * Currently: Bidder.strength + 2 * initialBidAmount / number of winners
	 * 
	 * @param winningBids
	 *            An ArrayList contain all the bidders to be paid
	 */
	private void payWinners(ArrayList<Bidder> winningBids) {
		// Award each of the winning bidders their portion of the pot
		for (Bidder bidder : winningBids) {
			bidder.setStrength(bidder.getStrength() + initialBidAmount * 2
					/ winningBids.size());
		}
	}

	/**
	 * Deducts a fixed amount from each bidders strength. Is used as a form of
	 * depreciation on all bidders to remove any bidder that isn't making bids.
	 */
	private void taxAllBidders() {
		for (Bidder bidder : allBidders) {
			bidder.setStrength(bidder.getStrength() - 1);
		}

		// Assess all the bidders to see if they should be removed after taxing
		assessAllBidders();
	}

	/**
	 * Charges each bidder a fixed amount to participate in the auction.
	 * 
	 * @param bidders
	 *            An ArrayList of bidders to be charged
	 */
	private void taxSpecificBidders(ArrayList<Bidder> bidders) {
		for (Bidder bidder : bidders) {
			switch (bidder.getBidType()) {
			case SPECIFICITY:
				bidder.setStrength((int) (bidder.getStrength() - bidder
						.getBid() * 0.015));
			case STRENGTH:
			default:
				bidder.setStrength((int) (bidder.getStrength() - bidder
						.getBid() * 0.1));
			}
		}
	}

	/**
	 * Loads bidders into the system from the specified file containing a list
	 * of rules.
	 * 
	 * @param rulePath
	 *            The file path of the rules to be loaded
	 */
	private void loadBidders(String rulePath) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(rulePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scanner.hasNext()) {
			addBidder(new Bidder(new Rule(scanner.next()), initialBidAmount));
		}

		scanner.close();
	}

	/**
	 * Default training method. Performs rule generation/mutation dependent on
	 * the rule type being employed.
	 * 
	 * @param outputPath
	 *            The file path of the output files.
	 */
	private void training() {
		PrintWriter bidOut = null;
		PrintWriter ruleOut = null;

		String resultsPath = "";

		switch (currentRuleType) {
		case EXEMPLAR:
			allBidders = new ArrayList<Bidder>();
			resultsPath = "F:\\Mining Results\\Exemplar\\";
			break;
		case VERIFICATION:
			allBidders = new ArrayList<Bidder>();
			resultsPath = "F:\\Mining Results\\Verification\\";
			loadBidders(resultsPath + "Hybrid.results");
			break;
		case HYBRID:
		default:
			allBidders = new ArrayList<Bidder>();
			resultsPath = "F:\\Mining Results\\Hybrid\\";
			loadBidders(RESOURCE_PATH + "RandomRules.rules");
		}

		int iterationCounter = 0;
		int outputCount = 0;

		Scanner scanner = null;

		while (true) {
			try {
				scanner = new Scanner(new File(TRAINING_DATA));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			while (scanner.hasNext()) {
				bidOn(new Data(scanner.next()));
			}

			if (iterationCounter++ % NUMBER_OF_ITERATIONS == 0) {
				try {
					bidOut = new PrintWriter(new File(String.format(
							"%sBids\\BidOutput%s.results", resultsPath,
							outputCount)));
					ruleOut = new PrintWriter(new File(String.format(
							"%sRules\\RuleOutput%s.results", resultsPath,
							outputCount++)));
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Sort the all bidders into descending order
				Collections.sort(allBidders);
				Collections.reverse(allBidders);

				for (Bidder bidder : allBidders) {
					bidOut.write(bidder.toString() + "\n");
					bidOut.flush();
					ruleOut.write(bidder.getRule().toString() + "\n");
					ruleOut.flush();
				}

				bidOut.close();
				ruleOut.close();
			}

			if (outputCount > 5)
				break;
		}

	}

	public static void main(String[] args) {
		TrainingModule training = new TrainingModule();

		//training.setRuleType(RuleType.HYBRID);
		
		training.setRuleType(RuleType.VERIFICATION);	
		
		training.training();
		
	}

}
