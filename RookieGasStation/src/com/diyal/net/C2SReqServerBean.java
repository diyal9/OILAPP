package com.diyal.net;

import android.content.Context;

import com.diyal.config.Constant;

/**
 * 定义账号登录的数据结构
 * 
 * @author diyal.yin
 * 
 */
public class C2SReqServerBean extends C2SLoginBaseBean {
	/** 当前apk的版本信息 */
	public String version;

	public static C2SReqServerBean create(Context context, String cmd) {
		C2SLoginBaseBean baseBean = C2SLoginBaseBean.create(context);
		baseBean.cmd = cmd;

		C2SReqServerBean bean = fromJson(baseBean.toJson(),
				C2SReqServerBean.class);

		bean.version = Constant.APP_VER_NAME;

		return bean;
	}
}
