package com.huangj.mireyijianpro.detail;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.huangj.mireyijianpro.R;
import com.huangj.mireyijianpro.base.BaseActivity;
import com.huangj.mireyijianpro.common.Constant;
import com.huangj.mireyijianpro.home.model.GankModel;

/**
 * Created by huangasys on 2017/11/25.16:57
 * WebView - 详情页;
 */

public class DetailActivity extends BaseActivity {

    private View mView;
    private WebView mWebView;

    @Override
    protected void initOptions() {
        mWebView = findViewById(R.id.webView);
        Intent intent = getIntent();
        GankModel.ResultsBean bean = (GankModel.ResultsBean) intent.getSerializableExtra(Constant.ResultsBean);
        String beanUrl = bean.getUrl();
        startLoading();
        if (StringUtils.isEmpty(beanUrl)) {
            ToastUtils.showShort("网络地址加载有误，请稍后再试");
            stopLoading();
            return;
        }
        initWebView();
        mWebView.loadUrl(beanUrl);
    }

    /**
     * 初始化WebView;
     */
    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        //支持js脚本
        webSettings.setJavaScriptEnabled(true);
        //支持缩放
        webSettings.setSupportZoom(true);
        //支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //多窗口
        webSettings.supportMultipleWindows();
        //当webview调用requestFocus时为webview设置节点
        webSettings.setNeedInitialFocus(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //优先使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 开启H5(APPCache)缓存功能
        webSettings.setAppCacheEnabled(true);
        // 开启 DOM storage 功能
        webSettings.setDomStorageEnabled(true);
        // 应用可以有数据库
        webSettings.setDatabaseEnabled(true);
        // 可以读取文件缓存(manifest生效)
        webSettings.setAllowFileAccess(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //网页加载完成
                    stopLoading();
                }
            }
        });
    }

    @Override
    protected View initLayout() {
        mView = getLayoutInflater().inflate(R.layout.activity_detail, null, false);
        return mView;
    }

    @Override
    protected String initToolbarTitle() {
        return "详情查看";
    }

    @Override
    protected void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_download).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                ToastUtils.showShort("分享");
                break;
            case R.id.action_save:
                ToastUtils.showShort("收藏");
                break;
        }
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.destroy();
    }
}
