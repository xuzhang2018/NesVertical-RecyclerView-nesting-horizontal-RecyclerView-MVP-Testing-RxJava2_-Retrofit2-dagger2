package com.jenway.slidenewslist.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.jenway.slidenewslist.MyApplication;
import com.jenway.slidenewslist.R;
import com.jenway.slidenewslist.model.BaseEntity;
import com.jenway.slidenewslist.model.NewsEntity;

import java.util.List;


/**
 * by Xu
 * Description: to show the breed info
 */

public class HorizontalItemViewHolder extends RecyclerView.ViewHolder {
    HorizontalRecyclerView recyclerView;

    private Context context;

    public HorizontalItemViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        recyclerView = itemView.findViewById(R.id.horizontal_rv_item);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

    }

    public void setData(final BaseEntity data) {
        InnerAdapter innerAdapter = new InnerAdapter(((NewsEntity) data).getHorizontalNews(), context);
        recyclerView.setAdapter(innerAdapter);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class InnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;
        private List<BaseEntity> mDataList;
        private LayoutInflater mInflater;
        private OnItemClickListener mOnItemClickListener;


        public InnerAdapter(List<BaseEntity> dataList, Context context) {
            mContext = context;
            mDataList = dataList;
            mInflater = LayoutInflater.from(MyApplication.getInstance().getApplicationContext());
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new InnerItemViewHolder(mInflater.inflate(R.layout.inner_item_layout, parent, false), mContext);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            ((InnerItemViewHolder) holder).setData(mDataList.get(position));
            ((InnerItemViewHolder) holder).newsSummary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }
    }
}
