package miningRules;

import java.util.Random;

public interface RuleGenerator {
	public final int RULE_LENGTH = Rule.RULE_LENGTH;
	public final String CATEGORIES = Rule.CATEGORIES;

	public final int UPPER_BOUND = Rule.UPPER_BOUND;

	public final int WILD_CARD = Rule.WILD_CARD;
	
	public final int FUZZINESS = 3;

	public Random random = new Random();
}
