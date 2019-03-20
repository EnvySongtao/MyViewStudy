package com.gst.myviewstudy.gameView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 作者：GuoSongtao on 2016/7/26 17:07
 * 邮箱: 157010607@qq.com
 */
public class Game1024Layout extends RelativeLayout {
    /**
     * 设置Item的数量n*n；默认为4
     */
    private int mColumn = 4;
    /**
     * 存放所有的Item
     */
    private Game1024Item[] mGame2048Items;

    /**
     * Item横向与纵向的边距
     */
    private int mMargin = 10;
    /**
     * 面板的padding
     */
    private int mPadding;
    /**
     * 检测用户滑动的手势
     */
    private GestureDetector mGestureDetector;

    // 用于确认是否需要生成一个新的值
    private boolean isMergeHappen = true;
    private boolean isMoveHappen = true;

    public interface OnGame2048Listener {
        void onScoreChange(int mScore);

        void onGameOver();
    }

    public OnGame2048Listener mGame2048Listener;
    /**
     * 记录分数
     */
    private int mScore;


    public Game1024Layout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mMargin, getResources().getDisplayMetrics());
        // 设置Layout的内边距，四边一致，设置为四内边距中的最小值
        mPadding = min(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
                getPaddingBottom());

        mGestureDetector = new GestureDetector(context, new MyGestureDetector());

    }


    public Game1024Layout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mMargin, getResources().getDisplayMetrics());
        // 设置Layout的内边距，四边一致，设置为四内边距中的最小值
        mPadding = min(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
                getPaddingBottom());

        mGestureDetector = new GestureDetector(context, new MyGestureDetector());

    }

    private int min(int a, int b, int c, int d) {
        int minab = (a > b) ? b : a;
        int mincd = (c > d) ? d : c;
        return (minab > mincd) ? mincd : minab;
    }


    private boolean once;

    /**
     * 测量Layout的宽和高，以及设置Item的宽和高，这里忽略wrap_content 以宽、高之中的最小值绘制正方形
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得正方形的边长
        int length = Math.min(getMeasuredHeight(), getMeasuredWidth());
        // 获得Item的宽度
        int childWidth = (length - mPadding * 2 - mMargin * (mColumn - 1))
                / mColumn;

        if (!once) {
            if (mGame2048Items == null) {
                mGame2048Items = new Game1024Item[mColumn * mColumn];
            }
            // 放置Item
            for (int i = 0; i < mGame2048Items.length; i++) {

                Game1024Item item = new Game1024Item(getContext());

                mGame2048Items[i] = item;
                item.setId(i + 1);
                LayoutParams lp = new LayoutParams(childWidth,
                        childWidth);
                // 设置横向边距,不是最后一列
                if ((i + 1) % mColumn != 0) {
                    lp.rightMargin = mMargin;
                }
                // 如果不是第一列
                if (i % mColumn != 0) {
                    lp.addRule(RelativeLayout.RIGHT_OF,//
                            mGame2048Items[i - 1].getId());
                }
                // 如果不是第一行，//设置纵向边距，非最后一行
                if ((i + 1) > mColumn) {
                    lp.topMargin = mMargin;
                    lp.addRule(RelativeLayout.BELOW,//
                            mGame2048Items[i - mColumn].getId());
                }
                addView(item, lp);
            }
            generateNum();
        }
        once = true;

        setMeasuredDimension(length, length);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }


    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        final int FLING_MIN_DISTANCE = 50;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();

            //向右滑
            if (x > FLING_MIN_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {

                action(ACTION.RIGHT);

            } else if (x < -FLING_MIN_DISTANCE && Math.abs(velocityX) > Math.abs(velocityY)) {

                action(ACTION.LEFT);

            } else if (y > FLING_MIN_DISTANCE && Math.abs(velocityX) < Math.abs(velocityY)) {

                action(ACTION.DOWM);

            } else if (y < -FLING_MIN_DISTANCE && Math.abs(velocityX) < Math.abs(velocityY)) {

                action(ACTION.UP);
            }
            return true;

        }

    }

    /**
     * 根据用户运动，整体进行移动合并值等
     */
    private void action(ACTION action) {
        // 行|列
        for (int i = 0; i < mColumn; i++) {
            List<Game1024Item> row = new ArrayList<Game1024Item>();
            // 行|列
            for (int j = 0; j < mColumn; j++) {
                // 得到下标
                int index = getIndexByAction(action, i, j);

                Game1024Item item = mGame2048Items[index];
                // 记录不为0的数字
                if (item.getNumber() != 0) {
                    row.add(item);
                }
            }

            for (int j = 0; j < mColumn && j < row.size(); j++) {
                int index = getIndexByAction(action, i, j);
                Game1024Item item = mGame2048Items[index];

                if (item.getNumber() != row.get(j).getNumber()) {
                    isMoveHappen = true;
                }
            }

            // 合并相同的
            mergeItem(row);


            // 设置合并后的值
            for (int j = 0; j < mColumn; j++) {
                int index = getIndexByAction(action, i, j);
                if (row.size() > j) {
                    mGame2048Items[index].setNumber(row.get(j).getNumber());
                } else {
                    mGame2048Items[index].setNumber(0);
                }
            }

        }
        generateNum();

    }

    private int getIndexByAction(ACTION action, int i, int j) {
        int index = 0;
        if (ACTION.DOWM == action) {
            index = i + (mColumn - j - 1) * mColumn;
        } else if (ACTION.UP == action) {
            index = i + j * mColumn;
        } else if (ACTION.LEFT == action) {//向左划
            index = i * mColumn + j;
        } else if (ACTION.RIGHT == action) {//向右划
            index = i * mColumn + (mColumn - j - 1);
        }
        return index;
    }

    /**
     * 合并相同的Item
     *
     * @param row
     */
    private void mergeItem(List<Game1024Item> row) {
        if (row.size() < 2)
            return;

        for (int j = 0; j < row.size() - 1; j++) {
            Game1024Item item1 = row.get(j);
            Game1024Item item2 = row.get(j + 1);

            if (item1.getNumber() == item2.getNumber()) {
                isMergeHappen = true;

                int val = item1.getNumber() + item2.getNumber();
                item1.setNumber(val);

                // 加分
                mScore += val;
                if (mGame2048Listener != null) {
                    mGame2048Listener.onScoreChange(mScore);
                }

                // 向前移动
                for (int k = j + 1; k < row.size() - 1; k++) {
                    row.get(k).setNumber(row.get(k + 1).getNumber());
                }

                row.get(row.size() - 1).setNumber(0);
                return;
            }

        }

    }

    /**
     * 产生一个数字
     */
    public void generateNum() {

        if (checkOver()) {
            Log.e("TAG", "GAME OVER");
            if (mGame2048Listener != null) {
                mGame2048Listener.onGameOver();
            }
            return;
        }

        if (!isFull()) {
            if (isMoveHappen || isMergeHappen) {
                Random random = new Random();
                int next = random.nextInt(16);
                Game1024Item item = mGame2048Items[next];

                while (item.getNumber() != 0) {
                    next = random.nextInt(16);
                    item = mGame2048Items[next];
                }

                item.setNumber(Math.random() > 0.75 ? 4 : 2);

                isMergeHappen = isMoveHappen = false;
            }

        }
    }

    /**
     * 检测当前所有的位置都有数字，且相邻的没有相同的数字
     *
     * @return
     */
    private boolean checkOver() {
        // 检测是否所有位置都有数字
        if (!isFull()) {
            return false;
        }

        for (int i = 0; i < mColumn; i++) {
            for (int j = 0; j < mColumn; j++) {

                int index = i * mColumn + j;

                // 当前的Item
                Game1024Item item = mGame2048Items[index];
                // 右边
                if ((index + 1) % mColumn != 0) {
                    Log.e("TAG", "RIGHT");
                    // 右边的Item
                    Game1024Item itemRight = mGame2048Items[index + 1];
                    if (item.getNumber() == itemRight.getNumber())
                        return false;
                }
                // 下边
                if ((index + mColumn) < mColumn * mColumn) {
                    Log.e("TAG", "DOWN");
                    Game1024Item itemBottom = mGame2048Items[index + mColumn];
                    if (item.getNumber() == itemBottom.getNumber())
                        return false;
                }
                // 左边
                if (index % mColumn != 0) {
                    Log.e("TAG", "LEFT");
                    Game1024Item itemLeft = mGame2048Items[index - 1];
                    if (itemLeft.getNumber() == item.getNumber())
                        return false;
                }
                // 上边
                if (index + 1 > mColumn) {
                    Log.e("TAG", "UP");
                    Game1024Item itemTop = mGame2048Items[index - mColumn];
                    if (item.getNumber() == itemTop.getNumber())
                        return false;
                }

            }

        }

        return true;

    }

    /**
     * 是否填满数字
     *
     * @return
     */
    private boolean isFull() {
        // 检测是否所有位置都有数字
        for (int i = 0; i < mGame2048Items.length; i++) {
            if (mGame2048Items[i].getNumber() == 0) {
                return false;
            }
        }
        return true;
    }

    public OnGame2048Listener getOnGame2048Listener() {
        return mGame2048Listener;
    }

    public void setOnGame2048Listener(OnGame2048Listener mGame2048Listener) {
        this.mGame2048Listener = mGame2048Listener;
    }

    public void restart() {
        // 检测是否所有位置都有数字
        for (Game1024Item item : mGame2048Items) {
            item.setNumber(0);
        }
        mScore = 0;
        if (mGame2048Listener != null) {
            mGame2048Listener.onScoreChange(mScore);
        }
        isMoveHappen = isMergeHappen = true;
        generateNum();
    }

    /**
     * 控件上的滑动手势
     */
    public enum ACTION {
        LEFT, RIGHT, UP, DOWM
    }


    /***
     * 1024的每个子控件
     */
    public class Game1024Item extends View {
        /**
         * 该View上的数字
         */
        private int mNumber;
        private String mNumberVal;
        private Paint mPaint;
        private float density;
        /**
         * 绘制文字的区域
         */
        private Rect mBound;

        public Game1024Item(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            density = getResources().getDisplayMetrics().density;
        }

        public Game1024Item(Context context) {
            this(context, null);
        }

        public Game1024Item(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public void setNumber(int number) {
            mNumber = number;
            mNumberVal = mNumber + "";
            mPaint.setTextSize(30.0f * density);
            mBound = new Rect();
            mPaint.getTextBounds(mNumberVal, 0, mNumberVal.length(), mBound);
            invalidate();
        }


        public int getNumber() {
            return mNumber;
        }

        @Override
        protected void onDraw(Canvas canvas) {

            super.onDraw(canvas);
            String mBgColor = "#FFFFFF";
            switch (mNumber) {
                case 0:
                    mBgColor = "#CCC0B3";
                    break;
                case 2:
                    mBgColor = "#EEE4DA";
                    break;
                case 4:
                    mBgColor = "#EDE0C8";
                    break;
                case 8:
                    mBgColor = "#F2B179";// #F2B179
                    break;
                case 16:
                    mBgColor = "#F49563";
                    break;
                case 32:
                    mBgColor = "#F5794D";
                    break;
                case 64:
                    mBgColor = "#F55D37";
                    break;
                case 128:
                    mBgColor = "#EEE863";
                    break;
                case 256:
                    mBgColor = "#EDB04D";
                    break;
                case 512:
                    mBgColor = "#ECB04D";
                    break;
                case 1024:
                    mBgColor = "#EB9437";
                    break;
                case 2048:
                    mBgColor = "#EA7821";
                    break;
                default:
                    mBgColor = "#EA7821";
                    break;
            }

            mPaint.setColor(Color.parseColor(mBgColor));
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);

            if (mNumber != 0) {//绘制文字
                mPaint.setColor(Color.BLACK);
                float x = (getWidth() - mBound.width()) / 2;
                float y = getHeight() / 2 + mBound.height() / 2;
                canvas.drawText(mNumberVal, x, y, mPaint);
            }

        }

    }

}
