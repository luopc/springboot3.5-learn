package com.luopc.learn;

import com.luopc.learn.utils.SequentialIDUtil;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        System.out.println(System.currentTimeMillis());
        Thread.sleep(1000);
        System.out.println(System.currentTimeMillis());

        System.out.println(SequentialIDUtil.nextPkId());
        System.out.println(SequentialIDUtil.shortIdString());
        System.out.println(SequentialIDUtil.shortIdString());
        Thread.sleep(1000);
        System.out.println(SequentialIDUtil.shortIdString());
        System.out.println(SequentialIDUtil.shortIdString());
        Thread.sleep(1000 * 60);
        System.out.println(SequentialIDUtil.nextPkId());
        System.out.println(SequentialIDUtil.shortIdString());
        Thread.sleep(1000);
        System.out.println(SequentialIDUtil.shortIdString());
        //1748757839460
        //1748757785526
    }
}
