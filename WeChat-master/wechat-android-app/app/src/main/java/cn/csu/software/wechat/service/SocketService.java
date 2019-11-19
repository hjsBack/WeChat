package cn.csu.software.wechat.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import cn.csu.software.wechat.constant.ConstantData;
import cn.csu.software.wechat.data.ChatMessageData;
import cn.csu.software.wechat.data.FriendChatInfoData;
import cn.csu.software.wechat.database.helper.UserInfoDatabaseHelper;
import cn.csu.software.wechat.entity.ChatMessage;
import cn.csu.software.wechat.entity.SocketData;
import cn.csu.software.wechat.entity.UserInfo;
import cn.csu.software.wechat.socket.SocketClient;
import cn.csu.software.wechat.util.BitmapUtil;
import cn.csu.software.wechat.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * socket 服务
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-11-12
 */
public class SocketService extends Service implements SocketClient.SocketClientListener {
    private static final String TAG = SocketService.class.getSimpleName();

    private SocketClient mSocketClient;

    private ExecutorService mThreadPool;

    private Context mContext;

    private UserInfoDatabaseHelper mDatabaseHelper;

    private SendMessageBinder mSendMessageBinder = new SendMessageBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mSendMessageBinder;
    }

    @Override
    public void onCreate() {
        LogUtil.i(TAG, "Socket Service onCreate");
        mContext = this;
        startSocket();
        super.onCreate();
    }

    private void sendBroadCast(ChatMessage chatMessage) {
        Intent intent = new Intent(ConstantData.RECEIVED_MESSAGE_BROADCAST);
        intent.putExtra(ConstantData.EXTRA_CHAT_MESSAGE, chatMessage);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void startSocket() {
        mSocketClient = new SocketClient();
        mSocketClient.setSocketClientListener(this);
        mThreadPool = Executors.newFixedThreadPool(2);
        mThreadPool.execute(mSocketClient);
    }

    @Override
    public void onSocketClientListener(SocketData socketData) {
        LogUtil.i(TAG, "receive socket data: %s", socketData.toString());
        mDatabaseHelper = UserInfoDatabaseHelper.getInstance(mContext, ConstantData.DATABASE_CREATE_VISION_SECOND_TIME);
        long sendTime = System.currentTimeMillis();
        UserInfo senderUserInfo = mDatabaseHelper.queryByAccount(socketData.getSenderAccount());
        UserInfo receiverUserInfo = mDatabaseHelper.queryByAccount(socketData.getReceiverAccount());
        ChatMessage chatMessage = new ChatMessage();
        if (socketData.getMessageType() == ChatMessage.TEXT_TYPE) {
            chatMessage = new ChatMessage(senderUserInfo.getAccount(), receiverUserInfo.getAccount(),
                senderUserInfo.getUsername(), receiverUserInfo.getUsername(), senderUserInfo.getAvatarPath(),
                0, socketData.getMessageType(), ChatMessage.RECEIVER_TYPE, sendTime,
                socketData.getTextMessage(),"", "", "");
            senderUserInfo.setLastMessage(socketData.getTextMessage());
        } else if (socketData.getMessageType() == ChatMessage.PHOTO_TYPE) {
            String imagePath = mContext.getFilesDir().getPath() + File.separator
                + ConstantData.PHOTO_DIRECTORY + File.separator + ConstantData.PHOTO_RECORD_DIRECTORY
                + File.separator + sendTime + ConstantData.EXTENSION_NAME_PNG;
            String compressionPath = mContext.getFilesDir().getPath() + File.separator
                + ConstantData.PHOTO_DIRECTORY + File.separator + ConstantData.PHOTO_RECORD_DIRECTORY
                + File.separator + ConstantData.PHOTO_RECORD_COMPRESSION_DIRECTORY;
            try {
                Files.write(Paths.get(imagePath), socketData.getBytes());
                Bitmap bitmap = BitmapFactory.decodeByteArray(socketData.getBytes(), 0, socketData.getBytes().length);
                Bitmap compressionBitmap = BitmapUtil.zoomImg(bitmap);
                String compressionImagePath = compressionPath + File.separator + System.currentTimeMillis()
                    + ConstantData.EXTENSION_NAME_PNG;
                BitmapUtil.saveImg(compressionImagePath, compressionBitmap);
                chatMessage = new ChatMessage(senderUserInfo.getAccount(), receiverUserInfo.getAccount(),
                    senderUserInfo.getUsername(), receiverUserInfo.getUsername(), senderUserInfo.getAvatarPath(),
                    0, socketData.getMessageType(), ChatMessage.RECEIVER_TYPE, sendTime,
                    socketData.getTextMessage(), "", compressionImagePath, "");
                senderUserInfo.setLastMessage(ConstantData.PHOTO_MESSAGE);
            } catch (IOException e) {
                LogUtil.e(TAG, "write image error");
            }
        } else {
            String voicePath = mContext.getFilesDir().getPath() + File.separator + ConstantData.VOICE_DIRECTORY
                + File.separator + sendTime + ConstantData.EXTENSION_NAME_MP3;
            try {
                Files.write(Paths.get(voicePath), socketData.getBytes());
                chatMessage = new ChatMessage(senderUserInfo.getAccount(), receiverUserInfo.getAccount(),
                    senderUserInfo.getUsername(), receiverUserInfo.getUsername(), senderUserInfo.getAvatarPath(),
                    0, socketData.getMessageType(), ChatMessage.RECEIVER_TYPE, sendTime,
                    socketData.getTextMessage(), voicePath, "", "");
                senderUserInfo.setLastMessage(ConstantData.VOICE_MESSAGE);
            } catch (IOException e) {
                LogUtil.e(TAG, "write mp3 error");
            }
        }
        senderUserInfo.setLastMessageSendTime(sendTime);
        FriendChatInfoData.addUserInfo(senderUserInfo);
        ChatMessageData.addChatMessage(chatMessage, senderUserInfo.getAccount());
        sendBroadCast(chatMessage);
    }

    /**
     * 发送消息Binder类
     *
     * @author huangjishun 874904407@qq.com
     * @since 2019-11-12
     */
    public class SendMessageBinder extends Binder {
        /**
         * 发送消息
         *
         * @param chatMessage ChatMessage
         * @param userInfo UserInfo
         */
        public void sendMessage(final ChatMessage chatMessage, final UserInfo userInfo) {
            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        FriendChatInfoData.addUserInfo(userInfo);
                        ChatMessageData.addChatMessage(chatMessage, userInfo.getAccount());
                        SocketData socketData;
                        if (chatMessage.getChatMessageType() == ChatMessage.TEXT_TYPE) {
                            socketData = new SocketData(chatMessage.getReceiverAccount(),
                                chatMessage.getReceiverAccount(), chatMessage.getChatMessageType(),
                                chatMessage.getChatMessageText(), new byte[0]);
                        } else if (chatMessage.getChatMessageType() == ChatMessage.PHOTO_TYPE) {
                            socketData = new SocketData(chatMessage.getReceiverAccount(),
                                chatMessage.getReceiverAccount(), chatMessage.getChatMessageType(),
                                chatMessage.getChatMessageText(), Files.readAllBytes(Paths.get(chatMessage.getChatMessagePhotoPath())));
                        } else {
                            socketData = new SocketData(chatMessage.getReceiverAccount(),
                                chatMessage.getReceiverAccount(), chatMessage.getChatMessageType(),
                                chatMessage.getChatMessageText(), Files.readAllBytes(Paths.get(chatMessage.getChatMessageVoicePath())));
                        }
                        mSocketClient.sendMessage(socketData);
                    } catch (IOException e) {
                        LogUtil.i(TAG, "send message error, %s", e);
                    }
                }
            });
        }
    }
}
