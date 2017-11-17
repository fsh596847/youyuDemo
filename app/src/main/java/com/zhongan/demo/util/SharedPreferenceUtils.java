package com.zhongan.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by gxj on 2016/6/24.
 */
public class SharedPreferenceUtils {

    private Context mContext;
    private String sp_name = "xm_preference";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String isLogin = "isLogin";

    private final String firstUse = "firstUse";//是否是第一次使用
    private final String userPhone = "userPhone";
    private final String MENBERID = "memberId";
    private final String TOKEN = "token";
    private final String userStateInfo = "userStateInfo";
    private final String idCardNum = "idCardNum";
    private final String isFirstLoanMoney = "isFirstLoanMoney";
    private final String canUsedSum = "canUsedSum";
    private final String cropImagePath = "cropImagePath";
    private String loginMobile = "loginMobile";
    private final String deviceId = "deviceId";


    public SharedPreferenceUtils(Context context){
        this.mContext = context;
        create();
    }

    private void create(){
        if(sharedPreferences == null || editor == null){
            sharedPreferences = mContext.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }


    public String getDeviceId(){
        return sharedPreferences.getString(deviceId,"");
    }


    public void setDeviceId(String data){
        editor.putString(deviceId, data);
        editor.commit();
    }


    /**
     * 保存memid
     */
    public void setMemId(String data){
        editor.putString(MENBERID, data);
        editor.commit();
    }


    public String getMemId(){
        return sharedPreferences.getString(MENBERID,"");
    }


    /**
     * 保存token
     */
    public void setToken(String data){
        editor.putString(TOKEN, data);
        editor.commit();
    }

    public void setIdCardNum(String data){
        editor.putString(idCardNum, data);
        editor.commit();
    }

    public String getIdCardNum(){
        return sharedPreferences.getString(idCardNum,"");
    }



    public boolean isFirstLoanMoney(){
        return sharedPreferences.getBoolean(isFirstLoanMoney,true);
    }


    public void setIsFirstLoanMoney(boolean data){
        editor.putBoolean(isFirstLoanMoney, data);
        editor.commit();
    }



    public String getUserStateInfo(){
        return sharedPreferences.getString(userStateInfo,"0");
    }


    public void setUserStateInfo(String data){
        editor.putString(userStateInfo, data);
        editor.commit();
    }

    public String getCanUsedSum(){
        return sharedPreferences.getString(canUsedSum,"0");
    }


    public void setCanUsedSum(String data){
        editor.putString(canUsedSum, data);
        editor.commit();
    }


    public String getCropImagePath(){
        return sharedPreferences.getString(cropImagePath,"");
    }


    public void setCropImagePath(String data){
        editor.putString(cropImagePath, data);
        editor.commit();
    }


    public String getPhone(){
        return sharedPreferences.getString(loginMobile,"");
    }


    public void setPhone(String data){
        editor.putString(loginMobile, data);
        editor.commit();
    }


    public String getToken(){
        return sharedPreferences.getString(TOKEN,"");
    }


    public void setUserName(String data){
        editor.putString("userName", data);
        editor.commit();
    }


    public String getUserName(){
        return sharedPreferences.getString("userName","");
    }


    /**
     * 设置不是第一次使用这个app
     */
    public void setFirstUse(boolean isFirst){
        editor.putBoolean(firstUse, isFirst);
        editor.commit();
    }


    public boolean getFirstUse(){
        return sharedPreferences.getBoolean(firstUse,true);
    }

    public void setLogin(boolean login){
        editor.putBoolean(isLogin, login);
        editor.commit();
    }


    public boolean iSLogin(){
        return sharedPreferences.getBoolean(isLogin,false);
    }




    public void setUserId(String data){
        editor.putString("userId", data);
        editor.commit();
    }

    public String getUserId(){
        return sharedPreferences.getString("userId","");
    }


    public void setCustNum(String data){
        editor.putString("custNum", data);
        editor.commit();
    }

    public String getCustNum(){
        return sharedPreferences.getString("custNum","");
    }


    public void setProductId(String data){
        editor.putString("productId", data);
        editor.commit();
    }

    public String getProductId(){
        return sharedPreferences.getString("productId","");
    }


    public void setIdentity(String data){
        editor.putString("identity", data);
        editor.commit();
    }

    public String getIdentity(){
        return sharedPreferences.getString("identity","");
    }



    public void setTokenKey(String data){
        editor.putString("tokenKey", data);
        editor.commit();
    }

    public String getTokenKey(){
        return sharedPreferences.getString("tokenKey","000000");
    }



    public void setShowTab(boolean showTab){
        editor.putBoolean("showTab", showTab);
        editor.commit();
    }

    public boolean isShowTab(){
        return sharedPreferences.getBoolean("showTab",false);
    }


    //------------------------------------------------


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


    public void setSessionID(String sessionId){
        editor.putString("SessionID",sessionId);
        editor.commit();
    }

    public String getSessionID(){
        return sharedPreferences.getString("SessionID","");
    }


    public void setDownLoadPath(String downLoadPath){
        editor.putString("downLoadPath",downLoadPath);
        editor.commit();
    }

    public String getDownLoadPath(){
        return sharedPreferences.getString("downLoadPath","");
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
}
