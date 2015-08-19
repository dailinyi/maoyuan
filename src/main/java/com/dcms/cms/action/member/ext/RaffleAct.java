package com.dcms.cms.action.member.ext;

import com.dcms.cms.entity.main.CmsSite;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.Content;
import com.dcms.cms.entity.main.MemberConfig;
import com.dcms.cms.manager.assist.CmsDictionaryMng;
import com.dcms.cms.manager.ext.CmsRaffleMng;
import com.dcms.cms.manager.main.ContentMng;
import com.dcms.cms.web.CmsUtils;
import com.dcms.cms.web.FrontUtils;
import com.dcms.common.page.Pagination;
import com.dcms.common.web.CookieUtils;
import com.dcms.common.web.springmvc.MessageResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import static com.dcms.common.page.SimplePage.cpn;

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

        //先扣除用户积分


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
        if (number < rafflePool.size() - 1 ){
            prize = rafflePool.get(number);
            Content gift = contentMng.findById(prize);
            model.addAttribute("raffle",gift.getTitle());
        } else {
            model.addAttribute("raffle","0");
        }

        FrontUtils.frontData(request, model, site);
        return FrontUtils.getTplPath(request, site.getSolutionPath(),
                TPLDIR_RAFFLE, EGG);
    }

    @Resource
    private CmsRaffleMng cmsRaffleMng;
    @Resource
    private ContentMng contentMng;
    @Resource
    private CmsDictionaryMng dicMng;

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>(1000);
        System.out.println(list.get(999));
    }
}