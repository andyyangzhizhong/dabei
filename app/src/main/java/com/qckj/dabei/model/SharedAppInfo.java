package com.qckj.dabei.model;

import com.qckj.dabei.util.json.JsonField;

/**
 * {
 * "title": '',
 * "link": '',
 * "iconUrl": '',
 * "describe": '',
 * }
 * <p>
 * 分享app信息
 * <p>
 * Created by yangzhizhong on 2019/4/15.
 */
public class SharedAppInfo {

    @JsonField("title")
    private String title;

    @JsonField("link")
    private String link;

    @JsonField("iconUrl")
    private String iconUrl;

    @JsonField("describe")
    private String describe;

    @JsonField("isCard")
    private boolean isCard;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean isCard() {
        return isCard;
    }

    public void setCard(boolean card) {
        isCard = card;
    }
}
