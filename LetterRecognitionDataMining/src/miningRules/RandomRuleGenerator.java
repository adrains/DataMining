package miningRules;

import java.io.FileWriter;
import java.io.IOException;

public class RandomRuleGenerator implements RuleGenerator {
	
	private static final int NUMBER_OF_RULES = 200;
	
	public static Rule generateRule(String category, int fuzziness) {
		Rule rule = new Rule();

		for (int i = 0; i < RULE_LENGTH; i++) {
			int ruleVal = random.nextInt(UPPER_BOUND + 1);
			double wildCardChance = random.nextDouble();
			
			if (wildCardChance <= WILD_CARD_CHANCE)
				rule.setRuleValue(i, WILD_CARD);
			else
				rule.setRuleValue(i, ruleVal);
		}
		
		rule.setRuleFuzziness(fuzziness);
		rule.setRuleCategory(category);

		return rule;
	}

	public static void main(String[] args) {
		String outputFile = "F:\\Data Mining\\LetterRecognitionDataMining\\resources\\";
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(String.format("%sRandomRulesW%s.rules", outputFile, FUZZINESS));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < RandomRuleGenerator.CATEGORIES.length(); i++) {
			String category;

			if (i == RandomRuleGenerator.CATEGORIES.length() - 1)
				category = CATEGORIES.substring(i);
			else
				category = CATEGORIES.substring(i, i + 1);

			for (int j = 0; j < NUMBER_OF_RULES; j++) {
				String out = RandomRuleGenerator.generateRule(category, FUZZINESS).toString();

				try {
					fw.write(out + "\n");
					fw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
