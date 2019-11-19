package cn.csu.software.wechat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.csu.software.wechat.constant.Configure;
import cn.csu.software.wechat.constant.ConstantData;
import cn.csu.software.wechat.data.ChatMessageData;
import cn.csu.software.wechat.data.FriendChatInfoData;
import cn.csu.software.wechat.data.UserInfoData;
import cn.csu.software.wechat.fragment.TabCircleFragment;
import cn.csu.software.wechat.fragment.TabFriendFragment;
import cn.csu.software.wechat.fragment.TabMessageFragment;
import cn.csu.software.wechat.fragment.TabMineFragment;
import cn.csu.software.wechat.fragment.adapter.WeChatFragmentPagerAdapter;
import cn.csu.software.wechat.service.SocketService;
import cn.csu.software.wechat.util.BitmapUtil;
import cn.csu.software.wechat.util.FileProcessUtil;
import cn.csu.software.wechat.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 主页面
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int IMAGE_REQUEST_CODE = 1;

    private Context mContext;

    private ViewPager mViewPager;

    private BottomNavigationView mBottomNavigationView;

    private MenuItem mMenuItem;

    private TextView mViewPagerNameTextView;

    private WeChatFragmentPagerAdapter mPagerAdapter;

    private RelativeLayout mHeaderRelativeLayout;

    private RelativeLayout mAddRelativeLayout;

    private RelativeLayout mSearchRelativeLayout;

    private RelativeLayout mGroupChatRelativeLayout;

    private RelativeLayout mAddFriendRelativeLayout;

    private RelativeLayout mScanRelativeLayout;

    private RelativeLayout mReceiverPaymentRelativeLayout;

    private RelativeLayout mHelpRelativeLayout;

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        verifyAppPermissions();
        initConfigure();
        isLogin();
    }

    private void initConfigure() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantData.SHARED_LOGIN_NAME, MODE_PRIVATE);
        Configure.setIsLogin(sharedPreferences.getBoolean(ConstantData.SHARED_LOGIN_KEY, false));
        Configure.setMyAccount(sharedPreferences.getInt(ConstantData.SHARED_ACCOUNT_KEY, 0));
    }

    private void initData() {
        UserInfoData.initDatabaseHelper(mContext);
        UserInfoData.queryAllFriendChatInfo();
        FriendChatInfoData.initDatabaseHelper(mContext);
        FriendChatInfoData.queryAllFriendChatInfo();
        ChatMessageData.initDatabaseHelper(mContext);
        ChatMessageData.queryChatMessage();
    }

    private void startSocketService() {
        Intent intent = new Intent(mContext, SocketService.class);
        startService(intent);
    }

    private void isLogin() {
        if (!Configure.isIsLogin()) {
            Intent intent = new Intent();
            intent.setClassName(ConstantData.PACKAGE_NAME, ConstantData.ACTIVITY_CLASS_NAME_LOGIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_main);
            createFileDirectory();
            initView();
            initTab();
            initData();
            startSocketService();
        }
    }

    private void initView() {
        mViewPager = findViewById(R.id.vp_viewpager);
        mViewPager.addOnPageChangeListener(this);
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mAddRelativeLayout = findViewById(R.id.rl_add);
        mAddRelativeLayout.setOnClickListener(this);
        mViewPagerNameTextView = findViewById(R.id.tv_tab_name);
        mHeaderRelativeLayout = findViewById(R.id.rl_header);
        mSearchRelativeLayout = findViewById(R.id.rl_search);
        mSearchRelativeLayout.setOnClickListener(this);
    }

    private void initTab() {
        mFragments.add(TabMessageFragment.newInstance());
        mFragments.add(TabFriendFragment.newInstance());
        mFragments.add(TabCircleFragment.newInstance());
        mFragments.add(TabMineFragment.newInstance());

        mPagerAdapter = new WeChatFragmentPagerAdapter(mFragments, getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void createFileDirectory() {
        LogUtil.i(TAG, "create directory");
        String voicePath = mContext.getFilesDir().getPath() + File.separator + ConstantData.VOICE_DIRECTORY;
        if (!FileProcessUtil.isExist(voicePath)) {
            if (FileProcessUtil.createDirectory(voicePath)) {
                LogUtil.i(TAG, "create voice directory success");
            }
        }
        String photoPath = mContext.getFilesDir().getPath() + File.separator + ConstantData.PHOTO_DIRECTORY;
        if (!FileProcessUtil.isExist(photoPath)) {
            if (FileProcessUtil.createDirectory(photoPath)) {
                LogUtil.i(TAG, "create photo directory success");
            }
        }
        String recordPath = mContext.getFilesDir().getPath() + File.separator + ConstantData.PHOTO_DIRECTORY
            + File.separator + ConstantData.PHOTO_RECORD_DIRECTORY;
        LogUtil.i(TAG, mContext.getFilesDir().getPath());
        if (!FileProcessUtil.isExist(recordPath)) {
            if (FileProcessUtil.createDirectory(recordPath)) {
                LogUtil.i(TAG, "create avatar directory success");
            }
        }
        String recordCompressionPath = mContext.getFilesDir().getPath() + File.separator
            + ConstantData.PHOTO_DIRECTORY + File.separator + ConstantData.PHOTO_RECORD_DIRECTORY
            + File.separator + ConstantData.PHOTO_RECORD_COMPRESSION_DIRECTORY;
        LogUtil.i(TAG, mContext.getFilesDir().getPath());
        if (!FileProcessUtil.isExist(recordCompressionPath)) {
            if (FileProcessUtil.createDirectory(recordCompressionPath)) {
                LogUtil.i(TAG, "create record directory success");
            }
        }
        String avatarPath = mContext.getFilesDir().getPath() + File.separator + ConstantData.PHOTO_DIRECTORY
            + File.separator + ConstantData.AVATAR_DIRECTORY;
        LogUtil.i(TAG, mContext.getFilesDir().getPath());
        if (!FileProcessUtil.isExist(avatarPath)) {
            if (FileProcessUtil.createDirectory(avatarPath)) {
                LogUtil.i(TAG, "create avatar directory success");
                copyAvatar();
            }
        }
    }

    private void copyAvatar() {
        LogUtil.i(TAG, "copy avatar");
        for (String name : ConstantData.EXAMPLE_AVATAR_NAME) {
            String avatarSourcePath = ConstantData.PHOTO_DIRECTORY + File.separator + ConstantData.AVATAR_DIRECTORY
                + File.separator + name + ConstantData.EXAMPLE_EXTENSION_NAME;
            String avatarTargetPath = mContext.getFilesDir().getPath() + File.separator + ConstantData.PHOTO_DIRECTORY
                + File.separator + ConstantData.AVATAR_DIRECTORY + File.separator + name + ConstantData.EXAMPLE_EXTENSION_NAME;
            Bitmap bitmap = FileProcessUtil.getAssetsFileBitmap(mContext, avatarSourcePath);
            if (bitmap != null) {
                try {
                    BitmapUtil.saveImg(avatarTargetPath, bitmap);
                } catch (IOException e) {
                    LogUtil.e(TAG, "save image error");
                }
            }
        }
    }

    private void verifyAppPermissions() {
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this,
            ConstantData.EXTERNAL_STORAGE_PERMISSIONS);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, ConstantData.PERMISSIONS, 1);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                moveTaskToBack(true);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        switch (itemId) {
            case R.id.tab_message:
                mHeaderRelativeLayout.setVisibility(View.VISIBLE);
                mViewPagerNameTextView.setText(R.string.tab_name_message);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tab_friend:
                mHeaderRelativeLayout.setVisibility(View.VISIBLE);
                mViewPagerNameTextView.setText(R.string.tab_name_friend);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tab_circle:
                mHeaderRelativeLayout.setVisibility(View.VISIBLE);
                mViewPagerNameTextView.setText(R.string.tab_name_circle);
                mViewPager.setCurrentItem(2);
                break;
            case R.id.tab_mine:
                mHeaderRelativeLayout.setVisibility(View.GONE);
                mViewPagerNameTextView.setText(R.string.tab_name_mine);
                mViewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mMenuItem = mBottomNavigationView.getMenu().getItem(position);
        mMenuItem.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int position) {
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_add) {
            View popupWindowView = LayoutInflater.from(mContext).inflate(R.layout.item_popup_window_add,
                null, false);
            mGroupChatRelativeLayout = popupWindowView.findViewById(R.id.rl_group_chat);
            mGroupChatRelativeLayout.setOnClickListener(this);
            mAddFriendRelativeLayout = popupWindowView.findViewById(R.id.rl_add_friend);
            mAddFriendRelativeLayout.setOnClickListener(this);
            mScanRelativeLayout = popupWindowView.findViewById(R.id.rl_scan);
            mScanRelativeLayout.setOnClickListener(this);
            mReceiverPaymentRelativeLayout = popupWindowView.findViewById(R.id.rl_receive_payment);
            mReceiverPaymentRelativeLayout.setOnClickListener(this);
            mHelpRelativeLayout = popupWindowView.findViewById(R.id.rl_help);
            mHelpRelativeLayout.setOnClickListener(this);
            PopupWindow popWindow = new PopupWindow(popupWindowView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popWindow.showAsDropDown(view, 50, -20, Gravity.END);
        } else if (view.getId() == R.id.rl_search) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_REQUEST_CODE);
        } else if (view.getId() == R.id.rl_group_chat) {
            Toast.makeText(mContext, R.string.group_chat, Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.rl_add_friend) {
            Toast.makeText(mContext, R.string.add_friend, Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.rl_scan) {
            Toast.makeText(mContext, R.string.scan, Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.rl_receive_payment) {
            Toast.makeText(mContext, R.string.receive_payment, Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.rl_help) {
            Toast.makeText(mContext, R.string.help, Toast.LENGTH_SHORT).show();
        }
    }
}
