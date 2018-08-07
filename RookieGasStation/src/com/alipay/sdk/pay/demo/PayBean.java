package com.alipay.sdk.pay.demo;

import android.content.Context;

import com.diyal.config.Constant;
import com.diyal.net.C2SLoginBaseBean;
import com.diyal.net.C2SReqServerBean;
import com.google.gson.Gson;

/**
 * C2SBaseBean.java
 * 
 * @category 支付宝支付信息Bean
 * 
 * @author diyal.yin
 */
public class PayBean extends C2SLoginBaseBean {
	/** 签约合作者身份ID */
	public String partner;

	/** 签约卖家支付宝账号 */
	public String seller_id;

	public static C2SReqServerBean create(Context context, String cmd) {
		C2SLoginBaseBean baseBean = C2SLoginBaseBean.create(context);
		baseBean.cmd = cmd;

		C2SReqServerBean bean = fromJson(baseBean.toJson(),
				C2SReqServerBean.class);

		bean.version = Constant.APP_VER_NAME;

		return bean;
	}
}
