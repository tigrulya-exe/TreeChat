package ru.nsu.manasyan.treechat.messagebuf

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class FiniteFifoMap<K, V>(
    private val bufferSize: Int
) {
    private val map = object : LinkedHashMap<K, V>() {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
            return size > bufferSize
        }
    }

    private val lock = ReentrantReadWriteLock()

    fun remove(key: K) = lock.write {
        map.remove(key)
    }

    fun containsKey(key: K) = lock.read {
        map.containsKey(key)
    }

    operator fun set(key: K, value: V) = lock.write {
        map[key] = value
    }

    operator fun get(key: K) = lock.read {
        map[key]
    }
}

