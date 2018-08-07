package com.diyal.net;

/**
 * S2CReqOrderBean
 * 
 * @category 下单信息 charge
 * 
 * @author diyal.yin
 */
public class S2CReqOrderBean extends S2CBaseBean {

	/** 订单ID */
	public String oid;
	/** money */
	public double money;
	/** 加油站别名 */
	public int state;
	/** feedback */
	public double coin;

	public static S2CReqOrderBean create(String jsonStr) {
		return (S2CReqOrderBean) fromJson(jsonStr, S2CReqOrderBean.class);
	}
}
