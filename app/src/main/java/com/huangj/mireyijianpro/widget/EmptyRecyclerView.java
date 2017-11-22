package com.huangj.mireyijianpro.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by huangasys on 2017/11/22.14:02
 * 设置空布局的RecycleView;
 */

public class EmptyRecyclerView extends RecyclerView {

    private View mEmptyView;
    private Context mContext;
    public EmptyRecyclerView(Context context) {
        super(context);
        this.mContext = context;
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    /**
     * 根据数据源判断是否为空;
     */
    private void checkIfEmpty() {
        if (mEmptyView != null && getAdapter() != null) {
            mEmptyView.setVisibility(getAdapter().getItemCount() > 0 ? VISIBLE : GONE);
        }
    }

    public void setEmptyView(View mEmptyView) {
        this.mEmptyView = mEmptyView;
        checkIfEmpty();
    }

    public void hideEmptyView() {
        if (mEmptyView.getVisibility() == VISIBLE) {
            mEmptyView.setVisibility(GONE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter adapterOld = getAdapter();
        if (adapterOld != null) {
            adapterOld.unregisterAdapterDataObserver(mObserver);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapterOld.registerAdapterDataObserver(mObserver);
        }
    }

    AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkIfEmpty();
        }
    };
}
