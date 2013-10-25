package miningRules;

import java.util.Random;

public class RuleMutator {
	
	private static Random random = new Random();

	public static Rule randomMutation(Rule parent) {
		
		String parentString = parent.toString();
		Rule mutatedRule = new Rule(parentString);
		
		int mutationIndex = random.nextInt(Rule.RULE_LENGTH);
		int mutationValue = random.nextInt(Rule.UPPER_BOUND + 1);
		
		mutatedRule.setRuleValue(mutationIndex, mutationValue);

		return mutatedRule;
	}
	
	public static Rule crossMutation(Rule parent1, Rule parent2) {
		
		Rule mutatedRule = new Rule();
		
		int pivot = random.nextInt(Rule.RULE_LENGTH);
		
		int[] parent1Rule = parent1.getRule();
		int[] parent2Rule = parent2.getRule();
		
		for(int i = 0; i < pivot; i++) {
			mutatedRule.setRuleValue(i, parent1Rule[i]);
		}
		
		for (int i = pivot; i < Rule.RULE_LENGTH; i++) {
			mutatedRule.setRuleValue(i, parent2Rule[i]);
		}
		
		mutatedRule.setRuleCategory(parent1.getRuleCategory());
		
		return mutatedRule;
	}
}
