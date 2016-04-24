/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.aling.hscache.sample;

import com.ailing.hscache.HsCacheException;
import com.ailing.hscache.impl.AbstractHsCache;

import java.util.HashMap;
import java.util.Map;


/**
 *@FileName  HscahceSample.java
 *@Date  16-4-24 下午3:05
 *@author Ma Yuanchao
 *@version 1.0
 */
public class HscahceSample extends AbstractHsCache {
    @Override
    public Map<Object, Object> getData() throws HsCacheException {
        Map<Object, Object> retdata = new HashMap<Object, Object>();
        retdata.put("123", "123");
        retdata.put("1234", "1234");
        return retdata;
    }
}
