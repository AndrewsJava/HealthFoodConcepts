package co.harlequinmettle.healthfoodconcepts;

import java.util.TreeMap;

import android.content.Context;
import android.view.View;

public class FoodRemovalScroller extends SubScroll{


	View.OnClickListener foodRemovalListener = new View.OnClickListener() {
		public void onClick(View view) {
	
	for(TreeMap<Integer, Boolean> foodi : WhatYouEat.allMyFoods){
		foodi.remove(view.getId());
	 removeButtonById(view.getId());
	} 
WhatYouEat.saveObject(WhatYouEat.allMyFoods, "MYFOODS");
}

};
	
	
	
	FoodRemovalScroller(Context c) {
		super(c);
	//	 addScrollingButtons(String[] buttonNames, int[] buttonIds, View.OnClickListener listener, int BUTTON_CLR) {
	addScrollingButtons(WhatYouEat.getMyFoodsAsTextArray(),WhatYouEat.getMyFoodsIdsArray(),foodRemovalListener,0);
	
	}


	private void removeButtonById(int id) {
		instanceChild.removeView(instanceChild.findViewById(id));
	}
	
	
}
