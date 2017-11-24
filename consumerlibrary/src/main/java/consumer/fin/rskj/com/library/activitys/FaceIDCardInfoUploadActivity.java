package consumer.fin.rskj.com.library.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.idcardquality.bean.IDCardAttr;
import com.megvii.licensemanager.Manager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.utils.Base64Util;
import consumer.fin.rskj.com.library.utils.ConUtil;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.utils.SysUtil;
import consumer.fin.rskj.com.library.utils.Util;

import static consumer.fin.rskj.com.library.utils.Constants.TRANS_CODE_UPLOAD_BACK_PICTURE;
import static consumer.fin.rskj.com.library.utils.Constants.TRANS_CODE_UPLOAD_FRONT_PICTURE;

/**
 * 绑定身份证页面
 */
public class FaceIDCardInfoUploadActivity extends BaseActivity implements OnClickListener {
  private IDCardAttr.IDCardSide mIDCardSide;
  /**
   * 垂直 ?有用吗？
   */
  private boolean isVertical;
  /**
   * 正面照
   */
  private String facePotoStr = "";
  /**
   * 反面照
   */
  private String backPhotoStr = "";
  /**
   * face++身份证识别SDK所需的uuid
   */
  private String uuid = "";
  private LinearLayout mWarringBarLL;
  /**
   * 网络检测提示
   */
  private TextView mWarringTv;
  /**
   * 网络检测进度
   */
  private ProgressBar mWarrantyBar;
  /**
   * 网络检测按钮
   */
  private Button againWarringBtn;
  private LinearLayout mIdcardFaceLl;
  private LinearLayout mIdcardBackLl;
  private LinearLayout mIdcardFaceShowLl;
  private LinearLayout mIdcardBackShowLl;
  private Button mBackBtn;
  private TextView mTitleTv;
  private ImageView mIdcardFaceShowIv;
  private TextView mIdcardNumTv;
  private ImageView mIdcardBackShowIv;
  private TextView mIdcardIssueAuthorityTv;
  private TextView mIdcardValidityTv;
  private Button mIdcardInfoSubmitBtn;
  private EditText mIdcardNameEt;
  /**
   * name:姓名 idcardNum：身份证号
   */
  private String name = "";
  /**
   * idcardNum：身份证号
   */
  private String idcardNum = "";
  /**
   * address：户籍地址
   */
  private String address = "";
  private String idcardType = "";
  /**
   * birthday：初始年月
   */
  private String birthday = "";
  /**
   * people:民族
   */
  private String people = "";
  /**
   * sex：性别
   */
  private String sex = "";
  /**
   * issue_authority：签发机关
   */
  private String issue_authority = "";
  /**
   * validity：身份证有效期
   */
  private String validity = "";
  /**
   * 身份证正面照片
   */
  private String custCardFront = "";
  /**
   * 身份证背面照片
   */
  private String custCardVerso = "";
  /**
   * bankCardNum：银行卡号
   */
  private String bankCardNum = "";

  private String modifyName;

