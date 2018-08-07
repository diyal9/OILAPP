package com.diyal.net;

import android.content.Context;

/**
 * 查询我的信息Bean 请求命令为myInfo
 * 
 * @author diyal.yin
 * 
 */
public class C2SReqQueryMyInfoBean extends C2SHttpBaseBean {

	public static C2SReqQueryMyInfoBean create(Context context, String cmd) {
		C2SHttpBaseBean baseBean = C2SHttpBaseBean.create(context);
		baseBean.cmd = cmd;

		C2SReqQueryMyInfoBean bean = fromJson(baseBean.toJson(),
				C2SReqQueryMyInfoBean.class);

		return bean;
	}
}
