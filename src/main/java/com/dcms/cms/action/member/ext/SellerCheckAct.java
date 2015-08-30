package com.dcms.cms.action.member.ext;

import com.dcms.cms.entity.main.*;
import com.dcms.cms.manager.main.ChannelMng;
import com.dcms.cms.manager.main.CmsModelMng;
import com.dcms.cms.manager.main.ContentMng;
import com.dcms.cms.manager.main.ContentTypeMng;
import com.dcms.cms.web.CmsUtils;
import com.dcms.cms.web.FrontUtils;
import com.dcms.cms.web.WebErrors;
import com.dcms.common.util.StrUtils;
import com.dcms.common.web.session.SessionProvider;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Daily on 2015/8/30.
 */
@Controller
public class SellerCheckAct {
    private static final Logger log = LoggerFactory.getLogger(SellerCheckAct.class);
    /**
     * 会员投稿保存
     *
     * @param id
     *            文章ID
     * @param title
     *            标题
     * @param author
     *            作者
     * @param description
     *            描述
     * @param txt
     *            内容
     * @param tagStr
     *            TAG字符串
     * @param channelId
     *            栏目ID
     * @param nextUrl
     *            下一个页面地址
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/seller/check_save.jspx")
    public String save(String title, String author, String description,
                       String txt, String tagStr, Integer channelId,Integer modelId, String captcha,
                       String nextUrl, HttpServletRequest request,
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
        WebErrors errors = validateSave(title, author, description, txt,
                tagStr, channelId, site, user, captcha, request, response);
        if (errors.hasErrors()) {
            return FrontUtils.showError(request, response, model, errors);
        }

        Content c = new Content();
        c.setSite(site);
        CmsModel defaultModel=cmsModelMng.getDefModel();
        if(modelId!=null){
            CmsModel m=cmsModelMng.findById(modelId);
            if(m!=null){
                c.setModel(m);
            }else{
                c.setModel(defaultModel);
            }
        }else{
            c.setModel(defaultModel);
        }
        ContentExt ext = new ContentExt();
        ext.setTitle(title);
        ext.setAuthor(author);
        ext.setDescription(description);
        ContentTxt t = new ContentTxt();
        t.setTxt(txt);
        ContentType type = contentTypeMng.getDef();
        if (type == null) {
            throw new RuntimeException("Default ContentType not found.");
        }
        Integer typeId = type.getId();
        String[] tagArr = StrUtils.splitAndTrim(tagStr, ",", null);
        c = contentMng.save(c, ext, t, null, null, null, tagArr, null, null,
                null, null, null, channelId, typeId, null, user, true);
        log.info("member contribute save Content success. id={}", c.getId());
        return "redirect:/seller/index.jspx";
    }

    private WebErrors validateSave(String title, String author,
                                   String description, String txt, String tagStr, Integer channelId,
                                   CmsSite site, CmsUser user, String captcha,
                                   HttpServletRequest request, HttpServletResponse response) {
        WebErrors errors = WebErrors.create(request);
        try {
            if (!imageCaptchaService.validateResponseForID(session
                    .getSessionId(request, response), captcha)) {
                errors.addErrorCode("error.invalidCaptcha");
                return errors;
            }
        } catch (CaptchaServiceException e) {
            errors.addErrorCode("error.exceptionCaptcha");
            log.warn("", e);
            return errors;
        }
        if (errors.ifBlank(title, "title", 150)) {
            return errors;
        }
        if (errors.ifMaxLength(author, "author", 100)) {
            return errors;
        }
        if (errors.ifMaxLength(description, "description", 255)) {
            return errors;
        }
        // 内容不能大于1M
        if (errors.ifBlank(txt, "txt", 1048575)) {
            return errors;
        }
        if (errors.ifMaxLength(tagStr, "tagStr", 255)) {
            return errors;
        }
        if (errors.ifNull(channelId, "channelId")) {
            return errors;
        }
        if (vldChannel(errors, site, user, channelId)) {
            return errors;
        }
        return errors;
    }

    private boolean vldChannel(WebErrors errors, CmsSite site, CmsUser user,
                               Integer channelId) {
        Channel channel = channelMng.findById(channelId);
        if (errors.ifNotExist(channel, Channel.class, channelId)) {
            return true;
        }
        if (!channel.getSite().getId().equals(site.getId())) {
            errors.notInSite(Channel.class, channelId);
            return true;
        }
        if (!channel.getContriGroups().contains(user.getGroup())) {
            errors.noPermission(Channel.class, channelId);
            return true;
        }
        return false;
    }

    @Autowired
    private ContentMng contentMng;
    @Autowired
    private ContentTypeMng contentTypeMng;
    @Autowired
    private ChannelMng channelMng;
    @Autowired
    protected CmsModelMng cmsModelMng;
    @Autowired
    private SessionProvider session;
    @Autowired
    private ImageCaptchaService imageCaptchaService;
}
