package com.zhongan.demo.hxin.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.zhongan.demo.R;
import com.zhongan.demo.hxin.HXBaseFragment;
import com.zhongan.demo.hxin.activitys.HXDealBankCardActivity;
import com.zhongan.demo.hxin.activitys.HXFaceIDCardInfoUploadActivity;
import com.zhongan.demo.hxin.activitys.HXFaceStartActivity;
import com.zhongan.demo.hxin.activitys.HXLoanMoneyFirstActivity;
import com.zhongan.demo.hxin.activitys.HXPayMoneyListActivity;
import com.zhongan.demo.hxin.activitys.HXPersonCenterActivity;
import com.zhongan.demo.hxin.activitys.HXResultActivity;
import com.zhongan.demo.hxin.impl.ResultCallBack;
import com.zhongan.demo.hxin.util.Contants;
import com.zhongan.demo.hxin.util.LoggerUtil;
import com.zhongan.demo.hxin.util.Util;
import com.zhongan.demo.hxin.view.CustomDialog;
import com.zhongan.demo.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 首页页面
 */

@SuppressLint("NewApi")
public class HXShouYeFragment extends HXBaseFragment implements OnClickListener {

    private View mBaseView;
    private LinearLayout mLoanPayMoneyLL;
    private TextView mCanUsedNumTv, mLoanBtn, mLoanMoneyBtn, mPayMoneyBtn;
//	private CircleFlowIndicator mFlowIndicator;
//
//	private PullToRefreshListView mListView;
//
//	private HXShouyeListAdapter mListAdapter;
//	private int[] advertPics = new int[] { R.drawable.m_icon_banner,
//			R.drawable.m_icon_banner, R.drawable.m_icon_banner,
//			R.drawable.m_icon_banner };
//	private int[] pics = new int[] { R.drawable.list_pic_1,
//			R.drawable.list_pic_2, R.drawable.list_pic_3 };
//
//	private List<HomeProductItem> titleData;
//	private HXHomeProduct homeProduct;
//	private List<HXAdvert> mImageUrl = new ArrayList<HXAdvert>();;
//	private ImageCycleView bannerView;
//	private View headerTopView;

    private String isFace;//00：通过验证 01：验证未通过
    private String userStateInfo = "0";//客户状态
    private String canUsedSum = "0";//可用额度
    private String selfName = "", selfIdcard = "";
    private Bundle loanAmountInfo;//额度信息
    private Dialog mdialog;
    private String quotaStatus;//有逾期，冻结资金
    private String quotaFreezeAmt;//冻结资金
    private int currentPage = 1;// 当前页
    private int pageSize = 10;// 分页条数
//	private boolean isRefresh = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.hxfragment_shouye_layout, null);
        return mBaseView;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
//			mListView.onRefreshComplete();
            switch (msg.what) {
                case Contants.MSG_QUERY_LOAN_AMOUNT_INFO_FAILURE:
                    LoggerUtil.debug("isLogin---------->" + sharePrefer.iSLogin());
                    mdialog.cancel();
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                    // builder.setIcon(R.drawable.ic_launcher);
                    // builder.setTitle(R.string.title_alert);
                    builder.setMessage((String) msg.obj);
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    // 设置你的操作事项
//								if(!sharePrefer.iSLogin())
//								{
//									
//									Intent intent = new Intent(getActivity(),HXLoginActivity.class);
//									startActivity(intent);
////									ActivityStackManagerUtils.getInstance().finishActivity(HXMainActivity.class);
//								}
                                }
                            });


                    builder.create().show();
                    break;
                case Contants.MSG_QUERY_LOAN_AMOUNT_INFO_SUCCESS:

                    loanAmountInfo = msg.getData();
                    canUsedSum = loanAmountInfo.getString("amountLoan");//可用额度
                    isFace = loanAmountInfo.getString("isFace");
                    userStateInfo = loanAmountInfo.getString("userStateInfo");
                    selfName = loanAmountInfo.getString("userName");
                    selfIdcard = loanAmountInfo.getString("idCard");
                    quotaStatus = loanAmountInfo.getString("quotaStatus");
                    quotaFreezeAmt = loanAmountInfo.getString("quotaFreezeAmt");
                    LoggerUtil.debug("1111111111selfName---->" + selfName);
                    LoggerUtil.debug("1111111111selfIdcard---->" + selfIdcard);
                    LoggerUtil.debug("1111111111userStateInfo---->" + userStateInfo);
                    LoggerUtil.debug("1111111111quotaStatus---->" + quotaStatus);
                    LoggerUtil.debug("1111111111quotaFreezeAmt---->" + quotaFreezeAmt);
                    //    			canUsedSum="30000.00";
                    //                isFace="00";
                    mCanUsedNumTv.setText(canUsedSum);
                    sharePrefer.setUserStateInfo(userStateInfo);

                    //				edit.putString("userStateInfo", userStateInfo);
                    //				edit.commit();
                    if ("ced".equals(userStateInfo) || "loaned".equals(userStateInfo) || "face++".equals(userStateInfo)) {
                        //已授信、 已借款、做过face++
                        sharePrefer.setCanUsedSum(canUsedSum);
                        //					edit.putString("canUsedSum", canUsedSum);
                        //					edit.commit();
                        mLoanBtn.setVisibility(View.GONE);
                        mLoanPayMoneyLL.setVisibility(View.VISIBLE);

                    } else {
                        //已注册、 完善客户信息、 验4  、授信申请中、授信申请被拒绝
                        mLoanBtn.setVisibility(View.VISIBLE);
                        mLoanPayMoneyLL.setVisibility(View.GONE);

                    }
                    //queryProductList();
                    if (mdialog != null) {
                        mdialog.cancel();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initviews();

        mdialog = Util.createLoadingDialog(getActivity(), "数据加载中,请稍等...");
        //currentPage = 1;
        //queryLoanAmountInfo();
//		mListAdapter.notifyDataSetChanged();

        //queryProductList();

    }
