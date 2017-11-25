package com.huangj.mireyijianpro.othercategory;

import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;

import com.huangj.mireyijianpro.R;
import com.huangj.mireyijianpro.base.BaseActivity;

/**
 * Created by huangasys on 2017/11/25.16:05
 */

public class OtherCategoryActivity extends BaseActivity {

    private View mView;
    private String mCategory;
    private OtherCategoryFragment mFragment;
    @Override
    protected void initOptions() {
        mCategory = getIntent().getStringExtra("category");
        mFragment = OtherCategoryFragment.getInstance(mCategory);
        //开启事务提交;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content,mFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected View initLayout() {
        mView = getLayoutInflater().inflate(R.layout.activity_other_category,null,false);
        return mView;
    }

    @Override
    protected String initToolbarTitle() {
        return mCategory;
    }

    @Override
    protected void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_save).setVisible(false);
        menu.findItem(R.id.action_download).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);

    }
}
