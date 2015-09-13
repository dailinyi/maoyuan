package com.dcms.cms.action.member.ext;

import com.dcms.cms.entity.ext.CmsOrder;
import com.dcms.cms.entity.ext.CmsScoreRecord;
import com.dcms.cms.entity.main.CmsSite;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.MemberConfig;
import com.dcms.cms.manager.ext.CmsOrderMng;
import com.dcms.cms.manager.ext.CmsScoreRecordMng;
import com.dcms.cms.web.CmsUtils;
import com.dcms.cms.web.FrontUtils;
import com.dcms.common.page.Pagination;
import com.dcms.common.web.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static com.dcms.cms.Constants.TPLDIR_SELLER_MEMBER;
import static com.dcms.common.page.SimplePage.cpn;

/**
 * Created by Daily on 2015/8/30.
 */
@Controller
public class SellerOrderAct {
    private static final Logger log = LoggerFactory.getLogger(SellerOrderAct.class);
    public static final String ORDER_LIST = "tpl.memberOrderList";
    public static final String ORDER_INFO = "tpl.memberOrderInfo";

    @RequestMapping(value = "/seller/orderList.jspx", method = RequestMethod.GET)
    public String list(Integer pageNo,HttpServletRequest request, HttpServletResponse response,
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


        Pagination orderList = cmsOrderMng.getPageByUser( user.getId(), cpn(pageNo), CookieUtils.getPageSize(request));

        model.addAttribute("pagination",orderList);

        return FrontUtils.getTplPath(request, site.getSolutionPath(),
                TPLDIR_SELLER_MEMBER, ORDER_LIST);
    }

    @RequestMapping(value = "/seller/orderInfo.jspx", method = RequestMethod.GET)
    public String info(String orderId,HttpServletRequest request, HttpServletResponse response,
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


        CmsOrder order = cmsOrderMng.findByOrderId(orderId);
        if (order == null || (!order.getBuyer().getId().equals(user.getId()) && !order.getSeller().getId().equals(user.getId()))){
            return FrontUtils.showMessage(request, model, "order.orderNotExist");
        }

        List<CmsScoreRecord> records = cmsScoreRecordMng.findByOrderId(order.getId());

        model.addAttribute("order",order);
        model.addAttribute("records",records);


        return FrontUtils.getTplPath(request, site.getSolutionPath(),
                TPLDIR_SELLER_MEMBER, ORDER_INFO);
    }

    @Resource
    private CmsOrderMng cmsOrderMng;
    @Resource
    private CmsScoreRecordMng cmsScoreRecordMng;

}
