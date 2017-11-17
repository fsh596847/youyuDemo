package com.zhongan.demo.hxin.xjactivitys;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.XJBaseActivity;
import com.zhongan.demo.hxin.adapters.HXBasicLinesListAdapter;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.hxin.view.OptionsPickerView;

import java.util.ArrayList;

/**
 * 基本信息-个人信息页面
 */
public class XJBasicSelfInfoActivity extends XJBaseActivity implements OnClickListener {
	private View mStatusView;//设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn,mSubmitBtn;
	private TextView mTitleView,mAllLinesBtn,mEducationTv,mMarryTv,mSelfNameTv,mSelfIdcardTv;
    private ScrollView mScrollView;
    private CheckBox mAllLinesCb;
    private EditText mSelfAddressEt,mRepaySumEt,mReferralSumEt;
    private String selfName="",selfIdcard="",selfAddress="",selfRepaySum="",selfEducation="",selfMarryState="",referralCode="";
    private PopupWindow mLinesPopWindow;
    private OptionsPickerView<String> mOptionView;
    private ArrayList<String> marrys,educations;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contants.MSG_UPLOAD_CUST_INFO_FAILURE:
				CustomDialog.Builder builder = new CustomDialog.Builder(
						XJBasicSelfInfoActivity.this);
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
		setContentView(R.layout.hxactivity_basic_self_info_layout);
		Bundle bundle = getIntent().getBundleExtra("uploadData");
		selfName = bundle.getString("name");// 姓名
		selfIdcard = bundle.getString("idnumber");// 身份证号
		initViews();
	}

	private void initViews() {
		 mStatusView=findViewById(R.id.status_bar_view);
		 int statusHeight= SysUtil.getStatusBarHeight(this);
		 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams(); 
		 params.height=statusHeight;
		 mStatusView.setLayoutParams(params);
		 mBackBtn=(Button) findViewById(R.id.left_btn);
		 mTitleView=(TextView) findViewById(R.id.center_title);
		 mTitleView.setText(R.string.basic_info_text);
		 mBackBtn.setOnClickListener(this);
		 mScrollView=(ScrollView) findViewById(R.id.basic_sv);
		 mAllLinesCb=(CheckBox) findViewById(R.id.allow_the_lines_cb);
		 mAllLinesBtn=(TextView) findViewById(R.id.all_the_lines_tv);
		 mSubmitBtn=(Button) findViewById(R.id.basic_next_or_finish_btn);
		 mSubmitBtn.setClickable(false);
		 mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
		 mSelfNameTv=(TextView) findViewById(R.id.basic_self_name_tv);
		 mSelfNameTv.setText(selfName);
		 mSelfIdcardTv= (TextView) findViewById(R.id.basic_self_idcard_tv);
		 String hideIdCard= Util.hideIdentityCard(selfIdcard);
		 mSelfIdcardTv.setText(hideIdCard);
		 mSelfAddressEt=(EditText) findViewById(R.id.basic_self_address_et);
		 mRepaySumEt=(EditText) findViewById(R.id.basic_self_repayment_sum_et);
		 mEducationTv=(TextView) findViewById(R.id.basic_self_education_tv);
		 mMarryTv=(TextView) findViewById(R.id.basic_self_marry_tv);
		 mReferralSumEt=(EditText) findViewById(R.id.basic_self_referral_code_et);
		 mAllLinesBtn.setOnClickListener(this);
		 mSubmitBtn.setOnClickListener(this);
		 mMarryTv.setOnClickListener(this);
		 mEducationTv.setOnClickListener(this);
		 mSelfNameTv.addTextChangedListener(textWacher);
		 mSelfIdcardTv.addTextChangedListener(textWacher);
		 mSelfAddressEt.addTextChangedListener(textWacher);
		 mRepaySumEt.addTextChangedListener(textWacher);
		 //文本选择器
		 mOptionView=new OptionsPickerView<String>(this);
		 marrys=new ArrayList<String>();
		 marrys.add("未婚");
		 marrys.add("已婚");
		 marrys.add("其他");
		 educations=new ArrayList<String>();
		 educations.add("硕士及以上");
		 educations.add("本科");
		 educations.add("大专");
		 educations.add("职高/技校");
		 educations.add("其他");
		 
	}
	//展示文本选择控件
	private void initChooseOptionPopWindow(final String title,final TextView parentView,View view,final ArrayList<String> items) 
	{	
		Util.hideKeyBoard(this,view);
		mOptionView.setTitle(title);
        String chooseOptions=parentView.getText().toString().trim();
        LoggerUtil.debug("chooseOptions1"+chooseOptions);
        if(chooseOptions==null||"".equals(chooseOptions))
        {
        	mOptionView.setSelectOptions(0,0,0);
        }else if(chooseOptions!=null&&!chooseOptions.equals(""))
        {
        	for (int i = 0; i < items.size(); i++) 
        	{
        		String text=items.get(i);
//        		 LoggerUtil.debug("chooseOptions  text"+text);
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
				if(title.equals("婚姻状况"))
				{
//					selfMarryState=(options1+1)+"";
					selfMarryState = "10";
					if(options1 == 0){
						selfMarryState = "10";
					}

					if(options1 == 1){
						selfMarryState = "20";
					}

					if(options1 == 2){
						selfMarryState = "90";
					}

					LoggerUtil.debug("婚姻状况"+selfMarryState);
				}else if(title.equals("学历信息"))
				{
					selfEducation=(options1+1)+"";
					LoggerUtil.debug("学历信息"+selfEducation);
				}
			}
		});
		
		mOptionView.show();
    }		
   private TextWatcher textWacher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
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
			
			selfAddress=mSelfAddressEt.getText().toString().trim();
			selfRepaySum=mRepaySumEt.getText().toString().trim();
			if(selfName!=null&&!selfName.equals("")&&selfIdcard!=null&&!selfIdcard.equals("")&&selfAddress!=null&&!selfAddress.equals("")&&selfRepaySum!=null&&!selfRepaySum.equals(""))
			{
				mSubmitBtn.setClickable(true);
				mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
				
			}else
			{
				mSubmitBtn.setClickable(false);
				mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
			}
		}
	};
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			//返回
			XJBasicSelfInfoActivity.this.finish();
			break;
		case R.id.basic_self_education_tv:
			initChooseOptionPopWindow("学历信息",mEducationTv,view,educations);
			break;
		case R.id.basic_self_marry_tv:
			initChooseOptionPopWindow("婚姻状况",mMarryTv,view,marrys);
			break;
		case R.id.basic_next_or_finish_btn:
			
			selfAddress=mSelfAddressEt.getText().toString().trim();
			selfRepaySum=mRepaySumEt.getText().toString().trim();
			referralCode=mReferralSumEt.getText().toString().trim();
			if(selfName==null||selfName.equals(""))
			{
//				Message msg=new Message();
//				msg.what=Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj="借款人真实姓名不能为空!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicSelfInfoActivity.this,"借款人真实姓名不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			if(selfIdcard==null||selfIdcard.equals(""))
			{
//				Message msg=new Message();
//				msg.what=Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj="借款人身份证号不能为空!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicSelfInfoActivity.this,"借款人身份证号不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			if(selfAddress==null||selfAddress.equals(""))
			{
//				Message msg=new Message();
//				msg.what=Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj="个人住址不能为空!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicSelfInfoActivity.this,"个人住址不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			if(selfRepaySum==null||selfRepaySum.equals(""))
			{
//				Message msg=new Message();
//				msg.what=Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj="期望额度不能为空!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicSelfInfoActivity.this,"期望额度不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!Util.isIdentityCard(selfIdcard))
			{
//				Message msg=new Message();
//				msg.what=Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj="身份证号格式不正确，请重新输入!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicSelfInfoActivity.this,"身份证号格式不正确，请重新输入!", Toast.LENGTH_SHORT).show();
				return;
			}			
			if(selfMarryState.equals("1"))
			{
				//未婚
				selfMarryState="10";
			}else if(selfMarryState.equals("2"))
			{
				//已婚
				selfMarryState="20";
			}else if(selfMarryState.equals("3"))
			{
				//其婚
				selfMarryState="90";
			}
			LoggerUtil.debug("婚姻状态 ："+selfMarryState);
			Intent intent=new Intent(XJBasicSelfInfoActivity.this,XJBasicCompanyInfoActivity.class);
			intent.putExtra("selfName", selfName);
			intent.putExtra("selfIdcard", selfIdcard);
			intent.putExtra("selfAddress", selfAddress);
			intent.putExtra("selfRepaySum", selfRepaySum);
			intent.putExtra("selfEducation", selfEducation);
			intent.putExtra("selfMarryState", selfMarryState);
			intent.putExtra("referralCode", referralCode);	
			startActivity(intent);
			break;
		case R.id.all_the_lines_tv:
			initAllLinesPopWindow();
			mLinesPopWindow.showAtLocation(view, 0, 0,0);
			break;
		default:
			break;
		}
	}
  private void initAllLinesPopWindow() {
		// 得到弹出菜单的view，login_setting_popup是弹出菜单的布局文件
		LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(this);
		View contentView = inflater.inflate(R.layout.hxall_lines_pop_layout,
				null);
		mLinesPopWindow = new PopupWindow(contentView,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, false);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x60000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		mLinesPopWindow.setBackgroundDrawable(dw);
		// 设置SelectPicPopupWindow弹出窗体可点击
		// mPopWindow.setFocusable(true);
		// mPopWindow.setOutsideTouchable(true);
		// 刷新状态
		mLinesPopWindow.update();
		ListView mListView=(ListView) contentView.findViewById(R.id.basic_all_lines_listview);
		final String[] titles=new String[]{"征信查询授权协议","平台服务协议","取消"};
		HXBasicLinesListAdapter adapter=new HXBasicLinesListAdapter(this, titles);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(position==(titles.length-1))
				{
					if(mLinesPopWindow!=null&&mLinesPopWindow.isShowing())
					{
						mLinesPopWindow.dismiss();
						mLinesPopWindow=null;
					}
				}else{
					Intent intent=new Intent(XJBasicSelfInfoActivity.this,XJUserLineActivity.class);
					startActivity(intent);
				}
			}
		});
		
		
	}
}
