package consumer.fin.rskj.com.library.utils;

/**
 * Created by HP on 2017/8/12.
 */

public interface BusinessParams {
    /**
     * registered:已注册     saveinfo  :完善客户信息
     realfied  :实名认证(OCR)     verifi4   :验4
     face++    :人脸识别     baseInfo  :基础信息
     suppleInfo:补充信息     aced      :授信申请中
     cedbad    :授信申请被拒绝     ced       :已授信
     0          ：未有状态
     */
    public static final String  registered = "registered";
    public static final String  saveinfo = "saveinfo";
    public static final String  realfied = "realfied";
    public static final String  verifi4 = "verifi4";
    public static final String  facepp = "face++";
    public static final String  baseInfo = "baseInfo";

    public static final String  suppleInfo = "suppleInfo";
    public static final String  aced = "aced";
    public static final String  cedbad = "cedbad";
    public static final String  ced = "ced";
    public static final String  noStatus = "0";


}
