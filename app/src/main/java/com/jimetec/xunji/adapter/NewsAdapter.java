package com.jimetec.xunji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.lib.utils.TimeUtils;
import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.NewsBean;
import com.jimetec.xunji.bean.TimeSortBean;
import com.jimetec.xunji.ui.MyWebViewActivity;
import com.jimetec.xunji.ui.NewDetailActivity;
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
public class NewsAdapter extends RecyclerView.Adapter {

    Context mContext;
    public List<TimeSortBean> mList = new ArrayList<>();


    public void setList(List<TimeSortBean> list) {
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }

        notifyDataSetChanged();
    }

    public NewsAdapter(Context context) {
        mContext = context;
//        mList.add(UserUtil.getUser());
//        mList.add(new UserBean());
//        mList.add(new UserBean());
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {

//        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_add_care, viewGroup, false);
//        return new AddCareHolder(inflate);
        if (type == 0) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_care, viewGroup, false);
            return new CareHolder(inflate);
        } else {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_my_news, viewGroup, false);
            return new NewsHolder(inflate);
        }


//        if (i == 0) {
//
//        } else {
//            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_add_care, viewGroup, false);
//            return new AddCareHolder(inflate);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        if (getItemViewType(i) == 0) {

            CareHolder careHolder = (CareHolder) viewHolder;
            final FriendBean bean = (FriendBean) mList.get(i);
            if (bean.status == 1) {
                careHolder.mTvDeal.setText("接受");
                careHolder.mTvDeal.setTextColor(mContext.getResources().getColor(R.color.color_theme));

            } else if (bean.status == 2) {
                careHolder.mTvDeal.setTextColor(mContext.getResources().getColor(R.color.color_remove));
                careHolder.mTvDeal.setText("已同意");
            } else if (bean.status == 3) {
                careHolder.mTvDeal.setText("已拒绝");
                careHolder.mTvDeal.setTextColor(mContext.getResources().getColor(R.color.color_F5445C));
            }

            if (bean.times > 0) {
                careHolder.mTvTime.setVisibility(View.VISIBLE);
                careHolder.mTvTime.setText(TimeUtils.millis2String(bean.times));
            } else {
                careHolder.mTvTime.setVisibility(View.GONE);
            }

            careHolder.mTvPhone.setText(bean.getNewsNickName());
            careHolder.mTvLast.setText(bean.getFriendPhone() + " 的用户特别关心您，接受并共享您的位置给TA");
            ImageManager.loadAvatar(mContext, bean.headImage, careHolder.mIvAvatar);
//
            careHolder.mTvDeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnFriendItemClickListener != null) {
                        if (bean.status == 1) {
                            mOnFriendItemClickListener.addFriend(bean);
                        }
                    }
                }
            });
        } else {
            final NewsHolder newsHolder = (NewsHolder) viewHolder;
            final NewsBean bean = (NewsBean) mList.get(i);
            if (bean.times > 0) {
                newsHolder.mTvTime.setVisibility(View.VISIBLE);
                newsHolder.mTvTime.setText(TimeUtils.millis2String(bean.times));
            } else {
                newsHolder.mTvTime.setVisibility(View.GONE);
            }

            if (bean.hasRead ==0){
                newsHolder.mIvRemind.setVisibility(View.VISIBLE);
            }else {
                newsHolder.mIvRemind.setVisibility(View.GONE);
            }

            ImageManager.loadAvatar(mContext, R.mipmap.app_icon, newsHolder.mIvAvatar);
            newsHolder.mTvPhone.setText(bean.title);
            newsHolder.mTvLast.setText(bean.text);
            newsHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bean.hasRead=1;
                    bean.save();
                    Intent nfIntent ;
                    if (!TextUtils.isEmpty(bean.url)) {
                        MyWebViewActivity.startTo(mContext,bean.url,"消息列表",bean.title);
                    } else {
                        nfIntent = new Intent(mContext, NewDetailActivity.class);
                        nfIntent.putExtra(NewsBean.TAG, bean);
                        mContext.startActivity(nfIntent);
                    }

//                    Intent intent = new Intent(mContext, NewDetailActivity.class);
//                    intent.putExtra(NewsBean.TAG,bean);


                }
            });
        }


    }

    @Override
    public int getItemCount() {

        return (mList == null || mList.size() == 0) ? 0 : mList.size();
    }


    @Override
    public int getItemViewType(int position) {
        TimeSortBean timeSortBean = mList.get(position);
        if (timeSortBean instanceof FriendBean) {
            return 0;
        } else {
            return 1;
        }
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

    OnFriendItemClickListener mOnFriendItemClickListener;

    public void addOnCareItemClickListener(OnFriendItemClickListener onCareItemClickListener) {
        mOnFriendItemClickListener = onCareItemClickListener;
    }

    public interface OnFriendItemClickListener {
        void addFriend(FriendBean userBean);
    }

    static
    class NewsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivAvatar)
        ImageView mIvAvatar;
        @BindView(R.id.iv_remind)
        ImageView mIvRemind;
        @BindView(R.id.tvPhone)
        TextView mTvPhone;
        @BindView(R.id.tvTime)
        TextView mTvTime;
        @BindView(R.id.tvLast)
        TextView mTvLast;
        @BindView(R.id.tvDeal)
        TextView mTvDeal;
        View mView;

        NewsHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }


}
