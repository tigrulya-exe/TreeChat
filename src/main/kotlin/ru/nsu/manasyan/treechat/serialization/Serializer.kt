package ru.nsu.manasyan.treechat.serialization

interface Serializer {
    fun <D> toObject(bytes: ByteArray, clazz: Class<D>): D
    fun <D> toBytes(source: D): ByteArray
}

inline fun <reified D> Serializer.toObject(bytes: ByteArray): D = toObject(bytes, D::class.java)