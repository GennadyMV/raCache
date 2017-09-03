package cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sergey
 * created on 03.09.17.
 */
class CacheStorage<K, V> {
    private final Map<K, CacheElement<K, V>> cache;

    CacheStorage() {
        this.cache = new ConcurrentHashMap<>();
    }

    Map<K, CacheElement<K, V>> getCache() {
        return cache;
    }
}
