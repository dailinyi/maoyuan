package com.dcms.cms.action.admin.ext;

import com.dcms.cms.action.admin.main.CmsMemberAct;
import com.dcms.cms.entity.ext.CmsScoreRecord;
import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.manager.ext.CmsScoreRecordMng;
import com.dcms.cms.manager.main.CmsLogMng;
import com.dcms.cms.manager.main.CmsUserMng;
import com.dcms.cms.web.CmsUtils;
import com.dcms.cms.web.WebErrors;
import com.dcms.common.page.Pagination;
import com.dcms.common.web.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.dcms.common.page.SimplePage.cpn;

/**
 * Created by Daily on 2015/8/25.
 */
@Controller
public class CmsChargeScore {
    private static final Logger log = LoggerFactory.getLogger(CmsMemberAct.class);

    @RequestMapping("/chargeScore/v_list.do")
    public String list(String queryUsername, String queryEmail, Boolean queryDisabled, Integer pageNo,
                       HttpServletRequest request, ModelMap model) {

        Integer queryGroupId = null;

        Pagination pagination = manager.getPage(queryUsername, queryEmail,
                null, queryGroupId, queryDisabled, false, null, cpn(pageNo),
                CookieUtils.getPageSize(request));
        model.addAttribute("pagination", pagination);

        model.addAttribute("queryUsername", queryUsername);
        model.addAttribute("queryEmail", queryEmail);
        model.addAttribute("queryGroupId", queryGroupId);
        model.addAttribute("queryDisabled", queryDisabled);

        return "charge_score/list";
    }

    @RequestMapping("/chargeScore/o_charge.do")
    public String charge(Integer userId,Integer chargeScoreCount,String queryUsername,String queryEmail,Integer pageNo,
        HttpServletRequest request,ModelMap model){

        WebErrors errors = validateUpdate(userId, request);
        CmsUser user = CmsUtils.getUser(request);

        CmsUser bean = manager.findById(userId);


        cmsScoreRecordMng.save(new CmsScoreRecord(CmsScoreRecord.ScoreTypeEnum.CHARGE_SCORE.getValue().byteValue(),chargeScoreCount ,bean,user));

        cmsLogMng.operating(request, "cmsMember.log.update", "id="
                + bean.getId() + ";username=" + bean.getUsername() + ";积分增加 [" + chargeScoreCount + "]");

        bean = manager.updateMemberScore(userId, chargeScoreCount);
        return list(queryUsername,queryEmail,null,pageNo,request,model);
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
    private CmsScoreRecordMng cmsScoreRecordMng;
    @Resource
    private CmsLogMng cmsLogMng;
    @Resource
    private CmsUserMng manager;

}
