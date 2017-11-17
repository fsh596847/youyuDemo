package com.youyu.fin.pro;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.megvii.idcardquality.IDCardQualityAssessment;
import com.megvii.idcardquality.IDCardQualityResult;
import com.megvii.idcardquality.bean.IDCardAttr;
import com.youyu.fin.pro.module.OcrData;
import com.youyu.fin.pro.util.DialogUtil;
import com.youyu.fin.pro.util.ICamera;
import com.youyu.fin.pro.util.IDCardUtil;
import com.youyu.fin.pro.util.RotaterUtil;
import com.youyu.fin.pro.util.Util;
import com.youyu.fin.pro.view.IDCardIndicator;
import com.youyu.fin.pro.view.IDCardNewIndicator;

import org.json.JSONObject;

public class IDCardScanActivity extends BaseActivity implements TextureView.SurfaceTextureListener, Camera.PreviewCallback {

	private static final String TAG = "YYYUUU";
//	private String base_url = "http://test.xmqq99.com/youyu-api";//身份证上传
	public  final String base_url = "http://youyuapi.rskj99.com/youyu-api";//生产环境
//	public  final String base_url = "http://zsc-website.rskj99.com/youyu-api";//准生产环境

	private String upload_url = "/member/idCard";//身份证上传
	private String identify_url = "/member/idcardrecog";//身份证识别


	private TextureView textureView;
	private DialogUtil mDialogUtil;
	private ICamera mICamera;// 照相机工具类
	private IDCardQualityAssessment idCardQualityAssessment = null;
	private IDCardNewIndicator mNewIndicatorView;
	private IDCardIndicator mIdCardIndicator;
	private IDCardAttr.IDCardSide mSide;
	private DecodeThread mDecoder = null;
	private boolean mIsVertical = false;
	private TextView fps;
	private TextView errorType;
	private TextView horizontalTitle, verticalTitle;
	private TextView logInfo;
	private View debugRectangle;
	private boolean isDebugMode = false;
	int continuousClickCount = 0;
	long lastClickMillis = 0;


	private Vibrator vibrator;
	private float mInBound, mIsIDCard;

	String idHeadImage;//头像
	String idImage;//身份证 正反面
	String token ;

	private Handler handler=new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 10:
					initData();
					break;
				case 1:
					showProgressDialog("请稍后");
					break;
				case 0:
					String text22 =(String) msg.obj;
					Log.d(TAG,"text22 = " + text22);

					JSONObject update_info;
					String code;
					try {
						update_info = new JSONObject(text22);
						//数据返回成功
						code = update_info.getString("code");
						Log.d(TAG,"code = " + code);

						if(!TextUtils.isEmpty(code) && "success".equals(code)){
							JSONObject updateItem = update_info.getJSONObject("data");
							if(null != updateItem){
								Intent intentResult = new Intent();
								//身份证 正面
								if(mSide == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT){
									intentResult.putExtra("side",0);

									String birthday = updateItem.getString("birthday");//出生日期
									String certiNo = updateItem.getString("number");//身份证号
									String address = updateItem.getString("address");//地址
									String nation = updateItem.getString("people");//国家
									String gender = updateItem.getString("sex");//性别
									String certiName = updateItem.getString("name");//姓名

									intentResult.putExtra("birthday",birthday);
									intentResult.putExtra("certiNo",certiNo);
									intentResult.putExtra("address",address);
									intentResult.putExtra("nation",nation);
									intentResult.putExtra("gender",gender);
									intentResult.putExtra("certiName",certiName);
									intentResult.putExtra("idHeadImage",idHeadImage);//身份证头像

									Log.d(TAG,"certiNo = " + certiNo);
									Log.d(TAG,"address = " + address);
									Log.d(TAG,"nation = " + nation);
									Log.d(TAG,"certiName = " + certiName);

								}else {
									intentResult.putExtra("side",1);

									String validPeriodBegin = updateItem.getString("date1");//身份证起始日
									String validPeriodEnd = updateItem.getString("date2");//身份证截止
									String issuedBy = updateItem.getString("office");//公安局

									intentResult.putExtra("validPeriodBegin",validPeriodBegin);
									intentResult.putExtra("validPeriodEnd",validPeriodEnd);
									intentResult.putExtra("issuedBy",issuedBy);

									Log.d(TAG,"validPeriodBegin = " + validPeriodBegin);
									Log.d(TAG,"validPeriodEnd = " + validPeriodEnd);
									Log.d(TAG,"issuedBy = " + issuedBy);
								}


								intentResult.putExtra("idImage",idImage);//身份证 正反面

								progressDialogDismiss();
								setResult(RESULT_OK, intentResult);
								Util.cancleToast(IDCardScanActivity.this);
								Log.e("IDCardScanActivity", "handleSuccess2(ms): " + System.currentTimeMillis());
								finish();

							}

						}else {
                            progressDialogDismiss();
							setResult(RESULT_OK);
							finish();
							Toast.makeText(IDCardScanActivity.this,"身份证识别识别，请打开重试",Toast.LENGTH_SHORT).show();
						}

					} catch (Exception e) {
						Log.d(TAG,"Exception22 = " + e.getMessage());
						progressDialogDismiss();
						setResult(RESULT_OK);
						finish();
					}
					break;

				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		  /*set it to be full screen*/
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.idcardscan_layout);

		init();
