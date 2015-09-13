package com.dcms.cms.action.member.ext;

import com.dcms.cms.entity.ext.CmsConsumeDetail;
import com.dcms.cms.entity.main.CmsSite;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.MemberConfig;
import com.dcms.cms.manager.ext.CmsConsumeMng;
import com.dcms.cms.manager.main.CmsUserMng;
import com.dcms.cms.statistic.utils.ConsumeMng;
import com.dcms.cms.web.CmsUtils;
import com.dcms.cms.web.FrontUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.dcms.cms.Constants.TPLDIR_SELLER_MEMBER;

/**
 * Created by Daily on 2015/9/9.
 */
@Controller
public class ConsumeAct {
    public static final String MEMBER_CONSUME = "tpl.memberConsume";
    public static final String MEMBER_AJAX = "tpl.memberConsumeAjax";

    @RequestMapping(value = "/seller/consume.jspx" , method = RequestMethod.GET)
    public String toConsume(HttpServletRequest request,ModelMap model){
        CmsSite site = CmsUtils.getSite(request);
        CmsUser user = CmsUtils.getUser(request);
        FrontUtils.frontData(request, model, site);
        MemberConfig mcfg = site.getConfig().getMemberConfig();
        // 没有开启会员功能
        if (!mcfg.isMemberOn()) {
            return FrontUtils.showMessage(request, model, "member.memberClose");
        }
        if (user == null) {
            return FrontUtils.showLogin(request, model, site);
        }

        return FrontUtils.getTplPath(request, site.getSolutionPath(),
                TPLDIR_SELLER_MEMBER, MEMBER_CONSUME);
    }

    @RequestMapping(value = "/seller/preConsume.jspx")
    public String preConsume(String buyerName ,String price, String useScore ,HttpServletRequest request,ModelMap model){
        CmsSite site = CmsUtils.getSite(request);
        CmsUser user = CmsUtils.getUser(request);
        FrontUtils.frontData(request, model, site);
        MemberConfig mcfg = site.getConfig().getMemberConfig();
        // 没有开启会员功能
        if (!mcfg.isMemberOn()) {
            return FrontUtils.showMessage(request, model, "member.memberClose");
        }
        if (user == null) {
            return FrontUtils.showLogin(request, model, site);
        }

        CmsUser buyer = cmsUserMng.findByUsername(buyerName);
        if (buyer == null || buyer.getId() == null) {
            model.addAttribute("error","查询不到此用户");
            return FrontUtils.getTplPath(request, site.getSolutionPath(),
                    TPLDIR_SELLER_MEMBER, MEMBER_AJAX);
        }

        CmsConsumeDetail result = consumeMng.compute(user, buyer, price, useScore);

        if (user.getScoreCount() + result.getUseScore() - result.getReturnScore() - result.getReturnSellerScore() < 0 ){
            model.addAttribute("error","账户余额不足,请联系客服充值!");
            return FrontUtils.getTplPath(request, site.getSolutionPath(),
                    TPLDIR_SELLER_MEMBER, MEMBER_AJAX);
        }

        model.addAttribute("consumeDetail",result);

        return FrontUtils.getTplPath(request, site.getSolutionPath(),
                TPLDIR_SELLER_MEMBER, MEMBER_AJAX);
    }

    @RequestMapping(value = "/seller/consume.jspx"  , method = RequestMethod.POST)
    public String consume(String buyerName ,String price, String useScore ,HttpServletRequest request,HttpServletResponse response,ModelMap model){
        CmsSite site = CmsUtils.getSite(request);
        CmsUser user = CmsUtils.getUser(request);
        FrontUtils.frontData(request, model, site);
        MemberConfig mcfg = site.getConfig().getMemberConfig();
        // 没有开启会员功能
        if (!mcfg.isMemberOn()) {
            return FrontUtils.showMessage(request, model, "member.memberClose");
        }
        if (user == null) {
            return FrontUtils.showLogin(request, model, site);
        }

        CmsUser buyer = cmsUserMng.findByUsername(buyerName);
        if (buyer == null || buyer.getId() == null) {
            model.addAttribute("error","查询不到此用户");
            return FrontUtils.getTplPath(request, site.getSolutionPath(),
                    TPLDIR_SELLER_MEMBER, MEMBER_AJAX);
        }

        CmsConsumeDetail result = consumeMng.compute(user, buyer, price, useScore);

        if (user.getScoreCount() + result.getUseScore() - result.getReturnScore() - result.getReturnSellerScore() < 0 ){
            model.addAttribute("error","账户余额不足,请联系客服充值!");
            return FrontUtils.getTplPath(request, site.getSolutionPath(),
                    TPLDIR_SELLER_MEMBER, MEMBER_AJAX);
        }

        cmsConsumeMng.consume(user.getId(),buyer.getId(),result);

        model.addAttribute("success",true);
        return cmsSellerMemberAct.index(request,response,model);
    }

    @Resource
    private CmsUserMng cmsUserMng;
    @Resource
    private ConsumeMng consumeMng;
    @Resource
    private CmsConsumeMng cmsConsumeMng;
    @Resource
    private SellerMemberAct cmsSellerMemberAct;
}
