package com.huangj.mireyijianpro.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.huangj.mireyijianpro.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by huangasys on 2017/11/25.14:11
 */

public abstract class BaseActivity extends AppCompatActivity {

    private LinearLayout mLinearLayout;
    private Toolbar mToolbar;
    private AVLoadingIndicatorView mLoadingIndicatorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mLinearLayout = findViewById(R.id.root_layout);
        mToolbar = findViewById(R.id.toolbar);
        mLoadingIndicatorView = findViewById(R.id.avi_loading);
        mLinearLayout.addView(initLayout());

        initOptions();//自定义子类操作;
        initBaseToolBar();//初始化toolbar
    }


    private void initBaseToolBar() {
        mToolbar.setTitle(initToolbarTitle());//设置显示的文字;
        setSupportActionBar(mToolbar);
        //设置返回键可用;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base_toolbar, menu);
        updateOptionsMenu(menu);
        return true;
    }

    /**
     * 子类自定义完成
     *
     * @param menu
     */
    protected void updateOptionsMenu(Menu menu) {

    }

    /**
     * 具体的业务逻辑，由子类实现
     */
    protected abstract void initOptions();

    /**
     * @return 子类自定义视图;
     */
    protected abstract View initLayout();

    /**
     * @return 显示的标题
     */
    protected abstract String initToolbarTitle();

    /**
     * 开始loading
     */
    public void startLoading() {
        mLoadingIndicatorView.smoothToShow();
    }

    /**
     * 停止Loading
     */
    public void stopLoading() {
        if (mLoadingIndicatorView.isShown()) {
            mLoadingIndicatorView.smoothToHide();
        }
    }
}


