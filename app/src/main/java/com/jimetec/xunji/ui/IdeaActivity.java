package com.jimetec.xunji.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.SystemClock;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.baseview.base.AbsCommonActivity;
import com.common.baseview.event.EventDataBean;
import com.common.baseview.event.EventHeadData;
import com.common.lib.utils.GsonUtil;
import com.common.lib.utils.SpUtil;
import com.common.lib.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.Constants;
import com.jimetec.xunji.R;
import com.jimetec.xunji.presenter.IdeaPresenter;
import com.jimetec.xunji.presenter.contract.IdeaContract;
import com.jimetec.xunji.util.UserUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class IdeaActivity extends AbsCommonActivity<IdeaPresenter> implements IdeaContract.View {

    @BindView(R.id.ivTitleLeft)
    ImageView mIvTitleLeft;
    @BindView(R.id.rlTitleLeft)
    RelativeLayout mRlTitleLeft;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tv_wx)
    TextView mTvWx;
    @BindView(R.id.rl_wx)
    RelativeLayout mRlWx;
    @BindView(R.id.tv_qq)
    TextView mTvQq;
    @BindView(R.id.tv_email)
    TextView mTvEmail;
    @BindView(R.id.rl_qq)
    RelativeLayout mRlQq;
    @BindView(R.id.rl_history)
    RelativeLayout mRlHistory;
    @BindView(R.id.et)
    TextInputEditText mEt;
    @BindView(R.id.til)
    TextInputLayout mTil;
    @BindView(R.id.tvTest)
    TextView mTvTest;
    @BindView(R.id.bt_submit)
    Button mBtSubmit;


    @Override
    public IdeaPresenter getPresenter() {
        return new IdeaPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_idea;
    }

    @Override
    public void initViewAndData() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);

        StatusBarUtil.setLightMode(this);
        mTvTitle.setText("客服支持");
    }

    @Override
    public void backLeaveWord(Object contactUs) {
        if (mEt != null) {
            mEt.setText("");
        }
        ToastUtil.showShort("留言成功");
        finish();
    }

    @OnClick({R.id.rlTitleLeft, R.id.rl_email, R.id.rl_wx, R.id.rl_qq, R.id.rl_history, R.id.bt_submit, R.id.tvTest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlTitleLeft:
                finish();
                break;
            case R.id.rl_email:
                String email = mTvEmail.getText().toString();
                if (!TextUtils.isEmpty(email)) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(email);
                    Toast.makeText(this, "复制成功", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.rl_wx:
                break;

            case R.id.rl_qq:
                break;
            case R.id.rl_history:
                break;
            case R.id.bt_submit:
                submitWord();
                break;

            case R.id.tvTest:
                test();
                break;
        }
    }

    long[] mHits = new long[10];

    public void test() {
        if (UserUtil.isTest()){
            String text = "\n\n" + " ===    EventHeadData   ===  " + "\n\n" +
                    GsonUtil.toGsonString(new EventHeadData()) + "\n\n" + " ===    DataBean   ===  " + "\n\n" +
                    GsonUtil.toGsonString(new EventDataBean());
            mTvTest.setText(text);
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(text);
        }else {
            //每点击一次 实现左移一格数据
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            //给数组的最后赋当前时钟值
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            long time = mHits[mHits.length - 1] - mHits[0];
            //当0出的值大于当前时间-200时  证明在200秒内点击了2次
            if (time < 2000 && time > 0) {
                SpUtil.putBoolean(Constants.TEST_DEBUG, true);
                String text = "\n\n" + " ===    EventHeadData   ===  " + "\n\n" +
                        GsonUtil.toGsonString(new EventHeadData()) + "\n\n" + " ===    DataBean   ===  " + "\n\n" +
                        GsonUtil.toGsonString(new EventDataBean());
                mTvTest.setText(text);
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(text);
            }
        }
    }


    public void submitWord() {
        String content = mEt.getText().toString();
        if (TextUtils.isEmpty(content) || content.length() < 5) {
            ToastUtil.showShort("留言不得低于五个文字");
            return;
        }
        mPresenter.leaveWord(content);
    }


    @Override
    public String getEventMode() {

        return "客服支持";
    }
}
