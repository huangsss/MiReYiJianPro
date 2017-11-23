package com.huangj.mireyijianpro.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.huangj.mireyijianpro.R;
import com.huangj.mireyijianpro.common.Constant;
import com.huangj.mireyijianpro.home.HomeFragment;
import com.huangj.mireyijianpro.home.model.DrawModel;
import com.huangj.mireyijianpro.me.MeFragment;
import com.huangj.mireyijianpro.read.ReadFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面;
 */
public class MainActivity extends AppCompatActivity {
    private final int NAVIGATION_HOME = 0;
    private final int NAVIGATION_READ = 1;
    private final int NAVIGATION_ME = 2;
    private ViewPager mVpMain;//主界面ViewPager
    private List<Fragment> mList;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;//侧滑菜单
    private BottomNavigationView mBottomNavigation;//底部导航
    private Menu mMenu;
    private RecyclerView mRvDrawList;
    private List<DrawModel> mListDrawModel;
    private MainDrawListAdapter mDrawListAdapter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mVpMain.setCurrentItem(NAVIGATION_HOME);
                    return true;
                case R.id.navigation_read:
                    mVpMain.setCurrentItem(NAVIGATION_READ);
                    return true;
                case R.id.navigation_me:
                    mVpMain.setCurrentItem(NAVIGATION_ME);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initView();
        initFragment();
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager(), getApplicationContext(), mList);
        mVpMain.setAdapter(adapter);
        mRvDrawList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mDrawListAdapter = new MainDrawListAdapter(this, initData());
        mRvDrawList.setAdapter(mDrawListAdapter);
        initListener();
    }

    private List<DrawModel> initData() {
        mListDrawModel = new ArrayList<>();
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_ios, Constant.CATEGORY_IOS));
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_girl, Constant.CATEGORY_GIRL));
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_client, Constant.CATEGORY_CLIENT));
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_recommend, Constant.CATEGROY_RECOMMEND));
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_app, Constant.CATEGORY_APP));
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_resource, Constant.CATEGORY_EXPANDRESOURCE));
        mListDrawModel.add(new DrawModel(R.drawable.drawer_icon_video, Constant.CATEGORY_VIDEO));
        return mListDrawModel;
    }

    /**
     * 初始化Toolbar信息
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mBottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mVpMain = (ViewPager) findViewById(R.id.vp_main);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mDrawer = (DrawerLayout) findViewById(R.id.drawlayout);
        mRvDrawList = (RecyclerView) findViewById(R.id.recyclerview_drawerlist);
        mDrawer.setScrimColor(Color.TRANSPARENT);//透明;
        mDrawer.setDrawerElevation(0);//默认选中Home;
    }

    /**
     * 初始化Fragment对象
     */
    private void initFragment() {
        mList = new ArrayList<>();
        mList.add(new HomeFragment());
        mList.add(new ReadFragment());
        mList.add(new MeFragment());
    }

    private void initListener() {
        mVpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case NAVIGATION_HOME:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_home);
                        break;
                    case NAVIGATION_READ:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_read);
                        break;
                    case NAVIGATION_ME:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_me);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // 得到contentView
                View content = mDrawer.getChildAt(0);
                int offset = (int) (drawerView.getWidth() * slideOffset);
                content.setTranslationX(offset);
                //content.setScaleX(1 - slideOffset * 0.2f);
                //content.setScaleY(1 - slideOffset * 0.2f);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mDrawListAdapter.setmOnMainDrawClickListener(new MainDrawListAdapter.OnMainDrawClickListener() {
            @Override
            public void onClick(int position) {
                String category = Constant.CATEGORY_ALL;
                switch (mListDrawModel.get(position).getTitle()) {
                    case "iOS":
                        category = Constant.CATEGORY_IOS;
                        showCategoryInfo(category);
                        break;
                    case "前端":
                        category = Constant.CATEGORY_CLIENT;
                        showCategoryInfo(category);
                        break;
                    case "瞎推荐":
                        category = Constant.CATEGROY_RECOMMEND;
                        showCategoryInfo(category);
                        break;
                    case "App":
                        category = Constant.CATEGORY_APP;
                        showCategoryInfo(category);
                        break;
                    case "拓展资源":
                        category = Constant.CATEGORY_EXPANDRESOURCE;
                        showCategoryInfo(category);
                        break;
                    case "休息视频":
                        category = Constant.CATEGORY_VIDEO;
                        showCategoryInfo(category);
                        break;
                    case "福利":
                        category = Constant.CATEGORY_GIRL;
                        showCategoryInfo(category);
                        break;
                }
                mDrawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
        return true;
    }

    /**
     * 跳转到分类展示界面
     *
     * @param categroy
     */
    private void showCategoryInfo(String categroy) {
        Toast.makeText(this, "跳转分类：" + categroy, Toast.LENGTH_SHORT).show();
    }

    private class MainAdapter extends FragmentPagerAdapter {
        private Context mContext;
        private List<Fragment> mList;

        private MainAdapter(FragmentManager fm, Context mContext, List<Fragment> mList) {
            super(fm);
            this.mList = mList;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }


        public void setmList(List<Fragment> mList) {
            this.mList = mList;
        }
    }
}
