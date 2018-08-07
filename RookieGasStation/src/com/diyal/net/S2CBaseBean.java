package com.diyal.net;

import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;

/**
 * S2CBaseBean
 * 
 * @category 协议通信基类
 * 
 * @author diyal.yin
 */
public abstract class S2CBaseBean {

	public int err; // 协议通信状态

	public String toJson() {
		return toJson(this);
	}

	public static String toJson(S2CBaseBean bean) {
		if (bean != null) {
			Gson gson = new Gson();
			return gson.toJson(bean);
		}

		return null;
	}

	public static <T extends S2CBaseBean> T fromJson(String jsonStr,
			Class<? extends S2CBaseBean> subClass) {
		try {
			Gson gson = new Gson();
			S2CBaseBean newObj = gson.fromJson(jsonStr, subClass);
			return (T) newObj;
		} catch (Exception e) {
			LogUtils.i("不是完整的Json：" + subClass.getClass().getName());
		}

		return null;
	}

}
