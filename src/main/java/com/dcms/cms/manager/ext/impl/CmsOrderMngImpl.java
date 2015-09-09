package com.dcms.cms.manager.ext.impl;

import com.dcms.cms.dao.ext.CmsOrderDao;
import com.dcms.cms.entity.ext.CmsOrder;
import com.dcms.cms.manager.ext.CmsOrderMng;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Daily on 2015/9/10.
 */
@Service
public class CmsOrderMngImpl implements CmsOrderMng{
    @Override
    public CmsOrder save(CmsOrder bean) {
        return cmsOrderDao.save(bean);
    }

    @Resource
    private CmsOrderDao cmsOrderDao;
}
