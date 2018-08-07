package com.diyal.util;

import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

public class NetUtil {
	/**
	 * 获取网络类型
	 */
	public static String getNetType(Context context) {
		String netTypeStr = "";
		ConnectivityManager conManager = null;
		try {
			if (conManager == null) {
				conManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
			}
			if (conManager == null) {
				return "";
			}
			NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
			if (networkInfo == null || !networkInfo.isConnected()
					|| !networkInfo.isAvailable()) {
				netTypeStr = "";
			} else {
				int type = networkInfo.getType();
				netTypeStr = updateNetProvider(context, type);
			}
		} catch (Exception e) {
			LogUtils.d("获取网络类型失败：" + e);
		}
		return netTypeStr;
	}

	private static String updateNetProvider(Context context, int type) {
		String netTypeStr = "";
		int tempType = getSwitchedType(type);
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String IMSI = null;
		int networkType = 0;
		if (telephonyManager != null) {
			IMSI = telephonyManager.getSubscriberId();
			networkType = telephonyManager.getNetworkType();
		}
		String provide = "";
		if (IMSI != null) {
			if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
					|| IMSI.startsWith("46007")) {
				provide = "移动";
			} else if (IMSI.startsWith("46001")) {
				provide = "联通";
			} else if (IMSI.startsWith("46003")) {
				provide = "电信";
			} else {
				provide = "未知";
			}
		}
		if (tempType == ConnectivityManager.TYPE_MOBILE) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
				switch (networkType) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_IDEN:
					netTypeStr = provide + "2G";
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
				case TelephonyManager.NETWORK_TYPE_EVDO_B:
					netTypeStr = provide + "3G";
					break;
				default:
					netTypeStr = provide + "UnKnow";
					break;
				}
			} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					if (networkType == TelephonyManager.NETWORK_TYPE_HSPAP) {
						netTypeStr = provide + "3G";
					}
				} else {
					if (networkType == TelephonyManager.NETWORK_TYPE_EHRPD) {
						netTypeStr = provide + "3G";
					} else if (networkType == TelephonyManager.NETWORK_TYPE_LTE) {
						netTypeStr = provide + "4G";
					}
				}
			} else {
				netTypeStr = provide + "UnKnow";
			}
		} else if (tempType == ConnectivityManager.TYPE_WIFI) {
			netTypeStr = "WIFI";
		}
		return netTypeStr;
	}

	private static int getSwitchedType(int type) {
		switch (type) {
		case ConnectivityManager.TYPE_MOBILE:
		case ConnectivityManager.TYPE_MOBILE_DUN:
		case ConnectivityManager.TYPE_MOBILE_HIPRI:
		case ConnectivityManager.TYPE_MOBILE_MMS:
		case ConnectivityManager.TYPE_MOBILE_SUPL:
			return ConnectivityManager.TYPE_MOBILE;
		default:
			return type;
		}
	}
}
