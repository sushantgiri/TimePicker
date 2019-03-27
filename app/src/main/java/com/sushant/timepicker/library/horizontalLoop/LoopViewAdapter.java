package com.sushant.timepicker.library.horizontalLoop;

import android.view.View;


public abstract class LoopViewAdapter<V extends View> {


    public int getCenterIndex() {
        return setCenterIndex();
    }


    protected abstract int setCenterIndex();


    public abstract int getChildWidth();


    public abstract int getItemCount();


    public abstract V getView(int position, boolean isCenter);


    public abstract void setData(V scrollView, int position);


    public abstract void onSelect(V selectView, int position);
}