package co.harlequinmettle.healthfoodconcepts;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionsMenu extends SubScroll {
	CompoundButton.OnCheckedChangeListener dbsaver = new CompoundButton.OnCheckedChangeListener() {
	 	@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				new Thread(new ObjectStoringThread(context, WhatYouEat.db)).start();
				WhatYouEat.saveObject(1, "SAVEDB");
			}else{
//delete saved data?
				WhatYouEat.saveObject(0, "SAVEDB");
			}
		}
	};
	RadioGroup.OnCheckedChangeListener setState = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// clear previously stored statistics now need other stats per
			// 100kcal or per 100g
			if (!WhatYouEat._loaded || WhatYouEat.calculatingStats) {
				return;
			}

			WhatYouEat.nutritionStats.clear();
			switch (checkedId) {
			case ID_PER_SERVING:
				WhatYouEat.Nutrient_Measure = WhatYouEat.USING_SERVING;
				break;
			case ID_PER_100_KCALS:
				WhatYouEat.Nutrient_Measure = WhatYouEat.USING_KCAL;
				break;
			case ID_PER_100_GRAMS:
				WhatYouEat.Nutrient_Measure = WhatYouEat.USING_GRAMS;
				break;
			case WhatYouEat.USE_MY_FOOD_GROUPS:
				WhatYouEat.Foods_Search = WhatYouEat.USE_MY_FOOD_GROUPS;
				// WhatYouEat.setFoodsIds();
				break;
			case WhatYouEat.USE_ALL_FOODS:
				WhatYouEat.Foods_Search = WhatYouEat.USE_ALL_FOODS;
				// WhatYouEat.setFoodsIds();
				break;
			case WhatYouEat.USE_MY_FOODS:
				WhatYouEat.Foods_Search = WhatYouEat.USE_MY_FOODS;
				// WhatYouEat.setFoodsIds();
				// .setChecked(true);
				break;
			case WhatYouEat.SEARCH_TYPE_SUM:
				WhatYouEat.Search_Type = WhatYouEat.SEARCH_TYPE_SUM;

				break;
			case WhatYouEat.SEARCH_TYPE_PRODUCT:
				WhatYouEat.Search_Type = WhatYouEat.SEARCH_TYPE_PRODUCT;
break;

			case 111111111:
			//case WhatYouEat.VIEW_HORIZONTAL:
				 //	WhatYouEat.myFoods_View = WhatYouEat.VIEW_HORIZONTAL;

				break;
			case WhatYouEat.VIEW_VERTICAL:
				WhatYouEat.myFoods_View = WhatYouEat.VIEW_VERTICAL;
				break;
				// ADD CASE ID_PER_SERVING:
			default:

				break;
			}

			if (checkedId < 25) {
				WhatYouEat.Foods_Search = WhatYouEat.USE_ONE_FOOD_GROUP;
				WhatYouEat.Food_Group = checkedId;
			}else{
			WhatYouEat.saveObject(WhatYouEat.Nutrient_Measure, "SEARCHUNITS");

			if (WhatYouEat.Foods_Search == WhatYouEat.USE_ALL_FOODS ||
					WhatYouEat.Foods_Search== WhatYouEat.USE_MY_FOOD_GROUPS   ||
					WhatYouEat.Foods_Search == WhatYouEat.USE_MY_FOODS  )
			WhatYouEat.saveObject(WhatYouEat.Foods_Search, "SEARCHFOODS");
			}
			WhatYouEat.setFoodsIds();
			WhatYouEat.highlightFactors.clear();
			WhatYouEat.calculateHighlightNumbers();

		}

	};

	public OptionsMenu(Context context) {
		super(context);
		boolean setFoodGroup = false;
		CheckBox optimize = new CheckBox(WhatYouEat.ctx);
		if (WhatYouEat.save_db == 1) {
			optimize.setChecked(true);
		}
		optimize.setId(848484848);
		optimize.setOnCheckedChangeListener(dbsaver );
		optimize.setText("Optimize database loading - requires 4 MB - loads 4X as fast");
		optimize.setMaxWidth(SubScroll.MAX_BUTTON_WIDTH);
		instanceChild.addView(optimize);
		////////////////////////////////////////////////
		RadioGroup cal = new RadioGroup(context);
		cal.setBackgroundColor(RADIO_OPTIONS_1_COLOR);
		cal.setOnCheckedChangeListener(setState);
		RadioButton gram100 = simpleRadioButton();
		RadioButton cal100 = simpleRadioButton();
		RadioButton serving = simpleRadioButton();

		gram100.setText("Nutrients per 100 grams");
		cal100.setId(ID_PER_100_KCALS);

		cal100.setText("Nutrients per 100 Calories");
		gram100.setId(ID_PER_100_GRAMS);

		serving.setText("Nutrients per serving");
		serving.setId(ID_PER_SERVING);

		switch (WhatYouEat.Nutrient_Measure) {
		case WhatYouEat.USING_KCAL:
			cal100.setChecked(true);

			break;
		case WhatYouEat.USING_GRAMS:
			gram100.setChecked(true);

			break;
		case WhatYouEat.USING_SERVING:
			serving.setChecked(true);

			break;
		// ADD CASE ID_PER_SERVING:
		default:

			break;
		}
		cal.addView(serving);
		cal.addView(gram100);
		cal.addView(cal100);
		instanceChild.addView(cal);

		// //////////////////////////////////////////////

		// //////////////////////////////////////////////
		if(false){
		RadioGroup algoritym = new RadioGroup(context);
		algoritym.setBackgroundColor(RADIO_OPTIONS_3_COLOR);
		algoritym.setOnCheckedChangeListener(setState);
		RadioButton sum = simpleRadioButton();
		RadioButton product = simpleRadioButton();

		sum.setText("Search algorithym: Sum");
		product.setText("Search algorithym: Product");
		sum.setId(WhatYouEat.SEARCH_TYPE_SUM);
		product.setId(WhatYouEat.SEARCH_TYPE_PRODUCT);
		switch (WhatYouEat.Search_Type) {
		case WhatYouEat.SEARCH_TYPE_SUM:
			sum.setChecked(true);

			break;
		case WhatYouEat.SEARCH_TYPE_PRODUCT:
			product.setChecked(true);
			break;
		// ADD CASE ID_PER_SERVING:
		default:

			break;
		}

		algoritym.addView(sum);
		algoritym.addView(product);
		instanceChild.addView(algoritym);
		}
		// /////////////////////////////////////
		// //////////////////////////////////////////////
		if(false){
		RadioGroup foodView = new RadioGroup(context);
		foodView.setBackgroundColor(RADIO_OPTIONS_3_COLOR);
		foodView.setOnCheckedChangeListener(setState);
		RadioButton horizontal = simpleRadioButton();
		RadioButton vertical = simpleRadioButton();

		horizontal.setText("MyFoods view: horizontal");
		vertical.setText("MyFoods view: vertical");
		//horizontal.setId(WhatYouEat.VIEW_HORIZONTAL);
		vertical.setId(WhatYouEat.VIEW_VERTICAL);
		switch (WhatYouEat.myFoods_View) {
		case 10:
		//case WhatYouEat.VIEW_HORIZONTAL:
			horizontal.setChecked(true);

			break;
		case WhatYouEat.VIEW_VERTICAL:
			vertical.setChecked(true);
			break;
		// ADD CASE ID_PER_SERVING:
		default:

			break;
		}

		foodView.addView(vertical);
		foodView.addView(horizontal);
		instanceChild.addView(foodView);
		}
		// /////////////////////////////////////
		RadioGroup search = new RadioGroup(context);
		search.setBackgroundColor(RADIO_OPTIONS_4_COLOR);
		search.setOnCheckedChangeListener(setState);
		RadioButton myfoodgroups = simpleRadioButton();
		RadioButton allfoodgroups = simpleRadioButton();
		myfoodgroups.setBackgroundColor(RADIO_OPTIONS_2_COLOR);
		allfoodgroups.setBackgroundColor(RADIO_OPTIONS_2_COLOR);
		myfoodgroups.setText("Search my food groups");
		allfoodgroups.setText("Search all foods");
		myfoodgroups.setId(WhatYouEat.USE_MY_FOOD_GROUPS);
		allfoodgroups.setId(WhatYouEat.USE_ALL_FOODS);
		switch (WhatYouEat.Foods_Search) {
		case WhatYouEat.USE_MY_FOOD_GROUPS:
			myfoodgroups.setChecked(true);

			break;
		case WhatYouEat.USE_ALL_FOODS:
			allfoodgroups.setChecked(true);

			break;
		case WhatYouEat.USE_ONE_FOOD_GROUP:
			// .setChecked(true);
			setFoodGroup = true;
			break;
		case WhatYouEat.USE_MY_FOODS:
			// .setChecked(true);

			break;
		// ADD CASE ID_PER_SERVING:
		default:

			break;
		}

		search.addView(myfoodgroups);
		search.addView(allfoodgroups);
		instanceChild.addView(search);
		// ///////////////////////////////////////
		// add all foodgroup radios
		// RadioGroup fdgp = new RadioGroup(context);
		// fdgp.setBackgroundColor(RADIO_OPTIONS_4_COLOR);
		// fdgp.setOnCheckedChangeListener(setState);
		for (int i = 0; i < WhatYouEat.foodGroups.length; i++) {
			RadioButton grp = simpleRadioButton();
			if (setFoodGroup && WhatYouEat.Food_Group == i) {
				grp.setChecked(true);
			}
			grp.setText("Search only: " + WhatYouEat.foodGroups[i]);
			grp.setId(i);
			// fdgp.addView(grp);
			search.addView(grp);
		}
		// instanceChild.addView(fdgp);
	}

}
