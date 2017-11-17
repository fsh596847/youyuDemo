package consumer.fin.rskj.com.library.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import consumer.fin.rskj.com.library.activitys.RecordListActivity;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;

/**
 * Created by HP on 2017/8/22.
 */

public class BaseFragment extends Fragment {

    //网络请求表单参数
    protected HashMap<String, String> requestParams = new HashMap<>();
    Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

}
