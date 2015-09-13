package com.dcms.cms.manager.ext.impl;

import com.dcms.cms.entity.ext.CmsConsumeDetail;
import com.dcms.cms.entity.ext.CmsOrder;
import com.dcms.cms.entity.ext.CmsScoreRecord;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.manager.ext.CmsConsumeMng;
import com.dcms.cms.manager.ext.CmsOrderMng;
import com.dcms.cms.manager.ext.CmsScoreRecordMng;
import com.dcms.cms.manager.main.CmsUserMng;
import com.dcms.cms.statistic.utils.KeyCodeMng;
import com.dcms.cms.statistic.utils.OrderUtils;
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
        CmsOrder order = new CmsOrder(OrderUtils.getNewOrderId(buyer,seller),detail.getPrice(),seller.getRate(), KeyCodeMng.getNewCode(),buyer,seller,detail.getUseScore());
        cmsOrderMng.save(order);

        //增加积分记录
        if (detail != null && detail.getReturnScore() != 0){
            scoreRecordMng.save(new CmsScoreRecord(CmsScoreRecord.ScoreTypeEnum.BUY_GOODS.getValue().byteValue(),detail.getReturnScore(),order,buyer,seller));
            seller.setScoreCount(seller.getScoreCount() - detail.getReturnScore());
            buyer.setScoreCount(buyer.getScoreCount() + detail.getReturnScore());
            cmsUserMng.updateUser(buyer);
            cmsUserMng.updateUser(seller);
        }

        //给推荐人增加积分记录
        if (detail != null && detail.getReturnSellerScore() != 0){
            CmsUser recommendUser = buyer.getRecommendUser();
            scoreRecordMng.save(new CmsScoreRecord(CmsScoreRecord.ScoreTypeEnum.BUY_GOODS_TO_UP.getValue().byteValue(),detail.getReturnSellerScore(),order,recommendUser,seller));
            seller.setScoreCount(seller.getScoreCount() - detail.getReturnSellerScore());
            recommendUser.setScoreCount(recommendUser.getScoreCount() + detail.getReturnSellerScore());
            cmsUserMng.updateUser(seller);
            cmsUserMng.updateUser(recommendUser);
        }

    }
    @Resource
    private CmsUserMng cmsUserMng;
    @Resource
    private CmsScoreRecordMng scoreRecordMng;
    @Resource
    private CmsOrderMng cmsOrderMng;
}
