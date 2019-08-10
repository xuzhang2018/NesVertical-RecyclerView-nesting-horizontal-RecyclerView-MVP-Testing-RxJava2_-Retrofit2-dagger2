package com.jenway.slidenewslist.model;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class BaseEntity implements Parcelable {

    public static final Creator<BaseEntity> CREATOR = new Creator<BaseEntity>() {
        @Override
        public BaseEntity createFromParcel(Parcel in) {
            return BaseEntity.getConcreteClass(in);
        }

        @Override
        public BaseEntity[] newArray(int size) {
            return new BaseEntity[size];
        }
    };

    private int index;

    public BaseEntity(int index) {
        this.index = index;
    }

    protected BaseEntity(Parcel in, boolean isBaseEntityParcelable) {
        if (!isBaseEntityParcelable) {
            in.readInt();
        }
        index = in.readInt();
    }

    //to get the right class for Parcelable process
    private static BaseEntity getConcreteClass(Parcel source) {
        switch (source.readInt()) {
            case NewsEntity.NEWS_ENTITY_INDEX:
                return new NewsEntity(source, true);
            default:
                throw new IllegalArgumentException("Fail to create parcelable for base entity");
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
