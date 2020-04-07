package com.jimetec.xunji.ui;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.baseview.base.AbsCommonActivity;
import com.common.baseview.event.EventDataBean;
import com.common.baseview.event.EventHeadData;
import com.common.lib.utils.GsonUtil;
import com.common.lib.utils.SpUtil;
import com.common.lib.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.Constants;
import com.jimetec.xunji.R;
import com.jimetec.xunji.adapter.EmergencyAdapter;
import com.jimetec.xunji.bean.ContactBean;
import com.jimetec.xunji.presenter.ContactPresenter;
import com.jimetec.xunji.presenter.contract.ContactContract;
import com.jimetec.xunji.util.UserUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EmergencyActivity extends AbsCommonActivity<ContactPresenter> implements ContactContract.View {

    @BindView(R.id.ivTitleLeft)
    ImageView mIvTitleLeft;
    @BindView(R.id.rlTitleLeft)
    RelativeLayout mRlTitleLeft;
    @BindView(R.id.ll1)
    LinearLayout mLl1;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvSuccess)
    TextView mTvSuccess;
    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.ivTitleRight)
    ImageView mIvTitleRight;
    @BindView(R.id.ivRemind)
    ImageView mIvRemind;
    @BindView(R.id.rlTitleRight)
    RelativeLayout mRlTitleRight;
    @BindView(R.id.tvDown)
    TextView mTvDown;
    @BindView(R.id.tvTest)
    TextView mTvTest;

    @BindView(R.id.tvAddContact)
    TextView mTvAddContact;
    @BindView(R.id.tvCancel)
    TextView mTvCancel;
    @BindView(R.id.lv)
    ListView mLv;
    private EmergencyAdapter mAdapter;


    CountDownTimer timer = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            int time = (int) Math.ceil((millisUntilFinished / 1000));
            if (mTvDown != null) {
                mTvDown.setText(time + "");
            }
        }

        @Override
        public void onFinish() {
            if (mTvDown != null) {
                mTvDown.setText(5 + "");
                mTvCancel.setSelected(false);
                mTvCancel.setText("开始发送");
            }
            if (sendSuccess) mActivity.finish();
//            if (mActivity!=null){
//                mActivity.finish();
//            }
        }

    };


    @Override
    public ContactPresenter getPresenter() {
        return new ContactPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_emergency;
    }

    @Override
    public void initViewAndData() {
        mTvTitle.setText("紧急警报");
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
        mAdapter = new EmergencyAdapter(this);
        mLv.setAdapter(mAdapter);
//        mLl1.setVisibility(View.VISIBLE);
        if (!UserUtil.isVip()) {
            mTvTitle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    popNoVip();
                }
            },500);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadingNetData();
    }

    @Override
    public void loadingNetData() {
        super.loadingNetData();
        if (UserUtil.isVip()){
            mPresenter.getContacts();
        }
    }

    @OnClick({R.id.rlTitleLeft, R.id.tvAddContact, R.id.tvCancel,R.id.tvTest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlTitleLeft:
                finish();
                break;
            case R.id.tvAddContact:
//                popAddType();
                startActivity(new Intent(this, PopSelectContactActivity.class));
                break;
            case R.id.tvCancel:
                mTvCancel.setSelected(!mTvCancel.isSelected());
                if (mTvCancel.isSelected()) {
                    mTvDown.setText(5 + "");
                    mTvCancel.setText("取消发送");
                    timer.start();
                    String phones = "";


                    for (int i = 0; i < mBeans.size(); i++) {
                        if (i == mBeans.size() - 1) {
                            phones = phones + mBeans.get(i).emergencyPhone;
                        } else {
                            phones = phones + mBeans.get(i).emergencyPhone + ",";
                        }
                    }
                    mPresenter.sendMsg(phones);

                } else {
                    timer.cancel();
                    mTvCancel.setText("开始发送");

                }

                break;

            case R.id.tvTest:
                test();
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    List<ContactBean> mBeans;

    @Override
    public void backContacts(List<ContactBean> beans) {

        if (beans.size() > 2) {
            mTvCancel.setVisibility(View.VISIBLE);
            mTvAddContact.setVisibility(View.GONE);
        } else {
            if (beans.size() == 0) {
                mTvCancel.setVisibility(View.GONE);
            } else {
                mTvCancel.setVisibility(View.VISIBLE);
            }
            mTvAddContact.setVisibility(View.VISIBLE);
        }
        mAdapter.setData(beans);
        mLl1.setVisibility(View.VISIBLE);
        mBeans = beans;
        if (!UserUtil.isVip()) {
            popNoVip();
        } else {
            if (TextUtils.isEmpty(UserUtil.getRealName())) {
                popRealName();
            }
        }
    }

    boolean sendSuccess = false;

    @Override
    public void backAdd(Object obj) {
        sendSuccess = true;


        mTvDown.setText("");
        mTvDown.setEnabled(false);
        mTvSuccess.setVisibility(View.VISIBLE);
        mTvCancel.setVisibility(View.GONE);
        mTvAddContact.setVisibility(View.GONE);

        mTv1.setText("求救消息已发送给……");
        if (timer != null) {
            timer.cancel();
        }
//        ToastUtil.showShort("消息已发送给好友");
//        finish();
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void backDelete(Object obj) {

    }

    @Override
    public void backrealName(Object obj) {
//        ToastUtil.showShort("名字设置成功");

        startActivity(new Intent(this, PopSelectContactActivity.class));

    }


    boolean isNameSetting = false;

    public void popRealName() {
        isNameSetting = false;
        View playView = View.inflate(this, R.layout.pop_item_rename, null);
        final EditText etPhone = (EditText) playView.findViewById(R.id.etPhone);
        TextView tvName = (TextView) playView.findViewById(R.id.tvName);
        Button dialogLeftBtn = (Button) playView.findViewById(R.id.dialog_left_btn);
        Button dialogRightBtn = (Button) playView.findViewById(R.id.dialog_right_btn);
//        tvName.setText("请输入你的真实姓名");


        SpannableString spannableString = new SpannableString("请输入你的真实姓名");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvName.setText(spannableString);//其中，”默认颜色红颜色” 为你要改变的文本。setSpan方法有四个参数，ForegroundColorSpan是为文本设置前景色，也就是文字颜色。如果要为文字添加背景颜色，可替换为BackgroundColorSpan。4为文本颜色改变的起始位置，spannableString.length()为文本颜色改变的结束位置。最后一个参数为布尔型，可以传入以下四种。
        etPhone.setHint("真实姓名不可修改");
        final PopupWindow mPopupWindow = new PopupWindow(playView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // PopupWindow popupWindow = new PopupWindow(popuView, 100, 100);
        // 设置点击外部区域, 自动隐藏
        mPopupWindow.setOutsideTouchable(false); // 外部可触摸
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable()); // 设置空的背景, 响应点击事件
        mPopupWindow.setFocusable(true); //设置可获取焦点
        mPopupWindow.setTouchable(true);

        mPopupWindow.setAnimationStyle(R.style.addCareFriendAnim);  //添加动画

//        mPopupWindow.setAnimationStyle(R.style.popslide_style);
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideInput(etPhone);
                if (TextUtils.isEmpty(UserUtil.getRealName()) && !isNameSetting) {
                    finish();
                    return;
                }
                params.alpha = 1.0f;
                getWindow().setAttributes(params);

            }
        });
//
//

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.dialog_left_btn:

                        break;
                    case R.id.dialog_right_btn:

                        String string = etPhone.getText().toString();
                        if (TextUtils.isEmpty(string)) {
                            ToastUtil.showShort("姓名不能为空");
                            return;
                        }
                        if (string.length() > 6) {
                            ToastUtil.showShort("姓名不能超过6位");
                            return;
                        }
                        isNameSetting = true;
                        mPresenter.realName(string);
                        break;
                }


                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }

            }
        };
