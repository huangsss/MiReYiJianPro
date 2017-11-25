package com.huangj.mireyijianpro.base;

/**
 * Created by huangasys on 2017/11/25.14:12
 */

public interface BaseView {

    interface isView {
        //开启加载动画
        void showLoading();

        //关闭加载动画
        void hideLoading();


        //关闭下拉刷新
        void stopRefresh();

        //开启加载更多
        void startLoadingMore();

        //开启加载更多
        void stopLoadingMore();
    }

}
