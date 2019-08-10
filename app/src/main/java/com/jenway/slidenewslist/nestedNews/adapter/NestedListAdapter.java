package com.jenway.slidenewslist.nestedNews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.jenway.slidenewslist.MyApplication;
import com.jenway.slidenewslist.R;
import com.jenway.slidenewslist.model.BaseEntity;
import com.jenway.slidenewslist.model.NewsEntity;
import com.jenway.slidenewslist.ui.HorizontalItemViewHolder;
import com.jenway.slidenewslist.ui.VerticalItemViewHolder;

import java.util.List;


public class NestedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_VERTICAL_ITEM = 1;
    private static final int TYPE_HORIZONTAL_ITEM = 2;
    private Context mContext;
    private List<BaseEntity> mDataList;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public NestedListAdapter(List<BaseEntity> dataList, Context context) {
        mContext = context;
        mDataList = dataList;
        mInflater = LayoutInflater.from(MyApplication.getInstance().getApplicationContext());
    }

    public void setItem(int position, BaseEntity item) {
        mDataList.set(position, item);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VERTICAL_ITEM) {
            return new VerticalItemViewHolder(mInflater.inflate(R.layout.vertical_item_layout, parent, false), mContext);
        } else {
            return new HorizontalItemViewHolder(mInflater.inflate(R.layout.horizontal_item_layout, parent, false), mContext);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HorizontalItemViewHolder) {
            ((HorizontalItemViewHolder) holder).setData(mDataList.get(position));

        }
        if (holder instanceof VerticalItemViewHolder) {
            ((VerticalItemViewHolder) holder).setData(mDataList.get(position));
            ((VerticalItemViewHolder) holder).newsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        NewsEntity entity = (NewsEntity) mDataList.get(position);
        if (entity.getHorizontalNews().size() > 0) {
            return TYPE_HORIZONTAL_ITEM;
        } else {
            return TYPE_VERTICAL_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


}
