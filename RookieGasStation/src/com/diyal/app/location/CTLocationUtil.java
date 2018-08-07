package com.diyal.app.location;

import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.os.Looper;
import android.text.TextUtils;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.lidroid.xutils.util.LogUtils;

public class CTLocationUtil {
	private static final String SDK_VERSION = "1.0.0";

	private static class MapPoint {

		public double x;
		public double y;

		public MapPoint(double x1, double y1) {
			x = x1;
			y = y1;
		}
	}

	// 中国内地坐标.
	private static final MapPoint locChinaArrs[][] = {
			{ new MapPoint(85.374342, 41.697126),
					new MapPoint(124.486996, 28.705892) },
			{ new MapPoint(98.942349, 28.714002),
					new MapPoint(122.527683, 23.331042) },
			{ new MapPoint(108.012216, 23.415965),
					new MapPoint(119.252965, 17.294543) },
			{ new MapPoint(120.025651, 51.286036),
					new MapPoint(126.391116, 41.330674) },
			{ new MapPoint(82.936701, 46.727439),
					new MapPoint(90.553182, 41.621242) },
			{ new MapPoint(126.188746, 48.211817),
					new MapPoint(129.757821, 42.485061) },
			{ new MapPoint(129.518656, 47.611932),
					new MapPoint(131.46877, 44.959641) },
			{ new MapPoint(131.376783, 47.487374),
					new MapPoint(133.805226, 46.225387) },
			{ new MapPoint(79.753968, 41.87613),
					new MapPoint(85.604309, 30.872189) },
			{ new MapPoint(113.457816, 44.802677),
					new MapPoint(120.117638, 41.517618) },
			{ new MapPoint(118.977005, 23.526282),
					new MapPoint(121.975765, 21.629857) },
			{ new MapPoint(109.667973, 17.321053),
					new MapPoint(119.050594, 14.580095) },
			{ new MapPoint(76.258482, 40.359687),
					new MapPoint(80.01153, 35.915704) },
			{ new MapPoint(90.534784, 44.710915),
					new MapPoint(94.030271, 41.531444) },
			{ new MapPoint(80.710628, 45.077082),
					new MapPoint(83.028687, 41.862379) },
			{ new MapPoint(85.93546, 48.414308),
					new MapPoint(88.437492, 46.645143) },
			{ new MapPoint(93.975079, 42.559912),
					new MapPoint(101.462779, 41.600531) },
			{ new MapPoint(93.956681, 44.157262),
					new MapPoint(95.354876, 42.491869) },
			{ new MapPoint(116.69574, 46.301949),
					new MapPoint(120.117638, 44.619006) },
			{ new MapPoint(116.401384, 49.444657),
					new MapPoint(120.191227, 48.057877) },
			{ new MapPoint(121.000708, 53.244099),
					new MapPoint(124.569783, 51.210984) },
			{ new MapPoint(106.724405, 42.232628),
					new MapPoint(113.494611, 41.683336) },
			{ new MapPoint(112.133211, 44.355602),
					new MapPoint(113.5682, 42.123151) },
			{ new MapPoint(110.918989, 43.155464),
					new MapPoint(112.2068, 42.232628) },
			{ new MapPoint(115.150367, 45.324216),
					new MapPoint(116.76933, 44.724032) },
			{ new MapPoint(126.299129, 49.588397),
					new MapPoint(128.102064, 48.057877) },
			{ new MapPoint(128.06527, 49.131761),
					new MapPoint(129.757821, 48.131826) },
			{ new MapPoint(129.721026, 48.62209),
					new MapPoint(130.530508, 47.611932) },
			{ new MapPoint(124.349016, 52.822665),
					new MapPoint(125.710416, 51.095279) },
			{ new MapPoint(122.325313, 28.884167),
					new MapPoint(123.760302, 25.662561) },
			{ new MapPoint(111.029373, 14.651757),
					new MapPoint(118.388292, 10.6053) },
			{ new MapPoint(109.778357, 10.095218),
					new MapPoint(109.778357, 10.095218) },
			{ new MapPoint(109.631178, 10.459649),
					new MapPoint(116.548562, 7.753573) },
			{ new MapPoint(110.514249, 7.826971),
					new MapPoint(113.678584, 4.73448) },
			{ new MapPoint(124.330619, 41.399976),
					new MapPoint(125.48045, 40.68961) },
			{ new MapPoint(126.345123, 42.51229),
					new MapPoint(128.046872, 41.827986) },
			{ new MapPoint(127.973283, 42.539507),
					new MapPoint(129.104717, 42.143692) },
			{ new MapPoint(74.510739, 40.16236),
					new MapPoint(76.350468, 38.678393) },
			{ new MapPoint(119.087389, 21.629857),
					new MapPoint(120.706351, 20.142916) },
			{ new MapPoint(106.853187, 23.339537),
					new MapPoint(108.067408, 21.990651) },
			{ new MapPoint(129.707229, 44.975967),
					new MapPoint(130.985841, 43.017244) },
			{ new MapPoint(130.958245, 44.582859),
					new MapPoint(131.169814, 43.104932) },
			{ new MapPoint(131.418177, 46.247729),
					new MapPoint(133.129126, 45.359896) },
			{ new MapPoint(133.073934, 48.054793),
					new MapPoint(134.269758, 47.409374) },
			{ new MapPoint(99.701237, 23.386249),
					new MapPoint(101.577762, 22.174986) },
			{ new MapPoint(100.179567, 22.243514),
					new MapPoint(101.559364, 21.745927) },
			{ new MapPoint(101.485775, 23.437187),
					new MapPoint(104.24537, 22.875776) },
			{ new MapPoint(98.008686, 25.240784),
					new MapPoint(99.057332, 24.181992) },
			{ new MapPoint(124.463999, 40.686109),
					new MapPoint(124.905534, 40.461646) },
			{ new MapPoint(125.457453, 41.334141),
					new MapPoint(126.055365, 40.979564) },
			{ new MapPoint(126.368119, 41.824546),
					new MapPoint(126.607284, 41.645397) },
			{ new MapPoint(125.47585, 40.979564),
					new MapPoint(125.687419, 40.853958) },
			{ new MapPoint(124.477797, 40.46516),
					new MapPoint(124.72846, 40.343852) },
			{ new MapPoint(124.470898, 40.347371),
					new MapPoint(124.618076, 40.285757) },
			{ new MapPoint(124.891736, 40.694862),
					new MapPoint(125.153898, 40.607283) },
			{ new MapPoint(126.046166, 41.332407),
					new MapPoint(126.262335, 41.165784) },
			{ new MapPoint(127.214395, 41.836586),
					new MapPoint(128.083667, 41.546995) },
			{ new MapPoint(126.386516, 50.257998),
					new MapPoint(126.386516, 50.257998) },
			{ new MapPoint(126.280732, 50.257998),
					new MapPoint(127.513351, 49.580921) },
			{ new MapPoint(126.36352, 50.934256),
					new MapPoint(127.117809, 50.225552) },
			{ new MapPoint(125.669022, 52.39849),
					new MapPoint(126.276133, 51.247082) },
			{ new MapPoint(80.948643, 30.905163),
					new MapPoint(81.403976, 30.280446) },
			{ new MapPoint(83.574857, 30.911112),
					new MapPoint(85.488176, 29.214825) },
			{ new MapPoint(98.136317, 28.872274),
					new MapPoint(99.079179, 27.642374) } };

