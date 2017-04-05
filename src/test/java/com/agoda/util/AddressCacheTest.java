package com.agoda.util;

import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Created by roshane on 4/5/2017.
 */
public class AddressCacheTest {

    private AddressCache addressCache;

    private final TimeUnit timeUnit=TimeUnit.SECONDS;
    private final long MAX_AGE=2;

    @Before
    public void setUpCache()throws Exception{
        addressCache=new AddressCache(3, TimeUnit.SECONDS);
    }

    @Test
    public void add() throws Exception {
        boolean added = addressCache.add(InetAddress.getLocalHost());
        assertTrue("adding value to cache",added);
    }

    @Test
    public void cacheExpiry() throws Exception {
        boolean added = addressCache.add(InetAddress.getLocalHost());
        Thread.sleep(timeUnit.toMillis(MAX_AGE+1));
//        addressCache.peek()
    }

    @Test
    public void remove() throws Exception {

    }

    @Test
    public void peek() throws Exception {

    }

    @Test
    public void take() throws Exception {

    }

}