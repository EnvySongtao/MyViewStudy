package com.gst.myviewstudy.baseUsed;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gst.myviewstudy.R;
import com.gst.myviewstudy.utils.LogUtil;
import com.gst.myviewstudy.utils.ViewHelper;

/**
 * 多个View组合成一个View
 * <p>
 * author: GuoSongtao on 2018/1/12 10:50
 * email: 157010607@qq.com
 */

public class ImageTextView extends LinearLayout {
    private View contentView;
    private ImageView mImageView;
    private TextView mTextView;
    private int imageResource = 0;
    private float imageHeight = 35;
    private float imageWidth = 35;
    private boolean isSelected = false;
    private CharSequence text = "";
    private int textColor = Color.WHITE;
    private float textSize = 18;

    /**
     * 构造方法使用 this（....） 模仿LinearLayout的构造方法，目的是三个构造方法都能调用 初始参数的init()方法
     *
     * @param context
     */
    public ImageTextView(Context context) {
        this(context, null);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    /**
     * @param context
     * @param set
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet set, int defStyleAttr) {

        // 获取 view自定义属性值
        TypedArray arr = context.getTheme().obtainStyledAttributes(set, R.styleable.ImageTextView, defStyleAttr, 0);
        imageResource = arr.getResourceId(R.styleable.ImageTextView_image_text_image_src, 0);
        imageHeight = arr.getDimension(R.styleable.ImageTextView_image_text_image_height, 0);
        imageWidth = arr.getDimension(R.styleable.ImageTextView_image_text_image_width, 0);
        isSelected = arr.getBoolean(R.styleable.ImageTextView_image_text_selected, false);
        text = arr.getString(R.styleable.ImageTextView_image_text_text);
        //注意 getDimension 最终得到的像素（px）单位的值
        textColor = arr.getColor(R.styleable.ImageTextView_image_text_text_color, 0);
        textSize = arr.getDimension(R.styleable.ImageTextView_image_text_text_size, 0);
        //记得 TypedArray 回收
        arr.recycle();

        //1. 如果root为null，attachToRoot将失去作用，设置任何值都没有意义。 (仅宽高属性失效，其他属性生效)
        // 2. 如果root不为null，attachToRoot设为true，则会给加载的布局文件的指定一个父布局，即root。(宽高属性生效，添加到父容器中)
        // 3. 如果root不为null，attachToRoot设为false，则会将布局文件最外层的所有layout属性进行设置，当该view被添加到父view当中时，这些layout属性会自动生效。(宽高属性生效，未添加到父容器中)
        // 4. 在不设置attachToRoot参数的情况下，如果root不为null，attachToRoot参数默认为true。(宽高属性生效，添加到父容器中)
        contentView = LayoutInflater.from(context).inflate(R.layout.view_image_text_view, this, true);
//        LogUtil.i("LayoutInflater.from(context).inflate()");
//        if (contentView.getParent() != null && contentView.getParent() instanceof ViewGroup) {
//            ((ViewGroup) contentView.getParent()).removeView(contentView);
//            LogUtil.i("((ViewGroup) contentView.getParent()).removeView(contentView)");
//        }
//        this.addView(contentView);
        mImageView = ViewHelper.bindView(contentView, R.id.iv_con);
        mTextView = ViewHelper.bindView(contentView, R.id.tv_content);

        if (contentView != null) contentView.setSelected(isSelected);

        if (mImageView != null) {
            if (imageHeight != 0 || imageWidth != 0) {
                ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
                layoutParams.height = (int) imageHeight;
                layoutParams.width = (int) imageWidth;
                mImageView.setLayoutParams(layoutParams);
            }
            if (imageResource != 0) mImageView.setImageResource(imageResource);
        }

        if (mTextView != null) {
            if (!TextUtils.isEmpty(text)) mTextView.setText(text);
            if (textColor != 0) mTextView.setTextColor(textColor);
            if (textSize != 0) mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
        if (imageResource != 0) mImageView.setImageResource(imageResource);
    }

    public void setLayoutParams(float imageWidth, float imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;

        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
        layoutParams.height = (int) imageHeight;
        layoutParams.width = (int) imageWidth;
        mImageView.setLayoutParams(layoutParams);
    }

//    @Override
//    public boolean isSelected() {
//        return isSelected;
//    }
//
//    @Override
//    public void setSelected(boolean selected) {
//        isSelected = selected;
//    }

    public void setSelect(boolean selected) {
        setSelected(selected);
        if (contentView != null) contentView.setSelected(isSelected);
    }

    public void setText(CharSequence text) {
        this.text = text;
        mTextView.setText(text);//setText 允许设置null
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        if (textColor != 0) mTextView.setTextColor(textColor);
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        if (textSize != 0) mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
}
