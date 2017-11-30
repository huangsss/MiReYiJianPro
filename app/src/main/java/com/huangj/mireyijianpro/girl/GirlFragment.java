package com.huangj.mireyijianpro.girl;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.huangj.mireyijianpro.R;
import com.huangj.mireyijianpro.base.BaseAdapter;
import com.huangj.mireyijianpro.base.BaseFragment;
import com.huangj.mireyijianpro.common.Constant;
import com.huangj.mireyijianpro.home.model.GankModel;

import java.util.ArrayList;

/**
 * Created by huangasys on 2017/11/29.16:11
 */

public class GirlFragment extends BaseFragment implements View.OnClickListener {


    private FloatingActionButton mItemLinearlayout;
    private FloatingActionButton mItemGridlayout;
    private FloatingActionButton mItemStaggeredlayout;
    private FloatingActionMenu mActionMenu;

    private int mCurrentType = 2;
    private final int TYPE_LINEARLAYOUT = 1;
    private final int TYPE_GRIDLAYOUT = 2;
    private final int TYPE_STAGGEREDLAYOUT = 3;
    //自定义操作;
    @Override
    protected void initOptions(View view) {
        super.initOptions(view);
        mItemLinearlayout = view.findViewById(R.id.menu_item_linearlayout);
        mItemGridlayout = view.findViewById(R.id.menu_item_gridlayout);
        mItemStaggeredlayout =  view.findViewById(R.id.menu_item_staggeredlayout);
        mActionMenu =  view.findViewById(R.id.actionmenu);

        mActionMenu.setVisibility(View.VISIBLE);
        mItemLinearlayout.setOnClickListener(this);
        mItemGridlayout.setOnClickListener(this);
        mItemStaggeredlayout.setOnClickListener(this);
    }

    //自定义适配器模式;
    @Override
    protected int initItemType() {
        return Constant.ITEM_TYPE_IMAGE;//图片模式;

    }
    // 自定义RecyclerView返回类型;
    @Override
    protected RecyclerView.LayoutManager initLayoutManager() {
        return new GridLayoutManager(mContext,2,GridLayoutManager.VERTICAL,false);
    }

    @Override
    protected String getCategoryText() {
        return "福利";
    }

    @Override
    protected void initItemListener() {
        //点击事件;
        mBaseAdapter.setAdapterOnClick(new BaseAdapter.AdapterOnClick() {

            @Override
            public void setImageOnClick(GankModel.ResultsBean bean, int position) {

            }

            @Override
            public void setItemOnClick(GankModel.ResultsBean bean, int position) {
                //跳转大图Activity;
                Intent intent = new Intent(mContext,ShowPicActivity.class);
                //传入参数，图片URL地址
                ArrayList<String> listPics = new ArrayList<>();
                for (int i = 0; i < mList.size(); i++) {
                    listPics.add(mList.get(i).getUrl());
                }
                intent.putStringArrayListExtra("picList",listPics);//图片集合;
                intent.putExtra("position",position);//所点击项;
                intent.putExtra("page",mPage);//点击的第几页;
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_item_linearlayout:
                if (mCurrentType == TYPE_LINEARLAYOUT) {
                    return;
                }
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                mActionMenu.close(true);
                mCurrentType = TYPE_LINEARLAYOUT;
                break;
            case R.id.menu_item_gridlayout:
                if (mCurrentType == TYPE_GRIDLAYOUT) {
                    return;
                }
                mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
                mActionMenu.close(true);
                mCurrentType = TYPE_GRIDLAYOUT;
                break;
            case R.id.menu_item_staggeredlayout:
                break;
        }
    }

}
