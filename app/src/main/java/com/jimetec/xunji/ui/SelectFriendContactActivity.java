package com.jimetec.xunji.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.baseview.base.AbsLoadActivity;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.R;
import com.jimetec.xunji.adapter.SelectContactAdapter;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.presenter.SelectContactPresenter;
import com.jimetec.xunji.presenter.contract.SelectContactContract;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectFriendContactActivity extends AbsLoadActivity<SelectContactPresenter> implements SelectContactContract.View {


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
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.base_view)
    FrameLayout mBaseView;
    private SelectContactAdapter mAdapter;

    @Override
    public SelectContactPresenter getPresenter() {
        return new SelectContactPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_friend_select_contact;
    }

    @Override
    public void initViewAndData() {

        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
        mTvTitle.setText("选择紧急联系人");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mAdapter = new SelectContactAdapter(this);
        mRv.setAdapter(mAdapter);
        mAdapter.addOnSelectItemClickListener(new SelectContactAdapter.OnSelectFriendItemClickListener() {
            @Override
            public void selectFriendInfoClick(FriendBean userBean) {

                Intent intent = new Intent();
                intent.putExtra(FriendBean.TAG,userBean);
                setResult(Activity.RESULT_OK,intent);
                finish();

            }
        });
        loadingNetData();


    }

    @Override
    public void loadingNetData() {
        super.loadingNetData();
        mPresenter.getFriends();
    }

    @Override
    public void backFriends(List<FriendBean> pageBean) {
        if (pageBean.size() ==0){
            showEmptyPage();
        }else {
            showSuccessPage();
            mAdapter.setList(pageBean);
        }

    }



    @OnClick(R.id.rlTitleLeft)
    public void onViewClicked() {
        finish();
    }


    @Override
    public String getEventMode() {
        return "选择紧急联系人";
    }
}
