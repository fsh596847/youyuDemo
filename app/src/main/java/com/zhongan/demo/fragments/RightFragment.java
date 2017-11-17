package com.zhongan.demo.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.umeng.analytics.MobclickAgent;
import com.zhongan.demo.HotPActivity;
import com.zhongan.demo.HtmlActivity;
import com.zhongan.demo.MainActivity;
import com.zhongan.demo.MenuListActivity;
import com.zhongan.demo.MenuListActivity2;
import com.zhongan.demo.MyApplication;
import com.zhongan.demo.R;
import com.zhongan.demo.contant.HttpContent;
import com.zhongan.demo.hxin.HXBaseFragment;
import com.zhongan.demo.module.BannerItem;
import com.zhongan.demo.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import consumer.fin.rskj.com.library.activitys.WebViewActivity;
import consumer.fin.rskj.com.library.utils.Constants;

import static consumer.fin.rskj.com.library.utils.Constants.BASE_URL;

/**
 * 首页页面
 */

public class RightFragment extends HXBaseFragment implements View.OnClickListener {

	public static final String TAG = "RightFragment";
	protected LayoutInflater inflater;
	private View mBaseView;
	private MenuListActivity2 activity2;
	private List<BannerItem> itemList01 = new ArrayList<>();
	private List<BannerItem> itemList02 = new ArrayList<>();

	private List<BannerItem> itemList03 = new ArrayList<>();

	private LinearLayout image_layout1;
	private LinearLayout image_layout2;
	private LinearLayout image_layout3;
	private ImageView[] imageList = new ImageView[3];

	private TextView[] textViewList = new TextView[3];


	private Intent intent;
	private HashMap<String,String> map = new HashMap<String,String>();


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		if (null != mBaseView) {
			ViewGroup parent = (ViewGroup) mBaseView.getParent();
			if (null != parent) {
				parent.removeView(mBaseView);
			}
		} else {
			mBaseView = inflater.inflate(R.layout.fragment_right, null);
			initView();// 控件初始化
		}

