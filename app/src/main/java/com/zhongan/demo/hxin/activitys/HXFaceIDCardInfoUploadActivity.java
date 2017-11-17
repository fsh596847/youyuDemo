package com.zhongan.demo.hxin.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.idcardquality.bean.IDCardAttr;
import com.megvii.licensemanager.Manager;
import com.zhongan.demo.MenuListActivity2;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Base64Util;
import com.zhongan.demo.hxin.util.ConUtil;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.SysUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 身份证信息页面
 */
public class HXFaceIDCardInfoUploadActivity extends HXBaseActivity implements
        OnClickListener {
    private View mStatusView;// 设置顶部标题栏距手机屏幕顶部距离为状态栏高度，防止状态栏遮挡
    private Button mBackBtn, mUploadBtn;
    private TextView mTitleView;
    private ImageView mIdcardFaceIv, mTdcardBackIv;
    private LinearLayout mWarringBarLL;
    private TextView mWarringTv;//网络检测提示
    private ProgressBar mWarrantyBar;//网络检测进度
    private Button againWarringBtn;//网络检测按钮
    private boolean isVertical; //垂直
    private IDCardAttr.IDCardSide mIDCardSide;
    private static final int TAKE_FACE_PICTURE = 0x000001;// 获取正面照码
    private static final int TAKE_BACK_PICTURE = 0x000002;// 获取反面照码
    private String facePotoStr = "";// 正面照
    private String backPhotoStr = "";// 反面照
    private String uuid = "";
    private String option = "";
    private Dialog mDialog;
    private EditText mTrueNameEt, mTrueIdcardNumEt;
    private String name = "", idCardNum = "";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            mDialog.cancel();
            switch (msg.what) {

                case Contants.MSG_UPLOAD_PICTURE_SUCCESS:
                    mUploadBtn.setClickable(true);
                    mUploadBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
                    // 上传
                    Bundle bundle = msg.getData();
//				if("centerUpload".equals(option))
//				{
//					
//				}else
//				{
//                    Intent uploadIntent = new Intent(
//                            HXFaceIDCardInfoUploadActivity.this,
//                            HXBasicSelfInfoActivity.class);
////					uploadIntent.putExtra("option","");//option: add(添加银行卡)、check(直接进入银行卡验证页面)、checkThree(开启验证三流程)
//                    uploadIntent.putExtra("uploadData", bundle);
//                    startActivity(uploadIntent);

                    Intent intent = new Intent(HXFaceIDCardInfoUploadActivity.this,HXDealBankCardActivity.class);
                    intent.putExtra("selfName", name);
                    intent.putExtra("selfIdcard", idCardNum);

                    getSharePrefer().setUserName(name);
                    getSharePrefer().setIdCardNum(idCardNum);

                    intent.putExtra("option", "check");
                    startActivity(intent);
                    finish();
//				}

                    break;
                case Contants.MSG_UPLOAD_PICTURE_FAILURE:

                    mUploadBtn.setClickable(true);
                    mUploadBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
                    CustomDialog.Builder builder = new CustomDialog.Builder(
                            HXFaceIDCardInfoUploadActivity.this);
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
        setContentView(R.layout.hxactivity_face_idcard_info_upload_layout);
//		option=getIntent().getStringExtra("option");
        mDialog = Util.createLoadingDialog(this, "请稍等...");
        initViews();
        netWorkWarring();
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
        mTitleView.setText(R.string.upload_idcard_text);
        mBackBtn.setOnClickListener(this);
        uuid = ConUtil.getUUIDString(this);//设备id
        mIdcardFaceIv = (ImageView) findViewById(R.id.upload_idcard_face);
        mTdcardBackIv = (ImageView) findViewById(R.id.upload_idcard_back);
        mTrueNameEt = (EditText) findViewById(R.id.upload_idcard_name_et);
        mTrueIdcardNumEt = (EditText) findViewById(R.id.upload_idcard_idcard_et);
        mUploadBtn = (Button) findViewById(R.id.upload_idcard_info_btn);
        mIdcardFaceIv.setOnClickListener(this);
        mTdcardBackIv.setOnClickListener(this);
        mUploadBtn.setOnClickListener(this);
        mWarringBarLL = (LinearLayout) findViewById(R.id.start_face_warring_bar_ll);
        mWarringTv = (TextView) findViewById(R.id.start_face_warring_tv);
        mWarrantyBar = (ProgressBar) findViewById(R.id.start_face_warring_bar);
        againWarringBtn = (Button) findViewById(R.id.start_face_again_warring_btn);
        againWarringBtn.setOnClickListener(this);
    }

    /**
     * 联网授权上传图片
     */
    private void netWorkWarring() {

        mUploadBtn.setVisibility(View.GONE);
        mWarringBarLL.setVisibility(View.VISIBLE);
        againWarringBtn.setVisibility(View.GONE);
        mWarringTv.setText("正在联网授权中...");
        mWarrantyBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {

                Manager manager = new Manager(HXFaceIDCardInfoUploadActivity.this);
                IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(HXFaceIDCardInfoUploadActivity.this);
                manager.registerLicenseManager(idCardLicenseManager);
//				String uuid = "13213214321424";
//				
                manager.takeLicenseFromNetwork(uuid);
                String contextStr = manager.getContext(uuid);
                Log.w("ceshi", "contextStr====" + contextStr);

                Log.w("ceshi",
                        "idCardLicenseManager.checkCachedLicense()===" + idCardLicenseManager.checkCachedLicense());
                if (idCardLicenseManager.checkCachedLicense() > 0)
                    UIAuthState(true);
                else
                    UIAuthState(false);
            }
        }).start();
    }

    private void UIAuthState(final boolean isSuccess) {
        runOnUiThread(new Runnable() {
            public void run() {
                authState(isSuccess);
            }
        });
    }

    private void authState(boolean isSuccess) {
        if (isSuccess) {
            mUploadBtn.setVisibility(View.VISIBLE);
            mWarringBarLL.setVisibility(View.GONE);
        } else {
            againWarringBtn.setVisibility(View.VISIBLE);
            mWarringTv.setText(R.string.link_network_error);
            mWarrantyBar.setVisibility(View.GONE);
            mUploadBtn.setVisibility(View.GONE);
        }
    }

    private static final int INTO_IDCARDSCAN_PAGE = 100;

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.left_btn:
                // 返回
                Intent intent = new Intent(this, MenuListActivity2.class);
                startActivity(intent);
                break;
            case R.id.start_face_again_warring_btn:
                //网络检查，是否连接face++
                netWorkWarring();
                break;
            case R.id.upload_idcard_face:
                Util.hideKeyBoard(HXFaceIDCardInfoUploadActivity.this, view);

                if (!findDeniedPermissions(needPermissions).isEmpty()) {
                    checkPermissions(needPermissions);
                    return;
                }

                // 正面照
