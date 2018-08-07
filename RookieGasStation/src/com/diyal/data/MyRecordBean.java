package com.diyal.data;

import java.util.List;

public class MyRecordBean {
	public List<C> records;

	public static class C {
		public String oid;
		public int style;
		public int state;
		public String remark;
		public String time;
		public String money;
		public String subject;
		public String paytime;
		public int paytype;
		public String paymoney;
		public int paycoin;
	}
}
