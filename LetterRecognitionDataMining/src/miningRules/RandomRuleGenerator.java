package miningRules;

import java.io.FileWriter;
import java.io.IOException;

public class RandomRuleGenerator implements RuleGenerator {
	
	public static Rule generateRule(String category) {
		Rule rule = new Rule();

		for (int i = 0; i < RULE_LENGTH; i++) {
			int ruleVal = random.nextInt(UPPER_BOUND + 1);
			double wildCardChance = random.nextDouble();
			
			if (wildCardChance <= WILD_CARD_CHANCE)
				rule.setRuleValue(i, WILD_CARD);
			else
				rule.setRuleValue(i, ruleVal);
		}
		
		rule.setRuleFuzziness(FUZZINESS);
		rule.setRuleCategory(category);

		return rule;
	}

	public static void main(String[] args) {
		String outputFile = "F:\\Data Mining\\LetterRecognitionDataMining\\resources\\";
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(outputFile + "RandomRules.rules");
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

			for (int j = 0; j < 200; j++) {
				String out = RandomRuleGenerator.generateRule(category).toString();

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