		LogUtils.Log(TAG,"onCreateView = " + activity2);
		return mBaseView;
	}


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity2.getMenuList(new FinishCallBackImpl() {
			@Override
			public void finishCallBack(String data) {
				try {
					JSONObject jsonObject = new JSONObject(data);
					//办卡推荐
					JSONArray jsonArray01 = jsonObject.getJSONObject("data").getJSONArray("rmcp");
					if(null != jsonArray01 && jsonArray01.length() > 0){
						JSONObject object = jsonArray01.getJSONObject(0);
						BannerItem bannerItem = new BannerItem();

						if(object.isNull("fundId")){
							bannerItem.setAdvertTitle(object.getString("advertTitle"));
							bannerItem.setAdvertPic(object.getString("advertPic"));
							bannerItem.setAdvertUrl(object.getString("advertUrl"));
						}else {
							bannerItem.setAdvertTitle("xiaojin15");
							bannerItem.setAdvertPic(object.getString("appLogoUrl"));
							bannerItem.setProductId(object.getString("id"));
							bannerItem.setAdvertUrl(object.getString("fundId"));
//							sharePrefer.setId15(object.getString("id"));
//							sharePrefer.setFundId15(object.getString("fundId"));
						}

						itemList01.add(bannerItem);
					}
//					for(int a = 0;a < jsonArray01.length(); a++){
//						JSONObject object = jsonArray01.getJSONObject(a);
//						BannerItem bannerItem = new BannerItem();
//						bannerItem.setAdvertTitle(object.getString("advertTitle"));
//						bannerItem.setAdvertPic(object.getString("advertPic"));
//						bannerItem.setAdvertUrl(object.getString("advertUrl"));
//						itemList01.add(bannerItem);
//					}

					//热门产品
					JSONArray jsonArray02 = jsonObject.getJSONObject("data").getJSONArray("bktj");
					for(int a = 0;a < jsonArray02.length(); a++){
						JSONObject object = jsonArray02.getJSONObject(a);
						BannerItem bannerItem = new BannerItem();
						bannerItem.setAdvertTitle(object.getString("advertTitle"));
						bannerItem.setAdvertPic(object.getString("advertPic"));
						bannerItem.setAdvertUrl(object.getString("advertUrl"));
						itemList02.add(bannerItem);
					}

					//更多推荐
					JSONArray jsonArray03 = jsonObject.getJSONObject("data").getJSONArray("gdtj");
					for(int a = 0;a < jsonArray03.length(); a++){
						JSONObject object = jsonArray03.getJSONObject(a);
						BannerItem bannerItem = new BannerItem();
						bannerItem.setAdvertTitle(object.getString("advertTitle"));
						bannerItem.setAdvertPic(object.getString("advertPic"));
						bannerItem.setAdvertUrl(object.getString("advertUrl"));
						itemList03.add(bannerItem);
					}

					LogUtils.Log(TAG,"------finishCallBack-------");

				} catch (JSONException e) {
					e.printStackTrace();
				}

				refreshData();
			}
		});

		LogUtils.Log(TAG,"onCreate = " + activity2);
	}

	@Override
	public void onResume() {
		super.onResume();
	}


	@Override
	public void onAttach(Activity context) {
		super.onAttach(context);
		activity2 = (MenuListActivity2) context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LogUtils.Log(TAG,"onAttach = " + activity2);
	}

	private void initView() {

//		mBaseView.findViewById(R.id.item1).setOnClickListener(this);
		image_layout1 = (LinearLayout) mBaseView.findViewById(R.id.image_layout1);
		image_layout2 = (LinearLayout) mBaseView.findViewById(R.id.image_layout2);
		image_layout3 = (LinearLayout) mBaseView.findViewById(R.id.image_layout3);

		textViewList[0] = (TextView) mBaseView.findViewById(R.id.read_more1);
		textViewList[1] = (TextView) mBaseView.findViewById(R.id.read_more2);
		textViewList[2] = (TextView) mBaseView.findViewById(R.id.read_more3);

		textViewList[0].setOnClickListener(this);
		textViewList[1].setOnClickListener(this);
		textViewList[2].setOnClickListener(this);

		imageList[0] = (ImageView) mBaseView.findViewById(R.id.image1);
		imageList[1] = (ImageView) mBaseView.findViewById(R.id.image2);
		imageList[2] = (ImageView) mBaseView.findViewById(R.id.image3);

	}

	@Override
	public void onClick(View view) {

		if(view.getId() == R.id.read_more1){
			intent = new Intent(getActivity(), HotPActivity.class);
			startActivity(intent);
		}

		if(view.getId() == R.id.read_more2){
			Intent intent = new Intent(getActivity(),HtmlActivity.class);
			String url = "http://app-web.rskj99.com/creditLoanMore.html";
//			String url = "http://test.xmqq99.com/youyu/creditLoanMore.html";
			intent.putExtra("url", url + "?url="+HttpContent.BASE_URL);
			intent.putExtra("title","加载中...");
			startActivity(intent);
		}

		if(view.getId() == R.id.read_more3){
			Intent intent = new Intent(getActivity(),HtmlActivity.class);
			String url = "http://app-web.rskj99.com/cardMore.html";
//			String url = "http://test.xmqq99.com/youyu/cardMore.html";
			intent.putExtra("url", url + "?url="+HttpContent.BASE_URL);
			intent.putExtra("title","加载中...");
			startActivity(intent);
		}

	}


	private void refreshData(){
		if(null != itemList01 && !itemList01.isEmpty()){
			if(itemList01.size() <= 1){
//				textViewList[0].setVisibility(View.GONE);
			}
			adapterList(itemList01,image_layout1);
		}else {
			image_layout1.setVisibility(View.GONE);
		}

		if(null != itemList02 && !itemList02.isEmpty()){
			adapterList(itemList02,image_layout2);
		}else {
			image_layout2.setVisibility(View.GONE);
		}

		if(null != itemList03 && !itemList03.isEmpty()){

//			if(itemList03.size() <= 3){
//				textViewList[2].setVisibility(View.GONE);
//			}

			for (int index = 0; index < itemList03.size();index ++){
				final BannerItem bannerItem = itemList03.get(index);
				if(!TextUtils.isEmpty(bannerItem.getAdvertPic())){
					Picasso.with(getActivity()).
							load(bannerItem.getAdvertPic()).
							placeholder(R.mipmap.defaul_pgt).
							error(R.mipmap.defaul_pgt).
							into(imageList[index]);
					imageList[index].setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							map.clear();
							map.put("memId", MyApplication.getSP(getActivity()).getMemId());
							map.put("phone", MyApplication.getSP(getActivity()).getPhone());
							MobclickAgent.onEvent(getActivity(), "click_"+bannerItem.getAdvertTitle(), map);
							intent = new Intent(getActivity(),HtmlActivity.class);
							intent.putExtra("url", bannerItem.getAdvertUrl());
							intent.putExtra("title", "加载中...");
							startActivity(intent);
						}
					});
				}
			}
		}else {
			image_layout3.setVisibility(View.GONE);
		}
	}

	private void adapterList(List<BannerItem> itemList , LinearLayout image_layout) {
		for (final BannerItem qPojo : itemList){
			final View convertView = inflater.inflate(R.layout.banner_layout, null);

			ImageView image = (ImageView) convertView.findViewById(R.id.image);

			Picasso.with(getActivity()).
					load(qPojo.getAdvertPic()).
					placeholder(R.mipmap.defaul_pgt).
					error(R.mipmap.defaul_pgt).
					transform(new MyTransformation()).
					into(image);

			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if("mashanghuan".equals(qPojo.getAdvertTitle())){
						intent = new Intent(getActivity(), MainActivity.class);
						startActivity(intent);
					}else if("xiaojin15".equals(qPojo.getAdvertTitle())){
						intent = new Intent(getActivity(), WebViewActivity.class);
						intent.putExtra("url", Constants.BASE_URL + sharePrefer.getApplyLoan());
						intent.putExtra("title","贷款");
						intent.putExtra("id",qPojo.getProductId());
						intent.putExtra("fundId",qPojo.getAdvertUrl());

						startActivity(intent);
					} else {
						map.clear();
						map.put("memId", MyApplication.getSP(getActivity()).getMemId());
						map.put("phone", MyApplication.getSP(getActivity()).getPhone());
						MobclickAgent.onEvent(getActivity(), "click_"+qPojo.getAdvertTitle(), map);
						intent = new Intent(getActivity(),HtmlActivity.class);
						intent.putExtra("url", qPojo.getAdvertUrl());
						intent.putExtra("title", "加载中...");
						startActivity(intent);
					}

				}
			});

			image_layout.addView(convertView, LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

		}
	}


	public interface FinishCallBackImpl {

		public void finishCallBack(String data);
	}


	public class MyTransformation implements Transformation {

		@Override
		public Bitmap transform(Bitmap source){
			//对source实现自定义裁剪

			int targetWidth = activity2.screenWidth;
			LogUtils.Log("TTTT",
					"source.getHeight()="+source.getHeight()+",source.getWidth()="+source.getWidth()+",targetWidth="+targetWidth);

			if(source.getWidth()==0){
				return source;
			}

			//如果图片小于设置的宽度，则返回原图
			if(source.getWidth() == targetWidth){
				return source;
			}else{
				//如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
				double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
				int targetHeight = (int) (targetWidth * aspectRatio);
				if (targetHeight != 0 && targetWidth != 0) {
					Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
					if (result != source) {
						// Same bitmap is returned if sizes are the same
						source.recycle();
					}
					return result;
				} else {
					return source;
				}
			}
		}

		@Override
		public String key(){
			return "square()";
		}

	}


}
