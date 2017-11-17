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
import android.util.Log;
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
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.hxin.view.TimePickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 基本信息-单位信息页面
 */
public class XJBasicCompanyInfoActivity extends XJBaseActivity implements
		OnClickListener {
	private View mStatusView;// 设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
	private Button mBackBtn, mSubmitBtn;
	private TextView mTitleView, mAllLinesBtn, mCompanyComedateTv;
	private ScrollView mScrollView;
	private TimePickerView pvTime;
	private CheckBox mAllLinesCb;

	private EditText mCompanyNameEt, mCompanyPhoneEt, mCompanyAddrEt;
	private String selfName = "", selfIdcard = "", selfAddress = "",
			selfRepaySum = "", selfEducation = "", selfMarryState = "",
			referralCode = "", companyName = "", companyPhone = "",
			companyComeDate = "", companyAddr = "";
	private PopupWindow mLinesPopWindow;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Contants.MSG_UPLOAD_CUST_INFO_FAILURE:
				CustomDialog.Builder builder = new CustomDialog.Builder(
						XJBasicCompanyInfoActivity.this);
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
		setContentView(R.layout.hxactivity_basic_company_info_layout);

		selfName = getIntent().getStringExtra("selfName");
		selfIdcard = getIntent().getStringExtra("selfIdcard");

//        selfName = sharePrefer.getUserName();
//        selfIdcard = sharePrefer.getIdCardNum();

		selfAddress = getIntent().getStringExtra("selfAddress");
		selfRepaySum = getIntent().getStringExtra("selfRepaySum");
		selfEducation = getIntent().getStringExtra("selfEducation");
		selfMarryState = getIntent().getStringExtra("selfMarryState");
		referralCode = getIntent().getStringExtra("referralCode");
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

		mAllLinesCb = (CheckBox) findViewById(R.id.allow_the_lines_cb);
		mAllLinesBtn = (TextView) findViewById(R.id.all_the_lines_tv);
		mSubmitBtn = (Button) findViewById(R.id.basic_next_or_finish_btn);
		mSubmitBtn.setClickable(false);
		mSubmitBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);

		mCompanyNameEt = (EditText) findViewById(R.id.basic_company_name_et);
		mCompanyPhoneEt = (EditText) findViewById(R.id.basic_company_phonenum_et);
		mCompanyComedateTv = (TextView) findViewById(R.id.basic_company_come_date_tv);
		mCompanyAddrEt = (EditText) findViewById(R.id.basic_company_address_et);
		mAllLinesBtn.setOnClickListener(this);
		mSubmitBtn.setOnClickListener(this);
		mCompanyComedateTv.setOnClickListener(this);
		mCompanyNameEt.addTextChangedListener(textWatcher);
		mCompanyPhoneEt.addTextChangedListener(textWatcher);
		mCompanyComedateTv.addTextChangedListener(textWatcher);
		mCompanyAddrEt.addTextChangedListener(textWatcher);
		// 时间选择器
		pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH);
	}

	// 将日期转换为yyyy年MM月dd日类型字符串
	public String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	// 将日期转换为yyyy年MM月dd日 HH:ss类型字符串转换为Date类型
	public Date toDate(String dateStr) {
		// 定义起始日期
		Date date = null;
		try {
			Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:ss").parse(dateStr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss");
			String str = sdf.format(d1);
			SimpleDateFormat formater = new SimpleDateFormat();
			formater.applyPattern("yyyy-MM-dd HH:ss");
			date = formater.parse(str);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return date;
	}

	// 展示日期控件
	private void initChooseDatePopWindow(String title,
			final TextView parentView, View view) {
		Util.hideKeyBoard(this, view);
		pvTime.setTitle(title);
		int currentYear= Util.getCurrentYear();
		pvTime.setRange(1980,currentYear);
		String chooseDate = parentView.getText().toString().trim();
		if ("".equals(chooseDate)) {
			pvTime.setTime(new Date());
		} else {
			Date date = toDate(chooseDate + "01" + " " + "00:00");
			pvTime.setTime(date);
			Log.v("tag", "date------------>" + date);
		}

		pvTime.setCyclic(false);
		pvTime.setCancelable(false);

		// 时间选择后回调
		// pvTime.setOnTimeSelectListener(new
		// TimePickerView.OnTimeSelectListener()
		// {
		//
		// @Override
		// public void onTimeSelect(Date date)
		// {
		// parentView.setText(getTime(date));
		// }
		// });
		pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

			@Override
			public void onTimeSelect(int year, int month, int day, int hour,
					int minute) {
				String monthStr = "";
				if (month < 10) {
					monthStr = "0" + month;
				} else {
					monthStr = month + "";
				}
				StringBuffer sb = new StringBuffer();
				sb.append((year)).append("-").append(monthStr);
				parentView.setText(sb.toString());
			}

		});
		pvTime.show();
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
			companyName = mCompanyNameEt.getText().toString().trim();
			companyPhone = mCompanyPhoneEt.getText().toString().trim();
			companyComeDate = mCompanyComedateTv.getText().toString().trim();
			companyAddr = mCompanyAddrEt.getText().toString().trim();
			if (companyName != null && !companyName.equals("")
					&& companyPhone != null && !companyPhone.equals("")
					&& companyComeDate != null && !companyComeDate.equals("")
					&& companyAddr != null && !companyAddr.equals("")) {
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

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_btn:
			// 返回
			XJBasicCompanyInfoActivity.this.finish();
			break;
		case R.id.basic_company_come_date_tv:
			String title = "入职时间";
			initChooseDatePopWindow(title, mCompanyComedateTv, view);
			break;
		case R.id.basic_next_or_finish_btn:
			companyName = mCompanyNameEt.getText().toString().trim();
			companyPhone = mCompanyPhoneEt.getText().toString().trim();
			companyComeDate = mCompanyComedateTv.getText().toString().trim();
			companyAddr = mCompanyAddrEt.getText().toString().trim();
			if (companyName == null || companyName.equals("")) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj = "单位名称不能为空!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicCompanyInfoActivity.this,"单位名称不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (companyPhone == null || companyPhone.equals("")) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj = "单位电话不能为空!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicCompanyInfoActivity.this,"单位电话不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (companyComeDate == null || companyComeDate.equals("")) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj = "入职时间不能为空!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicCompanyInfoActivity.this,"入职时间不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (companyAddr == null || companyAddr.equals("")) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj = "单位地址不能为空!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicCompanyInfoActivity.this,"单位地址不能为空!", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!Util.isAreaCode(companyPhone))
			{
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
//				msg.obj = "单位电话格式不正确，请重新输入!";
//				mHandler.sendMessage(msg);
				Toast.makeText(XJBasicCompanyInfoActivity.this,"单位电话格式不正确，请重新输入!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!mAllLinesCb.isChecked()) {
				Message msg = new Message();
				msg.what = Contants.MSG_UPLOAD_CUST_INFO_FAILURE;
				msg.obj = "请先勾选我已同意并阅读《相关协议》";
				mHandler.sendMessage(msg);
				return;
			}
			Intent intent = new Intent(XJBasicCompanyInfoActivity.this,
					XJBasicLinkInfoActivity.class);
			intent.putExtra("selfName", selfName);
			intent.putExtra("selfIdcard", selfIdcard);
			intent.putExtra("selfAddress", selfAddress);
			intent.putExtra("selfRepaySum", selfRepaySum);
			intent.putExtra("selfEducation", selfEducation);
			intent.putExtra("selfMarryState", selfMarryState);
			intent.putExtra("referralCode", referralCode);
			intent.putExtra("companyName", companyName);
			intent.putExtra("companyPhone", companyPhone);
			intent.putExtra("companyComeDate", companyComeDate);
			intent.putExtra("companyAddr", companyAddr);
			startActivity(intent);
			break;
		case R.id.all_the_lines_tv:
			initAllLinesPopWindow();
			mLinesPopWindow.showAtLocation(view, 0, 0, 0);
			break;
		default:
			break;
		}
	}

	private void initAllLinesPopWindow() {
		// 得到弹出菜单的view，login_setting_popup是弹出菜单的布局文件
		LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(this);
		View contentView = inflater
				.inflate(R.layout.hxall_lines_pop_layout, null);
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
		ListView mListView = (ListView) contentView
				.findViewById(R.id.basic_all_lines_listview);
		final String[] titles = new String[] { "征信查询授权协议", "平台服务协议", "取消" };
		HXBasicLinesListAdapter adapter = new HXBasicLinesListAdapter(this, titles);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == (titles.length - 1)) {
					if (mLinesPopWindow != null && mLinesPopWindow.isShowing()) {
						mLinesPopWindow.dismiss();
						mLinesPopWindow = null;
					}
				} else {
					Intent intent = new Intent(XJBasicCompanyInfoActivity.this,
							XJUserLineActivity.class);
					startActivity(intent);

				}
			}
		});

	}
}
