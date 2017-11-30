package com.huangj.mireyijianpro.girl;

import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;

import com.huangj.mireyijianpro.R;
import com.huangj.mireyijianpro.base.BaseActivity;

/**
 * Created by huangasys on 2017/11/29.15:54
 */

public class GirlActivity extends BaseActivity {

    private View mView;

    @Override
    protected void initOptions() {
        GirlFragment girlFragment = new GirlFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content,girlFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected View initLayout() {
        mView = getLayoutInflater().inflate(R.layout.activity_other_category, null, false);
        return mView;
    }

    @Override
    protected String initToolbarTitle() {
        return "福利";
    }

    @Override
    protected void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_share).setVisible(false);
        menu.findItem(R.id.action_download).setVisible(false);
        menu.findItem(R.id.action_save).setVisible(false);
    }
}
