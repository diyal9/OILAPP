package com.diyal.net;

import android.content.Context;

/**
 * 直接消费到加油站（小票打印机） 请求
 * 
 * @author diyal.yin
 * 
 */
public class C2SReqOrderBean extends C2SHttpBaseBean {

	public String pwd;

	public int sno;

	public String oil;

	public double money;

	public double coin;

	public int invoice;

	public String title;

	public String mobile;

	public String carid;

	public static C2SReqOrderBean create(Context context, String cmd,
			int sno, String oil, Double money, Double coin, int invoice,
			String title, String mobile, String carid) {
		C2SLoginBaseBean baseBean = C2SLoginBaseBean.create(context);
		baseBean.cmd = cmd;

		C2SReqOrderBean bean = fromJson(baseBean.toJson(),
				C2SReqOrderBean.class);

		bean.sno = sno;
		bean.oil = oil;
		bean.money = money;
		bean.coin = coin;
		bean.invoice = invoice;
		bean.title = title;
		bean.mobile = mobile;
		bean.carid = carid;
		
		bean.pwd = "";

		return bean;
	}
}
