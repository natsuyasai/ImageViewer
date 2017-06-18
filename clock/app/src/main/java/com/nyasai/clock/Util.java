package com.nyasai.clock;

import java.util.Date;

public class Util {

    // HH:mm:ss→ミリ秒
    public static long DateToMSec(Date date){
        long lMSec = date.getTime();
        return lMSec;
    }
    // ミリ秒→HH:mm:ss
    public static Date MSecToDate(long lMsec){
        Date date = new Date(lMsec);
        return date;
    }

}
