package com.diyal.ui.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.diyal.data.MyRecordBean;
import com.diyal.data.MyRecordBean.C;
import com.diyal.net.C2SReqMyRecordsBean;
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

@SuppressLint("NewApi")
public class FragmentRecord extends Fragment {

	private Context mAppContext;

	@ViewInject(R.id.myrecord_list)
	private ListView mMyRecordList;
	
	MyAdapter adapter = null;
	public List<C> listRecordTmpData = null;
	//我的消费记录ListBean
	private MyRecordBean MyRecordListBean = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.myrecord_list, container, false);
		ViewUtils.inject(this, view);

		mAppContext = inflater.getContext().getApplicationContext();
		
		adapter = new MyAdapter(); // 创建自定义适配器对象
		mMyRecordList.setAdapter(adapter);
		loadData();
		return view;
	}

	// 消费记录请求 startdate 开始日期  enddate 结束日期
	public void loadData() {

		String startdate = "2015-04-10";
		String enddate = "2015-04-11";

		C2SReqMyRecordsBean reqServBean = C2SReqMyRecordsBean.create(mAppContext,
				"transaction", startdate, enddate);
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
						java.lang.reflect.Type type = new TypeToken<MyRecordBean>() {
						}.getType();
						MyRecordListBean = gson.fromJson(retStr, type);
						listRecordTmpData = MyRecordListBean.records;
						
						
						// 更新UI
						adapter.notifyDataSetChanged();
						
						LogUtils.d("onsuccess myrecord" + retStr);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.d("onFailure");
					}
				}

		);

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
			if ((listRecordTmpData != null) && (listRecordTmpData.size() > 0)) {
				listCount = listRecordTmpData.size();
			}

			return listCount;
		}

		@Override
		public Object getItem(int position) {

			if ((listRecordTmpData != null) && (listRecordTmpData.size() > 0)) {
				return listRecordTmpData.get(position);
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
						R.layout.myrecord_item, null); // 通过listitem xml去填充视图
				vh = new ViewHolder();
				vh.tv_record_content = (TextView) convertView
						.findViewById(R.id.myrecord_content);
				convertView.setTag(vh); // 设置Tag，为该list item的数据存储对象
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			if ((listRecordTmpData != null) && (listRecordTmpData.size() > 0)) {
				C oneRecord = listRecordTmpData.get(position);
				vh.tv_record_content.setText("订单号：" + oneRecord.oid + " 类型："
						+ oneRecord.style + " 状态：" + oneRecord.state + " 备注："
						+ oneRecord.remark + " 时间：" + oneRecord.time + " 金额："
						+ oneRecord.money + " 主题：" + oneRecord.subject
						+ " 付款时间：" + oneRecord.paytime + " 付款类型："
						+ oneRecord.paytype + " 付款金额：" + oneRecord.paymoney
						+ " 付款硬币：" + oneRecord.paycoin);
			}
			return convertView;
		}
	}

	// 视图Holder 类似于Bean
	static class ViewHolder {
		TextView tv_record_content;
	}
}
