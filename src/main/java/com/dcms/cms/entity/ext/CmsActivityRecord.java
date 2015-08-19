package com.dcms.cms.entity.ext;

import com.dcms.cms.entity.ext.base.BaseCmsActivityRecord;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.Content;

import java.util.Date;

/**
 * Created by dailinyi on 15/8/19.
 */
public class CmsActivityRecord extends BaseCmsActivityRecord{
    /**
     * 积分明细类别
     */
    public static enum ScoreTypeEnum {
        /* 充值 */
        EGG(1);

        private Integer value ;
        ScoreTypeEnum(Integer value) {
            this.value = value;
        }

        public Integer getValue(){
            return value;
        }

        public ScoreTypeEnum findByType(Integer value){
            if (value.equals(EGG.getValue())){
                return EGG;
            }  else {
                return null;
            }
        }

    }
    public CmsActivityRecord(Byte activityType, Date recordTime, Boolean isOffer, CmsUser activityUser, Content activityContent) {
        super(activityType, recordTime, isOffer, activityUser, activityContent);
    }

    public CmsActivityRecord() {
    }

    public CmsActivityRecord( CmsUser activityUser, Content activityContent) {
        super(ScoreTypeEnum.EGG.getValue().byteValue(), new Date(), false, activityUser, activityContent);
    }

    public CmsActivityRecord(  CmsUser activityUser) {
        super(ScoreTypeEnum.EGG.getValue().byteValue(), new Date(), true, activityUser, null);
    }

    public CmsActivityRecord(Integer id, Byte activityType, Date recordTime, Boolean isOffer, CmsUser activityUser, Content activityContent) {
        super(id, activityType, recordTime, isOffer, activityUser, activityContent);
    }
}
