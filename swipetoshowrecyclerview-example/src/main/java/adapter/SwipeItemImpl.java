package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import learn.android.ghostish.swipetoshowrecyclerview.SwipeItemAdapter;
import learn.android.ghostish.swipetoshowrecyclerviewdemo.R;


/**
 * Created by Kangel on 2016/7/15.
 */

public class SwipeItemImpl extends SwipeItemAdapter<SwipeItemImpl.ViewHolder> {
    private List<Bean> mData;

    @Override
    public View onPrepareViewHolder(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_item_layout, parent, false);

    }

    @Override
    public ViewHolder onCreateViewHolder(View container) {
        return new ViewHolder(container);
    }

    @Override
    public void onButtonSheetItemClick(RecyclerView.ViewHolder viewHolder, int labelPosition, String labels) {
        super.onButtonSheetItemClick(viewHolder, labelPosition, labels);
        switch (labelPosition) {
            case 0: {
                Bean bean = mData.remove(viewHolder.getAdapterPosition());
                mData.add(0, bean);
                notifyItemRangeChanged(0, mData.size());
                break;
            }
            case 1: {
                Bean bean = mData.get(viewHolder.getAdapterPosition());
                bean.setRead(false);
                notifyItemChanged(viewHolder.getAdapterPosition());
                break;
            }

            case 2:
                mData.remove(viewHolder.getAdapterPosition());
                notifyItemRemoved(viewHolder.getAdapterPosition());
                break;
        }
    }


    public SwipeItemImpl(Context mContext, RecyclerView recyclerView, List<Bean> mData, int[] colors, String[] labels) {
        super(mContext, recyclerView, colors, labels);
        this.mData = mData;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bean bean = mData.get(position);
        holder.itemText.setText(bean.getId());
        holder.unreadIndicator.setVisibility(bean.isRead() ? View.GONE : View.VISIBLE);
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemText;
        View unreadIndicator;

        ViewHolder(final View itemView) {
            super(itemView);
            itemText = (TextView) itemView.findViewById(R.id.info_text);
            unreadIndicator = itemView.findViewById(R.id.unread_indicator);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Item " + mData.get(getAdapterPosition()).getId() + " Clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    static public class Bean {
        private String id;
        private boolean isRead;

        public Bean(String id, boolean isRead) {

            this.id = id;
            this.isRead = isRead;
        }

        public boolean isRead() {
            return isRead;
        }

        public void setRead(boolean read) {
            isRead = read;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


    }
}
