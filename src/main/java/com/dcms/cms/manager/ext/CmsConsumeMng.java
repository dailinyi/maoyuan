package com.dcms.cms.manager.ext;

import com.dcms.cms.entity.ext.CmsConsumeDetail;

/**
 * Created by Daily on 2015/9/10.
 */
public interface CmsConsumeMng {
    void consume(Integer sellerId,Integer buyerId,CmsConsumeDetail detail);
}
