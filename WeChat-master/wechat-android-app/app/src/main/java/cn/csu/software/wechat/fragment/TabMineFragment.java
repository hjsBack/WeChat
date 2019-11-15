/*
 * Copyright (c) 2019-2019 cn.csu.software. All rights reserved.
 */

package cn.csu.software.wechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import cn.csu.software.wechat.R;
import cn.csu.software.wechat.constant.ConstantData;

/**
 * 我的界面
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class TabMineFragment extends Fragment implements View.OnClickListener {
    // TODO: 19-10-20 后续开发我的界面
    private View mView;

    private RelativeLayout mSettingRelativeLayout;

    public static TabMineFragment newInstance(){
        return new TabMineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tab_mine, container, false);
        initView();
        return mView;
    }

    private void initView() {
        mSettingRelativeLayout = mView.findViewById(R.id.rl_mine_setting);
        mSettingRelativeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_mine_setting :
                Intent intent = new Intent();
                intent.setClassName(ConstantData.PACKAGE_NAME, ConstantData.ACTIVITY_CLASS_NAME_SETTING);
                startActivity(intent);
                break;
        }
    }
}
