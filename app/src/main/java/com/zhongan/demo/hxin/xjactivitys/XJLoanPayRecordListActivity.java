package com.zhongan.demo.hxin.xjactivitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.adapters.HXPayMoneyPayListAdapter;
import com.zhongan.demo.hxin.adapters.HXPayRecordPayListAdapter;
import com.zhongan.demo.hxin.bean.HXLoanMoneyListBean;
import com.zhongan.demo.hxin.bean.HXLoanMoneyListItemBean;
import com.zhongan.demo.hxin.bean.HXPayMoneyRecordListBean;
import com.zhongan.demo.hxin.bean.HXPayMoneyRecordListItemBean;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.refresh.library.ILoadingLayout;
import com.zhongan.demo.hxin.refresh.library.PullToRefreshBase;
import com.zhongan.demo.hxin.refresh.library.PullToRefreshListView;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 借还记录页面
 */
public class XJLoanPayRecordListActivity extends XJBaseActivity implements
		OnClickListener {
	private View mStatusView;// 设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn;
	private TextView mTitleView,noDataViewTip;
	// private ListView mLoanListView;
	private PullToRefreshListView mRefreshListView;
	private RadioGroup mRecordRg;
	private RadioButton mLoanRecordRb, mPayRecordRb;
	// private List<Map<String, String>> loanListData;// 借款列表
	// private List<Map<String, String>> payListData;// 还款列表
	private HXPayMoneyPayListAdapter loanAdapter;// 借款适配
	private HXPayRecordPayListAdapter payAdapter;// 还款适配
	private boolean isLoanRecord = true;
	private int currentPage = 1;// 当前页
	private int pageSize = 10;// 分页条数
	private Dialog mDialog;
	private List<HXLoanMoneyListItemBean> LoanRecordDatas;// 借款列表
	private List<HXPayMoneyRecordListItemBean> payRecordDatas;// 还款列表
	private LinearLayout mNoDataView;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			mDialog.cancel();
			Bundle bundle = msg.getData();
			switch (msg.what) {
			case Contants.MSG_REFRESH_DATA_SUCCESS:
				// 刷新数据完成
				mRefreshListView.onRefreshComplete();
				if (isLoanRecord) {
					List<HXLoanMoneyListItemBean> result = (List<HXLoanMoneyListItemBean>) bundle
							.getSerializable("data");
					noDataViewTip.setText("无借款记录");
					if (result != null && !result.isEmpty()) {
						mRefreshListView.setVisibility(View.VISIBLE);
						mNoDataView.setVisibility(View.GONE);
						LoanRecordDatas.clear();
						LoanRecordDatas.addAll(result);
						loanAdapter.upateData(result);
						loanAdapter.notifyDataSetChanged();
					}else
					{
						mRefreshListView.setVisibility(View.GONE);
						mNoDataView.setVisibility(View.VISIBLE);
					}
				} else {
					List<HXPayMoneyRecordListItemBean> result = (List<HXPayMoneyRecordListItemBean>) bundle
							.getSerializable("data");
					noDataViewTip.setText("无还款记录");
					if (result != null && !result.isEmpty()) {
						mRefreshListView.setVisibility(View.VISIBLE);
						mNoDataView.setVisibility(View.GONE);
						payRecordDatas.clear();
						payRecordDatas.addAll(result);
						payAdapter.updateData(result);
						payAdapter.notifyDataSetChanged();
					}else
					{
						mRefreshListView.setVisibility(View.GONE);
						mNoDataView.setVisibility(View.VISIBLE);
					}
				}

				break;
			case Contants.MSG_LOADE_DATA_SUCCESS:
				// 加载数据完成
				mRefreshListView.onRefreshComplete();
				if (isLoanRecord) {
					List<HXLoanMoneyListItemBean> result = (List<HXLoanMoneyListItemBean>) bundle
							.getSerializable("data");
					if (result != null && !result.isEmpty()) {
						
						LoanRecordDatas.addAll(result);
						loanAdapter.upateData(LoanRecordDatas);
						loanAdapter.notifyDataSetChanged();
					}
				} else {
					List<HXPayMoneyRecordListItemBean> result = (List<HXPayMoneyRecordListItemBean>) bundle.getSerializable("data");
					if (result != null && !result.isEmpty()) {
						
						payRecordDatas.addAll(result);
						payAdapter.updateData(result);
						payAdapter.notifyDataSetChanged();
					}
				}
				break;
			default:
				break;
			}
		};
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_loan_pay_record_layout);
		mDialog = Util.createLoadingDialog(this, "数据加载中,请稍等...");
		initViews();
	}

	private void initViews() {
		mStatusView = findViewById(R.id.status_bar_view);
		int statusHeight = SysUtil.getStatusBarHeight(this);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView
				.getLayoutParams();
		params.height = statusHeight;
		mStatusView.setLayoutParams(params);
		mBackBtn = (Button) findViewById(R.id.left_btn);
		mTitleView = (TextView) findViewById(R.id.center_title);
		mTitleView.setText(R.string.loan_pay_record_text);
		noDataViewTip=(TextView) findViewById(R.id.loan_record_no_data);
		mBackBtn.setOnClickListener(this);
		mRecordRg = (RadioGroup) findViewById(R.id.loan_pay_record_rg);
		mLoanRecordRb = (RadioButton) findViewById(R.id.loan_record_btn);// 借款记录
		mPayRecordRb = (RadioButton) findViewById(R.id.pay_record_btn);// 还款记录
		mNoDataView=(LinearLayout) findViewById(R.id.loan_record_no_data_ll);
		// mLoanListView=(ListView) findViewById(R.id.loan_pay_listview);
		mLoanRecordRb.setOnClickListener(this);
		mPayRecordRb.setOnClickListener(this);
		mRefreshListView = (PullToRefreshListView) findViewById(R.id.loan_pay_listview);
		LoanRecordDatas = new ArrayList<HXLoanMoneyListItemBean>();
		payRecordDatas = new ArrayList<HXPayMoneyRecordListItemBean>();
		
		mNoDataView.setOnClickListener(this);
		payAdapter=new HXPayRecordPayListAdapter(this, payRecordDatas);
		isLoanRecord = true;
		loadData(Contants.MSG_REFRESH_DATA_SUCCESS);
		mRecordRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkId) {
				// TODO Auto-generated method stub
				switch (checkId) {
				case R.id.loan_record_btn:
					// 借款记录
					isLoanRecord = true;
					currentPage = 1;
					mRefreshListView.setAdapter(loanAdapter);
					loadData(Contants.MSG_REFRESH_DATA_SUCCESS);
					break;
				case R.id.pay_record_btn:
					// 还款记录
					isLoanRecord = false;
					currentPage = 1;
					mRefreshListView.setAdapter(payAdapter);
					loadData(Contants.MSG_REFRESH_DATA_SUCCESS);
					// payAdapter=new
					// PayRecordPayListAdapter(XJLoanPayRecordListActivity.this,
					// payListData);
				
					break;

				default:
					break;
				}
			}
		});
		loanAdapter = new HXPayMoneyPayListAdapter(this, LoanRecordDatas);
		mRefreshListView.setAdapter(loanAdapter);
		loadData(Contants.MSG_REFRESH_DATA_SUCCESS);
		// 设置可上拉刷新和下拉刷新
		mRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

		// 设置刷新时显示的文本
		ILoadingLayout refreshLayout = mRefreshListView.getLoadingLayoutProxy(
				true, false);
		refreshLayout.setPullLabel("下拉刷新...");
		refreshLayout.setRefreshingLabel("正在刷新...");
		refreshLayout.setReleaseLabel("放开以刷新");

		ILoadingLayout loadLayout = mRefreshListView.getLoadingLayoutProxy(
				false, true);
		loadLayout.setPullLabel("上拉加载...");
		loadLayout.setRefreshingLabel("正在加载...");
		loadLayout.setReleaseLabel("放开以加载");
		mRefreshListView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新
						currentPage = 1;
						loadData(Contants.MSG_REFRESH_DATA_SUCCESS);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 上拉加载
						currentPage++;
						loadData(Contants.MSG_LOADE_DATA_SUCCESS);
					}
				});
		mRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (isLoanRecord) {
					if (LoanRecordDatas != null && LoanRecordDatas.size() != 0) {
						HXLoanMoneyListItemBean item = LoanRecordDatas
								.get(position - 1);
						Intent intent = new Intent(
								XJLoanPayRecordListActivity.this,
								XJLoanMoneyDetailActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("loanMeneyListItem", item);
						intent.putExtra("loanData", bundle);
						startActivity(intent);
					}
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		currentPage = 1;
		LoggerUtil.debug("onResume--------------------->currentPage:"+ currentPage);
		loadData(Contants.MSG_REFRESH_DATA_SUCCESS);
	}

	private void loadData(final int what) {

		int start = pageSize * (currentPage - 1);
		if (isLoanRecord) {
			LoggerUtil.debug("借款列表 currentPage-->" + currentPage
					+ "\nstart------->" + start);
			RequestParams params = new RequestParams("utf-8");
			params.addHeader("Content-Type",
					"application/x-www-form-urlencoded");
			params.addBodyParameter("transCode",
					Contants.TRANS_CODE_GET_LOAN_RECORD_LIST);
			params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
			params.addBodyParameter("clientToken", sharePrefer.getToken());
			params.addBodyParameter("legalPerNumber", Contants.LEGAL_PERNUMBER);// 法人编号
			params.addBodyParameter("indexNo", Integer.toString(start));
			params.addBodyParameter("pageSize", Integer.toString(pageSize));
			httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
				
				@Override
				public void onSuccess(String data) {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					HXLoanMoneyListBean resultData = gson.fromJson(
							data,
							HXLoanMoneyListBean.class);
					List<HXLoanMoneyListItemBean> resultList = resultData
							.getRows();
					LoggerUtil.debug("借款列表 rowsStr----------->"
							+ resultData.getRows());
					Message msg = new Message();
					msg.what = what;
					Bundle bundle = new Bundle();
					bundle.putSerializable("data",
							(Serializable) resultList);
					msg.setData(bundle);
					mHandler.handleMessage(msg);
				}
				
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					mDialog.show();
				}
				
				@Override
				public void onFailure(HttpException exception, String msg) {
					// TODO Auto-generated method stub
					mDialog.cancel();
					mRefreshListView.onRefreshComplete();
				}
				
				@Override
				public void onError(String returnCode, String msg) {
					// TODO Auto-generated method stub
					mDialog.cancel();
					mRefreshListView.onRefreshComplete();
				}
			});
