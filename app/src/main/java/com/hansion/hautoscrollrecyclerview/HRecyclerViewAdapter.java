package com.hansion.hautoscrollrecyclerview;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Description：
 * Author: Hansion
 * Time: 2017/7/3 16:24
 */
public class HRecyclerViewAdapter extends RecyclerView.Adapter<HRecyclerViewAdapter.ViewHolder> {

    private int[] mDatas;
    private HAutoScrollRecylerView mHAutoScrollRecylerView = null;
    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;

    public HRecyclerViewAdapter(Activity activity, HAutoScrollRecylerView hAutoScrollRecylerView, int[] datas) {
        this.mHAutoScrollRecylerView = hAutoScrollRecylerView;
        mDatas = datas;
        mInflater = LayoutInflater.from(activity);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.h_recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImageView = (ImageView) view.findViewById(R.id.item_iv);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mImageView.setImageResource(mDatas[position]);
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mHAutoScrollRecylerView != null) {
                        mHAutoScrollRecylerView.autoScroll(holder.getAdapterPosition());
                    }
                    mOnItemClickLitener.onItemClick(holder.itemView, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.length;
    }


    interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        ViewHolder(View arg0) {
            super(arg0);
        }
    }
}
