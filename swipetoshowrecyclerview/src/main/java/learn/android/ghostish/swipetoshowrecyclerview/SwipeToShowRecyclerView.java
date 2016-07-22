package learn.android.ghostish.swipetoshowrecyclerview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by Kangel on 2016/6/29.
 */

public class SwipeToShowRecyclerView extends RecyclerView {
    private int mLastX;
    private int mLastY;
    private int MAX_LENGTH = 0;
    private int MAX_EXTEND = 0;
    private int ITEM_OVER_SCROLL_LENGTH = 50;
    private View lastTouchView;  /*the last view set in the latest ACTION_DOWN event*/
    private int mLastAction;
    private ViewConfiguration mViewConfiguration = ViewConfiguration.get(getContext());
    private boolean isItemDragging = false;
    private boolean isViewAnimating = false;
    private boolean discardAllTouchEvent = false;


    public SwipeToShowRecyclerView(Context context) {
        super(context);
    }

    public SwipeToShowRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeToShowRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * return the layout position of the view which contains the point specified by the (x,y) coordinate
     *
     * @param x coordinate x
     * @param y coordinate y
     * @return Layout position of the view which contains the point specified by the (x,y) coordinate
     */

    private int pointToLayoutPosition(int x, int y) {
        int mFirstChildPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            Rect frame = new Rect();
            child.getHitRect(frame);
            if (frame.contains(x, y)) {
                return mFirstChildPosition + i;
            }
        }
        return -1;
    }

    /**
     * return the child view which covers the given coordinate
     *
     * @param x coordinate x
     * @param y coordinate y
     * @return child view which covers the given coordinate
     */
    /*return the child view which covers the given coordinate*/
    private View getChildAtPoint(int x, int y) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            Rect frame = new Rect();
            child.getHitRect(frame);
            if (frame.contains(x, y)) {
                return child;
            }
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        return super.onTouchEvent(e);
    }


    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {

        if (discardAllTouchEvent && ev.getAction() != MotionEvent.ACTION_DOWN) {
            return true;
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("ACTION", "DOWN");
                discardAllTouchEvent = false;
                if (lastTouchView != null && lastTouchView.getScrollX() == MAX_LENGTH) {
                    Rect frame = new Rect();
                    lastTouchView.getHitRect(frame);
                    Rect frame2 = new Rect(lastTouchView.getLeft(), lastTouchView.getTop(), lastTouchView.getRight() - lastTouchView.getScrollX(), lastTouchView.getBottom());
                    if (!frame.contains(x, y) || frame2.contains(x, y)) {
                        smoothScrollItem(lastTouchView, lastTouchView.getScrollX(), 0);
                        discardAllTouchEvent = true;
                        Log.d("ANIMATING", "DOWN");
                        return true;
                    }
                }
                lastTouchView = getChildAtPoint(x, y);
                if (lastTouchView !=null) {
                    lastTouchView.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    Log.d("ACTION", "CHILD " + "DOWN");
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    Log.d("ACTION", "CHILD " + "MOVE");
                                    break;
                                case MotionEvent.ACTION_CANCEL:
                                    Log.d("ACTION", "CHILD " + "CANCEL");
                                    break;
                                case MotionEvent.ACTION_UP:
                                    Log.d("ACTION", "CHILD " + "UP");
                                    break;
                                default:
                                    Log.d("ACTION", "CHILD " + "ELSE");
                                    break;
                            }
                            return lastTouchView.onTouchEvent(ev);
                        }
                    });
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("ACTION", "MOVE");

                if (lastTouchView != null) {
                    SwipeMenuItemAdapter adapter = (SwipeMenuItemAdapter) getAdapter();
                    MAX_LENGTH = adapter.getMenuWidth(getChildViewHolder(lastTouchView).getItemViewType());
                    MAX_EXTEND = MAX_LENGTH + ITEM_OVER_SCROLL_LENGTH;
                    int newScrollDistance = mLastX - x;
                    int scrollX = lastTouchView.getScrollX();
                    if (scrollX <= -ITEM_OVER_SCROLL_LENGTH && newScrollDistance < 0) {
                        newScrollDistance = 0;
                    }
                    if (scrollX >= MAX_EXTEND && newScrollDistance > 0) {
                        newScrollDistance = 0;
                    }

                    boolean isHorizontal = Math.abs(x - mLastX) > Math.abs(y - mLastY);
                    if (!isHorizontal) {
                        newScrollDistance = 0;
                    }
                    if (newScrollDistance != 0) {
                        lastTouchView.scrollBy(newScrollDistance, 0);
                        isItemDragging = true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.d("ACTION", "UP");

                if (lastTouchView != null && !isViewAnimating && isItemDragging) {
                    SwipeMenuItemAdapter adapter = (SwipeMenuItemAdapter) getAdapter();
                    MAX_LENGTH = adapter.getMenuWidth(getChildViewHolder(lastTouchView).getItemViewType());
                    MAX_EXTEND = MAX_LENGTH + ITEM_OVER_SCROLL_LENGTH;
                    int scrollX = lastTouchView.getScrollX();
                    final View v = lastTouchView;
                    if (scrollX >= MAX_LENGTH / 4) {
                        Log.d("ANIMATING", "UP");
                        smoothScrollItem(v, scrollX, MAX_LENGTH);
                    } else {
                        Log.d("ANIMATING", "UP");
                        smoothScrollItem(v, scrollX, 0);
                    }
                    isItemDragging = false;
                }
                break;
        }
        mLastX = x;
        mLastY = y;
        if ((isItemDragging || isViewAnimating) && lastTouchView != null) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
            return lastTouchView.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    private void smoothScrollItem(final View view, int startScrollX, int endScrollX) {
        ValueAnimator animator = ValueAnimator.ofInt(startScrollX, endScrollX);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currScrollX = (int) animation.getAnimatedValue();
                view.scrollTo(currScrollX, 0);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isViewAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isViewAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isViewAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
}
