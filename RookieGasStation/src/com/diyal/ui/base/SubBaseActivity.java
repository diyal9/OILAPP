package com.diyal.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.diyal.rookiegasstation.R;

public abstract class SubBaseActivity extends Activity {
	public ImageView preservation, mBackImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(setLayout());
		initTitleBar();
		initView();
		initData();
	}

	public abstract int setLayout();

	public abstract void initView();

	public abstract void initData();

	private void initTitleBar() {
		TextView title = (TextView) findViewById(R.id.general_title);
		if (!TextUtils.isEmpty(getResources().getString(setTitleBarTitle()))) {
			title.setText(getResources().getString(setTitleBarTitle()));
		}

		// preservation = (ImageView)
		// findViewById(R.id.general_right_interspace);
		// if (isHideOffsideIcon()) {
		// preservation.setVisibility(View.INVISIBLE);
		// }
		// if (isSetBackMonitor()) {
		// mBackImg = (ImageView) findViewById(R.id.general_left_interspace);
		// mBackImg.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });
		// }

	}

	public abstract int setTitleBarTitle();

	/***
	 * 、
	 * 
	 * @return true:隐藏右侧图标 false:不隐藏
	 */
	public abstract boolean isHideOffsideIcon();

	public ImageView getHideOffsideIconObject() {
		return preservation;
	}

	/***
	 * 
	 * @return true:监听返回false:不监听
	 */
	public abstract boolean isSetBackMonitor();
}
