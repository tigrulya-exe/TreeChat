package ru.nsu.manasyan.treechat.serialization

import ru.nsu.manasyan.treechat.model.Packet

interface PacketSerializer {
    fun toPacket(bytes: ByteArray): Packet
    fun toBytes(packet: Packet): ByteArray
}