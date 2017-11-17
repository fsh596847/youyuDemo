package com.zhongan.demo.hxin.xjactivitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.hxin.view.OptionsPickerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * 基本信息-联系人信息页面
 */
public class XJBasicLinkInfoActivity extends XJBaseActivity implements
		OnClickListener {
	private View mStatusView;// 设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn, mSubmitBtn;
	private TextView mTitleView,mLinkRelationShipTv;
	private ScrollView mScrollView;
	private OptionsPickerView<String> mOptionView;
	private ArrayList<String> relatipnships;
	
    private Dialog mDialog;
	private EditText mLinkNameEt, mLinkPhoneEt;
	private String selfName = "", selfIdcard = "", selfAddress="",selfRepaySum = "",
			selfEducation = "", selfMarryState = "", referralCode = "",
			companyName = "", companyPhone = "", companyComeDate="",companyAddr = "",
			linkName = "", linkPhone = "", linkShip = "";
	

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			mDialog.cancel();
			switch (msg.what) {
			case Contants.MSG_UPLOAD_CUST_INFO_FAILURE:
//				Toast.makeText(XJBasicLinkInfoActivity.this,"信息上传失败!",1).show();
				CustomDialog.Builder builder = new CustomDialog.Builder(
						XJBasicLinkInfoActivity.this);
				// builder.setIcon(R.drawable.ic_launcher);
				// builder.setTitle(R.string.title_alert);
				builder.setMessage((String) msg.obj);
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
			case Contants.MSG_UPLOAD_CUST_INFO_SUCCESS:
				Toast.makeText(XJBasicLinkInfoActivity.this,"信息上传成功!",Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(XJBasicLinkInfoActivity.this,XJDealBankCardActivity.class);
				intent.putExtra("selfName",selfName);
				intent.putExtra("selfIdcard",selfIdcard);
				intent.putExtra("option","check");//option: add(添加银行卡)、check(直接进入银行卡验证页面)、checkThree(开启验证三流程)
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
		}
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hxactivity_basic_link_info_layout);

		selfName = getIntent().getStringExtra("selfName");//借款人姓名
		selfIdcard = getIntent().getStringExtra("selfIdcard");//借款人身份证号

//        selfName = sharePrefer.getUserName();
//        selfIdcard = sharePrefer.getIdCardNum();

		selfAddress=getIntent().getStringExtra("selfAddress");//借款人住址
		selfRepaySum = getIntent().getStringExtra("selfRepaySum");//借款额度
		selfEducation = getIntent().getStringExtra("selfEducation");//借款人学历
		selfMarryState = getIntent().getStringExtra("selfMarryState");//借款人婚姻状况
		referralCode = getIntent().getStringExtra("referralCode");//推荐码
		companyName = getIntent().getStringExtra("companyName");//单位名称
		companyPhone = getIntent().getStringExtra("companyPhone");//单位电话
		companyComeDate= getIntent().getStringExtra("companyComeDate");//进入公司日期
		companyAddr = getIntent().getStringExtra("companyAddr");//公司地址
//		edit.putString("selfRepaySum", selfRepaySum);
//		edit.commit();
		mDialog= Util.createLoadingDialog(this,"请稍等...");
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
		mTitleView.setText(R.string.basic_info_text);
		mBackBtn.setOnClickListener(this);
		mScrollView = (ScrollView) findViewById(R.id.basic_sv);

	
		mSubmitBtn = (Button) findViewById(R.id.basic_next_or_finish_btn);
		mSubmitBtn.setClickable(false);
		mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);

		mLinkNameEt = (EditText) findViewById(R.id.basic_link_name_et);
		mLinkPhoneEt = (EditText) findViewById(R.id.basic_link_phonenum_et);
		mLinkRelationShipTv = (TextView) findViewById(R.id.basic_link_relationship_tv);
		mSubmitBtn.setOnClickListener(this);
		mLinkRelationShipTv.setOnClickListener(this);
		mLinkNameEt.addTextChangedListener(textWatcher);
		mLinkPhoneEt.addTextChangedListener(textWatcher);
		mLinkRelationShipTv.addTextChangedListener(textWatcher);
		 //文本选择器
		 mOptionView=new OptionsPickerView<String>(this);
		 relatipnships=new ArrayList<String>();
		 relatipnships.add("父母");
		 relatipnships.add("配偶");
		 relatipnships.add("子女");
		 relatipnships.add("同事");
		 relatipnships.add("朋友");
		
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable text) {
			// TODO Auto-generated method stub
			linkName = mLinkNameEt.getText().toString().trim();//联系人姓名
			linkPhone = mLinkPhoneEt.getText().toString().trim();//联系人电话
			linkShip = mLinkRelationShipTv.getText().toString().trim();//与联系人关系
			if (linkName != null && !linkName.equals("") && linkPhone != null
					&& !linkPhone.equals("") && linkShip != null
					&& !linkShip.equals("")) {
				mSubmitBtn
						.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
				mSubmitBtn.setClickable(true);
			} else {
				mSubmitBtn.setClickable(false);
				mSubmitBtn
						.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
			}
		}
	};
	//展示文本选择控件
		private void initChooseOptionPopWindow(final String title,final TextView parentView,View view,final ArrayList<String> items) 
		{	
			Util.hideKeyBoard(this,view);
			mOptionView.setTitle(title);
	        String chooseOptions=parentView.getText().toString().trim();
//	      LoggerUtil.debug("chooseOptions1"+chooseOptions);
	        if(chooseOptions==null||"".equals(chooseOptions))
	        {
	        	mOptionView.setSelectOptions(0,0,0);
	        }else if(chooseOptions!=null&&!chooseOptions.equals(""))
	        {
	        	for (int i = 0; i < items.size(); i++) 
	        	{
	        		String text=items.get(i);
//	        		 LoggerUtil.debug("chooseOptions  text"+text);
					if(text!=null&&chooseOptions.equals(text))
					{
						LoggerUtil.debug("chooseOptions position"+i);
						mOptionView.setSelectOptions(i,0,0);
					}  	
	        	}
	        }
	        mOptionView.setPicker(items);
	        mOptionView.setCyclic(false);
	        mOptionView.setCancelable(false);
			
			// 时间选择后回调
			mOptionView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {


				@Override
				public void onOptionsSelect(int options1, int option2, int options3) {
					// TODO Auto-generated method stub
					parentView.setText(items.get(options1));
					if(title.equals("联系人类型"))
					{
						linkShip=(options1+1)+"";
						LoggerUtil.debug("联系人类型"+linkShip);
					}
				}
			});
			
			mOptionView.show();
	    }		
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			// 返回
			XJBasicLinkInfoActivity.this.finish();
			break;
		case R.id.basic_link_relationship_tv:
			initChooseOptionPopWindow("联系人类型",mLinkRelationShipTv,view,relatipnships);
		    break;
		case R.id.basic_next_or_finish_btn:
			linkName = mLinkNameEt.getText().toString().trim();
			linkPhone = mLinkPhoneEt.getText().toString().trim();
			if (linkName == null || linkName.equals("")) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj = "联系人姓名不能为空!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicLinkInfoActivity.this,"联系人姓名不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (linkPhone == null || linkPhone.equals("")) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj = "联系人电话不能为空!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicLinkInfoActivity.this,"联系人电话不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			//判断手机号是否合法
			if(!Util.isMobile(linkPhone))
			{
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj = "请输入11位合法手机号码!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicLinkInfoActivity.this,"请输入11位合法手机号码!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (linkShip == null || linkShip.equals("")) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj = "联系人关系不能为空!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicLinkInfoActivity.this,"联系人关系不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			if(selfName.equals(linkName))
			{
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj = "联系人姓名不能与借款人姓名一致!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicLinkInfoActivity.this,"联系人姓名不能与借款人姓名一致!", Toast.LENGTH_SHORT).show();
				return;
			}
			String loginNumber=sharePrefer.getPhone();
			if(linkPhone.equals(loginNumber))
			{
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj = "联系人电话不能与登录手机号一致!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicLinkInfoActivity.this,"联系人电话不能与登录手机号一致!", Toast.LENGTH_SHORT).show();
				return;
			}
				   

			submitInfo(selfName,selfIdcard ,selfAddress, selfRepaySum,
					selfEducation , selfMarryState,referralCode,
					companyName ,companyPhone, companyComeDate, companyAddr,
					linkName,linkPhone,linkShip);

			break;
		
		default:
			break;
		}
	}

	// 基本信息提交接口
	private void submitInfo(String selfName, String selfIdcard ,String selfAddress, String selfRepaySum,
			String selfEducation , String selfMarryState, String referralCode,
			String companyName ,String companyPhone,String companyComeDate, String companyAddr,
			String linkName,String linkPhone,String linkShip) {
		
		mSubmitBtn.setClickable(false);
		mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
		
		LoggerUtil.debug("基本信息提交数据:url---->" + Contants.REQUEST_URL
				+ "\ncustName--->" + selfName + "\nisBorrower--->1" 
				+ "\ntransCode-->" + Contants.TRANS_CODE_UPLOAD_CUST_INFO
				+ "\ncredNo--->" + selfIdcard
				+ "\nlegalPerNumber---->00001" + "\nchannelNo---->"
				+ Contants.CHANNEL_NO+"\nclientToken"+sharePrefer.getToken()
				+"\ncustMobile-------->"+sharePrefer.getPhone());
		RequestParams params = new RequestParams("utf-8");
		params.addHeader("Content-Type","application/x-www-form-urlencoded");
		params.addBodyParameter("transCode", Contants.TRANS_CODE_UPLOAD_CUST_INFO);
		params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
		params.addBodyParameter("clientToken",sharePrefer.getToken());
		params.addBodyParameter("custMobile",sharePrefer.getPhone());// 登录手机号
//		params.addBodyParameter("custNum", custNum);// 客户编号
		params.addBodyParameter("custName", selfName);// 借款人姓名
		//params.addQueryStringParameter("credType", "1");// 证件类型
		params.addBodyParameter("credNo", selfIdcard);// 证件号码
		
		params.addBodyParameter("custHouseAddr", selfAddress);// 个人住址(家庭详细住址)
		params.addBodyParameter("expectLimit", selfRepaySum);//期望额度
		params.addBodyParameter("custEduCational", selfEducation);//最高学历
		params.addBodyParameter("custMarriage", selfMarryState);// 婚姻状况
		params.addBodyParameter("recommendCode", referralCode);// 推荐码
		params.addBodyParameter("custOrgName", companyName);//单位名称
		params.addBodyParameter("custWorkPhone", companyPhone);//单位电话
		params.addBodyParameter("custEnterTime", companyComeDate);//进公司日期
		params.addBodyParameter("custWorkplaceAddr", companyAddr);//单位地址
		params.addBodyParameter("contactName", linkName);//联系人姓名
		params.addBodyParameter("mobileNumber", linkPhone);//联系人电话
		params.addBodyParameter("familyFriendType", linkShip);//与贷款人关系类型
		
		LoggerUtil.debug("基本信息提交数据:url---->");
//		HttpUtils dataHttp = new HttpUtils("60 * 1000");
		httpRequest( Contants.REQUEST_URL, params, new ResultCallBack() {
			
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				mSubmitBtn.setClickable(true);
				mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
				Type type = new TypeToken<Map<String, String>>() {
					}.getType();
					Gson gson = new Gson();
					Map<String, String> resultMap = gson.fromJson(
							data, type);
						mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
				        mSubmitBtn.setClickable(true);
						Message msg = new Message();
						msg.what = Contants.MSG_UPLOAD_CUST_INFO_SUCCESS;
						mHandler.handleMessage(msg);
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				if(mDialog!=null)
				{
				
				mDialog.show();
				}
			}
			
			@Override
			public void onFailure(HttpException exception, String msg) {
				if(mDialog!=null)
				{
				
				mDialog.cancel();
				}
				mSubmitBtn.setClickable(true);
				mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
			}
			
			@Override
			public void onError(String returnCode, String msg) {
				// TODO Auto-generated method stub
				if(mDialog!=null)
				{
				  mDialog.cancel();
				}
				mSubmitBtn.setClickable(true);
				mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
				//Toast.makeText(XJBasicLinkInfoActivity.this,msg, Toast.LENGTH_SHORT).show();
//				Message errorMsg = new Message();
//				errorMsg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				errorMsg.obj = msg;
//				mHandler.handleMessage(errorMsg);
			}
		});
