package postDataMiningModule;

public class AccuracyResult {
	
	private int correctClassifications = 0;
	private int totalClassifications = 0;
	
	private double accuracy = 0.0;
	
	public AccuracyResult(int correctClassifications, int totalClassifications) {
		this.correctClassifications = correctClassifications;
		this.totalClassifications = totalClassifications;
		
		accuracy = (double) correctClassifications / (double) totalClassifications * 100;
	}
	
	@Override
	public String toString() {
		return String.format("Correct Classifications: %s\nTotal Classifications: %s\nAccuracy: %2.2f", correctClassifications, totalClassifications, accuracy);
	}

}
