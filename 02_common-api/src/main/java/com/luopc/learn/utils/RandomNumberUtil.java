package com.luopc.learn.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author by Robin
 * @className NumberUtil
 * @description TODO
 * @date 2024/1/16 0016 18:54
 */
public class RandomNumberUtil {


    public static int nextInt(int inbound) {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(inbound);
    }

    public static double nextDouble(double inbound) {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextDouble() * inbound;
    }

    public static double randomDouble(int maxNumberOfDecimals, int min, int max) {
        return decimalBetween(min, max).setScale(maxNumberOfDecimals, RoundingMode.HALF_UP).doubleValue();
    }

    private static BigDecimal decimalBetween(long min, long max) {
        return new BigDecimal(randomLong(min, max));
    }

    public static int randomNumber(long min, long max) {
        float a = min + (max - min) * (new Random().nextFloat());
        int b = (int) a;
        return ((a - b) > 0.5 ? 1 : 0) + b;
    }

    public static long randomLong(long min, long max) {
        if (min == max) {
            return min;
        } else {
            return min + (((long) (new Random().nextDouble() * (max - min))));
        }
    }
}
