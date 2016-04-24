/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.ailing.hscache.factory;

import com.ailing.hscache.HsCacheException;
import com.ailing.hscache.interfaces.IHsCache;
import com.ailing.hscache.job.HsCronJob;
import com.ailing.hscache.xml.HsCache;
import com.ailing.hscache.xml.HsCaches;
import com.ailing.hscache.xml.HsProperty;
import com.ailing.hscache.xml.HsQuartz;
import com.ailing.hscache.xml.XMLHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

import org.quartz.impl.StdSchedulerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 *@FileName  CacheFactory.java
 *@Date  16-4-13 下午6:18
 *@author Ma Yuanchao
 *@version 1.0
 */
@SuppressWarnings("rawtypes")
public final class HsCacheFactory {
    private static transient Log logger = LogFactory.getLog(HsCacheFactory.class);
    private static final Map<Class, IHsCache> INSTANCE = new HashMap<Class, IHsCache>();
    private static boolean IS_READONLY;

    static {
        initCacheFactory();
    }

    public static void initCacheFactory() {
        try {
            HsCaches caches = XMLHelper.getInstance().getCaches();
            Map<String, Class> clazzes = new HashMap<String, Class>();
            IS_READONLY = caches.getQuartz().isReadonly();
            HsCache[] objCache = caches.getCaches();
            for (int i = 0; i < objCache.length; i++) {
                IHsCache cacheInstance = null;
                try {
                    if (objCache[i].isSpringFactory() && (SpringHscacheContext.getContext() != null)) {
                        cacheInstance = (IHsCache) SpringHscacheContext.getBean(objCache[i].getId());
                    } else {
                        cacheInstance = (IHsCache) Class.forName(objCache[i].getId()).newInstance();
                    }
                } catch (Throwable ex) {
                    logger.error("The class:" + objCache[i].getId() + " failed to instantiate.", ex);
                    continue;
                }

                cacheInstance.setRemote(objCache[i].getExpired(), objCache[i].isRemote());
                clazzes.put(objCache[i].getId(), cacheInstance.getClass());
                INSTANCE.put(cacheInstance.getClass(), cacheInstance);
                if (objCache[i].getInit().equalsIgnoreCase("true")) {
                    try {
                        cacheInstance.refresh();
                        if (logger.isDebugEnabled()) {
                            logger.debug("The class:" + objCache[i].getId() +
                                " ,refresh data completely,load data completely,cache size is:" +
                                cacheInstance.getAll().size());
                        }
                    } catch (Throwable ex) {
                        logger.error("The class:" + objCache[i].getId() + " ,failed to refresh data", ex);
                    }
                } else if (logger.isDebugEnabled()) {
                    logger.debug("The class:" + objCache[i].getId() + " ,load delay......");
                }
            }

            HsQuartz objQuartz = caches.getQuartz();
            HsProperty[] property = objQuartz.getPropertys();
            Properties properties = new Properties();
            for (int i = 0; i < property.length; i++) {
                properties.put(property[i].getName(), property[i].getValue());
            }

            if (logger.isDebugEnabled()) {
                logger.debug("The properties of quartz:" + properties.toString());
            }

            SchedulerFactory objSchedulerFactory = new StdSchedulerFactory(properties);
            Scheduler objScheduler = objSchedulerFactory.getScheduler();
            for (int i = 0; i < objCache.length; i++) {
                if ((objCache[i].getPropertys() != null) && (objCache[i].getPropertys().length != 0) &&
                        (objCache[i].getPropertys()[0].getName().equalsIgnoreCase("cronExpression"))) {
                    JobDetail job = new JobDetail("CronJob" + i, "CronJobGrp" + i, HsCronJob.class);
                    Trigger trigger = new CronTrigger("CronTrigger" + i, "CronTriggerGrp" + i,
                            objCache[i].getPropertys()[0].getValue());

                    job.getJobDataMap().put(HsCache.SCHEDULER_VAR, INSTANCE.get(clazzes.get(objCache[i].getId())));

                    objScheduler.scheduleJob(job, trigger);
                    if (logger.isDebugEnabled()) {
                        logger.debug("The class:" + objCache[i].getId() +
                            ",refresh the useful data which have accessed by the application well-timed,the time expression is:" +
                            objCache[i].getPropertys()[0].getValue());
                    }
                }
            }

            objScheduler.start();
        } catch (Throwable ex) {
            logger.error("Failed to load data:", ex);
            throw new RuntimeException(ex);
        } finally {
            if (IS_READONLY) {
                logger.error("cache is readonly mode");
            } else {
                logger.error("cache is writeable mode");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class clazz, Object key) {
        IHsCache cacheInstance = (IHsCache) INSTANCE.get(clazz);
        if (cacheInstance != null) {
            try {
                return (T) cacheInstance.getObject(key);
            } catch (HsCacheException e) {
                logger.error("get cache data error", e);
            } catch (Exception e) {
                logger.error("Illegal Argument cache data", e);
            }
        }

        return null;
    }

    public static boolean containsKey(Class clazz, Object key) {
        IHsCache cacheInstance = (IHsCache) INSTANCE.get(clazz);
        if (cacheInstance != null) {
            try {
                return cacheInstance.containsKey(key);
            } catch (Exception e) {
                logger.error("Cannot get cache of class " + clazz.getName(), e);
            }
        }

        return false;
    }

    public static Map<Object, Object> getAll(Class clazz) {
        IHsCache cacheInstance = (IHsCache) INSTANCE.get(clazz);
        Map<Object, Object> alldatas = new HashMap<Object, Object>();
        if (cacheInstance == null) {
            logger.error("Cannot get cache of class " + clazz.getName());
            return Collections.unmodifiableMap(new HashMap<Object, Object>());
        }

        try {
            alldatas = cacheInstance.getAll();
        } catch (Exception e) {
            logger.error("Cannot get cache of class " + clazz.getName(), e);
        }

        if (IS_READONLY) {
            return Collections.unmodifiableMap(alldatas);
        }

        return alldatas;
    }

    public Map<Class, IHsCache> getCacheInstances() {
        return INSTANCE;
    }
}
