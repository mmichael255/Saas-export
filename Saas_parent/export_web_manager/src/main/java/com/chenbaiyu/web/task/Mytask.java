package com.chenbaiyu.web.task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Mytask {
    public void showTime(){
        System.out.println("當前時間："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
