package consumer.fin.rskj.com.library.activitys;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import consumer.fin.rskj.com.consumerlibrary.R;
import consumer.fin.rskj.com.library.callback.ResultCallBack;
import consumer.fin.rskj.com.library.message.MainMessage;
import consumer.fin.rskj.com.library.module.KVItem;
import consumer.fin.rskj.com.library.module.LinkmanItemBean;
import consumer.fin.rskj.com.library.okhttp.HttpInfo;
import consumer.fin.rskj.com.library.okhttp.OkHttpUtil;
import consumer.fin.rskj.com.library.okhttp.callback.Callback;
import consumer.fin.rskj.com.library.utils.Constants;
import consumer.fin.rskj.com.library.utils.GetJsonDataUtil;
import consumer.fin.rskj.com.library.utils.LogUtils;
import consumer.fin.rskj.com.library.utils.SysUtil;
import consumer.fin.rskj.com.library.utils.Util;
import consumer.fin.rskj.com.library.views.OptionsPickerView;

import static consumer.fin.rskj.com.library.utils.Constants.BASE_URL;


/**
 * 基本信息页面
 */

public class BasicInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "BasicInfoActivity";

    private Button mBackBtn;
    private TextView mTitleTv;
    private TextView mFamilyAddressTv;

    private EditText email_ed;//邮箱地址
    private EditText mFamilyDetailAddressEt;
    private ImageView mDetailAddressChooseIv;
    private RadioButton mAlreadyMarriedRb;
    private RadioButton mNotMarryRb;
    private RadioGroup mMarryStateRg;
    private EditText mLinkNameEt;
    private EditText mLinkPhoneTv;
    private ImageView mMobileChooseIv;
    private TextView mLinkRelationshipTv;
    private Button mSubmitBtn;
    private OptionsPickerView mOptionView;
    private ArrayList<KVItem> options1Items = new ArrayList<KVItem>();
    private ArrayList<ArrayList<KVItem>> options2Items = new ArrayList<ArrayList<KVItem>>();
    private ArrayList<ArrayList<ArrayList<KVItem>>> options3Items = new ArrayList<ArrayList<ArrayList<KVItem>>>();
    private OptionsPickerView mRegitionOptionView;
    private Thread thread;
    private boolean isFristLocation = true;
    private ArrayList<LinkmanItemBean> allContactsInfo;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x123:
                    // 三级联动效果
                    dismissLoading();
                    mRegitionOptionView.setPicker(options1Items, options2Items, options3Items);
                    mRegitionOptionView.setSelectOptions(0, 0, 0);
                    mFamilyAddressTv.setClickable(true);
                    break;
            }

        }

        ;
    };
    private ArrayList<KVItem> relatipnships; //联系人关系
    private String familyAddress = "", familyDetailAddress = "", marryState = "20", linkName = "", linkMoblie = "", linkRelationship = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info_layout);

        LogUtils.d("BasicInfoActivity","数组 = " + SysUtil.stepMap);
    }

    private void initJsonData() {//解析数据

        /** 从assert文件夹中读取省市区的json文件，然后转化为json对象 */
        try {
            String JsonData = new GetJsonDataUtil().getJson(getApplicationContext(), "province.json");//获取assets目录下的json文件数据
            JSONArray mJsonArr = new JSONArray(JsonData.toString());
            for (int i = 0; i < mJsonArr.length(); i++) {
                JSONObject jsonP = mJsonArr.getJSONObject(i);// 获取每个省的Json对象
                String province = jsonP.getString("name");
                ArrayList<KVItem> options2Items_01 = new ArrayList<KVItem>();
                ArrayList<ArrayList<KVItem>> options3Items_01 = new ArrayList<ArrayList<KVItem>>();
                JSONArray jsonCs = jsonP.getJSONArray("city");
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonC = jsonCs.getJSONObject(j);// 获取每个市的Json对象
                    String city = jsonC.getString("name");
                    options2Items_01.add(new KVItem(""+j,city));// 添加市数据

                    ArrayList<KVItem> options3Items_01_01 = new ArrayList<KVItem>();
                    JSONArray jsonAs = jsonC.getJSONArray("area");
                    for (int k = 0; k < jsonAs.length(); k++) {
                        options3Items_01_01.add(new KVItem(""+k,jsonAs.getString(k)));// 添加区数据
                    }
                    options3Items_01.add(options3Items_01_01);
                }
                options1Items.add(new KVItem(""+i,province));// 添加省数据
                options2Items.add(options2Items_01);// 添加市数据
                options3Items.add(options3Items_01);// 添加区数据

            }
            LogUtils.d("debug", "省数据" + options1Items.toString());
            LogUtils.d("debug", "市数据" + options2Items.toString());
            LogUtils.d("debug", "区数据" + options3Items.toString());
            LogUtils.d("debug", "provices size" + options1Items.size());
            LogUtils.d("debug", "citys size" + options2Items.size());
            LogUtils.d("debug", "areas size" + options3Items.size());
            handler.sendEmptyMessage(0x123);
            mJsonArr = null;
        } catch (Exception e) {
            LogUtils.d("debug", "获取数据失败");
        }

    }

    @Override
    public void init() {
        mBackBtn = (Button) findViewById(R.id.left_btn);
        mTitleTv = (TextView) findViewById(R.id.center_title);
        mFamilyAddressTv = (TextView) findViewById(R.id.basic_info_family_address);//家庭住址（省市区）显示控件
        mFamilyDetailAddressEt = (EditText) findViewById(R.id.basic_info_family_detail_address);//详细地址（街道信息等）

        email_ed = (EditText) findViewById(R.id.email_ed);

        mDetailAddressChooseIv = (ImageView) findViewById(R.id.basic_detail_address_choose_Iv);//详细地址定位按钮
        mAlreadyMarriedRb = (RadioButton) findViewById(R.id.basic_already_married_Rb);//婚姻状态控件
        mNotMarryRb = (RadioButton) findViewById(R.id.basic_not_marry_Rb);//未婚显示控件
        mMarryStateRg = (RadioGroup) findViewById(R.id.basic_marry_Rg);//已婚显示控件
        mLinkNameEt = (EditText) findViewById(R.id.basic_info_link_name);//联系人姓名控件
        mLinkPhoneTv = (EditText) findViewById(R.id.basic_info_link_phone);//联系人电话控件
        mMobileChooseIv = (ImageView) findViewById(R.id.basic_mobile_choose_Iv);//调取通讯录控件
        mLinkRelationshipTv = (TextView) findViewById(R.id.basic_info_link_relationship);//联系人关系显示控件
        mSubmitBtn = (Button) findViewById(R.id.basic_info_submit_btn);//信息提交按钮
        mBackBtn.setOnClickListener(this);
        mTitleTv.setText(R.string.basic_info_text);//设置标题
        mFamilyAddressTv.setOnClickListener(this);
        mDetailAddressChooseIv.setOnClickListener(this);
        mLinkPhoneTv.setOnClickListener(this);
        mAlreadyMarriedRb.setOnClickListener(this);
        mNotMarryRb.setOnClickListener(this);
        mLinkRelationshipTv.setOnClickListener(this);
        mMobileChooseIv.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
//        mLinkRelationshipTv.setText("父母");//设置默认关系
        mLinkRelationshipTv.setHint("请选择联系人关系");
        //婚姻状态变更监听
        mMarryStateRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.basic_already_married_Rb) {
                    //已婚
                    marryState = "20";
                } else if (checkedId == R.id.basic_not_marry_Rb) {
                    //未婚
                    marryState = "10";
                }
            }
        });
        mFamilyAddressTv.addTextChangedListener(textWacher);
        mFamilyDetailAddressEt.addTextChangedListener(textWacher);
        mLinkNameEt.addTextChangedListener(textWacher);
        mLinkPhoneTv.addTextChangedListener(textWacher);
        mLinkRelationshipTv.addTextChangedListener(textWacher);


        //初始化联系人关系
        relatipnships = new ArrayList<KVItem>();
        relatipnships.add(new KVItem("0","配偶"));
        relatipnships.add(new KVItem("1","父母"));
        relatipnships.add(new KVItem("4","子女"));
        relatipnships.add(new KVItem("2","兄妹"));
        relatipnships.add(new KVItem("3","其他亲属"));
        //初始话省市区选择控件
        initRegionChoose("家庭住址");

    }

    //联系人关系信息选择控件
    private void initChooseOptionPopWindow(final String title, final TextView parentView,
                                 View view, final ArrayList<KVItem> items) {
        Util.hideKeyBoard(this, view);
        //文本选择器
        if (mOptionView == null) {
            mOptionView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    parentView.setText(items.get(options1).getValue());
                    if (title.equals("联系人关系")) {
                        linkRelationship = /*(options1 + 1) + ""*/items.get(options1).getKey();
                        LogUtils.d("debug", "联系人关系" + linkRelationship);
                    }
                }
            }).setLineSpacingMultiplier(2.0f)
                    .setTitleText(title)
