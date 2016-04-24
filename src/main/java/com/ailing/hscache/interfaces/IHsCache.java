/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.ailing.hscache.interfaces;

import com.ailing.hscache.HsCacheException;

import java.util.Map;


/**
 *@FileName  ICache.java
 *@Date  16-4-13 下午5:27
 *@author Ma Yuanchao
 *@version 1.0
 */
public interface IHsCache {
    public abstract void refresh() throws HsCacheException;

    public abstract Object getObject(Object paramObject)
        throws HsCacheException;

    public abstract void setRemote(int expired, boolean isRemote);

    public abstract boolean containsKey(Object paramObject)
        throws HsCacheException;

    public abstract Map<Object, Object> getAll() throws HsCacheException;

    public abstract boolean isCacheLoaded();
}
