package miningRules;

import java.util.Random;

public class RandomRuleGeneration implements RuleGenerator {

	private final int RULE_LENGTH = Rule.RULE_LENGTH;
	private final String CATEGORIES = Rule.CATEGORIES;
	
	private final int UPPER_BOUND = Rule.WINDOW_UPPER_BOUND + 2;
	
	private final int WILD_CARD = Rule.WILD_CARD;
	
	private Random random = new Random();

	@Override
	public Rule generateRule() {
		Rule rule = new Rule();

		for (int i = 0; i < RULE_LENGTH; i++) {
			int ruleVal = random.nextInt(UPPER_BOUND);
			if (ruleVal > UPPER_BOUND - 2)
				rule.setRuleValue(i, WILD_CARD);
			else
				rule.setRuleValue(i, ruleVal);
		}
		
		int category = random.nextInt(CATEGORIES.length());
		
		if (category == CATEGORIES.length() - 1)
			rule.setRuleCategory(CATEGORIES.substring(category));
		else
			rule.setRuleCategory(CATEGORIES.substring(category, category+1));
		
		return rule;
	}

}
