package com.huangj.mireyijianpro.home;

import android.content.Intent;

import com.blankj.utilcode.util.ToastUtils;
import com.huangj.mireyijianpro.base.BaseAdapter;
import com.huangj.mireyijianpro.base.BaseFragment;
import com.huangj.mireyijianpro.common.Constant;
import com.huangj.mireyijianpro.detail.DetailActivity;
import com.huangj.mireyijianpro.home.model.GankModel;

public  class HomeFragment extends BaseFragment {


    @Override
    protected String getCategoryText() {
        return Constant.CATEGORY_Android;
    }

    @Override
    protected void initItemListener() {
        mBaseAdapter.setAdapterOnClick(new BaseAdapter.AdapterOnClick() {
            @Override
            public void setImageOnClick(GankModel.ResultsBean bean, int position) {
                ToastUtils.showShort("点击图片");
            }

            @Override
            public void setItemOnClick(GankModel.ResultsBean bean, int position) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(Constant.ResultsBean,bean);
                startActivity(intent);
            }
        });
    }
}
