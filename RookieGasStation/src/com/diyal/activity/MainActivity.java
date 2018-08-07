package com.diyal.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diyal.app.GasStationApplication;
import com.diyal.app.location.CTCoordinate2D;
import com.diyal.app.location.CTGeoAddress;
import com.diyal.app.location.CTLocation.CTLocationFailType;
import com.diyal.app.location.CTLocationListener;
import com.diyal.app.location.CTLocationManager;
import com.diyal.net.C2SReqServerBean;
import com.diyal.net.NetWorkManger;
import com.diyal.net.S2CReqUUIDLoginBean;
import com.diyal.rookiegasstation.R;
import com.diyal.ui.fragment.FragmentGas;
import com.diyal.ui.fragment.FragmentMine;
import com.diyal.ui.fragment.FragmentRecord;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

enum MAIN_VIEW_FLG {
	MAINVIEW_QYGAS, MAINVIEW_CYGAS, MAINVIEW_NOTICE
}

@ContentView(R.layout.activity_main)
public class MainActivity extends Activity {

	private Context context;

	// @ViewInject(R.id.main_gas_list)
	// private ListView mMaintList;

	// 定位城市title
	@ViewInject(R.id.location_city)
	private TextView locationCity;

	// 扫描按钮图标
	@ViewInject(R.id.general_scan_btn)
	private ImageView scanImageBtn;

	// 底部按钮监听
	// 实时油价
	@ViewInject(R.id.home_curgasprice)
	private LinearLayout lCurGasPrice;

	// 消费记录
	@ViewInject(R.id.home_costrecord)
	private LinearLayout lCostRecord;

	// 底部按钮监听
	@ViewInject(R.id.home_my)
	private LinearLayout lMyInfo;

	// 实时油价
	private FragmentGas mGasFragment;
	// 消费记录
	private FragmentRecord mRecordFragment;
	// 我的
	private FragmentMine mMineFragment;

	private final boolean TO_LOAD_STATE_FALSE = false;
	private final boolean TO_LOAD_STATE_TRUE = true;

	private Map<Integer, LinearLayout> tmpItemMap = null; // 底部控件集合
	private int lastClickItemOfFooterId; // 最后选中的底部菜单(默认设置为第一个)
	private Fragment con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		// No Title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_PROGRESS); // 进度指示器功能
		ViewUtils.inject(this);

		initView();

		// 设置扫描按钮图标
//		scanImageBtn.setImageResource(R.drawable.flicking);

		uuidLogin();

	}

	private void initView() {
		// 初始显示第一个Tab
		if (mGasFragment == null) {
			mGasFragment = new FragmentGas();
		}
		getFragment(TO_LOAD_STATE_TRUE, null, mGasFragment);
	}

	// 底部菜单存储，用来按钮式样和监听
	private void footerMenuGet() {
		tmpItemMap = new HashMap<Integer, LinearLayout>();
		tmpItemMap.put(R.id.home_curgasprice, lCurGasPrice);
		tmpItemMap.put(R.id.home_costrecord, lCostRecord);
		tmpItemMap.put(R.id.home_my, lMyInfo);
	}

	@OnClick(R.id.general_scan_btn)
	public void onClick(View v) {
		LogUtils.i("扫描");

//		Intent intent = new Intent();
//		intent.setClass(MainActivity.this, MipcaCaptureActivity.class);
//		startActivity(intent);

	}

	/**
	 * 底部按钮监听绑定
	 */
	@OnClick({ R.id.home_curgasprice, R.id.home_costrecord, R.id.home_my })
	private void bindFooterButtonEvent(View v) {

		if (tmpItemMap == null || tmpItemMap.size() == 0) {
			// 底部菜单存储
			footerMenuGet();
		}
		Iterator iter = tmpItemMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			LinearLayout item = tmpItemMap.get(key);

			if (v.getId() == Integer.valueOf(key.toString())) {
				item.setBackgroundColor(getResources().getColor(
						R.color.title_bar_main_press_color));
			} else {
				item.setBackgroundColor(getResources().getColor(
						R.color.title_bar_main_color));
			}
		}

		// 按钮监听
		if (lastClickItemOfFooterId != v.getId()) {
			switch (v.getId()) {
			case R.id.home_curgasprice:
				if (mGasFragment == null) {
					mGasFragment = new FragmentGas();
				}
				getFragment(TO_LOAD_STATE_FALSE, con, mGasFragment);

				break;
			case R.id.home_costrecord:
				if (mRecordFragment == null) {
					mRecordFragment = new FragmentRecord();
					getFragment(TO_LOAD_STATE_TRUE, con, mRecordFragment);
				} else {
					getFragment(TO_LOAD_STATE_FALSE, con, mRecordFragment);
				}

				break;

			case R.id.home_my:
				if (mMineFragment == null) {
					mMineFragment = new FragmentMine();
					getFragment(TO_LOAD_STATE_TRUE, con, mMineFragment);
				} else {
					getFragment(TO_LOAD_STATE_FALSE, con, mMineFragment);
				}

				break;

			default:
				break;
			}
		}

		lastClickItemOfFooterId = v.getId();
	}

	@SuppressLint("NewApi")
	private void getFragment(Boolean state, Fragment form, Fragment to) {
		FragmentTransaction homefragment = getFragmentManager()
				.beginTransaction();
		if (state) {
			if (form == null) {
				homefragment.add(R.id.fragment, to).commit();
			} else {
				homefragment.hide(form).add(R.id.fragment, to).commit();
			}
		} else {
			homefragment.hide(form).show(to).commit();
		}
		con = to;
	}

	/**
	 * UUID登录 一般情况会是在app启动的时候。这里暂时放在这里 如果有splash的话，就放在那
	 * 
	 **/
	private void uuidLogin() {

		C2SReqServerBean reqServBean = C2SReqServerBean
				.create(context, "login");

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
						LogUtils.d("onSuccess:" + retStr);

						// 存储数据
						S2CReqUUIDLoginBean uidLoginBean = S2CReqUUIDLoginBean
								.create(retStr);
						// 将解析出来的数据存到Application
						GasStationApplication.getInstance().setSessionData(
								uidLoginBean);

						// // 请求列表数据
						getDataByLocation();

						LogUtils.i("");
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.d("onFailure");
					}
				}

		);
	}

	// 获取经纬度
	private void getDataByLocation() {
		// 开始定位
		CTLocationManager.getInstance(getApplicationContext()).startLocate(
				new CTLocationListener() {

					@Override
					public void onLocationFailed(CTLocationFailType failedType) {
						LogUtils.i("");

					}

					@Override
					public void onCoordinateSuccess(CTCoordinate2D coordinate) {
						LogUtils.i("");

					}

					@Override
					public void onCTGeoAddressSuccess(
							CTGeoAddress addressLocation) {
						// 定位成功
						LogUtils.i("定位成功:");
						locationCity.setText(addressLocation.city);

						GasStationApplication.getInstance().addrLocation = addressLocation;

						mGasFragment.setLocation(addressLocation);
						mGasFragment.loadData();

					}
				});
	}

}
