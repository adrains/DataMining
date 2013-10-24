package miningRules;

public class Data {
	public static final int RULE_LENGTH = 16;

	private int[] data = new int[RULE_LENGTH];

	private String category = "";
	
	public Data(String rawData) {
		
		String[] splitData = rawData.split(",");
		
		category = splitData[0];
		
		for (int i = 0; i < RULE_LENGTH; i++) {
			data[i] = Integer.parseInt(splitData[i+1]);
		}	
	}
	
	public String getDataCategory() {
		return category;
	}
	
	public int[] getData() {
		return data;
	}
	
	@Override
	public String toString() {
		
		String output = "";
		output += category;		
		
		for (int i = 0; i < data.length; i++)
			output += "," + data[i];
		
		output += category;
		
		return output;
	}
}
