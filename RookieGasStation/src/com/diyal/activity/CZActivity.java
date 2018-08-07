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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.SignUtils;
import com.diyal.app.GasStationApplication;
import com.diyal.config.Constant;
import com.diyal.data.JsonBean;
import com.diyal.data.JsonBean.Q;
import com.diyal.net.C2SReqCZBean;
import com.diyal.net.C2SReqOrderBean;
import com.diyal.net.NetWorkManger;
import com.diyal.net.S2CReqCZBean;
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

@ContentView(R.layout.acitivty_chongzhi)
public class CZActivity extends Activity {

	@ViewInject(R.id.general_title)
	private TextView general_title;

	@ViewInject(R.id.cz_goodsname)
	private EditText cz_goodsname;

	@ViewInject(R.id.cz_detail)
	private EditText cz_detail;

	@ViewInject(R.id.detail_amount)
	private EditText detail_amount;

	private String orderid;

	private S2CReqCZBean order;

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtils.i(this.getClass().getName());
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		initView();

	}

	private void payLoadData() {
		C2SReqCZBean reqServBean = C2SReqCZBean
				.create(getApplicationContext(), "charge", 1,
						Double.valueOf(detail_amount.getText().toString()));
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

						order = S2CReqCZBean.create(retStr);

						orderid = order.oid;

						pay();

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

		general_title.setText("充    值");

		detail_amount.setText("100");
	}

	// 支付按钮监听
	@OnClick(R.id.mine_pay)
	private void payClick(View v) {
		try {
			if (check()) {
				payLoadData();
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("");
		}

	}

	private boolean check() {

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
					Toast.makeText(CZActivity.this, "支付成功", Toast.LENGTH_SHORT)
							.show();

					// 支付成功后跳转到主页
					Intent intent = new Intent(CZActivity.this,
							MainActivity.class);
					startActivity(intent);

				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(CZActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(CZActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Log.i("DOVE", msg + "校验");
				Toast.makeText(CZActivity.this, "检查结果为：" + msg.obj,
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
				String.valueOf(order.money));

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
				PayTask alipay = new PayTask(CZActivity.this);
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
				PayTask payTask = new PayTask(CZActivity.this);
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
		orderInfo += "&subject=" + "\"" + order.subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + order.body + "\"";

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
