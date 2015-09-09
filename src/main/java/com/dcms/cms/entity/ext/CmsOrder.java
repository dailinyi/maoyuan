package com.dcms.cms.entity.ext;

import com.dcms.cms.entity.ext.base.BaseCmsOrder;
import com.dcms.cms.entity.main.CmsUser;

import java.util.Date;

/**
 * Created by dailinyi on 15/8/18.
 */
public class CmsOrder extends BaseCmsOrder {
    public CmsOrder() {
    }

    public CmsOrder(String id, Integer price, Byte rate, String key, CmsUser buyer, CmsUser seller, CmsUser score) {
        super(id,new Date(), new Date(), price, rate, key, buyer, seller, score);
    }
}
