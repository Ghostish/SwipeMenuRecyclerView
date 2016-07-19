package learn.android.ghostish.swipetoshowrecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Kangel on 2016/7/15.
 */

public abstract class SwipeItemAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private int mButtonSheetWidth = 0;
    private Context mContext;
    private int[] mColors;
    private String[] mLabels;
    private RecyclerView mRecyclerView;

    public abstract View onPrepareViewHolder(ViewGroup parent, int viewType);

    public abstract VH onCreateViewHolder(View container);

    public void onButtonSheetItemClick(RecyclerView.ViewHolder holder, int labelPosition, String labels) {
        holder.itemView.scrollTo(0, 0);
    }

    public SwipeItemAdapter(Context mContext, RecyclerView recyclerView, int[] mColors, String[] mLabels) {
        this.mContext = mContext;
        this.mRecyclerView = recyclerView;
        this.mColors = mColors;
        this.mLabels = mLabels;
    }


    public int getButtonSheetWidth() {
        return mButtonSheetWidth;
    }

    public Context getContext() {
        return mContext;
    }

    public ViewGroup wrapItemView(View itemView, String[] labels, @ColorInt int[] colors) {
        if (colors.length != labels.length) {
            throw new IllegalArgumentException("labels array'size should equal to color array's size");
        }
        int paddingSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mContext.getResources().getDisplayMetrics());

        final LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.HORIZONTAL);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(lp);
        container.addView(itemView);
        mButtonSheetWidth = 0;
        for (int i = 0; i < labels.length; i++) {
            final TextView text = new TextView(mContext);
            text.setTextSize(16);
            text.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            text.setGravity(Gravity.CENTER);
            text.setText(labels[i]);
            text.setTextColor(Color.WHITE);
            text.setBackgroundColor(colors[i]);
            text.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int textWidth = text.getMeasuredWidth();
            text.setLayoutParams(new LinearLayout.LayoutParams(textWidth, ViewGroup.LayoutParams.MATCH_PARENT));

            final int finalI = i;
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecyclerView.ViewHolder vh = mRecyclerView.getChildViewHolder(container);
                    onButtonSheetItemClick(vh, finalI, mLabels[finalI]);
                }
            });

            container.addView(text);
            mButtonSheetWidth += textWidth;

        }
        return container;
    }

    @Override
    public void onViewDetachedFromWindow(VH holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.itemView.getScrollX() > 0) {
            Log.d("DETACH", "reset scroll");
            holder.itemView.scrollTo(0, 0);
        }
    }

    @Override
    final public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = onPrepareViewHolder(parent, viewType);
        View container = wrapItemView(v, mLabels, mColors);
        return onCreateViewHolder(container);
    }
}
