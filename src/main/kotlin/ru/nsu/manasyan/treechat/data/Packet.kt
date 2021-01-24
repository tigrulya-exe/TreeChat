package ru.nsu.manasyan.treechat.data

import java.util.*

class Packet(
    val GUID: UUID = UUID.randomUUID(),
    val type: Type,
    val userName: String?,
    val payload: ByteArray?,
) {
    enum class Type {
        ACK,
        HELLO,
        KEEP_ALIVE,
        MESSAGE
    }
}