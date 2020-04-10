package com.jimetec.xunji.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.common.baseview.base.BaseActivity;
import com.common.lib.utils.SpUtil;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.Constants;
import com.jimetec.xunji.MainActivity;
import com.jimetec.xunji.R;
import com.jimetec.xunji.transformer.AccordionPageTransformer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity {


    public static final String isPreVip = "isPreVip";
    @BindView(R.id.vp)
    ViewPager mVp;
    int[] indexs = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3,R.mipmap.guide_4};
//    @BindView(R.id.fl_come)
//    FrameLayout mFlCome;
    @BindView(R.id.rl_right)
    RelativeLayout mFlRight;
//    @BindView(R.id.tvSubmit)
//    TextView mTvSubmit;
//    @BindView(R.id.tvExplain)
//    TextView mTvExplain;
//    @BindView(R.id.llContent)
//    LinearLayout mLlContent;


    int  curIndex;
    boolean isScrolled;
    private boolean mPreVip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        SpUtil.putBoolean(Constants.BEFORE_SHOW_WELCOME,true);
        mPreVip = getIntent().getBooleanExtra(isPreVip, false);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
        WelcomeAdapter welcomeAdapter = new WelcomeAdapter();
        mVp.setOffscreenPageLimit(3);
//        mVp.setPageTransformer(true,new ZoomStackPageTransformer());
        mVp.setPageTransformer(true, new AccordionPageTransformer());
//        mVp.setPageTransformer(true,new AccordionPageTransformer());
//        mVp.setPageTransformer(true,new FadePageTransformer());
        mVp.setAdapter(welcomeAdapter);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                curIndex =i;
//                if (i == 2) {
//                    mFlCome.setVisibility(View.VISIBLE);
//                } else {
//                    mFlCome.setVisibility(View.GONE);
//
//                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {

                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING://拖曳状态
                        isScrolled = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING://拖曳后自动归为状态
                        isScrolled = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:////被用户滑动后的最终静止状态   并且是最后一页非滑动情况直接走相关的监听跳转


                        if (mVp.getCurrentItem() == mVp.getAdapter().getCount() - 1 && !isScrolled) {
                            if (mPreVip){
                                MyWebViewActivity.startToPreVip(mContext);
 
                            }else {
                                startActivity(new Intent(mContext,MainActivity.class));
                            }

                            finish();
//                            mGoToDianbaoListener.go(mPager.getAdapter().getCount() - 1);//这里使用了接口回调，可以根据自己的需求监听
                        }
                        isScrolled = true;

                        break;
                }
            }
        });
//        mFlCome.setVisibility(View.VISIBLE);


    }


    @OnClick({R.id.rl_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_right:
                startActivity(new Intent(this, MainActivity.class));
                finish();
//                animate(mLlContent.getPaddingBottom(),0);
                break;
//            case R.id.tvSubmit:
//                mLlContent.setPadding( 0, 0, 0, 500);
//                animate(mLlContent.getPaddingBottom(),200);
//                clicl();
//                SpUtil.putBoolean(ConstantUser.SPLASH_VERSION, false);
//                startActivity(new Intent(this, TestActivity.class));
//                finish();
//                break;
        }

    }


    public class WelcomeAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return indexs.length;
        }


        //view是否由对象产生，官方写arg0==arg1即可
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;

        }

        //销毁一个页卡(即ViewPager的一个item)
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        //对应页卡添加上数据
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(WelcomeActivity.this);
            imageView.setImageResource(indexs[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(imageView);//千万别忘记添加到container
            return imageView;
        }
    }


//
    @Override
    public String getEventMode() {
        return "欢迎页";
    }





}
