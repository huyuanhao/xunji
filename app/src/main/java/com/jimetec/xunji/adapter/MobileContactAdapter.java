package com.jimetec.xunji.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:capTain
 * 时间:2019-07-15 16:30
 * 描述:
 */
public class MobileContactAdapter extends RecyclerView.Adapter {


    Context mContext;
    public List<ContactBean> mList = new ArrayList<>();


    public void setList(List<ContactBean> list) {
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }

        notifyDataSetChanged();
    }

    public MobileContactAdapter(Context context) {
        mContext = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

//        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_add_care, viewGroup, false);
//        return new AddCareHolder(inflate);

        View add = LayoutInflater.from(mContext).inflate(R.layout.item_mobile_contact, viewGroup, false);
        return new AddHolder(add);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        AddHolder holder = (AddHolder) viewHolder;
        final ContactBean contactBean = mList.get(i);

        holder.mTvName.setText(contactBean.emergencyName);
        holder.mTvPhone.setText(contactBean.emergencyPhone);
        if (contactBean.type ==1){
            holder.mTvAdd.setEnabled(false);
            holder.mTvAdd.setText("已添加");
        }else {
            holder.mTvAdd.setEnabled(true);
            holder.mTvAdd.setText("添加");
        }
        holder.mTvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.selectContact(contactBean);
            }
        });

    }


    @Override
    public int getItemCount() {

//        return 10;
        return mList == null ? 0 : mList.size();
    }

    static
    class AddHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView mTvName;
        @BindView(R.id.tvPhone)
        TextView mTvPhone;
        @BindView(R.id.tvAdd)
        TextView mTvAdd;

        AddHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }





    OnSelectContactClickListener mListener;

    public void addOnSelectContactClickListener(OnSelectContactClickListener selectContactClickListener) {
        mListener = selectContactClickListener;
    }

    public interface OnSelectContactClickListener {
        void selectContact(ContactBean bean);
    }

}
