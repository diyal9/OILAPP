package com.diyal.app.location;

/**
 * 定义经纬度
 * @author lawang
 *
 */
public class CTCoordinate2D {
	/**
	 * 经度
	 */
	public double longitude;
	/**
	 * 纬度
	 */
	public double latitude;
	
	public CTCoordinate2D() {
		longitude = 0.0;
		latitude = 0.0;
	}
	
	public CTCoordinate2D(double x, double y) {
		longitude = x;
		latitude = y;
	}
	
	public String toString() {
		return "longitude:" + longitude + " latitude:" + latitude;
	}
}
