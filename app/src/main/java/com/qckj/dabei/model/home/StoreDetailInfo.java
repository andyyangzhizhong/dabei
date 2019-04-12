package com.qckj.dabei.model.home;


import com.qckj.dabei.util.json.JsonField;

import java.util.List;

/**
 * F_C_ADDRESS : 玉厂路284号家园内
 * F_C_DPNAME : 紫薇汽车用品商行
 * F_C_FMIMG : http://dabei.oss-cn-shenzhen.aliyuncs.com/WX/dianpu/20180721114921774-23F495B2-9115-4A4F-A1EF-A1A5709C59C9.jpeg
 * F_C_ID : 20180718141159YtQFOEDXAlXzdqZ1Yk
 * F_C_IMGS :
 * F_C_JJPHONE : 085185117127
 * F_C_X : 106.705254
 * F_C_Y : 26.5598
 * F_D_ISQIYE : 1.0
 * JULI : 7502.468
 * URL :
 * goods : [{"F_C_ID":"20180723093810CsA1jlOJl5qFGr5qMr02a5Ds7mwozfkqHFonT3IJFg7slgsZmx","
 * F_C_SPFMIMG":"http://dabei.oss-cn-shenzhen.aliyuncs.com/WX/spgl/2018072393741763-8093D948-1376-41FF-AEAE-5E8C4F07D50A.jpeg","
 * F_C_SPNAME":"啦啦啦","F_I_MONEY":2558},{"F_C_ID":"20180721114409LXRTypV18zALG1eobak96BwpanayfvSISa6HbKYnvVZgOPg4MA",
 * "F_C_SPFMIMG":"http://dabei.oss-cn-shenzhen.aliyuncs.com/WX/spgl/20180721114316520-1AD726B3-1FCA-4397-9005-E9F3601CC25E.jpeg",
 * "F_C_SPNAME":"5","F_I_MONEY":20}]
 * <p>
 * 店铺详情
 * <p>
 * Created by yangzhizhong on 2019/4/11.
 */
public class StoreDetailInfo {
    @JsonField("F_C_ADDRESS")
    private String address;

    @JsonField("F_C_DPNAME")
    private String name;

    @JsonField("F_C_FMIMG")
    private String imgUrl;

    @JsonField("F_C_ID")
    private String id;

    @JsonField("F_C_JJPHONE")
    private String phone;

    @JsonField("F_C_X")
    private String longitude;

    @JsonField("F_C_Y")
    private String latitude;

    @JsonField("JULI")
    private String distance;

    @JsonField("goods")
    private List<ServiceInfo> serviceInfos;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<ServiceInfo> getServiceInfos() {
        return serviceInfos;
    }

    public void setServiceInfos(List<ServiceInfo> serviceInfos) {
        this.serviceInfos = serviceInfos;
    }

    public static class ServiceInfo {
        @JsonField("F_C_ID")
        private String id;

        @JsonField("F_C_SPFMIMG")
        private String imgUrl;

        @JsonField("F_C_SPNAME")
        private String name;

        @JsonField("F_I_MONEY")
        private String money;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
