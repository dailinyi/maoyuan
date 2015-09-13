package com.dcms.cms.action.member.ext;

import com.dcms.cms.entity.main.CmsSite;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.MemberConfig;
import com.dcms.cms.manager.main.CmsUserMng;
import com.dcms.cms.web.CmsUtils;
import com.dcms.cms.web.FrontUtils;
import com.dcms.common.page.Pagination;
import com.dcms.common.web.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.dcms.cms.Constants.TPLDIR_SELLER_MEMBER;
import static com.dcms.common.page.SimplePage.cpn;

/**
 * Created by Daily on 2015/8/30.
 */
@Controller
public class SellerRecommendUserAct {
    private static final Logger log = LoggerFactory.getLogger(SellerRecommendUserAct.class);
    public static final String RECOMMEND_USER_LIST = "tpl.memberRecommendUser";

    @RequestMapping(value = "/seller/recommendUserList.jspx", method = RequestMethod.GET)
    public String list(String username,Integer pageNo,HttpServletRequest request, HttpServletResponse response,
                       ModelMap model){
        CmsUser user = CmsUtils.getUser(request);
        CmsSite site = CmsUtils.getSite(request);
        FrontUtils.frontData(request, model, site);
        MemberConfig mcfg = site.getConfig().getMemberConfig();
        // 没有开启会员功能
        if (!mcfg.isMemberOn()) {
            return FrontUtils.showMessage(request, model, "member.memberClose");
        }
        if (user == null) {
            return FrontUtils.showLogin(request, model, site);
        }


        Pagination userList = cmsUserMng.getRecommendUserPage(username, user.getId(), cpn(pageNo), CookieUtils.getPageSize(request));

        model.addAttribute("pagination",userList);
        model.addAttribute("username",username);

        return FrontUtils.getTplPath(request, site.getSolutionPath(),
                TPLDIR_SELLER_MEMBER, RECOMMEND_USER_LIST);
    }

    @Autowired
    private CmsUserMng cmsUserMng;

}
