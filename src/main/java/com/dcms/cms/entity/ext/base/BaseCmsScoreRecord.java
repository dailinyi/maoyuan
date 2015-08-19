package com.dcms.cms.entity.ext.base;

import com.dcms.cms.entity.ext.CmsOrder;
import com.dcms.cms.entity.main.CmsUser;

import java.util.Date;

/**
 * Created by dailinyi on 15/8/18.
 */
public class BaseCmsScoreRecord {
    private Integer id;
    private Byte recordType;
    private Date recordTime;
    private Integer scoreNum;
    private CmsOrder order;
    private CmsUser receiveUser;
    private CmsUser sendUser;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getRecordType() {
        return recordType;
    }

    public void setRecordType(Byte recordType) {
        this.recordType = recordType;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getScoreNum() {
        return scoreNum;
    }

    public void setScoreNum(Integer scoreNum) {
        this.scoreNum = scoreNum;
    }

    public CmsOrder getOrder() {
        return order;
    }

    public void setOrder(CmsOrder order) {
        this.order = order;
    }

    public CmsUser getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(CmsUser receiveUser) {
        this.receiveUser = receiveUser;
    }

    public CmsUser getSendUser() {
        return sendUser;
    }

    public void setSendUser(CmsUser sendUser) {
        this.sendUser = sendUser;
    }

    public BaseCmsScoreRecord(Integer id, Byte recordType, Date recordTime, Integer scoreNum, CmsOrder order, CmsUser receiveUser, CmsUser sendUser) {
        this.id = id;
        this.recordType = recordType;
        this.recordTime = recordTime;
        this.scoreNum = scoreNum;
        this.order = order;
        this.receiveUser = receiveUser;
        this.sendUser = sendUser;
    }

    public BaseCmsScoreRecord(Byte recordType, Date recordTime, Integer scoreNum, CmsOrder order, CmsUser receiveUser, CmsUser sendUser) {
        this.recordType = recordType;
        this.recordTime = recordTime;
        this.scoreNum = scoreNum;
        this.order = order;
        this.receiveUser = receiveUser;
        this.sendUser = sendUser;
    }

    public BaseCmsScoreRecord() {
    }
}
