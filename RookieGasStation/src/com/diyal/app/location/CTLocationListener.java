package com.diyal.app.location;

import com.diyal.app.location.CTLocation.CTLocationFailType;

public interface CTLocationListener {

	/**
	 *  定位经纬度成功；
	 * @param coordinate
	 */
	public void onCoordinateSuccess(CTCoordinate2D coordinate);

	/**
	 *  逆地址解析成功；
	 * @param addressLocation
	 */
	public void onCTGeoAddressSuccess(CTGeoAddress addressLocation);

	/**
	 *  定位失败
	 * @param faildType
	 */
	public void onLocationFailed(CTLocationFailType failedType);
}