	// 中国大范围
	private static final MapPoint locBigChina[][] = {
			{ new MapPoint(73.083331, 54.006559),
					new MapPoint(135.266195, 17.015367) }, // 中国本度大框.
			{ new MapPoint(109.806384, 17.579908),
					new MapPoint(112.529184, 3.638301) }, // 以下四个是西沙群岛.
			{ new MapPoint(112.124443, 17.773916),
					new MapPoint(115.583135, 7.159653) },
			{ new MapPoint(115.251984, 17.562261),
					new MapPoint(117.422865, 9.54155) },
			{ new MapPoint(117.054919, 17.773916),
					new MapPoint(118.931443, 11.507795) } };

	// 中国周边范围
	private static final MapPoint locSideChina[][] = {
			{ new MapPoint(125.478833, 40.538425),
					new MapPoint(135.928497, 16.590044) },
			{ new MapPoint(128.054454, 54.43779),
					new MapPoint(136.370032, 49.918776) },
			{ new MapPoint(89.567309, 54.351906),
					new MapPoint(115.617882, 47.881407) },
			{ new MapPoint(71.832315, 54.566279),
					new MapPoint(82.28198, 46.323836) },
			{ new MapPoint(72.788974, 28.001436),
					new MapPoint(85.88785, 16.590044) },
			{ new MapPoint(92.510877, 48.029708),
					new MapPoint(111.570476, 45.034268) },
			{ new MapPoint(85.593493, 26.157025),
					new MapPoint(97.294174, 16.519064) },
			{ new MapPoint(97.073406, 20.935216),
					new MapPoint(107.743838, 16.305964) },
			{ new MapPoint(98.324422, 45.190596),
					new MapPoint(109.142033, 42.854577) },
			{ new MapPoint(71.979493, 45.863038),
					new MapPoint(78.896877, 41.817208) },
			{ new MapPoint(72.374784, 34.326035),
					new MapPoint(78.372303, 27.905294) }, // 印度
			{ new MapPoint(120.131867, 19.569888),
					new MapPoint(125.411891, 15.992375) } // 菲律宾
	};

