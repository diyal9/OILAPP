package com.diyal.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diyal.activity.CZActivity;
import com.diyal.net.C2SReqQueryMyInfoBean;
import com.diyal.net.NetWorkManger;
import com.diyal.net.S2CReqQuerymyInfoBean;
import com.diyal.rookiegasstation.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@SuppressLint("NewApi")
public class FragmentMine extends Fragment {

	private Context mAppContext;

	@ViewInject(R.id.mine_amount)
	private TextView mine_amount;

	@ViewInject(R.id.mine_zhifu)
	private TextView mine_zhifu;

	private S2CReqQuerymyInfoBean queryMyInfoBean;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine, container, false);
		ViewUtils.inject(this, view);

		mAppContext = inflater.getContext().getApplicationContext();

		loadData();
		return view;
	}

	@OnClick({ R.id.leftreturn, R.id.mine_zhifu })
	public void onBtnClick(View v) {
		switch (v.getId()) {
		case R.id.leftreturn: // 返回主菜单
			// Intent intent = new Intent(GasDetailActivity.this,
			// MainActivity.class);
			// startActivity(intent);

			break;
		case R.id.mine_zhifu: // 确认下单

			Intent intent = new Intent();
			intent.setClass(mAppContext, CZActivity.class);
			getActivity().startActivity(intent);
			break;

		default:
			break;
		}
	}

	public void loadData() {

		C2SReqQueryMyInfoBean reqServBean = C2SReqQueryMyInfoBean.create(
				mAppContext, "myInfo");
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

						queryMyInfoBean = S2CReqQuerymyInfoBean.create(retStr);
						LogUtils.d("onsuccess myInfo" + retStr);

						reflashView();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.d("onFailure");
					}
				}

		);
	}

	private void reflashView() {
		String realAmount = "￥" + queryMyInfoBean.money;
		mine_amount.setText(realAmount);
	}
}
