package com.diyal.net;

/**
 * S2CReqUUIDLoginBean
 * 
 * @category UUID登录回复
 * 
 * @author diyal.yin
 */
public class S2CReqUUIDLoginBean extends S2CBaseBean {

	public String sid; // sessionId

	public static S2CReqUUIDLoginBean create(String jsonStr) {
		// return (S2CResultBean) create(jsonStr, S2CResultBean.class);
		return (S2CReqUUIDLoginBean) fromJson(jsonStr,
				S2CReqUUIDLoginBean.class);
	}
}
