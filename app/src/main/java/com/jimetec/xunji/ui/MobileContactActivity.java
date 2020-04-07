package com.jimetec.xunji.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.baseview.base.AbsLoadActivity;
import com.common.baseview.dialog.CommonDialogFragment;
import com.common.lib.utils.SpUtil;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.Constants;
import com.jimetec.xunji.R;
import com.jimetec.xunji.adapter.MobileContactAdapter;
import com.jimetec.xunji.bean.ContactBean;
import com.jimetec.xunji.presenter.MobileContactPresenter;
import com.jimetec.xunji.presenter.contract.MobileContactContract;
import com.jimetec.xunji.share.ShareActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MobileContactActivity extends AbsLoadActivity<MobileContactPresenter> implements MobileContactContract.View {

    public static final String NICK_NAME = "nick_name";

    @BindView(R.id.ivTitleLeft)
    ImageView mIvTitleLeft;
    @BindView(R.id.rlTitleLeft)
    RelativeLayout mRlTitleLeft;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.ivTitleRight)
    ImageView mIvTitleRight;
    @BindView(R.id.ivRemind)
    ImageView mIvRemind;
    @BindView(R.id.rlTitleRight)
    RelativeLayout mRlTitleRight;
    @BindView(R.id.rlTitleLayout)
    RelativeLayout mRlTitleLayout;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.base_view)
    FrameLayout mBaseView;
    private MobileContactAdapter mAdapter;
    String  nickName;
    @Override

    public MobileContactPresenter getPresenter() {
        return new MobileContactPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mobile_contact;
    }

    @Override
    public void initViewAndData() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
        mTvTitle.setText("添加好友");
        nickName = getIntent().getStringExtra(NICK_NAME);
        if (TextUtils.isEmpty(nickName)){
            finish();
            return;
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mAdapter = new MobileContactAdapter(this);
        mRv.setAdapter(mAdapter);
        mAdapter.addOnSelectContactClickListener(new MobileContactAdapter.OnSelectContactClickListener() {
            @Override
            public void selectContact(ContactBean bean) {
//                Intent intent = new Intent();
//                intent.putExtra(ContactBean.TAG,bean);
//                setResult(Activity.RESULT_OK,intent);
                addFriend(bean);
//                finish();
            }
        });

        loadingNetData();
    }


    public void addFriend(ContactBean bean) {
        mPresenter.addFriend(nickName,bean);
    }

    @Override
    public void loadingNetData() {
        super.loadingNetData();
        mPresenter.getContacts();
    }

    @Override
    public void backAdd(ContactBean bean) {
        bean.type =1;
        String history = SpUtil.getString(Constants.ADD_FRIENDs_HISTORY, "");
        history+= bean.emergencyPhone+",";
        SpUtil.putString(Constants.ADD_FRIENDs_HISTORY,history);
        mAdapter.notifyDataSetChanged();

        showSharePop();
    }

    @Override
    public void backDatas(List<ContactBean> datas) {
        if (datas == null || datas.size() == 0) {
            showEmptyPage();
        } else {
            mAdapter.setList(datas);
            showSuccessPage();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.rlTitleLeft)
    public void onViewClicked() {
        finish();
    }


    @Override
    public String getEventMode() {
        return "添加好友";
    }

    public void showSharePop() {
        new CommonDialogFragment.Builder()
                .showTitle(false)
                .setBtLeftColor(R.color.colorPrimary)
                .setBtRightColor(R.color.color_F5445C)
                .setContentText("安装短信已发送给对方！\n" +
                        "您还可以通过微信分享给TA")
                .setRightButtonText("立即分享")
                .setRightButtonClickListener(new CommonDialogFragment.OnClickListener() {
                    @Override
                    public void onClick(CommonDialogFragment dialogFragment, int which, String content) {
                        Intent share = new Intent(MobileContactActivity.this, ShareActivity.class);
                        startActivity(share);
                        dialogFragment.dismiss();
                    }


                })
                .create()
                .show(getSupportFragmentManager());

    }

}
