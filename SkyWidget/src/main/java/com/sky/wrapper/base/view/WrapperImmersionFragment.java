//package com.sky.wrapper.base.view;
//
//import android.content.res.Configuration;
//import android.os.Bundle;
//
//import androidx.annotation.Nullable;
//
//import com.gyf.immersionbar.components.ImmersionOwner;
//import com.gyf.immersionbar.components.ImmersionProxy;
//
//import me.yokeyword.fragmentation.SupportFragment;
//
///**
// * Author: sky on 2021/5/17 14:13
// * Email: xcode126@126.com
// * Desc:
// */
//@Deprecated
//public abstract class WrapperImmersionFragment extends SupportFragment implements ImmersionOwner {
//    /**
//     * ImmersionBar代理类
//     */
//    private ImmersionProxy mImmersionProxy = new ImmersionProxy(this);
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        mImmersionProxy.setUserVisibleHint(isVisibleToUser);
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mImmersionProxy.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mImmersionProxy.onActivityCreated(savedInstanceState);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mImmersionProxy.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mImmersionProxy.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mImmersionProxy.onDestroy();
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        mImmersionProxy.onHiddenChanged(hidden);
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mImmersionProxy.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    public void initImmersionBar() {
////        ImmersionBar.with(this).navigationBarColor(R.color.colorFore).init();
//    }
//
//    /**
//     * 懒加载，在view初始化完成之前执行
//     * On lazy after view.
//     */
//    @Override
//    public void onLazyBeforeView() {
//    }
//
//    /**
//     * 懒加载，在view初始化完成之后执行
//     * On lazy before view.
//     */
//    @Override
//    public void onLazyAfterView() {
//    }
//
//    /**
//     * Fragment用户可见时候调用
//     * On visible.
//     */
//    @Override
//    public void onVisible() {
//    }
//
//    /**
//     * Fragment用户不可见时候调用
//     * On invisible.
//     */
//    @Override
//    public void onInvisible() {
//    }
//
//    /**
//     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
//     * Immersion bar enabled boolean.
//     *
//     * @return the boolean
//     */
//    @Override
//    public boolean immersionBarEnabled() {
//        return true;
//    }
//}
