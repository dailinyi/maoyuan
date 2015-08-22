package com.dcms.cms.entity.ext.base;

import com.dcms.cms.entity.ext.CmsOrder;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.Content;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dailinyi on 15/8/18.
 */
public class BaseCmsActivityRecord implements Serializable {
    private Integer id;
    private Byte activityType;
    private Date recordTime;
    private Boolean isOffer;
    private CmsUser activityUser;
    private Content activityContent;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getActivityType() {
        return activityType;
    }

    public void setActivityType(Byte activityType) {
        this.activityType = activityType;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Boolean getIsOffer() {
        return isOffer;
    }

    public void setIsOffer(Boolean isOffer) {
        this.isOffer = isOffer;
    }

    public CmsUser getActivityUser() {
        return activityUser;
    }

    public void setActivityUser(CmsUser activityUser) {
        this.activityUser = activityUser;
    }

    public Content getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(Content activityContent) {
        this.activityContent = activityContent;
    }

    public BaseCmsActivityRecord(Byte activityType, Date recordTime, Boolean isOffer, CmsUser activityUser, Content activityContent) {
        this.activityType = activityType;
        this.recordTime = recordTime;
        this.isOffer = isOffer;
        this.activityUser = activityUser;
        this.activityContent = activityContent;
    }

    public BaseCmsActivityRecord() {
    }

    public BaseCmsActivityRecord(Integer id, Byte activityType, Date recordTime, Boolean isOffer, CmsUser activityUser, Content activityContent) {
        this.id = id;
        this.activityType = activityType;
        this.recordTime = recordTime;
        this.isOffer = isOffer;
        this.activityUser = activityUser;
        this.activityContent = activityContent;
    }
}
