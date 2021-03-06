package com.huangj.mireyijianpro.base;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.huangj.mireyijianpro.R;
import com.huangj.mireyijianpro.common.Constant;
import com.huangj.mireyijianpro.common.Utils;
import com.huangj.mireyijianpro.home.model.GankModel;
import com.huangj.mireyijianpro.image.ImageManager;

import java.util.List;

/**
 * Created by huangasys on 2017/11/22.15:19
 */

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {

    private List<GankModel.ResultsBean> mList;
    private Context mContext;
    private AdapterOnClick mAdapterOnClick;
    private int mItemType;//条目类型

    public interface AdapterOnClick {
        void setImageOnClick(GankModel.ResultsBean bean, int position);

        void setItemOnClick(GankModel.ResultsBean bean, int position);
    }

    public BaseAdapter(List<GankModel.ResultsBean> list, Context context, int mItemType) {
        this.mList = list;
        this.mContext = context;
        this.mItemType = mItemType;
    }

    public void setList(List<GankModel.ResultsBean> list) {
        mList = list;
    }

    public void setAdapterOnClick(AdapterOnClick adapterOnClick) {
        mAdapterOnClick = adapterOnClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView;
        if (mItemType == Constant.ITEM_TYPE_TEXT) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.item_homefragment, parent, false);
        } else {
            mView = LayoutInflater.from(mContext).inflate(R.layout.item_homefragment_girl, parent, false);
        }
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GankModel.ResultsBean resultsBean = mList.get(position);
        if (mItemType == Constant.ITEM_TYPE_TEXT) {
            holder.title.setText(resultsBean.getDesc());
            holder.author.setText(resultsBean.getWho());
            holder.time.setText(TimeUtils.getFriendlyTimeSpanByNow(Utils.formatDateFromStr(resultsBean.getPublishedAt())));
            //存在图片则展示,不存在则隐藏;
            if (resultsBean.getImages() != null && resultsBean.getImages().size() > 0) {
                holder.mImageView.setVisibility(View.VISIBLE);
                ImageManager.getInstance().loadImage(mContext, resultsBean.getImages().get(0), holder.mImageView);
            } else {
                holder.mImageView.setVisibility(View.GONE);
            }
            //图片点击事件;
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (resultsBean.getImages() != null && resultsBean.getImages().size() > 0 && mAdapterOnClick != null) {
                        mAdapterOnClick.setImageOnClick(resultsBean, position);
                    } else {
                        ToastUtils.showShort("未发现图片");
                    }
                }
            });
        }else if (mItemType == Constant.ITEM_TYPE_IMAGE){
            ImageManager.getInstance().loadImage(mContext,
                    resultsBean.getUrl(),
                    holder.imageViewGirl);
        }


        //整个条目点击事件;
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapterOnClick != null) {
                    mAdapterOnClick.setItemOnClick(resultsBean, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //整体布局
        View rootView;
        TextView title, author, time;
        ImageView mImageView;
        //福利模式下的图片控件
        ImageView imageViewGirl;
        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            title = itemView.findViewById(R.id.tv_title);
            author = itemView.findViewById(R.id.tv_author);
            time = itemView.findViewById(R.id.tv_time);
            mImageView = itemView.findViewById(R.id.iv_cover);
            imageViewGirl = (ImageView) itemView.findViewById(R.id.iv_girl);
        }
    }
}
