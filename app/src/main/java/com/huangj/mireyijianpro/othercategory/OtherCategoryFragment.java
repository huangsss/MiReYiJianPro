package com.huangj.mireyijianpro.othercategory;

import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.huangj.mireyijianpro.base.BaseAdapter;
import com.huangj.mireyijianpro.base.BaseFragment;
import com.huangj.mireyijianpro.common.Constant;
import com.huangj.mireyijianpro.detail.DetailActivity;
import com.huangj.mireyijianpro.home.model.GankModel;

/**
 * Created by huangasys on 2017/11/25.15:57
 */

public class OtherCategoryFragment extends BaseFragment {

    private static final String CATEGORY = "category";

    public static OtherCategoryFragment getInstance(String tag){
        OtherCategoryFragment otherCategoryFragment = new OtherCategoryFragment();
        Bundle bundle  = new Bundle();
        bundle.putString(CATEGORY,tag);
        otherCategoryFragment.setArguments(bundle);
        return otherCategoryFragment;
    }

    @Override
    protected String getCategoryText() {
        return getArguments().getString(CATEGORY);
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
                //跳转到详情界面
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(Constant.ResultsBean,bean);
                startActivity(intent);
            }
        });
    }

}
