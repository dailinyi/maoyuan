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
}
