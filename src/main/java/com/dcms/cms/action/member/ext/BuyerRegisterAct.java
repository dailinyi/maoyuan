package com.dcms.cms.action.member.ext;

import com.dcms.cms.entity.main.CmsSite;
import com.dcms.cms.entity.main.MemberConfig;
import com.dcms.cms.web.CmsUtils;
import com.dcms.cms.web.FrontUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.dcms.cms.Constants.TPLDIR_BUYER_MEMBER;

/**
 * Created by dailinyi on 15/8/16.
 */
@Controller
public class BuyerRegisterAct {
    private static final Logger log = LoggerFactory.getLogger(BuyerRegisterAct.class);

    public static final String REGISTER = "tpl.register";

    @RequestMapping(value = "/buyer/register.jspx", method = RequestMethod.GET)
    public String buyerRegister(HttpServletRequest request,
                        HttpServletResponse response, ModelMap model) {
        CmsSite site = CmsUtils.getSite(request);
        MemberConfig mcfg = site.getConfig().getMemberConfig();
        // 没有开启会员功能
        if (!mcfg.isMemberOn()) {
            return FrontUtils.showMessage(request, model, "member.memberClose");
        }
        // 没有开启会员注册
        if (!mcfg.isRegisterOn()) {
            return FrontUtils.showMessage(request, model, "member.registerClose");
        }
        FrontUtils.frontData(request, model, site);
        model.addAttribute("mcfg", mcfg);
        return FrontUtils.getTplPath(request, site.getSolutionPath(),
                TPLDIR_BUYER_MEMBER, REGISTER);
    }


}
