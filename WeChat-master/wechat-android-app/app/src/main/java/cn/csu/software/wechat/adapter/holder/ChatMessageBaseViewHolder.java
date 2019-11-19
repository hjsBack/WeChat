package cn.csu.software.wechat.adapter.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 聊天 Item 基础 View Holder 父类
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class ChatMessageBaseViewHolder extends RecyclerView.ViewHolder {
    /**
     * item view
     */
    public View mItemView;

    public ImageView mAvatarImageView;

    public RelativeLayout mHeaderRelativeLayout;

    public ChatMessageBaseViewHolder(@NonNull View itemView) {
        super(itemView);
        mItemView = itemView;
    }
}
