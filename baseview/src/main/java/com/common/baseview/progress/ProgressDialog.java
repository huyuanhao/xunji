package com.common.baseview.progress;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.common.baseview.R;


/**
 * 作者:zh
 * 时间:2018/8/7 下午3:52
 * 描述:
 */
public class ProgressDialog extends AlertDialog {

    private Context context;
    private LayoutInflater inflater;
    String mDes;

    private TextView tvDes;
    public ProgressDialog(Context context ,String text) {
        this(context);
        mDes =text;
    }

    public ProgressDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.progress_dialog, null);
        tvDes= (TextView) view.findViewById(R.id.tvDes);
        if (!TextUtils.isEmpty(mDes)){
            tvDes.setText(mDes);
        }
        setContentView(view);
        setCanceledOnTouchOutside(false);
    }

    public void dismiss(){
        if(isShowing()){
            super.dismiss();
        }
    }

    public void setDes(String des){
        tvDes.setText(""+des);
    }

}
