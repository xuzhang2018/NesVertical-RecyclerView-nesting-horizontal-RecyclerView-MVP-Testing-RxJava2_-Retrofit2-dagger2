package com.jenway.slidenewslist.network.bean;

import java.util.ArrayList;

public class NetData {
    private String totalItems;
    private ArrayList<Content> content;

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    public ArrayList<Content> getContent() {
        return content;
    }

    public void setContent(ArrayList<Content> content) {
        this.content = content;
    }
}
