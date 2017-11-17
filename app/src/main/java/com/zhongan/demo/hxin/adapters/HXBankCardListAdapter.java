package com.zhongan.demo.hxin.adapters;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhongan.demo.MyApplication;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.activitys.HXBankCardManageActivity;
import com.zhongan.demo.hxin.bean.HXBankListItemBean;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.util.SharedPreferenceUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


public class HXBankCardListAdapter extends BaseAdapter {
	private Context mContext;
	SharedPreferenceUtils sharePrefer;

	 private List<HXBankListItemBean> bankData;
	public HXBankCardListAdapter(Context mContext,
                                 List<HXBankListItemBean> bankData) {
		super();
		this.mContext = mContext;
		this.bankData = bankData;
		
		sharePrefer = new SharedPreferenceUtils(mContext);
	}
    public void updateData(List<HXBankListItemBean> bankData)
    {
    	this.bankData = bankData;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bankData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bankData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final int selectPosition=position;
		ViewHolder holder=null;
		View menuView=null;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.hxbank_card_swipe_content, null);
			menuView = View.inflate(mContext,
					R.layout.hxbank_card_swipe_menu, null);
			
//			convertView = new DragDelItem(convertView,menuView);;
			holder=new ViewHolder(convertView);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
			
			HXBankListItemBean item =   (HXBankListItemBean) getItem(position);
//			String bankLogo=item.get("bankLogo");//银行卡logo
//			String bankName=item.get("bankName");//银行名称
//			String bankType=item.get("bankCardType");//银行卡类型
//			String isDefaultCard=item.get("isDefaultCard");//是默认卡：1 不是默认卡0
//			String cardColor=item.get("bankCardColor");//银行卡背景
//			String cardId=item.get("bankCardId");//银行卡号
		    String bankName=item.getBankName();
		   
			holder.mBankLogoIv.setBackgroundResource(R.drawable.bank_default_logo);
		    String isDefaultCard=item.getIsMain();//是默认卡：1 不是默认卡0
			holder.mBankNameTv.setText(bankName);
//			holder.mBankTypeTv.setText(bankType);
			String cardColor="blue";
			String cardId=item.getBankCode();//银行卡号
			final String acctId=item.getAcctId();//银行卡主键
			if(isDefaultCard.equals("1"))
			{
			  holder.mDefaultCardTv.setVisibility(View.VISIBLE);
			}else{
				holder.mDefaultCardTv.setVisibility(View.GONE);
			}
			if(cardColor.equals("blue"))
			{
				holder.mBankBgRl.setBackgroundResource(R.drawable.m_icon_bank_card_bg_blue);
			}else if(cardColor.equals("green"))
			{
				holder.mBankBgRl.setBackgroundResource(R.drawable.m_icon_bank_card_bg_green);
			}else if(cardColor.equals("red"))
			{
				holder.mBankBgRl.setBackgroundResource(R.drawable.m_icon_bank_card_bg_red);
			}
			holder.mCardIdTv.setText(cardId);
//			holder.mSetDefautCardBtn.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					setDafaultBankCard(acctId);
//				}
//			});
//			holder.mDeleteCardBtn.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
////					
////					bankData.remove(selectPosition);
////					notifyDataSetChanged();
//				}
//			});
		return convertView;
	}
	
	//设置主还款卡接口
  	private void setDafaultBankCard(String acctId)
  	{
  		 LoggerUtil.debug("设置主还款卡: clientToken--->"+sharePrefer.getToken());
  		 
  		 RequestParams params = new RequestParams("utf-8");
  	     params.addQueryStringParameter("transCode", Contants.TRANS_CODE_SET_DEFAULT_BANK_CARD);
  		 params.addQueryStringParameter("channelNo", Contants.CHANNEL_NO);
  		 params.addBodyParameter("clientToken",sharePrefer.getToken());
  		 params.addBodyParameter("acctId",acctId);

//  		 HttpUtils dataHttp = new HttpUtils("60 * 1000");
  		 MyApplication.instance.dataHttp.send(HttpMethod.POST, Contants.REQUEST_URL, params,
  		 new RequestCallBack<String>() {

  						@Override
  						public void onFailure(HttpException arg0,
  								String error) {
  							// TODO Auto-generated method stub
  							LoggerUtil.debug("还款方式 error-------------->" + error);
  							Message msg = new Message();
  							msg.what = Contants.MSG_DO_SET_DEFAULT_BANK_CARD_FAILURE;
  							msg.obj="网络问题";
  							HXBankCardManageActivity.instance.getHandler().handleMessage(msg);
  						}

  						@Override
  						public void onSuccess(
  								ResponseInfo<String> responseInfo) {
  							LoggerUtil.debug( "设置主还款卡：result---->"
  									+ responseInfo.result
  									+ "\nresponseInfo.statusCode ===="
  									+ responseInfo.statusCode);
  							if (responseInfo.statusCode == 200) {
  								Type type = new TypeToken<Map<String, String>>() {
								}.getType();
  								Gson gson = new Gson();
  								Map<String, String> resultMap= gson.fromJson(responseInfo.result,type);
                              
  								String returnCode = resultMap
										.get("returnCode");
								if ("000000".equals(returnCode)) {
								    String code=resultMap.get("code");
								    	Message msg=new Message();
	  									msg.obj=code;
	  									msg.what=Contants.MSG_DO_SET_DEFAULT_BANK_CARD_SUCCESS;
	  									HXBankCardManageActivity.instance.getHandler().sendMessage(msg);
								    		
								} else {
									LoggerUtil.debug("设置主还款卡失败!");
									String returnMsg = resultMap.get("returnMsg");// 错误提示
									Message msg = new Message();
									msg.what = Contants.MSG_DO_SET_DEFAULT_BANK_CARD_FAILURE;
									msg.obj = returnMsg;
									HXBankCardManageActivity.instance.getHandler().handleMessage(msg);
								}
  								

  							}

  						}
  					});
  	}	
	class ViewHolder {
		ImageView mBankLogoIv;
		TextView mBankNameTv,mBankTypeTv,mDefaultCardTv,mCardIdTv;
		TextView mSetDefautCardBtn,mDeleteCardBtn;
		FrameLayout mBankBgRl;
		public ViewHolder(View view) {
			mBankBgRl=(FrameLayout) view.findViewById(R.id.bank_card_rl);
			mBankLogoIv = (ImageView) view.findViewById(R.id.bank_logo);//银行logo
			mBankNameTv = (TextView) view.findViewById(R.id.bank_name);//银行名称
			mBankTypeTv = (TextView) view.findViewById(R.id.bank_card_type);//银行卡类型
			mDefaultCardTv = (TextView) view.findViewById(R.id.bank_card_default);//默认卡提示
			mCardIdTv = (TextView) view.findViewById(R.id.bank_card_id);//银行卡号
			mSetDefautCardBtn=(TextView)view.findViewById(R.id.set_default_bank_card_btn);//设置默认卡按钮
			mDeleteCardBtn=(TextView)view.findViewById(R.id.delete_bank_card_btn);//更换银行卡按钮
			view.setTag(this);
		}
	}

}
