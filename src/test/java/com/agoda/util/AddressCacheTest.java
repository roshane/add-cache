package com.agoda.util;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThat;
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
    }

    @Test
    public void remove() throws Exception {
        boolean add = addressCache.add(InetAddress.getLoopbackAddress());
        assertTrue(add);
        boolean remove = addressCache.remove(InetAddress.getLoopbackAddress());
        assertTrue(remove);
    }

    @Test
    public void peek() throws Exception {
        addressCache.add(InetAddress.getByName("google.com"));
        addressCache.add(InetAddress.getByName("yahoo.com"));
        addressCache.add(InetAddress.getByName("youtube.com"));
        InetAddress peek = addressCache.peek();
        assertThat("before expiry",peek, Matchers.is(InetAddress.getByName("youtube.com")));
        Thread.sleep(3000L);
        assertThat("after expiry ",addressCache.peek(),Matchers.is(InetAddress.getByName("yahoo.com")));
    }

    @Test
    public void take() throws Exception {

    }

    @Test
    public void scratchTest() throws Exception {
        InetAddress address = InetAddress.getByName("google.com");
        System.out.println("\n>>>"+address+"\n");
        System.out.println(address);
        Map<Long, String> longStringMap = Collections.singletonMap(1L, "hell world");
        Long[] keysSorted = longStringMap.keySet().toArray(new Long[longStringMap.keySet().size()]);
        System.out.println(longStringMap.get(keysSorted[0]));
        System.out.println(">> "+TimeUnit.SECONDS.toNanos(MAX_AGE));
    }
}