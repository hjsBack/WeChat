package cn.csu.software.wechat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import cn.csu.software.wechat.R;
import cn.csu.software.wechat.constant.ConstantData;

/**
 * 设置界面
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-11-14
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mLogoutRelativeLayout;

    private RelativeLayout mExitRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_setting);
        mLogoutRelativeLayout = findViewById(R.id.rl_setting_logout);
        mLogoutRelativeLayout.setOnClickListener(this);
        mExitRelativeLayout = findViewById(R.id.rl_setting_exit);
        mExitRelativeLayout.setOnClickListener(this);
    }

    private void exit() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantData.SHARED_LOGIN_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ConstantData.SHARED_LOGIN_KEY, false);
        editor.apply();
        Intent intent = new Intent();
        intent.setClassName(ConstantData.PACKAGE_NAME, ConstantData.ACTIVITY_CLASS_NAME_LOGIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_setting_logout:
            case R.id.rl_setting_exit:
                exit();
                break;
            default:
                break;
        }
    }
}
