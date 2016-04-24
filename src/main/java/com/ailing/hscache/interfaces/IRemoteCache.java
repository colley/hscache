/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.ailing.hscache.interfaces;


/**
 *@FileName  IRemoteCache.java
 *@Date  16-4-24 下午2:55
 *@author Ma Yuanchao
 *@version 1.0
 */
public interface IRemoteCache {
    public void remove(String key);

    public <T> T get(String key);

    public <T> T put(String key, T val, int expid);
}
