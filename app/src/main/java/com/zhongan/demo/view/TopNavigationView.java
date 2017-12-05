package com.zhongan.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongan.demo.R;
import com.zhongan.demo.util.DensityUtil;


/**
 * Created by gxj on 2017/7/25.
 */
public class TopNavigationView extends RelativeLayout implements View.OnClickListener {

    private TextView backView;
    private TextView rightView;
    private TextView titleView;

    private String titleTextStr;
    private float titleTextSize ;
    private int  titleTextColor ;

    private Drawable leftImage ;
    private Drawable rightImage ;

    private String leftText;
    private String rightText;

    private Context mContext;

    public TopNavigationView(Context context){
        this(context, null);
    }


    public TopNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.AppTheme);
    }

    public TopNavigationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getConfig(context, attrs);
        mContext = context;
        initView(context);
    }

    /**
     * 从xml中获取配置信息
     */
    private void getConfig(Context context, AttributeSet attrs) {
        //TypedArray是一个数组容器用于存放属性值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopNavigationView);

        int count = ta.getIndexCount();
        for(int i = 0;i<count;i++)   {
            int attr = ta.getIndex(i);
            switch (attr)    {
                case R.styleable.TopNavigationView_titleText:
                    titleTextStr = ta.getString(attr);
                    break;
                case R.styleable.TopNavigationView_titleColor:
                    // 默认颜色设置为黑色
                    titleTextColor = ta.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.TopNavigationView_titleSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    //titleTextSize = ta.getDimension(attr,16);
                    break;

                case R.styleable.TopNavigationView_leftDrawable:
                    leftImage = ta.getDrawable(attr);
                    leftImage.setBounds(0, 0, leftImage.getMinimumWidth(),
                            leftImage.getMinimumHeight());
                    break;

                case R.styleable.TopNavigationView_leftText:
                    leftText = ta.getString(attr);

                    break;

                case R.styleable.TopNavigationView_rightDrawable:
                    rightImage = ta.getDrawable(attr);
                    rightImage.setBounds(0, 0, rightImage.getMinimumWidth(),
                            rightImage.getMinimumHeight());
                    break;

                case R.styleable.TopNavigationView_rightText:
                    rightText = ta.getString(attr);
                    break;
            }
        }

        //用完务必回收容器
        ta.recycle();

    }


    private void initView(Context context)  {
        View layout = LayoutInflater.from(context).inflate(
            R.layout.rskj_navigation_top, this, true);

        backView = (TextView) layout.findViewById(R.id.back_image);
        titleView = (TextView) layout.findViewById(R.id.text_title);
        rightView = (TextView) layout.findViewById(R.id.right_image);
        backView.setOnClickListener(this);
        rightView.setOnClickListener(this);

        if(null != leftImage)
            backView.setCompoundDrawables(leftImage,null,null,null);
        if(!TextUtils.isEmpty(leftText)){
            backView.setText(leftText);
        }
        backView.setCompoundDrawablePadding(DensityUtil.dip2px(context,5));//设置图片和text之间的间距
        if(null != rightImage)
            rightView.setCompoundDrawables(null,null,rightImage,null);
        rightView.setText(rightText);
        rightView.setCompoundDrawablePadding(DensityUtil.dip2px(context,5));//设置图片和text之间的间距
        if(null != titleTextStr) {
            titleView.setText(titleTextStr);
            titleView.setTextColor(titleTextColor);
        }
    }

    /**
     * 获取返回按钮
     * @return
     */
    public TextView getBackView() {
        return backView;
    }

    /**
     * 获取标题控件
     * @return
     */
    public TextView getTitleView() {
        return titleView;
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        titleView.setText(title);
    }

    /**
     * 获取右侧按钮,默认不显示
     * @return
     */
    public TextView getRightView() {
        return rightView;
    }

    public void setRightText(String text){
        rightView.setText(text);
    }

    private NavigationViewClickListener onNvClickListener;

    /**
     * 设置按钮点击监听接口
     * @param listener
     */
    public void setClickListener(NavigationViewClickListener listener) {
        this.onNvClickListener = listener;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.back_image:
                if(null != onNvClickListener)
                    onNvClickListener.onLeftClick();
                break;
            case R.id.right_image:
                if(null != onNvClickListener)
                    onNvClickListener.onRightClick();
                break;
        }
    }

    public interface NavigationViewClickListener {
        /**
         * 点击返回按钮回调
         */
        void onLeftClick();

        void onRightClick();
    }
}
