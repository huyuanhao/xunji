package com.jimetec.xunji.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.baseview.base.AbsCommonFragment;
import com.common.baseview.dialog.CommonDialogFragment;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.TimeUtils;
import com.common.lib.utils.ToastUtil;
import com.jimetec.basin.ui.ChangeIconActivity;
import com.jimetec.xunji.BuildConfig;
import com.jimetec.xunji.MyApplication;
import com.jimetec.xunji.R;
import com.jimetec.xunji.adapter.MyModuleAdapter;
import com.jimetec.xunji.bean.MyModuleBean;
import com.jimetec.xunji.bean.UserBean;
import com.jimetec.xunji.presenter.MyPresenter;
import com.jimetec.xunji.presenter.contract.MyContract;
import com.jimetec.xunji.share.ShareActivity;
import com.jimetec.xunji.util.Base64Helper;
import com.jimetec.xunji.util.ImageManager;
import com.jimetec.xunji.util.NoEmojiUtil;
import com.jimetec.xunji.util.UserUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends AbsCommonFragment<MyPresenter> implements MyContract.View {


    @BindView(R.id.ivIcon)
    ImageView mIvIcon;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvChangeName)
    TextView mTvChangeName;
    @BindView(R.id.llLogin)
    LinearLayout mLlLogin;
    @BindView(R.id.tvWeb)
    TextView mTvWeb;
    @BindView(R.id.tvIdea)
    TextView mTvIdea;
    @BindView(R.id.tvStar)
    TextView mTvStar;
    @BindView(R.id.tvShare)
    TextView mTvShare;
    @BindView(R.id.ll_logout)
    LinearLayout mLlLogout;
    @BindView(R.id.rlTitleLeft)
    RelativeLayout mRlTitleLeft;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.ivTitleLeft)
    ImageView mIvTitleLeft;
    @BindView(R.id.ivTitleRight)
    ImageView mIvTitleRight;
    @BindView(R.id.ivRemind)
    ImageView mIvRemind;
    @BindView(R.id.rlTitleRight)
    RelativeLayout mRlTitleRight;
    @BindView(R.id.rlTitleLayout)
    RelativeLayout mRlTitleLayout;
    @BindView(R.id.tvVip)
    TextView mTvVip;
    @BindView(R.id.ivVip)
    ImageView mIvVip;
    @BindView(R.id.ivLockMore)
    ImageView mIvLockMore;
    @BindView(R.id.rlVip)
    RelativeLayout mRlVip;
    @BindView(R.id.tvContact)
    TextView mTvContact;
    @BindView(R.id.tvNews)
    TextView mTvNews;

    @BindView(R.id.tvFriendTip)
    TextView mTvFriendTip;

    @BindView(R.id.tvExpireTime)
    TextView mTvExpireTime;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.tvContinueVip)
    TextView mTvContinueVip;
    private MyModuleAdapter mAdapter;


    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    protected void onFragmentResume(boolean isFirst, boolean isViewDestroyed) {
        super.onFragmentResume(isFirst, isViewDestroyed);
        if (isFirst) {
            mRlTitleLeft.setVisibility(View.GONE);
            mTvTitle.setText("我的");
        }
        updateInfoView();
        if (UserUtil.isLogined()) {
            mPresenter.my();
        }

    }


    public void updateInfoView() {

        if (UserUtil.isLogined()) {
            mLlLogout.setVisibility(View.VISIBLE);
            mTvName.setText(UserUtil.getNickName());

            if (TextUtils.isEmpty(UserUtil.getUser().getAvatar())) {
                ImageManager.loadAvatar(getContext(), R.mipmap.icon_setting_avatar, mIvIcon);
            } else {
                ImageManager.loadAvatar(getContext(), UserUtil.getUser().getAvatar(), mIvIcon);
            }
            mTvChangeName.setVisibility(View.VISIBLE);

            if (UserUtil.isVip()) {
                mIvVip.setEnabled(true);
                mIvLockMore.setVisibility(View.GONE);
                mTvVip.setText("功能已解锁");
                mTvContact.setVisibility(View.VISIBLE);

                if (UserUtil.getUser().expireTimes>0){
                    mTvExpireTime.setVisibility(View.VISIBLE);
                    mTvExpireTime.setText("到期时间: "+TimeUtils.millis2String(UserUtil.getUser().expireTimes));
                }else {
                    mTvExpireTime.setVisibility(View.GONE);

                }
                mTvVip.setTextColor(getResources().getColor(R.color.color_ABABAB));
//                mRv.setVisibility(View.GONE);


//                mRv.setVisibility(View.VISIBLE);
//                List<MyModuleBean> information = UserUtil.getInformation();
//                mAdapter.setList(information);
            } else {
//                mRv.setVisibility(View.GONE);
                controllerRowShow();
                mIvVip.setEnabled(false);
                mTvVip.setText("解锁功能");
                mTvVip.setTextColor(getResources().getColor(R.color.color_1C1C1C));
                mTvContact.setVisibility(View.GONE);
                mIvLockMore.setVisibility(View.VISIBLE);

                mTvContinueVip.setVisibility(View.GONE);
                mTvExpireTime.setVisibility(View.GONE);
                mTvExpireTime.setText("");
            }

            if (MyApplication.hasFriendNews){
                mTvFriendTip.setVisibility(View.VISIBLE);
            }else {
                mTvFriendTip.setVisibility(View.GONE);

            }
            controllerRowShow();
        } else {
            mRv.setVisibility(View.GONE);
            mTvFriendTip.setVisibility(View.GONE);
            mLlLogout.setVisibility(View.GONE);
            mTvName.setText("请先登录");
            ImageManager.loadAvatar(getContext(), R.mipmap.icon_avatar_deafult, mIvIcon);
            mTvChangeName.setVisibility(View.GONE);
            mTvContact.setVisibility(View.GONE);
            mIvVip.setEnabled(false);
            mTvVip.setText("解锁功能");
            mTvVip.setTextColor(getResources().getColor(R.color.color_1C1C1C));
            mIvLockMore.setVisibility(View.VISIBLE);
            mTvContinueVip.setVisibility(View.GONE);
            mTvExpireTime.setVisibility(View.GONE);
            mTvExpireTime.setText("");

        }
    }


    public void controllerRowShow(){
        List<MyModuleBean> information = UserUtil.getInformation();
        if (information ==null  || information.size() ==0){
            mRv.setVisibility(View.GONE);
        }else {
            mRv.setVisibility(View.VISIBLE);
            mAdapter.setList(information);
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public MyPresenter getPresenter() {
        return new MyPresenter(getActivity());
    }

    @Override
    public void initViewAndData() {
//        mTvShare.setVisibility(View.GONE);
//        mRv.setVisibility(View.GONE);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mAdapter = new MyModuleAdapter(mActivity);
        mRv.setAdapter(mAdapter);
        mAdapter.addOnMyModuleClickListener(new MyModuleAdapter.OnMyModuleClickListener() {
            @Override
            public void onModuleClick(MyModuleBean bean) {
                MyWebViewActivity.startTo(mActivity,bean.wordUrl, bean.useWord,  bean.useWord);

            }
        });

    }

    @OnClick({R.id.ivIcon, R.id.llLogin, R.id.tvWeb, R.id.rlNews, R.id.tvIdea,
            R.id.rlVip, R.id.tvContact,
            R.id.tvStar, R.id.tvShare, R.id.ll_logout,R.id.tvContinueVip})
    public void onViewClicked(View view) {
        if (!UserUtil.isLogined()) {
            Intent register = new Intent(getActivity(), RegisterActivity.class);
            startActivity(register);
//            MyWebViewActivity.startToPreVip(mActivity);
            return;
        }

        switch (view.getId()) {
            case R.id.ivIcon:
                Intent intent = new Intent(getActivity(), AvatarActivity.class);
                startActivityForResult(intent, 123);
                break;
            case R.id.llLogin:
                if (UserUtil.isLogined()) {
                    popRename();
//                    Intent ava = new Intent(getActivity(), AvatarActivity.class);
//                    startActivityForResult(ava, 123);
                } else {
                    Intent register = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(register);
                }
                break;
            case R.id.tvWeb:
                if (UserUtil.getUser().lastLocationTimes > 0) {
                    Intent last;
//                    if (BuildConfig.DEBUG){
//                         last = new Intent(getActivity(), QueryTestActivity.class);
//                    }else {
//                        last = new Intent(getActivity(), MyLastTrackActivity.class);
//                    }
//                    last = new Intent(getActivity(), QueryTestActivity.class);
                    last = new Intent(getActivity(), MyLastTrackActivity.class);
                    startActivity(last);
                } else {
                    ToastUtil.showShort("未发现我的轨迹数据");
                }


                break;
            case R.id.tvContinueVip:
                MyWebViewActivity.startToAfterVip(mContext);
            case R.id.rlVip:
                if (!UserUtil.isVip()) {
                    MyWebViewActivity.startToAfterVip(mContext);
                }

                break;
            case R.id.tvContact:
                Intent contact = new Intent(getActivity(), ContactActivity.class);
                startActivity(contact);

                break;
            case R.id.rlNews:
                Intent news = new Intent(getActivity(), NewsActivity.class);
                startActivity(news);
                break;
            case R.id.tvIdea:
                Intent idea = new Intent(getActivity(), IdeaActivity.class);
                startActivity(idea);
                break;
            case R.id.tvStar:
//                createNotification();
//                for (int i = 0; i <3; i++) {
//
//                    NewsBean newsBean = new NewsBean();
//                    if (i%2==0){
//                        newsBean.times= System.currentTimeMillis() - 1000*60*60*48;
//                    }else {
//
//                        newsBean.times= System.currentTimeMillis();
//                    }
//                    newsBean.title="title";
//                    newsBean.content="content";
//                    newsBean.type=1;
//                    newsBean.targetUserId= UserUtil.getUserId();
//                    newsBean.text="text";
//                    newsBean.save();
//
//                }


//                Intent intent1 = new Intent(getActivity(), SearchLocationActivity.class);
//                startActivity(intent1);
                goToMarket();
//                MyWebViewActivity.startToPreVip(mContext);
                break;
            case R.id.tvShare:

//                MyWebViewActivity.startToAfterVip(mContext);
//
                Intent share = new Intent(getActivity(), ShareActivity.class);
                startActivity(share);
                break;
            case R.id.ll_logout:

                new CommonDialogFragment.Builder()
                        .showTitle(false)
                        .setBtLeftColor(R.color.colorPrimary)
                        .setBtRightColor(R.color.color_F5445C)
                        .setContentText("您确定退出" + getResources().getString(R.string.app_name) + "?")
                        .setRightButtonClickListener(new CommonDialogFragment.OnClickListener() {
                            @Override
                            public void onClick(CommonDialogFragment dialogFragment, int which, String content) {
                                Intent register2 = new Intent(getActivity(), RegisterActivity.class);
                                startActivity(register2);
                                getActivity().finish();
                                UserUtil.loginOut();
                            }


                        })
                        .create()
                        .show(getChildFragmentManager());


                break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 123 && resultCode == Activity.RESULT_OK) {
            String imagePath = data.getStringExtra(ChangeIconActivity.KEY_FILEPATH);
            if (TextUtils.isEmpty(imagePath)) return;
            upIcon(imagePath);
//            AppData.getInstance().getUser().icon = imagePath;
//            AppData.getInstance().save();
        }
    }


    public void upIcon(String imgPath) {
        try {

            LogUtils.e(imgPath);
            if (TextUtils.isEmpty(imgPath)) return;
            int index = imgPath.lastIndexOf('.');

            if (imgPath.length() == index) return;
            String suffix = imgPath.substring(index + 1, imgPath.length());
            String base64 = Base64Helper.encodeBase64File(imgPath);
            String src = "data:image/" + suffix + ";base64," + base64;
            mPresenter.upIcon(src);
//            RequestClient.getInstance().updateIcon(src)
//                    .compose(Rxutil.<HttpResult<String>>rxSchedulerHelper())
//                    .map(new HttpResultFuc<String>())
//                    .subscribeWith(new ProgressSubscriber<String>(mActivity,mView) {
//                        @Override
//                        public void onUINext(String userCards) {
//                            mView.backSuccessIcon(userCards);
//                        }
//                    }));

        } catch (Exception e) {
            ToastUtil.showShort("图片上传错误");
            e.printStackTrace();
        }

    }


    public void goToMarket() {

        Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(goToMarket);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void backIcon(String info) {
        ImageManager.loadAvatar(getContext(), info, mIvIcon);
        UserUtil.getUser().headImage = info;
        UserUtil.save();
    }

    @Override
    public void backNickName(String nickname) {
        ToastUtil.showShort("修改昵称成功");
        UserUtil.getUser().userName = nickname;
        UserUtil.save();
        updateInfoView();
    }

    @Override
    public void backMyInfo(UserBean info) {
        UserUtil.setUser(info);
        updateInfoView();
    }

    @Override
    public String getEventMode() {
        return "我的";
    }


    public void popRename() {

        View playView = View.inflate(getActivity(), R.layout.pop_item_rename, null);
        final EditText etPhone = (EditText) playView.findViewById(R.id.etPhone);
        TextView tvName = (TextView) playView.findViewById(R.id.tvName);
        Button dialogLeftBtn = (Button) playView.findViewById(R.id.dialog_left_btn);
        Button dialogRightBtn = (Button) playView.findViewById(R.id.dialog_right_btn);
        tvName.setText("修改昵称");
        etPhone.setHint("请输入我的昵称");
        NoEmojiUtil.setNoEmoji(etPhone);

        final PopupWindow mPopupWindow = new PopupWindow(playView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // PopupWindow popupWindow = new PopupWindow(popuView, 100, 100);
        // 设置点击外部区域, 自动隐藏
        mPopupWindow.setOutsideTouchable(true); // 外部可触摸
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable()); // 设置空的背景, 响应点击事件
        mPopupWindow.setFocusable(true); //设置可获取焦点
        mPopupWindow.setAnimationStyle(R.style.addCareFriendAnim);  //添加动画

//        mPopupWindow.setAnimationStyle(R.style.popslide_style);
        mPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideInput(etPhone);
                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);

            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.dialog_left_btn:
                        if (mPopupWindow != null) {

                            mPopupWindow.dismiss();
                        }

                        break;
                    case R.id.dialog_right_btn:

                        String string = etPhone.getText().toString();
                        if (TextUtils.isEmpty(string)) {
                            ToastUtil.showShort("昵称不能为空");
                            return;
                        }
                        if (string.length() > 6) {
                            ToastUtil.showShort("昵称不能超过6位");
                            return;
                        }
                        if (mPopupWindow != null) {
//                            hideInput(etPhone);
                            mPopupWindow.dismiss();
                        }

                        mPresenter.updateName(string);

                        break;
                }
            }
        };
        etPhone.setText(UserUtil.getNickName());

        dialogLeftBtn.setOnClickListener(onClickListener);
        dialogRightBtn.setOnClickListener(onClickListener);
        etPhone.postDelayed(new Runnable() {
            @Override
            public void run() {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etPhone, InputMethodManager.SHOW_FORCED);
            }
        }, 50);
    }


    /**
     * 隐藏键盘
     */
    protected void hideInput(View view) {

        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null == imm)
                return;
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//            if (view.getCurrentFocus() != null) {
//                //有焦点关闭
//            } else {x
//                //无焦点关闭
//                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