	/**
	 * 是否在国外
	 * 
	 * @param location
	 * @return
	 */
	public static boolean isOverseaLocation(CTCoordinate2D location) {
		if (location == null) {
			return false;
		}

		// 不在中国大范围，肯定在国外.
		if (!isInRects(location.longitude, location.latitude, locBigChina)) {
			return true;
		}

		// 在中国大范围，但是却在周边国家，那也是国外.
		if (isInRects(location.longitude, location.latitude, locSideChina)) {
			return true;
		}

		// 可能不在国外.
		return false;
	}

	/**
	 * 是否在国内.
	 * 
	 * @param location
	 * @return
	 */
	public static boolean isDemosticLocation(CTCoordinate2D location) {
		if (location == null) {
			return false;
		}

		if (isInRects(location.longitude, location.latitude, locChinaArrs)) {
			return true;
		}

		return false;
	}

	public static boolean isInRects(double inx, double iny, MapPoint[][] rects) {
		// 没必要转换把？浮点精度可以到6位。
		int zoomTimes = 1;
		double lx = (inx * zoomTimes);
		double ly = (iny * zoomTimes);

		double startX = 0, startY = 0;
		double endX = 0, endY = 0;

		for (int i = 0; i < rects.length; ++i) {
			startX = rects[i][0].x * zoomTimes;
			startY = rects[i][0].y * zoomTimes;

			endX = rects[i][1].x * zoomTimes;
			endY = rects[i][1].y * zoomTimes;

			// 只要在任何一个区域内就行。
			if (lx >= startX && lx <= endX) {
				if (ly <= startY && ly >= endY) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 功能描述:( 是否为合法地址)
	 * 
	 * @param location
	 * @return
	 */
	public static boolean isRightLocation(CTCoordinate2D location) {
		// TODO Auto-generated method stub
		if (location != null && Math.abs(location.latitude) <= 90
				&& Math.abs(location.longitude) <= 180) {
			return true;
		}
		return false;
	}

	/**
	 * 根据经纬度获取地址（必须在非UI线程中使用）
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public static CTGeoAddress getAddrByCoordinate(Context context,
			double latitude, double longitude) {

		LogUtils.i("CTLocationUtil getAddrByCoordinate latitude:" + latitude
				+ " longitude:" + longitude);
		
		CTGeoAddress addr = getLocationFromAmap(context, latitude, longitude);
		if (addr == null) {
			// google api
			addr = getLocationFromGoogle(latitude, longitude);
		}

		return addr;
	}

	/**
	 * 使用高德进行同步逆地理解析
	 * @param context
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public static CTGeoAddress getLocationFromAmap(Context context,
			double latitude, double longitude) {
		try {
			if (!(latitude == 0 && longitude == 0)) {
				Looper.prepare();
				// wanglaian: 下面这边代码必须在UI线程执行
				GeocodeSearch geocoderSearch = new GeocodeSearch(context);
				// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
				RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(
						latitude, longitude), 200, GeocodeSearch.AMAP);
				if (Looper.myLooper() != null) {
					Looper.myLooper().quit();
				}

				// 下面进行高德网络逆地址解析
				RegeocodeAddress result = geocoderSearch.getFromLocation(query);

				if (result != null
						&& !TextUtils.isEmpty(result.getFormatAddress())) {
					Address address = new Address(new Locale("zh_CN"));
					address.setCountryName("中国");
					address.setCountryCode("CN");
					String province = result.getProvince();
					String city = result.getCity();

					address.setAdminArea(province);
					if (TextUtils.isEmpty(city) && !TextUtils.isEmpty(province)) {
						city = province;
					}
					if (!TextUtils.isEmpty(city)) {
						if (city.endsWith("市")) {
							city = city.substring(0, city.length() - 1);
						}
						city = city.replace("特别行政区", "");
						city = city.replace("特別行政區", "");
						if (city.equalsIgnoreCase("澳門")) {
							city = "澳门";
						}
					}

					address.setLocality(city);
					address.setSubLocality(result.getDistrict());
					address.setFeatureName(result.getFormatAddress());
					address.setLatitude(latitude);
					address.setLongitude(longitude);

					return addr2GeoAddr(address);
				}
			}
		} catch (Exception e) {
			LogUtils.i("CTLocationUtil getAddrByCoordinate e:" + e);
		}
		
		return null;
	}

	public static CTGeoAddress getLocationFromGoogle(double latitude,
			double longitude) {

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					20 * 1000);
			HttpConnectionParams
					.setSoTimeout(httpClient.getParams(), 20 * 1000);
			StringBuilder sb = new StringBuilder(
					"http://ditu.google.cn/maps/api/geocode/json?latlng=");
			String requestParam = String.valueOf(latitude + ","
					+ String.valueOf(longitude));
			sb.append(requestParam).append("&sensor=true")
					.append("&language=zh-CN");
			HttpGet get = new HttpGet(sb.toString());
			HttpResponse httpResponse = httpClient.execute(get);

			if (httpResponse != null) {
				int nStateCode = httpResponse.getStatusLine().getStatusCode();
				HttpEntity httpEntity = httpResponse.getEntity();
				byte[] arrayOfByte = null;
				if (nStateCode == 200) {
					arrayOfByte = EntityUtils.toByteArray(httpEntity);
				}
				httpEntity.consumeContent();
				String strResponse = new String(arrayOfByte, "UTF-8");
				JSONObject jsonObject = new JSONObject(strResponse);
				boolean isParseSuccess = false;
				if (jsonObject != null) {
					JSONArray array = jsonObject.getJSONArray("results");

					if (array != null) {
						String countryShortName = "";
						String country = "";
						String province = "";
						String city = "";
						String district = "";
						String address = "";

						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							address = obj.optString("formatted_address", "");
							JSONArray address_components = obj
									.optJSONArray("address_components");

							for (int j = 0; j < address_components.length(); j++) {
								JSONObject component = address_components
										.getJSONObject(j);
								String long_name = component
										.getString("long_name");
								JSONArray types = component
										.getJSONArray("types");
								isParseSuccess = true;
								String typeName = types.getString(0);
								if (typeName.equalsIgnoreCase("country")
										&& country.length() == 0) {
									country = long_name;
									countryShortName = component
											.getString("short_name");
								} else if (typeName
										.equalsIgnoreCase("administrative_area_level_1")
										&& province.length() == 0) {
									province = long_name;
								} else if ((typeName
										.equalsIgnoreCase("administrative_area_level_2") || typeName
										.equalsIgnoreCase("locality"))
										&& city.length() == 0) {
									city = long_name;
								} else if ((typeName.equalsIgnoreCase("ward")
										|| typeName
												.equalsIgnoreCase("sublocality_level_1") || typeName
											.equalsIgnoreCase("administrative_area_level_3"))
										&& district.length() == 0) {
									district = long_name;
								}
							}

							if (isParseSuccess) {
								Address addr = new Address(Locale.getDefault());
								addr.setCountryCode(countryShortName);
								addr.setCountryName(country);
								addr.setAdminArea(province);
								addr.setCountryName(country);
								addr.setLocality(city);
								addr.setSubLocality(district);
								addr.setFeatureName(address);
								addr.setLatitude(latitude);
								addr.setLongitude(longitude);
								// BusinessController.setmAddress(address1);

								return addr2GeoAddr(addr);
							}
						}
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static CTGeoAddress addr2GeoAddr(Address addr) {
		if (addr != null) {
			CTGeoAddress geoAddr = new CTGeoAddress();
			geoAddr.coordinate.latitude = addr.getLatitude();
			geoAddr.coordinate.longitude = addr.getLongitude();
			geoAddr.country = addr.getCountryName(); // 国家
			geoAddr.province = addr.getAdminArea(); // 省份
			geoAddr.city = addr.getLocality();
			geoAddr.district = addr.getSubLocality(); // 区名
			geoAddr.countryShortName = addr.getCountryCode();
			geoAddr.formattedAddress = addr.getFeatureName(); // 格式化之后的地址 省市区街道
			return geoAddr;
		} else {
			return null;
		}
	}

	/**
	 * 获取定位sdk当前版本号
	 * 
	 * @return
	 */
	public static String getSdkVersion() {
		return SDK_VERSION;
	}
}
