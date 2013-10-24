package dataMiningModule;

import miningRules.Data;
import miningRules.Rule;

public class Bidder implements Comparable<Bidder> {
	private int currentBidValue = 5000;
	
	private Rule rule;
	
	public Bidder(Rule rule) {
		this.rule = rule;
	}
	
	public Rule getRule() {
		return rule;
	}
	
	public int getBid() {
		return currentBidValue;
	}

	public void setBid(int value) {
		currentBidValue = value;
	}
	
	public boolean checkBid(Data data) {
		return rule.compare(data);
	}

	@Override
	public int compareTo(Bidder o) {
		if (o.getBid() > currentBidValue)
			return -1;
		else if (o.getBid() < currentBidValue)
			return 1;
		else
			return 0;
	}
	
	@Override
	public String toString() {
		return String.format("Bid: %s\tRule: %s",currentBidValue,rule);
	}

}
