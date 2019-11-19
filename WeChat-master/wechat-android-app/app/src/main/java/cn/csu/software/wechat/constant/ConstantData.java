package cn.csu.software.wechat.constant;

/**
 * 项目 constant data
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class ConstantData {
    /**
     * 保存图片资源的目录
     */
    public static final String PHOTO_DIRECTORY = "photo";

    /**
     * 保存录音资源的目录
     */
    public static final String VOICE_DIRECTORY = "voice";

    /**
     * 保存头像资源的目录
     */
    public static final String AVATAR_DIRECTORY = "avatar";

    /**
     * 保存聊天记录图片资源的目录
     */
    public static final String PHOTO_RECORD_DIRECTORY = "record";

    /**
     * 保存聊天记录图片资源的目录
     */
    public static final String PHOTO_RECORD_COMPRESSION_DIRECTORY = "compression";

    /**
     * PNG后缀
     */
    public static final String EXTENSION_NAME_PNG = ".png";

    /**
     * MP3后缀
     */
    public static final String EXTENSION_NAME_MP3 = ".mp3";

    /**
     * 全局查询条件
     */
    public static final String GLOBAL_QUERY_CONDITION = "1=1";

    /**
     * 数据库第一次创建
     */
    public static final int DATABASE_CREATE_VISION_FIRST_TIME = 1;

    /**
     * 数据库第二次创建
     */
    public static final int DATABASE_CREATE_VISION_SECOND_TIME = 2;

    /**
     * 我的界面头像尺寸
     */
    public static final int AVATAR_SIZE_MINE = 200;

    /**
     * 消息界面头像尺寸
     */
    public static final int AVATAR_SIZE_MESSAGE = 160;

    /**
     * 好友界面头像尺寸
     */
    public static final int AVATAR_SIZE_FRIEND = 120;

    /**
     * 头像圆角尺寸
     */
    public static final int AVATAR_CIRCLE_SIZE = 6;

    /**
     * extra account
     */
    public static final String EXTRA_ACCOUNT = "account";

    /**
     * extra remark
     */
    public static final String EXTRA_REMARK = "remark";

    /**
     * extra email
     */
    public static final String EXTRA_EMAIL = "email";

    /**
     * extra sex
     */
    public static final String EXTRA_SEX = "sex";

    /**
     * extra username
     */
    public static final String EXTRA_USERNAME = "username";

    /**
     * extra receiver name
     */
    public static final String EXTRA_RECEIVER_NAME = "receiverName";

    /**
     * extra friend name
     */
    public static final String EXTRA_FRIEND_NAME = "friendName";

    /**
     * extra avatar name
     */
    public static final String EXTRA_AVATAR_PATH = "avatarPath";

    /**
     * bundle key message
     */
    public static final String BUNDLE_KEY_MESSAGE = "message";

    /**
     * extra user info
     */
    public static final String EXTRA_USER_INFO = "userInfo";

    /**
     * extra chat message
     */
    public static final String EXTRA_CHAT_MESSAGE = "chatMessage";

    /**
     * shared login name
     */
    public static final String SHARED_LOGIN_NAME = "login";

    /**
     * shared login name
     */
    public static final String SHARED_LOGIN_KEY = "isLogin";

    /**
     * shared login name
     */
    public static final String SHARED_ACCOUNT_KEY = "account";

    /**
     * package name
     */
    public static final String PACKAGE_NAME = "cn.csu.software.wechat";

    /**
     * ChatActivity class name
     */
    public static final String ACTIVITY_CLASS_NAME_MAIN = "cn.csu.software.wechat.MainActivity";

    /**
     * PersonalInfoActivity class name
     */
    public static final String RECEIVED_MESSAGE_BROADCAST = "cn.csu.software.wechat.broadcast.message";

    /**
     * PersonalInfoActivity class name
     */
    public static final String ACTIVITY_CLASS_NAME_PERSONAL_INFO = "cn.csu.software.wechat.activity.PersonalInfoActivity";

    /**
     * ChatActivity class name
     */
    public static final String ACTIVITY_CLASS_NAME_CHAT = "cn.csu.software.wechat.activity.ChatActivity";

    /**
     * LoginActivity class name
     */
    public static final String ACTIVITY_CLASS_NAME_LOGIN = "cn.csu.software.wechat.activity.LoginActivity";

    /**
     * LoginActivity class name
     */
    public static final String ACTIVITY_CLASS_NAME_SETTING = "cn.csu.software.wechat.activity.SettingActivity";

    /**
     * ChatActivity class name
     */
    public static final String FILES_DIR = "/data/user/0/cn.csu.software.wechat/files";

    /**
     * EXTERNAL STORAGE PERMISSIONS
     */
    public static final String EXTERNAL_STORAGE_PERMISSIONS = "android.permission.WRITE_EXTERNAL_STORAGE";

    /**
     * PERMISSIONS
     */
    public static final String[] PERMISSIONS = {
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.INTERNET",
        "android.permission.ACCESS_NETWORK_STATE",
        "android.permission.ACCESS_WIFI_STATE",
        "android.permission.CHANGE_WIFI_STATE",
        "android.permission.RECORD_AUDIO",
        "android.permission.CAMERA"
    };

    /**
     * photo message
     */
    public static final String PHOTO_MESSAGE = "[ 图片 ]";

    /**
     * photo message
     */
    public static final String VOICE_MESSAGE = "[ 语音 ]";

    /**
     * My Name
     */
    public static final String MY_NAME = "黄绩顺";

    /**
     * 示例初始化好友列表
     */
    public static final String[] EXAMPLE_FRIEND_NAME = {"黄绩顺", "刘德华","郭富城", "黎明", "张学友", "周杰伦", "许嵩",
        "胡歌", "林俊杰", "成龙", "李连杰", "李小龙", "赵薇", "霍建华"};

    /**
     * 示例初始化好友列表
     */
    public static final int[] EXAMPLE_FRIEND_ACCOUNT = {100, 101, 102, 103, 104, 105, 106,
        107, 108, 109, 110, 111, 112, 113};

    /**
     * 示例初始化头像
     */
    public static final String[] EXAMPLE_AVATAR_NAME = {"me", "liudehua","guofucheng", "liming", "zhangxueyou",
        "zhoujielun", "xusong", "huge", "linjunjie", "chenglong", "lilianjie", "lixiaolong",
        "zhaowei", "huojianhua"};

    /**
     * extra avatar name
     */
    public static final String MY_AVATAR_NAME = "me";

    /**
     * 示例初始化头像后缀
     */
    public static final String EXAMPLE_EXTENSION_NAME = ".jpg";

    /**
     * 示例初始化最后一条消息
     */
    public static final String EXAMPLE_LAST_MESSAGE_HEADER = "hello, 我是";

    private ConstantData() {
    }
}
