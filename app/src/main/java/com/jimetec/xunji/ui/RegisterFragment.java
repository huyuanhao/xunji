package com.jimetec.xunji.ui;


import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.common.baseview.base.AbsCommonFragment;
import com.common.lib.utils.ToastUtil;
import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.UserBean;
import com.jimetec.xunji.rx.RxBus;
import com.jimetec.xunji.rx.event.LoginEvent;
import com.jimetec.xunji.presenter.RegisterPresenter;
import com.jimetec.xunji.presenter.contract.RegisterContract;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends AbsCommonFragment<RegisterPresenter> implements RegisterContract.View {


    @BindView(R.id.etPhone)
    EditText mEtPhone;
    @BindView(R.id.etCode)
    EditText mEtCode;
    @BindView(R.id.tvGetCode)
    TextView mTvGetCode;
    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;
    @BindView(R.id.tvAgree)
    TextView mTvAgree;


    CountDownTimer timer = new CountDownTimer(10000, 1000) {
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
    private String mMobile;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public RegisterPresenter getPresenter() {
        return new RegisterPresenter(mActivity);
    }

    @Override
    public void initViewAndData() {
        SpannableStringBuilder sb = new SpannableStringBuilder("同意 《服务协议及隐私条款》");
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                ToastUtil.showShort("eee");
//                startActivity(new Intent(getActivity(), LoanProtocolActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false); //去除下划线
            }
        };
        sb.setSpan(clickableSpan2, 3, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.color_theme)), 3, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvAgree.setText(sb);
        mTvAgree.setSelected(true);
        mTvAgree.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }





    @OnClick({R.id.tvGetCode, R.id.tvSubmit, R.id.tvAgree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvGetCode:
                getCode();
                break;
            case R.id.tvSubmit:
                onSubmit();
                break;
            case R.id.tvAgree:
                mTvAgree.setSelected(!mTvAgree.isSelected());
                break;
        }
    }



    public void onSubmit() {

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

       String mPassword = mEtPassword.getText().toString().trim();
//        String confirm = mEtPasswordConfirm.getText().toString();

        if (TextUtils.isEmpty(mPassword)) {
            ToastUtil.showShort("密码不能为空");
            return ;
        }

//        判断密码长度是否在6-16位
        int l = mPassword.length();
        if (l < 6 || l > 16) {
            ToastUtil.showShort("请输入6-16位密码");
            return ;
        }

        if (!mTvAgree.isSelected()){
            ToastUtil.showShort("请同意服务协议");
            return;
        }

        mPresenter.register(mMobile,code);
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
        RxBus.getDefault().post(new LoginEvent(mMobile));




    }

    @Override
    public void backGetCode() {
        ToastUtil.showShort("验证码发送成功");
        timer.start();
    }
}
