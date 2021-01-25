package ru.nsu.manasyan.treechat.data

import java.net.InetSocketAddress
import java.util.*

class Neighbour(
    val address: InetSocketAddress,
    var alternate: Alternate? = null,
    var isAlive: Boolean = true,
    uuid: UUID = UUID.randomUUID()
): Identifiable(uuid)