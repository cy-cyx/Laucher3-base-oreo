package com.lambda.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.lambda.common.utils.CommonUtil;

public class ChildViewPager extends ViewPager {
    public ChildViewPager(@NonNull Context context) {
        super(context);
    }

    public ChildViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private float x1;

    public boolean useFastWQuit = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getChildCount() == 1) return super.dispatchTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //告知父控件 把事件下发给子控件处理
                //  快速退出模式，靠边缘是相应退出的
                x1 = ev.getX();
                if (x1 < CommonUtil.getScreenWidth() - (float) CommonUtil.getScreenWidth() / 6 || !useFastWQuit) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float x2 = ev.getX();

                //拿到当前显示页下标
                int curPosition = getCurrentItem();
                //手指移动时的X坐标

                if (curPosition == 0) {
                    if ((x2 - x1) > 50) {
                        //当当前页面在下标为0的时候，由父亲拦截触摸事件
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } else if (curPosition == (getAdapter().getCount() - 1)) { //当前页面为最后一页时
                    if ((x1 - x2) > 50) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } else {
                    //其他情况，由孩子拦截触摸事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
