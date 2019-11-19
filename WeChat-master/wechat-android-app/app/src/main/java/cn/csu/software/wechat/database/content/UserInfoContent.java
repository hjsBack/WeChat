package cn.csu.software.wechat.database.content;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 好友消息 content
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class UserInfoContent implements BaseColumns {
    // TODO: 19-10-20 后续数据库的创建，列表数据放在这里，注释后续添加
    public static final String AUTHORITIES = "cn.csu.software.wechat.database.provider.UserInfoProvider";

    public static final String TABLE_NAME = "user_info";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITIES + "/user_info");

    public static final String ACCOUNT = "account";

    public static final String USERNAME = "username";

    public static final String REMARK = "remark";

    public static final String EMAIL = "email";

    public static final String AVATAR_PATH = "avatar_path";

    public static final String SEX = "sex";

    // 默认的排序方法
    public static final String DEFAULT_SORT_ORDER = "_id desc";
}
