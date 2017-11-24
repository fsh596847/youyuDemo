package consumer.fin.rskj.com.library.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.adapters.CommonRecycleViewAdapter;
import consumer.fin.rskj.com.library.adapters.RecyclerViewHolder;
import consumer.fin.rskj.com.library.callback.FinishCallBackImpl;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.module.PLItem;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.utils.SysUtil;
import consumer.fin.rskj.com.library.utils.Util;
import consumer.fin.rskj.com.library.views.CountDownTimer;
import me.leefeng.lfrecyclerview.LFRecyclerView;

/**
 * 银行卡验4
 */
public class DealSelfInfoActivity extends BaseActivity implements OnClickListener {
  private static final String TAG = DealSelfInfoActivity.class.getSimpleName();

  private View mStatusView;
  private Button mBackBtn;
  private Button mYzmBtn;
  private Button mSubmitBtn;
  private TextView mTitleView;
  private TextView mLinesWeituoshuTv;
  private TextView mLinesHaunkuanTv;
  private TextView mLinesJiekuanTv;
  private TextView mLinesSelfInfoZhengxinTv;

  private EditText mOpenNameTv;
  private EditText mOpenIdCardTv;
  /**
   * 支持银行卡
   */
  private ImageView open_card_warn_btn;
  private Dialog mBanksPopWindow;
  private List<String> itemList = new ArrayList<>();
  /**
   * 手机号
   */
  private EditText mMoblieEt;
  /**
   * 验证码
   */
  private EditText mYzmEt;
  /**
   * 银行卡号
   */
  private EditText mOpenBankCardIdEt;
  private TextView mAllLinesTv;
  /**
   * 协议选择框
   */
  private CheckBox mLinesCheckbox;
  private long surplusTime = 0;
  private long allsurplusTime = 120000;
  /**
   * 姓名
   */
  private String openName;
  /**
   * 身份证号
   */
  private String openIdcard;
  /**
   * 银行卡号
   */
  private String openBankCardId;
  /**
   * 手机号
   */
  private String openMobile;
  /**
   * 验证码
   */
  private String openYzm;
  private CountDownTimer timer;

  private String[] contractNameList;
  private String[] contractUrlList;
  private Intent intent;

  private Handler handler = new Handler() {
    @Override public void dispatchMessage(Message msg) {

      if (msg.what == 0) {
        if (mBanksPopWindow == null) {
          initAllBanksDialog();
        }
        mBanksPopWindow.show();
      }
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_deal_self_info_layout);
    LogUtils.d(TAG, "数组 = " + SysUtil.stepMap);
  }

