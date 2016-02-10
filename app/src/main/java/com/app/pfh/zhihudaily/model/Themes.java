package com.app.pfh.zhihudaily.model;

/**主题列表
 * limit : 返回数目之限制（仅为猜测）
 subscribed : 已订阅条目
 others : 其他条目
 */
public class Themes {
    private int limit;
    private String subscribed;
    private Theme_others others;


    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(String subscribed) {
        this.subscribed = subscribed;
    }

    public Theme_others getOthers() {
        return others;
    }

    public void setOthers(Theme_others others) {
        this.others = others;
    }
}
