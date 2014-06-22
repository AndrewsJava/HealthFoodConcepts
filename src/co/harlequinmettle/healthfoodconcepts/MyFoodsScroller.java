package co.harlequinmettle.healthfoodconcepts;

import java.util.Map.Entry;
import java.util.TreeMap;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

public class MyFoodsScroller extends SubScroll {
	CheckBox[] ckbox;
	int groupID;
	// effect of touching a specific food description button:
	// show detailed nutrition information
	View.OnClickListener nutritionInfoListener = new View.OnClickListener() {
		public void onClick(View view) {
			int id = view.getId();// food id code
			Button b = (Button) view;
			currentFoodID = id;
			b.getBackground()
					.setColorFilter(0xff999999, PorterDuff.Mode.SRC_IN);
			if (showingNutrients)
				WhatYouEat.appAccess.removeView(WhatYouEat.appAccess
						.findViewById(FOOD_NUTRIENT_ID));

		 NutritionScroller nutr = new NutritionScroller(context, id);

			if (showingStats) {
				WhatYouEat.appAccess.removeView(WhatYouEat.appAccess
						.findViewById(STATVIEW_ID));
				showingStats = false;
			}
			nutr.setId(FOOD_NUTRIENT_ID);
			index = groupID + 2;
		 switch(WhatYouEat.myFoods_View){
		// case WhatYouEat.VIEW_HORIZONTAL:
		 case 10:

				WhatYouEat.appAccess.addView(nutr, index);
				break;
			case WhatYouEat.VIEW_VERTICAL:

				WhatYouEat.appAccess.addView(nutr, 2);
				break;
				default:
					break;
			}

			WhatYouEat.application.scrollBy((int) (MAX_BUTTON_WIDTH * 0.9), 0);
			showingNutrients = true;
		}
	};

	CompoundButton.OnCheckedChangeListener foodPreferencesListener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			int foodId = buttonView.getId();

			WhatYouEat.allMyFoods.get(groupID).put(foodId, isChecked);

			WhatYouEat.saveObject(WhatYouEat.allMyFoods, "MYFOODS");
		}
	};

	MyFoodsScroller(Context c, int groupID, boolean addChild) {
		super(c , addChild);
		this.groupID = groupID;
		TreeMap<Integer, Boolean> myGroupFoods = WhatYouEat.allMyFoods
				.get(groupID);
		if (myGroupFoods.size() > 0) {
			String[] myFoodNames = new String[myGroupFoods.size()];
			int[] myFoodNumbers = new int[myGroupFoods.size()];
			boolean[] myFoodBool = new boolean[myGroupFoods.size()];
			int i = 0;
			for (Entry<Integer, Boolean> ent : myGroupFoods.entrySet()) {
				myFoodNames[i] = WhatYouEat.foods[ent.getKey()];
				myFoodNumbers[i] = ent.getKey();
				myFoodBool[i] = ent.getValue();
				i++;
			}
			addScrollingButtons(myFoodNames, myFoodNumbers, myFoodBool,
					BUTTON_COLORS[GROUPPREFS]);

		}

	}

	public void addScrollingButtons(String[] buttonNames, int[] buttonIds,
			boolean[] settings, int BUTTON_CLR) {

		// instanceChild.removeAllViews();
		buttons = new Button[buttonNames.length];

		ckbox = new CheckBox[buttonNames.length];

		for (int i = 0; i < buttonNames.length; i++) {
			LinearLayout layout = new LinearLayout(context);
			buttons[i] = simpleButton();
			buttons[i].setText(buttonNames[i]);
			buttons[i].setId(buttonIds[i]);
			// NAVIGATION or SEARCHWORDS or FOODINFO
			buttons[i].setOnClickListener(nutritionInfoListener);
			buttons[i].getBackground().setColorFilter(BUTTON_CLR,
					PorterDuff.Mode.MULTIPLY);

			ckbox[i] = new CheckBox(context);

			ckbox[i].setChecked(settings[i]);

			ckbox[i].setOnCheckedChangeListener(foodPreferencesListener);
			ckbox[i].setId(buttonIds[i]);// CHANGE TO ACTUAL FOOD ID
			layout.addView(ckbox[i]);
			layout.addView(buttons[i]);
			instanceChild.addView(layout);
		}

	}
}
