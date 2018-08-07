package com.diyal.app.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.diyal.app.location.CTLocation.CTLocationAccuracyLevel;
import com.diyal.app.location.CTLocation.CTLocationFailType;
import com.lidroid.xutils.util.LogUtils;

/**
 * 一个高德SDK定位请求对象
 * 
 * @author lawang
 * 
 */
public class CTAmapNetLocationClient extends BaseLocationClient {

	// 高德定位代理
	private LocationManagerProxy mLocationManagerProxy;
	private AMapLocationListener amapListener;

	public CTAmapNetLocationClient(Context context) {
		super(context);
	}

	/**
	 * 开始定位
	 * 
	 * @param accuracyLevel
	 * @param timeout
	 */
	@Override
	public void startLocate(CTLocationAccuracyLevel accuracyLevel, int timeout,
			LocationCallback callback) {
		LogUtils.i("CTAmapNetLocationClient startLocate request");

		setStartTimeout(timeout);
		mLocalLocationCallback = callback;

		// 初始化定位，只采用网络定位
		amapListener = new AmapLocationListenerImpl();
		mLocationManagerProxy = LocationManagerProxy.getInstance(mContext);
		mLocationManagerProxy.setGpsEnable(false);

		int accuracy = 0;
		switch (accuracyLevel) {
		case CTLocationAccuracyLevelNormal:
			accuracy = 200;
			break;
		case CTLocationAccuracyLevelBest:
			accuracy = 50;
			break;
		}

		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, accuracy, amapListener);
	}

	@Override
	public void releaseLocation() {
		LogUtils.i("CTAmapNetLocationClient releaseLocation");

		cleanTimeout();

		// 移除定位请求
		mLocationManagerProxy.removeUpdates(amapListener);
		// 销毁定位
		mLocationManagerProxy.destroy();
	}

	class AmapLocationListenerImpl implements AMapLocationListener {
		@Override
		public void onLocationChanged(Location location) {
			LogUtils.i("onLocationChanged location:" + location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			LogUtils.i("onProviderDisabled provider:" + provider);
		}

		@Override
		public void onProviderEnabled(String provider) {
			LogUtils.i("onProviderEnabled provider:" + provider);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			LogUtils.i("onStatusChanged provider:" + provider + " status:"
					+ status);
		}

		@Override
		public void onLocationChanged(AMapLocation amapLocation) {
			LogUtils.i("CTAmapNetLocationClient onLocationChanged time:"
					+ System.currentTimeMillis() + " amapLocation:"
					+ amapLocation);

			if (amapLocation != null
					&& amapLocation.getAMapException().getErrorCode() == 0) {
				// 定位完成
				CTGeoAddress addr = setLocation(amapLocation);
				LogUtils.i("CTAmapNetLocationClient onLocationChanged geoAddr:"
						+ addr);
				if (mLocalLocationCallback != null) {
					mLocalLocationCallback.onLocationAddr(
							CTAmapNetLocationClient.this, addr);
				}

				// // listener
				// mListener.onCoordinateSuccess(addr.coordinate);
				// mListener.onCTGeoAddressSuccess(addr);
			} else {
				// 失败
				locateFailed(CTLocationFailType.CTLocationFailTypeGeoAddressFailed);
			}
		}

		private CTGeoAddress setLocation(AMapLocation amapLocation) {
			CTGeoAddress addr = new CTGeoAddress();

			// addr.coordinate.latitude = amapLocation.getLatitude(); // 纬度
			// addr.coordinate.longitude = amapLocation.getLongitude(); // 经度

			addr.latitude = amapLocation.getLatitude(); // 纬度
			addr.longitude = amapLocation.getLongitude(); // 经度

			// 省市区街道
			addr.country = ""; // 国家
			addr.province = amapLocation.getProvider(); // 省份
			addr.city = amapLocation.getCity(); // 城市
			addr.district = amapLocation.getDistrict(); // 区名
			// TODO 不确定
			addr.formattedAddress = amapLocation.getAddress();
			addr.countryShortName = amapLocation.getAdCode();
			return addr;
		}

	}

}
