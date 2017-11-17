package com.zhongan.demo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.zhongan.demo.LoginActivity;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseFragment;
import com.zhongan.demo.view.MarqueeView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2017/10/12.
 * 未登录首页
 */

public class UnLoginFragment extends HXBaseFragment {

  private static final String TAG = "UnLoginFragment";
  private ArrayList<String> datas = new ArrayList<>();
  private MarqueeView marqueeView;
  List<CharSequence> list = new ArrayList<>();
  private View mBaseView;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    if (null != mBaseView) {
      ViewGroup parent = (ViewGroup) mBaseView.getParent();
      if (null != parent) {
        parent.removeView(mBaseView);
      }
    } else {
      mBaseView = inflater.inflate(R.layout.fragment_unlogin, null);
      initView();// 控件初始化
    }

    return mBaseView;
  }

  private void initView() {
    initData();
    marqueeView = (MarqueeView) mBaseView.findViewById(R.id.marqueeView);
    marqueeView.startWithList(list);
    marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
      @Override public void onItemClick(int position, TextView textView) {
        Toast.makeText(getActivity(), textView.getText() + "", Toast.LENGTH_SHORT).show();
      }
    });

    mBaseView.findViewById(R.id.loan_money).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
      }
    });
  }



  private void initData() {
    list.add("恭喜！150****4520成功借款14000元");
    list.add("恭喜！136****4562成功借款12000元");
    list.add("恭喜！137****4578成功借款13000元");
    list.add("恭喜！138****3457成功借款17000元");
    list.add("恭喜！152****1242成功借款16000元");
    list.add("恭喜！156****4510成功借款15000元");

    list.add("恭喜！180****5660成功借款20000元");
    list.add("恭喜！181****3690成功借款13000元");
    list.add("恭喜！182****3478成功借款17000元");
    list.add("恭喜！185****3468成功借款11000元");
    list.add("恭喜！188****4535成功借款20000元");
    list.add("恭喜！157****4529成功借款15000元");
  }
}
