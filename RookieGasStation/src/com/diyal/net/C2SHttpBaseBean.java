package com.diyal.net;

import android.content.Context;

import com.diyal.app.GasStationApplication;

/**
 * aC2SHttpBaseBean
 * 
 * @category SID类协议C2S阶段Http对象继承基类
 * 
 * @author diyal.yin
 * 
 */
public class C2SHttpBaseBean extends C2SBaseBean {

	/** 指令码 */
	public String cmd;

	public String sid;

	public static void setBaseInfo(C2SHttpBaseBean bean, Context context) {
		if (bean != null) {

			if (GasStationApplication.getInstance() != null
					&& GasStationApplication.getInstance().getSessionData() != null) {
				bean.sid = GasStationApplication.getInstance().getSessionData().sid;

			}
		}
	}

	public static C2SHttpBaseBean create(Context context) {
		C2SHttpBaseBean bean = new C2SHttpBaseBean();
		setBaseInfo(bean, context);
		return bean;
	}
}
