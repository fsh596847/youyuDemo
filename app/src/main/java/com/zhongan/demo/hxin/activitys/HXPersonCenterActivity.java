package com.zhongan.demo.hxin.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.LoginActivity;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseActivity;
import com.zhongan.demo.hxin.adapters.HXBasicLinesListAdapter;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.util.ToastUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

public class HXPersonCenterActivity extends HXBaseActivity implements OnClickListener {
    private Button mBackBtn, mLogoutBtn;
    private FrameLayout mBindMobileFl, mTrueNameFl, mIdcardUpLoadFl, mAccountSafeFl;
    private ImageView mTrueNameIv;
    private TextView mNameTv, mMobileTv, mTrueNameTv;
    private PopupWindow mPopWindow;
    private Dialog mDialog;
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;

    //头像2
    private ImageView headImageIv;
    //调用照相机返回图片临时文件
    private File tempFile;
    // 1: qq, 2: weixin
    private int type;
    private String isTrueNameChecked = "0";//是否实名认证标识 0:未实名认证 1:已实名认证
    private Bundle idcardBundle;
    private String userStateInfo = "0";

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hxactivity_person_center_layout);
        mDialog = Util.createLoadingDialog(this, "数据加载中,请稍等...");
        userStateInfo = getIntent().getStringExtra("userStateInfo");
        initViews();

        //创建拍照存储的临时文件
        createCameraTempFile(savedInstanceState);
        queryIdCardInfo();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (mDialog != null) {
                mDialog.cancel();
            }
            super.handleMessage(msg);
            switch (msg.what) {
                case Contants.MSG_DO_QUERY_IDCARD_INFO_FAILURE:
                    CustomDialog.Builder builder = new CustomDialog.Builder(HXPersonCenterActivity.this);
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

                case Contants.MSG_DO_QUERY_IDCARD_INFO_SUCCESS:
                    isTrueNameChecked = (String) msg.obj;//是否实名认证标识 0:未实名认证 1:已实名认证
                    if (isTrueNameChecked.equals("1")) {
                        //已经实名认证展示
                        idcardBundle = msg.getData();
                        String name = idcardBundle.getString("name");
                        mTrueNameTv.setText(R.string.alreday_open_text);
                        mNameTv.setText(name);
                        mTrueNameIv.setBackgroundResource(R.drawable.m_icon_true_name);
                        mTrueNameIv.setVisibility(View.VISIBLE);

                    } else if (isTrueNameChecked.equals("0")) {
                        //未实名认证展示
                        mTrueNameTv.setText(R.string.not_open_text);
                        mNameTv.setText("");
                        mTrueNameTv.setText("");
                        mNameTv.setVisibility(View.VISIBLE);
                        mTrueNameIv.setBackgroundResource(R.drawable.m_icon_not_true_name);
                        mTrueNameIv.setVisibility(View.VISIBLE);
                    }

                    break;
                default:
                    break;
            }
        }
    };

    private void initViews() {
        mBackBtn = (Button) findViewById(R.id.person_center_back_btn);//返回
        mLogoutBtn = (Button) findViewById(R.id.person_center_logout_btn);//退出登录
        mBindMobileFl = (FrameLayout) findViewById(R.id.person_center_moblie_fl);//绑定手机
        mTrueNameFl = (FrameLayout) findViewById(R.id.person_center_true_name_fl);//实名认证
        mIdcardUpLoadFl = (FrameLayout) findViewById(R.id.person_center_idcard_img_upload_fl);//身份证上传
        mAccountSafeFl = (FrameLayout) findViewById(R.id.person_center_accound_safe_fl);//账户安全
        mNameTv = (TextView) findViewById(R.id.person_name_tv);//姓名
        mMobileTv = (TextView) findViewById(R.id.person_center_moblie_tv);//手机号
        String hideMoblie = Util.hideMobile(getSharePrefer().getPhone());
        mMobileTv.setText(hideMoblie);
        mTrueNameTv = (TextView) findViewById(R.id.person_center_true_name_tv);//实名认证开启提示
        mTrueNameTv.setText("");
        headImageIv = (ImageView) findViewById(R.id.person_head_iv);//头像
        mTrueNameIv = (ImageView) findViewById(R.id.person_true_name_iv);//实名认证图标
        mTrueNameIv.setVisibility(View.GONE);
        headImageIv.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mLogoutBtn.setOnClickListener(this);
        mBindMobileFl.setOnClickListener(this);
        mTrueNameFl.setOnClickListener(this);
        mIdcardUpLoadFl.setOnClickListener(this);
        mAccountSafeFl.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.person_center_back_btn:
                HXPersonCenterActivity.this.finish();
                break;
            case R.id.person_head_iv:
//			type = 2;//方形图片
//			initPopWindow();
//			mPopWindow.showAtLocation(view, 0, 0, 0);
                break;
            case R.id.person_center_moblie_fl:
                //绑定手机
                break;
            case R.id.person_center_true_name_fl:
                //实名认证
                Intent trueNameIntent = new Intent(HXPersonCenterActivity.this, HXTrueNameActivity.class);
                trueNameIntent.putExtra("isTrueNameChecked", isTrueNameChecked);
                if (isTrueNameChecked.equals("1")) {
                    trueNameIntent.putExtra("idcardInfo", idcardBundle);
                }
                startActivity(trueNameIntent);
                break;
            case R.id.person_center_idcard_img_upload_fl:
                //身份证上传
//			Intent upoadIntent=new Intent(HXPersonCenterActivity.this,HXFaceIDCardInfoUploadActivity.class);
//			upoadIntent.putExtra("option","centerUpload");
//			startActivity(upoadIntent);
                break;
            case R.id.person_center_accound_safe_fl:
                //账户安全
                Intent accountIntent = new Intent(HXPersonCenterActivity.this, HXAccountSafeActivity.class);
                accountIntent.putExtra("userStateInfo", userStateInfo);
                startActivity(accountIntent);
                break;
            case R.id.person_center_logout_btn:
                //退出登录
                logout();
                break;

            default:
                break;
        }
    }

    private void logout() {
        mDialog = Util.createLoadingDialog(this, "数据加载中,请稍等...");
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("transCode", Contants.TRANS_CODE_LOGOUT);
        params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken", getSharePrefer().getToken());
        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                //登出成功
                getSharePrefer().setLogin(false);
                //Toast.makeText(HXPersonCenterActivity.this, "登出成功!", Toast.LENGTH_SHORT).show();
                ToastUtils.showCenterToast("登出成功",HXPersonCenterActivity.this);
//                Intent logoutIntent = new Intent(HXPersonCenterActivity.this, HXLoginActivity.class);
//                startActivity(logoutIntent);

                Intent intent = new Intent(HXPersonCenterActivity.this,LoginActivity.class);
                startActivity(intent);
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
            }

            @Override
            public void onError(String returnCode, String msg) {
                // TODO Auto-generated method stub
                if (mDialog != null) {
                    mDialog.cancel();
                }
            }
        });
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
//						LoggerUtil.debug("退出登录接口error-------------->" + error);
////						String returnMsg = resultMap.get("returnMsg");// 错误提示
////						Message msg = new Message();
////						msg.what = Contants.MSG_DO_QUERY_IDCARD_INFO_FAILURE;
////						msg.obj = "网络问题!";
////						mHandler.handleMessage(msg);
//						Toast.makeText(HXPersonCenterActivity.this,"网络问题!",Toast.LENGTH_SHORT).show();
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("退出登录接口result---->" + responseInfo.result
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
//								//登出成功
//								sharePrefer.setLogin(false);
//								Toast.makeText(HXPersonCenterActivity.this,"登出成功!",Toast.LENGTH_SHORT).show();
//								Intent logoutIntent=new Intent(HXPersonCenterActivity.this,HXLoginActivity.class);
//								startActivity(logoutIntent);
//							
//							} 
//							else
//							{
//								Toast.makeText(HXPersonCenterActivity.this,"returnMsg",Toast.LENGTH_SHORT).show();
////								String returnMsg = resultMap.get("returnMsg");// 错误提示
////								Message msg = new Message();
////								msg.what = Contants.MSG_DO_QUERY_IDCARD_INFO_FAILURE;
////								msg.obj = returnMsg;
////								mHandler.handleMessage(msg);
//							}
//
//						}
//
//					}
//				});

    }

    /**
     * 身份证信息查询
     **/
    private void queryIdCardInfo() {
        LoggerUtil.debug("身份证信息查询接口:url---->" + Contants.REQUEST_URL

                + "\ntransCode-->" + Contants.TRANS_CODE_QUERY_IDCARD_INFO
                + "\nchannelNo---->"
                + Contants.CHANNEL_NO + "\nclientToken--------->" + getSharePrefer().getToken()
                + "\ncustMobile-------->" + getSharePrefer().getPhone());
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("transCode", Contants.TRANS_CODE_QUERY_IDCARD_INFO);
        params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken", getSharePrefer().getToken());
        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Gson gson = new Gson();
                Map<String, String> resultMap = gson.fromJson(
                        data, type);
                //查询到身份信息
                String isTrueNameChecked = "1";
                String id_no = resultMap.get("id_no");//身份证
                String name = resultMap.get("name");//身份证
                String custCardFront = resultMap.get("custCardFront");//身份证正面
                String custCardVerso = resultMap.get("custCardVerso");//身份证反面
                LoggerUtil.debug("身份证信息查询接口custCardFront---->" + custCardFront);
                LoggerUtil.debug("身份证信息查询接口custCardVerso---->" + custCardVerso);
                Bundle bundle = new Bundle();
                bundle.putString("id_no", id_no);
                bundle.putString("name", name);
                bundle.putString("custCardFront", custCardFront);
                bundle.putString("custCardVerso", custCardVerso);
                Message msg = new Message();
                msg.what = Contants.MSG_DO_QUERY_IDCARD_INFO_SUCCESS;
                msg.setData(bundle);
                msg.obj = isTrueNameChecked;
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
                if (mDialog != null) {
                    mDialog.cancel();
                }
            }

            @Override
            public void onError(String returnCode, String errorMsg) {
                // TODO Auto-generated method stub
                if ("E1001062".equals(returnCode)) {
                    //查不到身份信息
                    String isTrueNameChecked = "0";
                    Message msg = new Message();
                    msg.what = Contants.MSG_DO_QUERY_IDCARD_INFO_SUCCESS;
                    msg.obj = isTrueNameChecked;
                    mHandler.handleMessage(msg);

                } else {
                    isTrueNameChecked = "0";
                    Message msg = new Message();
                    msg.what = Contants.MSG_DO_QUERY_IDCARD_INFO_SUCCESS;
                    msg.obj = isTrueNameChecked;
                    mHandler.handleMessage(msg);
                }
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
//						isTrueNameChecked="0";
//						// TODO Auto-generated method stub
//						LoggerUtil.debug("身份证信息查询接口error-------------->" + error);
////						String returnMsg = resultMap.get("returnMsg");// 错误提示
//						Message msg = new Message();
//						msg.what = Contants.MSG_DO_QUERY_IDCARD_INFO_FAILURE;
//						msg.obj = "网络问题!";
//						mHandler.handleMessage(msg);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						LoggerUtil.debug("身份证信息查询接口result---->" + responseInfo.result
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
//								//查询到身份信息
//								String isTrueNameChecked="1";
//								String id_no=resultMap.get("id_no");//身份证
//								String name=resultMap.get("name");//身份证
//								String custCardFront=resultMap.get("custCardFront");//身份证正面
//								String custCardVerso=resultMap.get("custCardVerso");//身份证反面
//								LoggerUtil.debug("身份证信息查询接口custCardFront---->" + custCardFront);
//								LoggerUtil.debug("身份证信息查询接口custCardVerso---->" + custCardVerso);
//							   	Bundle bundle=new Bundle();
//							   	bundle.putString("id_no", id_no);
//								bundle.putString("name", name);
//								bundle.putString("custCardFront", custCardFront);
//								bundle.putString("custCardVerso", custCardVerso);
//								Message msg = new Message();
//								msg.what = Contants.MSG_DO_QUERY_IDCARD_INFO_SUCCESS;
//								msg.setData(bundle);
//								msg.obj=isTrueNameChecked;
//								mHandler.handleMessage(msg);
//							
//							} 
//							else if("E1001062".equals(returnCode))
//							{
//								//查不到身份信息
//								String isTrueNameChecked="0";
//								Message msg = new Message();
//								msg.what = Contants.MSG_DO_QUERY_IDCARD_INFO_SUCCESS;	
//								msg.obj=isTrueNameChecked;
//								mHandler.handleMessage(msg);
//								
//							}else
//							{
//								isTrueNameChecked="0";
//								String returnMsg = resultMap.get("returnMsg");// 错误提示
//								Message msg = new Message();
//								msg.what = Contants.MSG_DO_QUERY_IDCARD_INFO_FAILURE;
//								msg.obj = returnMsg;
//								mHandler.handleMessage(msg);
//							}
//
//						}
//
//					}
//				});
    }

    /**
     * 外部存储权限申请返回
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @SuppressLint("Override")
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCarema();
            } else {
                // Permission Denied
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            } else {
                // Permission Denied
            }
        }
    }

    private void initPopWindow() {
        // 得到弹出菜单的view，login_setting_popup是弹出菜单的布局文件
        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(this);
        View contentView = inflater
                .inflate(R.layout.hxall_lines_pop_layout, null);
        mPopWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x60000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        mPopWindow.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体可点击
        // mPopWindow.setFocusable(true);
        // mPopWindow.setOutsideTouchable(true);
        // 刷新状态
        mPopWindow.update();
        ListView mListView = (ListView) contentView
                .findViewById(R.id.basic_all_lines_listview);
        final String[] titles = new String[]{"拍照", "从相册中选取", "取消"};
        HXBasicLinesListAdapter adapter = new HXBasicLinesListAdapter(this, titles);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (position == (titles.length - 1)) {
                    //取消
                    if (mPopWindow != null && mPopWindow.isShowing()) {
                        mPopWindow.dismiss();
                        mPopWindow = null;
                    }
                } else if (position == 0) {
                    //拍照
                    //权限判断
//	                if (ContextCompat.checkSelfPermission(HXPersonCenterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//	                        != PackageManager.PERMISSION_GRANTED) {
//	                    //申请WRITE_EXTERNAL_STORAGE权限
//	                    ActivityCompat.requestPermissions(HXPersonCenterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//	                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
//	                } else {
                    //跳转到调用系统相机
                    gotoCarema();
//	                }
                    if (mPopWindow != null && mPopWindow.isShowing()) {
                        mPopWindow.dismiss();
                        mPopWindow = null;
                    }
                } else if (position == 1) {
                    //相册选取
                    //权限判断
//	                if (ContextCompat.(HXPersonCenterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//	                        != PackageManager.PERMISSION_GRANTED) {
//	                    //申请READ_EXTERNAL_STORAGE权限
//	                    ActivityCompat.requestPermissions(HXPersonCenterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//	                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
//	                } else {
                    //跳转到调用系统图库
                    gotoPhoto();
//	                }
                    if (mPopWindow != null && mPopWindow.isShowing()) {
                        mPopWindow.dismiss();
                        mPopWindow = null;
                    }
                }
            }
        });

    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"), System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    //hasHeadImg

                    getSharePrefer().setCropImagePath(cropImagePath);//存储头像图片路径
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    if (type == 1) {
                        //圆角图片
                        // headImage.setImageBitmap(bitMap);
                    } else {
                        //方形图片
                        headImageIv.setImageBitmap(bitMap);
                        // headImage2.setImageBitmap(bitMap);
                    }
                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......

                }
                break;
        }
    }


    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, HXClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
