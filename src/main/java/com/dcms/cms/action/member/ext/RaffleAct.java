package com.dcms.cms.action.member.ext;

import com.dcms.cms.entity.ext.CmsActivityRecord;
import com.dcms.cms.entity.ext.CmsScoreRecord;
import com.dcms.cms.entity.main.CmsSite;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.Content;
import com.dcms.cms.entity.main.MemberConfig;
import com.dcms.cms.manager.assist.CmsDictionaryMng;
import com.dcms.cms.manager.ext.CmsActivityRecordMng;
import com.dcms.cms.manager.ext.CmsRaffleMng;
import com.dcms.cms.manager.ext.CmsScoreRecordMng;
import com.dcms.cms.manager.main.CmsUserMng;
import com.dcms.cms.manager.main.ContentMng;
import com.dcms.cms.web.CmsUtils;
import com.dcms.cms.web.FrontUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
        CmsActivityRecord record1 = activityRecordMng.findById(1);
        System.out.println(record1);
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
        user = userMng.findById(user.getId());

        //先扣除用户积分
        int cost = Integer.valueOf(dicMng.findValue("raffle","consumeScore").getValue());
        if (user.getScoreCount() == null ||user.getScoreCount() < cost){
            return FrontUtils.showMessage(request, model, "raffle.scoreNotEnough");
        }
        Content gift = raffleMng.doEggRaffle(user);

        if (gift != null){
            model.addAttribute("raffle",gift.getTitle());
        } else {
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
    @Resource
    private CmsRaffleMng raffleMng;

}
