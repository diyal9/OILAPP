package com.diyal.net;

import android.content.Context;

/**
 * http请求一家加油站的信息
 * 
 * @author diyal.yin
 * 
 */
public class C2SReqOilDetailBean extends C2SHttpBaseBean {
	/** 订单号 */
	public int sno;

	public static C2SReqOilDetailBean create(Context context, String cmd,
			int sno) {
		C2SHttpBaseBean baseBean = C2SHttpBaseBean.create(context);
		baseBean.cmd = cmd;

		C2SReqOilDetailBean bean = fromJson(baseBean.toJson(),
				C2SReqOilDetailBean.class);

		bean.sno = sno;

		return bean;
	}
}
