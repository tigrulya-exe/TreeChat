package ru.nsu.manasyan.treechat.model

import ru.nsu.manasyan.treechat.ApplicationProperties
import ru.nsu.manasyan.treechat.data.Alternate
import ru.nsu.manasyan.treechat.data.Neighbour
import ru.nsu.manasyan.treechat.data.Packet
import ru.nsu.manasyan.treechat.data.UnconfirmedPacket
import ru.nsu.manasyan.treechat.messagebuf.FiniteFifoMap
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ApplicationModel(
    var alternate: Alternate? = null,
    val neighbours: MutableMap<UUID, Neighbour> = ConcurrentHashMap(),
    val messages: FiniteFifoMap<UUID, Packet> = FiniteFifoMap(
        ApplicationProperties.messagesBuffSize
    ),
    val unconfirmedMessages: FiniteFifoMap<UUID, UnconfirmedPacket> = FiniteFifoMap(
        ApplicationProperties.unconfirmedMessagesBuffSize
    )
) {

}