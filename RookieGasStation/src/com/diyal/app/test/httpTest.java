/**   
 * @Title: httpTest.java 
 * @Package com.lunabox.app.test 
 * @System TODO  
 * @author 670924505@qq.com  
 * @date 2014-11-7 
 * @Copyright (c) Diyal All Rights Reserved.  
 */
package com.diyal.app.test;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.diyal.config.Constant;
import com.diyal.net.C2SHttpBaseBean;
import com.diyal.net.C2SReqServerBean;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.view.View;

/**
 * @ClassName: httpTest
 * @Description: TODO(用一句话描述这个类的作用)
 * @author diyal.yin
 * @date 2014-11-7 下午9:00:07
 * 
 */
public class httpTest {

	// @OnClick(R.id.download_btn)
	public void testPost(Context context) {

		// String jsonStr =
		// "{\"cmd\":\"probe\",\"app\":\"cnjyz\",\"proxy\":\"\",\"imei\":\"\",\"uid\":\"\",\"os\":\"\",\"version\":\"\"}";
		C2SReqServerBean reqServBean = C2SReqServerBean.create(context, "probe");

		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(reqServBean.toJson(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, Constant.DOMAIN_REMOTE, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						LogUtils.d("onStart");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						LogUtils.d("onLoading");
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String retStr = responseInfo.result;
						LogUtils.d("onSuccess:" + retStr);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.d("onFailure");
					}
				});
	}

}
