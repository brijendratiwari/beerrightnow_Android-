package com.beerrightnow.utility;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

public class DimensionUtils {

	private static final String TAG = DimensionUtils.class.getSimpleName();
	private static DimensionUtils self = new DimensionUtils();
	private DisplayMetrics metrics = null;

	public static DimensionUtils getInstance() {

		return self;
	}

	public void init(Context context) {

		metrics = context.getResources().getDisplayMetrics();
	}

	public int DpiToPixel(float dpi) {

		try {

			float pixel = TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, dpi, metrics);

			return (int) pixel;
		} catch (NullPointerException e) {

			Log.d(TAG, "init method must be called before");
			return 0;
		}

	}

	public DisplayMetrics getMetrics() {

		return metrics;
	}
}
