package com.jimetec.xunji.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.baseview.base.AbsCommonActivity;
import com.common.lib.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.R;
import com.jimetec.xunji.adapter.ContactAdapter;
import com.jimetec.xunji.bean.ContactBean;
import com.jimetec.xunji.presenter.ContactPresenter;
import com.jimetec.xunji.presenter.contract.ContactContract;
import com.jimetec.xunji.util.UserUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ContactActivity extends AbsCommonActivity<ContactPresenter> implements ContactContract.View {

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
    @BindView(R.id.lv)
    ListView mLv;
    @BindView(R.id.base_view)
    FrameLayout mBaseView;
    @BindView(R.id.rlTitleLayout)
    RelativeLayout mRlTitleLayout;
    @BindView(R.id.tvAddContact)
    TextView mTvAddContact;
    @BindView(R.id.view_empty)
    FrameLayout mViewEmpty;
    private ContactAdapter mContactAdapter;


    @Override
    public ContactPresenter getPresenter() {
        return new ContactPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact;
    }

    @Override
    public void initViewAndData() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
        mTvTitle.setText("紧急联系人");
        mContactAdapter = new ContactAdapter(this);
        mLv.setAdapter(mContactAdapter);
        mLv.setEmptyView(mViewEmpty);
        mContactAdapter.addOnDeleteListener(new ContactAdapter.OnDeleteListener() {
            @Override
            public void onDeleteListener(ContactBean bean) {
                mPresenter.deleteContact(bean.id);
            }
        });
    }

//    @OnClick(R.id.rlTitleLeft)
//    public void onViewClicked() {
//        finish();
//    }


    @Override
    protected void onResume() {
        super.onResume();
        loadingNetData();


    }

    @Override
    public void loadingNetData() {
        super.loadingNetData();
        mPresenter.getContacts();
    }

    @Override
    public void backContacts(List<ContactBean> beans) {
        showSuccessPage();
//        if (beans.size() == 0) {
//            showEmptyPage();
//        } else {
//            showSuccessPage();
//        }
        if (beans.size() > 2) {
            mTvAddContact.setVisibility(View.GONE);
        } else {
            mTvAddContact.setVisibility(View.VISIBLE);

        }
        mContactAdapter.setData(beans);

        if (TextUtils.isEmpty(UserUtil.getRealName())) {
            popRealName();
        }

//        popRealName();
    }

//    @Override
//    public boolean isOnlyFirst() {
//        return false;
//    }

    @Override
    public void backAdd(Object obj) {
        loadingNetData();
    }

    @Override
    public void backDelete(Object obj) {

        ToastUtil.showShort("删除联系人成功");
        loadingNetData();
    }

    @Override
    public void backrealName(Object obj) {
//        ToastUtil.showShort("设置成功");
        startActivity(new Intent(this,PopSelectContactActivity.class));


    }

    @OnClick({R.id.rlTitleLayout, R.id.tvAddContact})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlTitleLayout:
                finish();
                break;
            case R.id.tvAddContact:
                startActivity(new Intent(this, PopSelectContactActivity.class));
                break;
        }
    }


    boolean isNameSetting = false;

    public void popRealName() {
        isNameSetting = false;
        View playView = View.inflate(this, R.layout.pop_item_rename, null);
        final EditText etPhone = (EditText) playView.findViewById(R.id.etPhone);
        TextView tvName = (TextView) playView.findViewById(R.id.tvName);
        Button dialogLeftBtn = (Button) playView.findViewById(R.id.dialog_left_btn);
        Button dialogRightBtn = (Button) playView.findViewById(R.id.dialog_right_btn);

        SpannableString spannableString = new SpannableString("请输入你的真实姓名");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvName.setText(spannableString);//其中，”默认颜色红颜色” 为你要改变的文本。setSpan方法有四个参数，ForegroundColorSpan是为文本设置前景色，也就是文字颜色。如果要为文字添加背景颜色，可替换为BackgroundColorSpan。4为文本颜色改变的起始位置，spannableString.length()为文本颜色改变的结束位置。最后一个参数为布尔型，可以传入以下四种。
        etPhone.setHint("真实姓名不可修改");
        final PopupWindow mPopupWindow = new PopupWindow(playView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // PopupWindow popupWindow = new PopupWindow(popuView, 100, 100);
        // 设置点击外部区域, 自动隐藏
        mPopupWindow.setOutsideTouchable(false); // 外部可触摸
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable()); // 设置空的背景, 响应点击事件
        mPopupWindow.setFocusable(true); //设置可获取焦点
        mPopupWindow.setTouchable(true);

        mPopupWindow.setAnimationStyle(R.style.addCareFriendAnim);  //添加动画

//        mPopupWindow.setAnimationStyle(R.style.popslide_style);
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideInput(etPhone);
                if (TextUtils.isEmpty(UserUtil.getRealName()) && !isNameSetting) {
                    finish();
                    return;
                }
                params.alpha = 1.0f;
                getWindow().setAttributes(params);

            }
        });
//
//

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.dialog_left_btn:

                        break;
                    case R.id.dialog_right_btn:

                        String string = etPhone.getText().toString();
                        if (TextUtils.isEmpty(string)) {
                            ToastUtil.showShort("姓名不能为空");
                            return;
                        }
                        if (string.length() > 6) {
                            ToastUtil.showShort("姓名不能超过6位");
                            return;
                        }
                        isNameSetting = true;
                        mPresenter.realName(string);
                        break;
                }


                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }

            }
        };
//        etPhone.setText(UserUtil.getNickName());

        dialogLeftBtn.setOnClickListener(onClickListener);
        dialogRightBtn.setOnClickListener(onClickListener);
        etPhone.postDelayed(new Runnable() {
            @Override
            public void run() {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etPhone, InputMethodManager.SHOW_FORCED);
            }
        }, 50);
    }


    @Override
    public String getEventMode() {
        return "紧急联系人";
    }

}
