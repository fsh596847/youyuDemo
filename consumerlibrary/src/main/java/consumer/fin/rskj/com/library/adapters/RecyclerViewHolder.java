package consumer.fin.rskj.com.library.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by HP on 2017/7/19.
 *
 * ViewHolder的基类，设置view时候需要用到。
 * 操作步骤:
 * 1、绑定View时候，创建RecyclerViewHolder，并产生arrayView
 * 2、使用getView方法，实例化每一个View。
 * 3、为View设置具体的内容
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> arrayView;
    public RecyclerViewHolder(View itemView) {
        super(itemView);
        arrayView = new SparseArray<>();
    }

    /**
     * 通过填写的itemId来获取具体的View的对象
     * @param itemId  R.id.***
     * @param <T> 必须是View的子类
     * @return
     */
    public <T extends View> T getView(int itemId){
        //arrayVie类似于Map容器，get(key)取出的是value值
        View mView = arrayView.get(itemId);
        if(mView == null){
            //实例化具体的View类型
            mView = itemView.findViewById(itemId);
            arrayView.put(itemId,mView);
        }
        return (T) mView;
    }

    /**
     * 设置TextView的内容
     * @param itemId
     * @param text
     */
    public void setText(int itemId,String text){
        TextView tv = getView(itemId);
        tv.setText(text);
    }

    //设置字体颜色
    public void setTextColor(int itemId,int color){
        TextView tv = getView(itemId);
        tv.setTextColor(color);
    }


    public void setVisible(int itemId,boolean visible){
        getView(itemId).setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置图片
     * @param itemId
     * @param imageId
     */
    public void setBitmapImage(int itemId,int imageId){
        ImageView iv = getView(itemId);
        iv.setImageResource(imageId);
    }


    /**
     * 设置点击事件
     */
    public void setClickListerner(int itemId,View.OnClickListener clickListerner){

        getView(itemId).setOnClickListener(clickListerner);

    }



}
