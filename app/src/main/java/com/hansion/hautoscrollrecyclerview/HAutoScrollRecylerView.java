package com.hansion.hautoscrollrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Description：
 * Author: Hansion
 * Time: 2017/7/3 10:48
 */
public class HAutoScrollRecylerView extends RecyclerView {

    private Context mContext;

    //动画插值器
    public static final int LINEAR_INTERPOLATOR = 0;
    public static final int ACCELERATE_INTERPOLATOR = 1;
    public static final int DECELERATE_INTERPOLATOR = 2;
    public static final int ACCELERATEDECELERATE_INTERPOLATOR = 3;
    private Interpolator mInterpolator;

    private Scroller mScroller = null;

    //滚动速度
    private float mScrollSpeed = 0.5f;
    private int mLastx = 0;


    public HAutoScrollRecylerView(Context context) {
        this(context, null);
    }

    public HAutoScrollRecylerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HAutoScrollRecylerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HAutoScrollRecylerView);
            setItemSpace(typedArray.getInt(R.styleable.HAutoScrollRecylerView_itemSpace,0));
            setScrollSpeed(typedArray.getFloat(R.styleable.HAutoScrollRecylerView_speed, 0.5f));
            setInterpolator(typedArray.getInt(R.styleable.HAutoScrollRecylerView_interpolator, 0));
            typedArray.recycle();
        }
        initData();
    }

    /**
     * 设置条目间距
     * @param px 条目间距 单位px
     */
    private void setItemSpace(int px) {
        addItemDecoration(new SpacesItemDecoration(px));
    }

    /**
     * 设置滚动速度
     *
     * @param scrollSpeed
     */
    public void setScrollSpeed(float scrollSpeed) {
        mScrollSpeed = scrollSpeed;
    }

    /***
     * 设置动画插值器
     *
     * @param interpolator LINEAR_INTERPOLATOR      以常量速率改变
     *                     ACCELERATE_INTERPOLATOR     在动画开始的地方速率改变比较慢，然后开始加速
     *                     DECELERATE_INTERPOLATOR     在动画开始的地方快然后慢
     *                     ACCELERATEDECELERATE_INTERPOLATOR       在动画开始与结束的地方速率改变比较慢，在中间的时候加速
     */
    public void setInterpolator(int interpolator) {
        switch (interpolator) {
            case ACCELERATE_INTERPOLATOR:
                mInterpolator = new AccelerateInterpolator();
                break;
            case DECELERATE_INTERPOLATOR:
                mInterpolator = new DecelerateInterpolator();
                break;
            case ACCELERATEDECELERATE_INTERPOLATOR:
                mInterpolator = new AccelerateDecelerateInterpolator();
                break;
            case LINEAR_INTERPOLATOR:
            default:
                mInterpolator = new LinearInterpolator();
                break;
        }
    }

    private void initData() {
        if (mInterpolator == null) {
            mInterpolator = new LinearInterpolator();
        }
        mScroller = new Scroller(mContext, mInterpolator);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        setLayoutManager(linearLayoutManager);

        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller != null) {
            if (mScroller.computeScrollOffset()) {
                scrollBy(mLastx - mScroller.getCurrX(), 0);
                mLastx = mScroller.getCurrX();
                postInvalidate();
            }
        }
    }

    public void autoScroll(int position) {
        //获取第一个和最后一个可见的条目的索引
        int firstvisiableposition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        int lastvisiableposition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        //获取当前可见条目数量
        int visiableItemNum = getChildCount();
        //点击的条目在可见条目中的索引
        int clickPositionInVisiableItems = position - firstvisiableposition;
        //获取被点击的item
        View clickItem = getChildAt(clickPositionInVisiableItems);
        if(clickItem != null) {
            //移动起始X坐标
            int startX = clickItem.getLeft();
            //RecyclerView的中心点x坐标
            int centerX = (getRight() - getLeft()) / 2;
            //item的宽度
            int clickItem_width = clickItem.getWidth();
            //移动距离
            int deep = centerX - (startX + clickItem_width/2);

            //防止左右边界过度滑动
            if(lastvisiableposition == (getLayoutManager()).getItemCount() - 1) {
                if(deep < 0) {
                    deep = getRight() - getChildAt(visiableItemNum - 1).getRight();
                }
            } else if(firstvisiableposition == 0) {
                if(deep > 0) {
                    deep = getLeft() - getChildAt(0).getLeft();
                }
            }
            autoScroll(startX, deep);
        }
    }

    private void autoScroll(int start, int deep) {
        int duration = 0;
        if (mScrollSpeed != 0) {
            duration = (int) ((Math.abs(deep) / mScrollSpeed));
        }
        mLastx = start;
        mScroller.startScroll(start, 0, deep, 0, duration);
        postInvalidate();
    }


    /**
     * 用于设定条目间距
     * 仅适用于横向RecyclerView
     */
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;
        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            super.getItemOffsets(outRect, itemPosition, parent);
            outRect.top = 0;
            outRect.bottom = 0;
            if(itemPosition != 0) {
                outRect.left = space;
            } else {
                outRect.left = 0;
            }
        }
    }
}
