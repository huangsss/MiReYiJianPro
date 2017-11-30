package com.huangj.mireyijianpro.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.huangj.mireyijianpro.R;
import com.huangj.mireyijianpro.common.Constant;
import com.huangj.mireyijianpro.home.model.GankModel;
import com.huangj.mireyijianpro.net.Api;
import com.huangj.mireyijianpro.net.HttpManager;
import com.huangj.mireyijianpro.widget.EmptyRecyclerView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.NetworkUtils.isConnected;

/**
 * Created by huangasys on 2017/11/25.14:11
 */

public abstract class BaseFragment extends Fragment implements BaseView.isView {

    protected Activity mContext;

    private View mView;
    protected EmptyRecyclerView mRecyclerView;
    private Disposable mDisposable;//RxJava取消订阅;
    protected List<GankModel.ResultsBean> mList = new ArrayList<>();
    //空布局对象
    protected android.view.View mEmptyView;
    protected BaseAdapter mBaseAdapter;
    //刷新布局
    private SwipeRefreshLayout mRefreshLayout;
    //加载中动画
    private AVLoadingIndicatorView mAvi;
    //加载更多动画
    private AVLoadingIndicatorView mAviLoadMore;
    //加载更多布局
    private LinearLayout mLayoutLoadMore;
    /**
     * 是否可以加载更多
     */
    protected boolean mIsLoadMore = true;
    protected int mPage = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home, container, false);
        }
        return mView;
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);//初始化布局;
        initItemListener();
        initOptions(view);
        if (isConnected()) {
            //是否联网;
            showLoading();
            getData(Constant.GET_DATA_TYPE_NOMAL);
        }
    }

    /**
     * 实现各自的业务操作，由子类实现
     */
    protected void initOptions(View view) {

    }


    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.fragment_recyclerview);
        mEmptyView = view.findViewById(R.id.empty_view);
        mRefreshLayout = view.findViewById(R.id.refreshlayout);
        mAvi = view.findViewById(R.id.loading);
        mAviLoadMore = view.findViewById(R.id.avi_loadmore);
        mLayoutLoadMore = view.findViewById(R.id.layout_loadmore);
        //设置下拉刷新样式颜色
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        //下拉刷新;
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                mIsLoadMore = true;
                getData(Constant.GET_DATA_TYPE_NOMAL);
            }
        });
        //设置布局类型
        mRecyclerView.setLayoutManager(initLayoutManager());

        mBaseAdapter = new BaseAdapter(mList, mContext,initItemType());
        mRecyclerView.setAdapter(mBaseAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.hideEmptyView();
        //上拉加载监听;
        mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener());
    }

    /**
     * 初始化列表的LayoutManager，默认提供线性LinearLayoutManager
     * 如果有其他布局，由子类实现
     *
     */
    protected RecyclerView.LayoutManager initLayoutManager() {
        return new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
    }

    /**
     * 初始化类型,传入适配器;
     */
    protected int initItemType() {
        return Constant.ITEM_TYPE_TEXT;
    }

    /**
     * 从服务器获取数据;
     *
     * @param type 0 - 下拉刷新+首次进入 1 - 加载更多;
     */
    private void getData(final int type) {

        Api categoryService = HttpManager.getHttpManager().getCategoryService();
        categoryService.getCategoryData(getCategoryText(), Constant.PAGE_SIZE, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GankModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(GankModel gankModel) {
                        if (gankModel.isError()) {
                            ToastUtils.showShort("服务器异常,请稍后再试");
                            return;
                        }
                        if (type == Constant.GET_DATA_TYPE_NOMAL) {
                            //刷新;
                            mList.clear();
                            mList = gankModel.getResults();
                        } else {
                            //加载更多
                            mList.addAll(gankModel.getResults());
                        }
                        if (gankModel.getResults().size() < Constant.PAGE_SIZE) {
                            //不足一页;
                            mIsLoadMore = false;
                        }
                        mBaseAdapter.setList(mList);
                        mBaseAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopRefresh();
                        hideLoading();
                        stopLoadingMore();
                    }

                    @Override
                    public void onComplete() {
                        stopRefresh();
                        hideLoading();
                        stopLoadingMore();
                    }
                });

    }

    /**
     *
     * @return 自定义页面返回的数据;
     */
    protected abstract String getCategoryText();

    /**
     * 自定义item点击事件;
     */
    protected void initItemListener() {

    }

    @Override
    public void showLoading() {
        mAvi.smoothToShow();
    }

    @Override
    public void hideLoading() {
        if (mAvi.isShown()) {
            mAvi.smoothToHide();
        }
    }

    @Override
    public void stopRefresh() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void startLoadingMore() {
        mLayoutLoadMore.setVisibility(View.VISIBLE);
        mAviLoadMore.smoothToShow();
    }

    @Override
    public void stopLoadingMore() {
        mLayoutLoadMore.setVisibility(View.GONE);
        if (mAviLoadMore.isShown()) {
            mAviLoadMore.smoothToHide();
        }
    }

    /**
     * 滑动监听;
     */
    class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            int lastPosition = -1;
            //当前状态停止
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                //判断当前列表是否滑动到底部;
                if (!mRecyclerView.canScrollVertically(1)) {
                    //滑动到底部,需触发上拉加载;
                    mRecyclerView.smoothScrollToPosition(lastPosition);
                    if (!mIsLoadMore) {
                        ToastUtils.showShort("无更多数据");
                        return;
                    }
                    startLoadingMore();
                    mPage++;
                    getData(Constant.GET_DATA_TYPE_LOADMORE);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mDisposable.isDisposed()) {
            Log.d("print", "onDestroy: 未取消订阅则取消订阅");
            mDisposable.dispose();
        }
    }
}
