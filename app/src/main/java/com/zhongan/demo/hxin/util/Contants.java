package com.zhongan.demo.hxin.util;

import android.os.Environment;

public class Contants {
	// face++
	public static String cacheText = "livenessDemo_text";
	public static String cacheImage = "livenessDemo_image";
	public static String cacheVideo = "livenessDemo_video";
	public static String cacheCampareImage = "livenessDemo_campareimage";
	public static String dirName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/faceapp";

	//	public static final String BASE_URL="http://47.93.6.6:9090/quickloan";// 8090 测试环境
	//	public static final String BASE_URL="http://120.55.174.179:8070/quickloan";// 8070 测试环境
	public static final String BASE_URL = "http://quick.rskj99.com/quickloan";//生产环境

	public static final String REQUEST_URL = BASE_URL + "/jsonHttpServlet";

	public static final String SHAREDPREFERENCES_NAME = "rongshu_pref";
	public static final String CHANNEL_NO = "3";// 渠道标识

	public static final String BRANCH_NO = "00001";// 机构id
	public static final String LEGAL_PERNUMBER = "00001";// 法人编号

	public static final String TRANS_CODE_REGISTER = "M100101";// 注册入口接口标识
	public static final String TRANS_CODE_YZM = "M000010";// 获取验证码接口标识
	public static final String TRANS_CODE_LOGIN = "S200004";// 登录入口接口标识
	public static final String TRANS_CODE_CHECK_YZM = "M000008";// 验证短信验证码接口
	public static final String TRANS_CODE_MIX_PASSWORD = "M000003";// 密码加密接口标识
	public static final String TRANS_CODE_SET_PAY_PASSWORD = "M000158";// 设置支付密码接口接口标识
	public static final String TRANS_CODE_SET_WORK = "M000159";// 设置工作流接口接口标识
	public static final String TRANS_CODE_UPLOAD_PICTURE = "M100105";// 客户身份证照片上传接口接口标识

	public static final String TRANS_CODE_UPLOAD_CUST_INFO = "M010307";// 单一客户保存移动端

	public static final String TRANS_CODE_QUERY_LOAN_ACOOUNT_AMOUNT_INFO = "M100402";// 获取账户余额接口

	public static final String TRANS_CODE_QUERY_LOAN_AMOUNT_INFO = "M100603";// 获取额度接口(用户贷款金额统计查询)

	public static final String TRANS_CODE_FACE_CHECK = "M100107";// 活体检测接口

	public static final String TRANS_CODE_BANK_CARD_CHECK = "M100106";// 银行卡号验证四接口

	public static final String TRANS_CODE_DO_PAY_TYPE = "M100703";// 还款计划试算（还款方式：等额本金）接口

	public static final String TRANS_CODE_DO_LOAN_APPLY = "M090901";// 贷款业务申请接口

	public static final String TRANS_CODE_LOAN_RECORD_DETAIL = "M100300";// 借款借据接口

	public static final String TRANS_CODE_GET_LOAN_RECORD_LIST = "M100602";// 借款列表接口

	public static final String TRANS_CODE_GET_LOAN_RECORD_DETAIL_LIST = "M100705";// 借款详情列表接口

	public static final String TRANS_CODE_COMMON_PAY_MONEY = "M100604";// 借款详情列表正常还款试算接口
	public static final String TRANS_CODE_SUBMIT_COMMON_PAY_MONEY = "M100702";// 借款详情列表正常还款提交接口

	public static final String TRANS_CODE_AHEAD_PAY_MONEY = "M100712";// 借款详情列表提前还款试算接口
	public static final String TRANS_CODE_SUBMIT_AHEAD_PAY_MONEY = "M100710";// 借款详情列表提前还款提交接口

	public static final String TRANS_CODE_SUBMIT_DELAY_PAY_MONEY = "M100801";// 借款详情列表逾期还款提交接口

	public static final String TRANS_CODE_QUERY_LOAN_PAY_MONEY_FLAG = "M100133";// 借还款标志接口

	public static final String TRANS_CODE_QUERY_IDCARD_INFO = "M100131";// 身份证查询接口

