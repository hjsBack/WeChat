package cn.csu.software.wechat.fragment;

import android.content.Context;
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
import cn.csu.software.wechat.adapter.FriendInfoAdapter;
import cn.csu.software.wechat.data.UserInfoData;
import cn.csu.software.wechat.entity.UserInfo;

/**
 * 消息界面
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class TabFriendFragment extends Fragment {
    private static final String TAG = TabMessageFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;

    private FriendInfoAdapter adapter;

    private Context mContext;

    private View mView;

    /**
     * 静态工厂方法
     * @return TabFriendFragment
     */
    public static TabFriendFragment newInstance() {
        return new TabFriendFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_tab_friend, container, false);
        adapter = new FriendInfoAdapter(mContext);
        recyclerView = mView.findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        refreshData();
        return mView;
    }

    private void refreshData() {
        for (UserInfo userInfo : UserInfoData.getChatMessagesList()) {
            adapter.addItem(userInfo);
        }
    }
}
