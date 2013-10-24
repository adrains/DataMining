package dataMiningModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import miningRules.Data;
import miningRules.Rule;

public class AuctionHouse {

	private static String resourcePath = "F:\\Data Mining\\LetterRecognitionDataMining\\resources\\";

	private ArrayList<Bidder> bidders = new ArrayList<>();

	private int initialBidAmount = 4000;
	
	private Logger logger = Logger.getLogger("AuctionHouse");
	
	{
		try {
			logger.addHandler(new FileHandler("BidOutput.txt"));
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

		for (Bidder bidder : bidders) {
			if (bidder.getBid() < 1)
				bidders.remove(bidder);
		}
	}

	public void bidOn(Data data) {
		taxBids();

		Collections.sort(bidders);
		Bidder winningBid = bidders.get(0);

		if (winningBid.checkBid(data) && winningBid.getBid() != bidders.get(1).getBid()) {
			winningBid.setBid(winningBid.getBid() + initialBidAmount);
			logger.log(Level.INFO, String.format("Winning Bid\nWinner: %s\nData: %s\n", winningBid,data));
		} else {
			int correctBids = 0;
			for (Bidder bidder : bidders) {
				if (bidder.checkBid(data)) {
					correctBids++;
				}
			}
			logger.log(Level.INFO, String.format("Split bids\n%s Winners\n",correctBids));
			for (Bidder bidder : bidders) {
				if (bidder.checkBid(data)) {
					bidder.setBid(bidder.getBid() + initialBidAmount
							/ correctBids);
				}
			}
		}
	}

	private void taxBids() {
		for (Bidder bidder : bidders) {
			bidder.setBid((int) (bidder.getBid() * 0.9));
		}

		assessBidders();
	}

	public static void main(String[] args) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(resourcePath + "RandomRules.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AuctionHouse AH = new AuctionHouse();

		while (scanner.hasNext()) {
			AH.addBidder(new Bidder(new Rule(scanner.next())));
		}

		try {
			scanner = new Scanner(new File(resourcePath
					+ "letter-recognition.data"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (scanner.hasNext()) {
			AH.bidOn(new Data(scanner.next()));
		}
	}
}