//		 ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
//				new RequestCallBack<String>() {
//                    @Override
//                    public void onStart() {
//                    	// TODO Auto-generated method stub
//                    	super.onStart();
//                    	mDialog.show();
//                    }
//					@Override
//					public void onFailure(HttpException arg0, String error) {
//						// TODO Auto-generated method stub
//						LoggerUtil.debug("基本信息提交接口error-------------->" + error);
//						Message msg = new Message();
//						msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//						msg.obj = "网络问题!";
//						mHandler.handleMessage(msg);
//						mSubmitBtn.setClickable(true);
//						mSubmitBtn.setBackgroundResource(R.drawable.common_btn_normal_bg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("基本信息提交接口result---->" + responseInfo.result
//								+ "\nresponseInfo.statusCode ===="
//								+ responseInfo.statusCode);
//						if (responseInfo.statusCode == 200) {
//							Type type = new TypeToken<Map<String, String>>() {
//							}.getType();
//							Gson gson = new Gson();
//							Map<String, String> resultMap = gson.fromJson(
//									responseInfo.result, type);
//							String returnCode = resultMap.get("returnCode");
//							if ("000000".equals(returnCode)) {
//								mSubmitBtn.setBackgroundResource(R.drawable.common_btn_normal_bg);
//						        mSubmitBtn.setClickable(true);
//								Message msg = new Message();
//								msg.what = Contants.MSG_UPLOAD_CUST_INFO_SUCCESS;
//								mHandler.handleMessage(msg);
//							} else if("E999985".equals(returnCode))
//							{
//								mSubmitBtn.setBackgroundResource(R.drawable.common_btn_normal_bg);
//					         	mSubmitBtn.setClickable(true);
//								//登录超时
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
//								Message msg = new Message();
//								msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//								msg.obj = returnMsg;
//								mHandler.handleMessage(msg);
//							}
//							else{
//								mSubmitBtn.setClickable(true);
//								mSubmitBtn.setBackgroundResource(R.drawable.common_btn_normal_bg);
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
//								Message msg = new Message();
//								msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//								msg.obj = returnMsg;
//								mHandler.handleMessage(msg);
//								
//							}
//
//						}
//
//					}
//				});
	}

	
}
