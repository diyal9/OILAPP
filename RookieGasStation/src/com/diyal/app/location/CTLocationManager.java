package com.diyal.app.location;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.location.LocationManager;

import com.diyal.app.location.BaseLocationClient.LocationCallback;
import com.diyal.app.location.CTLocation.CTLocationAccuracyLevel;
import com.diyal.app.location.CTLocation.CTLocationFailType;
import com.lidroid.xutils.util.LogUtils;

public class CTLocationManager {
	/**
	 * 地址缓存信息的有效时间（单位：ms）
	 */
	private static final long CACHE_EFFECTIVE_TIME_MS = 5 * 60 * 1000;

	private static final int DEFAULT_TIMEOUT = 150000;

	private static CTLocationManager instance;

	private Context mContext;

	// 地址cache
	private CTGeoAddress mGeoAddrCache;
	private long cacheTime;

	private LinkedList<LocationClientTeam> locationClientList;

	private CTLocationManager(Context context) {
		mContext = context.getApplicationContext();

		locationClientList = new LinkedList<LocationClientTeam>();
	}

	public static CTLocationManager getInstance(Context context) {
		if (instance == null) {
			synchronized (LocationManager.class) {
				if (instance == null) {
					instance = new CTLocationManager(context);
				}
			}
		}

		return instance;
	}

	public void destory() {
		LogUtils.i("CTLocationManager destory clientCnt:"
				+ locationClientList.size());

		for (LocationClientTeam team : locationClientList) {
			team.releaseAll();
		}

		locationClientList.clear();
		locationClientList = null;

		instance = null;
	}

	/**
	 * 开始定位，默认精度CTLocationAccuracyLevelNormal, 默认超时15秒，默认使用缓存
	 * 
	 * @param listener
	 */
	public void startLocate(CTLocationListener listener) {
		startLocate(CTLocationAccuracyLevel.CTLocationAccuracyLevelNormal,
				DEFAULT_TIMEOUT, true, listener);
	}

	/**
	 * 开始定位，提供定位精度，超时时间，是否使用缓存，listener
	 * 
	 * @param accuracyLevel
	 * @param timeout
	 * @param canUseCache
	 * @param listener
	 */
	public void startLocate(CTLocationAccuracyLevel accuracyLevel, int timeout,
			boolean canUseCache, CTLocationListener listener) {
		LogUtils.i("CTLocationManager startLocate accuracyLevel:"
				+ accuracyLevel + " timeout:" + timeout + " canUseCache:"
				+ canUseCache);

		if (canUseCache) {
			if (!checkOverdue(cacheTime, CACHE_EFFECTIVE_TIME_MS)) {
				listener.onCoordinateSuccess(mGeoAddrCache.coordinate);
				listener.onCTGeoAddressSuccess(mGeoAddrCache);

				return;
			}
		}

		// 添加定位组并执行定位
		LocationClientTeam clientTeam = new LocationClientTeam(mContext,
				listener);
		locationClientList.add(clientTeam);
		clientTeam.startAll(accuracyLevel, timeout, locationCallback);
	}

	/**
	 * 检查是否过期
	 * 
	 * @param startTime
	 * @param timeout
	 * @return
	 */
	private boolean checkOverdue(long startTime, long timeout) {
		long curTime = System.currentTimeMillis();
		if (curTime - startTime > timeout) {
			LogUtils.i("CTLocationManager checkOverdue curTime:" + curTime
					+ " startTime:" + startTime + " timeout:" + timeout);
			return true;
		}
		return false;
	}

	private synchronized void setCache(CTGeoAddress geoAddr) {
		LogUtils.i("CTLocationManager setCache geoAddr:" + geoAddr);
		cacheTime = System.currentTimeMillis();
		mGeoAddrCache = geoAddr;
	}

	private LocationCallback locationCallback = new LocationCallback() {

		private LocationClientTeam findTeam(BaseLocationClient client) {
			LocationClientTeam curTeam = null;

			LogUtils.i("LocationCallback findTeam clientCnt:"
					+ locationClientList.size());
			for (LocationClientTeam team : locationClientList) {
				if (team.find(client)) {
					LogUtils.i("LocationCallback findTeam find team");
					curTeam = team;
					break;
				}
			}

			return curTeam;
		}

		@Override
		public void onLocationCoordinate(BaseLocationClient client,
				CTCoordinate2D coordinate) {
			LogUtils.i("LocationCallback onLocationCoordinate coordinate:"
					+ coordinate);

			if (coordinate != null) {
				LogUtils.i("LocationCallback onLocationCoordinate ok");

				LocationClientTeam curTeam = findTeam(client);
				if (curTeam != null) {
					if (curTeam.processReturnData(client, coordinate)) {
						// 定位处理完成
						locationClientList.remove(curTeam);
					}
				}
			}

		}

		@Override
		public void onLocationAddr(BaseLocationClient client, CTGeoAddress addr) {
			LogUtils.i("LocationCallback onLocationAddr addr:" + addr);

			if (addr != null) {
				LogUtils.i("LocationCallback onLocationAddr ok");
				setCache(addr);

				LocationClientTeam curTeam = findTeam(client);
				if (curTeam != null) {
					if (curTeam.processReturnData(client, addr)) {
						// 定位处理完成
						locationClientList.remove(curTeam);
					}
				}
			}
		}

		@Override
		public void onLocationFailed(BaseLocationClient client,
				CTLocationFailType failedType) {
			LogUtils.i("LocationCallback onLocationFailed failedType:"
					+ failedType);
			LocationClientTeam curTeam = findTeam(client);
			if (curTeam != null) {
				if (curTeam.processReturnData(client, failedType)) {
					// 定位处理完成
					locationClientList.remove(curTeam);
				}
			}
		}

	};

