package com.dcms.cms.dao.ext;

import com.dcms.cms.entity.ext.CmsScoreRecord;
import com.dcms.common.page.Pagination;

/**
 * Created by dailinyi on 15/8/18.
 */
public interface CmsScoreRecordDao {

    CmsScoreRecord save(CmsScoreRecord bean);

    Pagination getPage(String queryOrderId, Integer queryRecordType, String querySendUserName, String queryReceiveUserName, int cpn, int pageSize);
}
