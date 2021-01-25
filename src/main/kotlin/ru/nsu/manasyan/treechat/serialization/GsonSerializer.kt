package ru.nsu.manasyan.treechat.serialization

import com.google.gson.Gson
import ru.nsu.manasyan.treechat.ApplicationProperties

class GsonSerializer : Serializer {
    private val gson = Gson()

    override fun <D> toObject(bytes: ByteArray, clazz: Class<D>): D = gson.fromJson(
        bytes.toString(ApplicationProperties.charset),
        clazz
    )

    override fun <D> toBytes(source: D): ByteArray = gson.toJson(
        source
    ).toByteArray(ApplicationProperties.charset)
}