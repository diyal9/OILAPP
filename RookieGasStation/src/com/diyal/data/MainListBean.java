/**   
 * @Title: MainListBean.java 
 * @Package com.diyal.bean 
 * @System TODO  
 * @author 670924505@qq.com  
 * @date 2014-8-21 
 * @Copyright (c) Diyal All Rights Reserved.  
 */
package com.diyal.data;

import java.util.List;

/**
 * @ClassName: MainListBean
 * @Description: 主页面接收数据Bean
 * @author diyal.yin
 * @date 2014-8-21 下午2:18:57
 * 
 */
public class MainListBean {

	/** myinvoiceinfo list */
	private List myinvoiceinfo;

	/** 客户信息 list */
	private List mycustomerinfo;

	/** 商品信息 list */
	private List mygoodsinfo;

	/** 统计 list */
	private List statisticsinfo;

	private String invoicename;
	private String other;
	private String invoiceid;

	private String customername;
	private String customerid;
	private String customerother;

	private String goodsname;
	private String goodsid;
	private String goodsother;

	public List getMycustomerinfo() {
		return mycustomerinfo;
	}

	public void setMycustomerinfo(List mycustomerinfo) {
		this.mycustomerinfo = mycustomerinfo;
	}

	public List getMygoodsinfo() {
		return mygoodsinfo;
	}

	public void setMygoodsinfo(List mygoodsinfo) {
		this.mygoodsinfo = mygoodsinfo;
	}

	public List getStatisticsinfo() {
		return statisticsinfo;
	}

	public void setStatisticsinfo(List statisticsinfo) {
		this.statisticsinfo = statisticsinfo;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getCustomerother() {
		return customerother;
	}

	public void setCustomerother(String customerother) {
		this.customerother = customerother;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getGoodsother() {
		return goodsother;
	}

	public void setGoodsother(String goodsother) {
		this.goodsother = goodsother;
	}

	public String getStatisticsname() {
		return statisticsname;
	}

	public void setStatisticsname(String statisticsname) {
		this.statisticsname = statisticsname;
	}

	public String getStatisticsid() {
		return statisticsid;
	}

	public void setStatisticsid(String statisticsid) {
		this.statisticsid = statisticsid;
	}

	public String getStatisticsother() {
		return statisticsother;
	}

	public void setStatisticsother(String statisticsother) {
		this.statisticsother = statisticsother;
	}

	private String statisticsname;
	private String statisticsid;
	private String statisticsother;

	public List getMyinvoiceinfo() {
		return myinvoiceinfo;
	}

	public void setMyinvoiceinfo(List myinvoiceinfo) {
		this.myinvoiceinfo = myinvoiceinfo;
	}

	public String getInvoicename() {
		return invoicename;
	}

	public void setInvoicename(String invoicename) {
		this.invoicename = invoicename;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getInvoiceid() {
		return invoiceid;
	}

	public void setInvoiceid(String invoiceid) {
		this.invoiceid = invoiceid;
	}

}
