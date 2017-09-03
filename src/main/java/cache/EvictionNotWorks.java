package cache;

/**
 * @author sergey
 * created on 03.09.17.
 */
public class EvictionNotWorks<K, V> implements Eviction<K, V> {

    @Override
    public void init(CacheStorage<K, V> cache) {

    }

    @Override
    public void evictGet() {

    }

    @Override
    public void evictPut() {

    }

    @Override
    public void evictRemove() {

    }
}