//		initData();

		network(handler);

		token = getToken();
		Log.d(TAG, "token->: " + token);
		if(TextUtils.isEmpty(token)){
			Toast.makeText(this,"没有获取到token",Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK);
			this.finish();
			return;
		}
	}

	private void init() {
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		mSide = getIntent().getIntExtra("side", 0) == 0 ? IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT
				: IDCardAttr.IDCardSide.IDCARD_SIDE_BACK;
		mIsVertical = getIntent().getBooleanExtra("isvertical", false);

		mICamera = new ICamera(mIsVertical);
		mDialogUtil = new DialogUtil(this);
		textureView = (TextureView) findViewById(R.id.idcardscan_layout_surface);
		textureView.setSurfaceTextureListener(this);
		textureView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mICamera.autoFocus();
			}
		});
		fps = (TextView) findViewById(R.id.idcardscan_layout_fps);
		logInfo = (TextView) findViewById(R.id.text_debug_info);
		errorType = (TextView) findViewById(R.id.idcardscan_layout_error_type);
		horizontalTitle = (TextView) findViewById(R.id.idcardscan_layout_horizontalTitle);
		verticalTitle = (TextView) findViewById(R.id.idcardscan_layout_verticalTitle);
		mFrameDataQueue = new LinkedBlockingDeque<byte[]>(1);
		mNewIndicatorView = (IDCardNewIndicator) findViewById(R.id.idcardscan_layout_newIndicator);
		mIdCardIndicator = (IDCardIndicator) findViewById(R.id.idcardscan_layout_indicator);
		//LinearLayout idCardLinear = (LinearLayout) findViewById(R.id.idcardscan_layout_idCardImageRel);
		ImageView idCardImage = (ImageView) findViewById(R.id.idcardscan_layout_idCardImage);
		TextView idCardText = (TextView) findViewById(R.id.idcardscan_layout_idCardText);
		debugRectangle = findViewById(R.id.debugRectangle);

		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				mICamera.autoFocus();
				launchDebugMode();
			}
		};
		mNewIndicatorView.setOnClickListener(onClickListener);
		mIdCardIndicator.setOnClickListener(onClickListener);

		if (mIsVertical) {
			horizontalTitle.setVisibility(View.GONE);
			verticalTitle.setVisibility(View.VISIBLE);
			//idCardLinear.setVisibility(View.VISIBLE);
			mIdCardIndicator.setVisibility(View.VISIBLE);
			mNewIndicatorView.setVisibility(View.GONE);
			mIdCardIndicator.setCardSideAndOrientation(mIsVertical, mSide);
			mNewIndicatorView.setCardSideAndOrientation(mIsVertical, mSide);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			horizontalTitle.setVisibility(View.VISIBLE);
			verticalTitle.setVisibility(View.GONE);
			//idCardLinear.setVisibility(View.GONE);
			mIdCardIndicator.setVisibility(View.GONE);
			mNewIndicatorView.setVisibility(View.VISIBLE);
			mIdCardIndicator.setCardSideAndOrientation(mIsVertical, mSide);
			mNewIndicatorView.setCardSideAndOrientation(mIsVertical, mSide);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}

		/*
        if (mSide == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT) {
			mNewIndicatorView.setRightImage(true);
			idCardText.setText("请将身份证正面置于框内");
			idCardImage.setImageResource(R.drawable.sfz_front);
		} else if (mSide == IDCardAttr.IDCardSide.IDCARD_SIDE_BACK) {
			mNewIndicatorView.setRightImage(false);
			idCardImage.setImageResource(R.drawable.sfz_back);
			idCardText.setText("请将身份证反面置于框内");
		}
		*/
	}

	private void launchDebugMode() {
		long currentMillis = System.currentTimeMillis();
		if (continuousClickCount == 0 || (continuousClickCount > 0 && currentMillis - lastClickMillis < 200)) {
			continuousClickCount++;
		}
		lastClickMillis = currentMillis;
		if (continuousClickCount == 6) {
			isDebugMode = true;
			continuousClickCount = 0;
		}
	}

	private void setDebugRectanglePosition() {

		Rect debugRoi;
		if (!mIsVertical) {
			debugRoi = mNewIndicatorView.getMargin();
		} else {
			debugRoi = mIdCardIndicator.getMargin();
		}
		final Rect fDebugRect = debugRoi;

		ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)debugRectangle.getLayoutParams();
		params.setMargins(fDebugRect.left, fDebugRect.top, fDebugRect.right, fDebugRect.bottom);
		debugRectangle.setLayoutParams(params);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		idCardQualityAssessment = new IDCardQualityAssessment.Builder().setIsIgnoreShadow(true).setIsIgnoreHighlight(true).build();
		boolean initSuccess = idCardQualityAssessment.init(this, Util.readModel(this));
		if (!initSuccess) {
			mDialogUtil.showDialog("检测器初始化失败");
		} else {
			mInBound = idCardQualityAssessment.mInBound;
			mIsIDCard = idCardQualityAssessment.mIsIdcard;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDialogUtil.onDestory();
		try {
			if (mDecoder != null) {
				mDecoder.interrupt();
				mDecoder.join();
				mDecoder = null;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(null!= idCardQualityAssessment){
			idCardQualityAssessment.release();
			idCardQualityAssessment = null;
		}

		if(null != handler){
			handler.removeCallbacksAndMessages(null);
		}
	}

	private void doPreview() {
		if (!mHasSurface)
			return;

		mICamera.startPreview(textureView.getSurfaceTexture());
		setDebugRectanglePosition();
	}

	private boolean mHasSurface = false;

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
		Camera mCamera = mICamera.openCamera(this);
		if (mCamera != null) {
			RelativeLayout.LayoutParams layout_params = mICamera.getLayoutParam(this);
			textureView.setLayoutParams(layout_params);
			mNewIndicatorView.setLayoutParams(layout_params);
		} else {
			mDialogUtil.showDialog("打开摄像头失败");
		}

		mHasSurface = true;
		doPreview();
		mICamera.actionDetect(this);
		mDecoder = new DecodeThread();
		mDecoder.start();
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		mICamera.closeCamera();
		mHasSurface = false;
		return false;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {

	}

	@Override
	public void onPreviewFrame(final byte[] data, Camera camera) {
		mFrameDataQueue.offer(data);
	}

	private BlockingQueue<byte[]> mFrameDataQueue;

	private class DecodeThread extends Thread {
		boolean mHasSuccess = false;
		int mCount = 0;
		int mTimSum = 0;
		private IDCardQualityResult.IDCardFailedType mLstErrType;

		@Override
		public void run() {
			byte[] imgData = null;
			try {
				while ((imgData = mFrameDataQueue.take()) != null) {
					if (mHasSuccess)
						return;
					int imageWidth = mICamera.cameraWidth;
					int imageHeight = mICamera.cameraHeight;

					imgData = RotaterUtil.rotate(imgData, imageWidth, imageHeight,
							mICamera.getCameraAngle(IDCardScanActivity.this));
					if (mIsVertical) {
						imageWidth = mICamera.cameraHeight;
						imageHeight = mICamera.cameraWidth;
					}
					long start = System.currentTimeMillis();
					RectF rectF;
					if (!mIsVertical) {
						rectF = mNewIndicatorView.getPosition();
					} else {
						rectF = mIdCardIndicator.getPosition();
					}
					// Log.w("ceshi", "rectF === " + rectF);
					Rect roi = new Rect();
					roi.left = (int) (rectF.left * imageWidth);
					roi.top = (int) (rectF.top * imageHeight);
					roi.right = (int) (rectF.right * imageWidth);
					roi.bottom = (int) (rectF.bottom * imageHeight);

					if (!isEven01(roi.left))
						roi.left = roi.left + 1;
					if (!isEven01(roi.top))
						roi.top = roi.top + 1;
					if (!isEven01(roi.right))
						roi.right = roi.right - 1;
					if (!isEven01(roi.bottom))
						roi.bottom = roi.bottom - 1;

					final IDCardQualityResult result = idCardQualityAssessment.getQuality(imgData, imageWidth,
							imageHeight, mSide, roi);

					long end = System.currentTimeMillis();

					final long perFrameMillis = end - start;
					mCount++;
					mTimSum += (end - start);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (isDebugMode) {
								if (result != null && result.attr != null) {
									String debugResult = "clear: " + new BigDecimal(result.attr.lowQuality).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() + "\n"
											+ "in_bound: " + new BigDecimal(result.attr.inBound).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() + "\n"
											+ "is_idcard: " + new BigDecimal(result.attr.isIdcard).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() + "\n"
											+ "flare: " + result.attr.specularHightlightCount + "\n"
											+ "shadow: " + result.attr.shadowCount + "\n"
											+ "millis: " + perFrameMillis;
									logInfo.setText(debugResult);
								}
								debugRectangle.setVisibility(View.VISIBLE);
							} else {
								logInfo.setText("");
								debugRectangle.setVisibility(View.GONE);
							}
						}
					});

					//Log.e("ceshi", "run: result ==" + result);
					if (result != null) {
						if (result.attr != null) {
							float inBound = result.attr.inBound;
							float isIDCard = result.attr.isIdcard;
							//Log.e("ceshi", "run: isIDCard ==" + isIDCard + ", " + mIsIDCard + ", " + inBound + ", " + mInBound);
							if (isIDCard > mIsIDCard && inBound > 0)
								if (!mIsVertical)
									mNewIndicatorView.setBackColor(IDCardScanActivity.this, 0xaa000000);
								else
									mIdCardIndicator.setBackColor(IDCardScanActivity.this, 0xaa000000);
							else if (!mIsVertical)
								mNewIndicatorView.setBackColor(IDCardScanActivity.this, 0x00000000);
							else
								mIdCardIndicator.setBackColor(IDCardScanActivity.this, 0x00000000);
						}
						if (result.isValid()) {
							vibrator.vibrate(new long[]{0, 50, 50, 100, 50}, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
							mHasSuccess = true;
							handleSuccess(result);
							return;
						} else {
							if (!mIsVertical)
								mNewIndicatorView.setBackColor(IDCardScanActivity.this, 0x00000000);
							else
								mIdCardIndicator.setBackColor(IDCardScanActivity.this, 0x00000000);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									List<IDCardQualityResult.IDCardFailedType> failTypes = (result == null) ? null : result.fails;
									if (failTypes != null) {
										StringBuilder stringBuilder = new StringBuilder();
										IDCardQualityResult.IDCardFailedType errType = failTypes.get(0);
										// if (errType != mLstErrType) {
										if (mIsVertical)
											verticalTitle.setText(Util.errorType2HumanStr(failTypes.get(0), mSide));
										else
											horizontalTitle.setText(Util.errorType2HumanStr(failTypes.get(0), mSide));
										// Util.showToast(IDCardScanActivity.this,
										// Util.errorType2HumanStr(result.fails.get(0),
										// mSide));
										mLstErrType = errType;
										// }
										errorType.setText(stringBuilder.toString());
									} else {
										verticalTitle.setText("");
										horizontalTitle.setText("");
									}
									if (mCount != 0)
										fps.setText((1000 * mCount / mTimSum) + " FPS");
								}
							});
						}
					}
				}
			} catch (InterruptedException e) {

			}catch (Exception e){

			}
		}

		private void handleSuccess(IDCardQualityResult result) {
			Log.e("IDCardScanActivity", "handleSuccess1(ms): " + System.currentTimeMillis());
//			long start = System.currentTimeMillis();
//			Intent intent = new Intent();
//			intent.putExtra("side", mSide == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT ? 0 : 1);
//			intent.putExtra("idcardImg", Util.bmp2byteArr(result.croppedImageOfIDCard()));
//			if (result.attr.side == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT) {
//				intent.putExtra("portraitImg", Util.bmp2byteArr(result.croppedImageOfPortrait()));
//			}
//			setResult(RESULT_OK, intent);
//			Util.cancleToast(IDCardScanActivity.this);
//			Log.e("IDCardScanActivity", "handleSuccess2(ms): " + System.currentTimeMillis());
//			finish();



			handler.sendEmptyMessage(1);
			idImage =  Base64.encodeToString(Util.bmp2byteArr(result.croppedImageOfIDCard()), Base64.DEFAULT);
			OcrData ocrData = new OcrData();

			//身份证 正面
			if(mSide == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT){
				ocrData.setCmd("idcard_front");
				ocrData.setIdCardFrontSuffix("jpg");
				ocrData.setIdCardFront(idImage);
				//如果是正面  有头像返回
				idHeadImage = Base64.encodeToString(Util.bmp2byteArr(result.croppedImageOfPortrait()), Base64.DEFAULT);
			}else {
				ocrData.setCmd("idcard_back");
				ocrData.setIdCardBackSuffix("jpg");
				ocrData.setIdCardBack(idImage);
			}

			httpPost(ocrData);
		}
	}

	//上传图片 到服务器
	private void httpPost(OcrData ocrData) {
		Log.d(TAG, "token222: " + token);
		final String param = new Gson().toJson(ocrData);
		Log.d(TAG, "param: " + param);

        new Thread(){
            public void run() {
                PrintWriter writer = null;
                try {
                    //编码，将文字编码
                    //String ss= URLEncoder.encode(name, "utf-8");
                    //使用HttpURLConnection获得网络数据
                    URL url=new URL(base_url + identify_url);
					HttpURLConnection  urlConnection=(HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Charset", "UTF-8");
					urlConnection.addRequestProperty("deviceType", "android");
					urlConnection.addRequestProperty("code",  "youyu");
					urlConnection.addRequestProperty("token",token);
                    urlConnection.setConnectTimeout(5000);
					urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.connect();
                    writer=new PrintWriter(urlConnection.getOutputStream());
                    //writer.write(kk);
                    // 发送请求参数
                    //{recommandCode:}
                    writer.print(param);

                    writer.flush();
//                    writer.close();
                    int code=urlConnection.getResponseCode();
                    if (code==200) {
                        InputStream inputStream=urlConnection.getInputStream();
                        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                        String liner;
                        StringBuffer buffer=new StringBuffer();
                        while ((liner=reader.readLine())!=null) {
                            buffer.append(liner);

                        }
                        String str=buffer.toString();
						Log.d(TAG,"返回结果 = " + str);
						Message message=new Message();
						message.what=0;
						message.obj=str;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
					progressDialogDismiss();
                    Log.d(TAG,"Exception = " + e.getMessage());
					setResult(RESULT_OK);
					IDCardScanActivity.this.finish();

                }finally {
                    try {
                        if (writer != null) {
                            writer.close();
                        }
                        if (writer != null) {
                            writer.close();
                        }
                    } catch (Exception ex) {
                    }
                }

            };
        }.start();

	}



	public static void startMe(Context context, IDCardAttr.IDCardSide side) {
		if (side == null || context == null)
			return;
		Intent intent = new Intent(context, IDCardScanActivity.class);
		intent.putExtra("side", side == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT ? 0 : 1);
		context.startActivity(intent);
	}

	// 用取余运算
	public boolean isEven01(int num) {
		if (num % 2 == 0) {
			return true;
		} else {
			return false;
		}
	}


}