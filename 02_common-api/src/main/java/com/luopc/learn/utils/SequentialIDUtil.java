package com.luopc.learn.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class SequentialIDUtil {
    private static long tmpID = 0;
    private static final long LOCK_TIME = 1;
    private static final long INCREASE_STEP = 1;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyMMddHHmmssSSS");
    private static final Lock LOCK = new ReentrantLock();

    public static long nextPkId() {
        //当前：（年、月、日、时、分、秒、毫秒）
        long timeCount;
        try {
            if (LOCK.tryLock(LOCK_TIME, TimeUnit.SECONDS)) {
                timeCount = Long.parseLong(SDF.format(new Date()));
                try {
                    if (tmpID < timeCount) {
                        tmpID = timeCount;
                    } else {
                        tmpID += INCREASE_STEP;
                        timeCount = tmpID;
                    }
                    return timeCount;
                } finally {
                    LOCK.unlock();
                }
            } else {
                log.error("lock failed");
                return nextPkId();
            }
        } catch (InterruptedException e) {
            log.error("Unable to generate PK ID.");
            String randomNum = String.format("%04d", RandomNumberUtil.randomLong(1, 9999));
            return Long.parseLong(SDF.format(new Date()) + randomNum);
        }
    }

    public static long shortIdLong() {
        return Long.parseLong(shortIdString());
    }


    public static String shortIdString() {
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear()).substring(2);
        String dayHours = String.format("%03d", now.getDayOfYear()) + String.format("%02d", now.getHour());
        String minutes = String.format("%02d", now.getMinute());
        //当前：（年、月、日、时、分、秒、毫秒）
        String current = String.valueOf(System.currentTimeMillis()).substring(9);
        return year + dayHours + minutes + current;
    }

}