//        etPhone.setText(UserUtil.getNickName());

        dialogLeftBtn.setOnClickListener(onClickListener);
        dialogRightBtn.setOnClickListener(onClickListener);
        etPhone.postDelayed(new Runnable() {
            @Override
            public void run() {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etPhone, InputMethodManager.SHOW_FORCED);
            }
        }, 50);
    }


    public void popNoVip() {


        View playView = View.inflate(this, R.layout.pop_no_vip, null);
        TextView tvContent = (TextView) playView.findViewById(R.id.tvContent);

        Button dialogLeftBtn = (Button) playView.findViewById(R.id.dialog_left_btn);
        Button dialogRightBtn = (Button) playView.findViewById(R.id.dialog_right_btn);
        final PopupWindow mPopupWindow = new PopupWindow(playView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // PopupWindow popupWindow = new PopupWindow(popuView, 100, 100);
        // 设置点击外部区域, 自动隐藏
        mPopupWindow.setOutsideTouchable(false); // 外部可触摸
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable()); // 设置空的背景, 响应点击事件
        mPopupWindow.setFocusable(true); //设置可获取焦点
        mPopupWindow.setAnimationStyle(R.style.addCareFriendAnim);  //添加动画
//        mPopupWindow.setAnimationStyle(R.style.popslide_style);
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                if (!UserUtil.isVip()) {
                    finish();
                    return;
                }

                params.alpha = 1.0f;
                getWindow().setAttributes(params);

            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.dialog_left_btn:

                        break;
                    case R.id.dialog_right_btn:
                        MyWebViewActivity.startToAfterVip(mContext);

                        break;
                }
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        };

        dialogLeftBtn.setOnClickListener(onClickListener);
        dialogRightBtn.setOnClickListener(onClickListener);
    }


    @Override
    public String getEventMode() {
        return "紧急警报";
    }





    long[] mHits = new long[20];


    public void test() {
        try {
            if (UserUtil.isTest()){
                String text = "\n\n" + " ===    EventHeadData   ===  " + "\n\n" +
                        GsonUtil.toGsonString(new EventHeadData()) + "\n\n" + " ===    DataBean   ===  " + "\n\n" +
                        GsonUtil.toGsonString(new EventDataBean());
                mTvTest.setText(text);
                ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(text);
            }else {
                //每点击一次 实现左移一格数据
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                //给数组的最后赋当前时钟值
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                long time = mHits[mHits.length - 1] - mHits[0];
                //当0出的值大于当前时间-200时  证明在200秒内点击了2次
                if (time < 3000 && time > 0) {
                    SpUtil.putBoolean(Constants.TEST_DEBUG, true);
                    String text = "\n\n" + " ===    EventHeadData   ===  " + "\n\n" +
                            GsonUtil.toGsonString(new EventHeadData()) + "\n\n" + " ===    DataBean   ===  " + "\n\n" +
                            GsonUtil.toGsonString(new EventDataBean());
                    mTvTest.setText(text);
                    ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(text);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}