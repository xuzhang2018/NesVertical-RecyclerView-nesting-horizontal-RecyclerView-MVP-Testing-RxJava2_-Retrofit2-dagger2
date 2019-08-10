package com.jenway.slidenewslist.ui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jenway.slidenewslist.R;
import com.jenway.slidenewslist.model.BaseEntity;
import com.jenway.slidenewslist.model.NewsEntity;

public class InnerItemViewHolder extends RecyclerView.ViewHolder {
    TextView newsSummary;
    ImageView newsImage;

    private Context context;

    public InnerItemViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        newsImage = itemView.findViewById(R.id.inner_item_image);
        newsSummary = itemView.findViewById(R.id.inner_item_summary);

    }

    public void setData(final BaseEntity data) {

        NewsEntity entity = (NewsEntity) data;
        newsSummary.setText(entity.getSummary());
        newsImage.setImageAlpha(127);
        if (null == entity.getImage()) {
            Glide.with(context).clear(newsImage);
            newsImage.setImageBitmap(null);
        } else {
            Glide.with(context)
                    .load(entity.getImage())
                    .into(newsImage);
        }
    }
}
