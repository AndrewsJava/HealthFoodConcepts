package co.harlequinmettle.healthfoodconcepts;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;

public class StatTool {

	double sum, sumOfSquares, mean, median, min, max, n, range;

	int[] histogram;

	double cutOffPoint, interval;

	public double standardDeviation;

	// float nutrientPt;
	static int nbars = 100;

	public StatTool(int[] foodsToUse, int nutrientID) {
		long time = System.currentTimeMillis();
		doStatsOnList(foodsToUse, nutrientID);
	}

	public void doStatsOnList(int[] foodIds, int nutrientID) {
		ArrayList<Float> medianFinder = new ArrayList<Float>();
		outer: for (int i : foodIds) {
			float dataPoint = WhatYouEat.db[nutrientID][i];

			if (dataPoint < 0)
				continue;
			if (WhatYouEat.USING_NOT_ZEROS == WhatYouEat.DATA_CHOICES && dataPoint==0)
				continue outer;

			switch (WhatYouEat.Nutrient_Measure) {
			case WhatYouEat.USING_GRAMS:
				// DONT REALLY NEED THIS - ALREADY USING PER 100G BY DEFAULT
				break;
			case WhatYouEat.USING_KCAL:
				dataPoint = WhatYouEat.getPer100KcalDataPoint(i, dataPoint);

				break;

			case WhatYouEat.USING_SERVING:

				dataPoint = WhatYouEat.getPerServingDataPoint(i, dataPoint);
				if(dataPoint<0)
					dataPoint = WhatYouEat.db[nutrientID][i];
				
			default:
				// NEED ALGORITHM FOR FINDING REASONABLE SERVING SIZE
				break;
			}
			n++;
			sum += dataPoint;
			sumOfSquares += dataPoint * dataPoint;
			medianFinder.add(dataPoint);
		}
		mean = sum / n;
		if (medianFinder.size() < 1)
			return;
		Collections.sort(medianFinder);
		median = ((medianFinder.get((int) (n / 2)) + medianFinder
				.get((int) ((n - 1) / 2))) / 2);
		min = medianFinder.get(0);
		max = medianFinder.get(medianFinder.size() - 1);
		// mode = findMode(medianFinder);

		standardDeviation = (float) Math.sqrt((sumOfSquares - sum * sum / n)
				/ n);
		range = max - min;

		int topPt = (int) (medianFinder.size() * 0.9);

		cutOffPoint = medianFinder.get(topPt);//
	//	if (cutOffPoint == 0)
    //	cutOffPoint = 3 * standardDeviation;
		histogram = calculateHistogram(medianFinder);

		 
	}

	public int[] calculateHistogram(ArrayList<Float> _data) {

		interval = cutOffPoint / nbars;
		int[] histogram = new int[nbars];
		for (float dataPt : _data) {
			if (dataPt < 0)
				continue;
			int histo_pt = (int) ((dataPt) / (interval));
			if (histo_pt >= 0 && histo_pt < nbars)
				histogram[histo_pt]++;
		}
		return histogram;
	}

	public static float[] simpleStats(int[] foodIds, int nutrientID) {
		// ArrayList<Float> getEffectiveHigh = new ArrayList<Float>();
		float sum = 0, mean = 0, min = 1E20f, max = 0, n = 0;
		for (int i : foodIds) {
			float dataPoint = WhatYouEat.db[nutrientID][i];

			if (dataPoint < 0)
				continue;

			if (dataPoint < 0) {
				dataPoint = 0;
			}
			n++;

			switch (WhatYouEat.Nutrient_Measure) {
			case WhatYouEat.USING_GRAMS:
				// DONT REALLY NEED THIS - ALREADY USING PER 100G BY DEFAULT
				break;
			case WhatYouEat.USING_KCAL:
				dataPoint = WhatYouEat.getPer100KcalDataPoint(i, dataPoint);

				break;
			case WhatYouEat.USING_SERVING:
				dataPoint = WhatYouEat.getPerServingDataPoint(i, dataPoint);
				if(dataPoint<0)
					dataPoint = WhatYouEat.db[nutrientID][i];;
				break;
			default:
				// NEED ALGORITHM FOR FINDING REASONABLE SERVING SIZE
				break;
			}
			// getEffectiveHigh.add(dataPoint);
			sum += dataPoint;

			if (dataPoint < min)
				min = dataPoint;
			if (dataPoint > max)
				max = dataPoint;
		}
		mean = sum / n;
		// Collections.sort(getEffectiveHigh);
		float[] retrn = { min, mean, max / 3 };
		return retrn;
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

		return "\nsize: " + n + "\nmin: " + min + "\nmax: " + max + "\nmean: "
				+ mean + "\nmedian: " + median

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