//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                    .setContentTextSize(17)
                    .setOutSideCancelable(false)// default is true
                    .build();
        }
        String chooseOptions = parentView.getText().toString().trim();
        if (chooseOptions == null || "".equals(chooseOptions)) {
            mOptionView.setSelectOptions(0, 0, 0);
        } else if (chooseOptions != null && !chooseOptions.equals("")) {
            for (int i = 0; i < items.size(); i++) {
                String text = items.get(i).getValue();
//	        		 LoggerUtil.debug("chooseOptions  text"+text);
                if (text != null && chooseOptions.equals(text)) {
                    LogUtils.d("debug", "chooseOptions position" + i);
                    mOptionView.setSelectOptions(i, 0, 0);
                }
            }
        }
        mOptionView.setPicker(items);
        mOptionView.show();
    }

    //省市区信息选择控件
    private void initRegionChoose(final String title) {

        // 省市区选择器
        mRegitionOptionView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                KVItem provice = options1Items.get(options1);
                KVItem city = options2Items.get(options1).get(options2);
                KVItem distrit = options3Items.get(options1).get(options2).get(options3);
                String addressLocation = "";
                LogUtils.d("debug", "provice----->" + provice + "\ncity--------->" + city + "\ndistrit------>" + distrit);
                if ("北京市".equals(city.getValue()) || "上海市".equals(city.getValue()) || "天津市".equals(city.getValue()) || "重庆市".equals(city.getValue()) || "澳门".equals(city.getValue()) || "香港".equals(city.getValue()) || "台湾省".equals(city.getValue())) {
                    addressLocation = provice.getValue() + distrit.getValue();
                } else {
                    addressLocation = provice.getValue() + city.getValue() + distrit.getValue();
                }
                mFamilyAddressTv.setText(addressLocation);

            }
        }).setLineSpacingMultiplier(2.0f)
                .setTitleText(title)
