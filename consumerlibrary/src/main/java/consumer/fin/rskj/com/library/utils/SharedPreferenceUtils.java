package consumer.fin.rskj.com.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

import consumer.fin.rskj.com.library.activitys.BaseActivity;

/**
 * Created by gxj on 2017/04/22.
 */
public class SharedPreferenceUtils {

    private Context mContext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String isLogin = "isLogin";
    private String loginMobile = "loginMobile";

    private final String firstUse = "firstUse";//是否是第一次使用
    private final String TOKEN = "token";//用户Token
  private final String MENBERID = "memberId";
    private final String cropImagePath = "cropImagePath";
    private final String deviceId = "deviceId";//设备Id
    private final String canUsedSum = "canUsedSum";//可借金额
    private final String userStateInfo = "userStateInfo";//用户状态标识
    private final String isFirstLoanMoney = "isFirstLoanMoney";
    private final String idCardNum = "idCardNum";//身份证号
    private final String merchantId = "merchantId";//商户id
    private final String merchantName = "merchantName";//商户名称
    private final String idcardAddress = "idcardAddress";// 地址
    private final String idcardBirthday = "idcardBirthday";// 生日
    private final String idcardName = "idcardName";// 姓名
    private final String idcardPeople = "idcardPeople";// 民族
    private final String idcardSex = "idcardSex";// 性别
    private final String idcardType = "idcardType";// 证件类型
    private final String issueAuthority = "issueAuthority";// 签发单位
    private final String idcardValidity = "idcardValidity";// 身份证有效期
    private final String bankCardNumber = "bankCardNumber";//卡号
    private final String custCardFront = "custCardFront";//身份证正面照
    private final String custCardVerso = "custCardVerso";//身份证反面照

    public SharedPreferenceUtils(Context context) {
        this.mContext = context;
        create();
    }

