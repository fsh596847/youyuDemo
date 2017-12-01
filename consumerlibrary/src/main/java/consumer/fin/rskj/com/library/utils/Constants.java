package consumer.fin.rskj.com.library.utils;

import android.os.Environment;

/**
 * 常量
 */

public class Constants {
  public static final int TOAST_SHOW_POSITION = 1;//Toast 显示位置：0：顶部 1：中间 2：底部

  public static final String LEGALPER_NUM = "00001";
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

  //public static final String BASE_URL_STRAT = "http://47.93.6.6:9090";//9090  开发测试环境地址
  public static final String BASE_URL_STRAT = "http://120.55.174.179:8070";
  //public static final String BASE_URL_STRAT = "http://10.2.11.174:8090";
  public static final String BASE_URL = BASE_URL_STRAT + "/quickloan";
  public static String BASE_LOGIN_URL = "http://test.rskj99.com/rskj-core-api";//测试
  public static final String REQUEST_URL = BASE_URL + "/jsonHttpServlet";
  public static final String LOGIN_URL = "member/login";
  /**
   * 立即借款
   */
  public static final String FOUNDID_URL = "/quickloan/jsonHttpServlet";
  /**
   * 注册
   */
  public static final String HTTP_REGIST = "member/regist";
  /**
   * 注册验证码
   */
  public static final String HTTP_REGISTER_CODE = "verificacode/createtext/regist/";
  /**
   * 設置密碼验证码
   */
  public static final String HTTP_PASSWORD_CODE = "verificacode/createtext/password/";
  /**
   * 忘记密码
   */
  public static final String HTTP_FORGET_PASSWORD = "member/password";
  public static final String SHAREDPREFERENCES_NAME = "xm_preference";
  /**
   * 渠道标识
   */
  public static final String CHANNEL_NO = "3";
  public static final String PROJECTTYPE = "suixindai2.0";
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
  /**
   * 银行卡验证4接口
   */
  public static final String TRANS_CODE_M100106 = "M100106";
  /**
   * 获取支持银行卡列表
   */
  public static final String TRANS_CODE_M108104 = "M108104";

  /**
   * 客户还款列表
   */
  public static final String TRANS_CODE_M100607 = "M100607";
  /**
   * 客户借款列表
   */
  public static final String TRANS_CODE_M100602 = "M100602";
  /**
   * 客户借款列表
   */
  public static final String TRANS_CODE_M100724 = "M100724";
  /**
   * 支付密码验证
   */
  public static final String TRANS_CODE_M000192 = "M000192";
  /**
   * 全额还款
   */
  public static final String TRANS_CODE_M100710 = "M100710";
  /**
   * 全额还款
   */
  public static final String TRANS_CODE_M100800 = "M100800";
  /**
   * 资金方产品进件信息  进件页面
   */
  public static final String TRANS_CODE_M000191 = "M000191";
  /**
   * 短信验证码验证
   */
  public static final String TRANS_CODE_M000015 = "M000015";
  public static final String TRANS_CODE_CFCA0003 = "CFCA0003";
  /**
   * 获取短信验证码接口
   */
  public static final String TRANS_CODE_M090904 = "M090904";

  /**
   * 生产借款借据
   */
  public static final String TRANS_CODE_M100300 = "M100300";
  /**
   * 借款确认接口
   */
  public static final String TRANS_CODE_M090902 = "M090902";
  /**
   * 全额还款试算
   */
  public static final String TRANS_CODE_M100712 = "M100712";
  /**
   * 贷款简略信息 新增
   */
  public static final String TRANS_CODE_M100721 = "M100721";
  /**
   * 贷款应还款信息查询
   */
  public static final String TRANS_CODE_M100705 = "M100705";
  /**
   * 贷款应还款信息查询
   */
  public static final String TRANS_CODE_M110701 = "M110701";
  /**
   * 本月账单接口
   */
  public static final String TRANS_CODE_M100611 = "M100611";
  /**
   * 本月账单接口
   */
  public static final String TRANS_CODE_MM100132 = "M100132";
  /**
   * 获取页面连接
   */
  public static final String TRANS_CODE_M107102 = "M107102";
}
