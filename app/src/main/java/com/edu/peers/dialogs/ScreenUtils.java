package com.edu.peers.dialogs;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by nelson on 28/02/2017.
 */

public class ScreenUtils {
  public static int dp2px(Context context, int dp){
    int px = (int) TypedValue
        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    return px;
  }
}
