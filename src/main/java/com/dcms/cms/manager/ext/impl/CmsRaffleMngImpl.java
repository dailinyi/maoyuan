package com.dcms.cms.manager.ext.impl;

import com.dcms.cms.entity.ext.CmsActivityRecord;
import com.dcms.cms.entity.ext.CmsScoreRecord;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.Content;
import com.dcms.cms.manager.assist.CmsDictionaryMng;
import com.dcms.cms.manager.ext.CmsActivityRecordMng;
import com.dcms.cms.manager.ext.CmsRaffleMng;
import com.dcms.cms.manager.ext.CmsScoreRecordMng;
import com.dcms.cms.manager.main.CmsUserMng;
import com.dcms.cms.manager.main.ContentMng;
import com.dcms.cms.web.FrontUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional(rollbackFor=Exception.class)
public class CmsRaffleMngImpl implements CmsRaffleMng{

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Content doEggRaffle(CmsUser user){
        int cost = Integer.valueOf(dicMng.findValue("raffle","consumeScore").getValue());
        user.setScoreCount(user.getScoreCount() - cost);
        userMng.updateUser(user);

        //增加一条消耗积分的记录
        CmsScoreRecord record  = new CmsScoreRecord(CmsScoreRecord.ScoreTypeEnum.EGG_RAFFLE_COST.getValue().byteValue(),0-cost,userMng.findById(1),user);
        scoreRecordMng.save(record);


        //获取活动奖品
        String channelId = dicMng.findValue("raffle","channelId").getValue();
        String total = dicMng.findValue("raffle","total").getValue();


        List<Content> raffles = contentMng.getContentByChnId(Integer.valueOf(channelId),Integer.valueOf(total));

        //拼凑奖池
        List<Integer> rafflePool = new ArrayList<Integer>(1000);
        for (Content raffle : raffles){
            String probability = raffle.getAttr().get("probability");
            int i = Integer.valueOf(probability);

            for (int temp = 0 ; temp < i ; temp ++){
                rafflePool.add(raffle.getId());
            }
        }

        //拼抽奖号
        int number = new Random().nextInt(1000);
        Integer prize = null;
        Content gift = null;
        if (number < rafflePool.size() - 1 ){
            prize = rafflePool.get(number);
            gift = contentMng.findById(prize);

            //记录抽奖状态
            CmsActivityRecord cmsActivityRecord = new CmsActivityRecord(user, gift);

            String scoreCount = gift.getAttr().get("scoreCount");
            if (StringUtils.isNotBlank(scoreCount) && !Integer.valueOf(scoreCount).equals(0)){
                CmsScoreRecord win  = new CmsScoreRecord(CmsScoreRecord.ScoreTypeEnum.EGG_RAFFLE_REWARDS.getValue().byteValue(),Integer.valueOf(scoreCount),userMng.findById(1),user);
                scoreRecordMng.save(win);
                user.setScoreCount(user.getScoreCount() + Integer.valueOf(scoreCount));
                userMng.updateUser(user);
                cmsActivityRecord.setIsOffer(true);
            }

            activityRecordMng.save(cmsActivityRecord);

            return gift;
        } else {
            //记录抽奖状态
            CmsActivityRecord cmsActivityRecord = new CmsActivityRecord(user);
            activityRecordMng.save(cmsActivityRecord);
            return null;
        }
    }

    @Resource
    private CmsUserMng userMng;
    @Resource
    private ContentMng contentMng;
    @Resource
    private CmsDictionaryMng dicMng;
    @Resource
    private CmsScoreRecordMng scoreRecordMng;
    @Resource
    private CmsActivityRecordMng activityRecordMng;

}