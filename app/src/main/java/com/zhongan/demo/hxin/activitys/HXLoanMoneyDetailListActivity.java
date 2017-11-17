package com.zhongan.demo.hxin.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.adapters.HXLoanMoneyDetailListAdapter;
import com.zhongan.demo.hxin.bean.HXLoanMoneyDetailListBean;
import com.zhongan.demo.hxin.bean.HXLoanMoneyDetailListItemBean;
import com.zhongan.demo.hxin.bean.HXPayAllMoneyListBean;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.hxin.view.MyListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 借款详情列表页面
 */
public class HXLoanMoneyDetailListActivity extends HXBaseActivity implements
        OnClickListener {
	private View mStatusView;// 设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn, mPayAllBtn;
	private TextView mTitleView;
	private MyListView mListView;
	private List<HXLoanMoneyDetailListItemBean> datas;
	private HXLoanMoneyDetailListAdapter adapter;
	private String loanId = "";// 贷款id
	private Dialog mDialog;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			mDialog.cancel();
			switch (msg.what) {
			case Contants.MSG_LOAN_MONEY_DETAIL_LIST_SUCCESS:
				Bundle bundle = msg.getData();
				List<HXLoanMoneyDetailListItemBean> result = (List<HXLoanMoneyDetailListItemBean>) bundle
						.getSerializable("data");
				if (result != null && !result.isEmpty()) {
					String overDays = bundle.getString("overDays");
					datas.clear();
					datas.addAll(result);
					adapter.setOverdueCount(overDays);
					adapter.updateDatas(datas);
					adapter.notifyDataSetChanged();

					if (overDays.equals("1")) {
						// 有逾期
						mPayAllBtn.setVisibility(View.GONE);
					} else {
						// 没有逾期
						mPayAllBtn.setVisibility(View.VISIBLE);
					}
				}
				break;
			case Contants.MSG_LOAN_MONEY_DETAIL_LIST_FAILURE:
			case Contants.MSG_DO_AHEAD_PAY_MONEY_FAILURE:
				String errorMsg = (String) msg.obj;
				CustomDialog.Builder builder = new CustomDialog.Builder(
						HXLoanMoneyDetailListActivity.this);
				// builder.setIcon(R.drawable.ic_launcher);
				// builder.setTitle(R.string.title_alert);
				builder.setMessage(errorMsg);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				builder.create().show();
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
		setContentView(R.layout.hxactivity_loan_money_detai_list_layout);
		loanId = getIntent().getStringExtra("loanId");
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
		mTitleView.setText(R.string.loan_money_detail_text);
		mBackBtn.setOnClickListener(this);
		mPayAllBtn = (Button) findViewById(R.id.pay_money_all_btn);
		mPayAllBtn.setOnClickListener(this);
		mListView = (MyListView) findViewById(R.id.pay_money_loan_money_detail_listview);
		datas = new ArrayList<HXLoanMoneyDetailListItemBean>();
		// Map<String,String> map1=new HashMap<String, String>();
		// map1.put("title1", "1期");
		// map1.put("title2", "2017-01-26");
		// map1.put("title3", "10000");
		// map1.put("title4", "48");
		// map1.put("title5", "已还清");
		// datas.add(map1);
		// Map<String,String> map2=new HashMap<String, String>();
		// map2.put("title1", "2期");
		// map2.put("title2", "2017-02-26");
		// map2.put("title3", "1667");
		// map2.put("title4", "48");
		// map2.put("title5", "已还清");
		// datas.add(map2);
		// Map<String,String> map3=new HashMap<String, String>();
		// map3.put("title1", "3期");
		// map3.put("title2", "2016-03-26");
		// map3.put("title3", "2000");
		// map3.put("title4", "48");
		// map3.put("title5", "还逾期");
		// datas.add(map3);
		// Map<String,String> map4=new HashMap<String, String>();
		// map4.put("title1", "4期");
		// map4.put("title2", "2016-04-26");
		// map4.put("title3", "1667");
		//
		// map4.put("title4", "48");
		// map4.put("title5", "还逾期");
		// datas.add(map4);
		// Map<String,String> map5=new HashMap<String, String>();
		// map5.put("title1", "5期");
		// map5.put("title2", "2017-05-26");
		// map5.put("title3", "1987");
		// map5.put("title4", "48");
		// map5.put("title5", "还当期");
		//
		// datas.add(map5);
		// Map<String,String> map6=new HashMap<String, String>();
		// map6.put("title1", "6期");
		// map6.put("title2", "2016-06-26");
		// map6.put("title3", "1987");
		// map6.put("title4", "48");
		// map6.put("title5", "待还款");
		// datas.add(map6);
		adapter = new HXLoanMoneyDetailListAdapter(this, datas);
		adapter.setLoanId(loanId);
		mListView.setAdapter(adapter);
		getLoanMoneyDetailData(loanId);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LoggerUtil.debug("onResume----------");
		getLoanMoneyDetailData(loanId);
	}

	// 借款详情列表
	private void getLoanMoneyDetailData(String loanId) {

		LoggerUtil.debug("借款详情列表 loanId-->" + loanId);
		RequestParams params = new RequestParams("utf-8");
		params.addHeader("Content-Type", "application/x-www-form-urlencoded");
		params.addBodyParameter("transCode",
				Contants.TRANS_CODE_GET_LOAN_RECORD_DETAIL_LIST);
		params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
		params.addBodyParameter("clientToken", getSharePrefer().getToken());
		params.addBodyParameter("loanId", loanId);// 贷款Id
        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
			
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				HXLoanMoneyDetailListBean resultData = gson.fromJson(
						data,
						HXLoanMoneyDetailListBean.class);
				String overDaysFlag = resultData
						.getOverdueCount();// 是否逾期标识
				LoggerUtil
						.debug("借款详情列表 overDaysFlag----------->"
								+ overDaysFlag);
				List<HXLoanMoneyDetailListItemBean> resultList = resultData.getRows();
				LoggerUtil.debug("借款详情列表 rowsStr----------->"+ resultData.getRows());
				Message msg = new Message();
				msg.what = Contants.MSG_LOAN_MONEY_DETAIL_LIST_SUCCESS;
				Bundle bundle = new Bundle();
				bundle.putString("overDays", overDaysFlag);
				bundle.putSerializable("data",(Serializable) resultList);
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
			}
			
			@Override
			public void onError(String returnCode, String msg) {
				// TODO Auto-generated method stub
				mDialog.cancel();
			}
		});
