package com.zhongan.demo.hxin.util;

import android.content.Context;
import android.content.SharedPreferences;

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
    private final String TOKEN = "clientToken";
    private final String cropImagePath = "cropImagePath";
    private final String deviceId = "deviceId";
    private final String canUsedSum = "canUsedSum";
    private final String userStateInfo = "userStateInfo";
    private final String isFirstLoanMoney = "isFirstLoanMoney";
    private final String idCardNum = "idCardNum";


    public SharedPreferenceUtils(Context context){
        this.mContext = context;
        create();
    }

    private void create(){
        if(sharedPreferences == null || editor == null){
            sharedPreferences = mContext.getSharedPreferences(Contants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
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
    
    
    public String getDeviceId(){
    	return sharedPreferences.getString(deviceId,"");
    }

    
    public void setDeviceId(String data){
        editor.putString(deviceId, data);
        editor.commit();
    }

    /**
     * 保存token
     */
    public void setToken(String data){
        editor.putString(TOKEN, data);
        editor.commit();
    }


    public String getToken(){
        return sharedPreferences.getString(TOKEN,"");
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

}
