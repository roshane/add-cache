package com.agoda.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author roshane
 */

/*
 * The AddressCache has a max age for the elements it's storing, an add method
 * for adding elements, a remove method for removing, a peek method which
 * returns the most recently added element, and a take method which removes
 * and returns the most recently added element.
 */
public class AddressCache {

    private static final long INITIAL_DELAY = 100;
    private final long maxAge;
    private final TimeUnit timeUnit;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final ConcurrentMap<Object, ValueWrapper> store = new ConcurrentHashMap<>();

    // TODO: 4/7/17 added for test purpose only
    private static final Logger logger = LoggerFactory.getLogger(AddressCache.class);

    /**
     * wrapper class holding the actual cached value
     */
    private class ValueWrapper {
        private final InetAddress value;
        private final long addedTime;
        private final long expiryTime;

        public ValueWrapper(InetAddress value) {
            this.value = value;
            this.addedTime = System.nanoTime();
            this.expiryTime = getExpiryTime(this.addedTime);
        }

        private long getExpiryTime(long timeAdded) {
            return addedTime + timeUnit.toNanos(maxAge);
        }

        public InetAddress getValue() {
            return value;
        }

        public long getAddedTime() {
            return addedTime;
        }

        public long getExpiryTime() {
            return expiryTime;
        }

        @Override
        public String toString() {
            return "ValueWrapper{" +
                    "value=" + value +
                    ", addedTime=" + addedTime +
                    ", expiryTime=" + expiryTime +
                    '}';
        }
    }

    public AddressCache(long maxAge, TimeUnit unit) {

        this.maxAge = maxAge;
        this.timeUnit = unit;

        executorService.scheduleWithFixedDelay(() -> {
//            logger.info("cache cleaning job run");
            store.values().stream()
                    .filter(v -> v.expiryTime < System.nanoTime())
                    .forEach(v -> {
                        logger.info("cleaning cached entry [{}] at [{}]", v.toString(),System.nanoTime());
                        store.remove(v.value.hashCode());
                    });
        }, INITIAL_DELAY, maxAge, TimeUnit.MILLISECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!executorService.isShutdown()) {
                executorService.shutdown();
                logger.info("shutting down cache cleaner");
            }
        }));
    }

    /**
     * add() method must store unique elements only (existing elements must be ignored).
     * This will return true if the element was successfully added.
     *
     * @param address
     * @return
     */
    public boolean add(InetAddress address) {
        if (!store.containsKey(address.hashCode())) {
            store.put(address.hashCode(), new ValueWrapper(address));
            return true;
        }
        return false;
    }

    /**
     * remove() method will return true if the address was successfully removed
     *
     * @param address
     * @return
     */
    public boolean remove(InetAddress address) {
        ValueWrapper previous = store.remove(address.hashCode());
        return previous != null;
    }

    /**
     * The peek() method will return the most recently added element,
     * null if no element exists.
     *
     * @return
     */
    public InetAddress peek() {
        //TODO ideally we should use a parallel stream since many entries to process
        Map<Object, ValueWrapper> sortedMap = store.entrySet()
                .stream()
                .sorted((v1,v2)->Long.valueOf(v2.getValue().addedTime)
                        .compareTo(v1.getValue().addedTime))
                .limit(1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (v1, v2) -> v2, LinkedHashMap::new));

        return sortedMap.get(sortedMap.keySet()
                .toArray(new Object[sortedMap.keySet().size()])[0])
                .value;
    }

    /**
     * take() method retrieves and removes the most recently added element
     * from the cache and waits if necessary until an element becomes available.
     *
     * @return
     */
    public InetAddress take() {
        // TODO: 4/3/17
        return null;
    }

}