//			Intent openCameraIntent1 = new Intent(
//					MediaStore.ACTION_IMAGE_CAPTURE);
//			startActivityForResult(openCameraIntent1, TAKE_FACE_PICTURE);
                Intent frontIntent = new Intent(HXFaceIDCardInfoUploadActivity.this, HXFaceIDCardScanActivity.class);
                frontIntent.putExtra("side", 0);
                frontIntent.putExtra("isvertical", isVertical);
                startActivityForResult(frontIntent, INTO_IDCARDSCAN_PAGE);
                break;
            case R.id.upload_idcard_back:
                Util.hideKeyBoard(HXFaceIDCardInfoUploadActivity.this, view);

                if (!findDeniedPermissions(needPermissions).isEmpty()) {
                    checkPermissions(needPermissions);
                    return;
                }

                // 反面照
//			Intent openCameraIntent2 = new Intent(
//					MediaStore.ACTION_IMAGE_CAPTURE);
//			startActivityForResult(openCameraIntent2, TAKE_BACK_PICTURE);
                Intent backIntent = new Intent(HXFaceIDCardInfoUploadActivity.this, HXFaceIDCardScanActivity.class);
                backIntent.putExtra("side", 1);
                backIntent.putExtra("isvertical", isVertical);
                startActivityForResult(backIntent, INTO_IDCARDSCAN_PAGE);
                break;
            case R.id.upload_idcard_info_btn:
                name = mTrueNameEt.getText().toString().trim();
                idCardNum = mTrueIdcardNumEt.getText().toString().trim();

                if (name == null || name.equals("")) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_PICTURE_FAILURE;;
