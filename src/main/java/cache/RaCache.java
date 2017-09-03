package cache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.expiry.Duration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sergey
 * created on 27.08.17.
 */
public class RaCache<K, V, C extends Configuration<K, V>> implements Cache<K, V> {

    private final CacheStorage<K, V> storage;
    private final ScheduledExecutorService expirationService =  Executors.newScheduledThreadPool(1);
    private final Configuration<K, V> configuration;
    private Eviction<K, V> eviction = new EvictionNotWorks<>();

    <C extends Configuration<K, V>> RaCache(String cacheName, C configuration) {
        this.storage = new CacheStorage<>();
        this.configuration = configuration;
        processConfiguration();
    }

    private void processConfiguration() {
        if (configuration instanceof RaConfiguration) {
            RaConfiguration<K, V> raConfiguration = (RaConfiguration<K, V>) configuration;
            if (raConfiguration.getCapacityPolicyLimit() != RaConfiguration.CAPACITY_POLICY_UNLIMITED) {
                eviction = new EvictionLeastUsed<>(raConfiguration);
                eviction.init(storage);
            }
            Duration cacheExpiration = raConfiguration.getCacheExpiration();
            if (!cacheExpiration.isEternal()) {
                final long checkPeriod = (long) (cacheExpiration.getDurationAmount() / 1.8);
                expirationService.scheduleWithFixedDelay(
                        () -> removeExpiredValue(cacheExpiration.getDurationAmount(), cacheExpiration.getTimeUnit()), 0,
                        checkPeriod, cacheExpiration.getTimeUnit());
            }
        }
    }

    private void removeExpiredValue(long amountToAdd, TimeUnit unit) {
        final LocalDateTime now = LocalDateTime.now();
        storage.getCache().forEach((k, kvCacheElement) -> {
            if (kvCacheElement.getCreated().plusSeconds(unit.toSeconds(amountToAdd)).isBefore(now)) {
                remove(k);
            }
        });
    }

    @Override
    public V get(K key) {
        CacheElement<K, V> cacheElement = storage.getCache().get(key);
        if (cacheElement != null) {
            V value = cacheElement.getValue();
            eviction.evictGet();
            return value;
        }
        return null;
    }

    @Override
    public Map<K, V> getAll(Set keys) {
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return storage.getCache().containsKey(key);
    }

    @Override
    public void loadAll(Set keys, boolean replaceExistingValues, CompletionListener completionListener) {

    }

    @Override
    public void put(K key, V value) {
        CacheElement<K, V> cacheElement = new CacheElement<>(key, value);
        eviction.evictPut();
        storage.getCache().put(key, cacheElement);
    }



    @Override
    public V getAndPut(K key, V value) {
        return null;
    }

    @Override
    public void putAll(Map map) {

    }

    @Override
    public boolean putIfAbsent(K key, V value) {
        return false;
    }

    @Override
    public boolean remove(K key) {
        boolean result = storage.getCache().remove(key) != null;
        eviction.evictRemove();
        return result;
    }

    @Override
    public boolean remove(K key, V oldValue) {
        return false;
    }

    @Override
    public V getAndRemove(K key) {
        return null;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return false;
    }

    @Override
    public boolean replace(K key, V value) {
        return false;
    }

    @Override
    public V getAndReplace(K key, V value) {
        return null;
    }

    @Override
    public void removeAll(Set keys) {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public void clear() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public CacheManager getCacheManager() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }

    @Override
    public void registerCacheEntryListener(CacheEntryListenerConfiguration cacheEntryListenerConfiguration) {

    }

    @Override
    public void deregisterCacheEntryListener(CacheEntryListenerConfiguration cacheEntryListenerConfiguration) {

    }

    @Override
    public Object unwrap(Class clazz) {
        return null;
    }

    @Override
    public Map invokeAll(Set keys, EntryProcessor entryProcessor, Object... arguments) {
        return null;
    }

    @Override
    public Object invoke(Object key, EntryProcessor entryProcessor, Object... arguments) throws EntryProcessorException {
        return null;
    }

    @Override
    public Configuration<K, V> getConfiguration(Class clazz) {
        return configuration;
    }
}
