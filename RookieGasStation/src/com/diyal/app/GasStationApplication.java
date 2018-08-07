/**   
 * @Title: gasApplication.java 
 * @Package com.diyal.app 
 * @System 菜鸟加油站  
 * @author 670924505@qq.com  
 * @date 2015-4-8 
 * @Copyright (c) Diyal All Rights Reserved.  
 */
package com.diyal.app;

import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.diyal.activity.MainActivity;
import com.diyal.app.location.CTGeoAddress;
import com.diyal.config.Preferences;
import com.diyal.data.JsonBean.G;
import com.diyal.data.User;
import com.diyal.net.S2CReqUUIDLoginBean;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName: gasApplication
 * @Description: Application
 * @author diyal.yin
 * @date 2015-4-8
 * 
 */
public class GasStationApplication extends Application {

	public static Context mCountext;

	public static GasStationApplication self;

	/** 储存对象 */
	public static SharedPreferences mPref;

	public static User userCache;

	/** 登录应用级别信息缓存 */
	public static S2CReqUUIDLoginBean sessionData;
	/** 经纬度 */
	public CTGeoAddress addrLocation;

	// 临时放在这里的选中油站的SNO
	public G tmpGasInfo;

	private List<Activity> activityList; // 缓存Activity

	/*
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		LogUtils.d(this.getClass().getName());
		super.onCreate();

		mCountext = this.getApplicationContext();
		self = this;

		mPref = PreferenceManager.getDefaultSharedPreferences(this);
		String username = mPref.getString(Preferences.USERNAME_KEY, "");
		String password = mPref.getString(Preferences.PASSWORD_KEY, "");
		userCache = new User();
		userCache.setUserid(username);
		userCache.setPassword(password);

		// TODO 登陆消息 暂时放在这（后面优化登录跟注册）
		// uuidLogin();
		// getLocationData();

	}

	public void setActivity(Activity activty) {
		activityList.add(activty);
	}

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static GasStationApplication getInstance() {
		return self;
	}

	public S2CReqUUIDLoginBean getSessionData() {
		return sessionData;
	}

	public void setSessionData(S2CReqUUIDLoginBean sessionData) {
		GasStationApplication.sessionData = sessionData;
	}

	// 获得本地UserId
	public static String getLocalUserId() {
		return mPref.getString(Preferences.USERNAME_KEY, "");
	}

	// 获得密码
	public static String getLocalPwd() {
		return mPref.getString(Preferences.PASSWORD_KEY, "");
	}

}
