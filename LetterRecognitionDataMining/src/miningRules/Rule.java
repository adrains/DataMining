package miningRules;

public class Rule implements Cloneable {

	public static final int RULE_LENGTH = 16;
	public static final String CATEGORIES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final int UPPER_BOUND = 15;
	public static final int LOWER_BOUND = 0;
	public static final int WILD_CARD = 255;

	private int[] rule = new int[RULE_LENGTH];

	private String category = "";
	private int fuzziness = RuleGenerator.FUZZINESS;

	public Rule() {
	}

	public Rule(String rawData) {
		String[] splitData = rawData.split(",");

		category = splitData[0];

		int debug;

		if (splitData.length < RULE_LENGTH + 1)
			debug = 0;

		for (int i = 0; i < RULE_LENGTH; i++) {
			String[] ruleSplit = splitData[i + 1].split(":");
			rule[i] = Integer.parseInt(ruleSplit[0]);
		}
	}

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
		if (ruleValue == WILD_CARD) {
			rule[ruleIndex] = ruleValue;
			return true;
		} else if (ruleValue < LOWER_BOUND
				|| ruleValue > UPPER_BOUND)
			return false;
		else {
			rule[ruleIndex] = ruleValue;
			return true;
		}
	}

	public void setRuleFuzziness(int fuzziness) {
		this.fuzziness = fuzziness;
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
		output += category;

		for (int i = 0; i < rule.length; i++)
			output += "," + rule[i] + ":" + fuzziness;

		return output;
	}

	public boolean compareRule(Data data) {

		int[] ruleData = data.getData();

		for (int i = 0; i < RULE_LENGTH; i++) {
			if (rule[i] == WILD_CARD)
				continue;
			else if (ruleData[i] > rule[i] + fuzziness
					|| ruleData[i] < rule[i] - fuzziness)
				return false;
		}

		return true;
	}
}
