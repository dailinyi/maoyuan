package com.dcms.cms.action.member.ext;

import com.dcms.cms.entity.ext.CmsActivityRecord;
import com.dcms.cms.entity.ext.CmsScoreRecord;
import com.dcms.cms.entity.main.CmsSite;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.Content;
import com.dcms.cms.entity.main.MemberConfig;
import com.dcms.cms.manager.assist.CmsDictionaryMng;
import com.dcms.cms.manager.ext.CmsActivityRecordMng;
import com.dcms.cms.manager.ext.CmsScoreRecordMng;
import com.dcms.cms.manager.main.CmsUserMng;
import com.dcms.cms.manager.main.ContentMng;
import com.dcms.cms.web.CmsUtils;
import com.dcms.cms.web.FrontUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.dcms.cms.Constants.TPLDIR_RAFFLE;

/**
 * Created by dailinyi on 15/8/18.
 */
@Controller
public class RaffleAct {
    private static final Logger log = LoggerFactory.getLogger(RaffleAct.class);

    public static final String EGG = "tpl.egg";

    @RequestMapping(value = "/raffle/egg.jspx" , method = RequestMethod.GET)
    public String toEggPage(HttpServletRequest request , ModelMap model){
        CmsSite site = CmsUtils.getSite(request);

        FrontUtils.frontData(request, model, site);
        return FrontUtils.getTplPath(request, site.getSolutionPath(),
                TPLDIR_RAFFLE, EGG);
    }

    @RequestMapping(value = "/raffle/egg.jspx" , method = RequestMethod.POST)
    public String doEggRaffle(HttpServletRequest request , ModelMap model){
        CmsSite site = CmsUtils.getSite(request);
        CmsUser user = CmsUtils.getUser(request);
        MemberConfig mcfg = site.getConfig().getMemberConfig();
        // 没有开启会员功能
        if (!mcfg.isMemberOn()) {
            return FrontUtils.showMessage(request, model, "member.memberClose");
        }
        if (user == null) {
            return FrontUtils.showLogin(request, model, site);
        }
        CmsActivityRecord record1 = activityRecordMng.findById(1);
        user = userMng.findById(user.getId());

        //先扣除用户积分
        int cost = Integer.valueOf(dicMng.findValue("raffle","consumeScore").getValue());
        if (user.getScoreCount() == null ||user.getScoreCount() < cost){
            return FrontUtils.showMessage(request, model, "raffle.scoreNotEnough");
        }
        user.setScoreCount(user.getScoreCount() - cost);
        userMng.updateUser(user);

        //增加一条消耗积分的记录
        CmsScoreRecord record  = new CmsScoreRecord(CmsScoreRecord.ScoreTypeEnum.EGG_RAFFLE_COST.getValue().byteValue(),0-cost,userMng.findById(1),user);
        scoreRecordMng.save(record);

        //在抽奖表中增加一条抽奖记录



        //获取活动奖品
        String channelId = dicMng.findValue("raffle","channelId").getValue();
        String total = dicMng.findValue("raffle","total").getValue();


        List<Content> raffles = contentMng.getContentByChnId(Integer.valueOf(channelId),Integer.valueOf(total));

        //拼凑奖池
        List<Integer> rafflePool = new ArrayList<Integer>(1000);
        for (Content raffle : raffles){
            String probability = raffle.getAttr().get("probability");
            int i = 0;
            try {
                i = Integer.parseInt(probability);
            } catch (NumberFormatException e){
                return FrontUtils.showMessage(request, model, "global.configError");
            }
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
            if (StringUtils.isNotBlank(scoreCount)){
                CmsScoreRecord win  = new CmsScoreRecord(CmsScoreRecord.ScoreTypeEnum.EGG_RAFFLE_REWARDS.getValue().byteValue(),Integer.valueOf(scoreCount),userMng.findById(1),user);
                scoreRecordMng.save(win);
                user.setScoreCount(user.getScoreCount() + Integer.valueOf(scoreCount));
                userMng.updateUser(user);
                cmsActivityRecord.setIsOffer(true);
            }

            activityRecordMng.save(cmsActivityRecord);


            model.addAttribute("raffle",gift.getTitle());
        } else {
            //记录抽奖状态
            CmsActivityRecord cmsActivityRecord = new CmsActivityRecord(user);
            activityRecordMng.save(cmsActivityRecord);
            model.addAttribute("raffle","0");
        }



        FrontUtils.frontData(request, model, site);
        return FrontUtils.getTplPath(request, site.getSolutionPath(),
                TPLDIR_RAFFLE, EGG);
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
