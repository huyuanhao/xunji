package com.jimetec.xunji.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.lib.utils.TimeUtils;
import com.common.lib.utils.ToastUtil;
import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.util.ImageManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:capTain
 * 时间:2019-07-15 16:30
 * 描述:
 */
public class SelectContactAdapter extends RecyclerView.Adapter {
    Context mContext;
    public List<FriendBean> mList = new ArrayList<>();


    public void setList(List<FriendBean> list) {
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }

        notifyDataSetChanged();
    }

    public SelectContactAdapter(Context context) {
        mContext = context;
//        mList.add(UserUtil.getUser());
//        mList.add(new UserBean());
//        mList.add(new UserBean());
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

//        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_add_care, viewGroup, false);
//        return new AddCareHolder(inflate);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_care, viewGroup, false);
        return new CareHolder(inflate);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        if (getItemViewType(i) == 0) {
            CareHolder careHolder = (CareHolder) viewHolder;
//            if (i % 2 == 0) {
//                careHolder.mTvDeal.setText("接受");
//                careHolder.mTvDeal.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
//
//            } else {

            careHolder.mTvDeal.setTextColor(mContext.getResources().getColor(R.color.color_remove));

//            }
            final FriendBean bean = mList.get(i);
            careHolder.mTvPhone.setText(bean.getFriendNickName());


            if (bean.status == 1) {
                if (bean.lastLocationTimes > 0) {
                    careHolder.mTvTime.setVisibility(View.VISIBLE);
                    careHolder.mTvTime.setText(TimeUtils.millis2String(bean.lastLocationTimes));
                } else {
                    careHolder.mTvTime.setVisibility(View.GONE);
                }

                if (bean.lastLocationTimes > 0) {
                    careHolder.mTvLast.setVisibility(View.VISIBLE);
                    careHolder.mTvLast.setText("最后的位置: " + bean.lastLocation);
                } else {
//                    careHolder.mTvLast.setText("暂未发现该好友轨迹");
                    careHolder.mTvLast.setText("暂未获取到位置");
//                careHolder.mTvLast.setVisibility(View.GONE);
                }
                ImageManager.loadAvatar(mContext, bean.headImage, careHolder.mIvAvatar);
                careHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnCareItemClickListener != null && bean.status == 1) {
                            if (bean.lastLocationTimes > 0 ) {
                                if (bean.status ==1){
                                    mOnCareItemClickListener.selectFriendInfoClick(bean);
                                }
                            } else {
                                ToastUtil.showShort("暂未发现该好友轨迹");
                            }
                        }
                    }
                });

            } else {

                careHolder.mTvDeal.setTextColor(mContext.getResources().getColor(R.color.color_remove));
                careHolder.mTvDeal.setText("删除");
                careHolder.mTvLast.setVisibility(View.VISIBLE);
                careHolder.mTvLast.setText("对方已取消位置共享");
                careHolder.mTvTime.setVisibility(View.GONE);
            }
            careHolder.mTvDeal.setVisibility(View.GONE);


        }

    }


    @Override
    public int getItemCount() {

        return  mList.size();
    }


    static class CareHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivAvatar)
        ImageView mIvAvatar;
        @BindView(R.id.tvPhone)
        TextView mTvPhone;
        @BindView(R.id.tvTime)
        TextView mTvTime;
        @BindView(R.id.tvLast)
        TextView mTvLast;
        @BindView(R.id.tvDeal)
        TextView mTvDeal;
        View mView;

        CareHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }

    OnSelectFriendItemClickListener mOnCareItemClickListener;

    public void addOnSelectItemClickListener(OnSelectFriendItemClickListener onCareItemClickListener) {
        mOnCareItemClickListener = onCareItemClickListener;
    }

    public interface OnSelectFriendItemClickListener {
        void selectFriendInfoClick(FriendBean userBean);
    }



    static
    class AddCareHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bt_submit)
        Button mBtSubmit;

        AddCareHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
