package miningRules;

public class ExemplarRuleGenerator implements RuleGenerator {
	public static final double WILD_CARD_CHANCE = 0.05;
	
	private static final int NUMBER_OF_RULES = 2;

	public static Rule[] generateRule(Data data) {
		Rule[] rules = new Rule[NUMBER_OF_RULES];

		int[] dataClassifier = data.getData();

		for (int i = 0; i < rules.length; i++) {
			rules[i] = createRule(dataClassifier);
			rules[i].setRuleCategory(data.getDataCategory());
		}

		return rules;
	}

	private static Rule createRule(int[] ruleSet) {
		Rule rule = new Rule();

		for (int i = 0; i < RULE_LENGTH; i++) {
			double wildCard = random.nextDouble();

			if (wildCard <= WILD_CARD_CHANCE)
				rule.setRuleValue(i, WILD_CARD);
			else
				rule.setRuleValue(i, ruleSet[i]);
		}

		return rule;
	}
}