/*	@Override
    public void onHiddenChanged(boolean hidden)
	{
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {
			LoggerUtil.debug("hidden---------------->"+hidden);
			//currentPage = 1;
			queryLoanAmountInfo();
//			queryProductList();
//			mListAdapter.notifyDataSetChanged();
		}
	}*/


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

//        queryLoanAmountInfo();
        LoggerUtil.debug("QQQ", "---------onResume------->");
    }

    private void initviews() {
//		headerTopView = layoutInflater.inflate(R.layout.hxhome_topview,null,false);
//		bannerView = (ImageCycleView) headerTopView.findViewById(R.id.banner);
//		
//		titleData = new ArrayList<HomeProductItem>();
//
//		mCanUsedNumTv = (TextView) headerTopView.findViewById(R.id.total_used_sum);// 总额度
//		mCanUsedNumTv.setText(canUsedSum);
//		mLoanBtn = (TextView) headerTopView.findViewById(R.id.loan_btn);// 我要贷款
//		mLoanPayMoneyLL = (LinearLayout) headerTopView
//				.findViewById(R.id.loan_pay_money_ll);// 借款还款父控件
//		mLoanMoneyBtn = (TextView) headerTopView.findViewById(R.id.loan_money_btn);// 我要借款
//		mPayMoneyBtn = (TextView) headerTopView
//				.findViewById(R.id.loan_pay_money_btn);// 我要还款
//
//		mImageUrl.add(new HXAdvert());
//		bannerView.setImageResources(mImageUrl , new ImageCycleView.ImageCycleViewListener() {
//			@Override
//			public void onImageClick(String url,String jumpview,String title) {
//				//Toast.makeText(mainActivity,"banner click",Toast.LENGTH_SHORT).show();
//				if(!TextUtils.isEmpty(url)){
//
//				}
//			}
//		},2);
//
//
//		mLoanBtn.setOnClickListener(this);
//		mLoanMoneyBtn.setOnClickListener(this);
//		mPayMoneyBtn.setOnClickListener(this);
//		
//		mListView = (PullToRefreshListView) mBaseView.findViewById(R.id.shouye_listview);
//		mListAdapter = new HXShouyeListAdapter(getActivity(), titleData);
//
//		mListView.setMode(PullToRefreshBase.Mode.BOTH);
//		mListView.setVerticalScrollBarEnabled(false);
//		mListView.getRefreshableView().addHeaderView(headerTopView, null, false);
//		mListView.setAdapter(mListAdapter);
//
//
//		// 设置刷新时显示的文本
//		ILoadingLayout refreshLayout = mListView.getLoadingLayoutProxy(
//				true, false);
//		refreshLayout.setPullLabel("下拉刷新...");
//		refreshLayout.setRefreshingLabel("正在刷新...");
//		refreshLayout.setReleaseLabel("放开以刷新");
//
//		ILoadingLayout loadLayout = mListView.getLoadingLayoutProxy(
//				false, true);
//		loadLayout.setPullLabel("上拉加载...");
//		loadLayout.setRefreshingLabel("正在加载...");
//		loadLayout.setReleaseLabel("放开以加载");
//		mListView
//		.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//			@Override
//			public void onPullDownToRefresh(
//					PullToRefreshBase<ListView> refreshView) {
//				// 下拉刷新
//				currentPage = 1;
//				isRefresh = true;
//				queryLoanAmountInfo();
//			}
//
//			@Override
//			public void onPullUpToRefresh(
//					PullToRefreshBase<ListView> refreshView) {
//				// 上拉加载
//				currentPage++;
//				isRefresh = false;
//				queryLoanAmountInfo();
//			}
//		});
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int position, long arg3) {
//				//				if (titleData != null && !titleData.isEmpty()) {
//				//					HXLoanMoneyListItemBean item = titleData.get(position - 1);
//				//					Intent intent = new Intent(getActivity(),
//				//							HXLoanMoneyDetailActivity.class);
//				//					Bundle bundle = new Bundle();
//				//					bundle.putSerializable("loanMeneyListItem", item);
//				//					intent.putExtra("loanData", bundle);
//				//					startActivity(intent);
//				//				}
//			}
//		});
        mCanUsedNumTv = (TextView) mBaseView.findViewById(R.id.total_used_sum);// 总额度
        mCanUsedNumTv.setText(canUsedSum);
        mLoanBtn = (TextView) mBaseView.findViewById(R.id.loan_btn);// 我要贷款
        mLoanPayMoneyLL = (LinearLayout)
                mBaseView.findViewById(R.id.loan_pay_money_ll);// 借款还款父控件
        mLoanMoneyBtn = (TextView) mBaseView.findViewById(R.id.loan_money_btn);// 我要借款
        mPayMoneyBtn = (TextView)
                mBaseView.findViewById(R.id.loan_pay_money_btn);// 我要还款
        mLoanBtn.setOnClickListener(this);
        mLoanMoneyBtn.setOnClickListener(this);
        mPayMoneyBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.loan_btn:
                // 我要贷款
                if ("aced".equals(userStateInfo)) {
                    //授信申请中
                    //Toast.makeText(getActivity(), "授信申请中，请稍后...", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("授信申请中，请稍后...",getActivity());
                } else if ("cedbad".equals(userStateInfo)) {
                    //授信申请被拒绝
                    //Toast.makeText(getActivity(), "授信申请被拒绝，请重新申请...", Toast.LENGTH_SHORT).show();
                    ToastUtils.showCenterToast("授信申请被拒绝，请重新申请...",getActivity());
                    Intent uploadIntent = new Intent(getActivity(), HXFaceIDCardInfoUploadActivity.class);
                    getActivity().startActivity(uploadIntent);
                } else if ("0".equals(userStateInfo) || "registered".equals(userStateInfo) || "realfied".equals(userStateInfo)) {
                    //状态为0 、已注册、身份证已上传 跳转到身份证上传页面
                    Intent uploadIntent = new Intent(getActivity(), HXFaceIDCardInfoUploadActivity.class);
                    getActivity().startActivity(uploadIntent);
                } else if ("saveinfo".equals(userStateInfo)) {
                    //完成完善客户信息 跳转银行卡验证页面
                    Intent intent = new Intent(getActivity(), HXDealBankCardActivity.class);
                    intent.putExtra("selfName", selfName);
                    intent.putExtra("selfIdcard", selfIdcard);
                    intent.putExtra("option", "check");//option: add(添加银行卡)、check(直接进入银行卡验证页面)、checkThree(开启验证三流程)
                    startActivity(intent);
                } else if ("verifi4".equals(userStateInfo)) {
                    //完成银行卡验证 跳转结果页面
                    Intent submitIntent = new Intent(getActivity(), HXResultActivity.class);
                    startActivity(submitIntent);
                }
//			else if("verifi4".equals(userStateInfo))
//			{
//				//完成银行卡验证 跳转设置密码页面
//				Intent submitIntent = new Intent(getActivity(),HXSetBankCardPwdActivity.class);
//				startActivity(submitIntent);
//			}

                break;
            case R.id.loan_money_btn:
                // 我要借款
                if ("01".equals(isFace)) {
                    LoggerUtil.debug("人脸识别验证未通过");
                    //人脸识别未通过
                    //第一次借款,进入人脸识别界面
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                    builder.setMessage(R.string.loan_money_first_tip_text);
                    builder.setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    // 设置你的操作事项
                                    Intent loanMoneyIntent = new Intent(getActivity(), HXFaceStartActivity.class);
                                    getActivity().startActivity(loanMoneyIntent);
                                }
                            });

                    builder.create().show();
                } else if ("00".equals(isFace)) {
                    if ("face++".equals(userStateInfo) || "loaned".equals(userStateInfo)) {

                        //已做过人脸识别  或者已借款
                        LoggerUtil.debug("人脸识别验证已通过");
                        if ("0".equals(quotaStatus)) {
                            //Toast.makeText(getActivity(), "您有逾期，已冻结可用额度:" + quotaFreezeAmt, Toast.LENGTH_SHORT).show();
                            ToastUtils.showCenterToast("您有逾期，已冻结可用额度:" + quotaFreezeAmt,getActivity());
                        } else {
                            //人脸识别已通过
                            Intent loanMoneyIntent = new Intent(getActivity(), HXLoanMoneyFirstActivity.class);
                            getActivity().startActivity(loanMoneyIntent);
                        }
                    }
                }
                break;
            case R.id.loan_pay_money_btn:
                // 我要还款
                Intent payMoneyIntent = new Intent(getActivity(), HXPayMoneyListActivity.class);
                getActivity().startActivity(payMoneyIntent);
                break;

            default:
                break;
        }
    }

    /**
     * 贷款额度查询
     **/
    private void queryLoanAmountInfo() {

        LoggerUtil.debug("基本信息提交数据:url---->" + Contants.REQUEST_URL

                + "\ntransCode-->" + Contants.TRANS_CODE_QUERY_LOAN_AMOUNT_INFO
                + "\nchannelNo---->"
                + Contants.CHANNEL_NO + "\nclientToken--------->" + sharePrefer.getToken()
                + "\ncustMobile-------->" + sharePrefer.getPhone());
        RequestParams params = new RequestParams("utf-8");
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("transCode", Contants.TRANS_CODE_QUERY_LOAN_AMOUNT_INFO);
        params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
        params.addBodyParameter("clientToken", sharePrefer.getToken());
        params.addBodyParameter("legalPerNum", "00006");// 法人编号

        httpRequest(Contants.REQUEST_URL, params, new ResultCallBack() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                if (mdialog != null) {
                    mdialog.cancel();
                }

                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Gson gson = new Gson();
                Map<String, String> resultMap = gson.fromJson(data, type);
                String amountLoan = resultMap.get("amountLoan");//可贷款金额
                String userStateInfo = resultMap.get("userStateInfo");//客户状态
                String isFace = resultMap.get("isFace");//人脸识别验证结果   00：通过验证 01：验证未通过
                String userName = resultMap.get("userName");//客户姓名
                String idCard = resultMap.get("idCard");//客户身份证
                String quotaStatus = resultMap.get("quotaStatus");//quotaStatus： 0 是有逾期，有冻结
                String quotaFreezeAmt = resultMap.get("quotaFreezeAmt");//冻结的金额
                Bundle bundle = new Bundle();
                bundle.putString("amountLoan", amountLoan);//可用余额
                bundle.putString("isFace", isFace);//人脸识别验证结果
                bundle.putString("userStateInfo", userStateInfo);//客户状态
                bundle.putString("userName", userName);//客户姓名
                bundle.putString("idCard", idCard);//客户身份证
                bundle.putString("quotaStatus", quotaStatus);//quotaStatus： 0 是有逾期，有冻结
                bundle.putString("quotaFreezeAmt", quotaFreezeAmt);//冻结的金额
                Message msg = new Message();
                msg.what = Contants.MSG_QUERY_LOAN_AMOUNT_INFO_SUCCESS;
                msg.setData(bundle);
                mHandler.handleMessage(msg);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                mdialog.show();
            }

            @Override
            public void onFailure(HttpException exception, String msg) {
                // TODO Auto-generated method stub
                if (mdialog != null) {
                    mdialog.cancel();
                }
            }

            @Override
            public void onError(String returnCode, String msg) {
                // TODO Auto-generated method stub
                // TODO Auto-generated method stub
                if (mdialog != null) {
                    mdialog.cancel();
                }
                if ("E10030101".equals(returnCode) || "E00015301".equals(returnCode) || "E10030101".equals(returnCode) || "E10060301".equals(returnCode)) {
                    //限额信息不存在、用户不存在、利率参数不存在
                    Bundle bundle = new Bundle();
                    bundle.putString("amountLoan", "0");//可用余额
                    bundle.putString("isFace", "01");//人脸识别验证结果
                    bundle.putString("userStateInfo", "0");//客户状态
                    bundle.putString("userName", "");//客户姓名
                    bundle.putString("idCard", "");//客户身份证
                    Message errorMsg = new Message();
                    errorMsg.what = Contants.MSG_QUERY_LOAN_AMOUNT_INFO_SUCCESS;
                    errorMsg.setData(bundle);
                    mHandler.handleMessage(errorMsg);

                } else if (!"E999985".equals(returnCode)) {

                    Message errorMsg = new Message();
                    errorMsg.what = Contants.MSG_QUERY_LOAN_AMOUNT_INFO_FAILURE;
                    errorMsg.obj = msg;
                    mHandler.handleMessage(errorMsg);

                }
            }
        });
    }

