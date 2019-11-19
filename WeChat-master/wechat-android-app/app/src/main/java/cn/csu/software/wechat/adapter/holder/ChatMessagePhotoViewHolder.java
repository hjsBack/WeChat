package cn.csu.software.wechat.adapter.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

/**
 * 聊天 Item 图片消息 View Holder 父类
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class ChatMessagePhotoViewHolder extends ChatMessageBaseViewHolder {
    public ImageView mImageView;

    public ChatMessagePhotoViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