  private Dialog timeDialog;
  private int countDown = 6;
  TextView sureBtn;
  Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {

      if (countDown <= 0) {
        handler.removeCallbacksAndMessages(null);
        sureBtn.setText("确认无误");
        sureBtn.setEnabled(true);

      } else {
        countDown--;
        sureBtn.setText(countDown + "s");
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessageDelayed(0, 1000);
      }
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_face_idcard_info_upload_layout);
    LogUtils.d("FaceIDCardInfoUploadActivity", "数组 = " + SysUtil.stepMap);
  }

  @Override public void init() {
    bankCardNum = getIntent().getStringExtra("bankcardId");//银行卡号
    mWarringBarLL = (LinearLayout) findViewById(R.id.start_face_warring_bar_ll);
    mWarringTv = (TextView) findViewById(R.id.start_face_warring_tv);
    //网络检测提示
    mWarrantyBar = (ProgressBar) findViewById(R.id.start_face_warring_bar);//网络检测进度
    againWarringBtn = (Button) findViewById(R.id.start_face_again_warring_btn);//网络检测按钮
    mIdcardFaceLl = (LinearLayout) findViewById(R.id.upload_idcard_face_ll);//身份证正面识别控件
    mIdcardBackLl = (LinearLayout) findViewById(R.id.upload_idcard_back_ll);//身份证反面识别控件
    mIdcardFaceShowLl = (LinearLayout) findViewById(R.id.upload_idcard_face_info_ll);//身份证正面信息展示控件
    mIdcardBackShowLl = (LinearLayout) findViewById(R.id.upload_idcard_back_info_ll);//身份证背面信息展示控件
    mBackBtn = (Button) findViewById(R.id.left_btn);//返回按钮
    mIdcardFaceShowIv = (ImageView) findViewById(R.id.idcard_face);//身份证正面照片显示控件
    mIdcardNumTv = (TextView) findViewById(R.id.idcard_num);//身份证号显示控件
    mIdcardBackShowIv = (ImageView) findViewById(R.id.idcard_back);//身份证背面照片显示控件
    mIdcardIssueAuthorityTv = (TextView) findViewById(R.id.idcard_fazhengjiguan);//身份证签发机关显示控件
    mIdcardValidityTv = (TextView) findViewById(R.id.idcard_longtime);//身份证有效期显示控件
    mIdcardInfoSubmitBtn = (Button) findViewById(R.id.upload_idcard_info_btn);//身份证信息提交按钮
    mIdcardNameEt = (EditText) findViewById(R.id.idcard_name);//身份证姓名显示控件
    mTitleTv = (TextView) findViewById(R.id.center_title);//标题显示控件
    mTitleTv.setText("绑定身份证");
    mBackBtn.setOnClickListener(this);

    uuid = ConUtil.getUUIDString(this);//设备id
    mIdcardFaceLl.setOnClickListener(this);
    mIdcardBackLl.setOnClickListener(this);
    mIdcardFaceShowIv.setOnClickListener(this);
    mIdcardBackShowIv.setOnClickListener(this);
    mIdcardInfoSubmitBtn.setOnClickListener(this);
    mIdcardNameEt.addTextChangedListener(textWacher);//信息输入监听
    againWarringBtn.setOnClickListener(this);
    //face++ sdk 可用性检测
    netWorkWarring();
  }

  private TextWatcher textWacher = new TextWatcher() {

    @Override public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

    }

    @Override public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

    }

    @Override public void afterTextChanged(Editable text) {
      modifyName = mIdcardNameEt.getText().toString().trim();//姓名
      if (modifyName != null && !modifyName.equals("") && modifyName.length() >= 2) {
        //信息提交按钮可用状态
        mIdcardInfoSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
        mIdcardInfoSubmitBtn.setClickable(true);
      } else {
        //信息提交按钮不可用状态
        mIdcardInfoSubmitBtn.setClickable(false);
        mIdcardInfoSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_disable_bg);
      }
    }
  };

  /**
   * 联网授权上传图片
   */
  private void netWorkWarring() {

    mIdcardInfoSubmitBtn.setVisibility(View.GONE);
    mWarringBarLL.setVisibility(View.VISIBLE);
    againWarringBtn.setVisibility(View.GONE);
    mWarringTv.setText("正在联网授权中...");
    mWarrantyBar.setVisibility(View.VISIBLE);
    new Thread(new Runnable() {
      @Override public void run() {

        Manager manager = new Manager(FaceIDCardInfoUploadActivity.this);
        IDCardQualityLicenseManager idCardLicenseManager =
            new IDCardQualityLicenseManager(FaceIDCardInfoUploadActivity.this);
        manager.registerLicenseManager(idCardLicenseManager);
        manager.takeLicenseFromNetwork(uuid);
        String contextStr = manager.getContext(uuid);
        LogUtils.d("debug", "contextStr====" + contextStr);
        LogUtils.d("debug", "idCardLicenseManager.checkCachedLicense()==="
            + idCardLicenseManager.checkCachedLicense());
        if (idCardLicenseManager.checkCachedLicense() > 0) {
          UIAuthState(true);
        } else {
          UIAuthState(false);
        }
      }
    }).start();
  }

  private void UIAuthState(final boolean isSuccess) {
    runOnUiThread(new Runnable() {
      @Override public void run() {
        authState(isSuccess);
      }
    });
  }

  /**
   * face++ sdk可用性检测
   */
  private void authState(boolean isSuccess) {
    if (isSuccess) {
      mIdcardInfoSubmitBtn.setVisibility(View.VISIBLE);
      mWarringBarLL.setVisibility(View.GONE);
    } else {
      againWarringBtn.setVisibility(View.VISIBLE);
      mWarringTv.setText(R.string.link_network_error);
      mWarrantyBar.setVisibility(View.GONE);
      mIdcardInfoSubmitBtn.setVisibility(View.GONE);
    }
  }

  private static final int INTO_IDCARDSCAN_PAGE = 100;

  @Override public void onClick(View view) {
    if (view.getId() == R.id.left_btn) {
      // 返回
      FaceIDCardInfoUploadActivity.this.finish();
    } else if (view.getId() == R.id.start_face_again_warring_btn) {
      //网络检查，是否连接face++
      netWorkWarring();
    } else if (view.getId() == R.id.upload_idcard_face_ll || view.getId() == R.id.idcard_face) {
      Util.hideKeyBoard(FaceIDCardInfoUploadActivity.this, view);
      // 正面照识别操作
      Intent frontIntent = new Intent(FaceIDCardInfoUploadActivity.this, IDCardScanActivity.class);
      frontIntent.putExtra("side", 0);
      frontIntent.putExtra("isvertical", isVertical);
      startActivityForResult(frontIntent, INTO_IDCARDSCAN_PAGE);
    } else if (view.getId() == R.id.upload_idcard_back_ll || view.getId() == R.id.idcard_back) {
      Util.hideKeyBoard(FaceIDCardInfoUploadActivity.this, view);
      // 反面照识别操作
      Intent backIntent = new Intent(FaceIDCardInfoUploadActivity.this, IDCardScanActivity.class);
      backIntent.putExtra("side", 1);
      backIntent.putExtra("isvertical", isVertical);
      startActivityForResult(backIntent, INTO_IDCARDSCAN_PAGE);
    } else if (view.getId() == R.id.upload_idcard_info_btn) {

      //            showCountDownDialog();

      //注释 测试自己跳转下一页
      if (facePotoStr == null || facePotoStr.equals("")) {
        showToast("请拍摄身份证正面照片!", Constants.TOAST_SHOW_POSITION);
        return;
      }
      if (backPhotoStr == null || backPhotoStr.equals("")) {
        showToast("请拍摄身份证反面照!", Constants.TOAST_SHOW_POSITION);
        return;
      }
      modifyName = mIdcardNameEt.getText().toString().trim();// 姓名
      if (modifyName == null || modifyName.equals("")) {
        showToast("姓名不能为空!", Constants.TOAST_SHOW_POSITION);
        return;
      }
      if (idcardNum == null || idcardNum.equals("")) {
        showToast("身份证号不能为空,请重拍!", Constants.TOAST_SHOW_POSITION);
        return;
      }
      String idCardNumStr = Util.get18Ic(idcardNum);//将15位身份证号转为18位
      if (!Util.isIdCardNum(idCardNumStr)) {
        showToast("身份证号格式不正确，请重拍!", Constants.TOAST_SHOW_POSITION);
        return;
      }
      if (issue_authority == null || issue_authority.equals("")) {
        showToast("发证机关不能为空，请重拍!", Constants.TOAST_SHOW_POSITION);
        return;
      }
      if (validity == null || validity.equals("")) {
        showToast("有效期不能为空，请重拍!", Constants.TOAST_SHOW_POSITION);
        return;
      }

      showCountDownDialog();

      //            Intent intent = new Intent(FaceIDCardInfoUploadActivity.this, DealSelfInfoActivity.class);
      //            intent.putExtra("modifyName", name);
      //            intent.putExtra("idcardNum", idcardNum);
      //            intent.putExtra("bankcardId", bankCardNum);
      //            startActivity(intent);
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == INTO_IDCARDSCAN_PAGE && resultCode == RESULT_OK) {
      //身份证识别结果判断
      mIDCardSide = data.getIntExtra("side", 0) == 0 ? IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT
          : IDCardAttr.IDCardSide.IDCARD_SIDE_BACK;

      if (mIDCardSide == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT) {
        //获取身份证正面照
        byte[] idcardImgData = data.getByteArrayExtra("idcardImg");
        Bitmap faceBitmap = BitmapFactory.decodeByteArray(idcardImgData, 0, idcardImgData.length);
        facePotoStr = Base64Util.bitmapToBase64(faceBitmap);
        upLoadFrontPictrue(facePotoStr);
      } else if (mIDCardSide == IDCardAttr.IDCardSide.IDCARD_SIDE_BACK) {
        //获取身份证反面照
        byte[] idcardImgData = data.getByteArrayExtra("idcardImg");
        Bitmap backBitmap = BitmapFactory.decodeByteArray(idcardImgData, 0, idcardImgData.length);
        backPhotoStr = Base64Util.bitmapToBase64(backBitmap);
        upLoadBackPictrue(backPhotoStr);
      }

      //获取身份证正面右侧头像
      if (data.getIntExtra("side", 0) == 0) {
        byte[] portraitImg = data.getByteArrayExtra("portraitImg");
      }
    }
  }

  /**
   * 身份信息更新接口
   */
  private void upLoadIdCardInfo() {

    showLoading(getResources().getString(R.string.dialog_showloading));
    mIdcardInfoSubmitBtn.setClickable(false);
    mIdcardInfoSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_selected_bg);
    requestParams.clear();
    requestParams.put("transCode", Constants.TRANS_CODE_M100122);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token

    requestParams.put("idName", name);//修改后的姓名
    requestParams.put("modifyName", modifyName);//修改后的姓名
    requestParams.put("idnumber", idcardNum);//身份证号
    requestParams.put("birthday", birthday);//生日
    requestParams.put("people", people);//民族
    requestParams.put("sex", sex);//性别
    requestParams.put("address", address);//户籍地址
    requestParams.put("validity", validity);//身份证有效期
    requestParams.put("issue_authority", issue_authority);//发行机构

    requestParams.put("fundId", sharePrefer.getXJFundId());
    requestParams.put("productId", sharePrefer.getXJProductId());

    LogUtils.d("debug", "身份证信息上传: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {

        LogUtils.d("debug", "信息上传: data--->" + data);
        dismissLoading();
        mIdcardInfoSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
        mIdcardInfoSubmitBtn.setClickable(true);
        try {
          JSONObject jsonObject = new JSONObject(data);
          sharePrefer.setCustName(modifyName);
          sharePrefer.setIdCardNum(idcardNum);
          sharePrefer.setBankCardNumber(bankCardNum);
          String currentPKG = FaceIDCardInfoUploadActivity.class.getSimpleName();
          LogUtils.d("currentPKG", "currentPKG->" + currentPKG);
          LogUtils.d("currentPKG", "stepMap->" + SysUtil.stepMap);
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
          LogUtils.d("currentPKG", "currerntIndex->" + currerntIndex);
          Intent intent = new Intent();
          intent.setAction(SysUtil.stepMap[currerntIndex]);
          startActivity(intent);
          FaceIDCardInfoUploadActivity.this.finish();
        } catch (Exception e) {
          LogUtils.d("currentPKG", "Exception->" + e.getMessage());
          dismissLoading();
          showToast("数据格式有误!", Constants.TOAST_SHOW_POSITION);
        }
      }

      @Override public void onError(String retrunCode, String errorMsg) {

        dismissLoading();
        mIdcardInfoSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
        mIdcardInfoSubmitBtn.setClickable(true);
      }

      @Override public void onFailure(String errorMsg) {
        dismissLoading();
        mIdcardInfoSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
        mIdcardInfoSubmitBtn.setClickable(true);
      }
    });
  }

  /**
   * 上传身份证正面照接口
   */
  private void upLoadFrontPictrue(final String facePic) {
    showLoading(getResources().getString(R.string.dialog_showloading));
    Map<String, String> requestParams = new HashMap<>();
    requestParams.put("transCode", TRANS_CODE_UPLOAD_FRONT_PICTURE);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("custCardFront", facePic);//身份证正面照片

    requestParams.put("fundId", sharePrefer.getXJFundId());//资金方Id
    requestParams.put("productId", sharePrefer.getXJProductId());//产品Id

    LogUtils.d("debug", "身份证正面照上传: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {
        LogUtils.d("debug", "身份证正面返回值: data--->" + data);
        try {
          JSONObject jsonObject = new JSONObject(data);
          address = jsonObject.getString("address");// 地址
          birthday = jsonObject.getString("birthday");// 生日
          idcardNum = jsonObject.getString("idnumber");// 身份证号
          name = jsonObject.getString("name");// 姓名
          people = jsonObject.getString("people");// 民族
          sex = jsonObject.getString("sex");// 性别
          mIdcardFaceLl.setVisibility(View.GONE);
          mIdcardFaceShowLl.setVisibility(View.VISIBLE);
          mIdcardNameEt.setText(name);
          mIdcardNameEt.setSelection(name.length());
          mIdcardNumTv.setText(idcardNum);
          if (facePic != null && !facePic.equals("")) {
            Bitmap idcardFace = Base64Util.base64ToBitmap(facePic);
            mIdcardFaceShowIv.setImageBitmap(idcardFace);
          }
          dismissLoading();
        } catch (JSONException e) {
          dismissLoading();
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

  /**
   * 上传身份证背面照接口
   * @param backPic
   */
  private void upLoadBackPictrue(final String backPic) {
    showLoading(getResources().getString(R.string.dialog_showloading));
    Map<String, String> requestParams = new HashMap<>();
    requestParams.put("transCode", TRANS_CODE_UPLOAD_BACK_PICTURE);//接口标识
    requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
    requestParams.put("clientToken", sharePrefer.getToken());//登录后token
    requestParams.put("custCardVerso", backPic);//身份证背面照片

    requestParams.put("fundId", sharePrefer.getXJFundId());//资金方Id
    requestParams.put("productId", sharePrefer.getXJProductId());//产品Id

    LogUtils.d("debug", "身份证上传: requestParams--->" + requestParams.toString());
    sendPostRequest(requestParams, new ResultCallBack() {
      @Override public void onSuccess(String data) {

        LogUtils.d("debug", "身份证返面返回值: data--->" + data);
        try {
          JSONObject jsonObject = new JSONObject(data);
          issue_authority = jsonObject.getString("issue_authority");// 签发单位
          validity = jsonObject.getString("validity");// 身份证有效期
          mIdcardBackLl.setVisibility(View.GONE);
          mIdcardBackShowLl.setVisibility(View.VISIBLE);
          mIdcardIssueAuthorityTv.setText(issue_authority);
          mIdcardValidityTv.setText(validity);
          if (backPic != null && !backPic.equals("")) {
            Bitmap idcardBack = Base64Util.base64ToBitmap(backPic);
            mIdcardBackShowIv.setImageBitmap(idcardBack);
          }
          dismissLoading();
        } catch (JSONException e) {
          dismissLoading();
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

  private void showCountDownDialog() {

    if (timeDialog == null) {
      timeDialog = new Dialog(this, R.style.Dialog);
      View view = LayoutInflater.from(this).inflate(R.layout.timer_dialog_layout, null);
      TextView title = (TextView) view.findViewById(R.id.title);
      TextView name = (TextView) view.findViewById(R.id.name);
      TextView id_num = (TextView) view.findViewById(R.id.id_num);
      name.setText(modifyName);
      id_num.setText(idcardNum);

      sureBtn = (TextView) view.findViewById(R.id.sure);
      sureBtn.setText(countDown + "s");
      sureBtn.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {

          timeDialog.dismiss();
          handler.removeCallbacksAndMessages(null);
          //主动点击
          upLoadIdCardInfo();
        }
      });

      TextView cancleBtn = (TextView) view.findViewById(R.id.cancel);
      cancleBtn.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          timeDialog.dismiss();
          handler.removeCallbacksAndMessages(null);
        }
      });

      timeDialog.setContentView(view);
      timeDialog.setCanceledOnTouchOutside(false);
      WindowManager.LayoutParams params = timeDialog.getWindow().getAttributes();
      params.width = (int) (screenWidth * 0.8);
      params.height = WindowManager.LayoutParams.WRAP_CONTENT;
      timeDialog.getWindow().setAttributes(params);
    }

    countDown = 6;
    timeDialog.show();
    sureBtn.setEnabled(false);
    handler.sendEmptyMessageDelayed(0, 1);
  }
}
