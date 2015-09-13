package com.dcms.cms.dao.ext.impl;

import com.dcms.cms.dao.ext.CmsScoreRecordDao;
import com.dcms.cms.entity.ext.CmsScoreRecord;
import com.dcms.common.hibernate3.Finder;
import com.dcms.common.hibernate3.HibernateBaseDao;
import com.dcms.common.page.Pagination;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public Pagination getPage(String queryOrderId, Integer queryRecordType, String querySendUserName, String queryReceiveUserName, int cpn, int pageSize) {
        Finder finder = Finder.create("from CmsScoreRecord bean where 1=1 ");

        if (StringUtils.isNotBlank(queryReceiveUserName)){
            finder.append(" and bean.receiveUser.username = :queryReceiveUserName  ");
            finder.setParam("queryReceiveUserName",queryReceiveUserName);
        }

        if (StringUtils.isNotBlank(querySendUserName)){
            finder.append(" and bean.sendUser.username = :querySendUserName");
            finder.setParam("querySendUserName", querySendUserName);
        }

        if (StringUtils.isNotBlank(queryOrderId)){
            finder.append(" and bean.orderId = :queryOrderId ");
            finder.setParam("queryOrderId",queryOrderId);
        }

        if (queryRecordType != null){
            finder.append(" and bean.recordType = :queryRecordType ");
            finder.setParam("queryRecordType",queryRecordType.byteValue());
        }
        finder.append(" order by bean.id desc ");
        return find(finder,cpn,pageSize);
    }

    @Override
    public Pagination getPageByUser(String queryOrderId, Integer queryUserId, int cpn, int pageSize) {
        Finder finder = Finder.create("from CmsScoreRecord bean where 1=1 ");

        if (queryUserId != null) {
            finder.append(" and ( bean.receiveUser.id = :userId or bean.sendUser.id = :userId ) ");
            finder.setParam("userId", queryUserId);
        }

        if (StringUtils.isNotBlank(queryOrderId)){
            finder.append(" and bean.orderId = :queryOrderId ");
            finder.setParam("queryOrderId",queryOrderId);
        }

        finder.append(" order by bean.id desc ");
        return find(finder,cpn,pageSize);
    }

    @Override
    public List<CmsScoreRecord> findByOrderId(String orderId) {
        Finder finder = Finder.create("from CmsScoreRecord bean where bean.order.id = :orderId");
        finder.setParam("orderId",orderId);

        return find(finder);
    }
}
