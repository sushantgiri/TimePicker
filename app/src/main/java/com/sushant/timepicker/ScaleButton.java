package com.sushant.timepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.TypedValue;




public class ScaleButton extends AppCompatButton {

    private double scaleFactor = 0.15625;

    public ScaleButton(Context context) {
        super(context);
        init();
    }

    public ScaleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public ScaleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScaleButton_Layout, defStyle, 0);
        scaleFactor = (double) a.getFloat(R.styleable.ScaleImageView_Layout_scaleFactor, 0);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {

            int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
            int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);

            if (measuredWidth != 0) {
                int height = (int) (measuredWidth * scaleFactor);
                setMeasuredDimension(measuredWidth, height);
            } else {
                setMeasuredDimension(measuredWidth, measuredHeight);
            }

        } catch (Exception e) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void init() {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, ((float) (ScreenUtils.getScreenWidth(getContext()) * 0.035)));

    }
}
