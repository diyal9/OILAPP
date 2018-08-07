package com.diyal.net;

import android.content.Context;

/**
 * 消费记录Bean 请求命令为transaction
 * 
 * @author diyal.yin
 * 
 */
public class C2SReqMyRecordsBean extends C2SHttpBaseBean {
	/** 查询开始日 */
	public String startdate;

	/** 查询结果日 */
	public String enddate;

	public static C2SReqMyRecordsBean create(Context context, String cmd,
			String startdate, String enddate) {
		C2SHttpBaseBean baseBean = C2SHttpBaseBean.create(context);
		baseBean.cmd = cmd;

		C2SReqMyRecordsBean bean = fromJson(baseBean.toJson(),
				C2SReqMyRecordsBean.class);

		bean.startdate = startdate;
		bean.enddate = enddate;

		return bean;
	}
}
