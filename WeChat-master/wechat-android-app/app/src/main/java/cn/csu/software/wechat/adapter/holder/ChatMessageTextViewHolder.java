package cn.csu.software.wechat.adapter.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

/**
 * 聊天 Item 文本消息 View Holder 父类
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class ChatMessageTextViewHolder extends ChatMessageBaseViewHolder {
    public TextView mTextView;

    public ChatMessageTextViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
