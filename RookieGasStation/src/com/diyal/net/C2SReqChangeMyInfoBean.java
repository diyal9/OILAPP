package com.diyal.net;

import android.content.Context;

/**
 * 修改我的信息Bean 请求命令为changeInfo
 * 
 * @author diyal.yin
 * 
 */
public class C2SReqChangeMyInfoBean extends C2SHttpBaseBean {
	/** 姓名 */
	public String fullname;
	/** 性别 */
	public String gender;
	/** 手机号 */
	public String mobile;
	/** 电子邮件 */
	public String email;

	public static C2SReqChangeMyInfoBean create(Context context, String cmd,
			String fullname, String gender, String mobile, String email) {
		C2SHttpBaseBean baseBean = C2SHttpBaseBean.create(context);
		baseBean.cmd = cmd;

		C2SReqChangeMyInfoBean bean = fromJson(baseBean.toJson(),
				C2SReqChangeMyInfoBean.class);
		bean.fullname = fullname;
		bean.gender = gender;
		bean.mobile = mobile;
		bean.email = email;

		return bean;
	}
}
