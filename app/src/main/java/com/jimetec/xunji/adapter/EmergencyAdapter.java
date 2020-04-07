package com.jimetec.xunji.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:capTain
 * 时间:2019-08-14 15:05
 * 描述:
 */
public class EmergencyAdapter extends BaseAdapter {

    Context mContext;

    public EmergencyAdapter(Context context) {
        mContext = context;
    }

    List<ContactBean> mData = new ArrayList<>();


    public void setData(List<ContactBean> data) {
        mData.clear();
        if (data != null)
            mData.addAll(data);
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_emergency_contact, parent, false);
//        ContactHolder holder = ContactHolder.getHolder(convertView);
//        ContactHolder holder = ContactHolder.getHolder(convertView);
        EmergencyHolder holder = EmergencyHolder.getHolder(convertView);
        ContactBean contactBean = mData.get(position);
        holder.mTvName.setText(contactBean.getEmergencyName());
        holder.mTvPhone.setText(contactBean.getPhone());
        return convertView;
    }


    static class EmergencyHolder {
        @BindView(R.id.tvName)
        TextView mTvName;
        @BindView(R.id.tvPhone)
        TextView mTvPhone;


        EmergencyHolder(View view) {
            ButterKnife.bind(this, view);
        }
        public static EmergencyHolder getHolder(View convertView) {
            EmergencyHolder holder = (EmergencyHolder) convertView.getTag();
            if (holder == null) {
                holder = new EmergencyHolder(convertView);
                convertView.setTag(holder);
            }

            return holder;
        }
    }
}
