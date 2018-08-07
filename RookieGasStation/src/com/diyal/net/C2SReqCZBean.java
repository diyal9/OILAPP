package com.diyal.net;

import android.content.Context;

/**
 * 请求充值 charge
 * 
 * @author diyal.yin
 * 
 */
public class C2SReqCZBean extends C2SHttpBaseBean {
	/** 订单号 */
	public int paytype;

	/** 金额 */
	public Double money;

	public static C2SReqCZBean create(Context context, String cmd, int paytype,
			Double money) {
		C2SLoginBaseBean baseBean = C2SLoginBaseBean.create(context);
		baseBean.cmd = cmd;

		C2SReqCZBean bean = fromJson(baseBean.toJson(), C2SReqCZBean.class);

		bean.paytype = paytype;
		bean.money = money;

		return bean;
	}
}