//                .setDividerColor(Color.BLACK)
//                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(17)
                .setOutSideCancelable(false)// default is true
                .build();
        if (options1Items != null && options1Items.size() != 0 && options2Items != null && options2Items.size() != 0 && options3Items != null && options3Items.size() != 0) {
            LogUtils.d("debug", "已经加载省市区数据");
            handler.sendEmptyMessage(0x123);
            return;
        }
        if (thread == null) {
            showLoading("数据加载中...");
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.d("debug", "加载省市区数据");
                    initJsonData();
                }
            });
            thread.start();
        }
        // 点击弹出选项选择器
        mFamilyAddressTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mRegitionOptionView.setSelectOptions(0, 0, 0);
                mRegitionOptionView.show();
            }
        });
        mFamilyAddressTv.setClickable(false);
    }

    private TextWatcher textWacher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable text) {
            // TODO Auto-generated method stub
            familyAddress = mFamilyAddressTv.getText().toString().trim();//家庭住址
            familyDetailAddress = mFamilyDetailAddressEt.getText().toString().trim();//详细信息
            linkName = mLinkNameEt.getText().toString().trim();//联系人姓名
            linkMoblie = mLinkPhoneTv.getText().toString().trim();//联系人电话
            String linkRelationshipStr = mLinkRelationshipTv.getText().toString().trim();//联系人关系
            if (familyAddress != null && !familyAddress.equals("")
                    && familyDetailAddress != null && !familyDetailAddress.equals("") && linkName != null && !linkName.equals("") && linkMoblie != null && !linkMoblie.equals("") && linkMoblie.length() > 6
                    && linkRelationshipStr != null && !linkRelationshipStr.equals("")) {
                //信息提交按钮可用状态
                mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
                mSubmitBtn.setClickable(true);
            } else {
                //信息提交按钮不可用状态
                mSubmitBtn.setClickable(false);
                mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_disable_bg);
            }

        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.left_btn) {
            Util.hideKeyBoard(BasicInfoActivity.this, view);
            this.finish();
        } else if (view.getId() == R.id.basic_already_married_Rb) {
            Util.hideKeyBoard(BasicInfoActivity.this, view);
            //已婚
            mMarryStateRg.check(R.id.basic_already_married_Rb);
        } else if (view.getId() == R.id.basic_not_marry_Rb) {
            Util.hideKeyBoard(BasicInfoActivity.this, view);
            //未婚
            mMarryStateRg.check(R.id.basic_not_marry_Rb);
        } else if (view.getId() == R.id.basic_info_submit_btn) {
            Util.hideKeyBoard(BasicInfoActivity.this, view);
            //提交
            familyAddress = mFamilyAddressTv.getText().toString().trim();
            familyDetailAddress = mFamilyDetailAddressEt.getText().toString().trim();
            linkName = mLinkNameEt.getText().toString().trim();
            linkMoblie = mLinkPhoneTv.getText().toString().trim();
            //判断手机号是否合法
            if (Util.isMobile(linkMoblie)) {
                String houseAddress = familyAddress + familyDetailAddress;
                submitBasicInfo(houseAddress, marryState, linkName, linkMoblie, linkRelationship);
            } else {
                showToast("请输入或选择11位合法手机号码!", Constants.TOAST_SHOW_POSITION);
            }

        } else if (view.getId() == R.id.basic_mobile_choose_Iv) {
            Util.hideKeyBoard(BasicInfoActivity.this, view);
            //调用系统通讯录
            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
        } else if (view.getId() == R.id.basic_detail_address_choose_Iv) {
            //调用定位
//            Util.hideKeyBoard(BasicInfoActivity.this, view);
            mFamilyDetailAddressEt.setText(getLocation());
        } else if (view.getId() == R.id.basic_info_link_relationship) {
            Util.hideKeyBoard(BasicInfoActivity.this, view);
            initChooseOptionPopWindow("联系人关系", mLinkRelationshipTv, view, relatipnships);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String[] phone_name = getSelectPhone(data);
            mLinkNameEt.setText(phone_name[0]);
            mLinkPhoneTv.setText(phone_name[1]);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    allContactsInfo = new ArrayList<>();
                    allContactsInfo.addAll(getAllContacts());
                    if (allContactsInfo.size() > 0) {
                        submitConstantsInfo(allContactsInfo);
                        LogUtils.d("debug", "所有联系人信息" + allContactsInfo.toString());
                    }
                }
            }).start();
        }
    }

    /**
     * 获取选定联系人电话
     *
     * @return
     */
    private String[] getSelectPhone(Intent data) {
        String[] userPhone = new String[2];
        //ContentProvider展示数据类似一个单个数据库表
        //ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
        ContentResolver reContentResolverol = getContentResolver();
        //URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
        Uri contactData = data.getData();
        //查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
        Cursor cursor = managedQuery(contactData, null, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            //获得DATA表中的联系人姓名
            String userName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            userPhone[0] = userName;
            //条件为联系人ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
            Cursor phoneCur = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null);
            while (phoneCur != null && phoneCur.moveToNext()) {
                //获得DATA表中的联系人手机号
                userPhone[1] = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");
                if (!TextUtils.isEmpty(userPhone[1]) && userPhone[1].contains("-")) {
                    userPhone[1] = userPhone[1].replace("-", "");
                }
            }
        }
        return userPhone;
    }

    /**
     * 获取所有联系人信息
     *
     * @return
     */
    public ArrayList<LinkmanItemBean> getAllContacts() {
        //获取联系人信息的Uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //获取ContentResolver
        ContentResolver contentResolver = this.getContentResolver();
        //查询数据，返回Cursor
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        ArrayList<LinkmanItemBean> list = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            LinkmanItemBean itemBean = new LinkmanItemBean();
            String name = "";
            String telephone = "";
            String company = "";
            String remarks = "";
            StringBuilder sb = new StringBuilder();
            //获取联系人的ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人的姓名
            String nameStr = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            if (nameStr != null && !nameStr.equals("")) {
                name = nameStr;
            }
            //构造联系人信息
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));//联系人ID
            //查询电话类型的数据操作
            Cursor phoneCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null, null);
            if (phoneCur != null) {
                while (phoneCur.moveToNext()) {
                    String phoneNumber = phoneCur.getString(phoneCur.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (phoneNumber != null && !phoneNumber.equals("")) {
                        phoneNumber = phoneNumber.replace(" ", "").replace("-", "");
                        //添加Phone的信息
                        sb.append(phoneNumber).append(",");
                    }
                }
            }
            phoneCur.close();
            if (sb.toString().length() > 0 && sb.toString().contains(",")) {
                telephone = sb.toString().substring(0, sb.toString().length() - 1);
            }
            //查询==备注==类型的数据操作.Note.NOTE  ContactsContract.Data.CONTENT_URI
            String remarkWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            String[] remarkWhereParams = new String[]{id,
                    ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
            Cursor remarkCur = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                    null, remarkWhere, remarkWhereParams, null);
            if (remarkCur != null && remarkCur.moveToFirst()) {
                //查询备注
                String remarkStr = remarkCur.getString(remarkCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                if (remarkStr != null && !remarkStr.equals("")) {
                    remarks = remarkStr;
                }
            }
            remarkCur.close();

            //查询==公司名字==类型的数据操作.Organization.COMPANY  ContactsContract.Data.CONTENT_URI
            String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            String[] orgWhereParams = new String[]{id,
                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
            Cursor orgCur = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                    null, orgWhere, orgWhereParams, null);
            if (orgCur != null && orgCur.moveToFirst()) {
                //组织名 (公司名字)
                String companyStr = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                if (companyStr != null && !companyStr.equals("")) {
                    company = companyStr;
                }
            }
            orgCur.close();
            itemBean.setName(name);//姓名
            itemBean.setTelephone(telephone);//手机号
            itemBean.setCompany(company);//公司
            itemBean.setRemarks(remarks);//备注
            if (list.size() > 1000) {
                return list;
            } else {
                list.add(itemBean);
            }
            LogUtils.d("debug", "telephone----------->" + telephone);
            LogUtils.d("debug", "itemBean--------->" + itemBean.toString());
        }
        LogUtils.d("debug", "contacts list--------->" + list.toString());
        cursor.close();
        return list;
    }


    // 提交基本信息接口
    private void submitBasicInfo(String custHouseAddr, String custMarriage, String contactName, String contactPhone, String familyFriendType) {

        if(TextUtils.isEmpty(email_ed.getText())){
            showToast("邮箱不能为空" , Constants.TOAST_SHOW_POSITION);
            return;
        }

        if(!SysUtil.isEmail(email_ed.getText().toString())){
            showToast("邮箱不符合规则" , Constants.TOAST_SHOW_POSITION);
            return;
        }

        showLoading("信息上传中，请稍后...");
        mSubmitBtn.setClickable(false);
        mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_selected_bg);
        Map<String, String> requestParams = new HashMap<>();

        requestParams.put("transCode", Constants.TRANS_CODE_UPLOAD_BASIC_INFO);//接口标识
        requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
        requestParams.put("clientToken", sharePrefer.getToken());//登录后token

        requestParams.put("fundId",sharePrefer.getXJFundId());
        requestParams.put("productId",sharePrefer.getXJProductId());

