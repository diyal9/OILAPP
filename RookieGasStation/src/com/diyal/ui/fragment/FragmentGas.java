package com.diyal.ui.fragment;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.diyal.activity.GasDetailActivity;
import com.diyal.app.GasStationApplication;
import com.diyal.app.location.CTGeoAddress;
import com.diyal.data.JsonBean;
import com.diyal.data.JsonBean.G;
import com.diyal.data.JsonBean.Q;
import com.diyal.net.C2SReqInquiryBean;
import com.diyal.net.NetWorkManger;
import com.diyal.rookiegasstation.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@SuppressLint("NewApi")
public class FragmentGas extends Fragment implements OnScrollListener {

	private Context mAppContext;

	// 顶部按钮监听
	// 关注
	@ViewInject(R.id.linear_favorite_id)
	private LinearLayout linear_favorite;

	// 汽油站
	@ViewInject(R.id.linear_gasoil_station_id)
	private LinearLayout linear_gasoil_station;

	// 柴油站
	@ViewInject(R.id.linear_dieseloil_station_id)
	private LinearLayout linear_dieseloil_station;

	@ViewInject(R.id.main_gas_list)
	private ListView mMaintList;

	// 刷新提示视图
	private View footer;

	MyAdapter adapter = null;
	private List<G> listTmpData = null; // 首界面List数据缓冲区s
	private JsonBean mainListBean = null;

	/** 经纬度 */
	private static CTGeoAddress addrLocation;

