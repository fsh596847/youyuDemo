package com.zhongan.demo.hxin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.zhongan.demo.R;


/**
 * 自定义seekbar
 */

@SuppressLint("AppCompatCustomView") public class TSeekBar extends SeekBar {
    /**
     * 文本的颜色
     */
    private int mTitleTextColor;
    /**
     * 文本的大小
     */
    private float mTitleTextSize;
    private String mTitleText;//文字的内容

    /**
     * 背景图片
     */
    private int imgTop,imgBottom;
    private Bitmap top_bitmap ,bottom_bitmap;
    //顶部bitmap对应的宽高
    private float top_img_width, top_img_height,bottom_img_width,bottom_img_height;
    Paint paint;

    private float numTextWidth;
    //测量seekbar的规格
    private Rect rect_seek;
    private Paint.FontMetrics fm;

    public static final int TEXT_ALIGN_LEFT = 0x00000001;
    public static final int TEXT_ALIGN_RIGHT = 0x00000010;
    public static final int TEXT_ALIGN_CENTER_VERTICAL = 0x00000100;
    public static final int TEXT_ALIGN_CENTER_HORIZONTAL = 0x00001000;
    public static final int TEXT_ALIGN_TOP = 0x00010000;
    public static final int TEXT_ALIGN_BOTTOM = 0x00100000;
    /**
     * 文本中轴线X坐标
     */
    private float textCenterX;
    /**
     * 文本baseline线Y坐标
     */
    private float textBaselineY;
    /**
     * 文字的方位
     */
    private int textAlign;

    public TSeekBar(Context context) {
        this(context, null);
    }

    public TSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TSeekBar, defStyleAttr, 0);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.TSeekBar_textsize:
                    mTitleTextSize = array.getDimension(attr, 15f);
                    break;
                case R.styleable.TSeekBar_textcolor:
                    mTitleTextColor = array.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.TSeekBar_imgtop:
                    imgTop = array.getResourceId(attr, R.drawable.m_icon_seek_bar_number_bg_selected);
                    break;
                    
                case R.styleable.TSeekBar_imgbottom:
                	imgBottom = array.getResourceId(attr, R.drawable.m_icon_seek_bar_shoubing);
                    break;
            }
        }
        array.recycle();
        getImgWH();
        paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿
        paint.setTextSize(mTitleTextSize);//设置文字大小
        paint.setColor(mTitleTextColor);//设置文字颜色
        //设置控件的padding 给提示文字留出位置
        setPadding((int) Math.ceil(top_img_width) / 2,(int)( Math.ceil(top_img_height)+rect_seek.height()+ Math.ceil(bottom_img_height)+15),(int) Math.ceil(top_img_width) / 2,(int)( Math.ceil(top_img_height)+ Math.ceil(bottom_img_height)+rect_seek.height()+10));
        textAlign = TEXT_ALIGN_CENTER_HORIZONTAL | TEXT_ALIGN_TOP;

    }
    /**
     * 获取图片的宽高
     */
    private void getImgWH() {
    	rect_seek = this.getProgressDrawable().getBounds();
        top_bitmap = BitmapFactory.decodeResource(getResources(), imgTop);
        bottom_bitmap= BitmapFactory.decodeResource(getResources(), imgBottom);
        top_img_width=top_bitmap.getWidth();
        top_img_height=top_bitmap.getHeight();
        bottom_img_width = bottom_bitmap.getWidth();
        bottom_img_height = bottom_bitmap.getHeight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        setTextLocation();//定位文本绘制的位置
        //定位顶部文字与文字背景图片的位置
        float top_bm_x = rect_seek.width() * getProgress() / getMax();
        float top_bm_y =top_img_height-5;
        canvas.drawBitmap(top_bitmap, top_bm_x,top_bm_y, paint);//画顶部背景图
        //计算文字的中心位置在bitmap
        float text_x = rect_seek.width() * getProgress() / getMax() + (top_img_width - numTextWidth) / 2;
        canvas.drawText(mTitleText, text_x, (float) (textBaselineY + top_bm_y+5 + (0.16 * top_img_height / 2)), paint);//画文字
        
        //定义底部手柄
        float bottom_bm_x = rect_seek.width() * getProgress() / getMax()+top_img_width/4;
        float bottom_bm_y = rect_seek.height()+top_img_height+30+bottom_img_height;
        canvas.drawBitmap(bottom_bitmap, bottom_bm_x, bottom_bm_y, paint);//画底部背景图
      

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();//监听手势滑动，不断重绘文字和背景图的显示位置
        return super.onTouchEvent(event);
    }

    /**
     * 定位文本绘制的位置
     */
    private void setTextLocation() {

        fm = paint.getFontMetrics();
        //文本的宽度
        mTitleText = getProgress()  + "K";
        numTextWidth = paint.measureText(mTitleText);
        float textCenterVerticalBaselineY = top_img_height / 2 - fm.descent + (fm.descent - fm.ascent) / 2-15;
        switch (textAlign) {
            case TEXT_ALIGN_CENTER_HORIZONTAL | TEXT_ALIGN_CENTER_VERTICAL:
                textCenterX = top_img_width / 2;
                textBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_LEFT | TEXT_ALIGN_CENTER_VERTICAL:
                textCenterX = numTextWidth / 2;
                textBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_RIGHT | TEXT_ALIGN_CENTER_VERTICAL:
                textCenterX = top_img_width - numTextWidth / 2;
                textBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_BOTTOM | TEXT_ALIGN_CENTER_HORIZONTAL:
                textCenterX = top_img_width / 2;
                textBaselineY = top_img_height - fm.bottom;
                break;
            case TEXT_ALIGN_TOP | TEXT_ALIGN_CENTER_HORIZONTAL:
                textCenterX = top_img_width / 2;
                textBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_TOP | TEXT_ALIGN_LEFT:
                textCenterX = numTextWidth / 2;
                textBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_BOTTOM | TEXT_ALIGN_LEFT:
                textCenterX = numTextWidth / 2;
                textBaselineY = top_img_height - fm.bottom;
                break;
            case TEXT_ALIGN_TOP | TEXT_ALIGN_RIGHT:
                textCenterX = top_img_width - numTextWidth / 2;
                textBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_BOTTOM | TEXT_ALIGN_RIGHT:
                textCenterX = top_img_width - numTextWidth / 2;
                textBaselineY = top_img_height - fm.bottom;
                break;
        }
    }
}
