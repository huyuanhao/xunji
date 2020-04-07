package com.jimetec.xunji.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.baseview.base.AbsCommonActivity;
import com.common.lib.utils.ToastUtil;
import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.ContactBean;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.presenter.ContactPresenter;
import com.jimetec.xunji.presenter.contract.ContactContract;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PopSelectContactActivity extends AbsCommonActivity<ContactPresenter> implements ContactContract.View {

    @BindView(R.id.tvPopContact)
    TextView mTvPopContact;
    @BindView(R.id.tvPopFriend)
    TextView mTvPopFriend;
    @BindView(R.id.tvPopCancel)
    TextView mTvPopCancel;
    @BindView(R.id.llContent)
    LinearLayout mLlContent;
    List<ContactBean> mData = new ArrayList<>();

    @Override
    public ContactPresenter getPresenter() {
        return new ContactPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pop_select_contact;
    }

    @Override
    public void initViewAndData() {
        try {
            getWindow().setGravity(Gravity.BOTTOM);
            //设置布局在底部
            //设置布局填充满宽度
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(layoutParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPresenter.getLocContacts();
    }


    @OnClick({R.id.tvPopContact, R.id.tvPopFriend, R.id.tvPopCancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvPopContact:
                requestContact(Permission.READ_CONTACTS);
                break;
            case R.id.tvPopFriend:
                startActivityForResult(new Intent(PopSelectContactActivity.this, SelectFriendContactActivity.class), 2);
                break;
            case R.id.tvPopCancel:
                finish();
                break;
        }
        mLlContent.setVisibility(View.GONE);
    }


    private void requestContact(String... permissions) {

        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        startActivityForResult(new Intent(
                                Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 1);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                ContentResolver reContentResolverol = getContentResolver();
                Uri contactData = data.getData();
                @SuppressWarnings("deprecation")
                Cursor cursor = managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();
//                String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null,
                        null);
                if (phone.moveToNext()) {
                    String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (!TextUtils.isEmpty(usernumber)) {
                        usernumber = usernumber.replace(" ", "");
//                        ToastUtil.showShort(name+"-"+usernumber);
                        if (usernumber.length() != 11) {
                            ToastUtil.showShort("暂不支持该联系人号码");
                            finish();
                            return;
                        }
                        String locMan = name + usernumber;
                        for (int i = 0; i < mData.size(); i++) {
                            ContactBean contactBean = mData.get(i);
                            if (contactBean.type == 2) {
                                String netMan = contactBean.emergencyName + contactBean.emergencyPhone;
                                if (locMan.equals(netMan)) {
                                    ToastUtil.showShort("该紧急联系人已存在,请勿重复添加");
                                    return;
                                }
                            }

                        }
                        mPresenter.addContact(name, usernumber, 2);
                    }
                }else {
                    ToastUtil.showShort("暂不支持该联系人号码");
                    finish();
                    return;
                }


            } else if (requestCode == 2) {

                FriendBean friendBean = (FriendBean) data.getSerializableExtra(FriendBean.TAG);
                String locMan = friendBean.getFriendNickName() + friendBean.getFriendPhone();
                for (int i = 0; i < mData.size(); i++) {
                    ContactBean contactBean = mData.get(i);
                    if (contactBean.type == 1) {
                        String netMan = contactBean.emergencyName + contactBean.emergencyPhone;
                        if (locMan.equals(netMan)) {
                            ToastUtil.showShort("该紧急联系人已存在,请勿重复添加");
                            finish();
                            return;
                        }

                    }

                }

                mPresenter.addContact(friendBean.getFriendNickName(), friendBean.getFriendPhone(), 1);
//                ToastUtil.showShort(friendBean.getFriendNickName());
            }
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);

    }

    @Override
    public void backContacts(List<ContactBean> beans) {

        mData.clear();
        mData.addAll(beans);
    }

    @Override
    public void backAdd(Object obj) {
        ToastUtil.showShort("添加联系人成功");
        finish();

    }

    @Override
    public void backDelete(Object obj) {

    }

    @Override
    public void backrealName(Object obj) {

    }

//
//    @Override
//    public String getEventMode() {
//        return "选择";
//    }
}
