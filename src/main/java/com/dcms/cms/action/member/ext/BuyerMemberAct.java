package com.dcms.cms.action.member.ext;

import com.dcms.cms.entity.main.CmsSite;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.CmsUserExt;
import com.dcms.cms.entity.main.MemberConfig;
import com.dcms.cms.manager.assist.CmsDictionaryMng;
import com.dcms.cms.manager.main.CmsUserExtMng;
import com.dcms.cms.manager.main.CmsUserMng;
import com.dcms.cms.statistic.utils.QRMng;
import com.dcms.cms.web.CmsUtils;
import com.dcms.cms.web.FrontUtils;
import com.dcms.cms.web.WebErrors;
import com.dcms.common.web.ResponseUtils;
import org.apache.commons.lang.StringUtils;
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

import static com.dcms.cms.Constants.TPLDIR_BUYER_MEMBER;

/**
 * Created by Daily on 2015/8/30.
 */
@Controller
public class BuyerMemberAct {
    Logger log = LoggerFactory.getLogger(BuyerMemberAct.class);
    public static final String MEMBER_CENTER = "tpl.memberCenter";
    public static final String MEMBER_QR = "tpl.memberQr";
    public static final String MEMBER_INFO = "tpl.memberInfo";
    public static final String MEMBER_PASSWORD = "tpl.memberPassword";
    /**
     * 会员中心页
     *
     * 如果没有登录则跳转到登陆页
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/buyer/index.jspx", method = RequestMethod.GET)
     public String index(HttpServletRequest request,
                         HttpServletResponse response, ModelMap model) {
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
                TPLDIR_BUYER_MEMBER, MEMBER_CENTER);
    }

    @RequestMapping(value = "/buyer/qr.jspx", method = RequestMethod.GET)
    public String qrList(HttpServletRequest request,
                        HttpServletResponse response, ModelMap model) {
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

        if (StringUtils.isEmpty(user.getUserQR())){
            String template = cmsDictionaryMng.findValue("buyer", "URL模板").getValue();
            String userQrUrl = qrMng.stringToQR(request.getContextPath() + site.getUploadPath(),template.replace("#{buyerId}", user.getId().toString()));
            cmsUserMng.updateQr(user.getId(),userQrUrl,null,null);
        }
        return FrontUtils.getTplPath(request, site.getSolutionPath(),
                TPLDIR_BUYER_MEMBER, MEMBER_QR);
    }

    @RequestMapping(value = "/buyer/info.jspx", method = RequestMethod.GET)
    public String infoList(HttpServletRequest request,
                         HttpServletResponse response, ModelMap model) {
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
                TPLDIR_BUYER_MEMBER, MEMBER_INFO);
    }
    /**
     * 个人资料提交页
     *
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/buyer/profile.jspx", method = RequestMethod.POST)
    public String profileSubmit(CmsUserExt ext, String nextUrl,String email,
                                HttpServletRequest request, HttpServletResponse response,
                                ModelMap model) throws IOException {
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
        ext.setId(user.getId());
        cmsUserMng.updateEmail(user.getId(),email);
        cmsUserExtMng.update(ext, user);
        log.info("update CmsUserExt success. id={}", user.getId());
        model.addAttribute("success",true);
        return index(request,response,model);
    }

    /**
     * 密码修改输入页
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/buyer/pwd.jspx", method = RequestMethod.GET)
    public String passwordInput(HttpServletRequest request,
                                HttpServletResponse response, ModelMap model) {
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
                TPLDIR_BUYER_MEMBER, MEMBER_PASSWORD);
    }

    /**
     * 密码修改提交页
     *
     * @param origPwd
     *            原始密码
     * @param newPwd
     *            新密码
     * @param email
     *            邮箱
     * @param nextUrl
     *            下一个页面地址
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/buyer/pwd.jspx", method = RequestMethod.POST)
    public String passwordSubmit(String origPwd, String newPwd, String email,
                                 String nextUrl, HttpServletRequest request,
                                 HttpServletResponse response, ModelMap model) throws IOException {
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
        WebErrors errors = validatePasswordSubmit(user.getId(), origPwd,
                newPwd,  request);
        if (errors.hasErrors()) {
            errors.toModel(model);
            return passwordInput(request,response,model);
        }

        cmsUserMng.updatePwd(user.getId(), newPwd);
        model.addAttribute("success",true);
        return "redirect:info.jspx";
    }

    /**
     * 验证密码是否正确
     *
     * @param origPwd
     *            原密码
     * @param request
     * @param response
     */
    @RequestMapping("/buyer/checkPwd.jspx")
    public void checkPwd(String origPwd, HttpServletRequest request,
                         HttpServletResponse response) {
        CmsUser user = CmsUtils.getUser(request);
        boolean pass = cmsUserMng.isPasswordValid(user.getId(), origPwd);
        ResponseUtils.renderJson(response, pass ? "true" : "false");
    }

    private WebErrors validatePasswordSubmit(Integer id, String origPwd,
                                             String newPwd,  HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if (errors.ifBlank(origPwd, "origPwd", 100)) {
            return errors;
        }
        if (errors.ifMaxLength(newPwd, "newPwd", 100)) {
            return errors;
        }

        if (!cmsUserMng.isPasswordValid(id, origPwd)) {
            errors.addErrorCode("member.origPwdInvalid");
            return errors;
        }
        return errors;
    }

    @Autowired
    private CmsUserMng cmsUserMng;
    @Autowired
    private CmsUserExtMng cmsUserExtMng;
    @Autowired
    private QRMng qrMng;

    @Autowired
    private CmsDictionaryMng cmsDictionaryMng;

}
