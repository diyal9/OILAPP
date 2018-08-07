package com.diyal.net;

import android.content.Context;

import com.diyal.app.GasStationApplication;
import com.diyal.data.LocalPublicData;
import com.diyal.util.DeviceUtil;

/**
 * aC2SHttpBaseBean
 * 
 * @category Http C2SBean
 * 
 * @author diyal.yin
 * 
 */
public class C2SLoginBaseBean extends C2SBaseBean {
	public String uuid;
	public String imei;
	public String mac;
	public String androidid;

	public String sid;

	/** 指令码 */
	public String cmd;

	/** app-id */
	public String app;

	public static void setBaseInfo(C2SLoginBaseBean bean, Context context) {
		if (bean != null) {
			bean.uuid = LocalPublicData.getUUID(context);
			bean.mac = DeviceUtil.getMac(context);
			bean.imei = DeviceUtil.getIMEI(context);
			bean.androidid = DeviceUtil.getAndroidId(context);

			if (GasStationApplication.getInstance() != null
					&& GasStationApplication.getInstance().getSessionData() != null) {
				bean.sid = GasStationApplication.getInstance().getSessionData().sid;

			}

			bean.app = "cnjyz";
		}
	}

	public static C2SLoginBaseBean create(Context context) {
		C2SLoginBaseBean bean = new C2SLoginBaseBean();
		setBaseInfo(bean, context);
		return bean;
	}
}
