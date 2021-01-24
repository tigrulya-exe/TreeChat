package ru.nsu.manasyan.treechat.data

import java.net.InetSocketAddress

class Neighbour(
    val alternate: Alternate = Alternate.Me,
    val address: InetSocketAddress,
    val isFresh: Boolean = true
)