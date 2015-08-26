package com.dcms.cms.dao.ext.impl;

import com.dcms.cms.dao.ext.CmsActivityRecordDao;
import com.dcms.cms.entity.ext.CmsActivityRecord;
import com.dcms.common.hibernate3.Finder;
import com.dcms.common.hibernate3.HibernateBaseDao;
import com.dcms.common.page.Pagination;
import org.apache.commons.lang.StringUtils;
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
    @Override
    public CmsActivityRecord findById(Integer i ){
        return super.get(i);
    }

    @Override
    public Pagination getPage(Integer queryIsWin,Integer queryIsOffer, String queryActivityUserName, int cpn, int pageSize) {
        Finder finder = Finder.create(" from CmsActivityRecord bean where 1=1");

        if (StringUtils.isNotBlank(queryActivityUserName)){
            finder.append(" and bean.activityUser.username = :queryActivityUserName ");
            finder.setParam("queryActivityUserName",queryActivityUserName);
        }

        if (queryIsOffer != null){
            finder.append(" and bean.isOffer = :queryIsOffer ");
            if (queryIsOffer == 0 ){
                finder.setParam("queryIsOffer",false);
            } else {
                finder.setParam("queryIsOffer",true);
            }
            queryIsWin = 1;
        }

        if (queryIsWin != null){
            if (queryIsWin == 0){
                finder.append(" and bean.activityContent = null");
            } else {
                finder.append(" and bean.activityContent != null");
            }
        }

        finder.append(" order by activity_record_id desc");

        return find(finder,cpn,pageSize);
    }
}
