package com.ethopia.tradecabinet.app.management.user.dto;

import java.util.List;

public class UserListResponse {

    private List<UserListResponseItem> data;
    private int page;
    private int totalPage;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<UserListResponseItem> getData() {
        return data;
    }

    public void setData(List<UserListResponseItem> data) {
        this.data = data;
    }
}
