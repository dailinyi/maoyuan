package com.dcms.cms.dao.ext.impl;

import com.dcms.cms.dao.ext.CmsActivityRecordDao;
import com.dcms.cms.entity.ext.CmsActivityRecord;
import com.dcms.common.hibernate3.HibernateBaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by dailinyi on 15/8/20.
 */
@Repository
public class CmsActivityRecordDaoImpl extends HibernateBaseDao<CmsActivityRecord, Integer> implements CmsActivityRecordDao {
    @Override
    protected Class<CmsActivityRecord> getEntityClass() {
        return CmsActivityRecord.class;
    }
    @Override
    public CmsActivityRecord save(CmsActivityRecord bean) {
        getSession().save(bean);
        return bean;
    }
}
