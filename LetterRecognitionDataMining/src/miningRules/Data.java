package miningRules;

public class Data {
	public static final int RULE_LENGTH = 16;

	private int[] data = new int[RULE_LENGTH];

	private String category = "";
	
	public Data(String rawData) {
		
		
		
	}
	
	public String getDataCategory() {
		return category;
	}
	
	public int[] getData() {
		return data;
	}
}
