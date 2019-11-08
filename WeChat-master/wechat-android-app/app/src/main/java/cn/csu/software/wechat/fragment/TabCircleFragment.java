/*
 * Copyright (c) 2019-2019 cn.csu.software. All rights reserved.
 */

package cn.csu.software.wechat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.csu.software.wechat.R;

/**
 * 消息界面
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class TabCircleFragment extends Fragment implements View.OnClickListener {
    // TODO: 19-10-20 后续开发朋友圈界面
    private static final String TAG = TabCircleFragment.class.getSimpleName();

    private Context mContext;

    private View mView;

    public static TabCircleFragment newInstance(){
        return new TabCircleFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_tab_circle, container, false);
        initView();
        return mView;
    }

    private void initView() {
    }


    @Override
    public void onClick(View view) {

    }

}
