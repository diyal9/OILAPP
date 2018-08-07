package com.diyal.net;

public class S2CReqQuerymyInfoBean extends S2CBaseBean {

	public String uid;
	public int money;
	public int coin;
	public String fullname;
	public int gender;
	public String mobile;
	public String email;

	public static S2CReqQuerymyInfoBean create(String jsonStr) {
		return (S2CReqQuerymyInfoBean) fromJson(jsonStr, S2CReqQuerymyInfoBean.class);
	}
}