//				msg.obj ="请输入真实姓名!";
//				mHandler.handleMessage(msg);
                    //Toast.makeText(HXFaceIDCardInfoUploadActivity.this, "请输入真实姓名!", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("请输入真实姓名" ,HXFaceIDCardInfoUploadActivity.this);
                    return;
                }
                if (idCardNum == null || idCardNum.equals("")) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_PICTURE_FAILURE;;
//				msg.obj ="请输入身份证号!";
//				mHandler.handleMessage(msg);
                    //Toast.makeText(HXFaceIDCardInfoUploadActivity.this, "请输入身份证号!", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("请输入身份证号" ,HXFaceIDCardInfoUploadActivity.this);
                    return;
                }
                LoggerUtil.debug("idCardNum-------111--->" + idCardNum);
                LoggerUtil.debug("idCardNum-------isTure--->" + Util.isIdentityCard(idCardNum));
                boolean isCorrectIdcardNum = Util.isIdentityCard(idCardNum);
                if (!isCorrectIdcardNum) {
                    LoggerUtil.debug("身份证号格式不正确，请重新输入!");
//				Message msg=new Message();
//				msg.what=Contants.MSG_UPLOAD_PICTURE_FAILURE;
//				msg.obj="身份证号格式不正确，请重新输入!";
//				mHandler.sendMessage(msg);
                    //Toast.makeText(HXFaceIDCardInfoUploadActivity.this, "身份证号格式不正确，请重新输入!", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("身份证号格式不正确，请重新输入!" ,HXFaceIDCardInfoUploadActivity.this);
                    return;
                }
                if (facePotoStr == null || facePotoStr.equals("")) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_PICTURE_FAILURE;;
//				msg.obj ="请先拍摄身份证正面照片!";
//				mHandler.handleMessage(msg);
                    //Toast.makeText(HXFaceIDCardInfoUploadActivity.this, "请先拍摄身份证正面照片!", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("请先拍摄身份证正面照片!" ,HXFaceIDCardInfoUploadActivity.this);
                    return;
                }
                if (backPhotoStr == null || backPhotoStr.equals("")) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_PICTURE_FAILURE;;
//				msg.obj ="请先拍摄身份证反面照!";
//				mHandler.handleMessage(msg);
                    //Toast.makeText(HXFaceIDCardInfoUploadActivity.this, "请先拍摄身份证反面照!", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("请先拍摄身份证反面照!" ,HXFaceIDCardInfoUploadActivity.this);
                    return;
                }


                String idcard18NumStr = Util.get18Ic(idCardNum);
                LoggerUtil.debug("idCardNum-------18--->" + idcard18NumStr);
                int age = Util.IdNOToAge(idcard18NumStr);
                LoggerUtil.debug("age---------->" + age);

                if (age > 0 && age < 18) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_PICTURE_FAILURE;;
