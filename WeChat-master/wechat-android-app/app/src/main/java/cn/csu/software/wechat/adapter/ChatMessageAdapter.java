/*
 * Copyright (c) 2019-2019 cn.csu.software. All rights reserved.
 */

package cn.csu.software.wechat.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.csu.software.wechat.R;
import cn.csu.software.wechat.entity.ChatMessage;
import cn.csu.software.wechat.constant.ConstantData;
import cn.csu.software.wechat.util.BitmapUtil;
import cn.csu.software.wechat.util.FileProcessUtil;
import cn.csu.software.wechat.util.LogUtil;
import cn.csu.software.wechat.widget.ZoomImageView;

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

    private final static int ITEM_TYPE_VIDEO_RIGHT = 5;

    private int mPosition;

    private Context mContext;

    private LayoutInflater mInflater;

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
        } else {
            itemView = mInflater.inflate(R.layout.item_chat_photo_right, parent, false);
            return new RightPhotoViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof LeftTextViewHolder) {
            LeftTextViewHolder leftTextViewHolder = (LeftTextViewHolder) viewHolder;
            if (position == 0) {
                leftTextViewHolder.mHeaderRelativeLayout.setVisibility(View.VISIBLE);
            } else {
                leftTextViewHolder.mHeaderRelativeLayout.setVisibility(View.GONE);
            }
            leftTextViewHolder.mTextView.setText(mChatMessageList.get(position).getChatMessageText());
            Bitmap bitmap = FileProcessUtil.getBitmap(mContext, mChatMessageList.get(position).getAvatarPath());
            if (bitmap != null) {
                leftTextViewHolder.mAvatarImageView.setImageBitmap(bitmap);
            }
        } else if (viewHolder instanceof RightTextViewHolder) {
            RightTextViewHolder rightTextViewHolder = (RightTextViewHolder) viewHolder;
            if (position == 0) {
                rightTextViewHolder.mHeaderRelativeLayout.setVisibility(View.VISIBLE);
            } else {
                rightTextViewHolder.mHeaderRelativeLayout.setVisibility(View.GONE);
            }
            rightTextViewHolder.mTextView.setText(mChatMessageList.get(position).getChatMessageText());
            Bitmap bitmap = FileProcessUtil.getBitmap(mContext, mChatMessageList.get(position).getAvatarPath());
            if (bitmap != null) {
                rightTextViewHolder.mAvatarImageView.setImageBitmap(bitmap);
            }
        } else if (viewHolder instanceof LeftPhotoViewHolder) {
            LeftPhotoViewHolder leftPhotoViewHolder = (LeftPhotoViewHolder) viewHolder;
            if (position == 0) {
                leftPhotoViewHolder.mHeaderRelativeLayout.setVisibility(View.VISIBLE);
            } else {
                leftPhotoViewHolder.mHeaderRelativeLayout.setVisibility(View.GONE);
            }
            final Bitmap bitmap = FileProcessUtil.getBitmap(mContext, mChatMessageList.get(position).getChatMessagePhotoPath());
            if (bitmap != null) {
                leftPhotoViewHolder.mImageView.setImageBitmap(bitmap);
                leftPhotoViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View popupWindowView = LayoutInflater.from(mContext).inflate(R.layout.item_popup_window_image,
                            null, false);
                        ZoomImageView imageView = popupWindowView.findViewById(R.id.iv_popup_image);
                        imageView.setImageBitmap(bitmap);
                        final PopupWindow popupWindow = new PopupWindow(popupWindowView,
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                        popupWindow.setClippingEnabled(false);
                        popupWindow.showAsDropDown(view);
                    }
                });
            }
            Bitmap avatar = FileProcessUtil.getBitmap(mContext, mChatMessageList.get(position).getAvatarPath());
            if (avatar != null) {
                leftPhotoViewHolder.mAvatarImageView.setImageBitmap(avatar);
            }
        } else if (viewHolder instanceof RightPhotoViewHolder) {
            RightPhotoViewHolder rightPhotoViewHolder = (RightPhotoViewHolder) viewHolder;
            if (position == 0) {
                rightPhotoViewHolder.mHeaderRelativeLayout.setVisibility(View.VISIBLE);
            } else {
                rightPhotoViewHolder.mHeaderRelativeLayout.setVisibility(View.GONE);
            }
            final Bitmap bitmap = FileProcessUtil.getBitmap(mContext, mChatMessageList.get(position).getChatMessagePhotoPath());
            if (bitmap != null) {
                rightPhotoViewHolder.mImageView.setImageBitmap(bitmap);
                rightPhotoViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View popupWindowView = LayoutInflater.from(mContext).inflate(R.layout.item_popup_window_image,
                            null, false);
                        ZoomImageView imageView = popupWindowView.findViewById(R.id.iv_popup_image);
                        imageView.setImageBitmap(bitmap);
                        final PopupWindow popWindow = new PopupWindow(popupWindowView,
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                        popWindow.setClippingEnabled(false);
                        popWindow.showAsDropDown(view);
                    }
                });
            }
            Bitmap avatar = FileProcessUtil.getBitmap(mContext, mChatMessageList.get(position).getAvatarPath());
            if (avatar != null) {
                rightPhotoViewHolder.mAvatarImageView.setImageBitmap(avatar);
            }
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
                return ITEM_TYPE_VIDEO_RIGHT;
            }
        } else {
            if (mChatMessageList.get(position).getChatMessageType() == ChatMessage.TEXT_TYPE) {
                return ITEM_TYPE_TEXT_LEFT;
            } else if (mChatMessageList.get(position).getChatMessageType() == ChatMessage.PHOTO_TYPE) {
                return ITEM_TYPE_PHOTO_LEFT;
            } else {
                return ITEM_TYPE_VIDEO_RIGHT;
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

    public class LeftTextViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        private ImageView mAvatarImageView;

        private View mItemView;

        private RelativeLayout mHeaderRelativeLayout;

        public LeftTextViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTextView = itemView.findViewById(R.id.tv_chat_left);
            mAvatarImageView = itemView.findViewById(R.id.iv_avatar_text_left);
            mHeaderRelativeLayout = itemView.findViewById(R.id.rl_blank_text_left_top);
        }
    }

    public class RightTextViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        private ImageView mAvatarImageView;

        private View mItemView;

        private RelativeLayout mHeaderRelativeLayout;

        public RightTextViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTextView = itemView.findViewById(R.id.tv_chat_right);
            mAvatarImageView = itemView.findViewById(R.id.iv_avatar_text_right);
            mHeaderRelativeLayout = itemView.findViewById(R.id.rl_blank_text_right_top);
        }
    }

    public class RightPhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        private ImageView mAvatarImageView;

        private View mItemView;

        private RelativeLayout mHeaderRelativeLayout;

        public RightPhotoViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mImageView = itemView.findViewById(R.id.iv_chat_photo_right);
            mAvatarImageView = itemView.findViewById(R.id.iv_avatar_photo_right);
            mHeaderRelativeLayout = itemView.findViewById(R.id.rl_blank_photo_right_top);
        }
    }

    public class LeftPhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        private ImageView mAvatarImageView;

        private View mItemView;

        private RelativeLayout mHeaderRelativeLayout;

        public LeftPhotoViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mImageView = itemView.findViewById(R.id.iv_chat_photo_left);
            mAvatarImageView = itemView.findViewById(R.id.iv_avatar_photo_left);
            mHeaderRelativeLayout = itemView.findViewById(R.id.rl_blank_photo_left_top);
        }
    }
}
