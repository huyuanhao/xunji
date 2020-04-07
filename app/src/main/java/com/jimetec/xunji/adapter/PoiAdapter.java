package com.jimetec.xunji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.jimetec.xunji.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:capTain
 * 时间:2019-08-09 10:49
 * 描述:
 */
public class PoiAdapter extends BaseAdapter {

    Context mContext;
    List<PoiItem> mData = new ArrayList<>();

    public PoiAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<PoiItem> data) {
        mData.clear();
        if (data != null)
            mData.addAll(data);
        notifyDataSetChanged();
    }


    public List<PoiItem> getData() {
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
            convertView =  LayoutInflater.from(mContext).inflate( R.layout.item_search_poi_item,parent,false);
        }


//          View.inflate(mContext,, null);

        PoiHolder holder = PoiHolder.getHolder(convertView);
        PoiItem poiItem = mData.get(position);

        holder.mTvTop.setText( poiItem.getTitle());
        String address =poiItem.getProvinceName()+poiItem.getCityName();
        if (poiItem.getAdName().equalsIgnoreCase(poiItem.getSnippet())){
            address += poiItem.getAdName();
        }else {
            address += poiItem.getAdName()+poiItem.getSnippet();
        }
        holder.mTvBottom.setText(address);
        return convertView;
    }

    static class PoiHolder {
        @BindView(R.id.tvTop)
        TextView mTvTop;
        @BindView(R.id.tvBottom)
        TextView mTvBottom;

        PoiHolder(View view) {
            ButterKnife.bind(this, view);
        }


        public  static PoiHolder  getHolder(View convertView) {
            PoiHolder holder = (PoiHolder) convertView.getTag();
            if (holder ==null){
                holder = new PoiHolder(convertView);
                convertView.setTag(holder);
            }

            return holder;
        }
    }
}
