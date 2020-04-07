package com.jimetec.xunji.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.baseview.base.AbsLoadActivity;
import com.common.baseview.dialog.CommonDialogFragment;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.MyApplication;
import com.jimetec.xunji.R;
import com.jimetec.xunji.adapter.NewsAdapter;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.TimeSortBean;
import com.jimetec.xunji.presenter.NewsPresenter;
import com.jimetec.xunji.presenter.contract.NewsContract;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsActivity extends AbsLoadActivity<NewsPresenter> implements NewsContract.View {

    @BindView(R.id.ivTitleLeft)
    ImageView mIvTitleLeft;
    @BindView(R.id.rlTitleLeft)
    RelativeLayout mRlTitleLeft;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.base_view)
    FrameLayout mBaseView;
    private NewsAdapter mAdapter;


    @Override
    public NewsPresenter getPresenter() {
        return new NewsPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news;
    }

    @Override
    public void initViewAndData() {
        MyApplication.hasGloalNews=false;
        MyApplication.hasFriendNews=false;
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
        ButterKnife.bind(this);
        mTvTitle.setText(getEventMode());

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mAdapter = new NewsAdapter(this);
        mRv.setAdapter(mAdapter);
        mAdapter.addOnCareItemClickListener(new NewsAdapter.OnFriendItemClickListener() {
            @Override
            public void addFriend(FriendBean userBean) {
                accept(userBean.id);
            }
        });

    }

    public void accept (final long id){
        new CommonDialogFragment.Builder()
                .showTitle(false)
//                .setTitleText("是否删除位置")
                .setBtLeftColor(R.color.colorPrimary)
                .setBtRightColor(R.color.color_F5445C)
                .setContentText("接受好友申请后，您将对该好友公开您的位置信息。")
                .setRightButtonText("接受")
                .setLeftButtonText("取消")
                .setRightButtonClickListener(new CommonDialogFragment.OnClickListener() {
                    @Override
                    public void onClick(CommonDialogFragment dialogFragment, int which, String content) {
                        dialogFragment.dismiss();
                        mPresenter.agree(id);
                    }


                })
                .create()
                .show(getSupportFragmentManager());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingNetData();
    }

    @OnClick(R.id.rlTitleLeft)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void loadingNetData() {
        super.loadingNetData();
        mPresenter.getNews();
    }

    @Override
    public void backNews(List<TimeSortBean> pageBean) {
        if (pageBean ==null || pageBean.size()==0){
            showEmptyPage();
            return;
        }

        showSuccessPage();
        mAdapter.setList(pageBean);
    }

    @Override
    public void backAgree(Object obj) {
        loadingNetData();

    }


    @Override
    public String getEventMode() {
        return "我的消息";
    }
}
