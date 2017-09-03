package cache;

/**
 * @author sergey
 * created on 03.09.17.
 */
public interface Eviction<K, V> {

    void init(CacheStorage<K, V> cache);
    void evictGet();
    void evictPut();
    void evictRemove();
}
