package com.gst.myviewstudy.baseUsed;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * author: GuoSongtao on 2018/1/12 17:05
 * email: 157010607@qq.com
 */

public class SginView extends View {
    private Paint mPaint;
    private Path mPath;//记录手指路径

    public SginView(Context context) {
        this(context, null);
    }

    public SginView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SginView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        //Paint.ANTI_ALIAS_FLAG 加强渲染效果
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        //Paint.Style.STROKE 只绘制图形轮廓(描边) FILL，填充 FILL_AND_STORKE描边加填充
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
        //Paint.Join画转折位置的填充方法 ROUND圆角补齐转折位置 MITER，斜接的，接口处两条外边相交的角 BEVEL，斜边和斜面，接口处用斜线补齐
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //Paint.Cap 首尾两端的填充方法 BUTT(默认)直接切断方形头 ROUND，圆角 SQUARE超出画笔一般的方形头
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);//画笔描边的粗细
        mPaint.setTextSize(10);//画笔写字时的大小

        mPath = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
        canvas.save();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //event.getRawX() 获取屏幕中的x坐标
        float x = event.getX();//view内的x坐标
        float y = event.getY();//view内的y坐标
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                mPath.lineTo(x, y);
                invalidate();
                break;
        }
        // TODO: 2018/1/12 确定true和super.onTouchEvent(event) 的区别
//        return super.onTouchEvent(event);
        return true;
    }

    public void reSet() {

        mPath.reset();
        invalidate();
    }
}
