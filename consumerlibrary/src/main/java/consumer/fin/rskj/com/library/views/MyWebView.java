package consumer.fin.rskj.com.library.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import java.util.ArrayList;
import java.util.List;

import consumer.fin.rskj.com.library.utils.LogUtils;


/**
 * Created by HP on 2017/9/20.
 */

public class MyWebView extends BridgeWebView {

    private TextView textView;

    // 用于存储每层web的标题
    private List<String> titles =  new ArrayList<>();

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyWebView(Context context) {
        super(context);
    }


    /**
     * 由于系统默认返回null，这里覆写以返回真实的Title
     * @return
     */
    @Override
    public String getTitle() {
        LogUtils.d("MyWebView", "------getTitle------------");
        if(titles.size() > 0){
            return titles.get(titles.size() - 1) ;
        }else {
            return "" ;
        }
    }


    @Override
    public void goBack() {
        LogUtils.d("MyWebView", "------goBack------------" );
        if(titles.size() > 0){
            titles.remove(titles.size() - 1) ;
        }

        textView.setText(getTitle());

        super.goBack();
    }


    public void addTitle(String title){
        titles.add(title) ;
    }

    public void setTextView(TextView textView){
        this.textView = textView;
    }
}