//        requestParams.put("custName","张三999");//客户名称
        requestParams.put("credType","1");//证件类型
//        requestParams.put("credNo","123456789123456789");//身份证号
        requestParams.put("custMobile",sharePrefer.getPhone());
        requestParams.put("custHouseAddr", familyAddress);//家庭地址
        requestParams.put("detailAddr", familyDetailAddress);//家庭地址
        requestParams.put("custMarriage", custMarriage);//婚姻状况
        requestParams.put("mailbox",email_ed.getText().toString());

        requestParams.put("contactName", contactName);//联系人姓名
        requestParams.put("mobileNumber", contactPhone);//联系人电话
        requestParams.put("familyFriendType", familyFriendType);//与联系人关系

        LogUtils.d("debug", "基本信息上传: requestParams--->" + requestParams.toString());
        sendPostRequest(requestParams, new ResultCallBack() {
            @Override
            public void onSuccess(String data) {
                dismissLoading();
                LogUtils.d(TAG, "基本信息上传: data--->" + data);
                LogUtils.d(TAG, "基本信息上传: getReplenisherPages--->" + sharePrefer.getReplenisherPages());
                MainMessage mainMessage = new MainMessage();
                Intent intent = new Intent(BasicInfoActivity.this,WebViewActivity.class);

                //判断是否有 补充信息页面
                if(!TextUtils.isEmpty(sharePrefer.getReplenisherPages())){
                    mainMessage.setTitle("补充信息");
                    mainMessage.setUrl(BASE_URL + sharePrefer.getReplenisherPages());
//                    intent.putExtra("url",BASE_URL + sharePrefer.getReplenisherPages());
//                    intent.putExtra("title","补充信息");
                }else {
                    mainMessage.setTitle("审核中");
                    mainMessage.setUrl(BASE_URL + sharePrefer.getCedStatus());
//                    intent.putExtra("url",BASE_URL + sharePrefer.getCedStatus());
//                    intent.putExtra("title","审核中");
                }

                EventBus.getDefault().post(mainMessage);
                startActivity(intent);
                finish();


//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(data);
//                    String merchantName = jsonObject.getString("merchantName");// 商户名称
//                    sharePrefer.setMerchantName(merchantName);
//                    getMerchantURL("0");

//                } catch (JSONException e) {
//                    LogUtils.e("error", "数据解析有误" + e.toString());
//                    showToast("数据格式有误!", Constants.TOAST_SHOW_POSITION);
//                    mSubmitBtn.setClickable(true);
//                    mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
//                }
            }

            @Override
            public void onError(String retrunCode, String errorMsg) {
                LogUtils.d(TAG, "基本信息上传: errorMsg--->" + errorMsg);
                dismissLoading();
                mSubmitBtn.setClickable(true);
                mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
            }

            @Override
            public void onFailure(String errorMsg) {
                LogUtils.d(TAG, "基本信息2上传: errorMsg--->" + errorMsg);
                dismissLoading();
                mSubmitBtn.setClickable(true);
                mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
            }
        });
    }

    //查询补充信息h5地址
