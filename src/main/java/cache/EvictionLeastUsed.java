package cache;


import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sergey
 * created on 03.09.17.
 */
class EvictionLeastUsed<K, V> implements Eviction<K, V>{

    private long capacityLimit = RaConfiguration.CAPACITY_POLICY_UNLIMITED;
    private final AtomicLong actualSize = new AtomicLong(0);
    private final AtomicLong getCountTotal = new AtomicLong(0);
    private CacheStorage<K, V> cache;

    EvictionLeastUsed(RaConfiguration<K, V> raConfiguration) {
        this.capacityLimit = raConfiguration.getCapacityPolicyLimit();
    }

    @Override
    public void evictGet() {
        getCountTotal.incrementAndGet();
    }

    @Override
    public void evictPut() {
        actualSize.incrementAndGet();
        if (actualSize.longValue() > capacityLimit) {
            Optional<CacheElement<K, V>> cacheElement =
                    this.cache.getCache().values().stream().min(Comparator.comparingLong(v -> v.getReadCout()));

            cacheElement.ifPresent(kvCacheElement -> this.cache.getCache().remove(kvCacheElement.getKey()));
        }
    }

    @Override
    public void evictRemove() {
        actualSize.decrementAndGet();
    }

    @Override
    public void init(CacheStorage<K, V> cache) {
        this.cache = cache;
    }
}
