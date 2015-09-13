package com.dcms.cms.statistic.utils;

import org.springframework.stereotype.Service;

/**
 * Created by Daily on 2015/8/30.
 */
@Service
public class KeyCodeMng {

    public static String getNewCode(){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            sb.append((int) (Math.random() * 9000 + 1000));
            sb.append(" ");
        }


        return sb.toString().substring(0,19);
    }

    public static void main(String[] args) {
        System.out.println(new KeyCodeMng().getNewCode());
    }
}
