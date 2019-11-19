package cn.csu.software.wechat.fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Pager Adapter
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class WeChatFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    /**
     * 有参构造方法
     *
     * @param fragmentList Fragment
     * @param fragmentManager FragmentManager
     */
    public WeChatFragmentPagerAdapter(List<Fragment> fragmentList, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.fragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return fragmentList != null && !fragmentList.isEmpty() ? fragmentList.size() : 0;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
}
