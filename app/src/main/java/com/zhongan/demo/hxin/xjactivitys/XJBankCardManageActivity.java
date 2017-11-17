package com.zhongan.demo.hxin.xjactivitys;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.adapters.HXBankCardListAdapter;
import com.zhongan.demo.hxin.bean.HXBankListBean;
import com.zhongan.demo.hxin.bean.HXBankListItemBean;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.hxin.view.DragDelListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 银行卡管理页面
 */
public class XJBankCardManageActivity extends XJBaseActivity implements
		OnClickListener {
	private View mStatusView;// 设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;// 返回按钮
	private TextView mTitleView, mNoDataView;// 标题
	private DragDelListView mListView;
	private ListView mCommonListView;
	// private List<Map<String,String>> bankData;
	private List<HXBankListItemBean> bankData;//银行卡信息
	private HXBankCardListAdapter adapter;
	public static XJBankCardManageActivity instance;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.hxactivity_bank_card_manage_layout);
		instance = this;
		initViews();
		getBankList();
	}

	public Handler getHandler() {
		return mHandler;
	}

	/**
	 * 用Handler来更新UI
	 */
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case Contants.MSG_DO_QUERY_BANK_LIST_INFO_SUCCESS:
				Bundle bundle = msg.getData();
				List<HXBankListItemBean> data = (List<HXBankListItemBean>) bundle
						.getSerializable("bankList");
				if (data != null && !data.isEmpty()) {
					mNoDataView.setVisibility(View.GONE);
					mCommonListView.setVisibility(View.VISIBLE);
					bankData.clear();
					bankData.addAll(data);
					adapter.updateData(bankData);
					adapter.notifyDataSetChanged();
				} else {
					mNoDataView.setVisibility(View.VISIBLE);
					mCommonListView.setVisibility(View.GONE);
				}
				break;
			case Contants.MSG_DO_SET_DEFAULT_BANK_CARD_SUCCESS:
				String code = (String) msg.obj;
				if (code.equals("SUCCESS")) {
					getBankList();
				}
				break;
			case Contants.MSG_DO_QUERY_BANK_LIST_INFO_FAILURE:
			case Contants.MSG_DO_SET_DEFAULT_BANK_CARD_FAILURE:
				String payTypeError = (String) msg.obj;
				CustomDialog.Builder payTypebuilder = new CustomDialog.Builder(
						XJBankCardManageActivity.this);
				// builder.setIcon(R.drawable.ic_launcher);
				// builder.setTitle(R.string.title_alert);
				payTypebuilder.setMessage(payTypeError);
				payTypebuilder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				payTypebuilder.create().show();
				break;
			default:
				break;
			}

		}

	};

	private void initViews() {
		mStatusView = findViewById(R.id.status_bar_view);
		int statusHeight = SysUtil.getStatusBarHeight(this);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView
				.getLayoutParams();
		params.height = statusHeight;
		mStatusView.setLayoutParams(params);
		mBackBtn = (Button) findViewById(R.id.left_btn);
		// mAddBtn=(Button) findViewById(R.id.right_btn);
		// mAddBtn.setVisibility(View.VISIBLE);
		// Drawable rightDrawable =
		// getResources().getDrawable(R.drawable.m_icon_add);
		// rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
		// rightDrawable.getMinimumHeight());
		// mAddBtn.setCompoundDrawables(null, null,rightDrawable,null);
		mTitleView = (TextView) findViewById(R.id.center_title);
		mTitleView.setText(R.string.own_bank_card_text);
		mNoDataView = (TextView) findViewById(R.id.bank_card_no_data);
		mBackBtn.setOnClickListener(this);
		// mAddBtn.setOnClickListener(this);
		mListView = (DragDelListView) findViewById(R.id.bank_card_listview);
		mCommonListView = (ListView) findViewById(R.id.bank_card_listview_common);
		bankData = new ArrayList<HXBankListItemBean>();
		// bankData=new ArrayList<Map<String,String>>();
		// Map<String,String> map1=new HashMap<String, String>();
		// String bankLogo=String.valueOf(R.drawable.m_icon_jianhang_logo);
		// map1.put("bankLogo",bankLogo);//银行logo
		// map1.put("bankName","中国建设银行");//银行名称
		// map1.put("bankCardType", "储蓄卡");//银行卡类型
		// map1.put("isDefaultCard","1");//是否默认卡 是默认卡：1 不是默认卡0
		// map1.put("bankCardColor", "blue");//银行logo 颜色 蓝色：blue 绿色：green 红色：red
		// map1.put("bankCardId", "**** **** **** 8101");//银行卡号
		// bankData.add(map1);
		// Map<String,String> map2=new HashMap<String, String>();
		//
		// map2.put("bankLogo",bankLogo);//银行logo
		// map2.put("bankName","廊坊银行");//银行名称
		// map2.put("bankCardType", "二类卡");//银行卡类型
		// map2.put("isDefaultCard","0");//是否默认卡 是默认卡：1 不是默认卡0
		// map2.put("bankCardColor", "green");//银行logo 颜色 蓝色：blue 绿色：green
		// 红色：red
		// map2.put("bankCardId", "**** **** **** 0704");//银行卡号
		// bankData.add(map2);
		// Map<String,String> map3=new HashMap<String, String>();
		//
		// map3.put("bankLogo",bankLogo);//银行logo
		// map3.put("bankName","中国民生银行");//银行名称
		// map3.put("bankCardType", "信用卡");//银行卡类型
		// map3.put("isDefaultCard","0");//是否默认卡 是默认卡：1 不是默认卡0
		// map3.put("bankCardColor", "green");//银行logo 颜色 蓝色：blue 绿色：green
		// 红色：red
		// map3.put("bankCardId", "**** **** **** 0437");//银行卡号
		// bankData.add(map3);
		// Map<String,String> map4=new HashMap<String, String>();
		//
		// map4.put("bankLogo",bankLogo);//银行logo
		// map4.put("bankName","中国银行");//银行名称
		// map4.put("bankCardType", "储蓄卡");//银行卡类型
		// map4.put("isDefaultCard","0");//是否默认卡 是默认卡：1 不是默认卡0
		// map4.put("bankCardColor", "red");//银行logo 颜色 蓝色：blue 绿色：green 红色：red
		// map4.put("bankCardId", "**** **** **** 9674");//银行卡号
		// bankData.add(map4);

		// Collections.sort(bankData, new Comparator<Map<String,String>>() {
		//
		// @Override
		// public int compare(Map<String, String> leftMap,
		// Map<String, String> rightMap) {
		// //降序排列
		// String leftStr=leftMap.get("isDefaultCard");
		// String rightStr=leftMap.get("isDefaultCard");
		// int left =Integer.valueOf(leftStr);
		// int right =Integer.valueOf(rightStr);
		// int flag=0;
		// if(left<right)
		// {
		// flag=1;
		// }else if(left==right)
		// {
		// flag=0;
		//
		// }else if (left>right) {
		// flag=-1;
		//
		// }
		// return flag;
		// }
		// });
		adapter = new HXBankCardListAdapter(this, bankData);
		// mListView.setAdapter(adapter);
		mCommonListView.setAdapter(adapter);

	}

	// 查询卡列表接口
	private void getBankList() {
		LoggerUtil.debug("卡列表查询: clientToken--->" + sharePrefer.getToken());
		RequestParams params = new RequestParams("utf-8");
		params.addQueryStringParameter("transCode",
				Contants.TRANS_CODE_QUERY_BANK_LIST_INFO);
		params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
		params.addBodyParameter("clientToken", sharePrefer.getToken());
        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
			
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				HXBankListBean bankListData = gson.fromJson(
						data, HXBankListBean.class);// 还款计划数据
				List<HXBankListItemBean> bankList = bankListData.getBankList();
				LoggerUtil.debug("卡列表---->" + bankList);
				Bundle bundle = new Bundle();
				bundle.putSerializable("bankList",
						(Serializable) bankList);
				Message msg = new Message();
				msg.setData(bundle);
				msg.what = Contants.MSG_DO_QUERY_BANK_LIST_INFO_SUCCESS;
				mHandler.sendMessage(msg);

			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(HttpException exception, String msg) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(String returnCode, String msg) {
				// TODO Auto-generated method stub
				
			}
		});
