package com.diyal.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.diyal.app.GasStationApplication;
import com.diyal.data.JsonBean.Q;
import com.diyal.net.C2SReqOilDetailBean;
import com.diyal.net.NetWorkManger;
import com.diyal.net.S2CReqOilInfoBean;
import com.diyal.rookiegasstation.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.activity_gasdetail)
public class GasDetailActivity extends Activity {

	private int sno;

	@ViewInject(R.id.detail_adress)
	private TextView detail_adress;

	@ViewInject(R.id.gas_1)
	private TextView gas_1;

	@ViewInject(R.id.gas_2)
	private TextView gas_2;

	@ViewInject(R.id.gas_3)
	private TextView gas_3;

	@ViewInject(R.id.general_title)
	private TextView general_title;

	// 头部返回按钮
	@ViewInject(R.id.leftreturn)
	private TextView leftreturn;

	// 下单按钮
	@ViewInject(R.id.orderok)
	private TextView orderok;

	private S2CReqOilInfoBean oilInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtils.i(this.getClass().getName());
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		if (GasStationApplication.getInstance().tmpGasInfo == null) {
			return;
		}

		sno = GasStationApplication.getInstance().tmpGasInfo.sno;

		// 加载数据
		loadData();
	}

	@OnClick({ R.id.leftreturn, R.id.orderok })
	public void onBtnClick(View v) {
		switch (v.getId()) {
		case R.id.leftreturn: // 返回主菜单
			Intent intent = new Intent(GasDetailActivity.this,
					MainActivity.class);
			startActivity(intent);

			break;
		case R.id.orderok: // 确认下单
			// requestOrder();
			Intent intent2 = new Intent(GasDetailActivity.this,
					OrderInfoActivity.class);
			startActivity(intent2);
			break;

		default:
			break;
		}
	}

	private void loadData() {

		C2SReqOilDetailBean reqServBean = C2SReqOilDetailBean.create(
				getApplicationContext(), "station", sno);
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

						oilInfo = S2CReqOilInfoBean.create(retStr);

						LogUtils.d("onSuccess:" + retStr);

						fillView();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.d("onFailure");
					}
				}

		);

	}

	// private void requestOrder() {
	//
	// C2SReqOrderBean reqServBean = C2SReqOrderBean.create(
	// getApplicationContext(), "defray", "购油卡支付", oilInfo.name
	// + "(5.2L)", 1, 198);
	// NetWorkManger.getInstance().request(reqServBean.toJson(),
	// new RequestCallBack<String>() {
	//
	// @Override
	// public void onStart() {
	// LogUtils.d("onStart");
	// }
	//
	// @Override
	// public void onLoading(long total, long current,
	// boolean isUploading) {
	// LogUtils.d("onLoading");
	// }
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> responseInfo) {
	// String retStr = responseInfo.result;
	//
	// S2CReqOrderBean oidReq = S2CReqOrderBean.create(retStr);
	//
	// LogUtils.d("onSuccess:" + retStr);
	// if (oidReq.err == 0) {
	// Intent intent2 = new Intent(GasDetailActivity.this,
	// TwoDimensionCodeActivity.class);
	// intent2.putExtra("OrderId", oidReq.oid);
	// startActivity(intent2);
	// } else {
	// Toast.makeText(getApplicationContext(), "订单请求失败",
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// }
	//
	// @Override
	// public void onFailure(HttpException error, String msg) {
	// LogUtils.d("onFailure");
	// }
	// }
	//
	// );
	//
	// }

	private void fillView() {
		detail_adress.setText(oilInfo.province);

		general_title.setText(oilInfo.name);

		List<Q> gasdesclistTmp = GasStationApplication.getInstance().tmpGasInfo.quote;
		if (gasdesclistTmp.size() > 0) {
			for (int i = 0; i < gasdesclistTmp.size(); i++) {
				switch (i) {
				case 0:
					gas_1.setText(String.format(
							getResources().getString(R.string.oil_prite_txt),
							gasdesclistTmp.get(0).oil,
							(float) gasdesclistTmp.get(0).price,
							gasdesclistTmp.get(0).otdprice, 0.2));
					break;
				case 1:
					gas_2.setText(String.format(
							getResources().getString(R.string.oil_prite_txt),
							gasdesclistTmp.get(1).oil,
							(float) gasdesclistTmp.get(1).price,
							gasdesclistTmp.get(1).otdprice, 0.2));
					break;
				case 2:
					gas_3.setText(String.format(
							getResources().getString(R.string.oil_prite_txt),
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
}
