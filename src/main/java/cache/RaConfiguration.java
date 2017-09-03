package cache;

import javax.cache.Cache;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.EternalExpiryPolicy;
import java.util.HashSet;

/**
 * @author sergey
 * created on 02.09.17.
 */
public class RaConfiguration<K, V> implements Configuration<K, V> {
    static long CAPACITY_POLICY_UNLIMITED = -1;

    private final Class<K> keyType;
    private final Class<V> valueType;
    private final Duration cacheExpiration;
    private long capacityPolicyLimit = CAPACITY_POLICY_UNLIMITED;

    public RaConfiguration(Duration cacheExpiration) {
        this.cacheExpiration = cacheExpiration;
        this.keyType =   (Class<K>)Object.class;
        this.valueType = (Class<V>)Object.class;
    }

    @Override
    public Class<K> getKeyType() {
        return this.keyType;
    }

    @Override
    public Class<V> getValueType() {
        return this.valueType;
    }

    @Override
    public boolean isStoreByValue() {
        return false;
    }

    Duration getCacheExpiration() {
        return cacheExpiration;
    }

    public void setCapacityPolicyLimit(long capacityPolicyLimit) {
        this.capacityPolicyLimit = capacityPolicyLimit;
    }

    long getCapacityPolicyLimit() {
        return capacityPolicyLimit;
    }
}
