package top.gabin.oa.web.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public interface SystemCacheManager extends CacheManager {

    void put(String group, String key, Object value);

    Cache.ValueWrapper get(String group, String key);

    void remove(String group, String key);

    void update(String group, String key, Object value);

    void refresh(String group);
}
