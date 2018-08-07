package com.diyal.net;

import com.google.gson.Gson;

/**
 * C2SBaseBean
 * 
 * @category 协议通信基类
 * 
 * @author diyal.yin
 */
public abstract class C2SBaseBean {
	public String toJson() {
		return toJson(this);
	}

	public static String toJson(C2SBaseBean bean) {
		if (bean != null) {
			Gson gson = new Gson();
			return gson.toJson(bean);
		}

		return null;
	}

	public static <T extends C2SBaseBean> T fromJson(String jsonStr,
			Class<? extends C2SBaseBean> subClass) {
		Gson gson = new Gson();
		C2SBaseBean newObj = gson.fromJson(jsonStr, subClass);
		return (T) newObj;
	}

}
