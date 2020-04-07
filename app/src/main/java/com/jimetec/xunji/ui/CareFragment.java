package com.jimetec.xunji.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.baseview.base.AbsLoadFragment;
import com.common.baseview.dialog.CommonDialogFragment;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.ToastUtil;
import com.jimetec.xunji.MyApplication;
import com.jimetec.xunji.R;
import com.jimetec.xunji.adapter.CareAdapter;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.NewsBean;
import com.jimetec.xunji.presenter.CarePresenter;
import com.jimetec.xunji.presenter.contract.CareContract;
import com.jimetec.xunji.rx.event.HomeIndexEvent;
import com.jimetec.xunji.share.ShareActivity;
import com.jimetec.xunji.util.UserUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 */
public class CareFragment extends AbsLoadFragment<CarePresenter> implements CareContract.View {


    @BindView(R.id.rv)
    RecyclerView mRv;

    @BindView(R.id.rlTitleLeft)
    RelativeLayout mRlTitleLeft;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.rlTitleRight)
    RelativeLayout mRlTitleRight;
    @BindView(R.id.ivRemind)
    ImageView mIvRemind;
    @BindView(R.id.ivEmergency)
    ImageView mIvEmergency;
    private CareAdapter mCareAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_care;
    }

    @Override
    public CarePresenter getPresenter() {
        return new CarePresenter(mActivity);
    }

    @Override
    public void initViewAndData() {
        mRlTitleLeft.setVisibility(View.GONE);
        mRlTitleRight.setVisibility(View.VISIBLE);
        mTvTitle.setText("关心的人");
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mCareAdapter = new CareAdapter(getContext());


        mRv.setAdapter(mCareAdapter);
        mCareAdapter.addOnCareItemClickListener(new CareAdapter.OnCareItemClickListener() {
            @Override
            public void watchFriendInfoClick(FriendBean userBean) {
                if (UserUtil.isVip()) {
                    Intent intent = new Intent(getActivity(), LastTrackActivity.class);
                    intent.putExtra(FriendBean.TAG, userBean);
                    startActivity(intent);
                } else {
                    popNoVip();
                }
            }

            @Override
            public void friendSetting(FriendBean userBean) {
//                ToastUtil.showShort("friendSetting");
                if (userBean.status == 1) {
                    if (UserUtil.isVip()) {
                        popWarn(userBean);
                    } else {
                        unBindFriend(userBean);
                    }
//                    unBindFriend(userBean);
                } else {
                    mPresenter.unBinderFriend(userBean.id);
                }
            }

            @Override
            public void addFriend() {
                if (UserUtil.isLogined()) {

                    if (UserUtil.isVip()) {
                        if (TextUtils.isEmpty(UserUtil.getUserName())) {
                            popRename();
                        } else {
                            popAddFriend();
                        }

                    } else {
                        popNoVip();
                    }


                } else {
                    startActivity(new Intent(getActivity(), RegisterActivity.class));
                }

            }

            @Override
            public void watchMyself() {
                if (UserUtil.isLogined()) {
                    Intent last = new Intent(getActivity(), MyLastTrackActivity.class);
                    startActivity(last);
                }
            }
        });
        breakAnim();


        mTvTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(MyApplication.freeVipDes))
                    popFree();
            }
        }, 1000);
    }


    @Override
    protected void onFragmentResume(boolean isFirst, boolean isViewDestroyed) {
        super.onFragmentResume(isFirst, isViewDestroyed);
        loadingNetData();

    }


    @Override
    public void loadingNetData() {
        super.loadingNetData();
        if (UserUtil.isLogined()) {
            mPresenter.getFriends();
            int count = LitePal.where("hasRead = ?  and targetUserId = ?  ", "0", UserUtil.getUserId() + "").count(NewsBean.class);
            if (count > 0 || MyApplication.hasGloalNews) {
                mIvRemind.setVisibility(View.VISIBLE);
            } else {
                mIvRemind.setVisibility(View.GONE);
            }

        } else {
            showSuccessPage();
        }


    }


    public void unBindFriend(final FriendBean bean) {
        new CommonDialogFragment.Builder()
                .showTitle(false)
                .setBtLeftColor(R.color.colorPrimary)
                .setBtRightColor(R.color.color_F5445C)
                .setRightButtonText("确认解除")
                .setContentText("解除后双方删除关系，并无法获得对应的位置等相关信息")
                .setRightButtonClickListener(new CommonDialogFragment.OnClickListener() {
                    @Override
                    public void onClick(CommonDialogFragment dialogFragment, int which, String content) {
                        dialogFragment.dismiss();
                        mPresenter.unBinderFriend(bean.id);

                    }
                })
                .create()
                .show(getChildFragmentManager());

    }


    public void addFriend(String phone, String nickName) {
        mPresenter.addFriend(UserUtil.getUserPhone(), nickName, phone);
    }


    @Override
    public void backFriends(List<FriendBean> pageBean) {
        showSuccessPage();
        mCareAdapter.setList(pageBean);

        LogUtils.e(pageBean.toString());

    }

    @Override
    public void backAdd(Object obj) {
        showSharePop();
//        ToastUtil.showShort("好友申请发送成功");
    }


    @Override
    public void backUnbinder(Object obj) {
        ToastUtil.showShort("操作成功");
        loadingNetData();
    }

    @Override
    public void backNickName(String nickname) {


        ToastUtil.showShort("修改昵称成功");
        UserUtil.getUser().userName = nickname;
        UserUtil.save();


        popAddFriend();
//        updateInfoView();

    }

    @Override
    public void updateViewEvent(HomeIndexEvent event) {
        loadingNetData();
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
//                        Intent register2 = new Intent(getActivity(), RegisterActivity.class);
//                        startActivity(register2);
//                        getActivity().finish();
//                        UserUtil.loginOut();

                        Intent share = new Intent(getActivity(), ShareActivity.class);
                        startActivity(share);
                        dialogFragment.dismiss();
                    }


                })
                .create()
                .show(getChildFragmentManager());

    }


    public void popFree() {
        View playView = View.inflate(getActivity(), R.layout.pop_free_vip, null);
        final PopupWindow mPopupWindow = new PopupWindow(playView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tvContent = playView.findViewById(R.id.tvContent);
        TextView tvStart = playView.findViewById(R.id.tvStart);
        tvContent.setText(MyApplication.freeVipDes);
        mPopupWindow.setOutsideTouchable(true); // 外部可触摸
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable()); // 设置空的背景, 响应点击事件
        mPopupWindow.setFocusable(true); //设置可获取焦点
        mPopupWindow.setAnimationStyle(R.style.addCareFriendAnim);  //添加动画

//        mPopupWindow.setAnimationStyle(R.style.popslide_style);
        mPopupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tvStart:
                        if (!UserUtil.isLogined()) {
                            Intent intent = new Intent(getActivity(), RegisterActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
                mPopupWindow.dismiss();
            }
        };

        tvStart.setOnClickListener(onClickListener);
//        tvWarn.setOnClickListener(onClickListener);

    }


    public void popWarn(final FriendBean userBean) {
        View playView = View.inflate(getActivity(), R.layout.pop_location_warn, null);
        final PopupWindow mPopupWindow = new PopupWindow(playView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View tvUnbinder = playView.findViewById(R.id.tvUnbinder);
        View tvWarn = playView.findViewById(R.id.tvWarn);
        mPopupWindow.setOutsideTouchable(true); // 外部可触摸
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable()); // 设置空的背景, 响应点击事件
        mPopupWindow.setFocusable(true); //设置可获取焦点
        mPopupWindow.setAnimationStyle(R.style.addCareFriendAnim);  //添加动画

//        mPopupWindow.setAnimationStyle(R.style.popslide_style);
        mPopupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tvUnbinder:
                        unBindFriend(userBean);
                        break;
                    case R.id.tvWarn:
                        Intent intent = new Intent(getContext(), LocationWarnActivity.class);
                        intent.putExtra(FriendBean.TAG, userBean);
                        startActivity(intent);
                        break;
                }
                mPopupWindow.dismiss();
            }
        };
        tvUnbinder.setOnClickListener(onClickListener);
        tvWarn.setOnClickListener(onClickListener);

    }


    EditText etPhone;

    String nickName;

    //添加好友
    public void popAddFriend() {
        View playView = View.inflate(getActivity(), R.layout.pop_item_add_care, null);
        etPhone = (EditText) playView.findViewById(R.id.etPhone);
        final EditText etNickName = (EditText) playView.findViewById(R.id.etNickName);
        Button btSubmit = (Button) playView.findViewById(R.id.btSubmit);
        ImageView ivPopClose = (ImageView) playView.findViewById(R.id.ivPopClose);
        TextView tvMobileContact = (TextView) playView.findViewById(R.id.tvMobileContact);
        nickName = UserUtil.getNickName();
        etNickName.setText(nickName);
        final PopupWindow mPopupWindow = new PopupWindow(playView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // PopupWindow popupWindow = new PopupWindow(popuView, 100, 100);
        // 设置点击外部区域, 自动隐藏
        mPopupWindow.setOutsideTouchable(true); // 外部可触摸
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable()); // 设置空的背景, 响应点击事件
        mPopupWindow.setFocusable(true); //设置可获取焦点
        mPopupWindow.setAnimationStyle(R.style.addCareFriendAnim);  //添加动画

//        mPopupWindow.setAnimationStyle(R.style.popslide_style);
        mPopupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                hideInput(etPhone);
                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.tvMobileContact:
                        nickName = etNickName.getText().toString();
                        if (TextUtils.isEmpty(nickName)) {
                            ToastUtil.showShort("请输入您的昵称");
                            return;
                        }
//                        hideInput(etPhone);
                        requestContact(Permission.READ_CONTACTS);

                        if (mPopupWindow != null) {
                            mPopupWindow.dismiss();
                        }
//                        startActivityForResult(new Intent(mActivity, MobileContactActivity.class), 1);
                        break;
                    case R.id.btSubmit:
                        String phone = etPhone.getText().toString();
                        nickName = etNickName.getText().toString();


                        if (TextUtils.isEmpty(nickName)) {
                            ToastUtil.showShort("请输入您的昵称");
                            return;
                        }
                        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
                            ToastUtil.showShort("请输入正确的手机号码");
                            return;
                        } else {
                            if (phone.equalsIgnoreCase(UserUtil.getUserPhone())) {
                                ToastUtil.showShort("不能添加自己为好友");
                                return;
                            }

                            addFriend(phone, nickName);
                            if (mPopupWindow != null) {

                                mPopupWindow.dismiss();
                            }
                        }

                        break;
                    case R.id.ivPopClose:
                        if (mPopupWindow != null) {

                            mPopupWindow.dismiss();
                        }
                        break;
                }
            }
        };

        btSubmit.setOnClickListener(onClickListener);
        tvMobileContact.setOnClickListener(onClickListener);
        ivPopClose.setOnClickListener(onClickListener);
        etPhone.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etNickName, InputMethodManager.SHOW_FORCED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 50);
