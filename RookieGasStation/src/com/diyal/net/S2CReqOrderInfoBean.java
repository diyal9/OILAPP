package com.diyal.net;

/**
 * S2CReqOrderInfoBean
 * 
 * @category 订单信息
 * 
 * @author diyal.yin
 */
public class S2CReqOrderInfoBean extends S2CBaseBean {

	/** 订单ID */
	public String oid;
	/** 订单状态 */
	public String state;
	/** 订单创建时间 */
	public String time;
	/** 加油站序号 */
	public String sno;
	/** 加油站名称 */
	public String name;
	/** 加油站地址 */
	public String address;
	/** 油号 */
	public String oil;
	/** 数量 */
	public double amount;
	/** 单价 */
	public double price;
	/** 金额 */
	public double money;

	public static S2CReqOrderInfoBean create(String jsonStr) {
		return (S2CReqOrderInfoBean) fromJson(jsonStr,
				S2CReqOrderInfoBean.class);
	}
}
