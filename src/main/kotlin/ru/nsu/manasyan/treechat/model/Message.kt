package ru.nsu.manasyan.treechat.model

import java.util.*

enum class PacketType {
    ACK,
    HELLO,
    KEEP_ALIVE,
    MESSAGE
}

class Packet(
    val GUID: String = UUID.randomUUID().toString(),
    val type: PacketType,
    val userName: String?,
    val payload: ByteArray?,
)