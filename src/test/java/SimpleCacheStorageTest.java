import cache.RaConfiguration;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static javax.cache.Caching.JAVAX_CACHE_CACHING_PROVIDER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author sergey
 * created on 27.08.17.
 */
public class SimpleCacheStorageTest {

    @Test
    public void simpleCache() {
        //resolve a cache manager
        System.setProperty(JAVAX_CACHE_CACHING_PROVIDER, "cache.RaCachingProvider");
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        //configure the cache
        RaConfiguration<String, Integer> config = new RaConfiguration<>(Duration.ETERNAL);
        //create the cache
        Cache<String, Integer> cache = cacheManager.createCache("simpleCache", config);
        //cache operations
        String key = "key";
        Integer value1 = 1;
        cache.put(key, value1);
        Integer value2 = cache.get(key);
        assertEquals(value1, value2);
        cache.remove(key);
        assertNull(cache.get(key));
    }

    @Test
    public void ExpiryPolicyTest() {
        System.setProperty(JAVAX_CACHE_CACHING_PROVIDER, "cache.RaCachingProvider");
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        final int cacheExpirationSec = 10;
        RaConfiguration<String, Integer> config = new RaConfiguration<>(new Duration(TimeUnit.SECONDS, cacheExpirationSec));
        Cache<String, Integer> cache = cacheManager.createCache("simpleCache", config);
        String key = "key";
        Integer value1 = 1;
        cache.put(key, value1);
        sleep(cacheExpirationSec);
        sleep(1);
        assertNull(cache.get(key));
    }

    @Test
    public void CapacityPolicyTest() {
        System.setProperty(JAVAX_CACHE_CACHING_PROVIDER, "cache.RaCachingProvider");
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        final int capacityPolicyLimit = 10;
        RaConfiguration<String, Integer> config = new RaConfiguration<>(Duration.ETERNAL);
        config.setCapacityPolicyLimit(capacityPolicyLimit);
        Cache<String, Integer> cache = cacheManager.createCache("simpleCache", config);

        final Set<String> elementKeys = new HashSet<>();
        for (int idx = 0; idx < capacityPolicyLimit; idx++) {
            String key = "key:" + idx;
            elementKeys.add(key);
            cache.put(key, 1);
        }
        cache.put("AddkeyTest", 111);

        Set<String> evictedKeys = elementKeys.stream().filter(key -> !cache.containsKey(key)).collect(Collectors.toSet());
        assertEquals("evicted", 1, evictedKeys.size());
    }

    private void sleep(long sec) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(sec));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