    private void create() {
        if (sharedPreferences == null || editor == null) {
            sharedPreferences = mContext.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }


    public void setIdcardName(String data) {
        editor.putString(idcardName, data);
        editor.commit();
    }

    public String getIdcardName() {
        return sharedPreferences.getString(idcardName, "");
    }

    public void setIdCardNum(String data) {
        editor.putString(idCardNum, data);
        editor.commit();
    }

    public String getIdCardNum() {
        return sharedPreferences.getString(idCardNum, "");
    }

    public void setIdcardPeople(String data) {
        editor.putString(idcardPeople, data);
        editor.commit();
    }

    public String getIdcardPeople() {
        return sharedPreferences.getString(idcardPeople, "");
    }

    public void setIdcardSex(String data) {
        editor.putString(idcardSex, data);
        editor.commit();
    }

    public String getIdcardSex() {
        return sharedPreferences.getString(idcardSex, "");
    }

    public void setIdcardAddress(String data) {
        editor.putString(idcardAddress, data);
        editor.commit();
    }

    public String getIdcardAddress() {
        return sharedPreferences.getString(idcardAddress, "");
    }

    public void setIdcardType(String data) {
        editor.putString(idcardType, data);
        editor.commit();
    }

    public String getIdcardType() {
        return sharedPreferences.getString(idcardType, "");
    }

    public void setIdcardValidity(String data) {
        editor.putString(idcardValidity, data);
        editor.commit();
    }

    public String getIdcardValidity() {
        return sharedPreferences.getString(idcardValidity, "");
    }

    public void setIdcardBirthday(String data) {
        editor.putString(idcardBirthday, data);
        editor.commit();
    }

    public String getIdcardBirthday() {
        return sharedPreferences.getString(idcardBirthday, "");
    }

    public boolean isFirstLoanMoney() {
        return sharedPreferences.getBoolean(isFirstLoanMoney, true);
    }

    public void setIssueAuthority(String data) {
        editor.putString(issueAuthority, data);
        editor.commit();
    }

    public String getIssueAuthority() {
        return sharedPreferences.getString(issueAuthority, "");
    }

    public void setBankCardNumber(String data) {
        editor.putString(bankCardNumber, data);
        editor.commit();
    }

    public String getBankCardNumber() {
        return sharedPreferences.getString(bankCardNumber, "");
    }

    public void setCustCardFront(String data) {
        editor.putString(custCardFront, data);
        editor.commit();
    }

    public String getCustCardFront() {
        return sharedPreferences.getString(custCardFront, "");
    }

    public void setCustCardVerso(String data) {
        editor.putString(custCardVerso, data);
        editor.commit();
    }

    public String getCustCardVerso() {
        return sharedPreferences.getString(custCardVerso, "");
    }

    public void setIsFirstLoanMoney(boolean data) {
        editor.putBoolean(isFirstLoanMoney, data);
        editor.commit();
    }


    public String getUserStateInfo() {
        return sharedPreferences.getString(userStateInfo, "0");
    }


    public void setUserStateInfo(String data) {
        editor.putString(userStateInfo, data);
        editor.commit();
    }

    public String getCanUsedSum() {
        return sharedPreferences.getString(canUsedSum, "0");
    }


    public void setCanUsedSum(String data) {
        editor.putString(canUsedSum, data);
        editor.commit();
    }


    public String getCropImagePath() {
        return sharedPreferences.getString(cropImagePath, "");
    }


    public void setCropImagePath(String data) {
        editor.putString(cropImagePath, data);
        editor.commit();
    }

    public String getMerchantId() {
        return sharedPreferences.getString(merchantId, "");
    }

    public void setMerchantId(String data) {
        editor.putString(merchantId, data);
        editor.commit();
    }
    public String getMerchantName() {
        return sharedPreferences.getString(merchantName, "");
    }

    public void setMerchantName(String data) {
        editor.putString(merchantName, data);
        editor.commit();
    }
    public String getPhone() {
        return sharedPreferences.getString(loginMobile, "");
    }


    public void setPhone(String data) {
        editor.putString(loginMobile, data);
        editor.commit();
    }


    public String getDeviceId() {
        return sharedPreferences.getString(deviceId, "");
    }


    public void setReplenisherPages(String data){
        editor.putString("replenisherPages", data);
        editor.commit();

    }

    public String getReplenisherPages() {
        return sharedPreferences.getString("replenisherPages", "");
    }

    public void setDeviceId(String data) {
        editor.putString(deviceId, data);
        editor.commit();
    }

    /**
     * 保存token
     */
    public void setToken(String data) {
        editor.putString(TOKEN, data);
        editor.commit();
    }


    public String getToken() {
        return sharedPreferences.getString(TOKEN, "");
    }

  /**
   * 保存memid
   */
  public void setMemId(String data) {
    editor.putString(MENBERID, data);
    editor.commit();
  }

  public String getMemId() {
    return sharedPreferences.getString(MENBERID, "");
  }

  public void setXJProductId(String data) {
        editor.putString("XJProductId", data);
        editor.commit();
    }


    public String getXJProductId() {
        return sharedPreferences.getString("XJProductId", "");
    }


    public void setXJFundId(String data) {
        editor.putString("XJFundId", data);
        editor.commit();
    }

    public String getXJFundId() {
        return sharedPreferences.getString("XJFundId", "");
    }

    /**
     * 设置不是第一次使用这个app
     */
    public void setFirstUse(boolean isFirst) {
        editor.putBoolean(firstUse, isFirst);
        editor.commit();
    }


    public boolean getFirstUse() {
        return sharedPreferences.getBoolean(firstUse, true);
    }

    public void setLogin(boolean login) {
        editor.putBoolean(isLogin, login);
        editor.commit();
    }


    public boolean iSLogin() {
        return sharedPreferences.getBoolean(isLogin, false);
    }

    /**
     * 保存身份信息验证时间
     */
    public void setTimeIdcard(Long time) {
        editor.putLong("idCard", time);
        editor.commit();
    }
    public Long getTimeIdcard(){
        return sharedPreferences.getLong("idCard", 0);
    }

  /**
     * 保存身份信息验证当前系统时间
     */
    public void setTimeIdcardSys(Long time) {
        editor.putLong("idCardSys", time);
        editor.commit();
    }
    public Long getTimeIdcardSys(){
        return sharedPreferences.getLong("idCardSys", 0);
    }

    /**
     * 保存确认借款时间
     */
    public void setTimeLoan(Long time) {
        editor.putLong("Loan", time);
        editor.commit();
    }
    public Long getTimeLoan(){
        return sharedPreferences.getLong("Loan", 0);
    }
    /**
     * 保存确认借款当前系统时间
     */
    public void setTimeLoanSys(Long time) {
        editor.putLong("LoanSys", time);
        editor.commit();
    }
    public Long getTimeLoanSys(){
        return sharedPreferences.getLong("LoanSys", 0);
    }

    /**
     * 保存确认借款页面userId
     **/
    public void setUserId(String userId) {
        editor.putString("userId",userId);
        editor.commit();
    }
    public String getUserId(){
        return sharedPreferences.getString("userId","");
    }

    /**
     * 保存确认借款页面projectCode
     **/
    public void setProjectCode(String projectCode) {
        editor.putString("projectCode",projectCode);
        editor.commit();
    }
    public String getProjectCode(){
        return sharedPreferences.getString("projectCode","");
    }


    public void setSessionID(String sessionId){
        editor.putString("SessionID",sessionId);
        editor.commit();
    }

    public String getSessionID(){
        return sharedPreferences.getString("SessionID","");
    }



    public String getCustName() {
        return sharedPreferences.getString("modifyName", "");
    }


    public void setCustName(String data) {
        editor.putString("modifyName", data);
        editor.commit();
    }


    //还款成功
    public String getPaymentStatus() {
        return sharedPreferences.getString("paymentStatus", "");
    }


    public void setPaymentStatus(String data) {
        editor.putString("paymentStatus", data);
        editor.commit();
    }


    //放款中
    public String getPayingStatus() {
        return sharedPreferences.getString("payingStatus", "");
    }


    public void setPayingStatus(String data) {
        editor.putString("payingStatus", data);
        editor.commit();
    }


    //授信审核
    public String getCedStatus() {
        return sharedPreferences.getString("cedStatus", "");
    }


    public void setCedStatus(String data) {
        editor.putString("cedStatus", data);
        editor.commit();
    }


    public String getHelpCenter() {
        return sharedPreferences.getString("helpCenter", "");
    }


    public void setHelpCenter(String data) {
        editor.putString("helpCenter", data);
        editor.commit();
    }


    public String getUserFeedbacks() {
        return sharedPreferences.getString("userFeedbacks", "");
    }


    public void setUserFeedbacks(String data) {
        editor.putString("userFeedbacks", data);
        editor.commit();
    }

    public String getProtocolCenter() {
        return sharedPreferences.getString("protocolCenter", "");
    }


    public void setProtocolCenter(String data) {
        editor.putString("protocolCenter", data);
        editor.commit();
    }



    public String getLoanStatus() {
        return sharedPreferences.getString("loanStatus", "");
    }


    public void setLoanStatus(String data) {
        editor.putString("loanStatus", data);
        editor.commit();
    }



    public void setApplyLoan(String data) {
        editor.putString("applyLoan", data);
        editor.commit();
    }

    public String getApplyLoan() {
        return sharedPreferences.getString("applyLoan", "");
    }


    public void setFirstUrl(String firstUrl) {
        editor.putString("firstUrl", firstUrl);
        editor.commit();
    }


    public String getFirstUrl() {
        return sharedPreferences.getString("firstUrl", "");
    }


    public void setRows(String contractList) {
        editor.putString("contractList", contractList);
        editor.commit();
    }


    public String getRows() {
        return sharedPreferences.getString("contractList", "");
    }


    public void setId15(String data) {
        editor.putString("id15", data);
        editor.commit();
    }

    public String getId15() {
        return sharedPreferences.getString("id15", "");
    }


    public void setFundId15(String data) {
        editor.putString("fundId", data);
        editor.commit();
    }

    public String getFundId15() {
        return sharedPreferences.getString("fundId", "");
    }


    public void setTokenKey(String data){
        editor.putString("tokenKey", data);
        editor.commit();
    }

    public String getTokenKey(){
        return sharedPreferences.getString("tokenKey","000000");
    }

}
