package com.agoda;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class BaseTest{

    @Test
    public void simpleTest() throws Exception {
        TimeUnit timeUnit=TimeUnit.NANOSECONDS;
        long currentMilli=System.currentTimeMillis();
        System.out.printf("current %d ms converted seconds %d \n",currentMilli,timeUnit.toMillis(currentMilli));
    }
}