//				msg.obj ="您未满18周岁，不能申请贷款!";
//				mHandler.handleMessage(msg);
                    //Toast.makeText(HXFaceIDCardInfoUploadActivity.this, "您未满18周岁，不能申请贷款!", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("您未满18周岁，不能申请贷款!" ,HXFaceIDCardInfoUploadActivity.this);
                    return;
                }
                if (age > 0 && age > 60) {
//				Message msg = new Message();
//				msg.what = Contants.MSG_UPLOAD_PICTURE_FAILURE;;
//				msg.obj ="您年龄大于60岁，不能申请贷款!";
//				mHandler.handleMessage(msg);
//                    Toast.makeText(HXFaceIDCardInfoUploadActivity.this, "您年龄大于60岁，不能申请贷款!", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("您年龄大于60岁，不能申请贷款!" ,HXFaceIDCardInfoUploadActivity.this);
                    return;
                }
                upLoadPictrue(facePotoStr, backPhotoStr, name, idCardNum);
                break;
            default:
                break;
        }
    }

    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch (requestCode) {
//		case TAKE_FACE_PICTURE:
//			if (resultCode == RESULT_OK) {
//				// String fileName = String.valueOf(System.currentTimeMillis());
//				Bitmap faceBitmap = (Bitmap) data.getExtras().get("data");
//				Drawable faceDrawable = new BitmapDrawable(faceBitmap);
//				mIdcardFaceIv.setBackground(faceDrawable);
//				//facePotoStr = Base64Util.bitmapToBase64(faceBitmap);
//			}
//			break;
//		case TAKE_BACK_PICTURE:
//			if (resultCode == RESULT_OK) {
//
//				// String fileName = String.valueOf(System.currentTimeMillis());
//				Bitmap backBitmap = (Bitmap) data.getExtras().get("data");
//				Drawable backDrawable = new BitmapDrawable(backBitmap);	
//				mTdcardBackIv.setBackground(backDrawable);
//				//backPhotoStr = Base64Util.bitmapToBase64(backBitmap);
//				// FileUtils.saveBitmap(backBitmap, fileName);
//
//			}
//			break;
//		}
        if (requestCode == INTO_IDCARDSCAN_PAGE && resultCode == RESULT_OK) {
            mIDCardSide = data.getIntExtra("side", 0) == 0 ? IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT
                    : IDCardAttr.IDCardSide.IDCARD_SIDE_BACK;

            if (mIDCardSide == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT) {
                //获取身份证正面照
                byte[] idcardImgData = data.getByteArrayExtra("idcardImg");
                Bitmap faceBitmap = BitmapFactory.decodeByteArray(idcardImgData, 0, idcardImgData.length);
                Drawable faceDrawable = new BitmapDrawable(faceBitmap);
                mIdcardFaceIv.setBackground(faceDrawable);
                //mIdcardFaceIv.setImageBitmap(faceBitmap);
                facePotoStr = Base64Util.bitmapToBase64(faceBitmap);
            } else if (mIDCardSide == IDCardAttr.IDCardSide.IDCARD_SIDE_BACK) {
                //获取身份证反面照
                byte[] idcardImgData = data.getByteArrayExtra("idcardImg");
                Bitmap backBitmap = BitmapFactory.decodeByteArray(idcardImgData, 0, idcardImgData.length);
                Drawable backDrawable = new BitmapDrawable(backBitmap);
                mTdcardBackIv.setBackground(backDrawable);
                //mTdcardBackIv.setImageBitmap(idcardBmp);
                backPhotoStr = Base64Util.bitmapToBase64(backBitmap);
            }

            //获取身份证正面右侧头像
            if (data.getIntExtra("side", 0) == 0) {
                byte[] portraitImg = data.getByteArrayExtra("portraitImg");
            }
        }
    }

    // 上传身份证照接口
    private void upLoadPictrue(String facePic, String backPic, String truename, String trueidcard) {

        LoggerUtil.debug("上传身份证照: 正面照--->" + facePic);
        LoggerUtil.debug("上传身份证照: 反面照----->" + backPic);
        mUploadBtn.setClickable(false);
        mUploadBtn.setBackgroundResource(R.drawable.hxcommon_btn_selected_bg);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("transCode", Contants.TRANS_CODE_UPLOAD_PICTURE);
        params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken", getSharePrefer().getToken());
        params.addBodyParameter("custCardFront", facePic);
        params.addBodyParameter("custCardVerso", backPic);
        params.addBodyParameter("idno", trueidcard);
        params.addBodyParameter("name", truename);

        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Gson gson = new Gson();
                Map<String, String> resultMap = gson.fromJson(data, type);
                String address = resultMap.get("address");// 地址
                String birthday = resultMap.get("birthday");// 生日
                String idnumber = resultMap.get("idnumber");// 身份证号
                String name = resultMap.get("name");// 姓名
                String people = resultMap.get("people");// 民族
                String sex = resultMap.get("sex");// 性别
                String idcardType = resultMap.get("type");// 证件类型
                Bundle bundle = new Bundle();
                bundle.putString("address", address);
                bundle.putString("birthday", birthday);
                bundle.putString("idnumber", idnumber);
                bundle.putString("name", name);
                bundle.putString("people", people);
                bundle.putString("sex", sex);
                bundle.putString("idcardType", idcardType);
                Message msg = new Message();
                msg.setData(bundle);
                msg.what = Contants.MSG_UPLOAD_PICTURE_SUCCESS;
                mHandler.handleMessage(msg);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                if (mDialog != null) {
                    mDialog.show();
                }
            }

            @Override
            public void onFailure(HttpException exception, String msg) {
                // TODO Auto-generated method stub

                if (mDialog != null) {
                    mDialog.cancel();
                }
                mUploadBtn.setClickable(true);
                mUploadBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
            }

            @Override
            public void onError(String returnCode, String msg) {
                if (mDialog != null) {
                    mDialog.cancel();
                }
                mUploadBtn.setClickable(true);
                mUploadBtn.setBackgroundResource(R.drawable.hxcommon_btn_normal_bg);
                // TODO Auto-generated method stub
//				Message errorMsg = new Message();
//				errorMsg.what = Contants.MSG_UPLOAD_PICTURE_FAILURE;;
//				errorMsg.obj = msg;
//				mHandler.handleMessage(errorMsg);
            }
        });
