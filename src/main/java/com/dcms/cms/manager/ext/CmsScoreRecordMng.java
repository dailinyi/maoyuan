package com.dcms.cms.manager.ext;

import com.dcms.cms.entity.ext.CmsScoreRecord;
import com.dcms.common.page.Pagination;

/**
 * Created by dailinyi on 15/8/19.
 */
public interface CmsScoreRecordMng {
    CmsScoreRecord save(CmsScoreRecord record);

    Pagination getPage(String queryOrderId, Integer queryRecordType, String querySendUserName, String queryReceiveUserName, int cpn, int pageSize);

    Pagination getPageByUser(String queryOrderId, Integer id, int cpn, int pageSize);
}
