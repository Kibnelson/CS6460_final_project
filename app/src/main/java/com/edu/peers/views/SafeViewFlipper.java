package com.edu.peers.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class SafeViewFlipper extends ViewFlipper {


  public SafeViewFlipper(Context context) {
    super(context);
  }

  public SafeViewFlipper(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void onDetachedFromWindow() {
    try {
      super.onDetachedFromWindow();
    } catch (IllegalArgumentException e) {
      // This happens when you're rotating and opening the keyboard that the same time
      // Possibly other rotation related scenarios as well
      stopFlipping();
    }
  }

}

