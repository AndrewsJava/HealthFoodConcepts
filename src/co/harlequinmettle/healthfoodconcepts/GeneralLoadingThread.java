package co.harlequinmettle.healthfoodconcepts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.res.AssetManager;

public class GeneralLoadingThread implements Runnable {
	Context context;
	String[] info;
	String[] units;
	// float[] kcalMap;
	String fromFile;
	int mapID = 0;
	int[][] map;
	int readCount = 0;
	static final int FOOD = 10000203;
	static final int NUTR = 10444443;
	int instanceType;
	ArrayList<ArrayList<Integer>> dynamic = new ArrayList<ArrayList<Integer>>();
	private int type;

	// loads food group name list
	public GeneralLoadingThread(Context context, String[] info, String objFile) {
		this.context = context;
		this.info = info;
		this.fromFile = objFile;
	}

	// loads nutrient definitions map and nutrient unit mapping
	public GeneralLoadingThread(Context context, String[] info, String[] units,
			String objFile) {
		this.context = context;
		this.info = info;
		this.units = units;
		this.fromFile = objFile;

	}

	// loads food definitions and foodgroup mapping
	public GeneralLoadingThread(Context context, String[] info,
			int[][] mapping, String objFile, int type) {
		this.context = context;
		this.info = info;
		this.map = mapping;
		this.fromFile = objFile;
		// this.kcalMap = kcal;
		for (int i = 0; i < 25; i++)
			dynamic.add(new ArrayList<Integer>());
		instanceType = type;
	}

	public void run() {
 

		long start = System.currentTimeMillis();

		AssetManager am = context.getAssets();
		BufferedReader br = null;
		try {
			InputStream is = am.open(fromFile);
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			// 7~g~Water ---- units info
			// 9~Fats and Oils ---- info
			// 4039~19~Ham salad spread ---- mapping info ---- Kcal/100g
			String holder = "";
			while ((holder = br.readLine()) != null) {

				String[] values = holder.split("~");
				int id = doInt(values[0]);
				if (units == null && map == null)// first constructor
					// load foodgroup
					info[id] = values[1];
				else {
					if (units != null) {
						units[id] = values[1];// nutrient units
						info[id] = values[2];
					 
					} else {
						// food definition with food group and kcal/100g
						//
						// 3237~7~Egg, whole, raw, fresh~143
						dynamic.get(doInt(values[1])).add(id);
						// map[id] = (byte)doInt((values[1]));
						info[id] = values[2];
						// kcalMap[id] = (float)(doDouble(values[ 3]));
					}
				}

				if (readCount++ % 1000 == 10) {
					Thread.yield();
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (instanceType == FOOD){
			for (int i = 0; i < 25; i++) {
				ArrayList<Integer> foods = dynamic.get(i);
				map[i] = new int[foods.size()];
				for (int j = 0; j < foods.size(); j++) {
					map[i][j] = foods.get(j);
				}
				Arrays.sort(map[i]);
				//System.out.println(Arrays.toString(map[i]));
		 } 
		}
		 float threadTime = (float) ((System.currentTimeMillis() - start) / 1000.0);
		 System.out.println(fromFile + "--------------------------->"
				+ threadTime);
	}

	/*
	 * returns the double value of a string and returns -1E-7 if the string
	 * could not be parsed as a double.
	 * 
	 * @param value the string that gets converted into a double.
	 */
	public static double doDouble(String value) {
		try {
			double val = Double.parseDouble(value);
			if (val == val)// only return value if its not NaN , NaN==NaN is
							// false
				return val;
			else
				return -0.0000001;
		} catch (Exception e) {
		 	return -0.0000001;
		}
	}

	public static int doInt(String value) {
		try {
			int val = Integer.parseInt(value);
			return val;
		} catch (Exception e) {
			return -Integer.MAX_VALUE;
		}
	}
 
}// END FILE LOAD THREAD
