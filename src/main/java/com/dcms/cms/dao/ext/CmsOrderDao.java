package com.dcms.cms.dao.ext;

import com.dcms.cms.entity.ext.CmsOrder;
import com.dcms.common.page.Pagination;

/**
 * Created by dailinyi on 15/8/18.
 */
public interface CmsOrderDao {

    CmsOrder save(CmsOrder bean);

    Pagination getPageByUser(Integer id, int page, int pageSize);

    CmsOrder findById(String orderId);
}
