package com.ogz.mailassistance.dto;

import java.util.List;

public class SendMailDto {
    private String content;
    private String title;
    private List<String> toUserList;

    public SendMailDto(String content, String title, List<String> toUserList) {
        this.content = content;
        this.title = title;
        this.toUserList = toUserList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getToUserList() {
        return toUserList;
    }

    public void setToUserList(List<String> toUserList) {
        this.toUserList = toUserList;
    }
}