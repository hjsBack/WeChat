package cn.csu.software.wechat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.csu.software.wechat.R;
import cn.csu.software.wechat.constant.ConstantData;
import cn.csu.software.wechat.entity.UserInfo;
import cn.csu.software.wechat.util.FileProcessUtil;
import cn.csu.software.wechat.util.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 好友界面 recycle view adapter
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class FriendInfoAdapter extends RecyclerView.Adapter {
    private static final String TAG = FriendInfoAdapter.class.getSimpleName();

    private List<UserInfo> mUserInfoList = new ArrayList<>();

    private Context mContext;

    /**
     * 带参构造函数
     *
     * @param context Context
     */
    public FriendInfoAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(mContext).inflate(R.layout.item_friend_info, parent, false);
        return new FriendChatInfoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof FriendChatInfoHolder) {
            FriendChatInfoHolder friendChatInfoHolder = (FriendChatInfoHolder) holder;
            friendChatInfoHolder.mFriendNameTextView.setText(mUserInfoList.get(position).getUsername());
            Bitmap bitmap = null;
            try {
                bitmap = FileProcessUtil.getBitmap(mContext, mUserInfoList.get(position).getAvatarPath());
            } catch (IOException e) {
                LogUtil.e(TAG, "get bitmap error");
            }
            if (bitmap != null) {
                friendChatInfoHolder.mFriendAvatarImageView.setImageBitmap(bitmap);
            }
            friendChatInfoHolder.mFriendItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra(ConstantData.EXTRA_USER_INFO, mUserInfoList.get(position));
                    intent.setClassName(ConstantData.PACKAGE_NAME, ConstantData.ACTIVITY_CLASS_NAME_PERSONAL_INFO);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mUserInfoList.size();
    }

    /**
     * 刷新列表
     *
     * @param userInfoList UserInfo
     */
    public void refreshItems(List<UserInfo> userInfoList) {
        mUserInfoList.clear();
        mUserInfoList.addAll(userInfoList);
        notifyDataSetChanged();
    }

    /**
     * 添加Item
     *
     * @param userInfo UserInfo
     */
    public void addItem(UserInfo userInfo) {
        mUserInfoList.add(userInfo);
        notifyDataSetChanged();
    }

    /**
     * 通讯录Item
     *
     * @author huangjishun 874904407@qq.com
     * @since 2019-10-19
     */
    public class FriendChatInfoHolder extends RecyclerView.ViewHolder {
        private TextView mFriendNameTextView;

        private ImageView mFriendAvatarImageView;

        private View mFriendItemView;

        /**
         * 带参构造函数
         *
         * @param itemView View
         */
        public FriendChatInfoHolder(View itemView) {
            super(itemView);
            mFriendItemView = itemView;
            mFriendNameTextView = itemView.findViewById(R.id.tv_name_friend_info);
            mFriendAvatarImageView = itemView.findViewById(R.id.iv_avatar_friend_info);
        }
    }
}
