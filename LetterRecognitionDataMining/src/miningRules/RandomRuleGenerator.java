package miningRules;

import java.io.FileWriter;
import java.io.IOException;

public class RandomRuleGenerator implements RuleGenerator {
	
	private final int FUZZINESS = 5;

	@Override
	public Rule generateRule(String category) {
		Rule rule = new Rule();

		for (int i = 0; i < RULE_LENGTH; i++) {
			int ruleVal = random.nextInt(UPPER_BOUND + 2);
			
			if (ruleVal > UPPER_BOUND)
				rule.setRuleValue(i, WILD_CARD);
			else
				rule.setRuleValue(i, ruleVal);
		}
		
		rule.setRuleFuzziness(FUZZINESS);
		rule.setRuleCategory(category);

		return rule;
	}

	public static void main(String[] args) {
		RuleGenerator generator = new RandomRuleGenerator();

		String outputFile = "F:\\Data Mining\\LetterRecognitionDataMining\\resources\\Rules\\";
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(outputFile + "randomRules.rules");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < generator.CATEGORIES.length(); i++) {
			String category;

			if (i == generator.CATEGORIES.length() - 1)
				category = CATEGORIES.substring(i);
			else
				category = CATEGORIES.substring(i, i + 1);

			for (int j = 0; j < 300; j++) {
				String out = generator.generateRule(category).toString();

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
