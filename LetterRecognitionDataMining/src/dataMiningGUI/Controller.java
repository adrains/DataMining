package dataMiningGUI;

import java.io.File;

public class Controller implements ControllerInterface{ 

	@Override
	public String preProcessData(String dataset, String dataOutput) {
		return preDataMiningModule.Preprocessor.preprocessData(dataset, dataOutput);
	}



}
