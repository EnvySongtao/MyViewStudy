package com.gst.myviewstudy.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;

/**
 * Created by yu on 2016/3/22.
 * <p>
 * 自定义波浪球球控件
 *
 * @introduce 实现球形水纹进度
 */
public class CircleWaveView extends View {

    /**
     * 默认颜色
     */
    private static final int DEFULT_COLOR = 0xFF000000;
    /**
     * 布局宽度
     */
    private int laywidth;
    /**
     * 布局高度
     */
    private int layheight;
    /**
     * 布局背景颜色
     */
    private int laybackcolor;
    /**
     * 水波背景颜色
     */
    private int wavesbackcolor;
    /**
     * 波浪线条颜色
     */
    private int wavelinecolor;
    /**
     * 布局背景画笔
     */
    private Paint laybackpaint;
    /**
     * 波浪背景画笔
     */
    private Paint wavebackpaint;
    /**
     * 波浪线条画笔
     */
    private Paint wavelinepaint;
    /**
     * 波浪背景轨迹
     */
    private Path wavebackpath;
    /**
     * 百分比
     */
    private float wavepercent;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 振幅
     */
    private float amplitude;
    /**
     * 波长
     */
    private int wavelength;
    /**
     * 波浪线条宽度
     */
    private int wavelinewidth;
    /**
     * 角速度
     */
    private float w;
    /**
     * 波浪起始Y坐标
     */
    private float startY;
    /**
     * 偏移角度
     */
    private float angle;
    /**
     * 截图使用轨迹
     */
    private Path mPath;
    /**
     * 是否允许动画
     */
    private boolean isallowanim;
    /**
     * 刷新视图的线程
     */
    private WaveAnimThread wThread;
    /**
     * 刷新视图的线程 不变时的波动
     */
    private WaveSameValueAnimThread wSVThread;
    /**
     * 波浪起始颜色
     */
    private int wavestartcolor;
    /**
     * 波浪中间颜色
     */
    private int wavemindlecolor;
    /**
     * 波浪终止颜色
     */
    private int waveendcolor;
    /**
     * 渐变模式 0 线性, 1圆周,2径向
     */
    private int waveshadermode;
    /**
     * 渐变器
     */
    private Shader waveShader;
    /**
     * 边框画笔
     **/
    private Paint cyclePaint;
    /**
     * 圆环颜色
     */
    private int cycleColor;
    /**
     * 圆的边界的大小，也就是最外层的那一圈边界
     **/
    private int paintW;
    /**
     * text矩形框
     **/
    private Rect rect = new Rect();
    /**
     * 进度画笔
     **/
    private Paint progressPaint;
    /**
     * 进度画笔size
     **/
    private float progressSize = 0;
    /**
     * 进度格式
     **/
    private DecimalFormat df = new DecimalFormat("0.00");
    /**
     * 文本放置坐标
     **/
    private float textX, textY;
    /**
     * 进度文本颜色
     */
    private int progressColor;

    /**
     * 构造函数
     */
    public CircleWaveView(Context context) {
        this(context, null);
    }

