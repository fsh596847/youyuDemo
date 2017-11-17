package consumer.fin.rskj.com.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.activitys.CurrentBillActivity;
import consumer.fin.rskj.com.library.activitys.RecordListActivity;
import consumer.fin.rskj.com.library.adapters.CommonRecycleViewAdapter;
import consumer.fin.rskj.com.library.adapters.RecyclerViewHolder;
import consumer.fin.rskj.com.library.module.PLItem;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.views.MyItemDecoration;
import me.leefeng.lfrecyclerview.LFRecyclerView;
import me.leefeng.lfrecyclerview.OnItemClickListener;

/**
 * Created by HP on 2017/7/24.
 *
 * d记录
 */

public class PLFragment extends BaseFragment implements
        OnItemClickListener,LFRecyclerView.LFRecyclerViewListener,
        LFRecyclerView.LFRecyclerViewScrollChange {

    public static final String TAG = "PLFragment";

    private View rootView;

    private LFRecyclerView recyclerView;
    private List<PLItem> itemList;
    private CommonRecycleViewAdapter pAdapter;

    private boolean status = false;//判断加载成功还是失败

    private boolean isRefresh = true;
    private int pageNum = 1;
    private int index; // 1=积分记录，2=使用记录
    private String total; //总积分

    private RecordListActivity activity;
    DataCallBack dataCallBack;

    String url ;
    Handler handler =  new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what){

                case 1: //刷新
                    recyclerView.stopRefresh(status);
                    pAdapter.notifyDataSetChanged();
                    break;
                case 2: //加载
                    recyclerView.stopLoadMore();
                    pAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_pl, null);
            initView();// 控件初始化
        }

        return rootView;
    }


    public static PLFragment newInstance(int index) {
        PLFragment fragment = new PLFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这里我只是简单的用num区别标签，其实具体应用中可以使用真实的fragment对象来作为叶片
        index = getArguments().getInt("index",0) ;

        LogUtils.d(TAG, " index--->" + index);
    }

    private void initView() {

        //初始化list
        itemList = new ArrayList<>();

        recyclerView = (LFRecyclerView) rootView.findViewById(R.id.pull_refresh_list);
        recyclerView.setLoadMore(false);
        recyclerView.setRefresh(false);
        recyclerView.setNoDateShow();
        recyclerView.setAutoLoadMore(true);
        recyclerView.setOnItemClickListener(this);
        recyclerView.setLFRecyclerViewListener(this);
        recyclerView.setScrollChangeListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        pAdapter = new CommonRecycleViewAdapter(recyclerView, itemList, R.layout.plrecord_item){

            @Override
            public void bindConvert(RecyclerViewHolder holder, int position, Object item, boolean isScrolling) {

                PLItem plItem = (PLItem) item;

                if(index == 1){
                    holder.setText(R.id.currentPeriod_contractTerm,plItem.getContractAmt());
                    holder.setText(R.id.status,"");
                    holder.setText(R.id.startDate,plItem.getStartDate());
                    holder.setVisible(R.id.array,false);

                    switch (plItem.getCurrentPeriod()){

                        case "0":
                            holder.setText(R.id.contractAmt,"正常还款");
                            break;
                        case "1":
                            holder.setText(R.id.contractAmt,"提前还款");
                            break;
                        case "2":
                            holder.setText(R.id.contractAmt,"逾期还款");
                            break;
                        case "3":
                            holder.setText(R.id.contractAmt,"部分提前缩期");
                            break;
                        case "4":
                            holder.setText(R.id.contractAmt,"部分提前不缩期");
                            break;
                        case "5":
                            holder.setText(R.id.contractAmt,"自动扣款");
                            break;

                        default:
                            holder.setText(R.id.contractAmt,"--");
                            break;
                    }


                }else {
                    holder.setText(R.id.contractAmt,plItem.getContractAmt());
                    holder.setText(R.id.currentPeriod_contractTerm,plItem.getCurrentPeriod()+"/"+plItem.getContractTerm());
                    //0: 未还,1: 未还清,2: 已还,3: 已逾期,4: 停止计息,5: 已冲正.
                    holder.setVisible(R.id.array,true);
                    switch (plItem.getStatus()){

                        case "0":
                            holder.setText(R.id.status,"未还");
                            break;
                        case "1":
                            holder.setText(R.id.status,"未还清");
                            break;
                        case "2":
                            holder.setText(R.id.status,"已还");
                            break;
                        case "3":
                            holder.setText(R.id.status,"已逾期");
                            break;
                        case "4":
                            holder.setText(R.id.status,"停止计息");
                            break;
                        case "5":
                            holder.setText(R.id.status,"已冲正");
                            break;

                        case "12":
                            holder.setText(R.id.status,"还款中");
                            break;
                        case "16":
                            holder.setText(R.id.status,"放款中");
                            break;

                        default:
                            holder.setText(R.id.status,"未知");
                            break;
                    }

                    holder.setText(R.id.startDate,plItem.getStartDate() + "借");
                }

            }
        };

        //recyclerView.setHeaderView(headerTopView);
        recyclerView.setFootText("加载更多");
//        recyclerView.addItemDecoration(new MyItemDecoration());

        recyclerView.setAdapter(pAdapter);

        dataCallBack = new DataCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.d(TAG, "DataCallBack: index--->" + index);
                LogUtils.d(TAG, "DataCallBack: onSuccess--->" + data);


                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");
                    for (int i= 0; i < jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        PLItem plItem = new PLItem();

                        if(index == 1){
                            plItem.setContractAmt(object.getString("repayAmt"));
                            plItem.setCurrentPeriod(object.getString("cleanCutCd"));
                            plItem.setStartDate(object.getString("repayDate"));

                        }else {
                            plItem.setProductName(object.getString("productName"));
                            plItem.setStartDate(object.getString("startDate"));
                            plItem.setStatus(object.getString("status"));
                            plItem.setDayRate(object.getString("dayRate"));
                            plItem.setCurrentPeriod(object.getString("currentPeriod"));
                            plItem.setCurrentPrincipal(object.getString("currentPrincipal"));
                            plItem.setContractTerm(object.getString("contractTerm"));
                            plItem.setContractAmt(object.getString("contractAmt"));
                            plItem.setLoanId(object.getString("loanId"));

                        }

                        itemList.add(plItem);
                    }

                    pAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.d(TAG, "DataCallBack: onFailure--->" + errorMsg);
            }

            @Override
            public void onError(String retrunCode, String errorMsg) {
                LogUtils.d(TAG, "DataCallBack: onError--->" + errorMsg);
            }
        };




        if(index == 1){
            activity.getPayRecordList(dataCallBack);
        }else {

            activity.getCurrentRecordList(dataCallBack);
        }
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        activity = (RecordListActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(null != handler){
            handler.removeCallbacksAndMessages(null);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(null != handler){
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        isRefresh = true;

        activity.getCurrentRecordList(dataCallBack);
    }

    @Override
    public void onLoadMore() {
        pageNum ++;
        isRefresh = false;

        activity.getCurrentRecordList(dataCallBack);
    }


    @Override
    public void onRecyclerViewScrollChange(View view, int i, int i1) {

    }

    @Override
    public void onClick(int position) {

        if(index == 0){
            Intent intent = new Intent(getActivity(), CurrentBillActivity.class);
            intent.putExtra("loanId",itemList.get(position).getLoanId());
            //0: 未还,1: 未还清,2: 已还,3: 已逾期,4: 停止计息,5: 已冲正.
            if("3".equals(itemList.get(position).getStatus()) ){
                intent.putExtra("currentStatus",true);
            }else {
                intent.putExtra("currentStatus",false);
            }
            startActivity(intent);
        }

    }

    @Override
    public void onLongClick(int po) {

    }


    public interface DataCallBack{
        //成功回调
        void onSuccess(String data);
        //失败回调
        void onFailure(String errorMsg);
        //错误回调
        void onError(String retrunCode, String errorMsg);

    }


}
