package dataMiningModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import miningRules.Data;
import miningRules.Rule;

public class AuctionHouse {

	private static String resourcePath = "F:\\Data Mining\\LetterRecognitionDataMining\\resources\\";

	public ArrayList<Bidder> bidders = new ArrayList<>();

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

	public void addBidder(Bidder bidder) {
		bidders.add(bidder);
	}

	public void assessBidders() {
		if (bidders.size() < 1)
			return;

		ArrayList<Bidder> brokeBidders = new ArrayList<Bidder>();

		for (Bidder bidder : bidders) {
			if (bidder.getBid() < 1)
				brokeBidders.add(bidder);
		}

		for (Bidder bidder : brokeBidders) {
			if (bidder.getBid() < 1)
				bidders.remove(bidder);
		}

	}

	public void bidOn(Data data) {
		taxBids();

		Collections.sort(bidders);
		Collections.reverse(bidders);
		
		Bidder winningBid = bidders.get(0);

		String logOutput = "";

		if (bidders.size() > 1) {
			if (winningBid.getBid() == bidders.get(1).getBid()) {
				winningBid = null;
			}
		}

		if (winningBid != null && winningBid.checkBid(data)) {
			winningBid.setBid(winningBid.getBid() + initialBidAmount);
			logOutput = String.format("Winning Bid\nWinner: %s\nData: %s\n",
					winningBid, data);
		} else {
			int correctBids = 0;
			for (Bidder bidder : bidders) {

				if (bidder.checkBid(data)) {
					correctBids++;
				}
			}

			logOutput = String.format("Split bids\n%s Winners\n", correctBids);

			for (Bidder bidder : bidders) {
				if (bidder.checkBid(data)) {
					bidder.setBid(bidder.getStrength() + initialBidAmount
							/ correctBids);
				}
			}
		}

		logger.write(logOutput);
		logger.flush();
	}

	private void taxBids() {
		for (Bidder bidder : bidders) {
			bidder.setBid((int) (bidder.getStrength() * 0.9));
		}

		assessBidders();
	}

	public static void main(String[] args) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(resourcePath + "Rules\\A.rules"));
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
				scanner = new Scanner(new File(resourcePath + "Data\\A.data"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			while (scanner.hasNext()) {
				AH.bidOn(new Data(scanner.next()));
			}

			System.out.println(AH.bidders.size());
		}
		for (Bidder bidder : AH.bidders) {
			AH.logger.write(bidder.toString() + "\n");
			AH.logger.flush();
		}
	}
}
