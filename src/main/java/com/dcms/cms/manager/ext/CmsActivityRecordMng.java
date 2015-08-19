package com.dcms.cms.manager.ext;

import com.dcms.cms.entity.ext.CmsActivityRecord;

/**
 * Created by dailinyi on 15/8/20.
 */

public interface CmsActivityRecordMng {
    CmsActivityRecord save(CmsActivityRecord record);

    CmsActivityRecord findById(Integer id);
}
