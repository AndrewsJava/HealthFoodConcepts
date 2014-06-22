package co.harlequinmettle.healthfoodconcepts;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.Arrays;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NutritionSettings extends LinearLayout {
	SubScroll myNutrients;
	LinearLayout panel;
	EditText[] amounts = new EditText[130];
	CheckBox[] onoff = new CheckBox[130];
	
	View.OnClickListener saveSettingsListener = new View.OnClickListener() {
		public void onClick(View view) {
			int actionID = view.getId();
			// find EditText and get numbers - save
			switch (actionID) {
			case SAVE:
				WhatYouEat.myGoodNutrients = new boolean[130];
				for (int i = 0; i < 130; i++) {
					// if i not in my food groups dont add to scroll
					// if(false)
					WhatYouEat.myGoodNutrients[i] = true;
					if (!WhatYouEat.MY_NUTRIENTS[i])
						continue;
					float amount = 1;
					try{
						String amnt =  (amounts[i].getText().toString());
						String[] timeA = amnt.split("\\*");
						for(String q: timeA){
							amount*=Float.valueOf(q);
						} 
					}catch(NumberFormatException nfe){
						
					}
				WhatYouEat.goodNutritionGoals[i] = amount;
				WhatYouEat.myGoodNutrients[i] = onoff[i].isChecked();
				}
				WhatYouEat.saveObject(WhatYouEat.goodNutritionGoals, "GOODNUTRGOALS");
				WhatYouEat.saveObject(WhatYouEat.myGoodNutrients, "GOODNUTR");
				break;
			case RESET: 
				WhatYouEat.goodNutritionGoals = WhatYouEat.DEFAULT_NUTR_GOALS.clone();
				WhatYouEat.myGoodNutrients = WhatYouEat.DEFAULT_GOOD_NUTRIENTS.clone();
				for (int i = 0; i < 130; i++) {
					// if i not in my food groups dont add to scroll
					// if(false)
					if (!WhatYouEat.MY_NUTRIENTS[i])
						continue;
					if (WhatYouEat.myGoodNutrients[i]) {
						if(onoff!=null){
						onoff[i].setChecked(true);
						onoff[i].setBackgroundColor(0xff00ff00);
						}
						if (amounts[i] != null)
							amounts[i].setText(""
									+ WhatYouEat.goodNutritionGoals[i]);
					} else {
						if(onoff!=null){
						onoff[i].setChecked(false);
						onoff[i].setBackgroundColor(0xffff0000);
						}
						if (amounts[i] != null)
							amounts[i].setText(""
									+ WhatYouEat.goodNutritionGoals[i]);
							//amounts[i].setText("As low as possible");
					}
				}
				break;
			default:

				break;
			}
		}
	};

	CompoundButton.OnCheckedChangeListener nutrientBenefitListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			int id = buttonView.getId();
			// save wether or not this is a good nutrient

			if (isChecked) {
				buttonView.setChecked(true);
				buttonView.setBackgroundColor(0xff00ff00);
				if (amounts[id] != null)
					amounts[id].setText("" + WhatYouEat.goodNutritionGoals[id]);
				WhatYouEat.myGoodNutrients[id] = true;
			} else {

				buttonView.setChecked(false);
				buttonView.setBackgroundColor(0xffff0000);
				WhatYouEat.myGoodNutrients[id] = false;
				if (amounts[id] != null)
					amounts[id].setText("" + WhatYouEat.goodNutritionGoals[id]);
			}
		}
	};
	private static final int RESET = 43837;
	private static final int SAVE = 8473;

	public NutritionSettings() {
		super(WhatYouEat.ctx);
		this.setOrientation(VERTICAL);
		// /////////////////////////////////////
		myNutrients = new SubScroll(WhatYouEat.ctx);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT, 1.0f);
		myNutrients.setLayoutParams(params);
		panel = myNutrients.basicLinearLayout();
		buildSettingView();
	}

	private void buildSettingView() {
		for (int i = 0; i < 130; i++) {
			// if i not in my food groups dont add to scroll
			// if(false)
			if (!WhatYouEat.MY_NUTRIENTS[i])
				continue;

			LinearLayout horizontalL = new LinearLayout(WhatYouEat.ctx);

			horizontalL.setId(i);// access layout to access button to
									// change title
			// //////////////////////////////////////////////////////
			CheckBox selected = new CheckBox(WhatYouEat.ctx);
			onoff[i] = selected;
			selected.setWidth(250);
			selected.setText(WhatYouEat.nutrients[i]);
			selected.setId(i);
			// listener to change buttontitle and quantity and unit
			selected.setOnCheckedChangeListener(nutrientBenefitListener);
			horizontalL.addView(selected);
			EditText goal = SubScroll.editText("Enter Nutritient Goal");
			amounts[i] = goal;

			goal.setRawInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_VARIATION_NORMAL);
			if (WhatYouEat.myGoodNutrients[i]) {
				selected.setChecked(true);
				selected.setBackgroundColor(0xff00ff00);
				goal.setText("" + WhatYouEat.goodNutritionGoals[i]);
			} else {

				selected.setChecked(false);
				selected.setBackgroundColor(0xffff0000);

				goal.setText("" + WhatYouEat.goodNutritionGoals[i]);
			}
			horizontalL.addView(goal);

			TextView units = SubScroll.textView();
			units.setText("" + WhatYouEat.units[i]);

			horizontalL.addView(units);
			myNutrients.instanceChild.addView(horizontalL);
		}

		this.addView(myNutrients);
		LinearLayout hz = new LinearLayout(WhatYouEat.ctx);

		Button clr = SubScroll.simpleButton();
		clr.setText("Reset Defaults");
		clr.setId(RESET);
		clr.setOnClickListener(saveSettingsListener);

		Button save = SubScroll.simpleButton();
		save.setText("Save These Settings");
		save.setId(SAVE);
		save.setOnClickListener(saveSettingsListener);

		hz.addView(clr);
		hz.addView(save);

		this.addView(hz);
	}

}
