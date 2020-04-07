package com.jimetec.xunji.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.baseview.base.BaseActivity;
import com.jimetec.xunji.R;
import com.jimetec.xunji.rx.RxBus;
import com.jimetec.xunji.rx.event.LoginEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tvLogin)
    TextView mTvLogin;
    @BindView(R.id.vLogin)
    View mVLogin;
    @BindView(R.id.llLogin)
    LinearLayout mLlLogin;
    @BindView(R.id.tvRegister)
    TextView mTvRegister;
    @BindView(R.id.vRegister)
    View mVRegister;
    @BindView(R.id.llRegister)
    LinearLayout mLlRegister;
    @BindView(R.id.flContent)
    FrameLayout mFlContent;
    private FragmentManager mManager;
    private RegisterFragment mRegisterFragment;
    private LoginFragment mLoginFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mManager = getSupportFragmentManager();
        mLoginFragment = new LoginFragment();
        mRegisterFragment = new RegisterFragment();
        changeFrament(mCurrentFragment,mLoginFragment);
        subscriberEvent();
    }

    boolean isLogin = true;

    @OnClick({R.id.llLogin, R.id.llRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llLogin:
                isLogin = true;
                changeFrament(mCurrentFragment,mLoginFragment);
                break;
            case R.id.llRegister:
                isLogin = false;
                changeFrament(mCurrentFragment,mRegisterFragment);
                break;
        }
    }


    public void showSelectPage() {
        mLlLogin.setEnabled(!isLogin);
        mTvLogin.setEnabled(!isLogin);
        mVLogin.setVisibility(isLogin ? View.VISIBLE : View.INVISIBLE);

        mLlRegister.setEnabled(isLogin);
        mTvRegister.setEnabled(isLogin);
        mVRegister.setVisibility(isLogin ? View.INVISIBLE : View.VISIBLE);

    }

    private Fragment mCurrentFragment;

    public void changeFrament(Fragment from,Fragment to) {
        showSelectPage();
        FragmentTransaction transaction = mManager.beginTransaction();
//        transaction.replace(R.id.flContent, to).commit();
        if (from == null) {
            if (to.isAdded()) {
                transaction.show(to).commit();
            } else {
                transaction.add(R.id.flContent, to).commit();
            }
        } else {
            if (to.isAdded()) {
                transaction.hide(from).show(to).commit();
            } else {
                transaction.hide(from).add(R.id.flContent, to).commit();
            }
        }
//        if (to.isAdded()) {
//            transaction.hide(from).show(to).commit();
//        } else {
//            transaction.hide(from).add(R.id.flContent, to).commit();
//        }
//        transaction.replace(R.id.flContent, to).commit();

        mCurrentFragment = to;
    }


    private  Disposable mDisposable;


    private void subscriberEvent() {
        mDisposable   = RxBus.getDefault()
                .toFlowable(LoginEvent.class)
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        isLogin = true;
                        changeFrament(mCurrentFragment,mLoginFragment);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();
        super.onDestroy();
    }

}
