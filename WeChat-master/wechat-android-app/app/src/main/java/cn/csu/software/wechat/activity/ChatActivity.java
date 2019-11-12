/*
 * Copyright (c) 2019-2019 cn.csu.software. All rights reserved.
 */

package cn.csu.software.wechat.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.csu.software.wechat.R;
import cn.csu.software.wechat.adapter.ChatMessageAdapter;
import cn.csu.software.wechat.adapter.holder.MediaPlayerHolder;
import cn.csu.software.wechat.entity.ChatMessage;
import cn.csu.software.wechat.entity.UserInfo;
import cn.csu.software.wechat.constant.ConstantData;
import cn.csu.software.wechat.data.ChatMessageData;
import cn.csu.software.wechat.listener.KeyboardChangeListener;
import cn.csu.software.wechat.service.SocketService;
import cn.csu.software.wechat.util.BitmapUtil;
import cn.csu.software.wechat.util.FileProcessUtil;
import cn.csu.software.wechat.util.LogUtil;
import cn.csu.software.wechat.widget.RecordButton;

import java.io.File;
import java.io.IOException;

/**
 * 消息界面
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class ChatActivity extends Activity implements TextWatcher, View.OnClickListener, KeyboardChangeListener.KeyBoardListener, RecordButton.OnFinishedRecordListener {
    private static final String TAG = ChatActivity.class.getSimpleName();

    private static final int IMAGE_REQUEST_CODE = 1;

    private RecyclerView mRecyclerView;

    private Context mContext;

    private ChatMessageAdapter mChatMessageAdapter;

    private EditText mEditText;

    private ImageView mMoreButton;

    private RelativeLayout mPhotographButtonLayout;

    private RelativeLayout mMoreRelativeLayout;

    private ImageView mExpressionButton;

    private TextView mReceiverNameTextView;

    private ImageView mVoiceButton;

    private RecordButton mRecordButton;

    private Button mSendButton;

    private String mMessage;

    private String mMyAvatarPath;

    private String mCompressionPath;

    private LinearLayoutManager mLayoutManager;

    private UserInfo mUserInfo;

    private long mSendTime;

    private SocketService.SendMessageBinder sendMessageBinderService;

    private MessageBroadcastReceiver mMessageBroadcastReceiver;

    private KeyboardChangeListener mKeyboardChangeListener;

    @SuppressLint("HandlerLeak")
    private Handler mScrollRecyclerViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mRecyclerView.scrollToPosition(mChatMessageAdapter.getItemCount() - 1);
            }
            super.handleMessage(msg);
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.i(TAG, "onServiceConnected: ComponentName = " + name);
            if (service instanceof SocketService.SendMessageBinder) {
                sendMessageBinderService = (SocketService.SendMessageBinder) service;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            sendMessageBinderService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initIntent();
        initView();
        initData();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mMessageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(mContext, SocketService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        RegisterMessageBroadcastReceiver();
        super.onResume();
    }

    private void initIntent() {
        Intent intent = this.getIntent();
        if (intent == null || intent.getExtras() == null) {
            return;
        }
        Object object = intent.getSerializableExtra(ConstantData.EXTRA_USER_INFO);
        if (object instanceof UserInfo) {
            mUserInfo = (UserInfo) object;
        }
    }

    private void initView() {
        setContentView(R.layout.activity_chat);
        mChatMessageAdapter = new ChatMessageAdapter(mContext);
        mRecyclerView = findViewById(R.id.recycler_chat);
        mRecyclerView.setAdapter(mChatMessageAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSendButton = findViewById(R.id.bt_send);
        mSendButton.setOnClickListener(this);
        mMoreButton = findViewById(R.id.bt_more);
        mMoreButton.setOnClickListener(this);
        mVoiceButton = findViewById(R.id.bt_voice);
        mVoiceButton.setOnClickListener(this);
        mRecordButton = findViewById(R.id.bt_record);
        mRecordButton.setOnFinishedRecordListener(this);
        mExpressionButton = findViewById(R.id.bt_expression);
        mEditText = findViewById(R.id.et_input);
        mEditText.addTextChangedListener(this);
        mReceiverNameTextView = findViewById(R.id.tv_receiver_name);
        mReceiverNameTextView.setText(mUserInfo.getUsername());
        mMoreRelativeLayout = findViewById(R.id.rl_more);
        mPhotographButtonLayout = findViewById(R.id.rl_photograph_button);
        mPhotographButtonLayout.setOnClickListener(this);

        mKeyboardChangeListener = new KeyboardChangeListener(this);
        mKeyboardChangeListener.setKeyBoardListener(this);
    }

    private void RegisterMessageBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantData.RECEIVED_MESSAGE_BROADCAST);
        mMessageBroadcastReceiver = new MessageBroadcastReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mMessageBroadcastReceiver, intentFilter);
    }

    private void initData() {
        if (ChatMessageData.getChatMessageMap().get(mUserInfo.getAccount()) != null) {
            if (ChatMessageData.getChatMessageMap().get(mUserInfo.getAccount()).size() > 10) {
                mLayoutManager.setStackFromEnd(true);
            }
            mChatMessageAdapter.refreshItems(ChatMessageData.getChatMessageMap().get(mUserInfo.getAccount()));
        }
        mScrollRecyclerViewHandler.sendEmptyMessage(0);

        mMyAvatarPath = mContext.getFilesDir().getPath() + File.separator
            + ConstantData.PHOTO_DIRECTORY + File.separator + ConstantData.AVATAR_DIRECTORY
            + File.separator + ConstantData.MY_AVATAR_NAME + ConstantData.EXAMPLE_EXTENSION_NAME;
        mCompressionPath = mContext.getFilesDir().getPath() + File.separator + ConstantData.PHOTO_DIRECTORY
            + File.separator + ConstantData.PHOTO_RECORD_DIRECTORY + File.separator + ConstantData.PHOTO_RECORD_COMPRESSION_DIRECTORY;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        if (!"".equals(mEditText.getText().toString())) {
            mMoreButton.setVisibility(View.GONE);
            mSendButton.setVisibility(View.VISIBLE);
        } else {
            mMoreButton.setVisibility(View.VISIBLE);
            mSendButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        mMessage = mEditText.getText().toString();
    }

    @Override
    public void onClick(View view) {
        if (mMessage != null && !"".equals(mMessage) && view.getId() == R.id.bt_send) {
            ChatMessage chatMessage = createChatMessage(ChatMessage.TEXT_TYPE, mMessage, "", "", "");
            mUserInfo.setLastMessage(mMessage);
            mUserInfo.setLastMessageSendTime(chatMessage.getSendTime());
            addItem(chatMessage);
            sendMessageBinderService.sendMessage(chatMessage, mUserInfo);
            mEditText.setText("");
        } else if (view.getId() == R.id.bt_more) {
            hideKeyboard(view.getWindowToken());
            mScrollRecyclerViewHandler.sendEmptyMessage(0);
            mMoreRelativeLayout.setVisibility(View.VISIBLE);
        } else if (view.getId() == R.id.rl_photograph_button) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_REQUEST_CODE);
        } else if (view.getId() == R.id.bt_voice) {
            if (mRecordButton.isShown()) {
                mRecordButton.setVisibility(View.GONE);
                mEditText.setVisibility(View.VISIBLE);
                mVoiceButton.setImageResource(R.mipmap.icon_voice);

            } else {
                mRecordButton.setVisibility(View.VISIBLE);
                mEditText.setVisibility(View.GONE);
                mVoiceButton.setImageResource(R.drawable.ic_keyboard);
            }
        }
    }

    private void addItem(ChatMessage chatMessage) {
        mChatMessageAdapter.addItem(chatMessage);
        mScrollRecyclerViewHandler.sendEmptyMessageDelayed(0, 250);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    }
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                    if (cursor == null) {
                        return;
                    }
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String path = cursor.getString(columnIndex);  //获取照片路径
                    Bitmap bitmap = null;
                    String compressionImagePath = "";
                    try {
                        bitmap = FileProcessUtil.getBitmap(mContext, path);
                        if (bitmap == null) {
                            return;
                        }
                        Bitmap compressionBitmap = BitmapUtil.zoomImg(bitmap);
                        compressionImagePath = mCompressionPath + File.separator + System.currentTimeMillis() + ".png";
                        BitmapUtil.saveImg(compressionImagePath, compressionBitmap);
                    } catch (IOException e) {
                        LogUtil.e(TAG, "save image error");
                    }

                    ChatMessage chatMessage = createChatMessage(ChatMessage.PHOTO_TYPE, "", "", compressionImagePath, "");
                    mUserInfo.setLastMessage(ConstantData.PHOTO_MESSAGE);
                    mUserInfo.setLastMessageSendTime(chatMessage.getSendTime());
                    addItem(chatMessage);
                    sendMessageBinderService.sendMessage(chatMessage, mUserInfo);
                }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            if (isShouldHideKeyboard(getCurrentFocus(), motionEvent)) {
                hideKeyboard(getCurrentFocus().getWindowToken());
            }
            if (!isTouchPointInView(mMoreRelativeLayout, (int) motionEvent.getX(), (int) motionEvent.getY())) {
                mMoreRelativeLayout.setVisibility(View.GONE);
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    private ChatMessage createChatMessage(int chatMessageType, String chatMessageText,
                                          String chatMessageVoicePath, String chatMessagePhotoPath,
                                          String chatMessageVideoPath) {
        return new ChatMessage(ConstantData.MY_NAME.hashCode(), mUserInfo.getAccount(),
            ConstantData.MY_NAME, mUserInfo.getUsername(), mMyAvatarPath, 0,
            chatMessageType, ChatMessage.SEND_TYPE, System.currentTimeMillis(), chatMessageText,
            chatMessageVoicePath, chatMessagePhotoPath, chatMessageVideoPath);
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
            && x <= right) {
            return true;
        }
        return false;
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，
     * 来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    private boolean isShouldHideKeyboard(View view, MotionEvent event) {
        if ((view instanceof EditText)) {
            int[] l = {0, 0};
            view.getLocationInWindow(l);
            int left = l[0],
                top = l[1],
                bottom = top + view.getHeight(),
                right = left + view.getWidth();
            // 点击EditText的事件，忽略它。
            if (event.getRawX() > left - 200 && event.getRawX() < right + 300
                && event.getRawY() > top - 50 && event.getRawY() < bottom + 50) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }

        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onKeyboardChange(boolean isShow, int keyboardHeight) {
        if (isShow) {
            LogUtil.i(TAG, "height %s", keyboardHeight);
            mMoreRelativeLayout.setVisibility(View.GONE);
            mScrollRecyclerViewHandler.sendEmptyMessage(0);
        } else {
            mScrollRecyclerViewHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onFinishedRecord(String voicePath, int time) {
        LogUtil.d(TAG, "record finish");
        File file = new File(voicePath);
        if (file.exists()) {
            ChatMessage chatMessage = createChatMessage(ChatMessage.VOICE_TYPE, String.valueOf(time), voicePath, "", "");
            mUserInfo.setLastMessage(ConstantData.PHOTO_MESSAGE);
            mUserInfo.setLastMessageSendTime(chatMessage.getSendTime());
            addItem(chatMessage);
            sendMessageBinderService.sendMessage(chatMessage, mUserInfo);
        }
    }

    class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i(TAG, "receive broadcast");
            if (intent == null) {
                LogUtil.e(TAG, "intent is null");
                return;
            }
            Object object = intent.getSerializableExtra(ConstantData.EXTRA_CHAT_MESSAGE);
            if (object instanceof ChatMessage) {
                addItem((ChatMessage) object);
            }
        }
    }
}
