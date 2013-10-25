package dataMiningModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import miningRules.Data;
import miningRules.RandomRuleGenerator;
import miningRules.Rule;
import miningRules.RuleGenerator;

public class AuctionHouse {

	private static String resourcePath = "F:\\Data Mining\\LetterRecognitionDataMining\\resources\\";

	public ArrayList<Bidder> allBidders = new ArrayList<>();

	private ArrayList<Bidder> currentBidders = new ArrayList<Bidder>();

	private int initialBidAmount = 5000;

	public PrintWriter logger;

	{
		try {
			logger = new PrintWriter(new File(resourcePath + "BidOutput.log"));
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
	 * Check every bidder to see if any have fallen below the performance
	 * threshold. If a bidder has fallen below, it is removed and a new bidder
	 * generated in its place.
	 */
	private void assessAllBidders() {
		if (allBidders.size() < 1)
			return;

		ArrayList<Bidder> brokeBidders = new ArrayList<Bidder>();

		for (Bidder bidder : allBidders) {
			if (bidder.getStrength() <= 1)
				brokeBidders.add(bidder);
		}

		RuleGenerator generator = new RandomRuleGenerator();

		for (Bidder bidder : brokeBidders) {
			allBidders.remove(bidder);
			allBidders.add(new Bidder(generator.generateRule(bidder.getRule()
					.getRuleCategory())));
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

		logger.write(String.format("%s eligible bidders\n",
				currentBidders.size()));
		logger.flush();

		if (currentBidders.size() < 1) {
			return;
		}

		Collections.sort(currentBidders);
		Collections.reverse(currentBidders);

		Bidder winningBid = currentBidders.get(0);

		String logOutput = "";

		if (currentBidders.size() > 1) {
			if (winningBid.getBid() == currentBidders.get(1).getBid()) {
				winningBid = null;
			}
		}

		if (winningBid != null && winningBid.isCorrectBid(data)) {
			winningBid.setStrength(winningBid.getStrength() + initialBidAmount
					* 2);
			logOutput = String.format("Winning Bid\nWinner: %s\nData: %s\n",
					winningBid, data);
		} else {
			int correctBids = 0;
			for (Bidder bidder : currentBidders) {

				if (bidder.isCorrectBid(data)) {
					correctBids++;
				}
			}

			logOutput = String.format("Split bids\n%s Winners\n", correctBids);

			if (correctBids != 0) {
				for (Bidder bidder : currentBidders) {
					if (bidder.isCorrectBid(data)) {
						bidder.setStrength(bidder.getStrength()
								+ initialBidAmount * 2 / correctBids);
					}
				}
			}
		}

		logger.write(logOutput);
		logger.flush();

		taxAllBidders();
	}

	/**
	 * Deducts a fixed amount from each bidders strength. Is used as a form of
	 * depreciation on all bidders to remove any bidder that isn't making bids.
	 */
	private void taxAllBidders() {
		for (Bidder bidder : allBidders) {
			bidder.setStrength((int) (bidder.getStrength() - 1));
		}
		assessAllBidders();
	}

	public static void main(String[] args) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(resourcePath
					+ "Rules\\randomRules.rules"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AuctionHouse AH = new AuctionHouse();

		while (scanner.hasNext()) {
			AH.addBidder(new Bidder(new Rule(scanner.next())));
		}

		for (int i = 0; i < 10; i++) {
			try {
				scanner = new Scanner(new File(resourcePath
						+ "Data\\letter-recognition.data"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			while (scanner.hasNext()) {
				AH.bidOn(new Data(scanner.next()));
			}

			System.out.println(AH.allBidders.size());
		}
		for (Bidder bidder : AH.allBidders) {
			AH.logger.write(bidder.toString() + "\n");
			AH.logger.flush();
		}
	}
}
