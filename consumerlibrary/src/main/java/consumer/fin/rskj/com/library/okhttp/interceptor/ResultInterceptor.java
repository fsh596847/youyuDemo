package consumer.fin.rskj.com.library.okhttp.interceptor;


import consumer.fin.rskj.com.library.okhttp.HttpInfo;

/**
 * 请求结果拦截器
 */
public interface ResultInterceptor {

    HttpInfo intercept(HttpInfo info) throws Exception;

}
