package co.harlequinmettle.healthfoodconcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import android.content.Context;

public class ObjectLoadingThread implements Runnable {

	static final String DATA_100G = "database_object";
	Context context;
	Integer objLoading = 0;
	long loadStart = 0;
	float loadTime = 0;
	float[][] db;
	int i = 0;

	// CALCULATE COLOR FACTORS IN CODE
	public ObjectLoadingThread(Context context, float[][] db) {
		loadStart = System.currentTimeMillis();
		this.context = context;
		this.db = db;
	}

	public void run() {
		for (i = 0; i < db.length; i++) {
			// /TEST IF ALL FILES EXIST FIRST -
		}
		for (int i = 0; i < db.length; i++) {
			objLoading = i;
			loadTime = (float) ((System.currentTimeMillis() - loadStart) / 1000.0);
			try {
				// TEST ALTERNATIVE LOAD FROM PRESTORED OBJECTS ASSETMANAGER
				FileInputStream fis = context.openFileInput(DATA_100G + i);
				ObjectInputStream objin = new ObjectInputStream(fis);
				db[i] = (float[]) objin.readObject();
				objin.close();

			} catch (Exception ioe) {
				System.out.println("NO save: saver");
				WhatYouEat.longLoading = true;
				LoadDataFromTextFile dataLoader = new LoadDataFromTextFile(
						context, db);
				WhatYouEat.loadingThread = 
				new Thread(dataLoader) ;
				WhatYouEat.loadingThread.start();
				return;
			}

			if (i % 3 == 0) {
				WhatYouEat.mHandler.post(new Runnable() {
					public void run() {
						WhatYouEat.pb
								.setProgress((int) (100.0 * objLoading / 129));
					}
				});
			}	
		
			Thread.yield();
		}
		//WhatYouEat.setNutrientStats();
		//WhatYouEat.kCal = WhatYouEat.db[5];
		objLoading = 0;
		WhatYouEat.calculateHighlightNumbers();
		WhatYouEat._loaded = true;
		if(WhatYouEat.needToSetSearchFoods){
			WhatYouEat.setFoodsIds();
		}
		System.out.println("-----------------DONE RESTORING DB");
		System.out.println("---time to restore db---->" + (loadTime));
	}
}