//	/**
//	 * 获取首页轮播图和产品列表
//	 */
//	private void queryProductList() {
//
//		RequestParams params = new RequestParams("utf-8");
//		params.addHeader("Content-Type","application/x-www-form-urlencoded");
//		params.addBodyParameter("transCode", Contants.TRANS_CODE_QUERY_PRODUCT_INFO_LIST);
//		params.addBodyParameter("channelNo", Contants.CHANNEL_NO);
//		params.addBodyParameter("clientToken",sharePrefer.getToken());
//
//		params.addBodyParameter("indexNo",currentPage+"");
//		params.addBodyParameter("pageSize",pageSize+"");
//		
//		httpRequest( Contants.REQUEST_URL,params,new ResultCallBack() {
//
//			@Override
//			public void onStart() {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onFailure(HttpException exception, String msg) {
//				// TODO Auto-generated method stub
//				// TODO Auto-generated method stub
//				if(mdialog!=null)
//				{
//				  mdialog.cancel();
//				}
//				mListView.onRefreshComplete();
//			}
//
//			@Override
//			public void onSuccess(String data) {
//				mListView.onRefreshComplete();
//				// TODO Auto-generated method stub
//				// TODO Auto-generated method stub
//				if(mdialog!=null)
//				{
//				  mdialog.cancel();
//				}
//				Gson gson = new Gson();
//				homeProduct = gson.fromJson(data,	HXHomeProduct.class);
//				if(null != homeProduct)
//				{
//					if(isRefresh){
//						titleData.clear();
//					}
//
//					titleData.addAll(homeProduct.getRows());
//					mListAdapter.upateData(titleData);
//					mListAdapter.notifyDataSetChanged();
//				}else
//				{
//					Toast.makeText(getActivity(), "获取列表为空", Toast.LENGTH_SHORT).show();
//				}
//			}
//
//			@Override
//			public void onError(String returnCode, String msg) {
//				// TODO Auto-generated method stub
//				// TODO Auto-generated method stub
//				mListView.onRefreshComplete();
//				if(mdialog!=null)
//				{
//				  mdialog.cancel();
//				}
//			}});
//	}


}