//		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST,
//				Contants.REQUEST_URL, params, new RequestCallBack<String>() {
//					@Override
//					public void onStart() {
//						// TODO Auto-generated method stub
//						super.onStart();
//						mDialog.show();
//					}
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						// TODO Auto-generated method stub
//						Message msg = new Message();
//						msg.what = Contants.MSG_LOAN_MONEY_DETAIL_LIST_FAILURE;
//						msg.obj = "网络问题!";
//						mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("借款详情列表result---->"
//								+ responseInfo.result
//								+ "\nresponseInfo.statusCode ===="
//								+ responseInfo.statusCode);
//						if (responseInfo.statusCode == 200) {
//							Gson gson = new Gson();
//							HXLoanMoneyDetailListBean resultData = gson.fromJson(
//									responseInfo.result,
//									HXLoanMoneyDetailListBean.class);
//							String returnCode = resultData.getReturnCode();
//							LoggerUtil.debug("借款详情列表 returnCode----------->"
//									+ returnCode);
//							if ("000000".equals(returnCode)) {
//								String overDaysFlag = resultData
//										.getOverdueCount();// 是否逾期标识
//								LoggerUtil
//										.debug("借款详情列表 overDaysFlag----------->"
//												+ overDaysFlag);
//								List<HXLoanMoneyDetailListItemBean> resultList = resultData
//										.getRows();
//								// int overDaysCounts=0;
//								//
//								// for(int i=0;i<resultList.size();i++)
//								// {
//								// String
//								// payState=resultList.get(i).getStatus();
//								// if(payState.equals("3"))
//								// {
//								// overDaysCounts++;
//								// }
//								//
//								// }
//								LoggerUtil.debug("借款详情列表 rowsStr----------->"
//										+ resultData.getRows());
//								Message msg = new Message();
//								msg.what = Contants.MSG_LOAN_MONEY_DETAIL_LIST_SUCCESS;
//								Bundle bundle = new Bundle();
//								bundle.putString("overDays", overDaysFlag);
//								bundle.putSerializable("data",
//										(Serializable) resultList);
//								msg.setData(bundle);
//								mHandler.handleMessage(msg);
//							} else if ("E10070501".equals(returnCode)) {
//
//								// 列表不存在
//								List<HXLoanMoneyDetailListItemBean> resultList = new ArrayList<HXLoanMoneyDetailListItemBean>();
//								Message msg = new Message();
//								msg.what = Contants.MSG_LOAN_MONEY_DETAIL_LIST_SUCCESS;
//								Bundle bundle = new Bundle();
//								bundle.putSerializable("data",
//										(Serializable) resultList);
//								msg.setData(bundle);
//								mHandler.handleMessage(msg);
//							} else {
//								String errorMsg = resultData.getReturnMsg();
//								Message msg = new Message();
//								msg.what = Contants.MSG_LOAN_MONEY_DETAIL_LIST_FAILURE;
//								msg.obj = errorMsg;
//								mHandler.handleMessage(msg);
//							}
//						}
//					}
//				});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			// 返回
			HXLoanMoneyDetailListActivity.this.finish();
			break;
		case R.id.pay_money_all_btn:
			// 提前全额还款
			String perRepayDate= Util.getCurrentDate();;//还款日期
			aheadPayMoney(loanId,"1",perRepayDate);
			break;
		default:
			break;
		}
	}
	//提前还款试算
	 private void aheadPayMoney(final String loanId, String prepaymentType, String perRepayDate) {
			LoggerUtil.debug("提前还款试算: 贷款id-->" + loanId+"\n提前还款日期--->"+perRepayDate+"\n提前还款类型"+prepaymentType);
			RequestParams params = new RequestParams("utf-8");
			params.addHeader("Content-Type","application/x-www-form-urlencoded");
			params.addBodyParameter("transCode",Contants.TRANS_CODE_AHEAD_PAY_MONEY);
			params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
			params.addBodyParameter("clientToken",getSharePrefer().getToken());
			params.addBodyParameter("prepaymentType",prepaymentType);//提前还款类型  1:全额还款
			params.addBodyParameter("loanId",loanId);//贷款id
			params.addBodyParameter("perRepayDate",perRepayDate);//提前还款日期
			httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
				
				@Override
				public void onSuccess(String data) {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					HXPayAllMoneyListBean resultMap = gson.fromJson(data, HXPayAllMoneyListBean.class);
					String repayPricipal=resultMap.getCuAmt();//本金金额
					String repayInerest=resultMap.getCuInt();//利息金额
//					String overdueInt=resultMap.get("overdueInt");//逾期利息金额
					String totalAmt=resultMap.getCuTotalAmt();//本次应还总额
//					String repayFee=resultMap.get("repayFee");//费用金额
					String penalty=resultMap.getPenalty();//违约金
					
					mDialog.cancel();
					Intent intent = new android.content.Intent(
							HXLoanMoneyDetailListActivity.this,
							HXPayMoneyPayDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("payType", "all");
					bundle.putString("loanId", loanId);
					bundle.putString("repayPricipal", repayPricipal);
					bundle.putString("repayInerest", repayInerest);
					bundle.putString("overdueInt", "0.00");
					bundle.putString("totalAmt", totalAmt);
					bundle.putString("repayFee", "0.00");
					bundle.putString("penalty",penalty);
					intent.putExtra("loanMoneyBundle", bundle);
					startActivity(intent);		
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
				}
				
				@Override
				public void onError(String returnCode, String msg) {
					// TODO Auto-generated method stub
					mDialog.cancel();
				}
			});
