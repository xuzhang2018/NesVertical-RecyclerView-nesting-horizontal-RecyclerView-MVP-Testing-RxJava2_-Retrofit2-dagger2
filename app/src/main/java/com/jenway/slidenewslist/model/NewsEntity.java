package com.jenway.slidenewslist.model;

import android.graphics.Bitmap;
import android.os.Parcel;

import java.util.ArrayList;

public class NewsEntity extends BaseEntity {

    public static final int NEWS_ENTITY_INDEX = 0;
    public static final Creator<NewsEntity> CREATOR = new Creator<NewsEntity>() {
        @Override
        public NewsEntity createFromParcel(Parcel in) {
            return new NewsEntity(in, false);
        }

        @Override
        public NewsEntity[] newArray(int size) {
            return new NewsEntity[size];
        }
    };

    private String title;
    private String summary;
    private Bitmap image;
    private String imageUrl;
    private long time;
    private ArrayList<BaseEntity> horizontalNewsList = new ArrayList<>();


    public NewsEntity(int index, String title, String summary, String imageUrl) {
        super(index);
        this.title = title;
        this.summary = summary;
        this.imageUrl = imageUrl;
    }

    protected NewsEntity(Parcel in, boolean isBaseEntityParcelable) {
        super(in, isBaseEntityParcelable);
        title = in.readString();
        summary = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
        imageUrl = in.readString();
        time = in.readLong();
        in.readTypedList(horizontalNewsList, BaseEntity.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(NEWS_ENTITY_INDEX);
        super.writeToParcel(dest, i);
        dest.writeString(title);
        dest.writeString(summary);
        dest.writeParcelable(image, i);
        dest.writeString(imageUrl);
        dest.writeLong(time);
        dest.writeTypedList(horizontalNewsList);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageYrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public ArrayList<BaseEntity> getHorizontalNews() {
        return horizontalNewsList;
    }

    public void setHorizontalNews(ArrayList<BaseEntity> horizontalNewsList) {
        this.horizontalNewsList = horizontalNewsList;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
