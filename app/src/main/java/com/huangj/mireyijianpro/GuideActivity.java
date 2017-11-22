package com.huangj.mireyijianpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangj.mireyijianpro.main.MainActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 欢迎界面
 *
 * @date 2017
 */
public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    TextView mTvTime;
    int max = 5;
    ImageView mImageView;
    private Disposable mSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mTvTime = (TextView) findViewById(R.id.tv_guide_time);
        mImageView = findViewById(R.id.iv_guide_skip);
        mImageView.setOnClickListener(this);
        mTvTime.setText(max + " s");
        isRxJavaJump();
    }

    private void isRxJavaJump() {
        mSubscribe = Observable.timer(1, TimeUnit.SECONDS)
                .repeat(5)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        max--;
                        mTvTime.setText(max + " s");
                        Log.d("print", "accept:---- " +aLong);
                        if (max == 0) {
                            startActivity(new Intent(GuideActivity.this, MainActivity.class));
                            mSubscribe.dispose();
                            finish();
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        startActivity(new Intent(GuideActivity.this, MainActivity.class));
        mSubscribe.dispose();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
