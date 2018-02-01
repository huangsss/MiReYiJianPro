package com.huangj.mireyijianpro.girl;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huangj.mireyijianpro.image.ImageManager;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by huangasys on 2017/11/30.11:26
 */

public class PicAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mList;//图片地址集合

    public PicAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //        ImageView imageView  = new ImageView(mCotext);
        PhotoView imageView  = new PhotoView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //加载显示图片
        ImageManager.getInstance()
                .loadImage(mContext,mList.get(position),imageView);
        container.addView(imageView);
        return imageView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setList(List<String> mList) {
        this.mList = mList;
        this.notifyDataSetChanged();
    }
}
