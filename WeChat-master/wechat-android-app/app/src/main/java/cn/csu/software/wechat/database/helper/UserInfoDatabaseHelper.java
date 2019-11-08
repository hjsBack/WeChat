/*
 * Copyright (c) 2019-2019 cn.csu.software. All rights reserved.
 */

package cn.csu.software.wechat.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import cn.csu.software.wechat.entity.UserInfo;
import cn.csu.software.wechat.database.content.UserInfoContent;
import cn.csu.software.wechat.util.LogUtil;

/**
 * 好友消息 database helper
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class UserInfoDatabaseHelper extends SQLiteOpenHelper {
    // TODO: 19-10-20 后续数据库代码重构，暂时不加注释
    private static final String TAG = UserInfoDatabaseHelper.class.getSimpleName();

    public static final String TABLE_NAME = "user_info";

    private static final String DB_NAME = "user_info.db";

    private static final int DB_VERSION = 1;

    private static UserInfoDatabaseHelper sDatabaseHelper;

    private SQLiteDatabase mDatabase;

    private UserInfoDatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private UserInfoDatabaseHelper(@Nullable Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    public static UserInfoDatabaseHelper getInstance(Context context, int version) {
        if (version > 0 && sDatabaseHelper == null) {
            sDatabaseHelper = new UserInfoDatabaseHelper(context, version);
        } else if (sDatabaseHelper == null) {
            sDatabaseHelper = new UserInfoDatabaseHelper(context);
        }
        return sDatabaseHelper;
    }

    /**
     * 打开数据库的读连接
     *
     * @return SQLiteDatabase
     */
    public SQLiteDatabase openReadLink() {
        if (mDatabase == null || !mDatabase.isOpen()) {
            mDatabase = sDatabaseHelper.getReadableDatabase();
        }
        return mDatabase;
    }

    /**
     * 打开数据库的写连接
     *
     * @return SQLiteDatabase
     */
    public SQLiteDatabase openWriteLink() {
        if (mDatabase == null || !mDatabase.isOpen()) {
            mDatabase = sDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    /**
     * 关闭数据库连接
     */
    public void closeLink() {
        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
            mDatabase = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        LogUtil.i(TAG, "onCreate");
        String drop_sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        LogUtil.i(TAG, "drop_sql:" + drop_sql);
        sqLiteDatabase.execSQL(drop_sql);
        String create_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + "_id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,"
                + UserInfoContent.ACCOUNT + " INTEGER NOT NULL,"
                + UserInfoContent.EMAIL + " VARCHAR NOT NULL,"
                + UserInfoContent.USERNAME + " VARCHAR NOT NULL,"
                + UserInfoContent.REMARK + " VARCHAR NOT NULL,"
                + UserInfoContent.AVATAR_PATH + " VARCHAR NOT NULL,"
                + UserInfoContent.SEX + " INTEGER NOT NULL"
                + ");";
        LogUtil.d(TAG, "create_sql:" + create_sql);
        sqLiteDatabase.execSQL(create_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        LogUtil.d(TAG, "onUpgrade oldVersion=" + oldVersion + ", newVersion=" + newVersion);
        if (newVersion > 1) {
            //Android的ALTER命令不支持一次添加多列，只能分多次添加
            String alter_sql = "ALTER TABLE " + TABLE_NAME;
            LogUtil.d(TAG, "alter_sql:" + alter_sql);
            sqLiteDatabase.execSQL(alter_sql);
            alter_sql = "ALTER TABLE " + TABLE_NAME;
            LogUtil.d(TAG, "alter_sql:" + alter_sql);
            sqLiteDatabase.execSQL(alter_sql);
        }
    }

    // 根据指定条件删除表记录
    public int delete(String condition) {
        // 执行删除记录动作，该语句返回删除记录的数目
        return mDatabase.delete(TABLE_NAME, condition, null);
    }

    // 删除该表的所有记录
    public int deleteAll() {
        // 执行删除记录动作，该语句返回删除记录的数目
        return mDatabase.delete(TABLE_NAME, "1=1", null);
    }

    // 往该表添加一条记录
    public long insert(UserInfo userInfo) {
        ArrayList<UserInfo> userInfoList = new ArrayList<>();
        userInfoList.add(userInfo);
        return insert(userInfoList);
    }

    // 往该表添加多条记录
    public long insert(ArrayList<UserInfo> userInfoList) {
        long result = -1;
        for (int i = 0; i < userInfoList.size(); i++) {
            UserInfo userInfo = userInfoList.get(i);
            // 如果存在同名记录，则更新记录
            // 注意条件语句的等号后面要用单引号括起来
            // 如果存在同样的手机号码，则更新记录
            // 不存在唯一性重复的记录，则插入新记录
            ContentValues contentValues = new ContentValues();
            contentValues.put(UserInfoContent.ACCOUNT, userInfo.getAccount());
            contentValues.put(UserInfoContent.EMAIL, userInfo.getEmail());
            contentValues.put(UserInfoContent.USERNAME, userInfo.getUsername());
            contentValues.put(UserInfoContent.REMARK, userInfo.getRemark());
            contentValues.put(UserInfoContent.AVATAR_PATH, userInfo.getAvatarPath());
            contentValues.put(UserInfoContent.SEX, userInfo.getSex());
            // 执行插入记录动作，该语句返回插入记录的行号
            result = mDatabase.insert(TABLE_NAME, "", contentValues);
            // 添加成功后返回行号，失败后返回-1
            if (result == -1) {
                return result;
            }
        }
        return result;
    }

    // 根据条件更新指定的表记录
    public int update(UserInfo userInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserInfoContent.ACCOUNT, userInfo.getAccount());
        contentValues.put(UserInfoContent.EMAIL, userInfo.getEmail());
        contentValues.put(UserInfoContent.USERNAME, userInfo.getUsername());
        contentValues.put(UserInfoContent.REMARK, userInfo.getRemark());
        contentValues.put(UserInfoContent.AVATAR_PATH, userInfo.getAvatarPath());
        contentValues.put(UserInfoContent.SEX, userInfo.getSex());
        // 执行更新记录动作，该语句返回记录更新的数目
        String condition = String.format("update account from %s where %s", TABLE_NAME, userInfo.getAccount());
        return mDatabase.update(TABLE_NAME, contentValues, condition, null);
    }

    // 根据指定条件查询记录，并返回结果数据队列
    public ArrayList<UserInfo> query(String condition) {
        String sql = String.format("select " + UserInfoContent.ACCOUNT + ","
                + UserInfoContent.EMAIL + "," + UserInfoContent.USERNAME + ","
                + UserInfoContent.REMARK + "," + UserInfoContent.AVATAR_PATH + ","
                + UserInfoContent.SEX + " from %s where %s;", TABLE_NAME, condition);
        LogUtil.d(TAG, "query sql: " + sql);
        ArrayList<UserInfo> userInfoList = new ArrayList<>();
        // 执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mDatabase.rawQuery(sql, null);
        // 循环取出游标指向的每条记录
        while (cursor.moveToNext()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setAccount(cursor.getInt(0));
            userInfo.setEmail(cursor.getString(1));
            userInfo.setUsername(cursor.getString(2));
            userInfo.setRemark(cursor.getString(3));
            userInfo.setAvatarPath(cursor.getString(4));
            userInfo.setSex(cursor.getInt(5));
            userInfoList.add(userInfo);
        }
        cursor.close(); // 查询完毕，关闭游标
        return userInfoList;
    }

    // 根据指定条件查询记录，并返回结果数据队列
    public UserInfo queryByAccount(int account) {
        String sql = String.format("select " + UserInfoContent.ACCOUNT + ","
            + UserInfoContent.EMAIL + "," + UserInfoContent.USERNAME + ","
            + UserInfoContent.REMARK + "," + UserInfoContent.AVATAR_PATH + ","
            + UserInfoContent.SEX + " from %s where " + UserInfoContent.ACCOUNT + "=%s;", TABLE_NAME, account);
        LogUtil.d(TAG, "query sql: " + sql);
        ArrayList<UserInfo> userInfoList = new ArrayList<>();
        // 执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mDatabase.rawQuery(sql, null);
        // 循环取出游标指向的每条记录
        UserInfo userInfo = new UserInfo();
        while (cursor.moveToNext()) {
            userInfo.setAccount(cursor.getInt(0));
            userInfo.setEmail(cursor.getString(1));
            userInfo.setUsername(cursor.getString(2));
            userInfo.setRemark(cursor.getString(3));
            userInfo.setAvatarPath(cursor.getString(4));
            userInfo.setSex(cursor.getInt(5));
        }
        cursor.close(); // 查询完毕，关闭游标
        return userInfo;
    }
}
