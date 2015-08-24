package com.dcms.cms.action.admin.ext;

import com.dcms.cms.action.admin.main.CmsMemberAct;
import com.dcms.cms.entity.main.CmsGroup;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.CmsUserExt;
import com.dcms.cms.manager.assist.CmsDictionaryMng;
import com.dcms.cms.manager.main.CmsGroupMng;
import com.dcms.cms.manager.main.CmsLogMng;
import com.dcms.cms.manager.main.CmsUserMng;
import com.dcms.cms.web.WebErrors;
import com.dcms.common.page.Pagination;
import com.dcms.common.web.CookieUtils;
import com.dcms.common.web.RequestUtils;
import com.dcms.common.web.ResponseUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.dcms.common.page.SimplePage.cpn;

/**
 * Created by Daily on 2015/8/22.
 */
@Controller
public class CmsBuyerAct {

    private static final Logger log = LoggerFactory.getLogger(CmsMemberAct.class);

    @RequestMapping("/buyer/v_list.do")
    public String list(String queryUsername, String queryEmail, Boolean queryDisabled, Integer pageNo,
                       HttpServletRequest request, ModelMap model) {

        Integer queryGroupId = Integer.valueOf(cmsDictionaryMng.findValue("buyer", "用户组ID").getValue());

        Pagination pagination = manager.getPage(queryUsername, queryEmail,
                null, queryGroupId, queryDisabled, false, null, cpn(pageNo),
                CookieUtils.getPageSize(request));
        model.addAttribute("pagination", pagination);

        model.addAttribute("queryUsername", queryUsername);
        model.addAttribute("queryEmail", queryEmail);
        model.addAttribute("queryGroupId", queryGroupId);
        model.addAttribute("queryDisabled", queryDisabled);

        return "buyer/list";
    }


    @RequestMapping("/buyer/v_edit.do")
    public String edit(Integer id, Integer queryGroupId, Boolean queryDisabled,
                       HttpServletRequest request, ModelMap model) {
        String queryUsername = RequestUtils.getQueryParam(request,
                "queryUsername");
        String queryEmail = RequestUtils.getQueryParam(request, "queryEmail");
        WebErrors errors = validateEdit(id, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        List<CmsGroup> groupList = cmsGroupMng.getList();
        CmsUser byId = manager.findById(id);


        model.addAttribute("queryUsername", queryUsername);
        model.addAttribute("queryEmail", queryEmail);
        model.addAttribute("queryGroupId", queryGroupId);
        model.addAttribute("queryDisabled", queryDisabled);
        model.addAttribute("groupList", groupList);
        model.addAttribute("cmsMember", byId);
        return "buyer/edit";
    }


    @RequestMapping("/buyer/o_update.do")
    public String update(Integer id, String email, String password,
                         Boolean disabled, CmsUserExt ext, Integer groupId,
                         String queryUsername, String queryEmail, Integer queryGroupId,
                         Boolean queryDisabled, Integer pageNo, HttpServletRequest request,
                         ModelMap model) {
        WebErrors errors = validateUpdate(id, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        CmsUser bean = manager.updateMember(id, email, password, disabled, ext,
                groupId);
        log.info("update CmsMember id={}.", bean.getId());
        cmsLogMng.operating(request, "cmsMember.log.update", "id="
                + bean.getId() + ";username=" + bean.getUsername());

        return list(queryUsername, queryEmail,  queryDisabled,
                pageNo, request, model);
    }


    @RequestMapping(value = "/buyer/v_check_username.do")
    public void checkUsername(HttpServletRequest request,HttpServletResponse response) {
        String username=RequestUtils.getQueryParam(request,"username");
        String pass;
        if (StringUtils.isBlank(username)) {
            pass = "false";
        } else {
            pass = manager.usernameNotExist(username) ? "true" : "false";
        }
        ResponseUtils.renderJson(response, pass);
    }


    private WebErrors validateEdit(Integer id, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if (vldExist(id, errors)) {
            return errors;
        }
        return errors;
    }

    private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if (vldExist(id, errors)) {
            return errors;
        }
        // TODO 验证是否为管理员，管理员不允许修改。
        return errors;
    }


    private boolean vldExist(Integer id, WebErrors errors) {
        if (errors.ifNull(id, "id")) {
            return true;
        }
        CmsUser entity = manager.findById(id);
        if (errors.ifNotExist(entity, CmsUser.class, id)) {
            return true;
        }
        return false;
    }

    @Resource
    private CmsGroupMng cmsGroupMng;
    @Resource
    private CmsLogMng cmsLogMng;
    @Resource
    private CmsUserMng manager;
    @Resource
    private CmsDictionaryMng cmsDictionaryMng;
}