//    private void getMerchantURL(String type) {
//        Map<String, String> requestParams = new HashMap<>();
//        requestParams.put("transCode", Constants.TRANS_CODE_QUERY_H5_URL);//接口标识
//        requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
//        requestParams.put("clientToken", sharePrefer.getToken());//登录后token
//        requestParams.put("type", type);//H5页面标识
//        sendPostRequest(requestParams, new ResultCallBack() {
//            @Override
//            public void onSuccess(String data) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(data);
//                    String merchantURL = jsonObject.getString("h5url");//补充信息页面地址
//                    Intent intent = new Intent(BasicInfoActivity.this, MerchatBusisnessActivity.class);
//                    intent.putExtra("url", Constants.BASE_URL + merchantURL);//补充信息页面地址
//                    startActivity(intent);
//                    BasicInfoActivity.this.finish();
//                    mSubmitBtn.setClickable(true);
//                    mSubmitBtn.setBackgroundResource(R.mipmap.m_icon_common_button_normal_bg);
//
//
//                    dismissLoading();
//                } catch (JSONException e) {
//                    dismissLoading();
//                    LogUtils.e("error", "数据解析有误" + e.toString());
//                    showToast("数据格式有误!", Constants.TOAST_SHOW_POSITION);
//                }
//            }
//
//            @Override
//            public void onError(String retrunCode, String errorMsg) {
//                dismissLoading();
//            }
//
//            @Override
//            public void onFailure(String errorMsg) {
//                dismissLoading();
//            }
//        });
//    }

    // 提交所有联系人信息接口
    private void submitConstantsInfo(ArrayList<LinkmanItemBean> allContactsInfo) {

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("transCode", Constants.TRANS_CODE_UPLOAD_ALL_CONTANTS_INFO);//接口标识
        requestParams.put("channelNo", Constants.CHANNEL_NO);//渠道标识
        requestParams.put("clientToken", sharePrefer.getToken());//登录后token
        requestParams.put("rowsCount", allContactsInfo.size() + "");//联系人信息条数
        requestParams.put("mailJson", new Gson().toJson(allContactsInfo));//所有联系人信息
        LogUtils.d("debug", "联系人信息上传: requestParams--->" + requestParams.toString());
        HttpInfo httpInfo = HttpInfo.Builder()
                .setUrl(Constants.REQUEST_URL)//请求URL
                .addParams(requestParams).build();
        OkHttpUtil.getDefault(this).doPostAsync(httpInfo, new Callback() {
            @Override
            public void onSuccess(HttpInfo info) throws IOException {
                String result = info.getRetDetail().toString();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String returnCode = jsonObject.getString("returnCode");
                    String returnMsg = jsonObject.getString("returnMsg");
                    if ("000000".equals(returnCode)) {
                        LogUtils.d("debug", "联系人信息上传 Success result------------->" + result);
                    } else if ("E999985".equals(returnCode) || "ES00000303".equals(returnCode)) {
                        dismissLoading();
                        LogUtils.d("debug", "--------------token失效，或者用户未登录-------------");
//                        sharePrefer.setLogin(false);
//                        showToast(returnMsg,Constants.TOAST_SHOW_POSITION);
//                        if(!sharePrefer.iSLogin())
//                        {
//                            //重新获取token
////                            registerLogin(sharePrefer.getMerchantId(),sharePrefer.getPhone());
//                            Intent intent = new Intent(BaseActivity.this,LoanStateDealActivity.class);
//                            startActivity(intent);
//                        }
                    } else {
//                        showToast(returnMsg,Constants.TOAST_SHOW_POSITION);
//                        callBack.onError(returnCode,returnMsg);
                    }
                } catch (JSONException e) {
                    LogUtils.e("error", "数据解析有误" + e.toString());
//                    showToast("数据格式有误!", Constants.TOAST_SHOW_POSITION);
                }
            }

            @Override
            public void onFailure(HttpInfo info) throws IOException {
                String result = info.getRetDetail().toString();
                LogUtils.d("debug", "-联系人信息上传------------>" + result);
                //showToast(result,Constants.TOAST_SHOW_POSITION);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
          }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//
//            Intent backIntent = new Intent(BasicInfoActivity.this, LoanStateDealActivity.class);
//            startActivity(backIntent);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}