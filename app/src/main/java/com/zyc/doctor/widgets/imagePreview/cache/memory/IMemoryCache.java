package com.zyc.doctor.widgets.imagePreview.cache.memory;

/**
 * Created by Kyle on 2015/12/14.
 */
public interface IMemoryCache<K, V> {

    /**
     *  增
     * */
    boolean put(K key, V value);

    /**
     *  查
     * */
    V get(K key);

    /**
     *  删
     * */
    V remove(K key);

    /**
     *  删所有内存缓存
     * */
    void clear();

}