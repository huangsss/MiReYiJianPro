package com.huangj.mireyijianpro.base;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by huangasys on 2017/11/22.15:53
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化工具类;
        Utils.init(this);
    }
}
