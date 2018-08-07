package com.diyal.net;

/**
 * S2CReqUUIDLoginBean
 * 
 * @category UUID登录回复
 * 
 * @author diyal.yin
 */
public class S2CReqOilInfoBean extends S2CBaseBean {

	/** 订单ID */
	public String oid;
	/** 加油站名称 */
	public String name;
	/** 加油站别名 */
	public String alias;
	/** 状态 */
	public String state;
	/** 省份 */
	public String province;
	/** 城市 */
	public String city;
	/** 区县 */
	public String county;
	/** 加油站地址 */
	public String address;
	/** 联系电话 */
	public String telphone;
	/** 联系电话2 */
	public String telephone2;
	/** fax */
	public String fax;
	/** 标语、口令、宣传语 */
	public String slogan;

	public static S2CReqOilInfoBean create(String jsonStr) {
		return (S2CReqOilInfoBean) fromJson(jsonStr, S2CReqOilInfoBean.class);
	}
}
