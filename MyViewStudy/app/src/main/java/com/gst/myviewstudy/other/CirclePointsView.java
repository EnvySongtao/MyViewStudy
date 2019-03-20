package com.gst.myviewstudy.other;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.gst.myviewstudy.R;


/**
 * Created by yu on 2016/3/22.
 */
public class CirclePointsView extends View {
    private int mCount;// 圆钮数量
    private int mSpace;// 圆钮间距
    private int mRadius;// 圆钮半径
    private int mNormalColor;// 圆钮缺省颜色
    private int mFocusColor;// 圆钮焦点颜色
    private int mFocus;// 焦点的索引值

    public CirclePointsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取布局文件中定义的属性值，结果保存在数组中
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CirclePointsView);
        // 获取圆钮数量的属性值，若布局中没有设置，默认为4个
        mCount = array.getInt(R.styleable.CirclePointsView_count, 0);
        // 获取圆钮半径的属性值，若布局中没有设置，默认为8dp
        mRadius = array.getDimensionPixelOffset(
                R.styleable.CirclePointsView_radiu, 8);
        mNormalColor = array.getColor(R.styleable.CirclePointsView_normal_color, Color.parseColor("#808080"));
        mFocusColor = array.getColor(R.styleable.CirclePointsView_focus_color, Color.parseColor("#0485d0"));
        mSpace = array.getDimensionPixelOffset(R.styleable.CirclePointsView_space,
                10);
        array.recycle();// 回收属性数组,以后可重用
    }

    /**
     * 设置焦点索引值，该方法由播放图片的类调用，设置正当前的索引值
     *
     * @param focus 传入的索引值
     */
    public void setFocus(int focus) {
        mFocus = focus;
        invalidate();// 通知绘图onDraw重新绘制一排圆钮
    }

    public void setCount(int count) {
        this.mCount = count;
        invalidate();
    }

    // 为播放图片的类提供图片的数量
    public int getCount() {
        return mCount;
    }

    // 测量view的宽和高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measuredWidth(widthMeasureSpec),
                measuredHeight(heightMeasureSpec));
    }

    /**
     * 测量view的高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measuredHeight(int heightMeasureSpec) {
        int result = 0;// 计算高度的最终结果
        // 获取测量规格
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        // 获取系统计算的高度
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        // 若是精确测量规格
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {// 若是模糊测量规格，即wrap_content值
            result = getTopPaddingOffset() + getBottomPaddingOffset() + 2
                    * mRadius;
            // 取出最小值
            result = Math.min(result, specSize);
        }
        return result;
    }

    /**
     * 测量view的宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private int measuredWidth(int widthMeasureSpec) {
        int result = 0;// 计算宽度的最终结果
        // 获取测量规格
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        // 获取系统计算的宽度
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        // 若是精确测量规格
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {// 若是模糊测量规格，即wrap_content值
            result = getLeftPaddingOffset() + getRightPaddingOffset() + mCount
                    * 2 * mRadius + mSpace * (mCount - 1);

            // 取出最小值
            result = Math.min(result, specSize);
        }
        return result;
    }

    // 绘制圆形
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setAntiAlias(true);// 设置画笔抗锯齿
        // 计算左边距
        int leftSpace = mSpace / 2;
        //int leftSpace=mSpace;
        // 依次绘制所有圆点
        for (int i = 0; i < mCount; i++) {
            //int color = i == mFocus ? mFocusColor : mNormalColor;
            int color = (i <= mFocus ? mFocusColor : mNormalColor);
            p.setColor(color);// 设置画笔颜色
//            canvas.drawCircle(getLeftPaddingOffset() + mSpace + mRadius + i
//                    * (2 * mRadius + mSpace), getHeight() / 2, mRadius, p);
            canvas.drawCircle(leftSpace + i * mSpace, getHeight() / 2, mRadius, p);
        }
    }

    public int getmSpace() {
        return mSpace;
    }

    public void setmSpace(int mSpace) {
        this.mSpace = mSpace;
    }
}

