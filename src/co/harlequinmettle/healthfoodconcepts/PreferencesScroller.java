package co.harlequinmettle.healthfoodconcepts;

import java.util.Map.Entry;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreferencesScroller extends SubScroll {
	CheckBox[] ckbox;

	// effect of touching a specific food description button:
	// show detailed nutrition information
	View.OnClickListener foodGroupListener = new View.OnClickListener() {
		public void onClick(View view) {
			int id = view.getId();// food id code
			Button b = (Button) view;
			// SET INDIVIDUAL GROUP PREFERENCES FROM CHECKBOXES AND SAVE
		}
	};
	CompoundButton.OnCheckedChangeListener foodGroupPreferencesListener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			WhatYouEat.MY_FOOD_GROUPS[buttonView.getId()] = isChecked;
			WhatYouEat.saveObject(WhatYouEat.MY_FOOD_GROUPS, "MYFOODGROUPS");
			if (WhatYouEat.USE_MY_FOOD_GROUPS == WhatYouEat.Foods_Search)
				WhatYouEat.setFoodsIds();
		}
	};
	CompoundButton.OnCheckedChangeListener nutrientsPreferencesListener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			int bID = buttonView.getId();
			if (isChecked) {
				WhatYouEat.nutritionStats.put(bID,
						new StatTool(WhatYouEat.getFoodSearchIds(), bID));
			}
			WhatYouEat.MY_NUTRIENTS[bID] = isChecked;
			int[] foodIds = WhatYouEat.getFoodSearchIds();
			float[] basically = StatTool.simpleStats(foodIds, bID);

			WhatYouEat.highlightFactors.put(bID, basically);

			WhatYouEat.saveObject(WhatYouEat.MY_NUTRIENTS, "MYNUTRIENTS");

		}
	};

	PreferencesScroller(Context c, int type) {
		super(c);

		// choose what food groups to use
		if (type == 0) {
			int[] ids = new int[25];
			for (int i = 0; i < 25; i++)
				ids[i] = i;

			addScrollingButtons(WhatYouEat.foodGroups, ids,
					WhatYouEat.MY_FOOD_GROUPS, foodGroupListener,
					FOOD_GROUP_LABEL_COLOR, type);

		}
		//
		if (type == 1) {
	
			int[] ids = new int[130];
			for (int i = 0; i < 130; i++)
				ids[i] = i;

			addScrollingButtons(WhatYouEat.nutrients, ids,
					WhatYouEat.MY_NUTRIENTS, foodGroupListener,
					BUTTON_COLORS[GROUPPREFS], type);

		}
		if (type == 2) {
			boolean alternatecolor = true;
			for (int i = 0; i < 25; i++) {
				// if i not in my food groups dont add to scroll
				// if(false)
				if (!WhatYouEat.MY_FOOD_GROUPS[i])
					continue;
				// Scroller - label button doesn't scroll
				//
				MyFoodsScroller mfs = new MyFoodsScroller(context, i, false);
				mfs.setId(i);

				// linear layout to prevent title button from scrolling
				LinearLayout title = basicLinearLayout();
				// title button with kws.inflatorlistener id i
				TextView groupTitle = new TextView(context);
				if (false)
					if (i % 2 == 0) {
						groupTitle.setBackgroundColor(0xff9999dd);
					} else {
						groupTitle.setBackgroundColor(0xffdd9999);
					}
				if(alternatecolor){
					groupTitle
							.setBackgroundColor(0xff55cccc );
		alternatecolor = false;
		}else{

			groupTitle
					.setBackgroundColor( 0xff5555dd);
			alternatecolor = true;
		}
				groupTitle.setTextSize(22);
				groupTitle.setTextColor(0xff000000);
				groupTitle.setText(WhatYouEat.foodGroups[i]);
				groupTitle.setWidth(370);
				groupTitle.setHeight(46);

				switch (WhatYouEat.myFoods_View) {
			//	case WhatYouEat.VIEW_HORIZONTAL:
				case 100000000://never
				mfs.addView(mfs.instanceChild);
					title.addView(groupTitle);
					// add uninflated keywordscroller to linear layout
					title.addView(mfs);
					// add linear layout to application - default possition
					WhatYouEat.appAccess.addView(title);
					break;
				case WhatYouEat.VIEW_VERTICAL:
					instanceChild.addView(groupTitle);
					instanceChild.addView(mfs.instanceChild);

					break;
				// ADD CASE ID_PER_SERVING:
				default:

					break;
				}
				// add title button to layout

			}

		}

	}

	public void addScrollingButtons(String[] buttonNames, int[] buttonIds,
			boolean[] settings, View.OnClickListener listener, int BUTTON_CLR,
			int type) {

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
			if (type == 0) {
				buttons[i].getBackground().setColorFilter(BUTTON_CLR,
						PorterDuff.Mode.MULTIPLY);
			} else if (type == 1) {

				int bgColor = GENERAL;
				if (i >= 8 && i <= 14) {
					bgColor = INTERESTING;
				}
				if (i >= 15 && i <= 24) {
					bgColor = CARB;

				}
				if (i >= 25 && i <= 35) {
					bgColor = MINERAL;

				}
				if (i >= 36 && i <= 65) {
					bgColor = VITAMIN;

				}
				if (i >= 66 && i <= 84) {
					bgColor = PROTEIN;

				}
				if (i >= 85 && i <= 129) {
					bgColor = FAT;

				}
				int alpha = 150;
				int colorCombo = bgColor | (alpha << 24);

				buttons[i].getBackground().setColorFilter(colorCombo,
						PorterDuff.Mode.SRC_IN);

			}
			ckbox[i] = new CheckBox(context);

			ckbox[i].setChecked(settings[i]);
			if (type == 0)
				ckbox[i].setOnCheckedChangeListener(foodGroupPreferencesListener);
			if (type == 1)
				ckbox[i].setOnCheckedChangeListener(nutrientsPreferencesListener);

			ckbox[i].setId(buttonIds[i]);// CHANGE TO ACTUAL FOOD ID
			layout.addView(ckbox[i]);
			layout.addView(buttons[i]);
			instanceChild.addView(layout);
		}

	}

}
