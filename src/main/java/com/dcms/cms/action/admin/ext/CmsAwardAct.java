package com.dcms.cms.action.admin.ext;

import com.dcms.cms.entity.main.*;
import com.dcms.cms.manager.assist.CmsDictionaryMng;
import com.dcms.cms.manager.main.*;
import com.dcms.cms.web.CmsUtils;
import com.dcms.common.page.Pagination;
import com.dcms.common.web.CookieUtils;
import com.dcms.common.web.RequestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.dcms.common.page.SimplePage.cpn;

/**
 * Created by Daily on 2015/8/22.
 */
public class CmsAwardAct {


    @RequestMapping("/award/v_list.do")
    public String list(String queryStatus, Integer queryTypeId,
                       Boolean queryTopLevel, Boolean queryRecommend,
                       Integer queryOrderBy, Integer cid, Integer pageNo,
                       HttpServletRequest request, ModelMap model) {
        String queryTitle = RequestUtils.getQueryParam(request, "queryTitle");
        queryTitle = StringUtils.trim(queryTitle);
        String queryInputUsername = RequestUtils.getQueryParam(request,
                "queryInputUsername");
        queryInputUsername = StringUtils.trim(queryInputUsername);
        if (queryTopLevel == null) {
            queryTopLevel = false;
        }
        if (queryRecommend == null) {
            queryRecommend = false;
        }
        if (queryOrderBy == null) {
            queryOrderBy = 0;
        }
        Content.ContentStatus status;
        if (!StringUtils.isBlank(queryStatus)) {
            status = Content.ContentStatus.valueOf(queryStatus);
        } else {
            status = Content.ContentStatus.all;
        }
        Integer queryInputUserId = null;
        if (!StringUtils.isBlank(queryInputUsername)) {
            CmsUser u = cmsUserMng.findByUsername(queryInputUsername);
            if (u != null) {
                queryInputUserId = u.getId();
            } else {
                // 用户名不存在，清空。
                queryInputUsername = null;
            }
        }
        CmsSite site = CmsUtils.getSite(request);
        Integer siteId = site.getId();
        CmsUser user = CmsUtils.getUser(request);
        Integer userId = user.getId();
        byte currStep = user.getCheckStep(siteId);
        Pagination p = manager.getPageByRight(queryTitle, queryTypeId,
                queryInputUserId, queryTopLevel, queryRecommend, status, user
                        .getCheckStep(siteId), siteId, cid, userId,
                queryOrderBy, cpn(pageNo), CookieUtils.getPageSize(request));
        List<ContentType> typeList = contentTypeMng.getList(true);
        List<CmsModel>models=cmsModelMng.getList(false, true);
        if(cid!=null){
            Channel c=channelMng.findById(cid);
            models=c.getModels(models);
        }
        model.addAttribute("pagination", p);
        model.addAttribute("cid", cid);
        model.addAttribute("typeList", typeList);
        model.addAttribute("currStep", currStep);
        model.addAttribute("site", site);
        model.addAttribute("models", models);
        addAttibuteForQuery(model,pageNo);

        return "content/list";
    }

    private void addAttibuteForQuery(ModelMap model,Integer pageNo){

    }


    @Resource
    private ContentMng manager;
    @Resource
    private CmsUserMng cmsUserMng;
    @Resource
    private CmsModelMng cmsModelMng;
    @Resource
    private CmsDictionaryMng dicMng;
    @Resource
    private ContentTypeMng contentTypeMng;
    @Resource
    private ChannelMng channelMng;
}
