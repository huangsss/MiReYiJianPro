package com.huangj.mireyijianpro.girl;

import android.graphics.Bitmap;
import android.renderscript.RSRuntimeException;
import android.support.v4.view.ViewPager;
import android.test.mock.MockContext;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.huangj.mireyijianpro.R;
import com.huangj.mireyijianpro.base.BaseActivity;
import com.huangj.mireyijianpro.common.Constant;
import com.huangj.mireyijianpro.home.model.GankModel;
import com.huangj.mireyijianpro.image.ImageManager;
import com.huangj.mireyijianpro.net.Api;
import com.huangj.mireyijianpro.net.HttpManager;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huangasys on 2017/11/30.11:05
 */

public class ShowPicActivity extends BaseActivity implements MenuItem.OnMenuItemClickListener {

    private List<String> mListPics;
    private int mPosition;
    private int mPage;
    private PicAdapter mPicAdapter;
    private ViewPager mViewPager;
    private Disposable mDisposable;

    @Override
    protected void initOptions() {
        mViewPager = findViewById(R.id.pic_viewpager);
        //获取传递的参数
        mListPics = getIntent().getStringArrayListExtra("picList");
        mPosition = getIntent().getIntExtra("position", 0);
        mPage = getIntent().getIntExtra("page", 0);
        //给viewpager设置适配器
        mPicAdapter = new PicAdapter(this, mListPics);
        mViewPager.setAdapter(mPicAdapter);
        //设置点击项;
        mViewPager.setCurrentItem(mPosition);
        // 滑动至最后一个页面时,自动加载更多.
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mListPics.size() - 1) {
                    //滑动至最后一页,加载更多数据
                    mPage++;
                    loadMoreData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //下载更多数据;
    private void loadMoreData() {
        Api api = HttpManager.getHttpManager().getCategoryService();
        api.getCategoryData(Constant.CATEGORY_GIRL, Constant.PAGE_SIZE, mPage)
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
                            ToastUtils.showShort("服务端异常，请稍后再试");
                            return;
                        }
                        List<GankModel.ResultsBean> results = gankModel.getResults();
                        for (int i = 0; i < results.size(); i++) {
                            mListPics.add(results.get(i).getUrl());
                        }
                        mPicAdapter.setList(mListPics);
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
    protected View initLayout() {
        return getLayoutInflater().inflate(R.layout.activity_show_pic, null, false);
    }

    @Override
    protected String initToolbarTitle() {
        return "图片详情";
    }

    @Override
    protected void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_save).setVisible(false);
        menu.findItem(R.id.action_download).setOnMenuItemClickListener(this);
        menu.findItem(R.id.action_share).setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
//                startShareIntent("text/plain", "分享福利啦：" + mListPics.get(mViewPager.getCurrentItem()));
                ToastUtils.showShort("福利分享");
                break;
            case R.id.action_download:
                downLoadImg();
                break;
        }
        return true;
    }

    /**
     * 下载图片;
     */
    private void downLoadImg() {
        if (!SDCardUtils.isSDCardEnable()) {
            ToastUtils.showShort("当前SD卡不存在!");
            return;
        }
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                Bitmap bitmap = ImageManager.getInstance().loadImage(ShowPicActivity.this, mListPics.get(mViewPager.getCurrentItem()));
                e.onNext(bitmap);
            }
        }).subscribeOn(Schedulers.io())
                .map(new Function<Bitmap, Boolean>() {
                    @Override
                    public Boolean apply(Bitmap bitmap) throws Exception {
                        //保存本地是否成功;
                        return saveImg(bitmap);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ToastUtils.showShort("保存成功");
                        } else {
                            ToastUtils.showShort("保存失败");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showShort("保存出错");
                        Log.d("print", "accept: " + throwable);
                    }
                });
    }

    /**
     * 保存bitmap为本地图片
     *
     * @param bitmap
     * @return
     */
    public boolean saveImg(Bitmap bitmap) {
        //保存路径
        String directoryPath = SDCardUtils.getSDCardPath() + "meiriyijian";
        FileUtils.createOrExistsDir(directoryPath);
        String temp = mListPics.get(mViewPager.getCurrentItem());
        String fileName = temp.substring(temp.lastIndexOf("/"));
        String savePath = directoryPath + File.separator + fileName;
        return ImageUtils.save(bitmap, savePath, Bitmap.CompressFormat.JPEG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("print", "onDestroy: " +mDisposable);
        /*if (mDisposable.isDisposed()) {
            mDisposable.dispose();
        }*/
    }
}
