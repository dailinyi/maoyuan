package com.dcms.cms.dao.ext.impl;

import com.dcms.cms.dao.ext.CmsOrderDao;
import com.dcms.cms.entity.ext.CmsOrder;
import com.dcms.common.hibernate3.HibernateBaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by dailinyi on 15/8/18.
 */
@Repository
public class CmsOrderDaoImpl extends HibernateBaseDao<CmsOrder, Integer> implements CmsOrderDao   {
    @Override
    public CmsOrder save(CmsOrder bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
    protected Class<CmsOrder> getEntityClass() {
        return CmsOrder.class;
    }
}
