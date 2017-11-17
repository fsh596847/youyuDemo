package consumer.fin.rskj.com.library.okhttp.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 请求方法
 */
@IntDef({RequestMethod.POST,RequestMethod.GET,RequestMethod.PUT,RequestMethod.DELETE})
@Retention(RetentionPolicy.SOURCE)
public @interface RequestMethod {

    /**
     * POST
     */
    int POST = 1;

    /**
     * GET
     */
    int GET = 2;

    /**
     * PUT
     */
    int PUT = 3;

    /**
     * DELETE
     */
    int DELETE = 4;
}