package com.zhongan.demo.contant;

/**
 * Created by gxj on 2017/06/12.
 */
public interface HttpContent {
    //请求接口根地址
    //public static final String BASE_URL = "http://api-core.rskj99.com/rskj-core-api";//生产
    String BASE_URL = "http://test.rskj99.com/rskj-core-api";//测试
//    public static final String BASE_URL = "http://118.89.28.233:8087/rskj-core-api";//准生产

  String HTTP_BANNER = "ad/banner";//产品菜单

  String HTTP_REGIST = "member/regist";//注册
  String HTTP_REGISTER_CODE = "verificacode/createtext/regist/";//注册验证码
  String HTTP_PASSWORD_CODE = "verificacode/createtext/password/";//設置密碼验证码

  String HTTP_LOGIN = "member/login";//登录
  String HTTP_LOGOUT = "member/logout";//退出

  String HTTP_FORGET_PASSWORD = "member/password";//忘记密码
  String HTTP_PASSWORD_CHECK = "verificacode/check";//验证码校验

    //注册协议
    String REGISTER_PROTOCOL = "http://youyuapi.rskj99.com/youyu-api/html/s?n=register_protocol";
        //生产
//    public static final String REGISTER_PROTOCOL = "http://test.xmqq99.com/youyu-api/html/s?n=register_protocol";

//    public static final String VERSION_UPDATE = "http://118.89.28.233:8081/qqjf-api/system/release";//准生产
String VERSION_UPDATE = "http://qqjfapi.rskj99.com/qqjf-api/system/release";//生产

}
