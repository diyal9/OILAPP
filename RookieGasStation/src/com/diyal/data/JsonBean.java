package com.diyal.data;

import java.util.List;

public class JsonBean {
	public int err;
	public String date;
	public List<G> favorite;
	public List<G> gasoline;
	public List<G> dieseloil;

	public static class G {
		public int sno;
		public String name;
		public String address;
		public List<Q> quote;
	}

	public static class Q {
		public String oil;
		public int price;
		public double otdprice;
	}
}
