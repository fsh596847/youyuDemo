package consumer.fin.rskj.com.library.utils;

import android.os.Environment;

/**
 * 常量
 */

public class Constants {
  public static final int TOAST_SHOW_POSITION = 1;//Toast 显示位置：0：顶部 1：中间 2：底部

  /**
   * face++ 人脸识别
   */
  public static String cacheText = "livenessDemo_text";
  public static String cacheImage = "livenessDemo_image";
  public static String cacheVideo = "livenessDemo_video";
  public static String cacheCampareImage = "livenessDemo_campareimage";
  public static String dirName =
      Environment.getExternalStorageDirectory().getAbsolutePath() + "/faceapp";

  public static String PICTURE_SAVE_PATH = "/photo.png";

  //    public static final String BASE_URL_STRAT = "http://47.93.6.6:9090";//9090  开发测试环境地址
  public static final String BASE_URL_STRAT = "http://120.55.174.179:8070";
  //    public static final String BASE_URL_STRAT = "http://10.2.11.174:8090";
  public static final String BASE_URL = BASE_URL_STRAT + "/quickloan";

  public static final String REQUEST_URL = BASE_URL + "/jsonHttpServlet";
  public static final String LOGIN_URL = "http://test.rskj99.com/rskj-core-api/member/login";
  public static final String SHAREDPREFERENCES_NAME = "xm_preference";
  /**
   * 渠道标识
   */
  public static final String CHANNEL_NO = "3";
  /**
   * 获取验证码接口标识
   */
  public static final String TRANS_CODE_YZM = "M000010";
  /**
   * 客户身份证正面照片上传
   */
  public static final String TRANS_CODE_UPLOAD_FRONT_PICTURE = "M120003";
  /**
   * 客户身份证fan面照片上传
   */
  public static final String TRANS_CODE_UPLOAD_BACK_PICTURE = "M120004";
  /**
   * 身份信息更新接口
   */
  public static final String TRANS_CODE_M100122 = "M100122";
  /**
   * 客户基本信息上传接口标识
   */
  public static final String TRANS_CODE_UPLOAD_BASIC_INFO = "M010310";
  /**
   * 客户通讯录信息上传接口标识
   */
  public static final String TRANS_CODE_UPLOAD_ALL_CONTANTS_INFO = "S000010";
  /**
   * 查询H5URL接口
   */
  public static final String TRANS_CODE_QUERY_H5_URL = "S300002";
  /**
   * 判断是否为清远农商的银行卡接口
   */
  public static final String TRANS_CODE__CHECK_BANKCARD_IS_QINGYUAN = "S200005";
  /**
   * 活体检测接口
   */
  public static final String TRANS_CODE_FACE_CHECK = "M100107";
  /**
   * 银行卡号验证三接口
   */
  public static final String TRANS_CODE_BANK_CARD_CHECK_THREE = "M100109";
}
