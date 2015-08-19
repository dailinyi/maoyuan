package com.dcms.cms.manager.ext.impl;

import com.dcms.cms.dao.ext.CmsActivityRecordDao;
import com.dcms.cms.entity.ext.CmsActivityRecord;
import com.dcms.cms.manager.ext.CmsActivityRecordMng;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by dailinyi on 15/8/20.
 */
@Service
public class CmsActivityRecordMngImpl implements CmsActivityRecordMng{
    @Resource
    private CmsActivityRecordDao dao;

    @Override
    public CmsActivityRecord save(CmsActivityRecord record){
        return dao.save(record);
    }

    @Override
    public CmsActivityRecord findById(Integer id){
        return dao.findById(id);
    }
}
