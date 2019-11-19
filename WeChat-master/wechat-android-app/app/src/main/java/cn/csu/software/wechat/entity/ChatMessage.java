package cn.csu.software.wechat.entity;

import java.io.Serializable;

/**
 * 聊天消息
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 5956611960274891927L;

    /**
     * 文本消息类型
     */
    public static final int TEXT_TYPE = 0;

    /**
     * 图片消息类型
     */
    public static final int PHOTO_TYPE = 1;

    /**
     * 语音消息类型
     */
    public static final int VOICE_TYPE = 2;

    /**
     * 视频消息类型
     */
    public static final int VIDEO_TYPE = 3;

    /**
     * 文本图片综合消息类型
     */
    public static final int TEXT_AND_PHOTO_TYPE = 4;

    public static final int SEND_TYPE = 0;

    public static final int RECEIVER_TYPE = 1;

    private int senderAccount;

    private int receiverAccount;

    private String senderName;

    private String receiverName;

    private String avatarPath;

    private int unreadMessageCount;

    private int chatMessageType;

    private int sendOrReceiver;

    private long sendTime;

    private String chatMessageText;

    private String chatMessageVoicePath;

    private String chatMessagePhotoPath;

    private String chatMessageVideoPath;

    public ChatMessage() {
        this.senderAccount = 0;
        this.receiverAccount = 0;
        this.senderName = "";
        this.receiverName = "";
        this.avatarPath = "";
        this.unreadMessageCount = 0;
        sendOrReceiver = 0;
        this.chatMessageType = 0;
        this.sendTime = 0;
        this.chatMessageText = "";
        chatMessageVoicePath = "";
        this.chatMessagePhotoPath = "";
        this.chatMessageVideoPath = "";
    }

    public ChatMessage(int senderAccount, int receiverAccount, String senderName,
                       String receiverName, String avatarPath, int unreadMessageCount,
                       int chatMessageType, int sendOrReceiver, long sendTime, String chatMessageText,
                       String chatMessageVoicePath, String chatMessagePhotoPath, String chatMessageVideoPath) {
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.avatarPath = avatarPath;
        this.unreadMessageCount = unreadMessageCount;
        this.sendOrReceiver = sendOrReceiver;
        this.chatMessageType = chatMessageType;
        this.sendTime = sendTime;
        this.chatMessageText = chatMessageText;
        this.chatMessageVoicePath = chatMessageVoicePath;
        this.chatMessagePhotoPath = chatMessagePhotoPath;
        this.chatMessageVideoPath = chatMessageVideoPath;
    }

    public int getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(int senderAccount) {
        this.senderAccount = senderAccount;
    }

    public int getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(int receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public String getChatMessageText() {
        return chatMessageText;
    }

    public void setChatMessageText(String chatMessageText) {
        this.chatMessageText = chatMessageText;
    }

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public int getSendOrReceiver() {
        return sendOrReceiver;
    }

    public void setSendOrReceiver(int sendOrReceiver) {
        this.sendOrReceiver = sendOrReceiver;
    }

    public int getChatMessageType() {
        return chatMessageType;
    }

    public void setChatMessageType(int chatMessageType) {
        this.chatMessageType = chatMessageType;
    }

    public String getChatMessageVoicePath() {
        return chatMessageVoicePath;
    }

    public void setChatMessageVoicePath(String chatMessageVoicePath) {
        this.chatMessageVoicePath = chatMessageVoicePath;
    }

    public String getChatMessagePhotoPath() {
        return chatMessagePhotoPath;
    }

    public void setChatMessagePhotoPath(String chatMessagePhotoPath) {
        this.chatMessagePhotoPath = chatMessagePhotoPath;
    }

    public String getChatMessageVideoPath() {
        return chatMessageVideoPath;
    }

    public void setChatMessageVideoPath(String chatMessageVideoPath) {
        this.chatMessageVideoPath = chatMessageVideoPath;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
            "senderAccount=" + senderAccount +
            ", receiverAccount=" + receiverAccount +
            ", senderName='" + senderName + '\'' +
            ", receiverName='" + receiverName + '\'' +
            ", avatarPath='" + avatarPath + '\'' +
            ", unreadMessageCount=" + unreadMessageCount +
            ", chatMessageType=" + chatMessageType +
            ", sendOrReceiver=" + sendOrReceiver +
            ", sendTime=" + sendTime +
            ", chatMessageText='" + chatMessageText + '\'' +
            ", chatMessageVoicePath='" + chatMessageVoicePath + '\'' +
            ", chatMessagePhotoPath='" + chatMessagePhotoPath + '\'' +
            ", chatMessageVideoPath='" + chatMessageVideoPath + '\'' +
            '}';
    }
}
