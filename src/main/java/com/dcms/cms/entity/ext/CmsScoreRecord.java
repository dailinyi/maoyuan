package com.dcms.cms.entity.ext;

import com.dcms.cms.entity.ext.base.BaseCmsScoreRecord;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.statistic.utils.ScoreUtils;

import java.util.Date;

/**
 * Created by dailinyi on 15/8/18.
 */
public class CmsScoreRecord extends BaseCmsScoreRecord {
    /**
     * 积分明细类别
     */
    public static enum ScoreTypeEnum {
        /* 充值 */
        CHARGE_SCORE(1),
        /* 买家购买商品,卖家返买家的积分 */
        BUY_GOODS(2),
        /* 卖价购买商品,卖家返买家推荐人积分 */
        BUY_GOODS_TO_UP(3),
        /* 买家退回商品,买家返卖家积分 */
        REFUND_GOODS(4),
        /* 买家退回商品,买家推荐人返卖家积分 */
        REFUND_GOODS_TO_UP(5),
        /* 砸蛋抽奖消耗 */
        EGG_RAFFLE_COST(6),
        /* 砸蛋抽奖奖励 */
        EGG_RAFFLE_REWARDS(7);

        private Integer value ;
        ScoreTypeEnum(Integer value) {
            this.value = value;
        }

        public Integer getValue(){
            return value;
        }

        public ScoreTypeEnum findByType(Integer value){
            if (value.equals(CHARGE_SCORE.getValue())){
                return CHARGE_SCORE;
            } else if (value.equals(BUY_GOODS.getValue())){
                return BUY_GOODS;
            } else if (value.equals(BUY_GOODS_TO_UP.getValue())){
                return BUY_GOODS_TO_UP;
            } else if (value.equals(REFUND_GOODS.getValue())){
                return REFUND_GOODS;
            } else if (value.equals(REFUND_GOODS_TO_UP.getValue())){
                return REFUND_GOODS_TO_UP;
            } else if (value.equals(EGG_RAFFLE_COST.getValue())){
                return EGG_RAFFLE_COST;
            } else if (value.equals(EGG_RAFFLE_REWARDS.getValue())){
                return EGG_RAFFLE_REWARDS;
            } else {
                return null;
            }
        }

    }

    public String getScoreNoShow(){

        return ScoreUtils.intToStr(getScoreNum());
    }

    public CmsScoreRecord(Byte recordType, Date recordTime, Integer scoreNum, CmsOrder order, CmsUser receiveUser, CmsUser sendUser) {
        super(recordType, recordTime, scoreNum, order, receiveUser, sendUser);
    }

    public CmsScoreRecord(Byte recordType,  Integer scoreNum, CmsOrder order, CmsUser receiveUser, CmsUser sendUser) {
        super(recordType, new Date(), scoreNum, order, receiveUser, sendUser);
    }

    public CmsScoreRecord(Byte recordType,  Integer scoreNum, CmsUser receiveUser, CmsUser sendUser) {
        super(recordType, new Date(), scoreNum, null, receiveUser, sendUser);
    }


    public CmsScoreRecord() {
        super();
    }
}
