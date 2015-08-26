package com.dcms.cms.manager.ext;

import com.dcms.cms.entity.main.CmsUser;
import com.dcms.cms.entity.main.Content;
import com.dcms.common.page.Pagination;

public interface CmsRaffleMng {
    Content doEggRaffle(CmsUser user);

    Pagination getPage(Integer queryIsWin,Integer queryIsOffer, String queryActivityUserName, int cpn, int pageSize);
}