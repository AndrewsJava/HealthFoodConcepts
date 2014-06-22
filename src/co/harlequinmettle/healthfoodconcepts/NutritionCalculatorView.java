package co.harlequinmettle.healthfoodconcepts;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class NutritionCalculatorView extends LinearLayout {
	static float[] myNutritionalState;
	static float[] compareToGoal;
	SubScroll myNutritionBuilder;
	int scrollViewCount = 0;
	LinearLayout foodQuantityInput;
	EditText quantityInput;
	int currentFood = 0;
	ArrayList<Long> currentCalc = new ArrayList<Long>();
	View.OnClickListener foodSuggestionListener = new View.OnClickListener() {
		public void onClick(View view) {
			int id = view.getId();

		}
	};
	View.OnClickListener nutritionCalculaterListener = new View.OnClickListener() {
		public void onClick(View view) {
			int id = view.getId();
			switch (id) {
			case CLEAR:

				break; 
			case CALCULATE:
				compareThisDietToNutritionTargets();
				displayNutritionState();
				break;
			case SUGGEST:
				compareThisDietToNutritionTargets();
				searchforNutritionalFoods();
				break;
			default:
				break;

			}
			// from each checked food get quantity and unit - convert to grams
			// and calculate nutrient profile and display results

		}
	};
	View.OnClickListener saveQuantityListener = new View.OnClickListener() {
		public void onClick(View view) {
			try {
				float quantity = Float.valueOf(quantityInput.getText()
						.toString());
				WhatYouEat.myFoodQuantities.put(currentFood, quantity);
			} catch (NumberFormatException nfe) {
			}
			((Button) myNutritionBuilder.findViewById(currentFood)
					.findViewById(currentFood + 2000000))
					.setText(addFullUnitText(currentFood));
			WhatYouEat.saveObject(WhatYouEat.myFoodQuantities, "QUANTITYMAP");
			WhatYouEat.saveObject(WhatYouEat.myFoodUnits, "UNITYMAP");
			WhatYouEat.saveObject(WhatYouEat.myFoodUnitIDs, "UNITIDMAP");
		}
	};

	RadioGroup.OnCheckedChangeListener unitChoiceListener = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// DO SOMETHING WHEN USER SELECTS UNITS FOR NUMERIC TEXT INPUT

			int foodID = checkedId / 10000;
			int unitID = checkedId - foodID * 10000;

			WhatYouEat.myFoodUnits.put(foodID,
					WhatYouEat.oddUnits[foodID][unitID]);

			WhatYouEat.myFoodUnitIDs.put(foodID, unitID);

			// WhatYouEat.myFoodQuantities.put(foodID, getnumberfrom
			// textfield)
			WhatYouEat.saveObject(WhatYouEat.myFoodQuantities, "QUANTITYMAP");
			WhatYouEat.saveObject(WhatYouEat.myFoodUnits, "UNITYMAP");
			WhatYouEat.saveObject(WhatYouEat.myFoodUnitIDs, "UNITIDMAP");

		}
	};

	View.OnClickListener foodQuantityListener = new View.OnClickListener() {
		public void onClick(View view) {
			int foodId = view.getId() - 2000000;
			currentFood = foodId;			
			
			removeOtherViews();
			
			SubScroll selectAmount = new SubScroll(WhatYouEat.ctx);
			LinearLayout structure = selectAmount.basicLinearLayout();

			// option add linearlayout with stationary food title
			structure.setId(10001000);
			TextView title = SubScroll.textView();
			title.setMinimumWidth(300);
			title.setMaxWidth(SubScroll.MAX_BUTTON_WIDTH);
			title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, WhatYouEat.TEXT_MED);
			// title.setTextSize(25);
			title.setTextColor(0xffff00ff);
			title.setBackgroundColor(0xff333366);
			title.setText(WhatYouEat.foods[foodId]);
			// selectAmount.instanceChild.addView(title);
			structure.addView(title);
			quantityInput = SubScroll.editText("please enter a number");
			if (WhatYouEat.myFoodQuantities.containsKey(foodId)) {
				quantityInput.setText(""
						+ WhatYouEat.myFoodQuantities.get(foodId));
			}
			quantityInput.setRawInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_VARIATION_NORMAL);

			// selectAmount.instanceChild.addView(quantityInput);
			structure.addView(quantityInput);
			// insert text field and radio group of options
			RadioGroup unitChoices = new RadioGroup(WhatYouEat.ctx);
			unitChoices.setBackgroundColor(0xff222255);
			unitChoices.setOnCheckedChangeListener(unitChoiceListener);
			if (false) {
				RadioButton grams = new RadioButton(WhatYouEat.ctx);
				grams.setText("grams");
				grams.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
						WhatYouEat.TEXT_MED);
				grams.setId(1000000000);// 1 billion

				unitChoices.addView(grams);

				if (WhatYouEat.myFoodUnits.containsKey(foodId)
						&& WhatYouEat.myFoodUnits.get(foodId).equals("grams")) {
					grams.setChecked(true);

					if (WhatYouEat.myFoodQuantities.containsKey(foodId)) {
						quantityInput.setText(""
								+ WhatYouEat.myFoodQuantities.get(foodId));
					} else {
						quantityInput.setText("" + 10);
					}
				}
			}
			String[] servingUnits = WhatYouEat.oddUnits[foodId];
			if (servingUnits == null) {

				WhatYouEat.oddUnits[foodId] = new String[1];
				WhatYouEat.oddUnits[foodId][0] = "grams";
				WhatYouEat.quantityFactor[foodId] = new float[1];
				WhatYouEat.quantityFactor[foodId][0] = 100.0f;
				servingUnits = WhatYouEat.oddUnits[foodId];
				return;
			}

			for (int i = 0; i < servingUnits.length; i++) {
				RadioButton rb = new RadioButton(WhatYouEat.ctx);
				rb.setId(foodId * 10000 + i);
				rb.setText(servingUnits[i]);
				rb.setTextSize(TypedValue.COMPLEX_UNIT_DIP, WhatYouEat.TEXT_MED);
				unitChoices.addView(rb);
				if (WhatYouEat.myFoodUnits.containsKey(foodId)
						&& WhatYouEat.myFoodUnits.get(foodId).equals(
								servingUnits[i])) {
					rb.setChecked(true);
					if (WhatYouEat.myFoodQuantities.containsKey(foodId)) {
						quantityInput.setText(""
								+ WhatYouEat.myFoodQuantities.get(foodId));
					} else {
						quantityInput.setText("" + 10);
					}
				}
				// rb.setOnCheckedChangeListener(listener);
			}
			selectAmount.instanceChild.addView(unitChoices);
			structure.addView(selectAmount);
			// ///////////////////////////////////////
			Button enterButton = SubScroll.simpleButton();
			enterButton.setText("Save Food Quantity");
			enterButton.setId(100100100);
			enterButton.setOnClickListener(saveQuantityListener);

			structure.addView(enterButton);
			// /////////////////////////////////////
			WhatYouEat.appAccess.addView(structure);
			// /////////////////////////////////////////////////
			final ViewTreeObserver viewTreeObserver4 = WhatYouEat.application
					.getViewTreeObserver();
			if (viewTreeObserver4.isAlive()) {
				viewTreeObserver4
						.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
							@Override
							public void onGlobalLayout() {

								WhatYouEat.application.scrollBy((int) (350), 0);
								viewTreeObserver4
										.removeGlobalOnLayoutListener(this);
							}
						});
			}
		}
	};

	CompoundButton.OnCheckedChangeListener foodInclusionListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			int id = buttonView.getId();
			int foodId = id - 1000000;
			View structure = myNutritionBuilder.instanceChild
					.findViewById(foodId);
			if (isChecked) {
				((Button) structure.findViewById(id + 1000000))
						.setText(addFullUnitText(foodId));
				currentCalc.add(new Long(foodId));
			} else {
				((Button) structure.findViewById(id + 1000000))
						.setText(WhatYouEat.foods[foodId]);
				currentCalc.remove(new Long(foodId));
			}

			WhatYouEat.allMyFoods.get(WhatYouEat.findGroupFromFoodID(foodId))
					.put(foodId, false);
			WhatYouEat.saveObject(WhatYouEat.allMyFoods, "MYFOODS");
		}

	};
	private static final int CALCULATE = 341301473;
	private static final int CLEAR = 31349;
	private static final int SUGGEST = 606387;

	NutritionCalculatorView() {
		super(WhatYouEat.ctx);//

		this.setOrientation(VERTICAL);
		// /////////////////////////////////////

		myNutritionBuilder = new SubScroll(WhatYouEat.ctx);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT, 1.0f);
		myNutritionBuilder.setLayoutParams(params);
		foodQuantityInput = myNutritionBuilder.basicLinearLayout();
		buildCalculator();

	}

	// /////////////////////////////////////////
	public void searchforNutritionalFoods() {

		// static float[] myNutritionalState;
		// static float[] compareToGoal; //atan of ratio 1.57
		// static HashMap<Integer, Float> myFoodQuantities = new
		// static HashMap<Integer, String> myFoodUnits = new HashMap<Integer,
		// static boolean[] myGoodNutrients;
		// static float[] goodNutritionGoals;

		// static String[][] oddUnits = new String[FOOD_COUNT][];//
		// static float[][] metricConversion = new float[FOOD_COUNT][];//
		// static float[][] quantityFactor = new float[FOOD_COUNT][];//
		ArrayList<Integer> h = new ArrayList<Integer>();
		ArrayList<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i <130; i++) {
			
			if (!WhatYouEat.MY_NUTRIENTS[i]  )
				continue;
			if (WhatYouEat.myGoodNutrients[i] && WhatYouEat.goodNutritionGoals[i] > 0) {
				h.add(i);
			} else {
				l.add(i);
			}
		}
		{

			long time = System.currentTimeMillis();
			TreeMap<Float, Integer> foodRanking = new TreeMap<Float, Integer>();
			int[] foodsToSearch = WhatYouEat.getFoodSearchIds();

			float[] rankHigh = new float[WhatYouEat.FOOD_COUNT];
			float[] rankLow = new float[WhatYouEat.FOOD_COUNT];
			if (false)
				for (int i = 0; i < WhatYouEat.FOOD_COUNT; i++) {
					rankHigh[i] = -100;
					rankLow[i] = 100;
				}
			for (int i : foodsToSearch) {
				rankHigh[i] = 1;
				rankLow[i] = 1;
			}
			float null_rank_constant = 0;
			float measuredFactor = 0.0f;

			for (int nutrientID : h) {

				float[] nstat = WhatYouEat.highlightFactors.get(nutrientID);
				float min = nstat[0];
				float mean = nstat[1];
				float max = nstat[2];
				// OPTIONS FOR SEARCHING 1 FOOD GROUP/ MYFOODS/ MYFOODGROUPS
				for (int i : foodsToSearch) {

					float dataPoint = WhatYouEat.db[nutrientID][i];

					if (dataPoint < 0) {
						rankHigh[i] += null_rank_constant;
						continue;
					}

					switch (WhatYouEat.Nutrient_Measure) {
					case WhatYouEat.USING_KCAL:
						dataPoint = WhatYouEat.getPer100KcalDataPoint(i,
								dataPoint);

						break;
					case WhatYouEat.USING_SERVING:
						dataPoint = WhatYouEat.getPerServingDataPoint(i,
								dataPoint);
						if (dataPoint < 0) {

							dataPoint = WhatYouEat.db[nutrientID][i];
						}
						break;
					case WhatYouEat.USING_GRAMS:

						break;
					// ADD CASE ID_PER_SERVING:
					default:

						break;
					}

					float relativeData = dataPoint / max;

					relativeData = (float) (Math.atan(relativeData));
					// ranks[i] -=
					// WhatYouEat.getRelativeAbundance(nutrientID,dataPoint);
				 
				 
 
						rankHigh[i] += (measuredFactor + relativeData)
								* (Math.PI/2-  compareToGoal[nutrientID]);
						// the higher rank in obtaining goals the lower rank
						// rankHigh[i] *= (2.5 - compareToGoal[nutrientID]);
 

					rankHigh[i] -= (Math.random() / 100000000000.0);
				}
			}

			for (int nutrientID : l) {

				float[] nstat = WhatYouEat.highlightFactors.get(nutrientID);
				float min = nstat[0];
				float mean = nstat[1];
				float max = nstat[2];

				for (int i : foodsToSearch) {

					float dataPoint = WhatYouEat.db[nutrientID][i];
					if (dataPoint < 0) {
						rankLow[i] += null_rank_constant;
						continue;

					}

					switch (WhatYouEat.Nutrient_Measure) {
					case WhatYouEat.USING_KCAL:
						dataPoint = WhatYouEat.getPer100KcalDataPoint(i,
								dataPoint);

						break;
					case WhatYouEat.USING_SERVING:
						dataPoint = WhatYouEat.getPerServingDataPoint(i,
								dataPoint);
						if (dataPoint < 0) {
							dataPoint = WhatYouEat.db[nutrientID][i];
						}
						break;
					case WhatYouEat.USING_GRAMS:

						break;
					// ADD CASE ID_PER_SERVING:
					default:

						break;
					}
					float relativeData = dataPoint / max;

					relativeData = (float) (Math.atan(relativeData));
					// ranks[i] -=
					// WhatYouEat.getRelativeAbundance(nutrientID,dataPoint);
			 
						rankLow[i] += (measuredFactor + relativeData)
								* ( compareToGoal[nutrientID]);
			 
					rankLow[i] -= (Math.random() / 100000000000.0);
				}
			}
			int ctr = 0;
			for (int i : foodsToSearch) {
				float highRank = -(rankHigh[i]);
				float lowRank = (rankLow[i]);
				// order of tree set is from lowest to highest
				// high ranking use negative
				foodRanking.put(lowRank + highRank, i);
			}
			int counter = 0;
			int MAX = foodRanking.size() - 1;
			if (MAX > 300)
				MAX = 300;
			if (MAX < 1)
				MAX = 2;
			String[] priorityResults = new String[MAX];
			int[] pResIDs = new int[MAX];
			for (int rank : foodRanking.values()) {

				priorityResults[counter] = WhatYouEat.foods[rank];
				pResIDs[counter] = rank;

				if (counter++ == MAX - 1)
					break;
			}

			System.out.println("---::>DONE SEARCH:  "
					+ (System.currentTimeMillis() - time));
			WhatYouEat.searchResults = priorityResults;
			WhatYouEat.foodCodeResults = pResIDs;
			// WhatYouEat.appAccess.removeViewAt(2);
			removeOtherViews();
			FoodDescriptionsScroller fds = new FoodDescriptionsScroller(
					WhatYouEat.ctx, true);
			fds.setId(100010011);
			WhatYouEat.appAccess.addView(fds);

		}// END OF SEARCH
	}

	public void removeOtherViews(){

		WhatYouEat.appAccess.removeView(WhatYouEat.appAccess
				.findViewById(10001000));
		WhatYouEat.appAccess.removeView(WhatYouEat.appAccess
				.findViewById(101010101));
		WhatYouEat.appAccess.removeView(WhatYouEat.appAccess
				.findViewById(100010011));
		WhatYouEat.appAccess.removeView(WhatYouEat.appAccess
				.findViewById(SubScroll.FOOD_NUTRIENT_ID));
		WhatYouEat.appAccess.removeView(WhatYouEat.appAccess
				.findViewById(SubScroll.STATVIEW_ID));
		
	}
	// ////////////////////////////////////////////////////////////////////////////////
	protected void compareThisDietToNutritionTargets() {

		// IF(GOOD NUTRIENT)->
		// IF GOAL > 0 DO PERCENTAGE TO GOAL
		// IF SUM < 95%GOAL COLOR RED
		// IF SUM >=95%GOAL && <= 105% COLOR LIGHTGREEN
		// IF SUM > 105%GOAL COLOR GREEN
		// IF(BAD NUTRIETN) - > DO OPPOSITE
		myNutritionalState = new float[130];
		compareToGoal = new float[130];
		// static ArrayList<TreeMap<Integer, Boolean>> allMyFoods = new
		// ArrayList<TreeMap<Integer, Boolean>>();
		for (long f_d : currentCalc) {
			int foodID = (int) (f_d);
			//
			// if (WhatYouEat.allMyFoods.get(
			// WhatYouEat.findGroupFromFoodID(ent.getKey())).get(ent.getKey()))
			// {
			for (int i = 0; i < 130; i++) {
				// if i not in my food groups dont add to scroll
				// if(false)
				if (!WhatYouEat.MY_NUTRIENTS[i])
					continue;
				float nutritionPer100g = WhatYouEat.db[i][foodID];
				if (nutritionPer100g < 0
						|| WhatYouEat.myFoodQuantities.get(foodID) == null)
					continue;
				float servingSize = WhatYouEat.myFoodQuantities.get(foodID);// user
																			// chosen
																			// quantity
				// int smallestServingUnit =
				// WhatYouEat.optimalServingId[foodID];
				int currentUnitID = WhatYouEat.myFoodUnitIDs.get(foodID);
				float gramsPerServing = 100;
				float servingQuantity = 1;
				gramsPerServing = WhatYouEat.metricConversion[foodID][currentUnitID];
				servingQuantity = WhatYouEat.quantityFactor[foodID][currentUnitID];

				if (servingQuantity <= 0)
					servingQuantity = 1;
				float conversionFactor = servingSize * gramsPerServing
						/ servingQuantity;// COMPUTE THIS
				// FOR EACH FOOD IF ENT.GETVALUE()
				myNutritionalState[i] += nutritionPer100g * conversionFactor
						/ 100.0f;

			}
		}
		// state compared to goal default zero
		//QUESTION HOW DO DEFAULTS EFFECT CALCULATIONS  
		//what if goal is zero ie not set
		for (int i = 0; i < 130; i++) {
			// if i not in my food groups dont add to scroll
			// if(false)
			if (!WhatYouEat.MY_NUTRIENTS[i] || myNutritionalState[i] < 0)
				continue;

			float actualNutrition = myNutritionalState[i];
			actualNutrition = (float) (((long) (1000 * actualNutrition)) / 1000);
			float nutritionGoal = WhatYouEat.goodNutritionGoals[i];

			if (nutritionGoal > 0) {
				float ratio = actualNutrition / nutritionGoal;
				compareToGoal[i] = (float) Math.atan(ratio);
			}
		}

		for(int i = 0;i<compareToGoal.length;i++){
			System.out.println("calculate: "+i+"    "+compareToGoal[i]);
		}
	}// /////////////////

	public void displayNutritionState() {
		// COMPARE RESULTS TO GOALS; DISPLAY
		removeOtherViews();
		
		SubScroll compareNutrients = new SubScroll(WhatYouEat.ctx);

		LinearLayout framed = compareNutrients.basicLinearLayout();
		framed.setId(101010101);
		WhatYouEat.appAccess.addView(framed);

		TextView title = SubScroll.textView();
		title.setBackgroundColor(0xff223377);
		title.setText("Current Diet Relative to Goals");
		framed.addView(title);
		framed.addView(compareNutrients);
		Button doSomething = SubScroll.simpleButton();
		doSomething.setText("do something");
		doSomething.setOnClickListener(null);
		framed.addView(doSomething);
		for (int i = 0; i < 130; i++) {
			// if i not in my food groups dont add to scroll
			// if(false)
			if (!WhatYouEat.MY_NUTRIENTS[i] || myNutritionalState[i] < 0)
				continue;

			Button nutrB = SubScroll.simpleButton();
			nutrB.setId(i);
			nutrB.setOnClickListener(foodSuggestionListener);

			float actualNutrition = myNutritionalState[i];
			actualNutrition = (float) (((long) (1000 * actualNutrition)) / 1000);
			float nutritionGoal = WhatYouEat.goodNutritionGoals[i];

			String buttonTitle = "Intake of " + actualNutrition + " "
					+ WhatYouEat.units[i] + " of [" + WhatYouEat.nutrients[i]
					+ "] ";

			if (nutritionGoal <= 0) {
				nutrB.setTextColor(0xffaaaaaa);
				buttonTitle += " ;goal: not set";
				if (WhatYouEat.myGoodNutrients[i]) {
					nutrB.setBackgroundColor(0xaa227744);

				} else {

					nutrB.setBackgroundColor(0xaa772244);
				}
			} else {
				nutrB.setTextColor(0xffffffff);
				float ratio = actualNutrition / nutritionGoal;
				
				//compareToGoal[i] = ratio;
				compareToGoal[i] = (float) Math.atan(ratio);
				buttonTitle += " is " + (long) (100 * ratio) + "% of goal: "
						+ WhatYouEat.goodNutritionGoals[i] + " "
						+ WhatYouEat.units[i];
				int color = colorToGoal(ratio, WhatYouEat.myGoodNutrients[i]);

				nutrB.setBackgroundColor(color);
			}

			
			nutrB.setText(buttonTitle);
			compareNutrients.instanceChild.addView(nutrB);
		}
		// /////////////////////////////////////////////////
		final ViewTreeObserver viewTreeObserver4 = WhatYouEat.application
				.getViewTreeObserver();
		if (viewTreeObserver4.isAlive()) {
			viewTreeObserver4
					.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
						@Override
						public void onGlobalLayout() {

							WhatYouEat.application.scrollBy((int) (350), 0);
							viewTreeObserver4
									.removeGlobalOnLayoutListener(this);
						}
					});
		} 
		for(int i = 0;i<compareToGoal.length;i++){
			System.out.println("display: "+i+"    "+compareToGoal[i]);
		}
	}

	public static int colorToGoal(float ratioToGoal, boolean goodNutrient) {
		int color = 0x00ff0000;
		if (goodNutrient)
			color = 0x0000ff00;
		int alpha = 30 + ((int) (200 * Math.atan(ratioToGoal)));
		if (alpha > 255)
			alpha = 255;
		return alpha << 24 | color;

	}

	private void buildCalculator() {
		// layout to add grouptitle/food scroller and button at bottom
		boolean alternatecolor = true;
		for (int i = 0; i < 25; i++) {
			// if i not in my food groups dont add to scroll
			// if(false)
			if (!WhatYouEat.MY_FOOD_GROUPS[i])
				continue;

			// title button with kws.inflatorlistener id i
			TextView groupTitle = SubScroll.textView();
			if (alternatecolor) {
				groupTitle.setBackgroundColor(0xff55cccc);
				alternatecolor = false;
			} else {

				groupTitle.setBackgroundColor(0xff5555dd);
				alternatecolor = true;
			}
			groupTitle.setTextColor(0xff000000);
			groupTitle.setText(WhatYouEat.foodGroups[i]);
			myNutritionBuilder.instanceChild.addView(groupTitle);
			// allMyFoods = new ArrayList<TreeMap<Integer, Boolean>>();
			for (Entry<Integer, Boolean> ent : WhatYouEat.allMyFoods.get(i)
					.entrySet()) {
				LinearLayout horizontalL = new LinearLayout(WhatYouEat.ctx);

				int foodID = ent.getKey();
				horizontalL.setId(foodID);// access layout to access button to
											// change title
				// //////////////////////////////////////////////////////
				CheckBox selected = new CheckBox(WhatYouEat.ctx);
				String foodQ = "";

				if (WhatYouEat.allMyFoods.get(
						WhatYouEat.findGroupFromFoodID(foodID)).get(foodID)) {
					selected.setChecked(true);
					foodQ += addFullUnitText(foodID);
				} else {
					foodQ += WhatYouEat.foods[foodID];
				}
				selected.setId(1000000 + foodID);
				// listener to change buttontitle and quantity and unit
				selected.setOnCheckedChangeListener(foodInclusionListener);
				horizontalL.addView(selected);
				// ////////////////////////////////////////////////////
				Button foodButton = SubScroll.simpleButton();// new
																// Button(WhatYouEat.ctx);
				foodButton.setText(foodQ);
				foodButton.setId(2000000 + foodID);
				foodButton.setOnClickListener(foodQuantityListener);
				// set color
				horizontalL.addView(foodButton);
				// /////////////////////////////////////////////////

				// /////////////////////////////////////////
				myNutritionBuilder.instanceChild.addView(horizontalL);
			}
			// for each food in this group
			// build horizontal layout
			// add checkbox true if true
			// add food title (textfield?)
			// add two spinners : on with quanitty on with unit
			// add to

		}

		this.addView(myNutritionBuilder);
		// ////////////////////////////////////////////

		LinearLayout hz = new LinearLayout(WhatYouEat.ctx);

		Button search = SubScroll.simpleButton();
		search.setText("Calculate");
		search.setId(CALCULATE);
		search.setOnClickListener(nutritionCalculaterListener);
if(false){
		Button clr = SubScroll.simpleButton();
		clr.setText("Clear");
		clr.setId(CLEAR);
		clr.setOnClickListener(nutritionCalculaterListener);
		 hz.addView(clr);
	}
		Button sug = SubScroll.simpleButton();
		sug.setText("Complement");
		sug.setId(SUGGEST);
		sug.setOnClickListener(nutritionCalculaterListener);

		hz.addView(search);
		hz.addView(sug);

		this.addView(hz);

	}

	public String addFullUnitText(int foodID) {
		String qfd = "";
		if (WhatYouEat.myFoodQuantities.containsKey(foodID)
				&& WhatYouEat.myFoodUnits.containsKey(foodID)) {
			qfd += " " + WhatYouEat.myFoodQuantities.get(foodID);
			qfd += " [" + WhatYouEat.myFoodUnits.get(foodID) + "] of ";
			qfd += WhatYouEat.foods[foodID];
		} else {
			qfd += "[0]" + WhatYouEat.foods[foodID];
		}
		return qfd;
	}

}
