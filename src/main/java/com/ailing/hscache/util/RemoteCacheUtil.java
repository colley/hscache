/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.ailing.hscache.util;

import com.ailing.hscache.factory.SpringHscacheContext;
import com.ailing.hscache.interfaces.IRemoteCache;


/**
 *@FileName  RemoteCacheUtil.java
 *@Date  16-4-24 下午2:57
 *@author Ma Yuanchao
 *@version 1.0
 */
public class RemoteCacheUtil {
    private static IRemoteCache remoteCache = (IRemoteCache) SpringHscacheContext.getBean("remoteCache");

    public static void remove(String key) {
        remoteCache.remove(key);
    }

    public static <T> T get(String key) {
        return remoteCache.get(key);
    }

    public static <T> T put(String key, T val, int expid) {
        return remoteCache.put(key, val, expid);
    }
}
