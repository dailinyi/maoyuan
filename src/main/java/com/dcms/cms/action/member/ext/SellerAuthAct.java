package com.dcms.cms.action.member.ext;

import com.dcms.cms.entity.main.*;
import com.dcms.cms.manager.assist.CmsDictionaryMng;
import com.dcms.cms.manager.main.*;
import com.dcms.cms.statistic.DateUtils;
import com.dcms.cms.statistic.QRMng;
import com.dcms.cms.web.CmsUtils;
import com.dcms.cms.web.FrontUtils;
import com.dcms.cms.web.WebErrors;
import com.dcms.common.web.RequestUtils;
import com.dcms.common.web.session.SessionProvider;
import com.octo.captcha.service.image.ImageCaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.dcms.cms.Constants.TPLDIR_SELLER_MEMBER;

/**
 * Created by Daily on 2015/8/30.
 */
@Controller
public class SellerAuthAct {
    private static final Logger log = LoggerFactory.getLogger(SellerAuthAct.class);
    public static final String MEMBER_CENTER = "tpl.memberCenter";
    public static final String PROMOTION_QR = "tpl.memberPromotionQr";
    public static final String MEMBER_INFO = "tpl.memberInfo";
    public static final String MEMBER_AUTH = "tpl.memberAuth";
    public static final String MEMBER_PASSWORD = "tpl.memberPassword";


    @RequestMapping(value = "/seller/auth.jspx", method = RequestMethod.GET)
     public String authList(HttpServletRequest request,
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
                TPLDIR_SELLER_MEMBER, MEMBER_AUTH);
    }

    @RequestMapping(value = "/seller/auth.jspx", method = RequestMethod.POST)
    public String auth(String title , String description ,String txt , String titleImg ,String author ,
                       String attr_contactName , String attr_contactSex , String attr_contactTel,String attr_contactEmail ,
                       String attr_contactBirth,String attr_contactIdCard,HttpServletRequest request,
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

        WebErrors errors = validateSave( title ,  description , txt ,  titleImg , author ,
                 attr_contactName ,  attr_contactSex ,  attr_contactTel, attr_contactEmail ,
                 attr_contactBirth, attr_contactIdCard, request, response);

        if (errors.hasErrors()) {
            return FrontUtils.showError(request, response, model, errors);
        }

        Integer channelId = Integer.valueOf(cmsDictionaryMng.findValue("sellerCheck","栏目ID").getValue());
        Integer modelId = Integer.valueOf(cmsDictionaryMng.findValue("sellerCheck","模型ID").getValue());

        //如果有未审核通过的数据,则删除此数据
        List<Content> unfinishedCheck = contentMng.findUnfinishCheck(user.getId(),channelId);
        if (unfinishedCheck != null && !unfinishedCheck.isEmpty()){
            Integer[] willDelete = new Integer[unfinishedCheck.size()];
            for (int i = 0 ;i < unfinishedCheck.size(); i++){
                willDelete[i] = (unfinishedCheck.get(i).getId());
            }
            contentMng.deleteByIds(willDelete);
        }


        Content c = new Content();
        c.setSite(site);
        c.setModel(cmsModelMng.findById(modelId));;

        ContentExt ext = new ContentExt();
        ext.setTitle(title);
        ext.setTitleImg(titleImg);
        ext.setAuthor(author);
        ext.setDescription(description);
        ext.setOrigin(user.getId().toString());
        ContentTxt t = new ContentTxt();
        t.setTxt(txt);
        ContentType type = contentTypeMng.getDef();
        if (type == null) {
            throw new RuntimeException("Default ContentType not found.");
        }
        Integer typeId = type.getId();

        c.setAttr(RequestUtils.getRequestMap(request, "attr_"));

        c = contentMng.save(c, ext, t, null, null, null, null, null, null,
                null, null, null, channelId, typeId, null, user, true);


        model.addAttribute("message", "global.success");
        return FrontUtils.getTplPath(request, site.getSolutionPath(),
                TPLDIR_SELLER_MEMBER, MEMBER_AUTH);
    }


    private WebErrors validateSave(String title , String description ,String txt , String titleImg ,String author ,
                                   String contactName , String contactSex , String contactTel,String contactEmail ,
                                   String contactBirth,String contactIdCard,
                                   HttpServletRequest request, HttpServletResponse response) {
        WebErrors errors = WebErrors.create(request);

        if (errors.ifBlank(title, "商家名称", 150)) {
            return errors;
        }
        if (errors.ifMaxLength(author, "法人", 100)) {
            return errors;
        }
        if (errors.ifMaxLength(description, "商家地址", 255)) {
            return errors;
        }
        // 内容不能大于1M
        if (errors.ifBlank(txt, "主营介绍", 1048575)) {
            return errors;
        }
        if (errors.ifMaxLength(titleImg, "营业执照", 255)) {
            return errors;
        }
        if (errors.ifBlank(contactName, "联系人姓名", 50)) {
            return errors;
        }
        if (errors.ifBlank(contactSex, "联系人性别", 10)) {
            return errors;
        }
        if (errors.ifBlank(contactTel, "联系人电话", 20)) {
            return errors;
        }
        if (errors.ifNotEmail(contactEmail, "联系人Email", 50)) {
            return errors;
        }
        if (errors.ifBlank(contactIdCard, "联系人身份证号", 50)) {
            return errors;
        }

        if (!errors.ifBlank(contactBirth, "联系人生日", 50) && DateUtils.strToDate(contactBirth) == null) {
            return errors;
        }

        return errors;
    }



    @Autowired
    private CmsUserExtMng cmsUserExtMng;

    @Autowired
    private QRMng qrMng;

    @Autowired
    private CmsDictionaryMng cmsDictionaryMng;
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