//			ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//					new RequestCallBack<String>() {
//	                    @Override
//	                    public void onStart() {
//	                    	// TODO Auto-generated method stub
//	                    	super.onStart();
//	                    	mDialog.show();
//	                    }
//						@Override
//						public void onFailure(HttpException arg0, String arg1) {
//							// TODO Auto-generated method stub
////							Message msg = new Message();
////							msg.what = Contants.MSG_DO_AHEAD_PAY_MONEY_FAILURE;
////							msg.obj="网络问题!";
////							mHandler.handleMessage(msg);
//							mDialog.cancel();
//						}
//
//						@Override
//						public void onSuccess(ResponseInfo<String> responseInfo) {
//							LoggerUtil.debug("提前还款试算result---->" + responseInfo.result
//									+ "\nresponseInfo.statusCode ===="
//									+ responseInfo.statusCode);
//							if (responseInfo.statusCode==200) {	
//								
//								Gson gson = new Gson();
//								HXPayAllMoneyListBean resultMap = gson.fromJson(responseInfo.result, HXPayAllMoneyListBean.class);
//								String returnCode = resultMap.getReturnCode();
//								if ("000000".equals(returnCode)) 
//								{				
//									String repayPricipal=resultMap.getCuAmt();//本金金额
//									String repayInerest=resultMap.getCuInt();//利息金额
////									String overdueInt=resultMap.get("overdueInt");//逾期利息金额
//									String totalAmt=resultMap.getCuTotalAmt();//本次应还总额
////									String repayFee=resultMap.get("repayFee");//费用金额
//									String penalty=resultMap.getPenalty();//违约金
//									
//									mDialog.cancel();
//									Intent intent = new android.content.Intent(
//											HXLoanMoneyDetailListActivity.this,
//											HXPayMoneyPayDetailActivity.class);
//									Bundle bundle = new Bundle();
//									bundle.putString("payType", "all");
//									bundle.putString("loanId", loanId);
//									bundle.putString("repayPricipal", repayPricipal);
//									bundle.putString("repayInerest", repayInerest);
//									bundle.putString("overdueInt", "0.00");
//									bundle.putString("totalAmt", totalAmt);
//									bundle.putString("repayFee", "0.00");
//									bundle.putString("penalty",penalty);
//									intent.putExtra("loanMoneyBundle", bundle);
//									startActivity(intent);		
//									
//								}else
//								{
//									String errorMsg = resultMap.getReturnMsg();// 错误提示
//									Message msg = new Message();
//									msg.what = Contants.MSG_DO_AHEAD_PAY_MONEY_FAILURE;
//									msg.obj=errorMsg;
//									mHandler.handleMessage(msg);
//								}
//							}
//						}
//					});
		}
}
