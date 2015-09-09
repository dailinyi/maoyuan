package com.dcms.cms.statistic;

import com.dcms.cms.entity.ext.CmsConsumeDetail;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.manager.assist.CmsDictionaryMng;
import com.dcms.cms.manager.main.CmsUserMng;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Daily on 2015/9/9.
 */
@Service
public class ConsumeMng {

    public CmsConsumeDetail compute(CmsUser seller ,CmsUser buyer, String price, String useScore){
        CmsConsumeDetail detail = new CmsConsumeDetail().setPrice(ScoreUtils.strToInt(price)).
                setUseScore(ScoreUtils.strToInt(useScore)).setSeller(seller).setBuyer(buyer);



        Byte rate = seller.getRate();
        Integer consumeSellerRate = Integer.valueOf(cmsDictionaryMng.findValue("sellerConsume","返还推荐人比率").getValue());

        Integer returnScore = (detail.getPrice() - detail.getUseScore()) * rate / 100;
        detail.setReturnScore(returnScore);

        //如果当前用户推荐人不是该商家
        if (buyer.getRecommendUser() == null || buyer.getRecommendUser().getId().intValue() != seller.getId()){
            Integer returnSellerScore = detail.getPrice() * consumeSellerRate / 100;
            detail.setReturnSellerScore(returnSellerScore);
        }

        return detail;
    }

    @Resource
    private CmsDictionaryMng cmsDictionaryMng;
    @Resource
    private CmsUserMng cmsUserMng;
}
