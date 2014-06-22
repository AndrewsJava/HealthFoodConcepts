package co.harlequinmettle.healthfoodconcepts;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WhatYouEat extends Activity implements OnTouchListener,
		I_Preferences, HasServingSizeInfo {
	//
	static Thread loadingThread;
	static HorizontalScrollView application;
	static LinearLayout appAccess;
	static MenuScroller intro, settings;
	static ArrayList<TreeMap<Integer, Boolean>> allMyFoods = new ArrayList<TreeMap<Integer, Boolean>>();
	// static HashMap<String, Integer> searchResults = new HashMap<String,
	// Integer>();
	static HashMap<Integer, Float> myFoodQuantities = new HashMap<Integer, Float>();
	static HashMap<Integer, Integer> myFoodUnitIDs = new HashMap<Integer, Integer>();
	static HashMap<Integer, String> myFoodUnits = new HashMap<Integer, String>();

	// static HashMap<Integer, Boolean> myGoodNutrients = new HashMap<Integer,
	// Boolean>();
	static boolean[] myGoodNutrients;
	static float[] goodNutritionGoals;
	// static float[] defaultNutritionGoals;
	static Context ctx;
	// static final int[] CHILD_ID = new int[40];
	static boolean _loaded = false;

	static final int USING_KCAL = 200000;
	static final int USING_SERVING = 400000;
	static final int USING_GRAMS = 800000;

	static final int USE_ALL_FOODS = 10000;
	static final int USE_MY_FOOD_GROUPS = 5000;
	static final int USE_MY_FOODS = 500;
	static final int USE_ONE_FOOD_GROUP = 128;

	static final int SEARCH_TYPE_SUM = 10000002;
	static final int SEARCH_TYPE_PRODUCT = 10000001;

	// static final int VIEW_HORIZONTAL = 151516161;
	static final int VIEW_VERTICAL = 515151661;
	// PREFERENCE NEEDS TO BE STORED AND RESTORED AS PREFERENCE
	static int myFoods_View = VIEW_VERTICAL;
	static int Foods_Search = USE_ALL_FOODS;
	static int Nutrient_Measure = USING_GRAMS;
	static int Food_Group = 21;// default irrelevant
	static int Search_Type = SEARCH_TYPE_PRODUCT;

	static boolean longLoading = false;
	static boolean searching = false;
	static boolean calculatingStats = false;
	static boolean statsLoaded = false;
	static float sw, sh;
	static float[][] db = new float[130][8194];
	static final int FOOD_COUNT = 8194;// REPLACE ALL APP WIDE INSTANCEES WITH
										// CONTANT

	// 4052~23~Honey
	static String[] foods = new String[8194];
	// static int[] MY_FOODS;
	// static byte[] foodGroupMap = new byte[8194];// CHANGE TO BYTE
	// static float[] kCal ;// CHANGE TO BYTE
	// 9~Fats and Oils
	static String[] foodGroups = new String[25];
	// 5~kcal~Energy
	static String[] nutrients = new String[130];
	static String[] units = new String[130];

	static int[][] foodsByGroup = new int[25][];

	static String[] searchResults;
	static int[] foodCodeResults;
	static final int PROGRESS_BAR_ID = 100010001;
	static int[] searchTheseFoods;

	private static final String DATA_100G = "database_object";
	public static final int USING_NOT_ZEROS = 1024;
	public static final int USING_ZEROS = 2048;
	public static int DATA_CHOICES = USING_NOT_ZEROS;
	// loading definitions:
	// a - nutrient units
	// b - foodGroup
	// c - food foodGroupMap kcalPer100G
	GeneralLoadingThread a, b, c;
	public static boolean[] MY_FOOD_GROUPS;
	public static boolean[] MY_NUTRIENTS;
	static ProgressBar pb;
	static Handler mHandler = new Handler();

	static HashMap<Integer, StatTool> nutritionStats = new HashMap<Integer, StatTool>();
	static HashMap<Integer, float[]> highlightFactors = new HashMap<Integer, float[]>();

	// -1 if no serving size info is available
	static int[] optimalServingId = new int[FOOD_COUNT];
	// all food ids with servings size info (at least 1)
	static int[] WITH_SERVING_SIZE = HAS_SERVING_INFO;

	static String[][] oddUnits = new String[FOOD_COUNT][];// for each food id
															// list of different
															// units
	static float[][] metricConversion = new float[FOOD_COUNT][];// corresponding
																// grams
	static float[][] quantityFactor = new float[FOOD_COUNT][];// corresponding
																// amount of
																// different
																// units
	static final int TEXT_SMALL = 21;
	static final int TEXT_MED = 25;
	static final int TEXT_LARGE = 35;
	static boolean needToSetSearchFoods;
	public static int save_db = 0;
	static WhatYouEat _MyApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setDefaults();
		_MyApp = this;
		// int version = Build.VERSION.SDK_INT;
		// if(version>10 && version < 14 ){
		// ActionBar bar = getActionBar();
		// bar.hide();
		// }
		// always set metrics for screen width and height
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		sw = metrics.widthPixels;
		sh = metrics.heightPixels;
		
		ctx = this;// field reference to current context
		// main layout (and its one child) for whole app
		application = new HorizontalScrollView(this);
		appAccess = new LinearLayout(this);// may need to make sure its

		// try to obtain weather use prefers 100g 100kcal or 'serving'
		Nutrient_Measure = restorePreference("SEARCHUNITS");

		Foods_Search = restorePreference("SEARCHFOODS");//

		save_db = restorePreference("SAVEDB");

		restoreGuidelines();

		restoreMyGoodNutrients();

		restoreMyFoodUnitIds();

		if (!loadMyFoodGroups())
			MY_FOOD_GROUPS = DEFAULT_FOOD_GROUPS;

		if (!loadMyNutrients())
			MY_NUTRIENTS = DEFAULT_NUTRIENTS;

		for (int i = 0; i < optimalServingId.length; i++) {
			optimalServingId[i] = -1;
		}
		// OPTIONAL TO SAVE AS OBJECTS AND LOAD LIKE DB

		a = new GeneralLoadingThread(this, nutrients, units, "nutr.txt");

		b = new GeneralLoadingThread(this, foodGroups, "group.txt");
		// KCAL PER100G REPLACE WITH DIRECT REFERENCE TO DB[5]
		c = new GeneralLoadingThread(this, foods, foodsByGroup, "food.txt",
				GeneralLoadingThread.FOOD);
		// sorts each foodgroup id after loading

		new Thread(a).start();
		new Thread(b).start();
		new Thread(c).start();

		new Thread(new LoadWeightsConversion()).start();
		// load database if objects not found automatically lauch text reading
		// thread
		// either thread finishes by starting thread to load
		_loaded = false;
		ObjectLoadingThread objLoader = new ObjectLoadingThread(this, db);
		loadingThread = new Thread(objLoader);
		loadingThread.start();

		setFoodsIds();

		restoreQuantities();
		restoreUnits();

		
		pb = new ProgressBar(this, null,
				android.R.attr.progressBarStyleHorizontal);
		pb.setId(PROGRESS_BAR_ID);
		intro = new MenuScroller(this, pb, 0);

		appAccess.addView(intro);

		application.addView(appAccess);

		setContentView(application);
		
		
		Toast mToast = Toast
				.makeText(
						this,
						"\n\n\n\n\nPlease adjust some settings while database loads\n\n\n\n\n",
						1);
		mToast.setGravity(Gravity.TOP, 0, 50);

		mToast.show();
		if (!loadMyFoods())
			for (int i = 0; i < 25; i++) {
				allMyFoods.add(new TreeMap<Integer, Boolean>());
			}
		// saveObject(0, "SAVEDB");
	}

	public void resetStaticContext() {
ctx = this; 
	}

	private void setDefaults() {
		for (int i = 0; i < FOOD_COUNT; i++) {
			oddUnits[i] = new String[1];// for each food id
			oddUnits[i][0] = "grams"; // list of different
			metricConversion[i] = new float[1];// corresponding
			metricConversion[i][0] = 100;// defalut convert 100g into 100g
			quantityFactor[i] = new float[1];// corresponding
			quantityFactor[i][0] = 100;
		}
	}

	private void restoreMyGoodNutrients() {
		int def = 0;
		boolean[] x = null;
		try {
			FileInputStream fis = ctx.openFileInput("GOODNUTR");
			ObjectInputStream objin = new ObjectInputStream(fis);
			x = (boolean[]) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
		}
		if (x != null)
			myGoodNutrients = x;
		else
			myGoodNutrients = DEFAULT_GOOD_NUTRIENTS.clone();
	}

	public static String[] getMyFoodsAsTextArray() {
		ArrayList<String> foodListConstruct = new ArrayList<String>();
		for (TreeMap<Integer, Boolean> foodi : WhatYouEat.allMyFoods) {
			for (Integer i : foodi.keySet()) {
				foodListConstruct.add(foods[i]);
			}
		}
		String[] fd = new String[foodListConstruct.size()];
		for (int i = 0; i < fd.length; i++) {
			fd[i] = foodListConstruct.get(i);
		}
		return fd;
	}

	public static int[] getMyFoodsIdsArray() {
		ArrayList<Integer> foodIdsConstruct = new ArrayList<Integer>();
		for (TreeMap<Integer, Boolean> foodi : WhatYouEat.allMyFoods) {
			for (Integer i : foodi.keySet()) {
				foodIdsConstruct.add(i);
			}
		}
		int[] ids = new int[foodIdsConstruct.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = foodIdsConstruct.get(i);
		}
		return ids;
	}

	public static void setFoodsIds() {
		int[] theseFoods = null;
		switch (Foods_Search) {
		case USE_ALL_FOODS:
			theseFoods = new int[8194];
			for (int i = 0; i < 8194; i++)
				theseFoods[i] = i;
			break;
		case USE_MY_FOODS:
			ArrayList<Integer> getMySelectFoods = new ArrayList<Integer>();

			for (TreeMap<Integer, Boolean> amf : allMyFoods) {
				for (Entry<Integer, Boolean> ent : amf.entrySet()) {
					if (ent.getValue())
						getMySelectFoods.add(ent.getKey());
				}
			}
			theseFoods = new int[getMySelectFoods.size()];
			for (int i = 0; i < theseFoods.length; i++)
				theseFoods[i] = getMySelectFoods.get(i);

			break;
		case USE_MY_FOOD_GROUPS:
			if (_loaded)
				theseFoods = constructUniqueValuesFromMyFoodGroups();
			else
				needToSetSearchFoods = true;
			break;
		case USE_ONE_FOOD_GROUP:
			theseFoods = foodsByGroup[Food_Group];
			break;
		default:
			theseFoods = foodsByGroup[Food_Group];
			break;
		}

		searchTheseFoods = theseFoods;
	}

	public static String getFoodsSearchDescription() {

		switch (Foods_Search) {
		case USE_ALL_FOODS:
			return "all foods in database";

		case USE_MY_FOODS:
			return "My Foods";
		case USE_MY_FOOD_GROUPS:
			return "My Food Groups";
		case USE_ONE_FOOD_GROUP:
			return foodGroups[Food_Group];
		default:
			break;
		}

		return "";
	}

	private static int[] constructUniqueValuesFromMyFoodGroups() {
		HashSet<Integer> uniqueValues = new HashSet<Integer>();
		for (int i = 0; i < MY_FOOD_GROUPS.length; i++) {
			if (MY_FOOD_GROUPS[i]) {
				for (int j : foodsByGroup[i]) {
					uniqueValues.add(j);
				}
			}
		}
		int counter = 0;
		int[] uniques = new int[uniqueValues.size()];
		for (int i : uniqueValues) {
			uniques[counter++] = i;
		}
		return uniques;
	}

	private static int[] constructUniqueValuesFromMyFoods() {
		HashSet<Integer> uniqueValues = new HashSet<Integer>();
		for (int i = 0; i < allMyFoods.size(); i++) {
			TreeMap<Integer, Boolean> perGroup = allMyFoods.get(i);
			if (MY_FOOD_GROUPS[i]) {
				for (int j : perGroup.keySet()) {
					uniqueValues.add(j);
				}
			}
		}
		int counter = 0;
		int[] uniques = new int[uniqueValues.size()];
		for (int i : uniqueValues) {
			uniques[counter++] = i;
		}
		return uniques;
	}

	public static int[] getFoodSearchIds() {
		return searchTheseFoods;
	}

	// CALL AFTER DATABASE IS LOADED AND AFTER CHANGES TO SETTINGS
	public static void calculateHighlightNumbers() {
		new Thread(new SimpleStatsBuilder()).start();
	}

	private int restorePreference(String string) {
		int def = 0;
		try {
			FileInputStream fis = this.openFileInput(string);
			ObjectInputStream objin = new ObjectInputStream(fis);
			def = (Integer) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
			if (string.equals("SEARCHUNITS"))
				return WhatYouEat.USING_GRAMS;
			else if (string.equals("SEARCHFOODS"))
				return WhatYouEat.USE_MY_FOOD_GROUPS;
			else
				return 0;
		}
		return def;
	}

	static void restoreGuidelines() {
		int def = 0;
		float[] x = null;
		try {
			FileInputStream fis = ctx.openFileInput("GOODNUTRGOALS");
			ObjectInputStream objin = new ObjectInputStream(fis);
			x = (float[]) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
		}
		if (x != null)
			goodNutritionGoals = x;
		else
			goodNutritionGoals = DEFAULT_NUTR_GOALS.clone();
	}

	private void restoreUnits() {
		int def = 0;
		HashMap<Integer, String> x = null;
		try {
			FileInputStream fis = this.openFileInput("UNITYMAP");
			ObjectInputStream objin = new ObjectInputStream(fis);
			x = (HashMap<Integer, String>) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
		}
		if (x != null)
			myFoodUnits = x;
	}

	private void restoreMyFoodUnitIds() {
		int def = 0;
		HashMap<Integer, Integer> x = null;
		try {
			FileInputStream fis = this.openFileInput("UNITIDMAP");
			ObjectInputStream objin = new ObjectInputStream(fis);
			x = (HashMap<Integer, Integer>) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
		}
		if (x != null)
			myFoodUnitIDs = x;
	}

	private void restoreQuantities() {
		int def = 0;
		HashMap<Integer, Float> x = null;
		try {
			FileInputStream fis = this.openFileInput("QUANTITYMAP");
			ObjectInputStream objin = new ObjectInputStream(fis);
			x = (HashMap<Integer, Float>) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
		}
		if (x != null)
			myFoodQuantities = x;
	}

	public boolean loadMyFoodGroups() {
		try {
			// TEST ALTERNATIVE LOAD FROM PRESTORED OBJECTS ASSETMANAGER
			FileInputStream fis = this.openFileInput("MYFOODGROUPS");
			ObjectInputStream objin = new ObjectInputStream(fis);
			MY_FOOD_GROUPS = (boolean[]) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
			return false;
		}
		return true;
	}

	public boolean loadMyFoods() {
		try {
			// TEST ALTERNATIVE LOAD FROM PRESTORED OBJECTS ASSETMANAGER
			FileInputStream fis = this.openFileInput("MYFOODS");
			ObjectInputStream objin = new ObjectInputStream(fis);
			allMyFoods = (ArrayList<TreeMap<Integer, Boolean>>) objin
					.readObject();
			objin.close();

		} catch (Exception ioe) {
			return false;
		}
		return true;
	}

	public boolean loadMyNutrients() {
		try {
			// TEST ALTERNATIVE LOAD FROM PRESTORED OBJECTS ASSETMANAGER
			FileInputStream fis = this.openFileInput("MYNUTRIENTS");
			ObjectInputStream objin = new ObjectInputStream(fis);
			MY_NUTRIENTS = (boolean[]) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
			return false;
		}
		return true;
	}

	public static void saveObject(Object OBJ, String obLocatorName) {
		try {
			FileOutputStream fos = ctx.openFileOutput(obLocatorName,
					Context.MODE_PRIVATE);
			ObjectOutputStream objout = new ObjectOutputStream(fos);
			objout.writeObject(OBJ);
			objout.flush();
			objout.close();

		} catch (IOException ioe) {
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// OTHERWISE OBJECT IS NOT DESTROYED AND GETTING ERROR:
		// CHILD ALREADY HAS A PARENT ON MENUSCROLLER-ADDKEYWORDSCROLLERS

		// STORE PREFERENC ARRAYS AS OBJECTS

		// SAVE OTHER DATA EXPERIMENT
	}

	// ///////////////////////////////////////
	public LinearLayout basicLinearLayout() {
		LinearLayout basic = new LinearLayout(this);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				MATCH_PARENT, WRAP_CONTENT);
		basic.setLayoutParams(params);
		basic.setOrientation(VERTICAL);
		return basic;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		float xDown = event.getX();
		float yDown = event.getY();
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_POINTER_DOWN:
			break;
		case MotionEvent.ACTION_POINTER_UP:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_DOWN:
			// setContentView(_longLayout);
			break;
		case MotionEvent.ACTION_UP:

			break;
		}
		return true;
	}// END EVENTS

	public static void setSearchResultsFrom(String searchWord, int groups_search) {
		searching = true;
		if (searchWord.length() < 3) {
			searchResults = new String[1];
			foodCodeResults = new int[1];
			searchResults[0] = "Water, tap, drinking";
			foodCodeResults[0] = 8081;
			return;
		}
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<Long> resultsID = new ArrayList<Long>();

		// search for search word as one single unit only
		int[] foodIDsToSearch;

		switch (groups_search) {
		case USE_ALL_FOODS:
			foodIDsToSearch = new int[FOOD_COUNT];
			for (int i = 0; i < FOOD_COUNT; i++) {
				foodIDsToSearch[i] = i;
			}
			break;
		case USE_MY_FOOD_GROUPS:
			foodIDsToSearch = constructUniqueValuesFromMyFoodGroups();
			break;
		case USE_MY_FOODS:
			foodIDsToSearch = constructUniqueValuesFromMyFoods();
			break;
		case USE_ONE_FOOD_GROUP:
			foodIDsToSearch = foodsByGroup[Food_Group];
			break;
		default:

			foodIDsToSearch = foodsByGroup[groups_search];
			break;
		}

		String sw = searchWord.trim();
		while (sw.contains("  ")) {
			sw = sw.replaceAll("  ", " ");
		}
		sw = " " + sw + " ";
		// for each food description with spaces added check for an exact
		// match
		for (int fdID : foodIDsToSearch) {
			String item = " " + foods[fdID];
			if (item.toLowerCase().replaceAll("[,\\(\\)]", " ").contains(sw)) {
				results.add(item.trim());
				resultsID.add(new Long(fdID));
			}
		}
		if (false) {
			sw = sw.trim() + " ";
			if (results.size() < 50)
				for (int fdID : foodIDsToSearch) {
					String item = " " + foods[fdID];
					if (item.toLowerCase().replaceAll("[,\\(\\)]", " ")
							.contains(sw)
							&& !results.contains(item.trim())) {
						results.add(item.trim());
						resultsID.add(new Long(fdID));
					}
				}
		}
		// sw = " " + sw.trim();
		sw = sw.trim();
		if (results.size() < 40 && sw.length() > 2)
			for (int fdID : foodIDsToSearch) {
				String item = foods[fdID];
				if (!results.contains(item.trim())
						&& item.toLowerCase().contains(sw)) {
					//System.out .println("found with regex ---    ---    ----    ---    ----    ----");
					results.add(item.trim());
					resultsID.add(new Long(fdID));
				}
			}

		if (results.size() < 5)
			if (searchWord.length() > 3) {
				searchWord = searchWord.trim().substring(0,
						searchWord.length() - 1);
				for (int fdID : foodIDsToSearch) {
					String item = " " + foods[fdID];
					if (!results.contains(item.trim())
							&& item.toLowerCase().replaceAll("[,\\(\\)]", " ")
									.contains(searchWord))
						results.add(item.trim());
					resultsID.add(new Long(fdID));
					//System.out.println("found ON letter less ONE");

				}
			}

		if (results.size() < 50) {
			String[] sWords = searchWord.trim().split(" ");
			if (sWords.length > 1)
				outerloop: for (int fdID : foodIDsToSearch) {
					String item = foods[fdID];
					for (String pt : sWords) {
						if (results.contains(item.trim())
								|| !item.toLowerCase().contains(pt)
								&& pt.length() > 3)
							continue outerloop;
					}
					results.add(item.trim());
					resultsID.add(new Long(fdID));
					System.out.println("found MULTIPLE WORD CASE---");
					if (results.size() > 50) {
						break outerloop;
					}
				}
		}
		// load results of search int static array
		// string description with cooresponding food code
		searchResults = new String[results.size()];
		foodCodeResults = new int[results.size()];

		for (int i = 0; i < results.size(); i++) {
			searchResults[i] = results.get(i);
			foodCodeResults[i] = (resultsID.get(i).intValue());

		}
		searching = false;
	}

	static int[] concat(int[] A, int[] B) {
		int aLen = A.length;
		int bLen = B.length;
		int[] C = new int[aLen + bLen];
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);
		return C;
	}

	private static class SimpleStatsBuilder implements Runnable {

		@Override
		public void run() {
			try {
				loadingThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			calculatingStats = true;
			statsLoaded = false;
			long time = System.currentTimeMillis();

			int[] foodIds = WhatYouEat.getFoodSearchIds();
			for (int i = 0; i < 130; i++) {
				boolean b = MY_NUTRIENTS[i];
				if (!b)
					continue;
				float[] basically = StatTool.simpleStats(foodIds, i);

				highlightFactors.put(i, basically);

				Thread.yield();

			}

			statsLoaded = true;
			calculatingStats = false;
		}

	}

	public static byte getRelativeAbundance(int nutrient, float data) {

		float[] nstat = WhatYouEat.highlightFactors.get(nutrient);
		float min = nstat[0];
		float mean = nstat[1];
		float max = nstat[2];

		float n = 0;

		if (data > mean) {
			n = data - mean;
			n /= (max - mean);

		} else {

			n = -(mean - data) / (mean - min + 1);
		}
		int sim = (int) (n * 100);
		if (sim > 100)
			sim = (int) (101 + 100 * Math.log(n - 100));

		return (byte) sim;

	}

	public static float getPer100KcalDataPoint(int foodID, float data) {
		float calories = db[5][foodID];
		// RETURN NEGATIVE IF CALORIES ARE ZERO AND USE SERVING
		float rData = (float) (100.0 * data);
		if (calories > 10)
			rData /= (db[5][foodID]);
		else
			rData /= (20);
		return (float) (((int) (1000 * (rData))) / 1000.0);

	}

	// 7416~1~tsp~2.2 - weights conversion
	//
	public static float getPerServingDataPoint(int foodID,
			float nutritionPer100Grams) {
		if (quantityFactor[foodID] == null||optimalServingId[foodID]<0)
			return -1;

		float quantityOfOddUnit = quantityFactor[foodID][optimalServingId[foodID]];
		float gramsPerOddUnit = metricConversion[foodID][optimalServingId[foodID]];

		float rData = nutritionPer100Grams * gramsPerOddUnit / 100.0f;
		return (float) (((int) (1000 * (rData))) / 1000.0);

	}

	public static int findGroupFromFoodID(int foodId) {
		for (int i = 0; i < 25; i++) {
			if (Arrays.binarySearch(foodsByGroup[i], foodId) >= 0)
				return i;
		}

		return 10;
	}

}// END APP