  @Override public void init() {

    //        openName = getIntent().getStringExtra("modifyName");//获取姓名
    //        openIdcard = getIntent().getStringExtra("idcardNum");//获取身份证号
    //        openBankCardId = getIntent().getStringExtra("bankcardId");//获取银行卡号

        /*openName = sharePrefer.getCustName();
        openIdcard = sharePrefer.getIdCardNum();
        openBankCardId = sharePrefer.getBankCardNumber();*/

    mStatusView = findViewById(R.id.status_bar_view);
    int statusHeight = SysUtil.getStatusBarHeight(this);
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams();
    params.height = statusHeight;
    mStatusView.setLayoutParams(params);
    mBackBtn = (Button) findViewById(R.id.left_btn);
    mTitleView = (TextView) findViewById(R.id.center_title);
    mTitleView.setText("身份信息验证");
    mBackBtn.setOnClickListener(this);
    mOpenNameTv = (EditText) findViewById(R.id.open_card_name_value_tv);// 姓名
    mOpenIdCardTv = (EditText) findViewById(R.id.open_card_idcard_value_tv);// 身份证号

    //        mOpenNameTv.setText(openName);
    //        mOpenIdCardTv.setText(openIdcard);

    //        if(TextUtils.isEmpty(openName) || TextUtils.isEmpty(openIdcard) ){
    getConsumerMsg(new FinishCallBackImpl() {
      @Override public void finishCallBack(String data) {
        openName = sharePrefer.getCustName();
        openIdcard = sharePrefer.getIdCardNum();

        if (TextUtils.isEmpty(openName)) {
          mOpenNameTv.setText("");
          mOpenNameTv.setEnabled(true);
        } else {
          mOpenNameTv.setText(openName);
          mOpenNameTv.setEnabled(false);
        }

        if (TextUtils.isEmpty(openIdcard)) {
          mOpenIdCardTv.setText("");
          mOpenIdCardTv.setEnabled(true);
        } else {
          mOpenIdCardTv.setText(openIdcard);
          mOpenIdCardTv.setEnabled(false);
        }
      }
    });
    //        }else {
    //
    //            mOpenNameTv.setText(openName);
    //            mOpenNameTv.setEnabled(false);
    //
    //            mOpenIdCardTv.setText(openIdcard);
    //            mOpenIdCardTv.setEnabled(false);
    //        }

    mOpenBankCardIdEt = (EditText) findViewById(R.id.open_card_no_value_tv);// 银行卡号
    mOpenBankCardIdEt.setText(openBankCardId);
    open_card_warn_btn = (ImageView) findViewById(R.id.open_card_warn_btn);//支持银行卡
    open_card_warn_btn.setOnClickListener(this);

    mMoblieEt = (EditText) findViewById(R.id.open_card_phonenum_value_et);// 手机号
    mYzmEt = (EditText) findViewById(R.id.open_card_yzm_value_et);// 验证码
    mYzmBtn = (Button) findViewById(R.id.open_yzm_btn);
    mSubmitBtn = (Button) findViewById(R.id.open_submit_btn);// 提交按钮
    mLinesCheckbox = (CheckBox) findViewById(R.id.lines_check_box);//协议选择框

    JSONArray object = null;
    try {
      object = new JSONArray(sharePrefer.getRows());
      contractNameList = new String[object.length()];
      contractUrlList = new String[object.length()];
      for (int a = 0; a < object.length(); a++) {
        JSONObject object22 = object.getJSONObject(a);
        LogUtils.d(TAG, "contractType = " + object22.getString("contractType"));
        LogUtils.d(TAG, "contractName = " + object22.getString("contractName"));
        LogUtils.d(TAG, "contractUrl = " + object22.getString("contractUrl"));
        if (a < object.length() - 1) {
          contractNameList[a] = object22.getString("contractName") + "、";
        } else {
          contractNameList[a] = object22.getString("contractName");
        }
        contractUrlList[a] = object22.getString("contractUrl");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    mLinesWeituoshuTv = (TextView) findViewById(R.id.lines_zhanghu_weituoshu_tv);//账户委托扣款书
    mLinesHaunkuanTv = (TextView) findViewById(R.id.lines_zhidong_huankuan_tv); //自动还款协议
    mLinesJiekuanTv = (TextView) findViewById(R.id.lines_jiekuan_hetong_tv);//借款合同
    mLinesSelfInfoZhengxinTv =
        (TextView) findViewById(R.id.lines_person_info_zhengxin_tv);//个人信息及征信查询授权书
    mAllLinesTv = (TextView) findViewById(R.id.lines_all_lines_tv);

    String temp = "";
    if (null != contractNameList && contractNameList.length > 0) {
      for (String string : contractNameList) {
        temp = temp + string;
      }
    }

    SpannableStringBuilder spannable = new SpannableStringBuilder(/*getResources().getString(R.string.lines_text)*/
        "我已阅读并同意 " + temp);

    mAllLinesTv.setMovementMethod(LinkMovementMethod.getInstance());

    if (contractNameList != null && contractNameList.length > 0) {
      spannable.setSpan(new AccountLinesTextClick(), 8, 8 + contractNameList[0].length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_eb8311)), 8,
          8 + contractNameList[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    if (contractNameList != null && contractNameList.length > 1) {
      spannable.setSpan(new PersonLinesTextClick(), 8 + contractNameList[0].length(),
          8 + contractNameList[0].length() + contractNameList[1].length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_eb8311)),
          8 + contractNameList[0].length(),
          9 + contractNameList[0].length() + contractNameList[1].length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    if (contractNameList != null && contractNameList.length > 2) {
      spannable.setSpan(new ZhenXinLinesTextClick(),
          8 + contractNameList[0].length() + contractNameList[1].length(), spannable.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_eb8311)),
          8 + contractNameList[0].length() + contractNameList[1].length(), spannable.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    mAllLinesTv.setText(spannable);

    mAllLinesTv.setHighlightColor(
        getResources().getColor(android.R.color.transparent));//方法重新设置文字背景为透明色
    mSubmitBtn.setClickable(false);
    mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_disable_bg);
    mSubmitBtn.setOnClickListener(this);
    mYzmBtn.setOnClickListener(this);
    mLinesWeituoshuTv.setOnClickListener(this);
    mLinesHaunkuanTv.setOnClickListener(this);
    mLinesJiekuanTv.setOnClickListener(this);
    mLinesSelfInfoZhengxinTv.setOnClickListener(this);
    mLinesCheckbox.setOnClickListener(this);
    mOpenBankCardIdEt.addTextChangedListener(textWacher);
    mMoblieEt.addTextChangedListener(textWacher);
    mYzmEt.addTextChangedListener(textWacher);
  }

  private class AccountLinesTextClick extends ClickableSpan {

    @Override public void onClick(View widget) {
      //在此处理点击事件
      Util.hideKeyBoard(DealSelfInfoActivity.this, widget);
      //WebViewActivity.openWebViewActivity(DealSelfInfoActivity.this, contractNameList[0], Constants.BASE_URL + contractUrlList[0]);

      LogUtils.d(TAG, "getCustName == " + sharePrefer.getCustName());
      intent = new Intent(DealSelfInfoActivity.this, HtmlActivity2.class);
      intent.putExtra("title", contractNameList[0]);
      intent.putExtra("url", Constants.BASE_URL
          + contractUrlList[0]
          + "?name= "
          + sharePrefer.getCustName()
          + ""
          + "& idcard="
          + sharePrefer.getIdCardNum());
      startActivity(intent);
      //账户委托扣款授权书
      //getLinesURL("2");
    }

    @Override public void updateDrawState(TextPaint ds) {
      ds.setColor(getResources().getColor(R.color.color_eb8311));
      ds.clearShadowLayer();
    }
  }

  private class PersonLinesTextClick extends ClickableSpan {

    @Override public void onClick(View widget) {
      //在此处理点击事件
      Util.hideKeyBoard(DealSelfInfoActivity.this, widget);
      //WebViewActivity.openWebViewActivity(DealSelfInfoActivity.this, contractNameList[1], Constants.BASE_URL + contractUrlList[1]);
      //个人信息使用授权书
      //getLinesURL("3");
      intent = new Intent(DealSelfInfoActivity.this, HtmlActivity2.class);
      intent.putExtra("title", contractNameList[1]);
      intent.putExtra("url", Constants.BASE_URL
          + contractUrlList[1]
          + "?name= "
          + sharePrefer.getCustName()
          + ""
          + "& idcard="
          + sharePrefer.getIdCardNum());
      startActivity(intent);
    }

    @Override public void updateDrawState(TextPaint ds) {
      ds.setColor(getResources().getColor(R.color.color_eb8311));
      ds.clearShadowLayer();
    }
  }

  private class ZhenXinLinesTextClick extends ClickableSpan {

    @Override public void onClick(View widget) {
      //在此处理点击事件
      Util.hideKeyBoard(DealSelfInfoActivity.this, widget);
      //WebViewActivity.openWebViewActivity(DealSelfInfoActivity.this, contractNameList[2], Constants.BASE_URL + contractUrlList[2]);
      //征信查询授权书
      //getLinesURL("4");
      intent = new Intent(DealSelfInfoActivity.this, HtmlActivity2.class);
      intent.putExtra("title", contractNameList[2]);
      intent.putExtra("url", Constants.BASE_URL
          + contractUrlList[2]
          + "?name=  "
          + sharePrefer.getCustName()
          + ""
          + "& idcard="
          + sharePrefer.getIdCardNum());
      startActivity(intent);
    }

    @Override public void updateDrawState(TextPaint ds) {
      ds.setColor(getResources().getColor(R.color.color_eb8311));
      ds.clearShadowLayer();
    }
  }

  @Override protected void onResume() {
    super.onResume();
    //设置短信验证码时间
    Long thisTime = System.currentTimeMillis();//当前系统时间
    Long idCardSysTime = sharePrefer.getTimeIdcardSys();//存储倒计时时间
    Long idCardTime = sharePrefer.getTimeIdcard();
    Long surplusTime = idCardTime - (thisTime - idCardSysTime);//计算倒计时显示时间
    if (idCardSysTime != 0 && idCardTime != 0 && surplusTime > 0) {
      allsurplusTime = surplusTime;
      instantiationTime(allsurplusTime);
      timer.start();
    }
  }

  /**
   * 初始化倒计时控件
   */
  private void instantiationTime(Long time) {
    LogUtils.d("debug", "time------>" + time + "");
    timer = new CountDownTimer(time, 1000) {

      @Override public void onTick(long millisUntilFinished) {
        //LogUtils.d("debug", "allsurplusTime------>" + allsurplusTime + "");
        mYzmBtn.setClickable(false);// 防止重复点击
        mYzmBtn.setText(millisUntilFinished / 1000 + "s");
        surplusTime = millisUntilFinished;
        //LogUtils.d("debug", "surplusTime------>" + surplusTime + "");
      }

      @Override public void onFinish() {
        surplusTime = 0;
        allsurplusTime = 120000;
        mYzmBtn.setText(R.string.yzm_btn_text);
        mYzmBtn.setClickable(true);
      }
    };
  }

  //信息输入监听
  private TextWatcher textWacher = new TextWatcher() {

    @Override public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
      // TODO Auto-generated method stub
    }

    @Override public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
      // TODO Auto-generated method stub
    }

    @Override public void afterTextChanged(Editable text) {
      // TODO Auto-generated method stub
      openName = mOpenNameTv.getText().toString().trim();//姓名
      openIdcard = mOpenIdCardTv.getText().toString().trim();//身份证号
      openBankCardId = mOpenBankCardIdEt.getText().toString().trim();//银行卡号
      openMobile = mMoblieEt.getText().toString().trim();//手机号
      openYzm = mYzmEt.getText().toString().trim();//验证码
      //根据各项信息判断提交按钮是否可用
      if (openName != null
          && !openName.equals("")
          && openIdcard != null
          && !openIdcard.equals("")
          && openBankCardId != null
          && !openBankCardId.equals("")
          && openBankCardId.length() > 10
          && openBankCardId.length() <= 25
          && openMobile != null
          && !openMobile.equals("")
          && openYzm != null
          && !openYzm.equals("")) {
        //可用状态
        mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
        mSubmitBtn.setClickable(true);
      } else {
        //不可用状态
        mSubmitBtn.setClickable(false);
        mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_disable_bg);
      }
    }
  };

  @Override public void onClick(View view) {

    if (view.getId() == R.id.left_btn) {
      Util.hideKeyBoard(DealSelfInfoActivity.this, view);
      this.finish();
      // 返回
    } else if (view.getId() == R.id.open_yzm_btn) {
      Util.hideKeyBoard(DealSelfInfoActivity.this, view);
      openMobile = mMoblieEt.getText().toString().trim();
      if (openMobile == null || openMobile.equals("")) {
        showToast("手机号码不能为空!", Constants.TOAST_SHOW_POSITION);
        return;
      }
      //判断手机号是否合法
      if (!Util.isMobile(openMobile)) {
        showToast("手机号码格式不正确，请重新输入!", Constants.TOAST_SHOW_POSITION);
        return;
      }
      openName = mOpenNameTv.getText().toString().trim();//姓名
      openIdcard = mOpenIdCardTv.getText().toString().trim();//身份证号
      openBankCardId = mOpenBankCardIdEt.getText().toString().trim();//银行卡号

      if (openBankCardId == null || openBankCardId.equals("")) {
        showToast("请输入银行卡号!", Constants.TOAST_SHOW_POSITION);
        return;
      }

      //            isQingyuanBankCard(openBankCardId, openMobile, openIdcard, openName);
      //获取验证码 验四（场景码：17）
      getMsmCode("17", openMobile, new ResultCallBack() {
        @Override public void onSuccess(String data) {
          dismissLoading();
          allsurplusTime = 120000;
          instantiationTime(allsurplusTime);
          timer.start();
        }

        @Override public void onFailure(String errorMsg) {
          mYzmBtn.setClickable(true);
          dismissLoading();
        }

        @Override public void onError(String retrunCode, String errorMsg) {
          mYzmBtn.setClickable(true);
          dismissLoading();
        }
      });
    } else if (view.getId() == R.id.open_submit_btn) {

      //            Intent intent = new Intent(DealSelfInfoActivity.this, FaceStartActivity.class);
      //            startActivity(intent);
      //            DealSelfInfoActivity.this.finish();

      Util.hideKeyBoard(DealSelfInfoActivity.this, view);
      // 提交
      openName = mOpenNameTv.getText().toString().trim();//姓名
      openIdcard = mOpenIdCardTv.getText().toString().trim();//身份证号
      openBankCardId = mOpenBankCardIdEt.getText().toString().trim();//银行卡号
      openMobile = mMoblieEt.getText().toString().trim();//手机号
      openYzm = mYzmEt.getText().toString().trim();//验证码

      //判断手机号是否合法
      if (Util.isMobile(openMobile)) {
        //判断验证码是否合法
        if (!TextUtils.isEmpty(openYzm)) {
          if (mLinesCheckbox.isChecked()) {
            //checkBankCard(openBankCardId, openMobile, openIdcard, openName, openYzm);
            checkSMSCode(openYzm, openMobile, "17", new FinishCallBackImpl() {
              @Override public void finishCallBack(String data) {
                bindBankCard(openBankCardId, openMobile, openIdcard, openName, openYzm);
              }
            });
          } else {
            showToast("请勾选我已确认并同意各项协议", Constants.TOAST_SHOW_POSITION);
          }
        } else {
          showToast("请输入6位合法验证码!", Constants.TOAST_SHOW_POSITION);
        }
      } else {
        showToast("请输入11位合法手机号码!", Constants.TOAST_SHOW_POSITION);
        return;
      }
    } else if (view.getId() == R.id.lines_check_box) {
      Util.hideKeyBoard(DealSelfInfoActivity.this, view);
    } else if (view.getId() == R.id.open_card_warn_btn) {
      // 支持银行卡列表
      if (itemList.isEmpty()) {
        M108103();
      } else {
        handler.obtainMessage(0).sendToTarget();
      }
    }/*else if (view.getId() == R.id.lines_zhanghu_weituoshu_tv) {
            Util.hideKeyBoard(DealSelfInfoActivity.this, view);
            //账户委托扣款授权书
            getLinesURL("2");
        } else if (view.getId() == R.id.lines_zhidong_huankuan_tv) {
            Util.hideKeyBoard(DealSelfInfoActivity.this, view);
            //个人信息使用授权书
            getLinesURL("3");
        } else if (view.getId() == R.id.lines_person_info_zhengxin_tv) {
            Util.hideKeyBoard(DealSelfInfoActivity.this, view);
            //征信查询授权书
            getLinesURL("4");
        }*/
  }

  /**
   * 查询协议h5地址
   */
  private void getLinesURL(final String type) {
    requestParams.put("transCode", Constants.TRANS_CODE_QUERY_H5_URL);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("type", type);//H5页面标识
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        LogUtils.d(TAG, "data" + data);

        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(data);
          String linesURL = Constants.BASE_URL + jsonObject.getString("h5url");//补充信息页面地址
          if ("2".equals(type)) {
            //账户委托扣款授权书
            WebViewActivity.openWebViewActivity(DealSelfInfoActivity.this, "账户委托扣款授权书",
                Constants.BASE_URL + contractUrlList[0]);
          } else if ("3".equals(type)) {
            //个人信息使用授权书
            WebViewActivity.openWebViewActivity(DealSelfInfoActivity.this, "个人信息使用授权书",
                Constants.BASE_URL + contractUrlList[1]);
          } else if ("4".equals(type)) {
            //征信查询授权书
            String personalInformationURL = Constants.BASE_URL
                + contractUrlList[2]
                + "?username="
                + openName
                + "&idcard="
                + openIdcard;
            WebViewActivity.openWebViewActivity(DealSelfInfoActivity.this, "征信查询授权书",
                personalInformationURL);
          }
        } catch (JSONException e) {
          LogUtils.e(TAG, "数据解析有误" + e.toString());
          showToast("数据格式有误!", Constants.TOAST_SHOW_POSITION);
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {
      }

      @Override public void onFailure(String errorMsg) {
      }
    });
  }

  // 是否是清远农商银行卡验证接口
  private void isQingyuanBankCard(String bankCardid, String mobile, String idnumber, String name) {
    //        mYzmBtn.setClickable(false);
    //        showLoading("获取验证码中,请稍后...");
    ////        mSubmitBtn.setClickable(false);
    ////        mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_selected_bg);
    ////        showLoading("信息上传中，请稍后...");
    //        Map<String, String> requestParams = new HashMap<>();
    //        requestParams.put("transCode", Constants.TRANS_CODE__CHECK_BANKCARD_IS_QINGYUAN);//接口标识
    //        requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    //        requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    //        requestParams.put("bacc_num", bankCardid);//银行卡号
    //        requestParams.put("phone", mobile);//银行预留手机号码
    //        requestParams.put("user_name", name);//姓名
    //        requestParams.put("id_card", idnumber);//身份证号
    //        LogUtils.d("debug", "是否是清远农商银行卡: requestParams--->" + requestParams.toString());

    //        sendPostRequest(requestParams, new ResultCallBack() {
    //            @Override
    //            public void onSuccess(String data) {
    //                getMsmCode("4", openMobile);
    //            }
    //
    //            @Override
    //            public void onError(String retrunCode, String errorMsg) {
    //
    //                dismissLoading();
    //                mYzmBtn.setClickable(true);
    //                if ("01".equals(retrunCode) || "02".equals(retrunCode) || "99".equals(retrunCode)) {
    //                    mOpenBankCardIdEt.setText("");
    //                }
    //            }
    //
    //            @Override
    //            public void onFailure(String errorMsg) {
    //                mYzmBtn.setClickable(true);
    //                dismissLoading();
    //            }
    //        });
  }

  /**
   * 银行卡验证4接口
   */
  private void bindBankCard(final String bankCardid, String mobile, String idnumber, String name,
      String yzm) {

    mSubmitBtn.setClickable(false);
    mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_selected_bg);
    showLoading(getResources().getString(R.string.dialog_showloading));
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M100106);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("bankid", bankCardid);//银行卡号
    requestParams.put("cell", mobile);//银行预留手机号码
    requestParams.put("name", name);//姓名
    requestParams.put("idnumber", idnumber);//身份证号
    //        requestParams.put("smsCode", yzm);//验证码
    //        requestParams.put("isNo", "4");//验证码标识

    requestParams.put("fundId", sharePrefer.getXJFundId());//资金方Id
    requestParams.put("productId", sharePrefer.getXJProductId());//产品Id

    LogUtils.d("debug", "身份证信息验证: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {

        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(data);
          String code = jsonObject.getString("code");// 验证结果标识
          dismissLoading();
          if ("00".equals(code)) {
            LogUtils.d("debug", "银行卡验证4成功");
            surplusTime = 0;
            // showToast("身份验证成功!",Constants.TOAST_SHOW_POSITION);

            //String currentPKG = Util.getAppPackageName(DealSelfInfoActivity.this);

            String currentPKG = DealSelfInfoActivity.class.getSimpleName();
            LogUtils.d("currentPKG", "currentPKG->" + currentPKG);
            int currerntIndex = Util.getIndex(SysUtil.stepMap, currentPKG);
            LogUtils.d("currentPKG", "currerntIndex->" + currerntIndex);
            if (currerntIndex == -1) {
              showToast("包名有误：" + currentPKG, Constants.TOAST_SHOW_POSITION);
              return;
            } else {
              currerntIndex = currerntIndex + 1;
              if (currerntIndex > SysUtil.stepMap.length) {
                showToast("超出范围：" + currentPKG, Constants.TOAST_SHOW_POSITION);
                return;
              }
            }

            LogUtils.d("currentPKG", "currerntIndex2->" + currerntIndex);

            //                        Intent intent = new Intent(DealSelfInfoActivity.this, FaceStartActivity.class);
            Intent intent = new Intent();
            intent.setAction(SysUtil.stepMap[currerntIndex]);
            startActivity(intent);
            DealSelfInfoActivity.this.finish();
          } else if ("02".equals(code) || "01".equals(code)) {
            String msg = jsonObject.getString("msg");//验证结果提示语
            LogUtils.d("debug", "银行卡验证4失败：msg--->" + msg + "------code--------->" + code);
            showToast(msg, Constants.TOAST_SHOW_POSITION);
          }

          mSubmitBtn.setClickable(true);
          mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
        } catch (JSONException e) {
          dismissLoading();
          LogUtils.e("error", "数据解析有误" + e.toString());
          showToast("数据格式有误!", Constants.TOAST_SHOW_POSITION);
          mSubmitBtn.setClickable(true);
          mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {

        dismissLoading();
        mSubmitBtn.setClickable(true);
        mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
        if ("E1001065".equals(retrunCode)) {
          // 已经做过验四，直接进入人脸识别
          LogUtils.d("debug", "已经做过验4");
          surplusTime = 0;
          // showToast("身份验证成功!",Constants.TOAST_SHOW_POSITION);
          Intent intent = new Intent(DealSelfInfoActivity.this, FaceStartActivity.class);
          startActivity(intent);
        }
      }

      @Override public void onFailure(String errorMsg) {

        dismissLoading();
        mSubmitBtn.setClickable(true);
        mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
      }
    });
  }

  /**
   * 银行卡验证三接口
   */
  private void checkBankCard(final String bankCardid, String mobile, String idnumber, String name,
      String yzm) {

    mSubmitBtn.setClickable(false);
    mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_selected_bg);
    showLoading(getResources().getString(R.string.dialog_showloading));
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_BANK_CARD_CHECK_THREE);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("bankid", bankCardid);//银行卡号
    requestParams.put("cell", mobile);//银行预留手机号码
    requestParams.put("name", name);//姓名
    requestParams.put("idnumber", idnumber);//身份证号
    requestParams.put("smsCode", yzm);//验证码
    requestParams.put("isNo", "4");//验证码标识

    LogUtils.d("debug", "身份证信息验证: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {

        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(data);
          String code = jsonObject.getString("code");// 验证结果标识
          dismissLoading();
          if ("00".equals(code)) {
            LogUtils.d("debug", "银行卡验证三成功");
            surplusTime = 0;
            // showToast("身份验证成功!",Constants.TOAST_SHOW_POSITION);
            Intent intent = new Intent(DealSelfInfoActivity.this, FaceStartActivity.class);
            startActivity(intent);
            DealSelfInfoActivity.this.finish();
          } else if ("02".equals(code) || "01".equals(code)) {
            String msg = jsonObject.getString("msg");//验证结果提示语
            LogUtils.d("debug", "银行卡验证三失败：msg--->" + msg + "------code--------->" + code);
            showToast(msg, Constants.TOAST_SHOW_POSITION);
          }

          mSubmitBtn.setClickable(true);
          mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
        } catch (JSONException e) {
          dismissLoading();
          LogUtils.e("error", "数据解析有误" + e.toString());
          showToast("数据格式有误!", Constants.TOAST_SHOW_POSITION);
          mSubmitBtn.setClickable(true);
          mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {

        dismissLoading();
        mSubmitBtn.setClickable(true);
        mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
        if ("E1001065".equals(retrunCode)) {
          // 已经做过验四，直接进入人脸识别
          LogUtils.d("debug", "已经做过验三");
          surplusTime = 0;
          // showToast("身份验证成功!",Constants.TOAST_SHOW_POSITION);
          Intent intent = new Intent(DealSelfInfoActivity.this, FaceStartActivity.class);
          startActivity(intent);
        }
      }

      @Override public void onFailure(String errorMsg) {

        dismissLoading();
        mSubmitBtn.setClickable(true);
        mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
      }
    });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (surplusTime > 0) {
      //倒计时最后剩余时间存储
      sharePrefer.setTimeIdcard(surplusTime);
      sharePrefer.setTimeIdcardSys(System.currentTimeMillis());
    }
    //取消倒计时
    if (timer != null) {
      timer.cancel();
    }
  }

  private void initAllBanksDialog() {
    // 得到弹出菜单的view，login_setting_popup是弹出菜单的布局文件
    LayoutInflater inflater = LayoutInflater.from(this);
    View contentView = inflater.inflate(R.layout.support_bank_layout, null);

    dialogView = inflater.inflate(R.layout.support_bank_layout, null);// 得到加载view
    mBanksPopWindow = new Dialog(this, R.style.myDialogTheme);//
    mBanksPopWindow.setContentView(contentView);

    RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.bank_listview);

    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    recyclerView.setAdapter(new CommonRecycleViewAdapter(recyclerView, itemList, R.layout.text_) {

      @Override public void bindConvert(RecyclerViewHolder holder, int position, Object item,
          boolean isScrolling) {
        holder.setText(R.id.bank_name, itemList.get(position).toString());
      }
    });

    contentView.findViewById(R.id.close_btn).setOnClickListener(new OnClickListener() {

      @Override public void onClick(View arg0) {
        if (mBanksPopWindow != null && mBanksPopWindow.isShowing()) {
          mBanksPopWindow.dismiss();
          mBanksPopWindow = null;
        }
      }
    });

    WindowManager.LayoutParams params = mBanksPopWindow.getWindow().getAttributes();
    params.width = (int) (screenWidth * 0.8);
    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    params.gravity = Gravity.CENTER;

    mBanksPopWindow.getWindow().setAttributes(params);
  }

  /**
   * 获取支持银行卡列表
   */
  private void M108103() {
    showLoading("请稍后...");
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M108104);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token

    requestParams.put("indexNo", "0");//
    requestParams.put("pageSize", "999");//
    requestParams.put("fundId", sharePrefer.getXJFundId());//资金方Id
    requestParams.put("prodId", sharePrefer.getXJProductId());//产品Id

    LogUtils.d(TAG, "获取支持银行卡列表: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {

        dismissLoading();
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(data);
          JSONArray jsonArray = jsonObject.optJSONArray("rows");// 验证结果标识
          itemList.clear();
          for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            itemList.add(object.getString("openBankName"));
          }

          handler.obtainMessage(0).sendToTarget();
        } catch (JSONException e) {
          LogUtils.e("error", "数据解析有误" + e.toString());
          showToast("数据格式有误!", Constants.TOAST_SHOW_POSITION);
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {

        dismissLoading();
      }

      @Override public void onFailure(String errorMsg) {

        dismissLoading();
      }
    });
  }
}
