package com.diyal.net;

/**
 * 请求充值 charge 返回
 * 
 * @author diyal.yin
 */
public class S2CReqCZBean extends S2CBaseBean {

	/** 订单ID */
	public String oid;
	/** money */
	public double money;
	/** 通知服务器的地址 */
	public String feedback;
	/** subject */
	public String subject;

	/** paytype */
	public int paytype;

	/** body */
	public String body;

	/** pid */
	public String pid;

	/** sellerid */
	public String sellerid;

	public static S2CReqCZBean create(String jsonStr) {
		return (S2CReqCZBean) fromJson(jsonStr, S2CReqCZBean.class);
	}
}
