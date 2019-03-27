package com.sushant.timepicker.library.horizontalLoop;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.sushant.timepicker.R;


public class HorizontalLoopView extends LinearLayout {
    private final String TAG = "HorizontalLoopView";
    private boolean isLoop = false;
    private int mChildWidth = 70;
    /**
     * all child sum width
     */
    private int mChildrenSumWidth;
    /**
     * scroll offset
     */
    private int mInitialOffset;
    /**
     * X offset
     */
    private int mScrollX;
    /**
     * last scroll
     */
    private int mLastScroll;
    private Scroller mScroller;
    /**
     * min scroll speed
     */
    private int mMinimumVelocity;
    /**
     * max scroll speed
     */
    private int mMaximumVelocity;

    private final int NO_MODE = 0;

    private final int DRAG_MODE = 1;

    private final int CLICK_MODE = 2;

    private final int FLING_MODE = 3;

    private final int MOVE_CENTER_MODE = 4;

    private final int INDEX_TAG = R.id.horizontal_loop_view_key;
    private int stateMode = NO_MODE;
    private int mLastX;
    private int mFirstX;
    private VelocityTracker mVelocityTracker;
    private int mDataIndex;
    private View mCenterView;
    private LoopViewAdapter loopViewAdapter;

    public HorizontalLoopView(Context context) {
        this(context, null);
    }

