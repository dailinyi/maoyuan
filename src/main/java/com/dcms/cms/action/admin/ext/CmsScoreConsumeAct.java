package com.dcms.cms.action.admin.ext;

import com.dcms.cms.manager.ext.CmsScoreRecordMng;
import com.dcms.common.page.Pagination;
import com.dcms.common.web.CookieUtils;
import org.apache.commons.lang.StringUtils;
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
public class CmsScoreConsumeAct {
    @RequestMapping("/score/v_list.do")
    public String list(String queryOrderId , Integer queryRecordType,String querySendUserName,
                       String queryReceiveUserName, Integer pageNo ,HttpServletRequest request,ModelMap model){

        Pagination p = manager.getPage(queryOrderId, queryRecordType,
                querySendUserName, queryReceiveUserName, cpn(pageNo), CookieUtils.getPageSize(request));

        model.addAttribute("pagination", p);

        addAttibuteForQuery(queryOrderId, queryRecordType,
                querySendUserName, queryReceiveUserName,pageNo,model);

        return "score/list";
    }

    private void addAttibuteForQuery(String queryOrderId, Integer queryRecordType, String querySendUserName, String queryReceiveUserName, Integer pageNo, ModelMap model) {

        if (StringUtils.isNotBlank(queryReceiveUserName)){
            model.addAttribute("queryOrderId",queryOrderId);
        }

        if (StringUtils.isNotBlank(querySendUserName)){
            model.addAttribute("querySendUserName",querySendUserName);
        }

        if (queryOrderId != null){
            model.addAttribute("queryOrderId", queryOrderId);
        }

        if (queryRecordType != null){
            model.addAttribute("queryRecordType", queryRecordType);
        }

        if (pageNo != null){
            model.addAttribute("pageNo", pageNo);
        }
    }

    @Resource
    private CmsScoreRecordMng manager;
}
