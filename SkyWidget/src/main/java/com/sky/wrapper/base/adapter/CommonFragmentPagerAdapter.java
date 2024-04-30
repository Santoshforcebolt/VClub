package com.sky.wrapper.base.adapter;

import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author 刘琛慧
 *         date 2016/2/25.
 */
public abstract class CommonFragmentPagerAdapter<T> extends FragmentPagerAdapter {
    protected List<T> mDataList;
    protected ArrayMap<Integer, Fragment> mFragmentMaps = new ArrayMap<>();

    public CommonFragmentPagerAdapter(FragmentManager fm, List<T> mDataList) {
        super(fm);
        this.mDataList = mDataList;
    }

    @Override
    public Fragment getItem(int position) {
        T data = mDataList.get(position);
        Fragment fragment = mFragmentMaps.get(position);

        if (fragment == null) {
            fragment = initFragment(data);
            mFragmentMaps.put(position, fragment);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    protected abstract Fragment initFragment(T data);

    public List<T> getDataList() {
        return mDataList;
    }

    public void setDataList(List<T> dataList) {
        this.mDataList = dataList;
    }


}
