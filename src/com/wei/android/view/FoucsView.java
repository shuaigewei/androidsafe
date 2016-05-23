package com.wei.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class FoucsView extends TextView {

	public FoucsView(Context context) {
		super(context);
	}

	public FoucsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FoucsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean isFocused() {
		return true;
	}

	



}
