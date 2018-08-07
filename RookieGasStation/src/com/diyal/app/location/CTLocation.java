package com.diyal.app.location;

/**
 * 定义定位数据结构
 * 
 * @author lawang
 * 
 */
public class CTLocation {
	
	/**
	 * 国家类型
	 * 
	 * @author lawang
	 * 
	 */
	public enum CTLocationCountryType {
		CTLocationCountryTypeDomestic, // 国内位置
		CTLocationCountryTypeOversea, // 国外位置
		CTLocationCountryTypeUnknown, // 未知
	};

	// 精度级别
	public enum CTLocationAccuracyLevel {
		CTLocationAccuracyLevelNormal, // 普通定位精度（100-500米）
		CTLocationAccuracyLevelBest, // 最佳位置（100米以内）
	};

	// 定位失败类型
	public enum CTLocationFailType {
		CTLocationFailTypeNotEnabled, // 定位未开启
		CTLocationFailTypoCoordinate, // 获取经纬度失败
		CTLocationFailTypeTimeout, // 定位超时
		CTLocationFailTypeGeoAddressFailed, // GeoCoding失败
	};

}