//		HttpUtils dataHttp = new HttpUtils("120 * 1000");
//		ApplicationExtension.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
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
//						LoggerUtil.debug("error-------------->" + error);
//						Message msg = new Message();
//						msg.what = Contants.MSG_UPLOAD_PICTURE_FAILURE;
//						msg.obj = "上传失败!";
//						mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("上传身份证照：result---->"
//								+ responseInfo.result
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
//								LoggerUtil.debug("上传身份证照成功!"+resultMap.toString());
//								
//                                String address=resultMap.get("address");//地址
//                                String birthday=resultMap.get("birthday");//生日
//                                String idnumber=resultMap.get("idnumber");//身份证号
//                                String name=resultMap.get("name");//姓名
//                                String people=resultMap.get("people");//民族
//                                String sex=resultMap.get("sex");//性别   
//                                String idcardType=resultMap.get("type");//证件类型
////                                if(!truename.equals(name))
////                                {
////                                	Message msg = new Message();
////                                	msg.obj ="您输入的姓名与身份证上的姓名不匹配!";
////    								msg.what = Contants.MSG_UPLOAD_PICTURE_FAILURE;
////    								mHandler.handleMessage(msg);
////                                }else if(!trueidcard.equals(idnumber))
////                                {
////                                	Message msg = new Message();
////                                	msg.obj ="您输入的身份证号与身份证上的身份证号不匹配!";
////    								msg.what = Contants.MSG_UPLOAD_PICTURE_FAILURE;
////    								mHandler.handleMessage(msg);
////                                }else
////                                {
//                                	Bundle bundle=new Bundle();
//                                    bundle.putString("address", address);
//                                    bundle.putString("birthday", birthday);
//                                    bundle.putString("idnumber", idnumber);
//                                    bundle.putString("name", name);
//                                    bundle.putString("people", people);
//                                    bundle.putString("sex", sex);
//                                    bundle.putString("idcardType", idcardType);
//    								Message msg = new Message();
//    								msg.setData(bundle);
//    								msg.what = Contants.MSG_UPLOAD_PICTURE_SUCCESS;
//    								mHandler.handleMessage(msg);
////                                }  
//
//							} else {
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
//								Message msg = new Message();
//								msg.what = Contants.MSG_UPLOAD_PICTURE_FAILURE;;
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, MenuListActivity2.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
