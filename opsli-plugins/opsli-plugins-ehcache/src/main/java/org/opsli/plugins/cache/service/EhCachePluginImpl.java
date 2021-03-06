package org.opsli.plugins.cache.service;

import lombok.extern.slf4j.Slf4j;
import org.opsli.plugins.cache.msg.EhCacheMsg;
import org.springframework.cache.Cache;
import org.opsli.plugins.cache.EhCachePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.cache.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 21:08
 * @Description: EhCachePlugin 实现类
 */
@Slf4j
@Service
public class EhCachePluginImpl implements EhCachePlugin {

    @Autowired(required = false)
    CacheManager cacheManager;

    @Override
    public boolean put(String cacheName, String key, Object value) {
        if(cacheManager == null){
            return false;
        }
        boolean ret = false;
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if(cache != null){
                cache.put(key,value);
                ret = true;
            }
        } catch (Exception e) {
            log.error(EhCacheMsg.EXCEPTION_PUT.getMessage()+"：{}",e.getMessage());
        }
        return ret;
    }

    @Override
    public Object get(String cacheName, String key) {
        if(cacheManager == null){
            return null;
        }
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if(cache != null){
                return cache.get(key);
            }
        } catch (Exception e) {
            log.error(EhCacheMsg.EXCEPTION_GET.getMessage()+"：{}",e.getMessage());
        }
        return null;
    }

    @Override
    public <V> V get(String cacheName, String key, Class<V> vClass) {
        if(cacheManager == null){
            return null;
        }
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if(cache != null){
                return cache.get(key,vClass);
            }
        } catch (Exception e) {
            log.error(EhCacheMsg.EXCEPTION_GET.getMessage()+"：{}", e.getMessage());
        }
        return null;
    }

    @Override
    public boolean delete(String cacheName, String key) {
        if(cacheManager == null){
            return false;
        }
        boolean ret = false;
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if(cache != null){
                cache.evict(key);
                ret = true;
            }
        } catch (Exception e) {
            log.error(EhCacheMsg.EXCEPTION_DEL.getMessage()+"：{}", e.getMessage());
        }
        return ret;
    }

}