//			ApplicationExtension.instance.dataHttp.send(HttpMethod.POST,
//					Contants.REQUEST_URL, params,
//					new RequestCallBack<String>() {
//						@Override
//						public void onStart() {
//							// TODO Auto-generated method stub
//							super.onStart();
//							mDialog.show();
//						}
//
//						@Override
//						public void onFailure(HttpException arg0, String arg1) {
//							// TODO Auto-generated method stub
//							LoggerUtil.debug("借款列表erro---->" + arg1);
//							mDialog.cancel();
//						}
//
//						@Override
//						public void onSuccess(ResponseInfo<String> responseInfo) {
//							LoggerUtil.debug("借款列表result---->"
//									+ responseInfo.result
//									+ "\nresponseInfo.statusCode ===="
//									+ responseInfo.statusCode);
//							if (responseInfo.statusCode == 200) {
//								Gson gson = new Gson();
//								LoanMoneyListBean resultData = gson.fromJson(
//										responseInfo.result,
//										LoanMoneyListBean.class);
//								String returnCode = resultData.getReturnCode();
//								LoggerUtil.debug("借款列表 returnCode----------->"
//										+ returnCode);
//								if ("000000".equals(returnCode)) {
//									List<LoanMoneyListItemBean> resultList = resultData
//											.getRows();
//									LoggerUtil.debug("借款列表 rowsStr----------->"
//											+ resultData.getRows());
//									Message msg = new Message();
//									msg.what = what;
//									Bundle bundle = new Bundle();
//									bundle.putSerializable("data",
//											(Serializable) resultList);
//									msg.setData(bundle);
//									mHandler.handleMessage(msg);
//								} else if ("E10060201".equals(returnCode)) {
//									// 列表不存在
//									List<LoanMoneyListItemBean> resultList = new ArrayList<LoanMoneyListItemBean>();
//									Message msg = new Message();
//									msg.what = what;
//									Bundle bundle = new Bundle();
//									bundle.putSerializable("data",
//											(Serializable) resultList);
//									msg.setData(bundle);
//									mHandler.handleMessage(msg);
//								} else {
//									mDialog.cancel();
//								}
//							}
//						}
//					});
		} else {
			LoggerUtil.debug("还款列表 currentPage-->" + currentPage
					+ "\nstart------->" + start);
			RequestParams params = new RequestParams("utf-8");
			params.addHeader("Content-Type",
					"application/x-www-form-urlencoded");
			params.addBodyParameter("transCode",
					Contants.TRANS_CODE_QUERY_PAY_RECORD_LIST);
			params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
			params.addBodyParameter("clientToken", sharePrefer.getToken());
			params.addBodyParameter("legalPerNumber", Contants.LEGAL_PERNUMBER);// 法人编号
			params.addBodyParameter("loanId", "1111111");
			params.addBodyParameter("indexNo", Integer.toString(start));
			params.addBodyParameter("pageSize", Integer.toString(pageSize));
            httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
				
				@Override
				public void onSuccess(String data) {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					HXPayMoneyRecordListBean resultData = gson
							.fromJson(data,
									HXPayMoneyRecordListBean.class);
					List<HXPayMoneyRecordListItemBean> resultList = resultData
							.getRows();
					LoggerUtil.debug("还款列表 rowsStr----------->"
							+ resultData.getRows());
					Message msg = new Message();
					msg.what = what;
					Bundle bundle = new Bundle();
					bundle.putSerializable("data",
							(Serializable) resultList);
					msg.setData(bundle);
					mHandler.handleMessage(msg);
				}
				
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					mDialog.show();
				}
				
				@Override
				public void onFailure(HttpException exception, String msg) {
					// TODO Auto-generated method stub
					mDialog.cancel();
					mRefreshListView.onRefreshComplete();
				}
				
				@Override
				public void onError(String returnCode, String msg) {
					// TODO Auto-generated method stub
					mDialog.cancel();
					mRefreshListView.onRefreshComplete();
				}
			});