//        etPhone.requestFocus();
//
    }
//
//    public void verifyAddFriend(String nickName,String phone){
//        if (TextUtils.isEmpty(nickName)) {
//            ToastUtil.showShort("请输入您的昵称");
//            return;
//        }
//        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
//            ToastUtil.showShort("请输入正确的手机号码");
//            return;
//        } else {
//            if (phone.equalsIgnoreCase(UserUtil.getUserPhone())) {
//                ToastUtil.showShort("不能添加自己为好友");
//                return;
//            }
//
//            addFriend(phone, nickName);
//            if (mPopupWindow != null) {
//
//                mPopupWindow.dismiss();
//            }
//        }
//
//    }


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


    public void popNoVip() {
        View playView = View.inflate(getActivity(), R.layout.pop_no_vip, null);
        TextView tvContent = (TextView) playView.findViewById(R.id.tvContent);

        Button dialogLeftBtn = (Button) playView.findViewById(R.id.dialog_left_btn);
        Button dialogRightBtn = (Button) playView.findViewById(R.id.dialog_right_btn);
        final PopupWindow mPopupWindow = new PopupWindow(playView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // PopupWindow popupWindow = new PopupWindow(popuView, 100, 100);
        // 设置点击外部区域, 自动隐藏
        mPopupWindow.setOutsideTouchable(false); // 外部可触摸
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
                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);

            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.dialog_left_btn:

                        break;
                    case R.id.dialog_right_btn:
                        MyWebViewActivity.startToAfterVip(mContext);

                        break;
                }
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        };

        dialogLeftBtn.setOnClickListener(onClickListener);
        dialogRightBtn.setOnClickListener(onClickListener);
    }


    public void popRename() {

        View playView = View.inflate(getActivity(), R.layout.pop_item_rename, null);
        final EditText etPhone = (EditText) playView.findViewById(R.id.etPhone);
        TextView tvName = (TextView) playView.findViewById(R.id.tvName);
        Button dialogLeftBtn = (Button) playView.findViewById(R.id.dialog_left_btn);
        Button dialogRightBtn = (Button) playView.findViewById(R.id.dialog_right_btn);
        tvName.setText("修改昵称");
        etPhone.setHint("请输入我的昵称");
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
//        etPhone.setText(UserUtil.getNickName());

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


    @OnClick({R.id.ivEmergency, R.id.rlTitleRight})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.rlTitleRight:
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                startActivity(intent);
 
                break;
            case R.id.ivEmergency:
                if (UserUtil.isLogined()) {
//                    if (UserUtil.isVip()){
//                        startActivity(new Intent(mActivity, EmergencyActivity.class));
//                    }else {
//                        popNoVip();
//                    }
                    startActivity(new Intent(mActivity, EmergencyActivity.class));

                } else {
                    Intent register = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(register);
                }
                break;

        }

    }

    @Override
    public String getEventMode() {

        return "关心的人";
    }


    public void breakAnim() {


        // 动画执行完成后，默认会保持到最后一帧的状态
        AnimationSet animationSet = new AnimationSet(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.8f);
        alphaAnimation.setRepeatMode(Animation.REVERSE); // 放大并缩小，时间为750*2

        alphaAnimation.setDuration(2000);
        // 设置不断重复
        alphaAnimation.setRepeatCount(Animation.INFINITE);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 0.85f, 0.7f, 0.85f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(2000);
        // 设置不断重复
        scaleAnimation.setRepeatMode(Animation.REVERSE); // 放大并缩小，时间为750*2

        scaleAnimation.setRepeatCount(Animation.INFINITE);


        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setInterpolator(new AccelerateInterpolator());
        mIvEmergency.setAnimation(animationSet);
        animationSet.start();
//
//        ObjectAnimator animator0 = ObjectAnimator.ofFloat(),
//                "alpha", 0.5F, 1F);

    }


    private void requestContact(String... permissions) {

        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {

                        Intent intent = new Intent(mActivity, MobileContactActivity.class);
                        intent.putExtra(MobileContactActivity.NICK_NAME, nickName);
                        startActivity(intent);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        ToastUtil.showShort("授权失败");

                    }
                })
                .start();
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == 1) {
//                ContactBean selectBean = (ContactBean) data.getSerializableExtra(ContactBean.TAG);
////                if (etPhone !=null){
////                    etPhone.setText(selectBean.emergencyPhone);
////                }
//                if (!TextUtils.isEmpty(nickName)) {
//                    addFriend(selectBean.emergencyPhone,nickName);
//                }
//            }
//        }
//    }

}
