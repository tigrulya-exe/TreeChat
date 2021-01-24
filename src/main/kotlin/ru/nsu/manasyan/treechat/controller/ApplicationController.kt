package ru.nsu.manasyan.treechat.controller

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.nsu.manasyan.treechat.data.Packet
import ru.nsu.manasyan.treechat.model.ApplicationModel
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer

typealias PacketQueue = BlockingQueue<Packet>

typealias PacketHandler = Consumer<Packet>

class ApplicationController(
    private val model: ApplicationModel = ApplicationModel()
) {
    private val interrupted = AtomicBoolean()

    private val packetQueue: PacketQueue = LinkedBlockingQueue()

    private val packetHandlers = mapOf(
        Packet.Type.ACK to PacketHandler(this::handleAck),
        Packet.Type.HELLO to PacketHandler(this::handleHello),
        Packet.Type.KEEP_ALIVE to PacketHandler(this::handleKeepAlive),
        Packet.Type.MESSAGE to PacketHandler(this::handleMessage),
    )

    fun interrupt() {
        interrupted.set(true)
    }

    private fun handleAck(packet: Packet) {

    }

    private fun handleHello(packet: Packet) {

    }

    private fun handleKeepAlive(packet: Packet) {

    }

    private fun handleMessage(packet: Packet) {

    }

    private fun handlePackets() = runBlocking {
        launch {
            while (!interrupted.get()) {
                val packet: Packet
                withContext(Dispatchers.IO) {
                    packet = packetQueue.take()
                }

                packetHandlers[packet.type]!!.accept(packet)
            }
        }
    }
}