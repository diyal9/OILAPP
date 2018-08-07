package com.diyal.net;

public class S2CReqServerBean extends S2CBaseBean {

	public String sid; // sessionId

	public static S2CReqServerBean create(String jsonStr) {
		return (S2CReqServerBean) fromJson(jsonStr, S2CReqServerBean.class);
	}
}
