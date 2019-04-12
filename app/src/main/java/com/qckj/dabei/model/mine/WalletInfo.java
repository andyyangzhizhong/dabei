package com.qckj.dabei.model.mine;

import com.qckj.dabei.util.json.JsonField;

/**
 * balance : 1000.0
 * count : 1.0
 * point : 0.0
 * userid : 20180727153847pnqW8km0fCfTfVl6f9
 * <p>
 * 钱包信息
 * <p>
 * Created by yangzhizhong on 2019/4/11.
 */
public class WalletInfo {

    @JsonField("balance")
    private String balance;

    @JsonField("count")
    private String count;

    @JsonField("point")
    private String point;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
