package com.jimetec.xunji.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.LocationWarnBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:capTain
 * 时间:2019-08-12 10:30
 * 描述:
 */
public class LocationWarnAdapter extends RecyclerView.Adapter {

    Context mContext;
    List<LocationWarnBean> mData = new ArrayList<>();
    int mIndex = 0;


    public LocationWarnAdapter(Context context) {
        mContext = context;
    }


    public void setData(List<LocationWarnBean> data) {
        mData.clear();


        if (data != null) {
            mData.addAll(data);
            if (mData.size()<=mIndex){
                mIndex=0;
            }
        }else {
            mIndex=0;
        }

        notifyDataSetChanged();
    }

    public void setIndex(int index) {
        if (mData.size() > index)
            mIndex = index;
        else mIndex=0;
        notifyDataSetChanged();
    }

    public int getIndex() {
        return mIndex;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_location_warn, viewGroup, false);
        return new LocationWarnHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        LocationWarnHolder holder = (LocationWarnHolder) viewHolder;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onLocationWarnSelected(mData.get(i),i);
            }
        });
        holder.mIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onLocationWarnDelete(mData.get(i));
            }
        });
        holder.mTvLocation.setText(mData.get(i).remark +": "+mData.get(i).location);
        if (mIndex == i) {
            holder.mIvRight.setVisibility(View.VISIBLE);
            holder.mIvLeft.setVisibility(View.VISIBLE);
            holder.mTvLocation.setTextColor(mContext.getResources().getColor(R.color.color_theme));
        } else {
            holder.mIvRight.setVisibility(View.INVISIBLE);
            holder.mIvLeft.setVisibility(View.INVISIBLE);
            holder.mTvLocation.setTextColor(mContext.getResources().getColor(R.color.color_theme_press));
        }
        if (i ==0){
            holder.dottedView.setVisibility(View.INVISIBLE);
        }else {
            holder.dottedView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class LocationWarnHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivLeft)
        ImageView mIvLeft;
        @BindView(R.id.tvLocation)
        TextView mTvLocation;
        @BindView(R.id.ivRight)
        ImageView mIvRight;
        @BindView(R.id.dottedView)
        View dottedView;
        View mView;

        public LocationWarnHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    OnLocationWarnSelectedListener mListener;

    public void setListener(OnLocationWarnSelectedListener listener) {
        mListener = listener;
    }

    public interface OnLocationWarnSelectedListener {
        void onLocationWarnSelected(LocationWarnBean bean,int position);

        void onLocationWarnDelete(LocationWarnBean bean);
    }

}
