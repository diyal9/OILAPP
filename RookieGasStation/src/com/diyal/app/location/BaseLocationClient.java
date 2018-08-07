package com.diyal.app.location;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;

import com.diyal.app.location.CTLocation.CTLocationAccuracyLevel;
import com.diyal.app.location.CTLocation.CTLocationFailType;
import com.diyal.util.NetUtil;
import com.lidroid.xutils.util.LogUtils;

public abstract class BaseLocationClient {
	private static final int MSG_TIMEOUT = 1;

	// WIFI时要求定位精度500米
	protected static final int WIFI_ACCURACY = 500;

	// 非WIFI时定位精度 2000米
	protected static final int NO_WIFI_ACCURACY = 2000;

	protected Context mContext;
	// protected CTLocationListener mListener;
	protected LocationCallback mLocalLocationCallback;

	protected Handler mHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(android.os.Message msg) {
			LogUtils.i("BaseLocationClient handleMessage msg.what:" + msg.what);
			switch (msg.what) {
			case MSG_TIMEOUT:
				// timeout
				locateFailed(CTLocationFailType.CTLocationFailTypeTimeout);
				break;
			}
		};
	};

	public BaseLocationClient(Context context) {
		this.mContext = context;
	}

	protected void setStartTimeout(int timeout) {
		mHandler.sendEmptyMessageDelayed(MSG_TIMEOUT, timeout);
	}

	protected void cleanTimeout() {
		mHandler.removeMessages(MSG_TIMEOUT);
	}

	protected void locateFailed(CTLocationFailType type) {
		mLocalLocationCallback.onLocationFailed(this, type);
	}

	protected CTLocationAccuracyLevel convAccuracy(float acc) {
		if (acc <= 100.0 && acc > 0) {
			return CTLocationAccuracyLevel.CTLocationAccuracyLevelBest;
		} else {
			return CTLocationAccuracyLevel.CTLocationAccuracyLevelNormal;
		}
	}

	protected boolean isAvailableLocation(float accuracy) {
		float maxAcc = WIFI_ACCURACY;
		if (!"WIFI".equalsIgnoreCase(NetUtil.getNetType(mContext))) {
			// 非wifi情况下
			maxAcc = NO_WIFI_ACCURACY;
		}

		if (accuracy < maxAcc) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 异步逆地理解析
	 * 
	 * @param location
	 */
	protected void asyncReverseAddr(final Location location) {
		new Thread() {
			@Override
			public void run() {
				final CTGeoAddress geoAddr = CTLocationUtil
						.getAddrByCoordinate(mContext, location.getLatitude(),
								location.getLongitude());

				LogUtils.i("BaseLocationClient asyncReverseAddr geoAddr:"
						+ geoAddr);

				mHandler.post(new Runnable() {

					@Override
					public void run() {
						if (geoAddr != null) {
							if (mLocalLocationCallback != null) {
								mLocalLocationCallback.onLocationAddr(
										BaseLocationClient.this, geoAddr);
							}
						} else {
							locateFailed(CTLocationFailType.CTLocationFailTypeGeoAddressFailed);
						}
					}
				});
			}
		}.start();
	}

	/**
	 * 回调经纬度，并进行逆地理解析
	 * 
	 * @param location
	 */
	protected void processGpsLocation(Location location) {
		logLocationInfo(location);

		if (mLocalLocationCallback != null) {
			mLocalLocationCallback.onLocationCoordinate(
					this,
					new CTCoordinate2D(location.getLongitude(), location
							.getLatitude()));
		}

		// 定位成功回调信息，设置相关消息
		asyncReverseAddr(location);
	}

	protected void logLocationInfo(Location location) {
		LogUtils.i("BaseLocationClient showLocationInfo locationAcc:"
				+ location.getAccuracy() + " latitude:"
				+ location.getLatitude() + " longitude:"
				+ location.getLongitude());
	}

	/**
	 * 开始定位
	 * 
	 * @param accuracyLevel
	 * @param timeout
	 */
	public abstract void startLocate(CTLocationAccuracyLevel accuracyLevel,
			int timeout, LocationCallback callback);

	public abstract void releaseLocation();

	interface LocationCallback {
		public void onLocationCoordinate(BaseLocationClient client,
				CTCoordinate2D coordinate);

		public void onLocationAddr(BaseLocationClient client, CTGeoAddress addr);

		public void onLocationFailed(BaseLocationClient client,
				CTLocationFailType failedType);
	}

}
