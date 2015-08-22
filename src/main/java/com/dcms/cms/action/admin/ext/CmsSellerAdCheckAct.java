package com.dcms.cms.action.admin.ext;

import com.dcms.cms.entity.main.*;
import com.dcms.cms.manager.assist.CmsDictionaryMng;
import com.dcms.cms.manager.assist.CmsFileMng;
import com.dcms.cms.manager.main.*;
import com.dcms.cms.web.CmsUtils;
import com.dcms.cms.web.WebErrors;
import com.dcms.common.page.Pagination;
import com.dcms.common.util.StrUtils;
import com.dcms.common.web.CookieUtils;
import com.dcms.common.web.RequestUtils;
import com.dcms.common.web.springmvc.MessageResolver;
import com.dcms.core.tpl.TplManager;
import com.dcms.core.web.CoreUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.dcms.common.page.SimplePage.cpn;

/**
 * Created by Daily on 2015/8/22.
 */
@Controller
public class CmsSellerAdCheckAct {

    protected Logger log = LoggerFactory.getLogger(CmsSellerAdCheckAct.class);


    @RequestMapping("/sellerAdCheck/v_list.do")
    public String list(String queryStatus, Integer queryOrderBy,  Integer pageNo,
                       HttpServletRequest request, ModelMap model) {
        Boolean queryTopLevel = false;
        Boolean queryRecommend = false;
        if (queryOrderBy == null) {
            queryOrderBy = 0;
        }

        String queryTitle = RequestUtils.getQueryParam(request, "queryTitle");
        queryTitle = StringUtils.trim(queryTitle);
        String queryInputUsername = RequestUtils.getQueryParam(request,
                "queryInputUsername");
        queryInputUsername = StringUtils.trim(queryInputUsername);
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


        Content.ContentStatus status;
        if (!StringUtils.isBlank(queryStatus)) {
            status = Content.ContentStatus.valueOf(queryStatus);
        } else {
            status = Content.ContentStatus.all;
        }

        CmsSite site = CmsUtils.getSite(request);
        Integer siteId = site.getId();
        CmsUser user = CmsUtils.getUser(request);
        Integer userId = user.getId();
        byte currStep = user.getCheckStep(siteId);
        Integer cid = Integer.valueOf(dicMng.findValue("sellerAdCheck","channelId").getValue());
        Pagination p = manager.getPageByRight(queryTitle, null,
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
        addAttibuteForQuery(model, queryTitle, queryInputUsername, queryStatus,
                 queryTopLevel, queryRecommend, queryOrderBy,
                pageNo);

        return "seller_check/list";
    }

    @RequestMapping("/sellerAdCheck/o_reject.do")
    public String reject(String queryStatus, Integer queryOrderBy, Integer[] ids,  Byte rejectStep,
                         String rejectOpinion, Integer pageNo, HttpServletRequest request,
                         ModelMap model) {
        Boolean queryTopLevel = false;
        Boolean queryRecommend = false;
        if (queryOrderBy == null) {
            queryOrderBy = 0;
        }

        WebErrors errors = validateReject(ids, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        CmsUser user = CmsUtils.getUser(request);
        Content[] beans = manager.reject(ids, user, rejectStep, rejectOpinion);
        for (Content bean : beans) {
            log.info("reject Content id={}", bean.getId());
        }
        return list(queryStatus,
                queryOrderBy,  pageNo, request, model);
    }

    @RequestMapping("/sellerAdCheck/o_check.do")
    public String check(String queryStatus, Integer queryTypeId,
                        Boolean queryTopLevel, Boolean queryRecommend,
                        Integer queryOrderBy, Integer[] ids, Integer cid, Integer pageNo,
                        HttpServletRequest request, ModelMap model) {
        WebErrors errors = validateCheck(ids, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        CmsUser user = CmsUtils.getUser(request);
        Content[] beans = manager.check(ids, user);
        for (Content bean : beans) {
            log.info("check Content id={}", bean.getId());
        }
        return list(queryStatus, queryOrderBy,  pageNo, request, model);
    }

    @RequestMapping("/sellerAdCheck/v_edit.do")
    public String edit(String queryStatus, Integer queryTypeId,
                       Boolean queryTopLevel, Boolean queryRecommend,
                       Integer queryOrderBy, Integer pageNo, Integer cid, Integer id,
                       HttpServletRequest request, ModelMap model) {
        WebErrors errors = validateEdit(id, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        CmsSite site = CmsUtils.getSite(request);
        Integer siteId = site.getId();
        CmsUser user = CmsUtils.getUser(request);
        // 内容
        Content content = manager.findById(id);
        // 栏目
        Channel channel = content.getChannel();

        CmsModel m=content.getModel();
		/*
		// 模型
		CmsModel m = channel.getModel();
		*/
        // 模型项列表
        List<CmsModelItem> itemList = cmsModelItemMng.getList(m.getId(), false,
                false);
        // 栏目列表
        Set<Channel> rights;
        if (user.getUserSite(siteId).getAllChannel()) {
            // 拥有所有栏目权限
            rights = null;
        } else {
            rights = user.getChannels(siteId);
        }

        List<Channel> topList = channelMng.getTopList(site.getId(), true);
        List<Channel> channelList = Channel.getListForSelect(topList, rights,
                true);

        // 专题列表
        List<CmsTopic> topicList = cmsTopicMng
                .getListByChannel(channel.getId());
        Set<CmsTopic> topics = content.getTopics();
        for (CmsTopic t : topics) {
            if (!topicList.contains(t)) {
                topicList.add(t);
            }
        }
        Integer[] topicIds = CmsTopic.fetchIds(content.getTopics());
        // 内容模板列表
        List<String> tplList = getTplContent(site, m, content.getTplContent());
        // 会员组列表
        List<CmsGroup> groupList = cmsGroupMng.getList();
        Integer[] groupIds = CmsGroup.fetchIds(content.getViewGroups());
        // 内容类型
        List<ContentType> typeList = contentTypeMng.getList(false);
        // 当前模板，去除基本路径
        int tplPathLength = site.getTplPath().length();
        String tplContent = content.getTplContent();
        if (!StringUtils.isBlank(tplContent)) {
            tplContent = tplContent.substring(tplPathLength);
        }

        model.addAttribute("content", content);
        model.addAttribute("channel", channel);
        model.addAttribute("model", m);
        model.addAttribute("itemList", itemList);
        model.addAttribute("channelList", channelList);
        model.addAttribute("topicList", topicList);
        model.addAttribute("topicIds", topicIds);
        model.addAttribute("tplList", tplList);
        model.addAttribute("groupList", groupList);
        model.addAttribute("groupIds", groupIds);
        model.addAttribute("typeList", typeList);
        model.addAttribute("tplContent", tplContent);
        if (cid != null) {
            model.addAttribute("cid", cid);
        }

        String queryTitle = RequestUtils.getQueryParam(request, "queryTitle");
        String queryInputUsername = RequestUtils.getQueryParam(request, "queryInputUsername");
        addAttibuteForQuery(model, queryTitle, queryInputUsername, queryStatus, queryTopLevel, queryRecommend, queryOrderBy, pageNo);

        return "seller_check/edit";
    }

    @RequestMapping("/sellerAdCheck/o_update.do")
    public String update(String queryStatus,
                         Integer queryOrderBy, Content bean, ContentExt ext, ContentTxt txt,
                         Integer[] channelIds, Integer[] topicIds, Integer[] viewGroupIds,
                         String[] attachmentPaths, String[] attachmentNames,
                         String[] attachmentFilenames, String[] picPaths,String[] picDescs,
                         Integer channelId, Integer typeId, String tagStr, Boolean draft,
                         Integer cid,String[]oldattachmentPaths,String[] oldpicPaths,
                         String oldTitleImg,String oldContentImg,String oldTypeImg,
                         Integer pageNo, HttpServletRequest request,
                         ModelMap model) {
        WebErrors errors = validateUpdate(bean.getId(), request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        // 加上模板前缀
        CmsSite site = CmsUtils.getSite(request);
        CmsUser user = CmsUtils.getUser(request);
        String tplPath = site.getTplPath();
        if (!StringUtils.isBlank(ext.getTplContent())) {
            ext.setTplContent(tplPath + ext.getTplContent());
        }
        String[] tagArr = StrUtils.splitAndTrim(tagStr, ",", MessageResolver
                .getMessage(request, "content.tagStr.split"));
        Map<String, String> attr = RequestUtils.getRequestMap(request, "attr_");
        bean = manager.update(bean, ext, txt, tagArr, channelIds, topicIds,
                viewGroupIds, attachmentPaths, attachmentNames,
                attachmentFilenames, picPaths, picDescs, attr, channelId,
                typeId, draft, user, false);
        //处理之前的附件有效性
        fileMng.updateFileByPaths(oldattachmentPaths,oldpicPaths,null,oldTitleImg,oldTypeImg,oldContentImg,false,bean);
        //处理更新后的附件有效性
        fileMng.updateFileByPaths(attachmentPaths,picPaths,ext.getMediaPath(),ext.getTitleImg(),ext.getTypeImg(),ext.getContentImg(),true,bean);
        log.info("update Content id={}.", bean.getId());
        cmsLogMng.operating(request, "content.log.update", "id=" + bean.getId()
                + ";title=" + bean.getTitle());
        return list(queryStatus, queryOrderBy,  pageNo, request, model);
    }

    private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        CmsSite site = CmsUtils.getSite(request);
        if (vldExist(id, site.getId(), errors)) {
            return errors;
        }
        Content content = manager.findById(id);
        // TODO 是否有编辑的数据权限。
        // 是否有审核后更新权限。
        if (!content.isHasUpdateRight()) {
            errors.addErrorCode("content.error.afterCheckUpdate");
            return errors;
        }
        return errors;
    }

    private List<String> getTplContent(CmsSite site, CmsModel model, String tpl) {
        String sol = site.getSolutionPath();
        String tplPath = site.getTplPath();
        List<String> tplList = tplManager.getNameListByPrefix(model
                .getTplContent(sol, false));
        tplList = CoreUtils.tplTrim(tplList, tplPath, tpl);
        return tplList;
    }

    private WebErrors validateEdit(Integer id, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        CmsSite site = CmsUtils.getSite(request);
        if (vldExist(id, site.getId(), errors)) {
            return errors;
        }
        // Content content = manager.findById(id);
        // TODO 是否有编辑的数据权限。
        return errors;
    }

    private WebErrors validateCheck(Integer[] ids, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        CmsSite site = CmsUtils.getSite(request);
        errors.ifEmpty(ids, "ids");
        for (Integer id : ids) {
            vldExist(id, site.getId(), errors);
        }
        return errors;
    }

    private WebErrors validateReject(Integer[] ids, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        CmsSite site = CmsUtils.getSite(request);
        errors.ifEmpty(ids, "ids");
        for (Integer id : ids) {
            vldExist(id, site.getId(), errors);
        }
        return errors;
    }

    private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
        if (errors.ifNull(id, "id")) {
            return true;
        }
        Content entity = manager.findById(id);
        if (errors.ifNotExist(entity, Content.class, id)) {
            return true;
        }
        if (!entity.getSite().getId().equals(siteId)) {
            errors.notInSite(Content.class, id);
            return true;
        }
        return false;
    }

    private void addAttibuteForQuery(ModelMap model, String queryTitle,
                                     String queryInputUsername, String queryStatus,
                                     Boolean queryTopLevel, Boolean queryRecommend,
                                     Integer queryOrderBy, Integer pageNo) {
        if (!StringUtils.isBlank(queryTitle)) {
            model.addAttribute("queryTitle", queryTitle);
        }
        if (!StringUtils.isBlank(queryInputUsername)) {
            model.addAttribute("queryInputUsername", queryInputUsername);
        }
        if (queryStatus != null) {
            model.addAttribute("queryStatus", queryStatus);
        }
        if (queryTopLevel != null) {
            model.addAttribute("queryTopLevel", queryTopLevel);
        }
        if (queryRecommend != null) {
            model.addAttribute("queryRecommend", queryRecommend);
        }
        if (queryOrderBy != null) {
            model.addAttribute("queryOrderBy", queryOrderBy);
        }
        if (pageNo != null) {
            model.addAttribute("pageNo", pageNo);
        }
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
    @Resource
    private TplManager tplManager;
    @Resource
    private CmsTopicMng cmsTopicMng;
    @Resource
    private CmsModelItemMng cmsModelItemMng;
    @Resource
    private CmsGroupMng cmsGroupMng;
    @Resource
    private CmsLogMng cmsLogMng;
    @Resource
    private CmsFileMng fileMng;
}
