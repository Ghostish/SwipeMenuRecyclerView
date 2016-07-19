package learn.android.ghostish.swipetoshowrecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Kangel on 2016/7/15.
 */

public abstract class SwipeMenuItemAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private SparseIntArray mMenuWidths = new SparseIntArray();
    private Context mContext;
    private RecyclerView mRecyclerView;

    public static class MenuItemBean {
        String label;
        int color;

        public MenuItemBean(String label, int color) {
            this.label = label;
            this.color = color;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    /*you must return your itemview according to the given viewtype in this method*/
    public abstract View onPrepareViewHolder(ViewGroup parent, int viewType);

    /*return the viewholder created for your itemview, usually just simply return your viewHolder instance*/
    public abstract VH onCreateViewHolder(View container);

    /*return the MenuItemBean array which describe your menu according to the given viewtype.
    * Each MenuItemBean have a label and a color related to each menu item
    */
    public abstract MenuItemBean[] getMenuContent(int viewType);

    /*handle menu items' onClick event here*/
    public void onMenuItemClick(RecyclerView.ViewHolder holder, int labelPosition, String labels) {
        holder.itemView.scrollTo(0, 0);
    }

    public SwipeMenuItemAdapter(Context mContext, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.mRecyclerView = recyclerView;
    }

    public int getMenuWidth(int viewType) {
        return mMenuWidths.get(viewType);
    }

    public Context getContext() {
        return mContext;
    }

    public ViewGroup wrapItemView(View itemView, int viewType) {
        MenuItemBean[] menuItems = getMenuContent(viewType);
        int paddingSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mContext.getResources().getDisplayMetrics());

        final LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.HORIZONTAL);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(lp);
        container.addView(itemView);
        int menuWidith = 0;
        for (int i = 0; i < menuItems.length; i++) {
            final MenuItemBean bean = menuItems[i];
            final TextView text = new TextView(mContext);
            text.setTextSize(16);
            text.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            text.setGravity(Gravity.CENTER);
            text.setText(bean.getLabel());
            text.setTextColor(Color.WHITE);
            text.setBackgroundColor(bean.getColor());
            text.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int textWidth = text.getMeasuredWidth();
            text.setLayoutParams(new LinearLayout.LayoutParams(textWidth, ViewGroup.LayoutParams.MATCH_PARENT));

            final int finalI = i;
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecyclerView.ViewHolder vh = mRecyclerView.getChildViewHolder(container);
                    onMenuItemClick(vh, finalI, bean.getLabel());
                }
            });

            container.addView(text);
            menuWidith += textWidth;
            mMenuWidths.put(viewType, menuWidith);
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
        View container = wrapItemView(v, viewType);
        return onCreateViewHolder(container);
    }
}
