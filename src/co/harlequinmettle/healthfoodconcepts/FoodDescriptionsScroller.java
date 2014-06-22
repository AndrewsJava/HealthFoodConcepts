package co.harlequinmettle.healthfoodconcepts;

import java.util.TreeMap;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

public class FoodDescriptionsScroller extends SubScroll {
	CheckBox[] ckbox;

	CompoundButton.OnCheckedChangeListener foodPreferencesListener = new CompoundButton.OnCheckedChangeListener(	) {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			int foodId = buttonView.getId();
			
			//MUST GET FOODGROUP ID HERE
			int groupID = WhatYouEat.findGroupFromFoodID(foodId);
			
			
			
			
			
	WhatYouEat.allMyFoods.get(groupID).put(foodId, false);
		WhatYouEat.saveObject(WhatYouEat.allMyFoods, "MYFOODS");
		}
	};
	// effect of touching a specific food description button:
	// show detailed nutrition information
	View.OnClickListener nutritionInfoListener = new View.OnClickListener() {
		public void onClick(View view) {
			int id = view.getId();// food id code
			Button b = (Button) view;
currentFoodID  =  id;
			b.getBackground()
					.setColorFilter(0xff999999, PorterDuff.Mode.SRC_IN);
			if (showingNutrients)
				WhatYouEat.appAccess.removeView(WhatYouEat.appAccess
						.findViewById(FOOD_NUTRIENT_ID));
 
			NutritionScroller nutr = new NutritionScroller(
					context,id);

			if(showingStats){
				WhatYouEat.appAccess.removeView(WhatYouEat.appAccess
						.findViewById(STATVIEW_ID));
				showingStats = false;
			}
			nutr.setId(FOOD_NUTRIENT_ID);
			index = lastFoodsId +1;
			if(SubScroll.fromKeywordSearch){
				index-=1;
			}
			if(SubScroll.fromFoodSuggestions){
				index+=2;
			}
			// WhatYouEat.viewHolder.add(index, results); 
			WhatYouEat.appAccess.addView(nutr, index);
			 
			WhatYouEat.application.scrollBy((int) (MAX_BUTTON_WIDTH*0.9), 0);
			showingNutrients = true;
			
			
			final ViewTreeObserver viewTreeObserver4 = WhatYouEat.application
			.getViewTreeObserver();
	if (viewTreeObserver4.isAlive()) {
		viewTreeObserver4
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {

						WhatYouEat.application.scrollBy(
								(int) (0.1 * MAX_BUTTON_WIDTH), 0);
						viewTreeObserver4
								.removeGlobalOnLayoutListener(this);
					}
				});
	}
			
		}
	};

	FoodDescriptionsScroller(Context c,boolean loadButtonsOnCreation) {
		super(c);
		this.setId(FOOD_RESULTS_ID); 
		// /override to add checkboxes
		if(loadButtonsOnCreation)
		addScrollingButtons(WhatYouEat.searchResults,
				WhatYouEat.foodCodeResults, nutritionInfoListener,
				BUTTON_COLORS[SEARCH_RESULTS]);
	}

	public void addScrollingButtons(String[] buttonNames, int[] buttonIds,
			View.OnClickListener listener, int BUTTON_CLR) {

		// instanceChild.removeAllViews();
		buttons = new Button[buttonNames.length];

		ckbox = new CheckBox[buttonNames.length];

		for (int i = 0; i < buttonNames.length; i++) {
			LinearLayout layout = new LinearLayout(context);
			buttons[i] = simpleButton();
			buttons[i].setText(buttonNames[i]);
			buttons[i].setId(buttonIds[i]);
			// NAVIGATION or SEARCHWORDS or FOODINFO
			buttons[i].setOnClickListener(listener);
			buttons[i].getBackground().setColorFilter(BUTTON_CLR,
					PorterDuff.Mode.MULTIPLY);

			buttons[i].setMinimumWidth(MAX_BUTTON_WIDTH-75);
			ckbox[i] = new CheckBox(context);
			ckbox[i].setId(buttonIds[i]);// CHANGE TO ACTUAL FOOD ID
			for(TreeMap<Integer,Boolean> myGroupsFoods: WhatYouEat.allMyFoods){
			if(myGroupsFoods.containsKey(buttonIds[i])){
			ckbox[i].setChecked(myGroupsFoods.get(buttonIds[i]));
			}
			}
			ckbox[i].setOnCheckedChangeListener(foodPreferencesListener);
			layout.addView(ckbox[i]);
			layout.addView(buttons[i]);
			instanceChild.addView(layout);
		}
 

	}

}
