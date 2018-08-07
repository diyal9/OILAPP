package com.diyal.net;

import android.content.Context;

import com.diyal.config.Constant;

/**
 * 价格请求Bean 请求命令为inquiry
 * 
 * @author diyal.yin
 * 
 */
public class C2SReqInquiryBean extends C2SHttpBaseBean {
	/** 经度 */
	public double longitude;

	/** 纬度 */
	public double latitude;

	public static C2SReqInquiryBean create(Context context, String cmd,
			double longitude, double latitude) {
		C2SHttpBaseBean baseBean = C2SHttpBaseBean.create(context);
		baseBean.cmd = cmd;

		C2SReqInquiryBean bean = fromJson(baseBean.toJson(),
				C2SReqInquiryBean.class);

		bean.longitude = longitude;
		bean.latitude = latitude;

		return bean;
	}
}
