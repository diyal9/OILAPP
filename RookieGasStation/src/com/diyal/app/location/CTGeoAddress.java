package com.diyal.app.location;

public class CTGeoAddress {
	public CTCoordinate2D coordinate;
	public String country; // 国家
	public String province; // 省份
	public String city; // 城市
	public String district; // 区名
	public String countryShortName;
	public String formattedAddress; // 格式化之后的地址 省市区街道

	public double latitude; // 纬度
	public double longitude; // 经度

	// public CTLocationAccuracyLevel accuracyLevel; // 精确度
	// public long geoTimestamp; // 获取到经纬度时间
	// public String cityCode; // 城市码－高德返回
	// public String adCode; // 区位码－高德返回
	// public String street; // 街道

	public CTGeoAddress() {
		coordinate = new CTCoordinate2D();
		country = "";
		countryShortName = "";
		province = "";
		city = "";
		district = "";
		formattedAddress = "";
		longitude = 0d;
		latitude = 0d;
	}

	public String toString() {
		return "coordinate:" + coordinate + " country:" + country
				+ " countryShortName:" + countryShortName + " province:"
				+ province + " city:" + city + " district:" + district
				+ " formattedAddress:" + formattedAddress;
	}
}