	class ClientModel {
		static final byte NONE = 0;
		static final byte COORDINATE_FINISH = 1;
		static final byte GEO_ADDR_FINISH = 2;
		static final byte FAILED = 0xf;

		BaseLocationClient client;
		// 0:未返回，1:经纬度完成，2:地址解析完成，f:失败
		byte state;

		public ClientModel(BaseLocationClient c) {
			client = c;
			state = NONE;
		}
	}

	/**
	 * 定义一个定位工作组
	 * 
	 * @author lawang
	 * 
	 */
	class LocationClientTeam {
		private ArrayList<ClientModel> clientModels = null;

		private CTLocationListener mListener;

		public LocationClientTeam(Context context, CTLocationListener listener) {
			mListener = listener;
			clientModels = new ArrayList<ClientModel>();

			clientModels.add(new ClientModel(new CTAmapNetLocationClient(
					mContext)));
		}

		/**
		 * 查找组中是否有指定的client
		 * 
		 * @param client
		 * @return
		 */
		boolean find(BaseLocationClient client) {
			if (findModel(client) != null) {
				return true;
			}

			return false;
		}

		ClientModel findModel(BaseLocationClient client) {
			for (ClientModel model : clientModels) {
				if (client == model.client) {
					return model;
				}
			}
			return null;
		}

		/**
		 * team内定位经纬度数据处理
		 * 
		 * @return 如果判断team内完成定位则返回true，否则为false
		 */
		boolean processReturnData(BaseLocationClient client,
				CTCoordinate2D coordinate) {
			LogUtils.i("LocationClientTeam processReturnData coordinate:"
					+ coordinate);
			for (ClientModel model : clientModels) {
				if (client == model.client) {
					model.state = ClientModel.COORDINATE_FINISH;

					// listener返回数据
					mListener.onCoordinateSuccess(coordinate);
				} else {
					releaseModel(model);
				}
			}

			return false;
		}

		/**
		 * team内定位详细地址数据处理
		 * 
		 * @return 如果判断team内完成定位则返回true，否则为false
		 */
		boolean processReturnData(BaseLocationClient client, CTGeoAddress addr) {
			LogUtils.i("LocationClientTeam processReturnData addr:" + addr);
			for (ClientModel model : clientModels) {
				if (client == model.client) {
					model.state = ClientModel.GEO_ADDR_FINISH;

					// listener返回数据
					mListener.onCTGeoAddressSuccess(addr);
				} else {
					releaseModel(model);
				}
			}

			// 定位完成返回true
			return true;
		}

		/**
		 * team内定位失败数据处理
		 * 
		 * @return 如果判断team内完成定位则返回true，否则为false
		 */
		boolean processReturnData(BaseLocationClient client,
				CTLocationFailType type) {
			LogUtils.i("LocationClientTeam processReturnData type:" + type);
			for (ClientModel model : clientModels) {
				if (client == model.client) {
					model.state = ClientModel.FAILED;

					// listener返回数据
					mListener.onLocationFailed(type);
				}
			}

			// 此时只有一个client时，表示全部失败了
			if (clientModels.size() == 1) {
				return true;
			}

			return false;
		}

		private void releaseModel(ClientModel c) {
			c.client.releaseLocation();
			c.state = ClientModel.NONE;
			clientModels.remove(c);
		}

		void startAll(CTLocationAccuracyLevel accuracyLevel, int timeout,
				LocationCallback callback) {
			LogUtils.i("LocationClientTeam startAll accuracyLevel:"
					+ accuracyLevel + " timeout:" + timeout);
			for (ClientModel c : clientModels) {
				c.client.startLocate(accuracyLevel, timeout, callback);
			}
		}

		void releaseAll() {
			LogUtils.i("LocationClientTeam releaseAll size:"
					+ clientModels.size());
			for (ClientModel c : clientModels) {
				releaseModel(c);
			}
		}
	}
}