	public static final String TRANS_CODE_QUERY_BANK_LIST_INFO = "M100132";// 银行卡列表查询接口
	public static final String TRANS_CODE_SET_DEFAULT_BANK_CARD = "M100134";// 修改银行卡为主还款卡接口

	public static final String TRANS_CODE_QUERY_PRODUCT_INFO = "M200024";// 产品信息查询接口

	public static final String TRANS_CODE_QUERY_PRODUCT_INFO_LIST = "M200023";// 产品信息列表查询接口

	public static final String TRANS_CODE_QUERY_PAY_RECORD_LIST = "M100704";// 查询还款记录
	public static final String TRANS_CODE_QUERY_LINES_URL = "M000139";// 协议查询
	public static final String TRANS_CODE_LOGOUT = "M000002";// 登出接口

	public static final int MSG_M100101_SUCCESS = 0x10000;// 调用M100101接口成功
	public static final int MSG_M000008_SUCCESS = 0x10001;// 调用M000008接口成功
	public static final int MSG_M000003_SUCCESS = 0x10002;// 调用M000003接口成功
	public static final int MSG_M000003_FAILURE = 0x10003;// 调用M000003接口失败

	public static final int MSG_GET_YZM_CODE_SUCCESS = 0x10004;// 获取验证码成功
	public static final int MSG_GET_YZM_CODE_FAILURE = 0x10005;// 获取验证码失败

	public static final int MSG_REGISTER_SUCCESS = 0x10006;// 注册成功
	public static final int MSG_REGISTER_FAILURE = 0x10007;// 注册失败

	public static final int MSG_LOGIN_SUCCESS = 0x10008;// 登录成功
	public static final int MSG_LOGIN_FAILURE = 0x10009;// 登录失败

	public static final int MSG_UPLOAD_PICTURE_SUCCESS = 0x10014;// 上传图片成功
	public static final int MSG_UPLOAD_PICTURE_FAILURE = 0x10015;// 上传图片失败

	public static final int MSG_UPLOAD_CUST_INFO_SUCCESS = 0x10016;// 上传客户信息成功
	public static final int MSG_UPLOAD_CUST_INFO_FAILURE = 0x10017;// 上传客户信息失败

	public static final int MSG_GET_PAYTYPE_DATA_SUCCESS = 0x10018;// 获取还款计划成功
	public static final int MSG_GET_PAYTYPE_DATA_FAILURE = 0x10019;// 获取还款计划失败

	public static final int MSG_DO_LOAN_APPLY_SUCCESS = 0x10020;// 贷款业务申请成功
	public static final int MSG_DO_LOAN_APPLY_FAILURE = 0x10021;// 贷款业务申请失败

	public static final int MSG_SET_JIAOYI_PWD_SUCCESS = 0x10022;// 设置交易密码成功
	public static final int MSG_SET_JIAOYI_PWD_FAILURE = 0x10023;// 设置交易密码失败

	public static final int MSG_QUERY_LOAN_AMOUNT_INFO_SUCCESS = 0x10024;// 获取额度成功
	public static final int MSG_QUERY_LOAN_AMOUNT_INFO_FAILURE = 0x10025;// 获取额度失败

	public static final int MSG_CHECK_BNAK_CARD_INFO_SUCCESS = 0x10026;// 设置银行卡信息成功
	public static final int MSG_CHECK_BNAK_CARD_INFO_FAILURE = 0x10027;// 设置银行卡信息失败

	public static final int MSG_GET_LOAN_LOAN_RECORD_INFO_SUCCESS = 0x10028;// 获取借款借据信息成功
	public static final int MSG_GET_LOAN_LOAN_RECORD_INFO_FAILURE = 0x10029;// 获取借款借据信息失败

	public static final int MSG_LOGOUT_SUCCESS = 0x10028;// 退出登录成功
	public static final int MSG_LOGOUT_FAILURE = 0x10029;// 退出登录失败

	public static final int MSG_REFRESH_DATA_SUCCESS = 0x10030;// 刷新数据完成
	public static final int MSG_LOADE_DATA_FAILURE = 0x10031;// 获取数据失败
	public static final int MSG_LOADE_DATA_SUCCESS = 0x10032;// 加载数据完成

