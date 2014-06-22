package co.harlequinmettle.healthfoodconcepts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.FrameLayout;

public class HistogramView extends FrameLayout {

	int _nPaints = 10;
	private Paint[] _paints = new Paint[_nPaints];
	private double sum = 0, sumOfSquares = 0, mean, median, min, max, n, range;
	private int size;
	private float upperLim;
	private int meanBar, pointBar;
	public double standardDeviation;

	ArrayList<Float> _data = new ArrayList<Float>();
	private int[] histogram;
	// use keybord to change parameters
	private int BAR_BUFFER = 2;
	// private int categories = 30;
	private RectF[] histoBars;
	int frameW, frameH, barMax;
	double barwidth;
	double eMin_0, eMax_3rd;
	String title, title2, title3, title4;
	float nutrientPt;
	float scale;
	StatTool boiledDown;
	int barcolor = 0xffcccccc;

	public HistogramView(Context context, StatTool blDn, int sw, int sh,int bgcolor) {
		super(context);
		this.setWillNotDraw(false);
this.barcolor = bgcolor;
		this.boiledDown = blDn;
		this.frameW = (int) (sw * 2.3f);
		this.frameH = sh;

		this.nutrientPt = WhatYouEat.db[SubScroll.currentNutrientID][SubScroll.currentFoodID];

		boolean useServingOK = true;

		switch (WhatYouEat.Nutrient_Measure) {
		case WhatYouEat.USING_KCAL:
			this.nutrientPt = WhatYouEat.getPer100KcalDataPoint(
					SubScroll.currentFoodID, this.nutrientPt);

			break;
		case WhatYouEat.USING_SERVING:
			this.nutrientPt = WhatYouEat.getPerServingDataPoint(
					SubScroll.currentFoodID, this.nutrientPt);
			if (this.nutrientPt < 0) {
				useServingOK = false;
				this.nutrientPt =WhatYouEat.db[SubScroll.currentNutrientID][SubScroll.currentFoodID];
			}
			break;
		case WhatYouEat.USING_GRAMS:

			break;
		// ADD CASE ID_PER_SERVING:
		default:

			break;
		}

		this.title = "Distribution of "
				+ WhatYouEat.nutrients[SubScroll.currentNutrientID]
				+ " in all foods in: "+WhatYouEat.getFoodsSearchDescription();

		this.title2 = "["+WhatYouEat.foods[SubScroll.currentFoodID]+"]" + " contains ";
		this.title3 =  this.nutrientPt + " "
				+ WhatYouEat.units[SubScroll.currentNutrientID]+" of ["+ WhatYouEat.nutrients[SubScroll.currentNutrientID]+"]";

		switch (WhatYouEat.Nutrient_Measure) {
		case WhatYouEat.USING_KCAL:
			this.title3 += "  per 100 kilocalories";
			break;
		case WhatYouEat.USING_GRAMS:
			this.title3 += "  per 100 grams";
			break;
		case WhatYouEat.USING_SERVING:

			if (useServingOK) {
				this.title3 += " in "
						+ WhatYouEat.quantityFactor[SubScroll.currentFoodID][WhatYouEat.optimalServingId[SubScroll.currentFoodID]]
						+ " "
						+ WhatYouEat.oddUnits[SubScroll.currentFoodID][WhatYouEat.optimalServingId[SubScroll.currentFoodID]]
						+ " of " + WhatYouEat.foods[SubScroll.currentFoodID];
			} else {
				this.title3 += " per 100 gram serving";
			}
			break;

		default:

			break;
		}

		// SET TITLE4 BASED ON WHATYOUEAT SETTINGS: USE ZERO USE NULL
		// doStatsOnList(foodsToUse, currentNutrient);

		histogram = boiledDown.histogram;
		sum = boiledDown.sum;
		sumOfSquares = boiledDown.sumOfSquares;
		mean = boiledDown.mean;
		median = boiledDown.median;
		min = boiledDown.min;
		max = boiledDown.max;
		n = boiledDown.n;
		range = boiledDown.range;
		standardDeviation = boiledDown.standardDeviation;
		barwidth = (frameW - 30 - BAR_BUFFER * StatTool.nbars) / StatTool.nbars;
		upperLim = (float) boiledDown.cutOffPoint;

		meanBar = (int) ((mean) / boiledDown.interval);
		pointBar = (int) ((nutrientPt) / boiledDown.interval);

		if (false) {
			int[] histoTemp = (int[]) histogram.clone();
			// get third hights bar
			Arrays.sort(histoTemp);
			if (histoTemp.length > 3)
				barMax = histoTemp[histoTemp.length - 4] + 1;
			else
				barMax = 1;
		}
		setUpBars(StatTool.nbars);
		for (int i = 0; i < _nPaints; i++)
			_paints[i] = new Paint();

		_paints[0].setARGB(255, 70, 120, 140);
		_paints[1].setARGB(180, 230, 40, 50);
		_paints[2].setColor(0xff000000 | barcolor);
		//_paints[2].setARGB(200, 90, 100, 200);
		_paints[3].setARGB(255, 200,200,200);
		_paints[3].setTextSize(22);
		_paints[4].setARGB(200, 250, 70, 170);
		_paints[5].setARGB(200, 250, 70, 170);

	}

