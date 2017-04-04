package com.agoda.util;

import java.net.InetAddress;
import java.util.concurrent.*;

/**
 * @author roshane
 *         Created by roshane on 4/3/17.
 */

/*
 * The AddressCache has a max age for the elements it's storing, an add method
 * for adding elements, a remove method for removing, a peek method which
 * returns the most recently added element, and a take method which removes
 * and returns the most recently added element.
 */
public class AddressCache {

    private final long maxAge;
    private final TimeUnit timeUnit;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private static final ConcurrentMap<Object, InetAddress> store = new ConcurrentHashMap<>();

    private static final long INITIAL_DELAY = 1;

    private static class ValueWrapper<T> {
        private final T value;
        private final long addedTime;
        private final long timeToLive;

        public ValueWrapper(T value) {
            this.value = value;
            this.addedTime = System.currentTimeMillis();
            this.timeToLive = getTTL(this.addedTime);
        }

        private long getTTL(long timeAdded) {
            // TODO: 4/3/17
            return 0;
        }

        public T getValue() {
            return value;
        }

        public long getAddedTime() {
            return addedTime;
        }

        public long getTimeToLive() {
            return timeToLive;
        }
    }

    public AddressCache(long maxAge, TimeUnit unit) {
        this.maxAge = maxAge;
        this.timeUnit = unit;
        executorService.schedule(() -> {

        }, INITIAL_DELAY, unit);
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
            store.put(address.hashCode(), address);
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
        InetAddress previous = store.remove(address.hashCode());
        return previous == null;
    }

    /**
     * The peek() method will return the most recently added element,
     * null if no element exists.
     *
     * @return
     */
    public InetAddress peek() {
        // TODO: 4/3/17
        return null;
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
