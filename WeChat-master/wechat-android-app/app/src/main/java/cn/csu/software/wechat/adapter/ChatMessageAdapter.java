/*
 * Copyright (c) 2019-2019 cn.csu.software. All rights reserved.
 */

package cn.csu.software.wechat.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import cn.csu.software.wechat.R;
import cn.csu.software.wechat.adapter.holder.ChatMessageBaseViewHolder;
import cn.csu.software.wechat.adapter.holder.ChatMessagePhotoViewHolder;
import cn.csu.software.wechat.adapter.holder.ChatMessageTextViewHolder;
import cn.csu.software.wechat.adapter.holder.ChatMessageVoiceViewHolder;
import cn.csu.software.wechat.adapter.holder.MediaPlayerHolder;
import cn.csu.software.wechat.entity.ChatMessage;
import cn.csu.software.wechat.util.FileProcessUtil;
import cn.csu.software.wechat.util.LogUtil;
import cn.csu.software.wechat.widget.ZoomImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天界面消息 recycle view adapter
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class ChatMessageAdapter extends RecyclerView.Adapter {
    private static final String TAG = ChatMessageAdapter.class.getSimpleName();

    private final static int ITEM_TYPE_TEXT_LEFT = 1;

    private final static int ITEM_TYPE_TEXT_RIGHT = 2;

    private final static int ITEM_TYPE_PHOTO_LEFT = 3;

    private final static int ITEM_TYPE_PHOTO_RIGHT = 4;

    private final static int ITEM_TYPE_VOICE_LEFT = 5;

    private final static int ITEM_TYPE_VOICE_RIGHT = 6;

    private int mPosition;

    private ImageView mLastImageView;

    private Context mContext;

    private LayoutInflater mInflater;

    private  OnItemClickListener mOnItemClickListener;

    List<ChatMessage> mChatMessageList = new ArrayList<>();

    public ChatMessageAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == ITEM_TYPE_TEXT_LEFT) {
            itemView = mInflater.inflate(R.layout.item_chat_text_left, parent, false);
            return new LeftTextViewHolder(itemView);
        } else if (viewType == ITEM_TYPE_TEXT_RIGHT){
            itemView = mInflater.inflate(R.layout.item_chat_text_right, parent, false);
            return new RightTextViewHolder(itemView);
        } else if (viewType == ITEM_TYPE_PHOTO_LEFT){
            itemView = mInflater.inflate(R.layout.item_chat_photo_left, parent, false);
            return new LeftPhotoViewHolder(itemView);
        } else if (viewType == ITEM_TYPE_PHOTO_RIGHT){
            itemView = mInflater.inflate(R.layout.item_chat_photo_right, parent, false);
            return new RightPhotoViewHolder(itemView);
        } else if (viewType == ITEM_TYPE_VOICE_LEFT) {
            itemView = mInflater.inflate(R.layout.item_chat_voice_left, parent, false);
            return new LeftVoiceViewHolder(itemView);
        } else {
            itemView = mInflater.inflate(R.layout.item_chat_voice_right, parent, false);
            return new RightVoiceViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (!(viewHolder instanceof ChatMessageBaseViewHolder)) {
            return;
        }
        ChatMessageBaseViewHolder chatMessageBaseViewHolder = (ChatMessageBaseViewHolder) viewHolder;
        Bitmap avatarBitmap = null;
        try {
            avatarBitmap = FileProcessUtil.getBitmap(mContext, mChatMessageList.get(position).getAvatarPath());
        } catch (IOException e) {
            LogUtil.e(TAG, "get avatar bitmap error %s", e);
        }
        chatMessageBaseViewHolder.mAvatarImageView.setImageBitmap(avatarBitmap);
        if (position == 0) {
            chatMessageBaseViewHolder.mHeaderRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            chatMessageBaseViewHolder.mHeaderRelativeLayout.setVisibility(View.GONE);
        }
        if (chatMessageBaseViewHolder instanceof ChatMessageTextViewHolder) {
            ChatMessageTextViewHolder chatMessageTextViewHolder = (ChatMessageTextViewHolder) chatMessageBaseViewHolder;
            chatMessageTextViewHolder.mTextView.setText(mChatMessageList.get(position).getChatMessageText());
        } else if (chatMessageBaseViewHolder instanceof ChatMessagePhotoViewHolder) {
            ChatMessagePhotoViewHolder chatMessagePhotoViewHolder = (ChatMessagePhotoViewHolder) chatMessageBaseViewHolder;
            Bitmap bitmap = null;
            try {
                bitmap = FileProcessUtil.getBitmap(mContext, mChatMessageList.get(position).getChatMessagePhotoPath());
            } catch (IOException e) {
                LogUtil.e(TAG, "get photo bitmap error %s", e);
            }
            if (bitmap != null) {
                chatMessagePhotoViewHolder.mImageView.setImageBitmap(bitmap);
                final Bitmap finalBitmap = bitmap;
                chatMessagePhotoViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        @SuppressLint("InflateParams") View popupWindowView = LayoutInflater.from(mContext).inflate(R.layout.item_popup_window_image,
                            null, false);
                        ZoomImageView imageView = popupWindowView.findViewById(R.id.iv_popup_image);
                        imageView.setImageBitmap(finalBitmap);
                        final PopupWindow popupWindow = new PopupWindow(popupWindowView,
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                        popupWindow.setClippingEnabled(false);
                        popupWindow.showAsDropDown(view);
                    }
                });
            }
        } else if (chatMessageBaseViewHolder instanceof ChatMessageVoiceViewHolder){
            ChatMessageVoiceViewHolder chatMessageVoiceViewHolder = (ChatMessageVoiceViewHolder) chatMessageBaseViewHolder;
            chatMessageVoiceViewHolder.mTextView.setText(mChatMessageList.get(position).getChatMessageText() + '"');
            final String voicePath = mChatMessageList.get(position).getChatMessageVoicePath();
            final ImageView imageView = chatMessageVoiceViewHolder.mImageView;
            LogUtil.i(TAG, "prepare position %s, voice %s", position, voicePath);
            chatMessageVoiceViewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final MediaPlayerHolder mediaPlayerHolder = MediaPlayerHolder.getMediaPlayerHolder(mContext);
                    mediaPlayerHolder.reset();
                    mediaPlayerHolder.setPlaybackInfoListener(new MediaPlayerHolder.PlaybackInfoListener() {
                        @Override
                        public void onPlaybackCompleted() {
                            imageView.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
                        }

                        @Override
                        public void onPlaybackInterrupt() {
                            mLastImageView.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
                        }
                    });
                    try {
                        mLastImageView = imageView;
                        mediaPlayerHolder.loadMedia(voicePath);
                        mediaPlayerHolder.play();
                        imageView.setBackgroundResource(R.drawable.audio_animation_right_list);
                        AnimationDrawable drawable = (AnimationDrawable) imageView.getBackground();
                        drawable.start();

                    } catch (IOException e) {
                        LogUtil.e(TAG, "play media error %s", e);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mChatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (ChatMessage.SEND_TYPE == mChatMessageList.get(position).getSendOrReceiver()) {
            if (mChatMessageList.get(position).getChatMessageType() == ChatMessage.TEXT_TYPE) {
                return ITEM_TYPE_TEXT_RIGHT;
            } else if (mChatMessageList.get(position).getChatMessageType() == ChatMessage.PHOTO_TYPE) {
                return ITEM_TYPE_PHOTO_RIGHT;
            } else {
                return ITEM_TYPE_VOICE_RIGHT;
            }
        } else {
            if (mChatMessageList.get(position).getChatMessageType() == ChatMessage.TEXT_TYPE) {
                return ITEM_TYPE_TEXT_LEFT;
            } else if (mChatMessageList.get(position).getChatMessageType() == ChatMessage.PHOTO_TYPE) {
                return ITEM_TYPE_PHOTO_LEFT;
            } else {
                return ITEM_TYPE_VOICE_LEFT;
            }
        }
    }

    public void refreshItems(List<ChatMessage> list) {
        mChatMessageList.clear();
        mChatMessageList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(ChatMessage message) {
        mChatMessageList.add(message);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, ChatMessage chatMessage);
    }

    public class LeftTextViewHolder extends ChatMessageTextViewHolder {
        LeftTextViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTextView = itemView.findViewById(R.id.tv_chat_left);
            mAvatarImageView = itemView.findViewById(R.id.iv_avatar_text_left);
            mHeaderRelativeLayout = itemView.findViewById(R.id.rl_blank_text_left_top);
        }
    }

    public class RightTextViewHolder extends ChatMessageTextViewHolder {
        RightTextViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTextView = itemView.findViewById(R.id.tv_chat_right);
            mAvatarImageView = itemView.findViewById(R.id.iv_avatar_text_right);
            mHeaderRelativeLayout = itemView.findViewById(R.id.rl_blank_text_right_top);
        }
    }


    public class LeftPhotoViewHolder extends ChatMessagePhotoViewHolder {
        LeftPhotoViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mImageView = itemView.findViewById(R.id.iv_chat_photo_left);
            mAvatarImageView = itemView.findViewById(R.id.iv_avatar_photo_left);
            mHeaderRelativeLayout = itemView.findViewById(R.id.rl_blank_photo_left_top);
        }
    }

    public class RightPhotoViewHolder extends ChatMessagePhotoViewHolder {
        RightPhotoViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mImageView = itemView.findViewById(R.id.iv_chat_photo_right);
            mAvatarImageView = itemView.findViewById(R.id.iv_avatar_photo_right);
            mHeaderRelativeLayout = itemView.findViewById(R.id.rl_blank_photo_right_top);
        }
    }

    public class LeftVoiceViewHolder extends ChatMessageVoiceViewHolder {
        LeftVoiceViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mImageView = itemView.findViewById(R.id.iv_chat_voice_left);
            mAvatarImageView = itemView.findViewById(R.id.iv_avatar_voice_left);
            mHeaderRelativeLayout = itemView.findViewById(R.id.rl_blank_voice_left_top);
            mTextView = itemView.findViewById(R.id.tv_chat_voice_left);
            mLinearLayout = itemView.findViewById(R.id.ll_chat_voice_left);
        }
    }

    public class RightVoiceViewHolder extends ChatMessageVoiceViewHolder {
        RightVoiceViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mImageView = itemView.findViewById(R.id.iv_chat_voice_right);
            mAvatarImageView = itemView.findViewById(R.id.iv_avatar_voice_right);
            mHeaderRelativeLayout = itemView.findViewById(R.id.rl_blank_voice_right_top);
            mTextView = itemView.findViewById(R.id.tv_chat_voice_right);
            mLinearLayout = itemView.findViewById(R.id.ll_chat_voice_right);
        }
    }
}
