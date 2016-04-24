/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.ailing.hscache.xml;

import java.util.ArrayList;
import java.util.List;


/**
 *@FileName  Caches.java
 *@Date  16-4-13 下午5:33
 *@author Ma Yuanchao
 *@version 1.0
 */
public class HsCaches {
    private List<HsCache> list = new ArrayList<HsCache>();
    private HsQuartz quartz;

    public HsQuartz getQuartz() {
        return this.quartz;
    }

    public void setQuartz(HsQuartz quartz) {
        this.quartz = quartz;
    }

    public void addCache(HsCache cache) {
        this.list.add(cache);
    }

    public HsCache[] getCaches() {
        return (HsCache[]) this.list.toArray(new HsCache[0]);
    }
}
