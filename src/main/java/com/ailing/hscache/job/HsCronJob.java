/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.ailing.hscache.job;

import com.ailing.hscache.interfaces.IHsCache;
import com.ailing.hscache.xml.HsCache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 *@FileName  CronJob.java
 *@Date  16-4-13 下午5:39
 *@author Ma Yuanchao
 *@version 1.0
 */
public class HsCronJob implements Job {
    private static transient Log log = LogFactory.getLog(HsCronJob.class);

    public void execute(JobExecutionContext jobExecutionContext)
        throws JobExecutionException {
        try {
            JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
            IHsCache cache = (IHsCache) data.get(HsCache.SCHEDULER_VAR);
            if (cache.isCacheLoaded()) {
                cache.refresh();
                if (log.isInfoEnabled()) {
                    log.info("The cached data loaded by the class named:" + cache.getClass().getName() +
                        ",has refreshed successfully");
                }
            } else if (log.isInfoEnabled()) {
                log.info("The cached data loaded by the class named:" + cache.getClass().getName() +
                    ", has not loaded,so will not refresh it immediately and do it by the scheduler timely.");
            }
        } catch (Exception ex) {
            log.error("The schedule job failed to refresh the cached data:", ex);
        }
    }
}
