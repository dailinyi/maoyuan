package com.dcms.cms.manager.ext;

import com.dcms.cms.entity.ext.CmsOrder;
import com.dcms.common.page.Pagination;

/**
 * Created by Daily on 2015/9/10.
 */
public interface CmsOrderMng {
    CmsOrder save(CmsOrder bean);

    Pagination getPageByUser(Integer id, int cpn, int pageSize);

    CmsOrder findByOrderId(String orderId);
}
