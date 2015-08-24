package com.dcms.cms.statistic;

import com.dcms.cms.entity.main.Content;
import com.dcms.cms.manager.assist.CmsDictionaryMng;
import com.dcms.cms.manager.main.ContentMng;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daily on 2015/8/23.
 */
@Service
public class GiftPoolMng {
    private static List<Integer> POOL = new ArrayList<Integer>();


    public synchronized void clear(){
        POOL.clear();
    }

    public synchronized List<Integer> getPool(){
        if (POOL == null || POOL.isEmpty()){
            //获取活动奖品
            String channelId = dicMng.findValue("raffle","栏目ID").getValue();
            String total = dicMng.findValue("raffle","奖品种类数").getValue();

            List<Content> raffles = contentMng.getContentByChnId(Integer.valueOf(channelId),Integer.valueOf(total));
            //拼凑奖池
            for (Content raffle : raffles){
                String probability = raffle.getAttr().get("probability");
                int i = Integer.valueOf(probability);

                for (int temp = 0 ; temp < i ; temp ++){
                    POOL.add(raffle.getId());
                }
            }
        }

        return POOL;
    }





    @Resource
    private ContentMng contentMng;
    @Resource
    private CmsDictionaryMng dicMng;

}
