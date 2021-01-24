package ru.nsu.manasyan.treechat.serialization

import com.google.gson.Gson
import ru.nsu.manasyan.treechat.ApplicationProperties
import ru.nsu.manasyan.treechat.data.Packet

class GsonPacketSerializer(
    private val properties: ApplicationProperties
) : PacketSerializer {
    private val gson = Gson()

    override fun toPacket(bytes: ByteArray): Packet = gson.fromJson(
        bytes.toString(properties.charset),
        Packet::class.java
    )

    override fun toBytes(packet: Packet): ByteArray = gson.toJson(
        packet
    ).toByteArray(properties.charset)
}