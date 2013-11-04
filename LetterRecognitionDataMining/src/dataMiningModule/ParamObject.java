package dataMiningModule;

import dataMiningModule.TrainingModule.AuctionType;
import dataMiningModule.TrainingModule.RuleType;

public class ParamObject {
	
	private TrainingModule.RuleType ruleType = RuleType.HYBRID;
	private TrainingModule.AuctionType auctionType = AuctionType.SHARED;
	
	private int fuzziness = 0;
	private int numberOfIteration = 10;
	
	private String trainingDataFilePath = "";
	private String ruleDataFilePath = "";
	
	public ParamObject(String trainingPath, String rulePath){
		trainingDataFilePath = trainingPath;
		ruleDataFilePath = rulePath;
	}

	public TrainingModule.RuleType getRuleType() {
		return ruleType;
	}

	public void setRuleType(TrainingModule.RuleType ruleType) {
		this.ruleType = ruleType;
	}

	public TrainingModule.AuctionType getAuctionType() {
		return auctionType;
	}

	public void setAuctionType(TrainingModule.AuctionType auctionType) {
		this.auctionType = auctionType;
	}

	public int getFuzziness() {
		return fuzziness;
	}

	public void setFuzziness(int fuzziness) {
		this.fuzziness = fuzziness;
	}

	public int getNumberOfIteration() {
		return numberOfIteration;
	}

	public void setNumberOfIteration(int numberOfIteration) {
		this.numberOfIteration = numberOfIteration;
	}

	public String getTrainingDataFilePath() {
		return trainingDataFilePath;
	}

	public void setTrainingDataFilePath(String trainingDataFilePath) {
		this.trainingDataFilePath = trainingDataFilePath;
	}

	public String getRuleDataFilePath() {
		return ruleDataFilePath;
	}

	public void setRuleDataFilePath(String ruleDataFilePath) {
		this.ruleDataFilePath = ruleDataFilePath;
	}
}
