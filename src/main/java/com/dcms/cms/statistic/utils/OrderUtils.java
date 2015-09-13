package com.dcms.cms.statistic.utils;

import com.dcms.cms.entity.main.CmsUser;

import java.util.Date;

/**
 * Created by Daily on 2015/9/12.
 */
public class OrderUtils {
    public static String getNewOrderId(CmsUser buyer,CmsUser seller){
        StringBuilder sb = new StringBuilder();

        sb.append( new Date().getTime() + "").append(buyer.getId()).append(seller.getId());


        return sb.toString().trim();
    }


}
