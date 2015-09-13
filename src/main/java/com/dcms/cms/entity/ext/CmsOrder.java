package com.dcms.cms.entity.ext;

import com.dcms.cms.entity.ext.base.BaseCmsOrder;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.statistic.utils.ScoreUtils;

import java.util.Date;

/**
 * Created by dailinyi on 15/8/18.
 */
public class CmsOrder extends BaseCmsOrder {
    public CmsOrder() {
    }

    public CmsOrder(String id, Integer price, Byte rate, String key, CmsUser buyer, CmsUser seller, Integer score) {
        super(id,new Date(), new Date(), price, rate, key, buyer, seller, score);
    }

    public String getPriceShow(){
        return ScoreUtils.intToStr(getPrice());
    }

    public String getScoreShow(){
        return ScoreUtils.intToStr(getScore());
    }
}