	public static final int MSG_LOAN_MONEY_DETAIL_LIST_SUCCESS = 0x10033;// 获取借款详情列表成功
	public static final int MSG_LOAN_MONEY_DETAIL_LIST_FAILURE = 0x10034;// 获取借款详情列表失败

	public static final int MSG_DO_COMMON_PAY_MONEY_SUCCESS = 0x10035;// 正常还款试算成功
	public static final int MSG_DO_COMMON_PAY_MONEY_FAILURE = 0x10036;// 正常还款试算失败

	public static final int MSG_DO_SUBMIT_COMMON_PAY_MONEY_SUCCESS = 0x10037;// 正常还款提交成功
	public static final int MSG_DO_SUBMIT_COMMON_PAY_MONEY_FAILURE = 0x10038;// 正常还款提交失败

	public static final int MSG_DO_AHEAD_PAY_MONEY_SUCCESS = 0x10039;// 提前还款试算成功
	public static final int MSG_DO_AHEAD_PAY_MONEY_FAILURE = 0x10040;// 提前还款试算失败

	public static final int MSG_DO_SUBMIT_AHEAD_PAY_MONEY_SUCCESS = 0x10041;// 提前还款提交成功
	public static final int MSG_DO_SUBMIT_AHEAD_PAY_MONEY_FAILURE = 0x10042;// 提前还款提交失败

	public static final int MSG_DO_SUBMIT_DELAY_PAY_MONEY_SUCCESS = 0x10043;// 逾期还款提交成功
	public static final int MSG_DO_SUBMIT_DELAY_PAY_MONEY_FAILURE = 0x10044;// 逾期还款提交失败

	public static final int MSG_DO_CHECK_PWD_DIFFRIENT_FAILURE = 0x10045;// 密码输入不一致

	public static final int MSG_DO_QUERY_LOAN_PAY_MONEY_FLAG_SUCCESS = 0x10046;// 借还款标志查询成功
	public static final int MSG_DO_QUERY_LOAN_PAY_MONEY_FLAG_FAILURE = 0x10047;// 借还款标志查询失败

	public static final int MSG_DO_QUERY_IDCARD_INFO_SUCCESS = 0x10048;// 身份证信息查询成功
	public static final int MSG_DO_QUERY_IDCARD_INFO_FAILURE = 0x10049;// 身份证信息查询失败

	public static final int MSG_DO_QUERY_BANK_LIST_INFO_SUCCESS = 0x10050;// 银行卡列表查询成功
	public static final int MSG_DO_QUERY_BANK_LIST_INFO_FAILURE = 0x10051;// 银行卡列表查询失败

	public static final int MSG_DO_SET_DEFAULT_BANK_CARD_SUCCESS = 0x10052;// 修改银行卡为主还款卡成功
	public static final int MSG_DO_SET_DEFAULT_BANK_CARD_FAILURE = 0x10053;// 修改银行卡为主还款卡失败

	public static final int MSG_DO_QUERY_PRODUCT_INFO_SUCCESS = 0x10054;// 产品信息查询成功
	public static final int MSG_DO_QUERY_PRODUCT_INFO_FAILURE = 0x10055;// 产品信息查询失败

	public static final int MSG_GET_LOAN_MONEY_NUM_SUCCESS = 0x10056;// 金额滑动成功

	public static final int MSG_DO_QUERY_PRODUCT_INFO_LIST_SUCCESS = 0x10057;// 产品信息列表查询成功
	public static final int MSG_DO_QUERY_PRODUCT_INFO_LIST_FAILURE = 0x10058;// 产品信息列表查询失败

	// 修改密码
	public static final String CHANGE_CODE_PWD = "M000156";// 修改密码
	public static final String CHANGE_CODE_LOGIN_PWD = "M000011";// 修改登录密码
	public static final String IDCARD_CODE_PAY_PWD = "M000013";// 验证身份证
	public static final String FORGET_CODE_PAY_PWD = "M000014";// 忘记支付密码

}
