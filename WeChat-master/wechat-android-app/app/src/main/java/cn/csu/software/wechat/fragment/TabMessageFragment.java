package cn.csu.software.wechat.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.csu.software.wechat.R;
import cn.csu.software.wechat.adapter.FriendChatInfoAdapter;
import cn.csu.software.wechat.constant.ConstantData;
import cn.csu.software.wechat.data.FriendChatInfoData;
import cn.csu.software.wechat.entity.FriendChatInfo;

import java.util.List;

/**
 * 消息界面
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class TabMessageFragment extends Fragment {
    private static final String TAG = TabMessageFragment.class.getSimpleName();

    private static String sTabName;

    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;

    private FriendChatInfoAdapter mAdapter;

    private Context mContext;

    private View mView;

    private List<FriendChatInfo> mFriendChatInfoList;

    private MessageBroadcastReceiver mMessageBroadcastReceiver;

    /**
     * 静态工厂方法
     * @return TabFriendFragment
     */
    public static TabMessageFragment newInstance() {
        return new TabMessageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_tab_message, container, false);
        initView();
        refreshData();
        return mView;
    }

    private void registerMessageBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantData.RECEIVED_MESSAGE_BROADCAST);
        mMessageBroadcastReceiver = new MessageBroadcastReceiver();
        mContext.registerReceiver(mMessageBroadcastReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        refreshData();
        registerMessageBroadcastReceiver();
        super.onResume();
    }

    @Override
    public void onPause() {
        mContext.unregisterReceiver(mMessageBroadcastReceiver);
        super.onPause();
    }

    private void initView() {
        mAdapter = new FriendChatInfoAdapter(mContext);
        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = mView.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void refreshData() {
        mAdapter.refreshItems(FriendChatInfoData.getUserInfoList());
    }

    /**
     * 接收消息监听器
     *
     * @author huangjishun 874904407@qq.com
     * @since 2019-10-19
     */
    class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshData();
        }
    }
}
