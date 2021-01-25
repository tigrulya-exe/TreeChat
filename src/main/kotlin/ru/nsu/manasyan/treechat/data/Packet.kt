package ru.nsu.manasyan.treechat.data

import java.net.InetSocketAddress
import java.util.*

class Packet(
    val type: Type,
    uuid: UUID = UUID.randomUUID(),
    val data: ByteArray? = null,
    val userName: String? = null,
): Identifiable(uuid) {
    enum class Type {
        ACK,
        HELLO,
        KEEP_ALIVE,
        MESSAGE
    }
}

data class PacketContext(
    val packet: Packet,
    val address: InetSocketAddress
)