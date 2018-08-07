package com.diyal.net;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Context;

import com.diyal.config.Constant;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

/**
 * NetWorkManger维护一个模拟客户端，控制整个模拟过程的进程，可以进行客户端的各种请求操作和返回数据的解析。</br>
 * 
 * @author diyal.yin
 * 
 */
public class NetWorkManger {
	private Context context;

	private static NetWorkManger netManager = null;

	private HttpUtils http = null;

	public static NetWorkManger getInstance() {
		if (netManager == null) {
			netManager = new NetWorkManger();
		}
		return netManager;
	}

	private static void saveCache(Context content, String key,
			Serializable entity) {
	}

	public <T> void request(String jsonStr, RequestCallBack<T> callBack) {

		// TODO 协议加密
		String reqJsonStr = jsonStr;

		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(reqJsonStr, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			LogUtils.e("UnsupportedEncodingException");
			e.printStackTrace();
		}

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, Constant.DOMAIN_REMOTE, params,
				callBack);
	}

	public void destory() {
	}
}