//			ApplicationExtension.instance.dataHttp.send(HttpMethod.POST,
//					Contants.REQUEST_URL, params,
//					new RequestCallBack<String>() {
//						@Override
//						public void onStart() {
//							// TODO Auto-generated method stub
//							super.onStart();
//							mDialog.show();
//						}
//
//						@Override
//						public void onFailure(HttpException arg0, String arg1) {
//							// TODO Auto-generated method stub
//							LoggerUtil.debug("还款列表erro---->" + arg1);
//							mDialog.cancel();
//						}
//
//						@Override
//						public void onSuccess(ResponseInfo<String> responseInfo) {
//							LoggerUtil.debug("还款列表result---->"
//									+ responseInfo.result
//									+ "\nresponseInfo.statusCode ===="
//									+ responseInfo.statusCode);
//							if (responseInfo.statusCode == 200) {
//								Gson gson = new Gson();
//								PayMoneyRecordListBean resultData = gson
//										.fromJson(responseInfo.result,
//												PayMoneyRecordListBean.class);
//								String returnCode = resultData.getReturnCode();
//								LoggerUtil.debug("还款列表 returnCode----------->"
//										+ returnCode);
//								if ("000000".equals(returnCode)) {
//									List<PayMoneyRecordListItemBean> resultList = resultData
//											.getRows();
//									LoggerUtil.debug("还款列表 rowsStr----------->"
//											+ resultData.getRows());
//									Message msg = new Message();
//									msg.what = what;
//									Bundle bundle = new Bundle();
//									bundle.putSerializable("data",
//											(Serializable) resultList);
//									msg.setData(bundle);
//									mHandler.handleMessage(msg);
//								} else if ("E10070405".equals(returnCode)) {
//									// 列表不存在
//									List<PayMoneyRecordListItemBean> resultList = new ArrayList<PayMoneyRecordListItemBean>();
//									Message msg = new Message();
//									msg.what = what;
//									Bundle bundle = new Bundle();
//									bundle.putSerializable("data",
//											(Serializable) resultList);
//									msg.setData(bundle);
//									mHandler.handleMessage(msg);
//								} else {
//									mDialog.cancel();
//								}
//							}
//						}
//					});
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			// 返回
			XJLoanPayRecordListActivity.this.finish();
			break;
		case R.id.loan_record_btn:
			// 借款记录
			mRecordRg.check(R.id.loan_record_btn);
			break;
		case R.id.pay_record_btn:
			// 还款记录
			mRecordRg.check(R.id.pay_record_btn);
			break;
		case R.id.loan_record_no_data_ll:
			
			loadData(Contants.MSG_REFRESH_DATA_SUCCESS);
			break;
		default:
			break;
		}
	}

}
