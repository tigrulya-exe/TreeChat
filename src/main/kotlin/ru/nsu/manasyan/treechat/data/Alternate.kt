package ru.nsu.manasyan.treechat.data

import java.net.InetSocketAddress

sealed class Alternate {
    object Me : Alternate()
    class Other(val address: InetSocketAddress) : Alternate()
}
