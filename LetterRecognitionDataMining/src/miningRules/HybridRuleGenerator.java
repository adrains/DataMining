package miningRules;

public class HybridRuleGenerator implements RuleGenerator {
	
	private static final double RANDOM_RULE_PERCENT = 0.2;
	private static final double MUTATION_RULE_PERCENT = 0.6;
	private static final double CROSSOVER_RULE_PERCENT = 0.2;

	public static Rule generateRule(String category) {
		return RandomRuleGenerator.generateRule(category);
	}
	
	public static Rule generateRule(String category, Rule parent1, Rule parent2) {
		
		if (parent1 == null) {
			return RandomRuleGenerator.generateRule(category);
		}
		
		double ruleChoice = random.nextDouble();
		
		if (ruleChoice < RANDOM_RULE_PERCENT) {
			return RandomRuleGenerator.generateRule(category);
		} else if (parent2 == null || ruleChoice < RANDOM_RULE_PERCENT + MUTATION_RULE_PERCENT) {
			return RuleMutator.randomMutation(parent1);
		} else {
			return RuleMutator.crossMutation(parent1, parent2);
		}
	}

}
