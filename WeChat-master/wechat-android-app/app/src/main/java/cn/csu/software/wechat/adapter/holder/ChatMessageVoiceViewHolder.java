package cn.csu.software.wechat.adapter.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 聊天 Item View Holder 接口
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class ChatMessageVoiceViewHolder extends ChatMessageBaseViewHolder {
    public ImageView mImageView;

    public TextView mTextView;

    public LinearLayout mLinearLayout;

    public ChatMessageVoiceViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
