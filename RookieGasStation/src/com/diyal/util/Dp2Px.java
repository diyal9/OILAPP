package com.diyal.util;

import android.content.Context;

public class Dp2Px {
	public static int conv(Context paramContext, double d) {
		return (int) (0.5F + d
				* paramContext.getResources().getDisplayMetrics().density);
	}
}