    /**
     * 构造函数
     */
    public CircleWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数
     */
    public CircleWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);//重写了onDraw（）方法，如果不能绘制，则可以调用此方法解决
        mContext = context;
        setCustomAttributes();// 调用初始化属性的方法
        init();// 调用初始化的方法
    }

    /**
     * 初始化一些属性值的方法
     */
    private void setCustomAttributes() {
        //颜色
        wavesbackcolor = Color.parseColor("#70b6cf");//波浪背景色
        wavelinecolor = Color.parseColor("#70b6cf");//波浪线颜色
        laybackcolor = Color.parseColor("#00000000");//布局背景色
        cycleColor = Color.parseColor("#70b6cf");//圆环颜色
        progressColor = Color.parseColor("#70b6cf");//进度颜色
        //progressColor = Color.parseColor("#00000000");//进度颜色
        //渐变
        wavestartcolor = DEFULT_COLOR;//波浪开始颜色
        wavemindlecolor = DEFULT_COLOR;//波浪中间色
        waveendcolor = DEFULT_COLOR;//波浪结束色
        waveshadermode = 0;//渐变模式
        //参数
        wavelength = dpTopx(mContext, 60);//波长
        wavelinewidth = dpTopx(mContext, 2);//波浪线条宽度
        paintW = dpTopx(mContext, 1);
        wavepercent = 0f;//百分比
        amplitude = 0;//振幅
        w = 7f;//角速度
        //w=0;
        startY = 0;//波浪其实Y坐标
        angle = 0;//偏移角度
    }

    /**
     * 初始化方法
     */
    private void init() {
        //初始化进度文本画笔
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(progressColor);//设置进度数字颜色
        progressPaint.setTypeface(Typeface.DEFAULT);//设置画笔字体
        //初始化圆环画笔
        cyclePaint = new Paint();
        cyclePaint.setStyle(Paint.Style.STROKE);// 空心画笔
        cyclePaint.setColor(cycleColor);
        cyclePaint.setAntiAlias(true);
        cyclePaint.setStrokeWidth(paintW);
        // 初始化画笔和轨迹对象
        laybackpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavebackpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavelinepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavebackpath = new Path();
        mPath = new Path();
        wavelinepaint.setStyle(Paint.Style.STROKE);
        // 设置画笔颜色
        laybackpaint.setColor(laybackcolor);
        wavebackpaint.setColor(wavesbackcolor);
        wavelinepaint.setColor(wavelinecolor);
        // 设置水波线条宽度
        wavelinepaint.setStrokeWidth(wavelinewidth);
        wThread = new WaveAnimThread();
        wSVThread = new WaveSameValueAnimThread();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        laywidth = getMeasuredWidth();
        layheight = getMeasuredHeight();
        //		Log.d("msg", "laywidth = "+laywidth+"   "+"layheight = "+layheight);
        //		Log.d("msg", "laywidth = "+pxTodp(mContext, laywidth)+"   "+"layheight = "+pxTodp(mContext, layheight));
        startY = layheight - paintW + wavelinewidth / 2;
        if (progressSize == 0) {
            int proRate = 5;//进度文字所占比例
            if (laywidth >= layheight) {
                progressSize = layheight / proRate;
            } else {
                progressSize = laywidth / proRate;
            }
            progressPaint.setTextSize(progressSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        String str = "0";
        progressPaint.getTextBounds(str, 0, str.length(), rect);
        //设置progressText的显示高度
        //textY = (float) ((getHeight() + rect.height()) * 0.5);
        textY = (float) ((getHeight() + rect.height()) * 0.3);
    }


    /**
     * 创建视图之前调用的方法
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //		isallowanim = true;
        //		wThread.start();
    }

    /**
     * 销毁视图之前调用的方法
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isallowanim = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
        //绘制内层背景圆
        canvas.drawCircle((float) laywidth / 2, (float) laywidth / 2,
                (float) laywidth / 2 - paintW / 2, laybackpaint);
        // 下面几句是设置截取轨迹的方法
        canvas.save();// save用来保存Canvas的状态。save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。
        mPath.reset();
        canvas.clipPath(mPath); // makes the clip empty
        mPath.addCircle(laywidth / 2, layheight / 2, laywidth / 2 - paintW / 2,
                Path.Direction.CCW);
        canvas.clipPath(mPath, Region.Op.REPLACE);
        // 调用绘制水纹波浪的方法
        setPath(canvas);
        canvas.drawCircle(laywidth / 2, layheight / 2, laywidth / 2 - paintW / 2, cyclePaint);
        //绘制进度条文本
        drawProgress(canvas);
    }

    /**
     * 绘制进度文本方法
     *
     * @param canvas 画笔
     */
    private String str;

    private void drawProgress(Canvas canvas) {
//        if(textShow<=0){
//            str = "0%";
//        }else if(textShow == wavepercent){
//            str = df.format(wavepercent) + "%";
//            isallowanim = false;
//            if(textShow == 100){
//                wavebackpaint.setColor(Color.parseColor("#2775d9"));//进度条满了之后，颜色变成蓝色
//                wavelinepaint.setColor(Color.parseColor("#2775d9"));
//            }
//        }else{
//            //str = df.format(textShow) + "%";
//            str=df.format(wavepercent)+"%";
//        }
        str = df.format(wavepercent) + "%";
        textX = (float) ((getWidth() - rect.width() * str.length()) * 0.45);
        canvas.drawText(str, textX, textY, progressPaint);

    }

    /**
     * 设置渐变器的方法
     */
    private void setShaderMethod() {
        if (wavestartcolor != DEFULT_COLOR && wavemindlecolor != DEFULT_COLOR
                && waveendcolor != DEFULT_COLOR) {
            setShaderMethod(waveshadermode, new int[]{wavestartcolor,
                    wavemindlecolor, waveendcolor});
        } else if (wavestartcolor != DEFULT_COLOR
                && wavemindlecolor != DEFULT_COLOR
                && waveendcolor == DEFULT_COLOR) {
            setShaderMethod(waveshadermode, new int[]{wavestartcolor,
                    wavemindlecolor});
        } else if (wavestartcolor != DEFULT_COLOR
                && wavemindlecolor == DEFULT_COLOR
                && waveendcolor != DEFULT_COLOR) {
            setShaderMethod(waveshadermode, new int[]{wavestartcolor,
                    waveendcolor});
            System.out.println();
        } else if (wavestartcolor != DEFULT_COLOR
                && wavemindlecolor == DEFULT_COLOR
                && waveendcolor == DEFULT_COLOR) {
            waveShader = null;
        } else if (wavestartcolor == DEFULT_COLOR
                && wavemindlecolor != DEFULT_COLOR
                && waveendcolor != DEFULT_COLOR) {
            setShaderMethod(waveshadermode, new int[]{wavemindlecolor,
                    waveendcolor});
        } else if (wavestartcolor == DEFULT_COLOR
                && wavemindlecolor != DEFULT_COLOR
                && waveendcolor == DEFULT_COLOR) {
            waveShader = null;
        } else if (wavestartcolor == DEFULT_COLOR
                && wavemindlecolor == DEFULT_COLOR
                && waveendcolor != DEFULT_COLOR) {
            waveShader = null;
        } else if (wavestartcolor == DEFULT_COLOR
                && wavemindlecolor == DEFULT_COLOR
                && waveendcolor == DEFULT_COLOR) {
            waveShader = null;
        }
    }

    /**
     * 设置渐变器的方法
     *
     * @param modle  渐变模式 0 线性, 1圆周,2径向
     * @param colors 颜色值
     */
    private void setShaderMethod(int modle, int[] colors) {
        switch (modle) {
            case 0:
                waveShader = new LinearGradient(laywidth / 2, startY - amplitude,
                        laywidth / 2, layheight, colors, null,
                        Shader.TileMode.CLAMP);
                break;
            case 1:
                waveShader = new SweepGradient(laywidth / 2, layheight / 2, colors,
                        null);
                break;
            case 2:
                waveShader = new RadialGradient(laywidth / 2, layheight / 2,
                        laywidth / 2, colors, null, Shader.TileMode.CLAMP);
                break;
            default:
                break;
        }
    }

    /**
     * 绘制水纹波浪球的方法
     *
     * @param canvas
     */
    private void setPath(Canvas canvas) {
        float mX = 0;
        float mY = startY;
        wavebackpath.reset();//清除路径所有设置
        wavebackpath.moveTo(mX, startY);
        for (float i = 0; i <= laywidth; i++) {
            float y = startY - (float) (Math.sin((i / wavelength + angle / 180) * Math.PI) * amplitude);
            wavebackpath.quadTo(mX, mY, (i + mX) / 2, (y + mY) / 2);
            mX = i;
            mY = y;
        }
        wavebackpath.lineTo(laywidth, layheight);
        wavebackpath.lineTo(0, layheight);
        //setShaderMethod();// 调用设置渐变器的方法
        //wavebackpaint.setShader(waveShader);
        canvas.drawPath(wavebackpath, wavebackpaint);
        canvas.drawPath(wavebackpath, wavelinepaint);
        canvas.restore();
    }

    /**
     * DP转化PX
     *
     * @param context
     * @param dpValue
     * @return
     */
    private int dpTopx(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;//获得当前屏幕密度
        return (int) (dpValue * scale + 0.5f);
    }

    private float textShow = 0;
    private int textPercent = 100;//解决0费率无法显示问题

    /**
     * 执行波浪动画的线程
     */
    private class WaveAnimThread extends Thread {
        @Override
        public void run() {
            while (isallowanim) {
                //				float nowY = (int) ((layheight + 2*paintW - amplitude) * (1 - wavepercent / 100));
                int lineWide = wavelinewidth / 2;
                float basePercent = getWavepercent() * 50;
                float wavepercent = basePercent + 25;
                if (basePercent >= 0 && basePercent < 1) {
                    textPercent--;

                    //Log.d("msg","textPercent="+textPercent);
                }
//                if(wavepercent>=1 && wavepercent<=5){
//                    lineWide = -(wavelinewidth*2);
//                }
                //若显示的数字比较小，可调整此处wavepercent/m 调整显示水波效果
                int nowY = (int) ((layheight - paintW) * (1 - wavepercent / 100) + paintW + lineWide);
                if (angle >= 360) {
                    angle -= 360;
                }
                angle += w;
                if (startY > nowY) {
                    startY--;
                    textShow = (1 - startY / (layheight - paintW)) * 100;

                    changeAmplitude(textShow);
                    // Log.d("msg", "textShow = "+textShow);
                    //					Log.d("msg", "nowY = "+nowY +"   "+"startY = "+startY);

                } else if (startY == nowY) {
                    textShow = getWavepercent();
                } else {
                    startY++;
                }
                refreshHandler.sendEmptyMessage(1);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 执行波浪动画的线程
     */
    private float sameValueDy = 0.01F;
    private boolean isSameAdded = false;

    private class WaveSameValueAnimThread extends Thread {
        @Override
        public void run() {
            while (isallowanim) {
                //				float nowY = (int) ((layheight + 2*paintW - amplitude) * (1 - wavepercent / 100));
                int lineWide = wavelinewidth / 2;
                float basePercent = getWavepercent() * 50;
                float wavepercent = basePercent + 25;
                if (basePercent >= 0 && basePercent < 1) {
                    textPercent--;
                }
                //若显示的数字比较小，可调整此处wavepercent/m 调整显示水波效果
                int nowY = (int) ((layheight - paintW) * (1 - wavepercent / 100) + paintW + lineWide);
                if (angle >= 360) {
                    angle -= 360;
                }
                angle += w;

                textShow = getWavepercent();

                if (isSameAdded) {
                    startY = nowY + sameValueDy;
                } else {
                    startY = nowY - sameValueDy;
                }
                isSameAdded = !isSameAdded;
                refreshHandler.sendEmptyMessage(1);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据进度动态改变振幅
     */
    private void changeAmplitude(float textShow) {
        if (textShow > 95) {
            amplitude = dpTopx(mContext, 0);
            //			Log.d("msg", "1");
        } else if (textShow > 85) {
            amplitude = dpTopx(mContext, 1);
            //			Log.d("msg", "2");
        } else if (textShow > 65) {
            amplitude = dpTopx(mContext, 3);
            //			Log.d("msg", "3");
        } else if (textShow > 35) {
            amplitude = dpTopx(mContext, 6);
            //			Log.d("msg", "4");
        } else if (textShow > 15) {
            amplitude = dpTopx(mContext, 4);
            //			Log.d("msg", "5");
        } else if (textShow > 5) {
            amplitude = dpTopx(mContext, 2);
            //			Log.d("msg", "6");
        } else if (textShow >= 0) {
            amplitude = dpTopx(mContext, 0);
            //			Log.d("msg", "7");
        }
    }


    /**
     * 执行刷新视图工作的Handler
     */
    private Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            postInvalidate();//调用该方法，则回调onDraw方法
            if (wavepercent != 0) {
                if (textShow == wavepercent) {
                    isallowanim = false;
                }
            } else {
                if (textPercent < 0) {
                    isallowanim = false;
                }
            }

        }
    };
    //----------------------------------------------------------------------------------------

    /**
     * 获取振幅
     */
    public float getAmplitude() {
        return amplitude;
    }

    /**
     * 设置振幅
     */
    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
        postInvalidate();
    }

    /**
     * 获取布局背景颜色
     */
    public int getLaybackcolor() {
        return laybackcolor;
    }

    /**
     * 设置布局背景颜色
     */
    public void setLaybackcolor(int laybackcolor) {
        this.laybackcolor = laybackcolor;
        postInvalidate();
    }

    /**
     * 获取水波背景颜色
     */
    public int getWavesbackcolor() {
        return wavesbackcolor;
    }

    /**
     * 设置水波背景颜色
     */
    public void setWavesbackcolor(int wavesbackcolor) {
        this.wavesbackcolor = wavesbackcolor;
        postInvalidate();
    }

    /**
     * 获取水波线条颜色
     */
    public int getWavelinecolor() {
        return wavelinecolor;
    }

    /**
     * 设置水波线条颜色
     */
    public void setWavelinecolor(int wavelinecolor) {
        this.wavelinecolor = wavelinecolor;
        postInvalidate();
    }

    /**
     * 获取波长
     */
    public int getWavelength() {
        return wavelength;
    }

    /**
     * 设置波长
     */
    public void setWavelength(int wavelength) {
        this.wavelength = wavelength;
        postInvalidate();
    }

    /**
     * 获取水波线条宽度
     */
    public int getWavelinewidth() {
        return wavelinewidth;
    }

    /**
     * 设置水波线条宽度
     */
    public void setWavelinewidth(int wavelinewidth) {
        this.wavelinewidth = wavelinewidth;
        postInvalidate();
    }

    /**
     * 获取百分比
     */
    public float getWavepercent() {
        return wavepercent;
    }

    /**
     * 设置百分比
     */
    public void setWavepercent(float wavepercent) {
        this.wavepercent = wavepercent;
        postInvalidate();
    }

    /**
     * 设置是否动画
     */
    public void setIsallowanim(boolean isallowanim) {
        this.isallowanim = isallowanim;
    }

    /**
     * 开启动画
     */
    private boolean isFirstStart = true;

    public void startAnimationThread() {
        if (isFirstStart && !wThread.isAlive() && wThread != null) {
            isallowanim = true;
            wThread.start();
            isFirstStart = false;
        } else {
            return;
        }
    }

    /**
     *
     */
    public void startSameValueAnimationThread() {
        if (isFirstStart && !wThread.isAlive() && wThread != null) {
            isallowanim = true;
            wSVThread.start();
            isFirstStart = false;
        } else {
            return;
        }
    }

    /**
     * 设置圆环宽度
     *
     * @param paintW
     */
    public void setPaintW(int paintW) {
        this.paintW = paintW;
    }

    public Paint getWavebackpaint() {
        return wavebackpaint;
    }

    public void setWavebackpaint(Paint wavebackpaint) {
        this.wavebackpaint = wavebackpaint;
    }

    public Paint getWavelinepaint() {
        return wavelinepaint;
    }

    public void setWavelinepaint(Paint wavelinepaint) {
        this.wavelinepaint = wavelinepaint;
    }

    public Paint getCyclePaint() {
        return cyclePaint;
    }

    public void setCyclePaint(Paint cyclePaint) {
        this.cyclePaint = cyclePaint;
    }

    public Paint getProgressPaint() {
        return progressPaint;
    }

    public void setProgressPaint(Paint progressPaint) {
        this.progressPaint = progressPaint;
    }
}