//		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST,
//				Contants.REQUEST_URL, params, new RequestCallBack<String>() {
//
//					@Override
//					public void onFailure(HttpException arg0, String error) {
//						// TODO Auto-generated method stub
//						LoggerUtil.debug("卡列表查询 error-------------->" + error);
//						// Message msg = new Message();
//						// msg.what =
//						// Contants.MSG_DO_QUERY_BANK_LIST_INFO_FAILURE;
//						// msg.obj="网络问题";
//						// mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("卡列表查询：result---->"
//								+ responseInfo.result
//								+ "\nresponseInfo.statusCode ===="
//								+ responseInfo.statusCode);
//						if (responseInfo.statusCode == 200) {
//
//							Gson gson = new Gson();
//							BankListBean bankListData = gson.fromJson(
//									responseInfo.result, BankListBean.class);// 还款计划数据
//
//							String returnCode = bankListData.getReturnCode();// 返回交易吗
//							if ("000000".equals(returnCode)) {
//								List<BankListItemBean> bankList = bankListData
//										.getBankList();
//								LoggerUtil.debug("卡列表---->" + bankList);
//								Bundle bundle = new Bundle();
//								bundle.putSerializable("bankList",
//										(Serializable) bankList);
//								Message msg = new Message();
//								msg.setData(bundle);
//								msg.what = Contants.MSG_DO_QUERY_BANK_LIST_INFO_SUCCESS;
//								mHandler.sendMessage(msg);
//
//							} else {
//								String returnMsg = bankListData.getReturnMsg();// 交易提示
//								LoggerUtil.debug("查询卡列表失败---->" + returnMsg);
//								Message msg = new Message();
//								msg.obj = returnMsg;
//								msg.what = Contants.MSG_DO_QUERY_BANK_LIST_INFO_FAILURE;
//								mHandler.sendMessage(msg);
//
//							}
//
//						}
//
//					}
//				});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			// 返回
			XJBankCardManageActivity.this.finish();
			break;
		case R.id.right_btn:
			// 添加银行卡
			Intent addIntent = new Intent(XJBankCardManageActivity.this,
					XJDealBankCardActivity.class);
			addIntent.putExtra("option", "add");// option:
												// add(添加银行卡)、check(直接进入银行卡验证页面)、checkThree(开启验证三流程)
			startActivity(addIntent);
			break;
		default:
			break;
		}
	}

}
