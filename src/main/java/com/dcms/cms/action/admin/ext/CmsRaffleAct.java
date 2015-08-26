package com.dcms.cms.action.admin.ext;

import com.dcms.cms.manager.ext.CmsRaffleMng;
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
 * Created by Daily on 2015/8/22.
 */
@Controller
public class CmsRaffleAct {
    @RequestMapping("/raffle/v_list.do")
    public String list(Integer queryIsWin ,Integer queryIsOffer, String queryActivityUserName,Integer pageNo ,HttpServletRequest request,ModelMap model){

        Pagination p = manager.getPage( queryIsWin,queryIsOffer, queryActivityUserName, cpn(pageNo), CookieUtils.getPageSize(request));

        model.addAttribute("pagination", p);

        addAttibuteForQuery(queryIsWin,queryIsOffer, queryActivityUserName,pageNo,model);

        return "raffle/list";
    }

    private void addAttibuteForQuery(Integer queryIsWin ,Integer queryIsOffer, String queryActivityUserName, Integer pageNo, ModelMap model) {

        if (StringUtils.isNotBlank(queryActivityUserName)){
            model.addAttribute("queryActivityUserName",queryActivityUserName);
        }

        if (queryIsOffer != null){
            model.addAttribute("queryIsOffer", queryIsOffer);
        }

        if (queryIsWin != null){
            model.addAttribute("queryIsWin", queryIsWin);
        }

        if (pageNo != null){
            model.addAttribute("pageNo", pageNo);
        }
    }

    @Resource
    private CmsRaffleMng manager;
}
