package co.harlequinmettle.healthfoodconcepts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;

public class LoadDataFromTextFile implements Runnable {
	Context context;
	float[][] db;
	static Integer lineCount = 0;
	Boolean finished;
	float threadTime = 0;

	public LoadDataFromTextFile(Context context, float[][] db) {
		this.context = context;
		this.db = db;
	}

	public void run() {
 
		long start = System.currentTimeMillis();
		for (int i = 0; i < db.length; i++)
			for (int j = 0; j < db[i].length; j++)
				db[i][j] = -1;

		AssetManager am = context.getAssets();
		BufferedReader br = null;
		try {
			InputStream is = am.open("DATA__3.txt");
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String holder = "";
			while ((holder = br.readLine()) != null) {
				// REMOVE INDEX4 - DO PER KAL CALC IN PROGRAM - ADD CAL/100G TO
				// FOOD DEF
				// 1573~6~2999~73~2999~70
				// 1573~6~2999~73~70
				String[] values = holder.split("~");
				db[doInt(values[1])][doInt(values[0])] = (float) (doDouble(values[2]));

				if (lineCount++ % 1000 == 10) {
					WhatYouEat.mHandler.post(new Runnable() {
						public void run() {
							int pgrs = (int) (100.0 * lineCount / 578642);
							if (pgrs > 97)
								pgrs = 100; 
							WhatYouEat.pb
							.setProgress(pgrs);
						}
					});
					Thread.yield();
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WhatYouEat.calculateHighlightNumbers();
		//WhatYouEat.setNutrientStats();
		//WhatYouEat.kCal = WhatYouEat.db[5];
		WhatYouEat._loaded = true;
		if(WhatYouEat.needToSetSearchFoods){
			WhatYouEat.setFoodsIds();
		}
		threadTime = (float) ((System.currentTimeMillis() - start) / 1000.0);
lineCount = 0;
		System.out.println("----------DATA BASE LOADED IN----------------->"
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
			// System.out.println(" TEXT TO NUMBER ERR "+ value +" to -1e-7 ");
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
