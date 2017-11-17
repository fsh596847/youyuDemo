package com.zhongan.demo.hxin.xjactivitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.adapters.HXBasicLinesListAdapter;
import com.zhongan.demo.hxin.bean.HXLoanProductListBean;
import com.zhongan.demo.hxin.bean.HXLoanProductPayWayListItemBean;
import com.zhongan.demo.hxin.bean.HXLoanProductRateListItemBean;
import com.zhongan.demo.hxin.bean.HXLoanRecordDetailBean;
import com.zhongan.demo.hxin.bean.HXPayTypeBean;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.hxin.view.OptionsPickerView;
import com.zhongan.demo.hxin.view.TSeekBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我要借款
 */
public class XJLoanMoneyFirstActivity extends XJBaseActivity implements OnClickListener
{
	private TSeekBar mSeekBar;
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn,mSubmitBtn;
	private TextView mTitleView,mLoanNumTv,mLoanMonthTv,mPayTypeTv,mLoanTip,mCanLoanNumTv;
	private FrameLayout mLoanMonthFl,mPayTypeFl;
	private String loanAmount="1000";//贷款金额
	private String loanTerm="";//贷款期限
	private String loanDate="";//首次还款日
	private String repayMode="";//还款方式
	private String repayModeName="";//还款方式名称
	private String loanRate="";//贷款利率
	private String prodNum="";//产品编号
	private String repayWayId="";//还款方式Id
	private List<HXLoanProductPayWayListItemBean> repayWayData; // 产品还款方式
	private String productId="85f1b02a6b4e4b7287e949894e312d77";//产品Id
	private OptionsPickerView<String> mOptionView;
//	private ArrayList<String> months;
	private List<HXLoanProductRateListItemBean> productRate;
    private Bundle loanAmountInfo;
    private String canUsedTotalNum;//可借款总额度
    private int minLoanAmt=2000;//产品最小允许贷款金额
    private int maxLoanAmt=3000;//产品最大允许贷款金额
    private int canusedNum;
    private boolean isFrist=true;//第一次进入
    private Dialog mDialog;
    private PopupWindow mLoanTypePopWindow;
	/**
	 * 用Handler来更新UI
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case Contants.MSG_GET_LOAN_MONEY_NUM_SUCCESS:
				loanAmount=(String) msg.obj;
			    mLoanNumTv.setText(loanAmount+"(元)");
			    if(loanAmount==null||loanAmount.equals("")||loanAmount.equals("0"))
			    {
			    	mSubmitBtn.setClickable(false);
			    	mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
			    }else if(loanAmount!=null&&!loanAmount.equals("")&&!loanAmount.equals("0"))
			    {
			    	mSubmitBtn.setClickable(true);
			    	mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
			    }
				break;
			case Contants.MSG_DO_QUERY_PRODUCT_INFO_SUCCESS:
				Bundle productBundle=msg.getData();
				HXLoanProductListBean productData=(HXLoanProductListBean) productBundle.getSerializable("productData");
				productRate=productData.getRows();
				loanTerm=productRate.get(0).getTerm();//默认借款期限
				loanRate=productRate.get(0).getRate();//默认借款利率
				prodNum=productData.getProdNum();//产品编号
				productId=productData.getProdId();//产品id
				repayWayData=productData.getRepayWay();//还款方式相关信息
				repayMode=repayWayData.get(0).getRepayMode();//还款方式
//				loanDate=repayWayData.get(0).getRepayDay();
				repayWayId=repayWayData.get(0).getRepayWayId();//还款方式id
				mLoanMonthTv.setText(loanTerm+"个月");
				repayModeName=repayWayData.get(0).getRepayWayName();//还款方式中文名称
				mPayTypeTv.setText(repayModeName); 
				String minLoanAmtStr=productData.getMinLoanAmt();//产品最小借款金额
				String maxLoanAmtStr=productData.getMaxLoanAmt();//产品最大借款金额
				double canuseDouble=Double.valueOf(canUsedTotalNum);
			    double minLoanAmtDouble=Double.valueOf(minLoanAmtStr);
			    double maxLoanAmtDouble=Double.valueOf(maxLoanAmtStr);
				canusedNum= (int) canuseDouble;
				minLoanAmt=(int)minLoanAmtDouble;
				maxLoanAmt=(int)maxLoanAmtDouble;
				if(canusedNum<minLoanAmt)
				{
					mDialog.cancel();
					String errorMsg="您的额度小于最小可借金额,不能申请借款!";
					CustomDialog.Builder builder = new CustomDialog.Builder(
							XJLoanMoneyFirstActivity.this);
					// builder.setIcon(R.drawable.ic_launcher);
					// builder.setTitle(R.string.title_alert);
					builder.setMessage(errorMsg);
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									// 设置你的操作事项
//									ActivityStackManagerUtils.getInstance().finishAllActivity();
									Intent intent = new Intent(XJLoanMoneyFirstActivity.this,XJMainActivity.class);
									//intent.putExtra("flag", value);
									startActivity(intent);
								}
							});
					builder.create().show();
					return;
				}
				if(canusedNum>maxLoanAmt)
				{
					//可借额度大于产品配置最大额度
					LoggerUtil.debug("可借总额度---222222------->"+maxLoanAmt);
					mCanLoanNumTv.setText((maxLoanAmt/1000)+"K");
					mSeekBar.setMax(maxLoanAmt/1000);
				}else if(canusedNum<=maxLoanAmt)
				{
					//可借额度小于等于产品配置最大额度
					LoggerUtil.debug("可借总额度---222222------->"+canusedNum);
					mCanLoanNumTv.setText((canusedNum/1000)+"K");
					mSeekBar.setMax(canusedNum/1000);
				}
				mSeekBar.setProgress(minLoanAmt/1000);
				doPayType(loanAmount,loanTerm,loanRate,repayMode,"1",repayWayId,productId);
				break;
			case Contants.MSG_GET_PAYTYPE_DATA_SUCCESS:
				 mDialog.cancel();
				 Bundle bundle=msg.getData();
				if(isFrist)
				{					  
				   String tipText="首次还款日"+loanDate+"(首笔贷款还款日为贷款默认还款日)";
				   mLoanTip.setText(tipText);
				   isFrist=false;
				}else{
				
				   String tipText="首次还款日"+loanDate+"(首笔贷款还款日为贷款默认还款日)";
				   mLoanTip.setText(tipText);
				   Intent intent=new Intent(XJLoanMoneyFirstActivity.this, XJLoanMoneySecondActivity.class);
				   intent.putExtra("payTypeInfo", bundle);
				   intent.putExtra("repayModeName", repayModeName);
				   startActivity(intent);
				}
				break;
			case Contants.MSG_GET_LOAN_LOAN_RECORD_INFO_SUCCESS:
				 Bundle loanDetail=msg.getData();
				 Intent submitIntent=new Intent(XJLoanMoneyFirstActivity.this, XJLoanMoneyThirdActivity.class);
				loanDetail.putString("applyAmount", loanAmount);//贷款金额
				loanDetail.putString("loanTerm", loanTerm);//贷款期限
				loanDetail.putString("productId", productId);//产品id
				loanDetail.putString("repayMode", repayMode);//还款方式
				loanDetail.putString("repayWayId", repayWayId);//还款方式Id
				loanDetail.putString("repayModeName", repayModeName);//还款方式名称
				submitIntent.putExtra("loanDetail",loanDetail);	
				startActivity(submitIntent);
				mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
	        	mSubmitBtn.setClickable(true);
	        	mDialog.cancel();
				break;
			case Contants.MSG_GET_LOAN_LOAN_RECORD_INFO_FAILURE:
				mDialog.cancel();
				String loanRecordError=(String) msg.obj;
				CustomDialog.Builder builder = new CustomDialog.Builder(
						XJLoanMoneyFirstActivity.this);
				// builder.setIcon(R.drawable.ic_launcher);
				// builder.setTitle(R.string.title_alert);
				builder.setMessage(loanRecordError);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				builder.create().show();
				mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
	        	mSubmitBtn.setClickable(true);
				break;
			case Contants.MSG_GET_PAYTYPE_DATA_FAILURE:
			case Contants.MSG_DO_QUERY_PRODUCT_INFO_FAILURE:
				mDialog.cancel();
				String payTypeError=(String) msg.obj;
				CustomDialog.Builder payTypebuilder = new CustomDialog.Builder(
						XJLoanMoneyFirstActivity.this);
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

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_loan_money_first_layout);
		canUsedTotalNum=sharePrefer.getCanUsedSum();
		mDialog= Util.createLoadingDialog(this,"数据加载中,请稍等...");
	    LoggerUtil.debug("可借总额度---------->"+canUsedTotalNum);
		initViews();
		doQueryProduct(productId);
	}
    private void initViews()
    {
    	 mStatusView=findViewById(R.id.status_bar_view);
		 int statusHeight= SysUtil.getStatusBarHeight(this);
		 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams(); 
		 params.height=statusHeight;
		 mStatusView.setLayoutParams(params);
		 mBackBtn=(Button) findViewById(R.id.left_btn);
		 mTitleView=(TextView) findViewById(R.id.center_title);
		 mTitleView.setText(R.string.loan_money_title_text);
		 mBackBtn.setOnClickListener(this);
		 mLoanNumTv=(TextView) findViewById(R.id.loan_money_loan_num_tv);
		 mLoanNumTv.setText(loanAmount+"(元)");
		 mLoanMonthFl=(FrameLayout) findViewById(R.id.loan_money_loan_month_fl);
		 mLoanMonthTv=(TextView) findViewById(R.id.loan_money_loan_month_tv);
		 mCanLoanNumTv=(TextView) findViewById(R.id.loan_money_canusednum_tv);
		
		 mLoanMonthFl.setOnClickListener(this);
		 mPayTypeFl=(FrameLayout) findViewById(R.id.loan_money_pay_type_fl);
		 mPayTypeTv=(TextView) findViewById(R.id.loan_money_pay_type_tv);
		 mPayTypeFl.setOnClickListener(this);
		 mLoanTip=(TextView) findViewById(R.id.loan_money_tip_tv);
		 mSubmitBtn=(Button) findViewById(R.id.loan_money_submit_btn);
		 mSubmitBtn.setOnClickListener(this);
		//文本选择器
		 mOptionView=new OptionsPickerView<String>(this);	 
    	 mSeekBar= (TSeekBar) findViewById(R.id.loan_money_seek_bar);
    	
    	 mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				// TODO Auto-generated method stub
				String progressChoose=progress*1000+"";
				Message msg=new Message();
				msg.what=Contants.MSG_GET_LOAN_MONEY_NUM_SUCCESS;
				msg.obj=progressChoose;
				mHandler.sendMessage(msg);
				
			}
		});
    }
  //展示文本选择控件
  	private void initChooseOptionPopWindow(final String title,final TextView parentView,View view,final List<HXLoanProductRateListItemBean> datas)
  	{	
  		Util.hideKeyBoard(this,view);
  		mOptionView.setTitle(title);
//        if(loanTerm!=null&&!loanTerm.equals(""))
//         {
//        	mOptionView.setSelectOptions(Integer.parseInt(loanTerm.trim())-1);
//         }
         final ArrayList<String> items=new ArrayList<String>();
         for (int i = 0; i < datas.size(); i++) 
         {
        	 String loanTerm=datas.get(i).getTerm();
        	 items.add(loanTerm);	
		 }
          mOptionView.setPicker(items);
          mOptionView.setCyclic(false);
          mOptionView.setCancelable(false);
  		// 时间选择后回调
  		  mOptionView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {


  			@Override
  			public void onOptionsSelect(int options1, int option2, int options3) {
  				// TODO Auto-generated method stub
  				if(title.equals("借款期限"))
  				{
  					loanTerm=items.get(options1);
  					loanRate=datas.get(options1).getRate();
  					LoggerUtil.debug("借款期限"+loanTerm);
  					LoggerUtil.debug("借款利率"+loanRate);
  				}
  				parentView.setText(loanTerm+"个月");
  				
  			}
  		});
  		
  		mOptionView.show();
      }	
   //产品查询接口
  	private void doQueryProduct(final String prodId)
  	{
  		 LoggerUtil.debug("产品查询: clientToken--->"+sharePrefer.getToken());
  		 
  		 RequestParams params = new RequestParams("utf-8");
  	     params.addQueryStringParameter("transCode",Contants.TRANS_CODE_QUERY_PRODUCT_INFO);
  		 params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
  		 params.addBodyParameter("clientToken",sharePrefer.getToken());
  		 params.addQueryStringParameter("prodId",prodId);//产品id
  		 httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
			
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				HXLoanProductListBean productData= gson.fromJson(data, HXLoanProductListBean.class);//还款计划数据
				LoggerUtil.debug("产品查询:productData--------->"+productData);
				Bundle bundle=new Bundle();
				bundle.putSerializable("productData", productData);
				Message msg=new Message();
				msg.setData(bundle);
				msg.what=Contants.MSG_DO_QUERY_PRODUCT_INFO_SUCCESS;
				mHandler.sendMessage(msg);
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
//  		 ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//  		 new RequestCallBack<String>() {
//                        @Override
//                        public void onStart() {
//                        // TODO Auto-generated method stub
//                        super.onStart();
//                          mDialog.show();
//                        }
//  						@Override 
//  						public void onFailure(HttpException arg0,
//  								String error) {
//  							// TODO Auto-generated method stub
//  							LoggerUtil.debug("产品查询 error-------------->" + error);
//  							Message msg = new Message();
//  							msg.what = Contants.MSG_DO_QUERY_PRODUCT_INFO_FAILURE;
//  							msg.obj="网络问题";
//  							mHandler.handleMessage(msg);
//  						}
//
//  						@Override
//  						public void onSuccess(
//  								ResponseInfo<String> responseInfo) {
//  							LoggerUtil.debug( "产品查询result---->"
//  									+ responseInfo.result
//  									+ "\nresponseInfo.statusCode ===="
//  									+ responseInfo.statusCode);
//  							if (responseInfo.statusCode == 200) {
////  							
//  								Gson gson = new Gson();
//  								LoanProductListBean productData= gson.fromJson(responseInfo.result, LoanProductListBean.class);//还款计划数据
//                               
//								String returnCode = productData.getReturnCode();//返回交易吗
//  								if ("000000".equals(returnCode)) 
//  								{
//  									
//  									LoggerUtil.debug("产品查询成功---->"+productData);
//  									Bundle bundle=new Bundle();
//  									bundle.putSerializable("productData", productData);
//  									Message msg=new Message();
//  									msg.setData(bundle);
//  									msg.what=Contants.MSG_DO_QUERY_PRODUCT_INFO_SUCCESS;
//  									mHandler.sendMessage(msg);
//  									
//  								} else 
//  								{
//  									String returnMsg = productData.getReturnMsg();// 交易提示
//  									LoggerUtil.debug("还款方式失败---->"+returnMsg);
//  									Message msg=new Message();
//  									msg.obj=returnMsg;
//  									msg.what=Contants.MSG_DO_QUERY_PRODUCT_INFO_FAILURE;
//  									mHandler.sendMessage(msg);
//  									
//  								}
//
//  							}
//
//  						}
//  					});
  	}	
  //还款方式接口
  	private void doPayType(final String loanAmt,final String loanTerm,String loanRate,String repayMode,String repayPrincipalMonthes,String repayWayId,String productId)
  	{
  		 LoggerUtil.debug("还款方式: clientToken--->"+sharePrefer.getToken());
  		 
  		 RequestParams params = new RequestParams("utf-8");
  	     params.addQueryStringParameter("transCode",Contants.TRANS_CODE_DO_PAY_TYPE);
  		 params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
  		 params.addBodyParameter("clientToken",sharePrefer.getToken());
  		 params.addQueryStringParameter("loanAmt",loanAmt);//贷款金额
  		 params.addQueryStringParameter("loanTerm",loanTerm);//贷款期限
  		 params.addQueryStringParameter("loanRate",loanRate);//贷款利率
  		 params.addQueryStringParameter("repayMode",repayMode);//还款方式
  		 params.addQueryStringParameter("repayPrincipalMonthes",repayPrincipalMonthes);//还款周期月数 1月
  		 params.addQueryStringParameter("repayWayId",repayWayId);//还款方式ID
  		 params.addQueryStringParameter("produtId", productId);//产品id
  		 httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
			
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				Type type = new TypeToken<Map<String, String>>() {
					}.getType();
					Gson gson = new Gson();
                HXPayTypeBean payTypeData= gson.fromJson(data, HXPayTypeBean.class);//还款计划数据
                payTypeData.setTotalLoanAmt(loanAmt);//贷款总额
                payTypeData.setTotalMonths(loanTerm);//总期数
                loanDate=payTypeData.getRepayPlanList().get(0).getCurrentEndDate();
				LoggerUtil.debug("还款方式成功---->"+payTypeData);
				Bundle bundle=new Bundle();
				bundle.putSerializable("payTypeData", payTypeData);
				Message msg=new Message();
				msg.setData(bundle);
				msg.what=Contants.MSG_GET_PAYTYPE_DATA_SUCCESS;
				mHandler.sendMessage(msg);
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
//  		 ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//  		 new RequestCallBack<String>() {
//                        @Override
//                        public void onStart() {
//                        // TODO Auto-generated method stub
//                        super.onStart();
//                        mDialog.show();
//                        }
//  						@Override
//  						public void onFailure(HttpException arg0,
//  								String error) {
//  							// TODO Auto-generated method stub
//  							LoggerUtil.debug("还款方式 error-------------->" + error);
//  							Message msg = new Message();
//  							msg.what = Contants.MSG_GET_PAYTYPE_DATA_FAILURE;
//  							msg.obj="网络问题";
//  							mHandler.handleMessage(msg);
//  						}
//
//  						@Override
//  						public void onSuccess(
//  								ResponseInfo<String> responseInfo) {
//  							LoggerUtil.debug( "还款方式：result---->"
//  									+ responseInfo.result
//  									+ "\nresponseInfo.statusCode ===="
//  									+ responseInfo.statusCode);
//  							if (responseInfo.statusCode == 200) {
////  								Type type = new TypeToken<Map<String, String>>() {
////  								}.getType();
//  								Gson gson = new Gson();
//                                PayTypeBean payTypeData= gson.fromJson(responseInfo.result, PayTypeBean.class);//还款计划数据
//                                payTypeData.setTotalLoanAmt(loanAmt);//贷款总额
//                                payTypeData.setTotalMonths(loanTerm);//总期数
//								String returnCode = payTypeData.getReturnCode();//返回交易吗
//  								if ("000000".equals(returnCode)) 
//  								{
//  									
//  									loanDate=payTypeData.getRepayPlanList().get(0).getCurrentEndDate();
//  									LoggerUtil.debug("还款方式成功---->"+payTypeData);
//  									Bundle bundle=new Bundle();
//  									bundle.putSerializable("payTypeData", payTypeData);
//  									Message msg=new Message();
//  									msg.setData(bundle);
//  									msg.what=Contants.MSG_GET_PAYTYPE_DATA_SUCCESS;
//  									mHandler.sendMessage(msg);
//  									
//  								} else 
//  								{
//  									String returnMsg = payTypeData.getReturnMsg();// 交易提示
//  									LoggerUtil.debug("还款方式失败---->"+returnMsg);
//  									Message msg=new Message();
//  									msg.obj=returnMsg;
//  									msg.what=Contants.MSG_GET_PAYTYPE_DATA_FAILURE;
//  									mHandler.sendMessage(msg);
//  									
//  								}
//
//  							}
//
//  						}
//  					});
  	}	
  	//24.M100300 生成个人借款借据接口
  	private void getLoanDetailInfo(final String loanAmt,final String loanTerm,String repayMode,String prodNum,String repayWayId)
  	{
  		 LoggerUtil.debug("个人借款借据: clientToken--->"+sharePrefer.getToken());
  		 mSubmitBtn.setClickable(false);
		 mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
  		 RequestParams params = new RequestParams("utf-8");
  	     params.addQueryStringParameter("transCode",Contants.TRANS_CODE_LOAN_RECORD_DETAIL);
  		 params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
  		 params.addBodyParameter("clientToken",sharePrefer.getToken());
  		 params.addQueryStringParameter("applyAmt",loanAmt);//贷款金额
  		 params.addQueryStringParameter("period",loanTerm);//贷款期限   
     	 params.addQueryStringParameter("prodNum",prodNum);//产品编号
  		 params.addQueryStringParameter("repayMode",repayMode);//还款方式
  		 params.addQueryStringParameter("repayWayId",repayWayId);//还款方式ID
  		 httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {
			
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				Type type = new TypeToken<Map<String, String>>() {
					}.getType();
					Gson gson = new Gson();
				HXLoanRecordDetailBean loanDetail= gson.fromJson(data, HXLoanRecordDetailBean.class);//还款计划数据
					LoggerUtil.debug("个人借款借据---->"+loanDetail);
					Bundle bundle=new Bundle();
					bundle.putSerializable("loanDetailInfo", loanDetail);
					Message msg=new Message();
					msg.setData(bundle);
					msg.what=Contants.MSG_GET_LOAN_LOAN_RECORD_INFO_SUCCESS;
					mHandler.sendMessage(msg);
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
				  mSubmitBtn.setClickable(true);
			    	mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
			}
			
			@Override
			public void onError(String returnCode, String msg) {
				// TODO Auto-generated method stub
				
				mDialog.cancel();
				mSubmitBtn.setClickable(true);
		    	mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
			}
		});
//  		 ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//  		 new RequestCallBack<String>() {
//                       @Override
//                    public void onStart() {
//                    // TODO Auto-generated method stub
//                    super.onStart();
//                    mDialog.show();
//                    }
//  						@Override
//  						public void onFailure(HttpException arg0,
//  								String error) {
//  							// TODO Auto-generated method stub
//  							LoggerUtil.debug("个人借款借据 error-------------->" + error);
//  							Message msg = new Message();
//  							msg.what = Contants.MSG_GET_LOAN_LOAN_RECORD_INFO_FAILURE;
//  							msg.obj="网络问题!";
//  							mHandler.handleMessage(msg);
//  						}
//
//  						@Override
//  						public void onSuccess(
//  								ResponseInfo<String> responseInfo) {
//  							LoggerUtil.debug( "个人借款借据：result---->"
//  									+ responseInfo.result
//  									+ "\nresponseInfo.statusCode ===="
//  									+ responseInfo.statusCode);
//  							if (responseInfo.statusCode == 200) {
////  								Type type = new TypeToken<Map<String, String>>() {
////  								}.getType();
//  								Gson gson = new Gson();
//  								LoanRecordDetailBean loanDetail= gson.fromJson(responseInfo.result, LoanRecordDetailBean.class);//还款计划数据
//                               
//								String returnCode = loanDetail.getReturnCode();//返回交易吗
//  								if ("000000".equals(returnCode)) 
//  								{
//  									
//  									LoggerUtil.debug("个人借款借据---->"+loanDetail);
//  									Bundle bundle=new Bundle();
//  									bundle.putSerializable("loanDetailInfo", loanDetail);
//  									Message msg=new Message();
//  									msg.setData(bundle);
//  									msg.what=Contants.MSG_GET_LOAN_LOAN_RECORD_INFO_SUCCESS;
//  									mHandler.sendMessage(msg);
//  									
//  								} else 
//  								{
//  									String returnMsg = loanDetail.getReturnMsg();// 交易提示
//  									LoggerUtil.debug("个人借款借据---->"+returnMsg);
//  									Message msg=new Message();
//  									msg.obj=returnMsg;
//  									msg.what=Contants.MSG_GET_LOAN_LOAN_RECORD_INFO_FAILURE;
//  									mHandler.sendMessage(msg);
//  									
//  								}
//
//  							}
//
//  						}
//  					});
  	}		
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			//返回
			XJLoanMoneyFirstActivity.this.finish();
			break;
		case R.id.loan_money_loan_month_fl:
			//贷款期限
			initChooseOptionPopWindow("借款期限",mLoanMonthTv,view,productRate);
			break;
		case R.id.loan_money_pay_type_fl:
			//还款方式
			initAllPayTypePopWindow();
			mLoanTypePopWindow.showAtLocation(view, 0, 0, 0);
			//doPayType(loanAmount,loanTerm,loanRate,repayMode,"1",repayWayId,productId);		
			break;
		case R.id.loan_money_submit_btn:
			//提交
			if(loanAmount==null||loanAmount.equals("0")||loanAmount.equals(""))
			{
				CustomDialog.Builder builder = new CustomDialog.Builder(
						XJLoanMoneyFirstActivity.this);
				// builder.setIcon(R.drawable.ic_launcher);
				// builder.setTitle(R.string.title_alert);
				builder.setMessage("借款金额不能为0!");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				builder.create().show();
			}
			getLoanDetailInfo(loanAmount,loanTerm,repayMode,prodNum,repayWayId);
			break;
		default:
			break;
		}
	}
	private void initAllPayTypePopWindow() {
		// 得到弹出菜单的view，login_setting_popup是弹出菜单的布局文件
		LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(this);
		View contentView = inflater
				.inflate(R.layout.hxall_lines_pop_layout, null);
		mLoanTypePopWindow = new PopupWindow(contentView,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, false);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x60000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		mLoanTypePopWindow.setBackgroundDrawable(dw);
		// 设置SelectPicPopupWindow弹出窗体可点击
		// mPopWindow.setFocusable(true);
		// mPopWindow.setOutsideTouchable(true);
		// 刷新状态
		mLoanTypePopWindow.update();
		ListView mListView = (ListView) contentView
				.findViewById(R.id.basic_all_lines_listview);
		final String[] repayWayNames=new String[repayWayData.size()+1];
		for (int i = 0; i < repayWayData.size(); i++) 
		{
			String repayWayName=repayWayData.get(i).getRepayWayName();
			repayWayNames[i]=repayWayName;
		}
		repayWayNames[repayWayData.size()]="取消";
//		final String[] titles = new String[] { "个人征信授权书", "信息使用授权书","借款合同","委托扣款协议", "取消" };
		HXBasicLinesListAdapter adapter = new HXBasicLinesListAdapter(this, repayWayNames);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == (repayWayNames.length - 1)) {
					if (mLoanTypePopWindow != null && mLoanTypePopWindow.isShowing()) {
						mLoanTypePopWindow.dismiss();
						mLoanTypePopWindow = null;
					}
				} else {
					repayMode=repayWayData.get(position).getRepayMode();
					repayWayId=repayWayData.get(position).getRepayWayId();
					repayModeName=repayWayData.get(position).getRepayWayName();
					mPayTypeTv.setText(repayModeName); 
					if (mLoanTypePopWindow != null && mLoanTypePopWindow.isShowing()) {
						mLoanTypePopWindow.dismiss();
						mLoanTypePopWindow = null;
					}
					doPayType(loanAmount,loanTerm,loanRate,repayMode,"1",repayWayId,productId);		
				}
			}
		});

	}
}
