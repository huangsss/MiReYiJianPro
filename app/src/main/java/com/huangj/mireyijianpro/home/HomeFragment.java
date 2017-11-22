package com.huangj.mireyijianpro.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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


public class HomeFragment extends Fragment implements HomeContract.View {


    protected Activity mContext;
    /**
     * 整个布局
     */
    private View mView;
    protected EmptyRecyclerView mRecyclerView;
    private Disposable mDisposable;//RxJava取消订阅;
    protected List<GankModel.ResultsBean> mList = new ArrayList<>();
    //空布局对象
    protected View mEmptyView;
    protected HomeAdapter mHomeAdapter;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home, container, false);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);//初始化布局;

        if (isConnected()) {
            //是否联网;
            showLoading();
            getData(Constant.GET_DATA_TYPE_NOMAL);
        }
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

    }

    /**
     * 从服务器获取数据;
     *
     * @param type 0 - 下拉刷新+首次进入 1 - 加载更多;
     */
    private void getData(final int type) {

        Api categoryService = HttpManager.getHttpManager().getCategoryService();
        categoryService.getCategoryData(Constant.CATEGORY_Android, Constant.PAGE_SIZE, mPage)
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

                        } else {
                            //加载更多
                            
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

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

    }

    @Override
    public void startLoadingMore() {

    }

    @Override
    public void stopLoadingMore() {

    }


}
