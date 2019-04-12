package com.qckj.dabei.model.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.util.json.JsonField;

/**
 * "F_C_ID": "dbs_2018050810520997",
 * "F_C_IMG": "http://dabei.oss-cn-shenzhen.aliyuncs.com/upload/system/20180508110659wakY9fZXgRRreUbEEP.png",
 * "category": "汽车"
 * <p>
 * 首页精品推荐信息
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class HomeBoutiqueRecommendInfo {

    @JsonField("F_C_ID")
    private String id;

    @JsonField("F_C_IMG")
    private String imgUrl;

    @JsonField("category")
    private String categoryName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @NonNull
    @Override
    public String toString() {
        return "HomeBoutiqueRecommendInfo{" +
                "id='" + id + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
