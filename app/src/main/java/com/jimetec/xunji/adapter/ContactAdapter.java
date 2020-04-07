package com.jimetec.xunji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:capTain
 * 时间:2019-08-09 10:49
 * 描述:
 */
public class ContactAdapter extends BaseAdapter {

    Context mContext;
    List<ContactBean> mData = new ArrayList<>();

    public ContactAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<ContactBean> data) {
        mData.clear();
        if (data != null)
            mData.addAll(data);
        notifyDataSetChanged();
    }


    public List<ContactBean> getData() {
        return mData;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);
        }


//          View.inflate(mContext,, null);

        ContactHolder holder = ContactHolder.getHolder(convertView);
       final ContactBean contactBean = mData.get(position);
        holder.mTvName.setText(contactBean.getEmergencyName());
        holder.mTvPhone.setText(contactBean.getPhone());
        holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteListener!=null){
                    mOnDeleteListener.onDeleteListener(contactBean);
                }
            }
        });
//        ContactBean poiItem = mData.get(position);

//        holder.mTvTop.setText( poiItem.getTitle());
//        String address =poiItem.getProvinceName()+poiItem.getCityName();
//        if (poiItem.getAdName().equalsIgnoreCase(poiItem.getSnippet())){
//            address += poiItem.getAdName();
//        }else {
//            address += poiItem.getAdName()+poiItem.getSnippet();
//        }
//        holder.mTvBottom.setText(address);
        return convertView;
    }

    static class ContactHolder {
        @BindView(R.id.tvName)
        TextView mTvName;
        @BindView(R.id.tvPhone)
        TextView mTvPhone;
        @BindView(R.id.ivDelete)
        ImageView mIvDelete;


        ContactHolder(View view) {
            ButterKnife.bind(this, view);
        }


        public static ContactHolder getHolder(View convertView) {
            ContactHolder holder = (ContactHolder) convertView.getTag();
            if (holder == null) {
                holder = new ContactHolder(convertView);
                convertView.setTag(holder);
            }

            return holder;
        }
    }



    OnDeleteListener mOnDeleteListener;

    public void addOnDeleteListener(OnDeleteListener onDeleteListener) {
        mOnDeleteListener = onDeleteListener;
    }

    public interface OnDeleteListener {
        void onDeleteListener(ContactBean userBean);
    }
}
