/**   
 * @Title: BaseActivity.java 
 * @Package com.diyal.xinvoice.ui.base 
 * @System TODO  
 * @author 670924505@qq.com  
 * @date 2014-8-13 
 * @Copyright (c) Diyal All Rights Reserved.  
 */
package com.diyal.ui.base;

import android.app.Activity;
import android.os.Bundle;

import com.diyal.app.GasStationApplication;

/**
 * @ClassName: BaseActivity
 * @Description: UI基类
 * @author diyal.yin
 * @date 2014-8-13 下午2:39:30
 * 
 */
public class BaseActivity extends Activity {

	private static final int RESULT_LOGOUT = RESULT_FIRST_USER + 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_onCreate(savedInstanceState);
	}

	/*
	 * 因为onCreate方法无法返回状态，因此无法进行状态判断， 为了能对上层返回的信息进行判断处理，我们使用_onCreate代替真正的
	 * onCreate进行工作。onCreate仅在顶层调用_onCreate。
	 */
	protected boolean _onCreate(Bundle savedInstanceState) {
		if (!checkIsLogedIn()) {
			return false;
		} else {
			return true;
		}
	}

	// 检查是否登录
	protected boolean checkIsLogedIn() {
		String localuseid = GasStationApplication.getLocalUserId();
		String localPwd = GasStationApplication.getLocalUserId();
		if ((localuseid == null || "".equals(localuseid))
				|| (localPwd == null || "".equals(localPwd))) {
			showLogin();
			return false;
		}
		return true;
	}

	// 显示登陆框
	protected void showLogin() {
		// Intent intent = new Intent(this, LoginActivity.class);
		// // TODO: might be a hack?
		// intent.putExtra(Intent.EXTRA_INTENT, getIntent());
		//
		// startActivity(intent);
	}
}
