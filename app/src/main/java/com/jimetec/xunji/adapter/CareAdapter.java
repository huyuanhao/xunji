package com.jimetec.xunji.adapter;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.MaskFilterSpan;
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
import com.jimetec.xunji.bean.UserBean;
import com.jimetec.xunji.util.ImageManager;
import com.jimetec.xunji.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:capTain
 * 时间:2019-07-15 16:30
 * 描述:
 */
public class CareAdapter extends RecyclerView.Adapter {


    Context mContext;
    public List<FriendBean> mList = new ArrayList<>();


    public void setList(List<FriendBean> list) {
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public CareAdapter(Context context) {
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

        if (i == 0) {
            View myself = LayoutInflater.from(mContext).inflate(R.layout.item_care, viewGroup, false);
            return new MyselfHolder(myself);
        } else if (i == 1) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_care, viewGroup, false);
            return new CareHolder(inflate);
        } else {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_add_care, viewGroup, false);
            return new AddCareHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        if (getItemViewType(i) == 0) {
            MyselfHolder myselfHolder = (MyselfHolder) viewHolder;
            myselfHolder.mTvDeal.setVisibility(View.GONE);
            myselfHolder.mTvPhone.setText("我自己");

            final UserBean user = UserUtil.getUser();
            if (user != null) {
                ImageManager.loadAvatar(mContext, user.headImage, myselfHolder.mIvAvatar);

                if (user.lastLocationTimes > 0) {
                    myselfHolder.mTvTime.setVisibility(View.VISIBLE);
                    myselfHolder.mTvTime.setText(TimeUtils.millis2String(user.lastLocationTimes));
                    myselfHolder.mTvLast.setVisibility(View.VISIBLE);
                    myselfHolder.mTvLast.setText("最后的位置: " + user.lastLocation);
                } else {
                    myselfHolder.mTvTime.setVisibility(View.GONE);
                    myselfHolder.mTvLast.setText("暂未获取到位置");
                }

            } else {
                myselfHolder.mTvTime.setVisibility(View.GONE);
                myselfHolder.mTvLast.setText("暂未获取到位置");
            }


            myselfHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnCareItemClickListener != null && user != null) {
                        if (user.lastLocationTimes > 0) {
                            mOnCareItemClickListener.watchMyself();
                        } else {
                            ToastUtil.showShort("暂未发现轨迹");
                        }
                    }
                }
            });

//            myselfHolder.mView.
        } else if (getItemViewType(i) == 1) {
            CareHolder careHolder = (CareHolder) viewHolder;
//            if (i % 2 == 0) {
//                careHolder.mTvDeal.setText("接受");
//                careHolder.mTvDeal.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
//
//            } else {
//            }
            final FriendBean bean = mList.get(i - 1);
            careHolder.mTvPhone.setText(bean.getFriendNickName());


            if (bean.status == 1) {
                careHolder.mView.setEnabled(true);
                if (bean.lastLocationTimes > 0) {
                    String time = TimeUtils.millis2String(bean.lastLocationTimes);
                    if (UserUtil.isVip()) {
                        careHolder.mTvTime.setText(TimeUtils.millis2String(bean.lastLocationTimes));
                        careHolder.mTvLast.setText("最后的位置: " + bean.lastLocation);

                    } else {
                        blurText(careHolder.mTvLast, "最后的位置: " + bean.lastLocation, 7);
                        blurText(careHolder.mTvTime, time, 0);
                    }
                    careHolder.mTvTime.setVisibility(View.VISIBLE);
                    careHolder.mTvLast.setVisibility(View.VISIBLE);
                } else {
                    careHolder.mTvTime.setVisibility(View.GONE);
//                    careHolder.mTvLast.setText("暂未获取到位置");
                    if (UserUtil.isVip()) {
                        careHolder.mTvLast.setText("最后的位置: " + "暂未获取到位置");
                    } else {
                        blurText(careHolder.mTvLast, "最后的位置: 暂未获取到位置", 7);
                    }


                }

                ImageManager.loadAvatar(mContext, bean.headImage, careHolder.mIvAvatar);
                if (bean.onlineStatus ==1){
                    ImageManager.loadDefault(mContext, R.mipmap.icon_online_s, careHolder.mIvOnlineStatus);
                }else {
                    ImageManager.loadDefault(mContext, R.mipmap.icon_online_n, careHolder.mIvOnlineStatus);

                }


                careHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnCareItemClickListener != null && bean.status == 1) {
                            if (bean.lastLocationTimes > 0) {
                                mOnCareItemClickListener.watchFriendInfoClick(bean);
                            } else {
                                ToastUtil.showShort("暂未发现该好友轨迹");
                            }
                        }
                    }
                });
                if (UserUtil.isVip()) {
                    careHolder.mTvDeal.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                    careHolder.mTvDeal.setText("设置");
                }else {
                    careHolder.mTvDeal.setTextColor(mContext.getResources().getColor(R.color.color_remove));
                    careHolder.mTvDeal.setText("解除");
                }

            } else {
                careHolder.mTvDeal.setTextColor(mContext.getResources().getColor(R.color.color_remove));
                careHolder.mTvDeal.setText("删除");
                careHolder.mTvLast.setVisibility(View.VISIBLE);
                careHolder.mTvLast.setText("对方已取消位置共享");
                careHolder.mTvTime.setVisibility(View.GONE);
                careHolder.mView.setEnabled(false);
            }


            if (UserUtil.isVip()) {
//                careHolder.mTvDeal.setVisibility(View.VISIBLE);
//                careHolder.mIvNoVip.setVisibility(View.GONE);

            } else {
//                careHolder.mTvDeal.setVisibility(View.GONE);
//                careHolder.mIvNoVip.setVisibility(View.VISIBLE);
            }

            careHolder.mTvDeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnCareItemClickListener != null) {
                        mOnCareItemClickListener.friendSetting(bean);
                    }
                }
            });


        } else {
            AddCareHolder addCareHolder = (AddCareHolder) viewHolder;
            addCareHolder.mBtSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnCareItemClickListener != null) {
                        mOnCareItemClickListener.addFriend();
                    }
                }
            });
        }

    }


    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return 0;
        } else if (position == getItemCount() - 1) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {

        return (mList == null || mList.size() == 0) ? 2 : mList.size() + 2;
    }


    static class MyselfHolder extends RecyclerView.ViewHolder {
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

        MyselfHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }


    static class CareHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivAvatar)
        ImageView mIvAvatar;

        @BindView(R.id.ivOnlineStatus)
        ImageView mIvOnlineStatus;
        @BindView(R.id.ivNoVip)
        ImageView mIvNoVip;
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

    OnCareItemClickListener mOnCareItemClickListener;

    public void addOnCareItemClickListener(OnCareItemClickListener onCareItemClickListener) {
        mOnCareItemClickListener = onCareItemClickListener;
    }

    public interface OnCareItemClickListener {
        void watchFriendInfoClick(FriendBean userBean);

        void friendSetting(FriendBean userBean);

        void addFriend();

        void watchMyself();
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


    public void blurText(TextView tv, String text, int startIndex) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //View从API Level 11才加入setLayerType方法 //设置View以软件渲染模式绘图   
            tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        SpannableString stringBuilder = new SpannableString(text);
        stringBuilder.setSpan(new MaskFilterSpan(new BlurMaskFilter(12f, BlurMaskFilter.Blur.NORMAL)), startIndex, stringBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(stringBuilder);

    }


}
