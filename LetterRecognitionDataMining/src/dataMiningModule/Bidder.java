package dataMiningModule;

import miningRules.Data;
import miningRules.Rule;

public class Bidder implements Comparable<Bidder> {

	public enum BidType {
		STRENGTH, SPECIFICITY
	}

	private int strength;
	private Rule rule;
	private BidType bidType;

	public Bidder(Rule rule, int initialStrength) {
		this.rule = rule;
		this.strength = initialStrength;
		this.bidType = BidType.STRENGTH;
	}
	
	public Bidder(Rule rule, int initialStrength, BidType bidType) {
		this.rule = rule;
		this.strength = initialStrength;
		this.bidType = bidType;
	}

	public Rule getRule() {
		return rule;
	}

	public BidType getBidType() {
		return bidType;
	}
	
	/**
	 * Returns the current bid of the Bidder. Calculation varies depending on
	 * bidding method.
	 * 
	 * @return The current bid value of the Bidder
	 */
	public int getBid() {
		switch (bidType) {
		case SPECIFICITY:
			return strength * rule.getSpecificity();
		case STRENGTH:
		default:
			return strength;
		}
	}

	/**
	 * Sets the strength value of the Bidder.
	 * 
	 * @param value
	 *            New strength value
	 */
	public void setStrength(int value) {
		strength = value;
	}

	/**
	 * Returns the current strength of the bidder.
	 * 
	 * @return The current strength value of the bidder
	 */
	public int getStrength() {
		return strength;
	}

	/**
	 * Checks the Bidder's rule to see if it matches the provided data.
	 * 
	 * @param data
	 *            Data to be checked against
	 * @return A boolean representing whether or not the rule matches the
	 *         provided data
	 */
	public boolean isValidBidder(Data data) {
		return rule.compareRule(data);
	}

	/**
	 * Checks the data to see if the rule's category matches that of the
	 * data/training set.
	 * 
	 * @param data
	 *            Data to be checked against
	 * @return A boolean representing whether or not the rule category and data
	 *         category match
	 */
	public boolean isCorrectBid(Data data) {
		return rule.getRuleCategory().equals(data.getDataCategory());
	}

	/**
	 * Compares to bidders strengths to determine which is higher.
	 */
	@Override
	public int compareTo(Bidder o) {
		if (o.getBid() > getBid())
			return -1;
		else if (o.getBid() < getBid())
			return 1;
		else
			return 0;
	}

	@Override
	public String toString() {
		return String.format("Bid: %s\tRule: %s", strength, rule);
	}

}
