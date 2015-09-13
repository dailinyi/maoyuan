package com.dcms.cms.manager.ext.impl;

import com.dcms.cms.dao.ext.CmsScoreRecordDao;
import com.dcms.cms.entity.ext.CmsScoreRecord;
import com.dcms.cms.manager.ext.CmsScoreRecordMng;
import com.dcms.common.page.Pagination;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public Pagination getPage(String queryOrderId, Integer queryRecordType, String querySendUserName, String queryReceiveUserName, int cpn, int pageSize) {

        return dao.getPage(queryOrderId, queryRecordType, querySendUserName, queryReceiveUserName, cpn, pageSize);
    }

    @Override
    public Pagination getPageByUser(String queryOrderId, Integer id, int pageNo, int pageSize) {
        return dao.getPageByUser(queryOrderId, id, pageNo, pageSize);
}

    @Override
    public List<CmsScoreRecord> findByOrderId(String orderId) {
        return dao.findByOrderId(orderId);
    }

}
