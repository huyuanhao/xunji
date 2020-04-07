package com.jimetec.xunji.ui;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.baseview.base.AbsCommonFragment;
import com.jimetec.xunji.R;
import com.jimetec.xunji.rx.event.LoginEvent;
import com.jimetec.xunji.presenter.LoginPresenter;
import com.jimetec.xunji.presenter.contract.LoginContract;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends AbsCommonFragment<LoginPresenter> implements LoginContract.View {


    @BindView(R.id.etPhone)
    EditText mEtPhone;
    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @BindView(R.id.tvForget)
    TextView mTvForget;
    @BindView(R.id.ivEyes)
    ImageView mIvEyes;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public LoginPresenter getPresenter() {
        return new LoginPresenter(mActivity);
    }

    @Override
    public void initViewAndData() {


    }


    public void backlogin() {

    }

    @Override
    public void dealLoginEvent(LoginEvent loginEvent) {
        if (!TextUtils.isEmpty(loginEvent.getPhone())){
            mEtPhone.setText(loginEvent.getPhone());
            mEtPassword.requestFocus();
            show(mEtPassword);
        }
    }

    public void show(View view) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }



    @OnClick({R.id.tvForget, R.id.ivEyes, R.id.tvSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvForget:
                break;
            case R.id.ivEyes:
                mIvEyes.setSelected(!mIvEyes.isSelected());
                openPasswd(mIvEyes.isSelected());
                break;
            case R.id.tvSubmit:
                openPasswd(false);
                break;
        }
    }



    public void openPasswd(boolean isOpen) {
        mIvEyes.setSelected(isOpen);
        if (isOpen) {
            // 显示为普通文本
            mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            // 使光标始终在最后位置
            Editable etable = mEtPassword.getText();
            Selection.setSelection(etable, etable.length());
        } else {
            // 显示为密码
            mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            // 使光标始终在最后位置
            Editable etable = mEtPassword.getText();
            Selection.setSelection(etable, etable.length());
        }
    }
}
