package miningRules;

public class Rule {

	public static final int RULE_LENGTH = 16;
	public static final String CATEGORIES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final int WINDOW_UPPER_BOUND = 15;
	public static final int WINDOW_LOWER_BOUND = 0;
	public static final int WILD_CARD = 255;

	private int[] rule = new int[16];

	private String category = "";

	public int getSpecificity() {
		int specificity = 0;
		for (int x : rule) {
			if (x != WILD_CARD)
				specificity++;
		}
		return specificity;
	}

	public String getCategory() {
		return category;
	}

	public boolean setRuleValue(int ruleIndex, int ruleValue) {
		if (ruleValue == WILD_CARD) {
			rule[ruleIndex] = ruleValue;
			return true;
		} else if (ruleValue < WINDOW_LOWER_BOUND
				|| ruleValue > WINDOW_UPPER_BOUND)
			return false;
		else {
			rule[ruleIndex] = ruleValue;
			return true;
		}
	}

	public boolean setRuleCategory(String category) {
		if (CATEGORIES.contains(category)) {
			this.category = category;
			return true;
		} else
			return false;
	}
	
	@Override
	public String toString() {
		
		String output = "";
		for (int val : rule)
			output += "" + val + ",";
		
		output += category;
		
		return output;
	}
}
