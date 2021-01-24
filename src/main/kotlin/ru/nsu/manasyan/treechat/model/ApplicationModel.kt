package ru.nsu.manasyan.treechat.model

import ru.nsu.manasyan.treechat.data.Alternate
import ru.nsu.manasyan.treechat.data.Neighbour
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ApplicationModel(
    val alternate: Alternate? = null,
    val neighbours: Map<UUID, Neighbour> = ConcurrentHashMap()
) {

}