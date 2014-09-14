package com.udevel.bettershare.view.relativelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by benny on 7/14/2014.
 */
public class SquareRelativeLayout extends RelativeLayout
{

    public SquareRelativeLayout(Context context)
    {
        super(context);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        // or you can use this if you want the square to use height as it basis
        // super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}