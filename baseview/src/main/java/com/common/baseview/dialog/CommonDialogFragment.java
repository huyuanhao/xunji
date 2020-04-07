package com.common.baseview.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.common.baseview.R;
import com.common.lib.utils.Utils;


/**
 * 作者:zh
 * 时间:1/3/19 3:48 PM
 * 描述:
 */
public class CommonDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private ViewHolder mHolder = new ViewHolder();
    private Params mParams = new Params();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        createContentView(dialog);
        configDialogSize(dialog);
        return dialog;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        bindData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mParams.leftButtonClickListener = null;
        mParams.rightButtonClickListener = null;
        mParams.singleChoiceItemClickListener = null;
        mParams.onDismissListener = null;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mParams.onDismissListener != null) {
            mParams.onDismissListener.onDismiss(dialog);
        }
    }

    public void show(FragmentManager manager) {
        super.show(manager, getLogTag());
    }

    public int show(FragmentTransaction transaction) {
        return super.show(transaction, getLogTag());
    }

    public void setParams(Params params) {
        mParams = params;
    }

    public View getCustomContentView() {
        return mParams.contentView;
    }

    private void createContentView(Dialog dialog) {
        // create and set dialog content view
        View dialogView = LayoutInflater.from(getActivity()).inflate(
                R.layout.common_dialog,
                (ViewGroup) dialog.getWindow().getDecorView(),
                false);
        dialog.setContentView(dialogView);
        findViews(dialogView);
    }

    private void findViews(View dialogView) {
        mHolder.dialog_title = $(dialogView, R.id.dialog_title);
        mHolder.dialog_content = $(dialogView, R.id.dialog_content);
//        mHolder.dialog_image = $(dialogView, R.id.dialog_image);
        mHolder.dialog_left_btn = $(dialogView, R.id.dialog_left_btn);
        mHolder.dialog_right_btn = $(dialogView, R.id.dialog_right_btn);
        mHolder.bottom_button_container = $(dialogView, R.id.bottom_button_container);
        mHolder.content_view = $(dialogView, R.id.content_view);
//        mHolder.dialog_btn_closed = $(dialogView, R.id.dialog_btn_closed);
        mHolder.bottom_button_line = $(dialogView, R.id.bottom_button_line);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mParams.onCancelListener != null) {
            mParams.onCancelListener.onCancel(dialog);
        }
    }

    private void bindData() {
        // 对话框属性设置
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(mParams.canCanceledOnTouchOutside);
            getDialog().setCancelable(mParams.cancelable);
        }

        // 标题
        mHolder.dialog_title.setVisibility(mParams.showTitle ? View.VISIBLE : View.GONE);
        mHolder.dialog_title.setText(mParams.titleText);

        //关闭按钮
