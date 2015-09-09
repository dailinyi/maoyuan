package com.dcms.cms.entity.ext.base;

import com.dcms.cms.entity.main.CmsUser;

import java.util.Date;

/**
 * Created by dailinyi on 15/8/18.
 */
public class BaseCmsOrder {
    private String id;
    private Date orderTime;
    private Date lastUpdateTime;
    private Integer price;
    private Byte rate;
    private String key;
    private CmsUser buyer;
    private CmsUser seller;
    private CmsUser score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Byte getRate() {
        return rate;
    }

    public void setRate(Byte rate) {
        this.rate = rate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public CmsUser getBuyer() {
        return buyer;
    }

    public void setBuyer(CmsUser buyer) {
        this.buyer = buyer;
    }

    public CmsUser getSeller() {
        return seller;
    }

    public void setSeller(CmsUser seller) {
        this.seller = seller;
    }

    public CmsUser getScore() {
        return score;
    }

    public BaseCmsOrder setScore(CmsUser score) {
        this.score = score;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseCmsOrder cmsOrder = (BaseCmsOrder) o;

        if (id != null ? !id.equals(cmsOrder.id) : cmsOrder.id != null) return false;
        if (key != null ? !key.equals(cmsOrder.key) : cmsOrder.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }

    public BaseCmsOrder() {
    }

    public BaseCmsOrder(String id, Date orderTime, Date lastUpdateTime, Integer price, Byte rate, String key, CmsUser buyer, CmsUser seller, CmsUser score) {
        this.id = id;
        this.orderTime = orderTime;
        this.lastUpdateTime = lastUpdateTime;
        this.price = price;
        this.rate = rate;
        this.key = key;
        this.buyer = buyer;
        this.seller = seller;
        this.score = score;
    }
}