	private void setUpBars(int nBars) {
		// UPDATE FOR CONCURRENT HISTOGRAMS
		histoBars = new RectF[nBars];
		float scaleFactor = 2;
		// scale = (frameH - 50) / barMax;
		for (int i = 0; i < nBars; i++) {
			int top = frameH - 50 - (int) (scaleFactor*histogram[i]);
			int left = 10 + BAR_BUFFER * i + ((int) barwidth) * i;
			int width = (int) (barwidth);
			int height = (int) (scaleFactor*histogram[i]);
			histoBars[i] = new RectF(left, top, left + width, top + height);

		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(frameW, frameH);
		setMeasuredDimension(frameW, frameH);
	}

	// //////////////////////////
	protected void onDraw(Canvas c) {
		c.drawColor(0x00000000);
		// c.save();
		// c.scale(1, 3);
		for (int i = 0; i < histoBars.length; i++) {
			if (i == pointBar)
				c.drawRect(histoBars[i], _paints[3]);
			else if (false && i == meanBar)
				c.drawRect(histoBars[i], _paints[4]);
			else

				c.drawRect(histoBars[i], _paints[2]);
		}
		// c.restore();
		c.drawText(title, 50, 100, _paints[3]);
		c.drawText(title2, 50, 150, _paints[3]);
		c.drawText(title3, 50, 200, _paints[3]);
		c.drawText("0.0", 15, frameH - 20, _paints[3]);
		c.drawText(
				" "
						+ Double.toString((double) ((int) (10000.0 * (upperLim))) / 10000.0),
				frameW - 120, frameH - 20, _paints[3]);
		invalidate();
	}

	/**
	 * @return the range
	 */
	public double getRange() {
		return range;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return "\nactual size: " + n + "\nmin: " + min + "\nmax: " + max
				+ "\nmean: " + mean + "\nmedian: " + median

				+ "\nstandard deviation: " + standardDeviation;
	}

	public double getEffectiveRange() {
		return 4 * standardDeviation;
	}

	/**
	 * @return the sum
	 */
	public double getSum() {
		return sum;
	}

	/**
	 * @return the sumOfSquares
	 */
	public double getSumOfSquares() {
		return sumOfSquares;
	}

	/**
	 * @return the mean
	 */
	public double getMean() {
		return mean;
	}

	/**
	 * @return the median
	 */
	public double getMedian() {
		return median;
	}

	/**
	 * @return the min
	 */
	public double getMin() {
		return min;
	}

	/**
	 * @return the max
	 */
	public double getMax() {
		return max;
	}

	/**
	 * @return the n
	 */
	public double getN() {
		return n;
	}

	/**
	 * @return the standardDeviation
	 */
	public double getStandardDeviation() {
		return standardDeviation;
	}

}