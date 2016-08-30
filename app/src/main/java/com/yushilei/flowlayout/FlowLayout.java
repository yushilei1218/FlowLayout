package com.yushilei.flowlayout;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by  yushilei.
 * @time 2016/8/30 -14:27.
 * @Desc
 */
public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    List<List<View>> allViews = new ArrayList<>();
    List<Integer> lineHeights = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout");
        allViews.clear();
        lineHeights.clear();

        int width = getWidth();
        int childCount = getChildCount();

        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (childWidth + mlp.rightMargin + mlp.leftMargin + lineWidth > width) {//换行
                lineHeights.add(lineHeight);
                lineWidth = 0;
                allViews.add(lineViews);
                lineViews = new ArrayList<View>();
            }

            lineHeight = Math.max(lineHeight, childHeight + mlp.topMargin + mlp.bottomMargin);
            lineWidth += childWidth + mlp.leftMargin + mlp.rightMargin;
            lineViews.add(child);
        }
        lineHeights.add(lineHeight);
        allViews.add(lineViews);

        int left = 0;
        int top = 0;
        int lineNums = allViews.size();
        for (int i = 0; i < lineNums; i++) {
            List<View> sLineViews = allViews.get(i);
            Integer height = lineHeights.get(i);
            for (int j = 0; j < sLineViews.size(); j++) {
                View child = sLineViews.get(j);
                if (child.getVisibility() == GONE) {
                    continue;
                }
                MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
                int measuredWidth = child.getMeasuredWidth();
                int measuredHeight = child.getMeasuredHeight();

                int childLeft = left + mlp.leftMargin;
                int childTop = top + mlp.topMargin;
                int childRight = childLeft + measuredWidth;
                int childBotton = childTop + measuredHeight;
                child.layout(childLeft, childTop, childRight, childBotton);

                left += mlp.rightMargin + measuredWidth + mlp.leftMargin;

            }
            left = 0;
            top += height;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int childCount = getChildCount();

        int width = 0;
        int height = 0;

        int lineWidth = 0;//记录每行的宽度
        int lineHeight = 0;//记录每行的高度

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams mLp = (MarginLayoutParams) child.getLayoutParams();
            //子View实际占用的宽高
            int childWith = child.getMeasuredWidth() + mLp.rightMargin + mLp.leftMargin;
            int childHeight = child.getMeasuredHeight() + mLp.topMargin + mLp.bottomMargin;

            if (childWith + lineWidth > widthSize) {//View 宽度
                width = Math.max(lineWidth, childWith);
                height += lineHeight;
                lineWidth = childWith;
            } else {
                lineHeight = Math.max(lineHeight, childHeight);
                lineWidth += childWith;
            }
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY ? widthSize : width)
                , (heightMode == MeasureSpec.EXACTLY ? heightSize : height));

    }

    String TAG = "FlowLayout";

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        Log.d(TAG, "generateLayoutParams");
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onFinishInflate() {
        Log.d(TAG, "onFinishInflate");
        super.onFinishInflate();
    }
}
