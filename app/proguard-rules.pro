# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/dfqin/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep interface com.zhongan.finance.model.UnMix
-keep public class * implements com.zhongan.finance.model.UnMix { *; }
-keep class * implements java.io.Serializable { *;}

#okhttp
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault

#保护注解
#忽略警告
-ignorewarning
-keepattributes *Annotation*
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
-keep class io.realm.**{*;}
-keep class zhongan.com.idbankcard.idcard.Util{*;}
-keep class zhongan.com.idbankcard.bankcard.util.Util{*;}
-keep class zhongan.com.sdkManager.ZAIDBankCardSDKManager{*;}
-keep class zhongan.com.idbankcard.idcard.IDCardScanActivity{*;}
-keep class zhongan.com.idbankcard.idcard.OCRCallBack{*;}
-keep class zhongan.com.idbankcard.bankcard.BankCardScanActivity{*;}
-keep class com.zhongan.sdkauthcheck.model.**{*;}
-keep class zhongan.com.idbankcard.idcard.model.** {*;}
-keep class com.zhongan.liveness.ZALivenessSDK{*;}
-keep class com.zhongan.liveness.util.ConUtil{*;}
-keep class com.zhongan.liveness.model.**{*;}
-keep class com.zhongan.liveness.R{*;}
-keep class com.zhongan.liveness.LivenessActivity{*;}
-keep class com.megvii.**{*;}
-keep class com.zhongan.security.** {*;}
-keep class * implements android.os.Parcelable {
   public static final android.os.Parcelable$Creator *;
 }

-keep class com.zhongan.demo.**{*;}
-keep class com.youyu.fin.pro.module.**{*;}
-keep public class * extends android.support.v7.app
-keep class com.youyu.fin.pro.**{*;}

-keep class com.wknight.keyboard.**{*;}

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}