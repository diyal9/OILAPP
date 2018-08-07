package com.diyal.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.SignUtils;
import com.diyal.app.GasStationApplication;
import com.diyal.config.Constant;
import com.diyal.data.JsonBean;
import com.diyal.data.JsonBean.Q;
import com.diyal.net.C2SReqOrderBean;
import com.diyal.net.NetWorkManger;
import com.diyal.net.S2CReqOrderBean;
import com.diyal.rookiegasstation.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.acitivty_orderdetail)
public class OrderInfoActivity extends Activity {

	@ViewInject(R.id.general_title)
	private TextView general_title;

	// 车牌
	@ViewInject(R.id.detail_carno)
	private EditText detail_carno;

	// 手机号码
	@ViewInject(R.id.detail_phoneno)
	private EditText detail_phoneno;

	// 发票类型
	@ViewInject(R.id.detail_invoicetype)
	private Spinner detail_invoicetype;

	// 发票抬头
	@ViewInject(R.id.detail_invoicetitle)
	private EditText detail_invoicetitle;

	// 油品
	@ViewInject(R.id.detail_oilchange)
	private Spinner detail_oilchange;

	// // 预计油量
	// @ViewInject(R.id.detail_mayoilnum)
	// private EditText detail_mayoilnum;

	// 余额
	@ViewInject(R.id.detail_amount)
	private EditText detail_amount;

	private String orderid;

	private ArrayAdapter<String> _oilAdapter;

	private Map<String, JsonBean.Q> oilMap; // 油价容器

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtils.i(this.getClass().getName());
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		initView();

	}

	private void loadData() {

		int invType = detail_invoicetype.getSelectedItemPosition();

		C2SReqOrderBean reqServBean = C2SReqOrderBean.create(
				getApplicationContext(), "defray", GasStationApplication
						.getInstance().tmpGasInfo.sno, detail_oilchange
						.getSelectedItem().toString(), Double
						.valueOf(detail_amount.getText().toString()), Double
						.valueOf(0), invType, detail_invoicetitle.getText()
						.toString(), detail_phoneno.getText().toString(),
				detail_carno.getText().toString());
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

						S2CReqOrderBean order = S2CReqOrderBean.create(retStr);

						if ((order.err == 0) && (order != null)
								&& (order.oid != null)) {
							Toast.makeText(getApplicationContext(), "购买成功",
									Toast.LENGTH_LONG).show();

							Intent intent = new Intent(OrderInfoActivity.this,
									MainActivity.class);
							startActivity(intent);
						} else {
							orderid = order.oid;

							Toast.makeText(getApplicationContext(), "购买失败",
									Toast.LENGTH_SHORT).show();

							// 如果钱不足再去充值
							// pay();
						}

						LogUtils.d("onSuccess:" + retStr);

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.d("onFailure");
					}
				}

		);

	}

	private void initView() {

		general_title.setText("下单");

		String[] mGoodsNameItems = { "普通发票", "增值税发票" };
		ArrayAdapter<String> _goodsAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mGoodsNameItems);
		detail_invoicetype.setAdapter(_goodsAdapter);

		List<Q> gList = GasStationApplication.getInstance().tmpGasInfo.quote;
		oilMap = new HashMap<String, JsonBean.Q>();
		String[] mOilItems = new String[gList.size()];
		for (int i = 0; i < gList.size(); i++) {
			try {
				mOilItems[i] = new String(gList.get(i).oil);
				oilMap.put(gList.get(i).oil, gList.get(i));

			} catch (Exception e) {
				System.out.println("");
			}

		}

		// 根据油品找到价格
		_oilAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mOilItems);
		detail_oilchange.setAdapter(_oilAdapter);
		detail_oilchange
				.setOnItemSelectedListener(new SpinnerSelectedListener());
	}

	class SpinnerSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// if (!"".equals(detail_mayoilnum.getText().toString())) {
			// detail_amount
			// .setText(""
			// + (Double.valueOf(detail_mayoilnum.getText()
			// .toString()) * oilMap.get(_oilAdapter
			// .getItem(arg2)).price));
			// }

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	// 支付按钮监听
	@OnClick({ R.id.scan_pay, R.id.scan_resharge })
	private void payClick(View v) {
		switch (v.getId()) {
		case R.id.scan_pay:

			if (check()) {
				loadData();
			}

			break;
		case R.id.scan_resharge:

			break;
		default:
			break;
		}
	}

	private boolean check() {
		// if ("".equals(detail_mayoilnum.getText().toString())) {
		// Toast.makeText(getApplicationContext(), "预计油量不能为空",
		// Toast.LENGTH_SHORT).show();
		// return false;
		// }

		if ("".equals(detail_amount.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请输入消费金额",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Log.i("DOVE", resultInfo + "\n" + resultStatus);
					Toast.makeText(OrderInfoActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();

					// 支付成功后跳转到主页
					Intent intent = new Intent(OrderInfoActivity.this,
							MainActivity.class);
					startActivity(intent);

				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(OrderInfoActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(OrderInfoActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Log.i("DOVE", msg + "校验");
				Toast.makeText(OrderInfoActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay() {
		// 订单 (名称、详情、金额)
		String orderInfo = getOrderInfo("Diyal购油卡 ", "Test Data of Diyal Test",
				"0.01");

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(OrderInfoActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(OrderInfoActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + Constant.PARTNER + "\"";
		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + Constant.SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + Constant.NOTIFY_URL + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		// orderInfo += "&return_url=\"https://ms.alipay.com/index.htm\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
		Log.i("DOVE", "顶单：" + orderInfo);
		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {

		if (orderid == null) {
			SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
					Locale.getDefault());
			Date date = new Date();
			String key = format.format(date);

			Random r = new Random();
			key = key + r.nextInt();
			key = key.substring(0, 15);
			Log.i("DOVE", "商户订单号" + key);
			return key;
		}

		return orderid;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		Log.i("DOVE", "订单号签名" + SignUtils.sign(content, Constant.RSA_PRIVATE));
		return SignUtils.sign(content, Constant.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
