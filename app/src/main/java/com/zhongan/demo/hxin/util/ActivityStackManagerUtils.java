package com.zhongan.demo.hxin.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;
import java.util.Stack;

/** 
* @ClassName ActivityManager 
* @Description 应用程序Activity管理类
* @author Kay linkai.pro@gmail.com 
* @date 2014-8-5 上午9:34:19 
*  
*/ 
public class ActivityStackManagerUtils {
	
	private static Stack<Activity> sActivitiesStack;
	private static ActivityStackManagerUtils sInstance;
	
	private ActivityStackManagerUtils(){}
	/** 
	* @Description 单例
	*/ 
	public static ActivityStackManagerUtils getInstance(){
		if(sInstance == null){
			sInstance = new ActivityStackManagerUtils();
		}
		return sInstance;
	}
	/** 
	* @Description Activity入栈
	* @param Activity   
	*/ 
	public void addActivity(Activity activity){
		if(sActivitiesStack == null){
			sActivitiesStack = new Stack<Activity>();
		}
		sActivitiesStack.add(activity);
	}
	/** 
	* @Description 获取当前Activity(最后一个入栈的)
	*/ 
	public Activity getCurrentActivity(){
		Activity activity = sActivitiesStack.lastElement();
		return activity;
	}
	/** 
	* @Description 结束当前Activity(最后一个入栈的)
	*/ 
	public void finishCurrentActivity(){
		Activity activity = sActivitiesStack.lastElement();
		finishActivity(activity);
	}
	/** 
	* @Description 结束指定Activity
	* @param Activity   
	*/ 
	public void finishActivity(Activity activity){
		if(activity != null){
			sActivitiesStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}
	/** 
	* @Description 结束指定类名的Activity
	* @param cls   
	*/ 
	public void finishActivity(Class<?> cls){
		for (Activity activity : sActivitiesStack) {
			if(activity.getClass().equals(cls)){
				finishActivity(activity);
			}
		}
	}
	
	
	/** 
	* @Description 结束所有Activity
	*/ 
	public void finishAllActivity(){
		if(sActivitiesStack!=null)
		{
		  for (int i = 0; i < sActivitiesStack.size(); i++) {
			if(sActivitiesStack.get(i) != null){
				sActivitiesStack.get(i).finish();
			}
		  }
		  sActivitiesStack.clear();
		}
	}
	/** 
	* @Description 退出应用程序
	* @param context   
	*/ 
	public void ExitApplication(Context context){
		try {
			finishAllActivity();
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityManager.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/** 
	* @Description 判断当前程序是否在前台运行
	* @param @param context
	* @return boolean    
	*/ 
	public boolean isRunningForeground(Context context){
        String packageName=context.getPackageName();
        String topActivityClassName=getTopActivityName(context);
        if (packageName!=null&&topActivityClassName!=null&&topActivityClassName.startsWith(packageName)) {
            return true;
        } else {
            return false;
        }
    }
	private String getTopActivityName(Context context){
        String topActivityClassName=null;
         ActivityManager activityManager =
        (ActivityManager)(context.getSystemService(android.content.Context.ACTIVITY_SERVICE )) ;
         List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1) ;
         if(runningTaskInfos != null){
             ComponentName f=runningTaskInfos.get(0).topActivity;
             topActivityClassName=f.getClassName();
         }
         return topActivityClassName;
    }
}
