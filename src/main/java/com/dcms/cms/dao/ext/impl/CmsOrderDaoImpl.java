package com.dcms.cms.dao.ext.impl;

import com.dcms.cms.dao.ext.CmsOrderDao;
import com.dcms.cms.entity.ext.CmsOrder;
import com.dcms.common.hibernate3.Finder;
import com.dcms.common.hibernate3.HibernateBaseDao;
import com.dcms.common.page.Pagination;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public Pagination getPageByUser(Integer id, int page, int pageSize) {
        Finder finder = Finder.create("from CmsOrder bean where bean.buyer.id = :userId or bean.seller.id = :userId order by bean.id desc");
        finder.setParam("userId",id);
        return find(finder,page,pageSize);
    }

    @Override
    public CmsOrder findById(String orderId) {
        Finder finder = Finder.create("from CmsOrder bean where bean.id = :orderId order by bean.id desc");
        finder.setParam("orderId",orderId);

        List<CmsOrder> orderList = find(finder);
        if (orderList == null || orderList.size() == 0){
            return null;
        }
        return orderList.get(0);
    }

    @Override
    protected Class<CmsOrder> getEntityClass() {
        return CmsOrder.class;
    }
}
