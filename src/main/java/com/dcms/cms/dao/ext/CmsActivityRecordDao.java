package com.dcms.cms.dao.ext;

import com.dcms.cms.entity.ext.CmsActivityRecord;
import com.dcms.common.page.Pagination;

/**
 * Created by dailinyi on 15/8/20.
 */
public interface CmsActivityRecordDao {
    CmsActivityRecord save(CmsActivityRecord bean);

    CmsActivityRecord findById(Integer i);

    Pagination getPage(Integer queryIsWin,Integer queryIsOffer, String queryActivityUserName, int cpn, int pageSize);
}
