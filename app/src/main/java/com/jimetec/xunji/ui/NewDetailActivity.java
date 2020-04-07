package com.jimetec.xunji.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.baseview.base.BaseActivity;
import com.common.lib.utils.LogUtils;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.NewsBean;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.LinkHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.LinkFixCallback;
import com.zzhoujay.richtext.callback.OnUrlClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewDetailActivity extends BaseActivity {

    @BindView(R.id.ivTitleLeft)
    ImageView mIvTitleLeft;
    @BindView(R.id.rlTitleLeft)
    RelativeLayout mRlTitleLeft;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.ivTitleRight)
    ImageView mIvTitleRight;
    @BindView(R.id.rlTitleRight)
    RelativeLayout mRlTitleRight;
    @BindView(R.id.tv_rich)
    TextView mTvRich;
    @BindView(R.id.tvNewsTitle)
    TextView mTvNewsTitle;
    private NewsBean mNewsBean;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            mNewsBean = (NewsBean) getIntent().getSerializableExtra(NewsBean.TAG);
        }
        mTvTitle.setText(getEventTitle());
        mTvNewsTitle.setText(mNewsBean.title);
        RichText.initCacheDir(this);
        init();

    }


    private void init() {
        RichText.from(mNewsBean.content).bind(this)
                .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                .urlClick(new OnUrlClickListener() {
                    @Override
                    public boolean urlClicked(String url) {
                        LogUtils.e("url" + url);
                        submitClickEvent(url, mNewsBean.title);
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);
//                        startToTitleWeb(url,mMyNewsBean.title,ConstantPageCode.page_new_detail_h5_mode,getClickReferer(mMyNewsBean.title));
                        return true;
                    }
                }) // 设置链接点击回调
                .linkFix(new LinkFixCallback() {
                    @Override
                    public void fix(LinkHolder holder) {
                        holder.setUnderLine(false);
                    }
                })
                .into(mTvRich);
    }


    @OnClick(R.id.rlTitleLeft)
    public void onViewClicked() {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.clear(this);
        RichText.recycle();
    }

    @Override
    public String getEventMode() {
        if (TextUtils.isEmpty(mEventMode)){
            return "消息详情";
        }

       return  mEventMode;
    }
}