	private Map<Integer, LinearLayout> tmpItemheadMap = null; // 顶部控件集合
	private int lastClickItemOfHeaderId; // 最后选中的关注列(默认设置为第一个)

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gas, container, false);
		ViewUtils.inject(this, view);

		mAppContext = getActivity().getApplicationContext();

		lastClickItemOfHeaderId = R.id.linear_gasoil_station_id; // 初始选中第一个菜单

		// 在MainAct中判断是否取得，如果没有渠道则使用缓存中的，如没缓存则使用默认北京
		addrLocation = GasStationApplication.getInstance().addrLocation;

		// 下拉列表加载提示
		// footer = getActivity().getLayoutInflater().inflate(
		// R.layout.xlistview_footer, null);
		// mMaintList.addFooterView(footer);

		mMaintList.setOnScrollListener(this);

		adapter = new MyAdapter(); // 创建自定义适配器对象
		mMaintList.setAdapter(adapter);

		// List Item 监听
		mMaintList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onListItemClickListener(arg1, arg2, arg3);
			}

		});

		return view;
	}

	// 查看更多
	private void onListItemClickListener(View arg1, int arg2, long arg3) {

		System.out.println("选中了:" + arg2);

		if (arg2 == adapter.getCount()) { // 查看更多
			// loadData();
		} else {

			Bundle bundle = new Bundle(); // 创建Bundle对象
			bundle.putString("sno", String.valueOf(listTmpData.get(arg2).sno)); // 装入数据

			// TODO Fragment -> Activity传数据有点问题，赶时间先不管。之后再改
			GasStationApplication.getInstance().tmpGasInfo = listTmpData
					.get(arg2);

			Intent intent = new Intent();
			intent.setClass(mAppContext, GasDetailActivity.class);
			getActivity().startActivity(intent);

		}
	}

	@OnClick({ R.id.linear_gasoil_station_id, R.id.linear_dieseloil_station_id,
			R.id.linear_favorite_id })
	private void bindButtonEvent(View v) {
		lastClickItemOfHeaderId = v.getId();
		loadData();
		// switch (v.getId()) {
		// case R.id.linear_gasoil_station_id:
		// break;
		// case R.id.linear_dieseloil_station_id:
		//
		// break;
		// case R.id.linear_favorite_id:
		//
		// break;
		//
		// default:
		// break;
		// }
	}

	public void setLocation(CTGeoAddress addr) {
		addrLocation = addr;
	}

	// 数据请求 x 经度 y 纬度
	public void loadData() {

		double x = addrLocation.longitude;
		double y = addrLocation.latitude;

		C2SReqInquiryBean reqServBean = C2SReqInquiryBean.create(mAppContext,
				"inquiry", x, y);
		NetWorkManger.getInstance().request(reqServBean.toJson(),
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						LogUtils.d("onStart");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						LogUtils.d("onLoading");
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String retStr = responseInfo.result;
						Gson gson = new Gson();
						java.lang.reflect.Type type = new TypeToken<JsonBean>() {
						}.getType();
						mainListBean = gson.fromJson(retStr, type);
						LogUtils.d("onSuccess:" + retStr);

						// 取出当前页面需要显示的数据
						// 按钮监听
						switch (lastClickItemOfHeaderId) {
						case R.id.linear_favorite_id:
							listTmpData = mainListBean.favorite;
							break;
						case R.id.linear_gasoil_station_id:
							listTmpData = mainListBean.gasoline;

							break;

						case R.id.linear_dieseloil_station_id:
							listTmpData = mainListBean.dieseloil;

							break;

						default:
							break;
						}

						// 更新UI
						adapter.notifyDataSetChanged();

						// footer.setVisibility(View.GONE);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.d("onFailure");
					}
				}

		);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.
	 * AbsListView, int, int, int)
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisableitem,
			int visiableItemCount, int totalItemCount) {
		System.out.println("firstVisableitem:" + firstVisableitem);
		System.out.println("visiableItemCount:" + visiableItemCount);
		System.out.println("totalItemCount:" + totalItemCount);
		// 可见的最后一行的索引 =第一行index + 可见的行数 - 1
		visibleLastNum = firstVisableitem + visiableItemCount - 1;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android
	 * .widget.AbsListView, int)
	 */
	// 记录屏幕可见范围最大的行index
	int visibleLastNum = 0;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 加载数据刷新
		if ((visibleLastNum == adapter.getCount()) // 如果可见的最后一行index等于适配器对象
				// 的总数据行数（及最后一行）
				&& (scrollState == OnScrollListener.SCROLL_STATE_IDLE)) { //

			// 并且视图不处于scoll状态
			loadData();
		}

	}

	int index = 1; // 加载的数据行数index

	/***
	 * 
	 * @ClassName: 自定义适配器类\
	 * @Description: listview的Adapter
	 * @author diyal.yin
	 * @date 2014-8-22 上午10:57:25
	 * 
	 */
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			int listCount = 0;
			if ((listTmpData != null) && (listTmpData.size() > 0)) {
				listCount = listTmpData.size();
			}

			return listCount;
		}

		@Override
		public Object getItem(int position) {

			if ((listTmpData != null) && (listTmpData.size() > 0)) {
				return listTmpData.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder vh = null;
			if (convertView == null) { // 如果convertView不存在，则去创建Listcell
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.follower_item, null); // 通过listitem xml去填充视图
				vh = new ViewHolder();
				vh.iv_gas_image = (ImageView) convertView
						.findViewById(R.id.gas_image);
				vh.tv_gas_name = (TextView) convertView
						.findViewById(R.id.gas_name_id);
				vh.tv_gas_address = (TextView) convertView
						.findViewById(R.id.gas_address_id);
				vh.tv_gas_desc1 = (TextView) convertView
						.findViewById(R.id.gas_desc1_id);
				vh.tv_gas_desc2 = (TextView) convertView
						.findViewById(R.id.gas_desc2_id);
				vh.tv_gas_desc3 = (TextView) convertView
						.findViewById(R.id.gas_desc3_id);

				convertView.setTag(vh); // 设置Tag，为该list item的数据存储对象
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			if ((listTmpData != null) && (listTmpData.size() > 0)) {
				vh.iv_gas_image.setImageDrawable(getResources().getDrawable(
						R.drawable.logo_icon));
				vh.tv_gas_name.setText(listTmpData.get(position).name);
				vh.tv_gas_address.setText(listTmpData.get(position).address);
				List<Q> gasdesclistTmp = listTmpData.get(position).quote;
				if (gasdesclistTmp.size() > 0) {
					for (int i = 0; i < gasdesclistTmp.size(); i++) {
						switch (i) {
						case 0:
							vh.tv_gas_desc1.setText(String.format(
									getResources().getString(
											R.string.oil_prite_txt),
									gasdesclistTmp.get(0).oil,
									(float) gasdesclistTmp.get(0).price,
									gasdesclistTmp.get(0).otdprice, 0.2));
							break;
						case 1:
							vh.tv_gas_desc2.setText(String.format(
									getResources().getString(
											R.string.oil_prite_txt),
									gasdesclistTmp.get(1).oil,
									(float) gasdesclistTmp.get(1).price,
									gasdesclistTmp.get(1).otdprice, 0.2));
							break;
						case 2:
							vh.tv_gas_desc3.setText(String.format(
									getResources().getString(
											R.string.oil_prite_txt),
									gasdesclistTmp.get(2).oil,
									(float) gasdesclistTmp.get(2).price,
									gasdesclistTmp.get(2).otdprice, 0.2));
							break;
						default:
							break;
						}
					}
				}
			}
			// convertView.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View pos) {
			// LogUtils.d("onClick" + pos);
			//
			// // Bundle bundle = new Bundle(); // 创建Bundle对象
			// // bundle.putInt("sno", listTmpData.get(pos.get).sno); // 装入数据
			// //
			// // Intent intent = new Intent();
			// // intent.setClass(MainActivity.this,
			// // MipcaCaptureActivity.class);
			// // startActivity(intent);
			// }
			// });
			return convertView;
		}
	}

	// 视图Holder 类似于Bean
	static class ViewHolder {
		ImageView iv_gas_image;
		TextView tv_gas_name;
		TextView tv_gas_address;
		TextView tv_gas_desc1;
		TextView tv_gas_desc2;
		TextView tv_gas_desc3;
	}

}
