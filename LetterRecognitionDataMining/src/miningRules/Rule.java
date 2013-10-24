package miningRules;

public class Rule {

	public static final int RULE_LENGTH = 16;
	public static final String CATEGORIES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final int WEIGHT_UPPER_BOUND = RULE_LENGTH - 1;
	public static final int WEIGHT_LOWER_BOUND = 0;
	public static final int WILD_CARD = 255;

	private int[] rule = new int[RULE_LENGTH];
	
	private int[] ruleWeight = new int[RULE_LENGTH];

	private String category = "";

	public int getSpecificity() {
		int specificity = 0;
		for (int x : rule) {
			if (x != WILD_CARD)
				specificity++;
		}
		return specificity;
	}

	public String getRuleCategory() {
		return category;
	}
	
	public int[] getRule() {
		return rule;
	}

	public boolean setRuleValue(int ruleIndex, int ruleValue) {
			if (ruleValue < WEIGHT_LOWER_BOUND
				|| ruleValue > WEIGHT_UPPER_BOUND)
			return false;
		else {
			rule[ruleIndex] = ruleValue;
			return true;
		}
	}
	
	public boolean setRuleWeight(int weightIndex, int weightValue) {
		if (weightValue == WILD_CARD) {
			ruleWeight[weightIndex] = weightValue;
			return true;
		} else if (weightValue < WEIGHT_LOWER_BOUND
				|| weightValue > WEIGHT_UPPER_BOUND)
			return false;
		else {
			ruleWeight[weightIndex] = weightValue;
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
		for (int i = 0; i < rule.length; i++)
			output += "" + rule[i] + ":" + ruleWeight[i] + ",";
		
		output += category;
		
		return output;
	}
	
	public boolean compare(Data data) {
		
		int[] ruleData = data.getData();
		
		for (int i = 0; i < RULE_LENGTH; i++) {
			if (ruleWeight[i] == WILD_CARD)
				continue;
			else if (ruleData[i] > rule[i] + ruleWeight[i] || ruleData[i] < rule[i] - ruleWeight[i])
				return false;	
		}
		
		if (!data.getDataCategory().equals(category))
			return false;
		
		return true;
	}
}
