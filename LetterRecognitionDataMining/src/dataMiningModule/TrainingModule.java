package dataMiningModule;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import miningRules.Data;
import miningRules.HybridRuleGenerator;
import miningRules.Rule;

public class TrainingModule {

	public enum AuctionType {
		WINNER_TAKES_ALL, SHARED
	}

	private static String resourcePath = "F:\\Data Mining\\LetterRecognitionDataMining\\resources\\";

	private ArrayList<Bidder> allBidders = new ArrayList<>();
	private ArrayList<Bidder> currentBidders = new ArrayList<Bidder>();

	private int initialBidAmount = 500;

	private AuctionType currentAuction = AuctionType.SHARED;

	private PrintWriter logger;

	{
		try {
			logger = new PrintWriter(new File(resourcePath
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

			int i = 0;

			// Get the current highest bidder
			Bidder highBidder = allBidders.get(i);

			// Get the broke bidders rule
			Rule brokeBidderRule = bidder.getRule();

			// Get the high bidders rule
			Rule highBidderRule = highBidder.getRule();

			// Loop through all the bidders in descending order until a bidder
			// with a matching category is found
			while (!highBidderRule.getRuleCategory().equals(
					brokeBidderRule.getRuleCategory())) {
				highBidder = allBidders.get(++i);
				highBidderRule = highBidder.getRule();
			}

			// If the highest bidder found has a strength lower than the
			// "threshold" disregard
			if (highBidder.getBid() <= initialBidAmount)
				highBidderRule = null;

			// Get parameters from existing and highest bidder
			String category = bidder.getRule().getRuleCategory();
			Rule parent1 = bidder.getRule();
			Rule parent2 = highBidderRule;

			// Create a new rule using aforementioned parameters
			Rule newRule = HybridRuleGenerator.generateRule(category, parent1,
					parent2);

			// Create a new bidder using the new rule. Retains old bidders
			// BidType
			Bidder replacement = new Bidder(newRule, initialBidAmount,
					bidder.getBidType());

			// Place the replacement bidder back into the pool
			allBidders.add(replacement);
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

	public void bidOn(Data data) {
		currentBidders = getEligibleBidders(data);

		// TODO: Remove log line
		logger.write(String.format("%s eligible bidders\n",
				currentBidders.size()));
		logger.flush();

		if (currentBidders.size() < 1) {
			return;
		}

		// Sort the eligible bidders into descending order
		Collections.sort(currentBidders);
		Collections.reverse(currentBidders);

		// TODO: Remove log line
		String logOutput = "";

		// An ArrayList for the winning bids, the winning criterion determined
		// by the auction type
		ArrayList<Bidder> winningBids = new ArrayList<Bidder>();

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
					logOutput = String.format(
							"Winner-Takes-All\nsShared max bid: %s\n",
							winningBids.size());

				} else {
					// Otherwise check that the winning bidder made a correct
					// bid
					if (winningBidder.isCorrectBid(data)) {
						winningBids.add(winningBidder);
					}

					// TODO: Remove log line
					logOutput = String.format(
							"Winner-Takes-All\nSingle Bid, was correct: %s\n",
							winningBidder.isCorrectBid(data));
				}
			} else {
				// Winning bidder is the only bidder
				Bidder winningBidder = currentBidders.get(0);

				// Check that the winning bidder made a correct bid
				if (winningBidder.isCorrectBid(data)) {
					winningBids.add(winningBidder);
				}

				// TODO: Remove log line
				logOutput = String
						.format("Winner-Takes-All\nOne eligible bidder, was correct: %s\n",
								winningBidder.isCorrectBid(data));
			}
			break;
		case SHARED:
			for (Bidder bidder : currentBidders) {
				if (bidder.isCorrectBid(data))
					winningBids.add(bidder);
			}

			// TODO: Remove log line
			logOutput = String.format("Reward Sharing\n%s correct bidders\n",
					winningBids.size());
		default:

		}

		// Award each of the winning bidders their portion of the pot
		for (Bidder bidder : winningBids) {
			bidder.setStrength(bidder.getStrength() + initialBidAmount * 2
					/ winningBids.size());
		}

		logger.write(logOutput);
		logger.flush();

		taxSpecificBidders(currentBidders);
		taxAllBidders();
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
}
