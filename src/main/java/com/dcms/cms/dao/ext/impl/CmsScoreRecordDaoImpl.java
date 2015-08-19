package com.dcms.cms.dao.ext.impl;

import com.dcms.cms.dao.ext.CmsScoreRecordDao;
import com.dcms.cms.entity.ext.CmsScoreRecord;
import com.dcms.common.hibernate3.HibernateBaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by dailinyi on 15/8/18.
 */
@Repository
public class CmsScoreRecordDaoImpl extends HibernateBaseDao<CmsScoreRecord, Integer> implements CmsScoreRecordDao  {

    @Override
    protected Class<CmsScoreRecord> getEntityClass() {
        return CmsScoreRecord.class;
    }

    @Override
    public CmsScoreRecord save(CmsScoreRecord bean) {
        getSession().save(bean);
        return bean;
    }
}
