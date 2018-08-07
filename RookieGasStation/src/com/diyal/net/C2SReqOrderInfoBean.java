package com.diyal.net;

import android.content.Context;

/**
 * http订单请求信息
 * 
 * @author diyal.yin
 * 
 */
public class C2SReqOrderInfoBean extends C2SHttpBaseBean {
	/** 订单号 */
	public String oid;

	public static C2SReqOrderInfoBean create(Context context, String cmd,
			String oid) {
		C2SLoginBaseBean baseBean = C2SLoginBaseBean.create(context);
		baseBean.cmd = cmd;

		C2SReqOrderInfoBean bean = fromJson(baseBean.toJson(),
				C2SReqOrderInfoBean.class);

		bean.oid = oid;

		return bean;
	}
}
