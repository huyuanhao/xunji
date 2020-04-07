package com.jimetec.xunji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.MyModuleBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:capTain
 * 时间:2019-07-15 16:30
 * 描述:
 */
public class MyModuleAdapter extends RecyclerView.Adapter {


    Context mContext;
    public List<MyModuleBean> mList = new ArrayList<>();


    public void setList(List<MyModuleBean> list) {
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }

        notifyDataSetChanged();
    }

    public MyModuleAdapter(Context context) {
        mContext = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

//        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_add_care, viewGroup, false);
//        return new AddCareHolder(inflate);

        View add = LayoutInflater.from(mContext).inflate(R.layout.item_my_module, viewGroup, false);
        return new MyHolder(add);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        MyHolder holder = (MyHolder) viewHolder;
       final MyModuleBean bean = mList.get(i);
        holder.mTvName.setText(bean.useWord);
        try {
            if (!TextUtils.isEmpty(bean.color)){
                holder.mTvName.setTextColor(Color.parseColor(bean.color));
            }
        } catch (Exception e) {
            holder.mTvName.setTextColor(mContext.getResources().getColor(R.color.COLOR_1C1C1C));
            e.printStackTrace();
        }
        holder.mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onModuleClick(bean);
            }
        });

    }


    @Override
    public int getItemCount() {

//        return 2;
        return mList == null ? 0 : mList.size();
    }

    static
    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView mTvName;

        MyHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    OnMyModuleClickListener mListener;

    public void addOnMyModuleClickListener(OnMyModuleClickListener selectContactClickListener) {
        mListener = selectContactClickListener;
    }

    public interface OnMyModuleClickListener {
        void onModuleClick(MyModuleBean bean);
    }

}