    public HorizontalLoopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalLoopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setWillNotDraw(false);
        initScroll();
        mDataIndex = 0;
    }


    public void setLoopViewAdapter(LoopViewAdapter loopViewAdapter) {
        if (loopViewAdapter == null) {
            throw new RuntimeException("loopViewAdapter is null");
        }
        this.loopViewAdapter = loopViewAdapter;
        this.mChildWidth = loopViewAdapter.getChildWidth();
        initView();
    }


    private void initScroll() {
        mScroller = new Scroller(getContext());
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(HORIZONTAL);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    private void initView() {
        final int displayWidth = DensityUtils.getWindowWidth(getContext());
        int childCount = displayWidth / mChildWidth;

        if (childCount % 2 == 0) {
            childCount++;
        }

        childCount += 2;
        removeAllViews();
        int mCenterIndex = childCount / 2;

        mDataIndex = loopViewAdapter.getCenterIndex();
        for (int i = 0; i < childCount; i++) {
            View childView = loopViewAdapter.getView(i, i == mCenterIndex);
            childView.setSelected(i == mCenterIndex);
            addView(childView);
        }


        mCenterView = getChildAt(mCenterIndex);
        loopViewAdapter.setData(mCenterView, mDataIndex);
        mCenterView.setTag(INDEX_TAG, mDataIndex);
        loopViewAdapter.onSelect(mCenterView, mDataIndex);

        for (int i = mCenterIndex + 1, position = mDataIndex + 1; i < childCount; i++, position++) {
            if (position == loopViewAdapter.getItemCount()) {
                position = 0;
            }
            View childAtView = getChildAt(i);
            loopViewAdapter.setData(childAtView, position);
            childAtView.setTag(INDEX_TAG, position);
        }

        for (int i = mCenterIndex - 1, position = mDataIndex - 1; i >= 0; i--, position--) {
            if (position < 0) {
                position = loopViewAdapter.getItemCount() - 1;
            }
            View childAtView = getChildAt(i);
            loopViewAdapter.setData(childAtView, position);
            childAtView.setTag(INDEX_TAG, position);
        }


        mChildrenSumWidth = mChildWidth * childCount;
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mScrollX = mScroller.getCurrX();
            reScrollTo(mScrollX, 0);
            postInvalidate();
        } else {
            if (stateMode == FLING_MODE) {
                stateMode = MOVE_CENTER_MODE;
                selectPosition(getChildCount() / 2);
            }
        }
    }

    public boolean isScrolling(){
        return mScroller != null && !mScroller.isFinished();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        reScrollTo(x, y);
        selectPosition(getChildCount() / 2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mInitialOffset = (mChildrenSumWidth - w) / 2;
        super.scrollTo(mInitialOffset, 0);
        mScrollX = mInitialOffset;
        mLastScroll = mInitialOffset;
    }

    protected void reScrollTo(int x, int y) {
        int scrollX = getScrollX();
        int scrollDiff = x - mLastScroll;

        if (getChildCount() > 0) {
            scrollX += scrollDiff;

            if (scrollX - mInitialOffset > mChildWidth / 2) {
                int relativeScroll = scrollX - mInitialOffset;
                int stepsRight = (relativeScroll + (mChildWidth / 2)) / mChildWidth;
                moveChildres(-stepsRight);
                scrollX = ((relativeScroll - mChildWidth / 2) % mChildWidth) + mInitialOffset - mChildWidth / 2;
            } else if (mInitialOffset - scrollX > mChildWidth / 2) {//向前滑动
                int relativeScroll = mInitialOffset - scrollX;
                int stepsLeft = (relativeScroll + (mChildWidth / 2)) / mChildWidth;
                moveChildres(stepsLeft);
                scrollX = (mInitialOffset + mChildWidth / 2 - ((mInitialOffset + mChildWidth / 2 - scrollX) % mChildWidth));
            }
        }
        super.scrollTo(scrollX, y);

        if (mCenterView != null && mScroller.isFinished()) {
            if (stateMode == CLICK_MODE || stateMode == MOVE_CENTER_MODE) {
                if (loopViewAdapter != null) {
                    loopViewAdapter.onSelect(mCenterView, ((int) mCenterView.getTag(INDEX_TAG)));
                }
            }
        }
        mLastScroll = x;
    }


    protected void moveChildres(int steps) {

        if (steps == 0 || loopViewAdapter == null) {
            return;
        }
        int start;
        int end;
        int incr;
        if (steps < 0) {
            start = 0;
            end = getChildCount();
            incr = 1;
        } else {
            start = getChildCount() - 1;
            end = -1;
            incr = -1;
        }
        for (int i = start; i != end; i += incr) {
            View childAtView = getChildAt(i);
            int mNowIndex = (int) childAtView.getTag(INDEX_TAG);
            if (steps > 0) {
                if (mNowIndex == 0 /*&& isLoop*/) {
                    mNowIndex = loopViewAdapter.getItemCount();
                }
                mNowIndex--;
               /* if (isLoop && mNowIndex >= 0) {
                    childAtView.setVisibility(VISIBLE);
                }
                if (!isLoop && mNowIndex < 0) {
                    childAtView.setVisibility(INVISIBLE);
                }*/
            } else {
                if (mNowIndex == loopViewAdapter.getItemCount() - 1/* && isLoop*/) {
                    mNowIndex = -1;
                }
                mNowIndex++;
               /* if (isLoop && mNowIndex <= loopViewAdapter.getItemCount() - 1) {
                    childAtView.setVisibility(VISIBLE);
                }
                if (!isLoop && mNowIndex > loopViewAdapter.getItemCount() - 1) {
                    childAtView.setVisibility(INVISIBLE);
                }*/
            }

            if (childAtView.isSelected()) {
                mCenterView = childAtView;
            }
            childAtView.setTag(INDEX_TAG, mNowIndex);

            if (loopViewAdapter != null && childAtView.getVisibility() == VISIBLE) {
                loopViewAdapter.setData(childAtView, mNowIndex);
            }

        }
    }


    /**
     * finding whether to scroll or not
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final int x = (int) ev.getX();
        if (action == MotionEvent.ACTION_DOWN) {
            stateMode = CLICK_MODE;
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
        }

        if (stateMode == NO_MODE)
            return super.onTouchEvent(ev);

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mFirstX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mFirstX - x) > 10) {
                    stateMode = DRAG_MODE;
                }
                mScrollX += mLastX - x;
                reScrollTo(mScrollX, 0);
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(300);
                int initialVelocity = (int) Math.min(velocityTracker.getXVelocity(), mMaximumVelocity);
                if (getChildCount() > 0 && Math.abs(initialVelocity) > mMinimumVelocity) {
                    fling(-initialVelocity);
                    stateMode = FLING_MODE;
                } else {
                    if (stateMode == CLICK_MODE) {
                        clickFunction(ev);
                    } else {
                        stateMode = MOVE_CENTER_MODE;
                        selectPosition(getChildCount() / 2);
                    }
                }
            case MotionEvent.ACTION_CANCEL:
            default:
        }
        mLastX = x;

        return true;
    }


    public void clickFunction(MotionEvent ev) {
        int x = (int) ev.getX();
        int selectIndex = (x + getScrollX()) / mChildWidth;
        selectPosition(selectIndex);
    }



    public void selectPosition(int selectIndex) {
        int childCount = getChildCount();
        final int centerIndex = (childCount / 2);
        int centerX = getWidth() / 2;
        int posX = mChildWidth * selectIndex - getScrollX() + mChildWidth / 2;
        int diff = posX - centerX;
        int count = Math.abs(selectIndex - centerIndex);
        count = (count == 0) ? 1 : count;
        mScroller.startScroll(mScrollX, 0, diff, 0, 800 * count);
        postInvalidate();
    }


    private void fling(int velocityX) {
        if (getChildCount() > 0) {
            mScroller.fling(mScrollX, 0, velocityX, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
            invalidate();
        }
    }
}
