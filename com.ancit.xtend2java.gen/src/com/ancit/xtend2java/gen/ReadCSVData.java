package com.ancit.xtend2java.gen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ancit.xtend2java.gen.model.OAWModel;

public class ReadCSVData {
	public static void main(String[] args) {

		String tmp = "CanTSyn_getCanTSynGlobalTimeDomainSlave().canTSynGlobalTimeSlave.canTSynGlobalTimeSlavePdu";

		HashMap<String, OAWModel> mp = readCSV();
		for (Map.Entry mapElement : mp.entrySet()) {
			System.out.println(tmp.matches("" + mapElement.getKey().toString() + "(.*)"));
			if (tmp.toLowerCase().contains(mapElement.getKey().toString().toLowerCase())) {
				System.out.println("Working.......");
			}
		}
	}
	public static ArrayList<OAWModel> oawList = new ArrayList<OAWModel>();
	public static ArrayList<OAWModel> getModelList(){
		return oawList;
	}
	public static HashMap<String, OAWModel> readCSV() {
		HashMap<String, OAWModel> listOfMethods = new HashMap<String, OAWModel>();
		File file = new File(
				"C:\\Users\\LENOVO\\git\\xtendtojava\\com.ancit.xtend2java.gen\\resources\\LoadMethods.csv");
		String absolutePath = file.getAbsolutePath();
		System.out.println(absolutePath);
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(absolutePath));
			OAWModel utilObj = new OAWModel();
			while ((line = br.readLine()) != null) {
				utilObj = new OAWModel();
				String[] country = line.split(cvsSplitBy);
				utilObj.setMethodName(country[1]);
				utilObj.setRetuneType(country[2]);
				utilObj.setRetunObjectName(country[3]);
				oawList.add(utilObj);
				
				listOfMethods.put(country[0], utilObj);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return listOfMethods;
	}
}