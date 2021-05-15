package com.vnapnic.common.service

/**
 * redis operation Service
 * Created by nankai on 2020/3/3.
 */
interface RedisService {
    /**
     * Save properties
     */
    operator fun set(key: String, value: Any, time: Long)

    /**
     *
     * Save properties
     */
    operator fun set(key: String, value: Any)

    /**
     * Get attributes
     */
    fun gets(key: String): Set<Any>?

    /**
     * Get attributes
     */
    operator fun get(key: String): Any?

    /**
     * Delete attribute
     */
    fun del(key: String): Boolean?

    /**
     * Delete attributes in bulk
     */
    fun del(keys: List<String>): Long?

    /**
     * Set expiration time
     */
    fun expire(key: String, time: Long): Boolean?

    /**
     * Get expiration time
     */
    fun getExpire(key: String): Long?

    /**
     * Determine whether there is this attribute
     */
    fun hasKey(key: String): Boolean?

    /**
     * Increment by delta
     */
    fun incr(key: String, delta: Long): Long?

    /**
     * Decrease by delta
     */
    fun decr(key: String, delta: Long): Long?

    /**
     * Get the attributes in the Hash structure
     */
    fun hGet(key: String, hashKey: String): Any?

    /**
     * Put an attribute into the Hash structure
     */
    fun hSet(key: String, hashKey: String, value: Any, time: Long): Boolean?

    /**
     * Put an attribute into the Hash structure
     */
    fun hSet(key: String, hashKey: String, value: Any)

    /**
     * Get the entire Hash structure directly
     */
    fun hGetAll(key: String): Map<Any, Any?>?

    /**
     * Set the entire Hash structure directly
     */
    fun hSetAll(key: String, map: Map<String, Any>, time: Long): Boolean?

    /**
     * Set the entire Hash structure directly
     */
    fun hSetAll(key: String, map: Map<String, *>)

    /**
     * Delete the attributes in the Hash structure
     */
    fun hDel(key: String, vararg hashKey: Any)

    /**
     * Determine whether there is this attribute in the Hash structure
     */
    fun hHasKey(key: String, hashKey: String): Boolean?

    /**
     * Incremental attributes in the Hash structure
     */
    fun hIncr(key: String, hashKey: String, delta: Long): Long?

    /**
     * Hash结构中属性递减
     */
    fun hDecr(key: String, hashKey: String, delta: Long): Long?

    /**
     * Get the Set structure
     */
    fun sMembers(key: String): Set<Any?>?

    /**
     * Add attributes to the Set structure
     */
    fun sAdd(key: String, vararg values: Any): Long?

    /**
     * Add attributes to the Set structure结构中添加属性
     */
    fun sAdd(key: String, time: Long, vararg values: Any): Long?

    /**
     * Is it an attribute in Set
     */
    fun sIsMember(key: String, value: Any): Boolean?

    /**
     * Get the length of the Set structure
     */
    fun sSize(key: String): Long?

    /**
     * Delete the attributes in the Set structure
     */
    fun sRemove(key: String, vararg values: Any): Long?

    /**
     * Get the attributes in the List structure
     */
    fun lRange(key: String, start: Long, end: Long): List<Any?>?

    /**
     * Get the length of the List structure
     */
    fun lSize(key: String): Long?

    /**
     * Get the attributes in the List according to the index
     */
    fun lIndex(key: String, index: Long): Any?

    /**
     * Add attributes to the List structure
     */
    fun lPush(key: String, value: Any): Long?

    /**
     * Add attributes to the List structure
     */
    fun lPush(key: String, value: Any, time: Long): Long?

    /**
     * Add attributes to the List structure in bulk
     */
    fun lPushAll(key: String, vararg values: Any): Long?

    /**
     * Add attributes to the List structure in bulk
     */
    fun lPushAll(key: String, time: Long, vararg values: Any): Long?

    /**
     * Remove attributes from the List structure
     */
    fun lRemove(key: String, count: Long, value: Any): Long?
}