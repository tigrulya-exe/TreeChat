package ru.nsu.manasyan.treechat.data

import java.net.InetSocketAddress

data class UnconfirmedPacket(
    val packet: Packet,
    val unconfirmedDestinations: MutableList<InetSocketAddress>
)