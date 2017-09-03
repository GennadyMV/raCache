package cache;

import java.time.LocalDateTime;

/**
 * @author sergey
 * created on 02.09.17.
 */
class CacheElement<K, V> {
    private final K key;
    private V value;

    private final LocalDateTime created;
    private LocalDateTime lastModified;
    private LocalDateTime lastRead;

    private long readCout = 0;
    private long modifyCout = 0;

    CacheElement(K key, V value) {
        this.key = key;
        this.value = value;
        this.created = LocalDateTime.now();
        this.lastModified = null;
        this.lastRead = null;
    }

    K getKey() {
        return key;
    }

    public void setValue(V value) {
        modifyCout++;
        this.value = value;
    }

    V getValue() {
        this.lastRead = LocalDateTime.now();
        this.readCout++;
        return value;
    }

    LocalDateTime getCreated() {
        return created;
    }

    LocalDateTime getLastModified() {
        return lastModified;
    }

    LocalDateTime getLastRead() {
        return lastRead;
    }

    long getReadCout() {
        return readCout;
    }

    long getModifyCout() {
        return modifyCout;
    }
}
