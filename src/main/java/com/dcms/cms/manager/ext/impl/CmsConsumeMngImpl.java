package com.dcms.cms.manager.ext.impl;

import com.dcms.cms.entity.ext.CmsConsumeDetail;
import com.dcms.cms.entity.ext.CmsOrder;
import com.dcms.cms.entity.ext.CmsScoreRecord;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.manager.ext.CmsConsumeMng;
import com.dcms.cms.manager.ext.CmsScoreRecordMng;
import com.dcms.cms.manager.main.CmsUserMng;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Daily on 2015/9/10.
 */
@Service
public class CmsConsumeMngImpl implements CmsConsumeMng{

    @Override
    public void consume(Integer sellerId, Integer buyerId, CmsConsumeDetail detail) {
        CmsUser seller = cmsUserMng.findById(sellerId);
        CmsUser buyer = cmsUserMng.findById(buyerId);
        //创建订单
        CmsOrder order = new CmsOrder();

        //增加积分记录
        if (detail != null && detail.getReturnScore() != 0){
            scoreRecordMng.save(new CmsScoreRecord(CmsScoreRecord.ScoreTypeEnum.BUY_GOODS.getValue().byteValue(),detail.getReturnScore(),buyer,seller));
            seller.setScoreCount(seller.getScoreCount() - detail.getReturnScore());
            buyer.setScoreCount(buyer.getScoreCount() + detail.getReturnScore());
        }

    }
    @Resource
    private CmsUserMng cmsUserMng;
    @Resource
    private CmsScoreRecordMng scoreRecordMng;
}
