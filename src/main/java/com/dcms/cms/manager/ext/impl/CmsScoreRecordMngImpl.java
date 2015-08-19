package com.dcms.cms.manager.ext.impl;

import com.dcms.cms.dao.ext.CmsScoreRecordDao;
import com.dcms.cms.entity.ext.CmsScoreRecord;
import com.dcms.cms.manager.ext.CmsScoreRecordMng;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by dailinyi on 15/8/19.
 */
@Service
public class CmsScoreRecordMngImpl implements CmsScoreRecordMng {
    @Resource
    private CmsScoreRecordDao dao;

    @Override
    public CmsScoreRecord save(CmsScoreRecord record){
        return dao.save(record);
    }
}
