package com.dcms.cms.action.member.ext;

import com.dcms.cms.entity.main.*;
import com.dcms.cms.manager.assist.CmsDictionaryMng;
import com.dcms.cms.manager.main.CmsUserMng;
import com.dcms.cms.statistic.QRMng;
import com.dcms.cms.web.CmsUtils;
import com.dcms.cms.web.FrontUtils;
import com.dcms.cms.web.WebErrors;
import com.dcms.common.upload.FileRepository;
import com.dcms.common.web.RequestUtils;
import com.dcms.core.manager.UnifiedUserMng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.dcms.cms.Constants.TPLDIR_SELLER_MEMBER;

/**
 * Created by dailinyi on 15/8/16.
 */
@Controller
public class SellerRegisterAct {
    private static final Logger log = LoggerFactory.getLogger(BuyerRegisterAct.class);

    public static final String REGISTER = "tpl.register";
    public static final String LOGIN_INPUT = "tpl.loginInput";

    @RequestMapping(value = "/seller/register.jspx", method = RequestMethod.GET)
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
                TPLDIR_SELLER_MEMBER, REGISTER);
    }


    @RequestMapping(value = "/seller/register.jspx", method = RequestMethod.POST)
    public String submit(String username, String email, String password, String recommendCode,
                         CmsUserExt userExt, String captcha, String nextUrl,
                         HttpServletRequest request, HttpServletResponse response,
                         ModelMap model) throws IOException {
        CmsSite site = CmsUtils.getSite(request);
        CmsConfig config=site.getConfig();
        WebErrors errors = validateSubmit(username, email, password, captcha,recommendCode,
                site, request, response);

        CmsUser recommendUser = cmsUserMng.getUserByRecommendCode(recommendCode);

        if (recommendUser == null ){
            errors.addErrorCode("error.invalidRecommend");
        }
        userExt.setMobile(username);
        if (errors.hasErrors()) {
            return FrontUtils.showError(request, response, model, errors);
        }

        String ip = RequestUtils.getIpAddr(request);
        Integer sellerGroupId = Integer.valueOf(cmsDictionaryMng.findValue("seller", "用户组ID").getValue());
        CmsUser cmsUser = cmsUserMng.registerMember(username, email, password, ip, sellerGroupId, userExt, recommendUser);

        //生成二维码
        String template = cmsDictionaryMng.findValue("buyer", "URL模板").getValue();
        String userQrUrl = qrMng.stringToQR(request.getContextPath() + site.getUploadPath(),template.replace("#{buyerId}", cmsUser.getId().toString()));

        //生成推广链接
        String promotionUrl = cmsDictionaryMng.findValue("seller", "URL模板").getValue().replace("#{sellerId}", cmsUser.getId().toString());
        String promotionQrUrl = qrMng.stringToQR(request.getContextPath() + site.getUploadPath(),promotionUrl);
        cmsUserMng.updateQr(cmsUser.getId(),userQrUrl,promotionQrUrl,promotionQrUrl);
        log.info("member register success. username={}", username);
        FrontUtils.frontData(request, model, site);
        FrontUtils.frontPageData(request, model);
        model.addAttribute("success",true);
        return FrontUtils.getTplPath(request, site.getSolutionPath(),
                TPLDIR_SELLER_MEMBER, LOGIN_INPUT);
    }

    private WebErrors validateSubmit(String username, String email,
                                     String password, String captcha,String remmondCode, CmsSite site,
                                     HttpServletRequest request, HttpServletResponse response) {
        MemberConfig mcfg = site.getConfig().getMemberConfig();
        WebErrors errors = WebErrors.create(request);


        if (errors.ifNotRemmondCode(remmondCode, "remmondCode", 0, 100)){
            return errors;
        }

        if (errors.ifOutOfLength(username, "username",
                mcfg.getUsernameMinLen(), 100)) {
            return errors;
        }
        if (errors.ifNotUsername(username, "username",
                mcfg.getUsernameMinLen(), 100)) {
            return errors;
        }
        if (errors.ifOutOfLength(password, "password",
                mcfg.getPasswordMinLen(), 100)) {
            return errors;
        }

        // 保留字检查不通过，返回false。
        if (!mcfg.checkUsernameReserved(username)) {
            errors.addErrorCode("error.usernameReserved");
            return errors;
        }
        // 用户名存在，返回false。
        if (unifiedUserMng.usernameExist(username)) {
            errors.addErrorCode("error.usernameExist");
            return errors;
        }
        return errors;
    }

    @Autowired
    private UnifiedUserMng unifiedUserMng;
    @Autowired
    private CmsUserMng cmsUserMng;

    @Autowired
    private QRMng qrMng;

    @Autowired
    private CmsDictionaryMng cmsDictionaryMng;
    @Autowired
    protected FileRepository fileRepository;
}
