package miningRules;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RandomRuleGeneration implements RuleGenerator {

	@Override
	public Rule generateRule(String category) {
		Rule rule = new Rule();

		for (int i = 0; i < RULE_LENGTH; i++) {
			int ruleVal = random.nextInt(UPPER_BOUND + 1);
			rule.setRuleValue(i, ruleVal);

			int weightVal = random.nextInt(UPPER_BOUND + 2);
			
			if (weightVal > UPPER_BOUND - 2)
				rule.setRuleWeight(i, WILD_CARD);
			else
				rule.setRuleWeight(i, weightVal);
		}

		rule.setRuleCategory(category);

		return rule;
	}

	public static void main(String[] args) {
		RuleGenerator generator = new RandomRuleGeneration();

		File output = new File(
				"F:\\Data Mining\\LetterRecognitionDataMining\\resources\\RandomRules.txt");
		FileWriter fw = null;
		try {
			fw = new FileWriter(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < generator.CATEGORIES.length(); i++) {
			for (int j = 0; j < 300; j++) {
				String out;
				if (i == generator.CATEGORIES.length() - 1)
					out = generator.generateRule(CATEGORIES.substring(i))
							.toString();
				else
					out = generator
							.generateRule(CATEGORIES.substring(i, i + 1))
							.toString();

				try {
					fw.write(out + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