//        mHolder.dialog_btn_closed.setVisibility(mParams.showCloseButton ? View.VISIBLE : View.GONE);
//        mHolder.dialog_btn_closed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });

        // 正文
        mHolder.dialog_content.setGravity(mParams.contentGravity);
        if (!TextUtils.isEmpty(mParams.contentText)) {
            mHolder.dialog_content.setText(mParams.contentText);

//            mHolder.dialog_content.requestFocus();
//            if (!TextUtils.isEmpty(mParams.contentText)) {
//                mHolder.dialog_content.setSelection(mParams.contentText.length());
//            }
        }
        if (mParams.showContentImage && mParams.imageResource > 0) {
//            mHolder.dialog_image.setVisibility(View.VISIBLE);
//            mHolder.dialog_image.setImageResource(mParams.imageResource);
        }


        if (mParams.btRightColor > 0) {
            mHolder.dialog_right_btn.setTextColor(getActivity().getResources().getColor(mParams.btRightColor));
        }

        if (mParams.btLeftColor > 0) {
            mHolder.dialog_left_btn.setTextColor(getActivity().getResources().getColor(mParams.btLeftColor));
        }
        // 底部左右按钮
        mHolder.dialog_left_btn.setText(!TextUtils.isEmpty(mParams.leftButtonText) ? mParams.leftButtonText : getString(R.string.btn_cancel));
        mHolder.dialog_right_btn.setText(!TextUtils.isEmpty(mParams.rightButtonText) ? mParams.rightButtonText : getString(R.string.btn_ok));
        mHolder.dialog_left_btn.setOnClickListener(this);
        mHolder.dialog_right_btn.setOnClickListener(this);
        mHolder.bottom_button_line.setVisibility(mParams.showBottomButton ? View.VISIBLE : View.GONE);
        mHolder.bottom_button_container.setVisibility(mParams.showBottomButton ? View.VISIBLE : View.GONE);
        if (mParams.showBottomButton && mParams.showSingleButton) {
            mHolder.dialog_left_btn.setVisibility(View.GONE);
            mHolder.dialog_right_btn.setVisibility(View.VISIBLE);
            mHolder.dialog_right_btn.setBackgroundResource(R.drawable.common_dialog_bottom_button);
            mHolder.dialog_right_btn.setText(mParams.singleButtonText);
        }

        // 内容有自定义View
        if (mParams.contentView != null) {
            mHolder.content_view.removeAllViews();
            mHolder.content_view.addView(mParams.contentView);
        }
    }

    @Override
    public void onClick(View v) {
        String content = mHolder.dialog_content.getText().toString();


        int i = v.getId();
        if (i == R.id.dialog_left_btn) {
            if (!mParams.showBottomButton) {
                return;
            }
            if (mParams.leftButtonClickListener != null) {
                mParams.leftButtonClickListener.onClick(this, Dialog.BUTTON_NEGATIVE, content);
            } else {
                dismiss();
            }
        } else if (i == R.id.dialog_right_btn) {
            if (!mParams.showBottomButton) {
                return;
            }
            if (mParams.showSingleButton) {
                if (mParams.singleButtonClickListener != null) {
                    mParams.singleButtonClickListener.onClick(this, Dialog.BUTTON_POSITIVE, content);
                } else {
                    dismiss();
                }
            } else {
                if (mParams.rightButtonClickListener != null) {
                    mParams.rightButtonClickListener.onClick(this, Dialog.BUTTON_POSITIVE, content);
                } else {
                    dismiss();
                }
            }
        }
    }

    public interface OnClickListener {
        void onClick(CommonDialogFragment dialogFragment, int which, String content);
    }

    private static class ViewHolder {
        TextView dialog_title;
        TextView dialog_content;
        //        ImageView dialog_image;
        Button dialog_left_btn;
        Button dialog_right_btn;
        View bottom_button_container;
        View bottom_button_line;
        //        View dialog_btn_closed;
        ViewGroup content_view;
    }

    public static class Builder {


        private Context mContext = Utils.getApp();
        private Params mParams = new Params();

        public Builder showTitle(boolean shown) {
            mParams.showTitle = shown;
            return this;
        }

        public Builder showBottomButton(boolean shown) {
            mParams.showBottomButton = shown;
            return this;
        }

        public Builder showSingleButton(boolean shown) {
            mParams.showSingleButton = shown;
            return this;
        }

        public Builder showContentImage(boolean shown) {
            mParams.showContentImage = shown;
            return this;
        }

        public Builder setTitleText(String title) {
            mParams.titleText = title;
            return this;
        }

        public Builder setTitleText(int title) {
            mParams.titleText = mContext.getString(title);
            return this;
        }


        public Builder setContentGravity(int gravity) {
            mParams.contentGravity = gravity;
            return this;
        }

        public Builder setContentText(int content) {
            mParams.contentText = mContext.getString(content);
            return this;
        }

        public Builder setContentImage(int imageResource) {
            mParams.imageResource = imageResource;
            return this;
        }

        public Builder setContentText(String content) {
            mParams.contentText = content;
            return this;
        }

        public Builder setLeftButtonText(int buttonText) {
            mParams.leftButtonText = mContext.getString(buttonText);
            return this;
        }

        public Builder setLeftButtonText(String buttonText) {
            mParams.leftButtonText = buttonText;
            return this;
        }

        public Builder setRightButtonText(int buttonText) {
            mParams.rightButtonText = mContext.getString(buttonText);
            return this;
        }

        public Builder setRightButtonText(String buttonText) {
            mParams.rightButtonText = buttonText;
            return this;
        }

        public Builder setSingleButtonText(int buttonText) {
            mParams.singleButtonText = mContext.getString(buttonText);
            return this;
        }

        public Builder setSingleButtonText(String buttonText) {
            mParams.singleButtonText = buttonText;
            return this;
        }

        public Builder setLeftButtonClickListener(OnClickListener listener) {
            mParams.leftButtonClickListener = listener;
            return this;
        }

        public Builder setRightButtonClickListener(OnClickListener listener) {
            mParams.rightButtonClickListener = listener;
            return this;
        }

        public Builder setSingleButtonClickListener(OnClickListener listener) {
            mParams.singleButtonClickListener = listener;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener listener) {
            mParams.onDismissListener = listener;
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener listener) {
            mParams.onCancelListener = listener;
            return this;
        }

        public Builder setContentView(View contentView) {
            mParams.contentView = contentView;
            return this;
        }

        public Builder showCloseButton(boolean shown) {
            mParams.showCloseButton = shown;
            return this;
        }

        public Builder setBtRightColor(int shown) {
            mParams.btRightColor = shown;
            return this;
        }

        public Builder setBtLeftColor(int shown) {
            mParams.btLeftColor = shown;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean shown) {
            mParams.canCanceledOnTouchOutside = shown;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            mParams.cancelable = cancelable;
            return this;
        }

        public CommonDialogFragment create() {
            // set default values
            if (TextUtils.isEmpty(mParams.leftButtonText)) {
                mParams.leftButtonText = mContext.getString(R.string.btn_cancel);
            }
            if (TextUtils.isEmpty(mParams.rightButtonText)) {
                mParams.rightButtonText = mContext.getString(R.string.btn_ok);
            }

            // create dialog fragment instance
            CommonDialogFragment dialogFragment = new CommonDialogFragment();
            dialogFragment.setParams(mParams);
            return dialogFragment;
        }

        public void refreshUI(CommonDialogFragment dialogFragment) {
            dialogFragment.setParams(mParams);
            dialogFragment.bindData();
//            int ic_launcher = R.mipmap.ic_launcher;
        }

    }

    static class Params {

        OnClickListener leftButtonClickListener;
        public OnClickListener rightButtonClickListener;
        public OnClickListener singleButtonClickListener;
        public OnClickListener singleChoiceItemClickListener;
        public DialogInterface.OnDismissListener onDismissListener;
        public DialogInterface.OnCancelListener onCancelListener;
        public boolean showTitle = false;
        public boolean showBottomButton = true;
        public boolean showContentImage = false;
        //        int dialogImage ;
        public int imageResource;
        public boolean showSingleButton = false;
        public boolean showCloseButton = false;
        public boolean canCanceledOnTouchOutside = true;
        public boolean cancelable = true;
        public int contentGravity = Gravity.CENTER;
        public String titleText = null;
        public String contentText = null;
        public String leftButtonText = null;
        public String rightButtonText = null;
        public String singleButtonText = null;
        public View contentView = null;
        public int btRightColor;
        public int btLeftColor;
    }

}
