/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.ailing.hscache.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ailing.hscache.HsCacheException;
import com.ailing.hscache.interfaces.IHsCache;
import com.ailing.hscache.util.RemoteCacheUtil;


/**
 *@FileName  AbstractCache.java
 *@Date  16-4-13 下午5:40
 *@author Ma Yuanchao
 *@version 1.0
 */
public abstract class AbstractHsCache implements IHsCache {
    private static transient Log log = LogFactory.getLog(AbstractHsCache.class);
    private Map<Object, Object> cache = new HashMap<Object, Object>();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private AtomicLong COUNT = new AtomicLong(0L);
    private volatile boolean isFitstInit = false;
    private int expired = 1 * 60 * 24; //24小时
    private boolean isRemote = false;
    private String cacheKey = getClass().getName() + "#getObjectByRemote";

    public void refresh() throws HsCacheException {
        rwl.writeLock().lock();
        try {
            refreshData();
        } finally {
            rwl.writeLock().unlock();
        }
    }

    private Map<Object, Object> refreshData() throws HsCacheException {
        log.error("Starting to load " + getClass().getName());
        long startTime = System.currentTimeMillis();
        long oldCount = this.cache.size();
        Map<Object, Object> cachemdatas = getData();
        this.cache = null;
        if (isRemote) {
            //远程缓存，存入memcahced或者redis
        	RemoteCacheUtil.put(cacheKey, cachemdatas, expired);
            this.cache = cachemdatas;
        } else {
            //本地缓存
            this.cache = cachemdatas;
        }

        this.COUNT.incrementAndGet();
        this.isFitstInit = Boolean.TRUE.booleanValue();
        long newCount = this.cache.size();
        log.warn(new StringBuffer().append(getClass()).append(" oldCount=").append(oldCount).append(",newCount=")
                                   .append(newCount).append(",startTime=").append(startTime).append(",endTime=")
                                   .append(System.currentTimeMillis()).toString());
        return cachemdatas;
    }

    public Object getObject(Object key) throws HsCacheException {
        if (isRemote) {
            getRemoteData();
        } else {
            getLocalData();
        }

        return this.cache.get(key);
    }

    public boolean containsKey(Object key) throws HsCacheException {
        if (isRemote) {
            getRemoteData();
        } else {
            getLocalData();
        }

        return this.cache.containsKey(key);
    }

    public Map<Object, Object> getAll() throws HsCacheException {
        if (isRemote) {
            getRemoteData();
        } else {
            getLocalData();
        }

        return this.cache;
    }

    private void getLocalData() throws HsCacheException {
        try {
            rwl.readLock().lock();
            if (!this.isFitstInit) {
                if (log.isDebugEnabled()) {
                    log.debug("The class of cache named :" + getClass().toString() +
                        ",load data in a lazy mode.Now starting to load ......");
                }

                rwl.readLock().unlock();
                rwl.writeLock().lock();
                if (!this.isFitstInit) {
                    refreshData();
                }

                rwl.readLock().lock();
                rwl.writeLock().unlock(); // Unlock write, still hold read
            }
        } finally {
            rwl.readLock().unlock();
        }
    }

    //缓存中数据放入到本地
    private void getRemoteData() throws HsCacheException {
        try {
            rwl.writeLock().lock();
            Map<Object, Object> cachemdatas = RemoteCacheUtil.get(cacheKey);
            if (MapUtils.isEmpty(cachemdatas)) {
                cachemdatas = refreshData();
            } else {
                this.cache = null;
                this.cache = cachemdatas;
            }
        } finally {
            rwl.readLock().unlock();
        }
    }

    public void setRemote(int expired, boolean isRemote) {
        if (expired > 0) {
            this.expired = expired;
        }

        this.isRemote = isRemote;
    }

    public boolean isCacheLoaded() {
        return this.isFitstInit;
    }

    public abstract Map<Object, Object> getData() throws HsCacheException;
}
