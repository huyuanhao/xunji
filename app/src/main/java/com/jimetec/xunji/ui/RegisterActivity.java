package com.jimetec.xunji.ui;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.common.baseview.base.AbsCommonActivity;
import com.common.baseview.event.EventDataUtil;
import com.common.lib.utils.SpUtil;
import com.common.lib.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.Constants;
import com.jimetec.xunji.MainActivity;
import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.UserBean;
import com.jimetec.xunji.http.client.RequestClient;
import com.jimetec.xunji.presenter.RegisterPresenter;
import com.jimetec.xunji.presenter.contract.RegisterContract;
import com.jimetec.xunji.util.UserUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends AbsCommonActivity<RegisterPresenter> implements RegisterContract.View {


    public static final String IS_AGREE_VIP = "is_agree_vip";
    @BindView(R.id.etPhone)
    EditText mEtPhone;
    @BindView(R.id.etCode)
    EditText mEtCode;
    @BindView(R.id.tvGetCode)
    TextView mTvGetCode;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;
    @BindView(R.id.tvAgree)
    TextView mTvAgree;
    @BindView(R.id.tvAgreeCost)
    TextView mTvAgreeUse;


    CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            int time = (int) Math.ceil((millisUntilFinished / 1000));
            mTvGetCode.setText(time + "秒后重发");
            mTvGetCode.setEnabled(false);
        }

        @Override
        public void onFinish() {
            mTvGetCode.setText("获取验证码");
            mTvGetCode.setEnabled(true);
        }
    };
    @BindView(R.id.tvAgreementCost)
    TextView mTvAgreementCost;
    @BindView(R.id.tvAgreement)
    TextView mTvAgreement;

    private String mMobile;

    @Override
    public RegisterPresenter getPresenter() {
        return new RegisterPresenter(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initViewAndData() {

        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);

        String vip = getResources().getString(R.string.agree_vip_des);
        String web_vip = String.format(vip, getResources().getString(R.string.app_name));

        String ues = getResources().getString(R.string.agree_app_user_des);
        String web_ues = String.format(ues, getResources().getString(R.string.app_name));

        mTvAgreementCost.setText(web_vip);
        mTvAgreement.setText(web_ues);

//        SpannableStringBuilder sb = new SpannableStringBuilder("同意 《服务协议及隐私条款》");
//        ClickableSpan clickableSpan2 = new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
////                ToastUtil.showShort("eee");
//                mTvSubmit.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(new Intent(RegisterActivity.this, MyWebViewActivity.class));
//                    }
//                });
//            }
//
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                ds.setUnderlineText(false); //去除下划线
//            }
//        };
//        sb.setSpan(clickableSpan2, 3, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        sb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_theme)), 3, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mTvAgree.setSelected(true);
        boolean isAgree = getIntent().getBooleanExtra(IS_AGREE_VIP, false);

        if (isAgree) {
            mTvAgreeUse.setSelected(true);
            mTvAgree.setSelected(true);
        }
//        mTvAgree.setText(sb);
//        mTvAgree.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }


    @OnClick({R.id.tvGetCode, R.id.tvSubmit, R.id.tvAgree, R.id.tvAgreement, R.id.tvAgreeCost, R.id.tvAgreementCost})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvGetCode:
                getCode();
                break;
            case R.id.tvSubmit:
                onSubmit();
                break;
            case R.id.tvAgreementCost:

                String sAgeFormat = getResources().getString(R.string.title_web_vip);
                String web_vip = String.format(sAgeFormat, getResources().getString(R.string.app_name));
                MyWebViewActivity.startTo(mActivity, Constants.LOGIN_URL_COST, web_vip, web_vip);

 
                break;
            case R.id.tvAgreeCost:
                mTvAgreeUse.setSelected(!mTvAgreeUse.isSelected());
                break;
            case R.id.tvAgreement:
                String web_use = getResources().getString(R.string.title_web_use);
                String title_use = String.format(web_use, getResources().getString(R.string.app_name));
                MyWebViewActivity.startTo(mActivity, Constants.LOGIN_URL_USE, title_use, title_use);
 

                break;
            case R.id.tvAgree:
                mTvAgree.setSelected(!mTvAgree.isSelected());
                break;
        }
    }


    public void onSubmit() {

//         View view=getWindow().getDecorView().findFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        mMobile = mEtPhone.getText().toString();
        if (TextUtils.isEmpty(mMobile) || mMobile.length() < 11) {
            ToastUtil.showShort("请输入正确的手机号");
            return;
        }
        String code = mEtCode.getText().toString();

        if (TextUtils.isEmpty(code)) {
            ToastUtil.showShort("请输入验证码");
            return;
        }
        hideKeyboard();

//        String mPassword = mEtPassword.getText().toString().trim();
////        String confirm = mEtPasswordConfirm.getText().toString();
//
//        if (TextUtils.isEmpty(mPassword)) {
//            ToastUtil.showShort("密码不能为空");
//            return ;
//        }

////        判断密码长度是否在6-16位
//        int l = mPassword.length();
//        if (l < 6 || l > 16) {
//            ToastUtil.showShort("请输入6-16位密码");
//            return ;
//        }


        if (!mTvAgreeUse.isSelected()) {
            ToastUtil.showShort("请同意付费会员协议");
            return;
        }


        if (!mTvAgree.isSelected()) {
            ToastUtil.showShort("请同意用户服务协议");
            return;
        }

        mPresenter.register(mMobile, code);
    }


    public void getCode() {
        String mobile = mEtPhone.getText().toString();
        if (TextUtils.isEmpty(mobile) || mobile.length() < 11) {
            ToastUtil.showShort("请输入正确的手机号");
            return;
        }
        mEtCode.requestFocus();
        show(mEtCode);
        mPresenter.getCode(mobile);
//        mPresenter.getCode(getAesPhone());
    }


    public void show(View view) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }


    @Override
    public void backRegister(UserBean user) {
        RequestClient.getInstance().upUmengpId(EventDataUtil.getUmDeviceToken(), EventDataUtil.getApplicationId(), EventDataUtil.getEventChannel());

//        RxBus.getDefault().post(new LoginEvent(mMobile));
        UserUtil.setUser(user);
        EventDataUtil.setPhone(user.phone);
        EventDataUtil.setUserId(user.userId);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        if (user!=null && user.lastLocationTimes ==0 ){
            SpUtil.putBoolean(Constants.LOGIN_REFRESH_NEWS,true);
        }

        finish();
    }

    @Override
    public void backGetCode() {
        ToastUtil.showShort("验证码发送成功");
        timer.start();
    }

//    @OnClick({R.id.tvGetCode, R.id.tvSubmit, R.id.tvAgree})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tvGetCode:
//                break;
//            case R.id.tvSubmit:
//                break;
//            case R.id.tvAgree:
//                break;
//        }
//    }

    @Override
    public String getEventMode() {

        return "登录";
    }

}
