package ru.nsu.manasyan.treechat.controller

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.nsu.manasyan.treechat.ApplicationProperties
import ru.nsu.manasyan.treechat.data.*
import ru.nsu.manasyan.treechat.model.ApplicationModel
import ru.nsu.manasyan.treechat.network.UdpSocketSender
import ru.nsu.manasyan.treechat.serialization.Serializer
import ru.nsu.manasyan.treechat.serialization.toObject
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer

typealias PacketQueue = BlockingQueue<PacketContext>

typealias PacketHandler = Consumer<PacketContext>

class ApplicationController(
    private val serializer: Serializer,
    private val model: ApplicationModel = ApplicationModel(),
) {
    private val interrupted = AtomicBoolean()

    private val packetQueue: PacketQueue = LinkedBlockingQueue()

    private val sender: UdpSocketSender

    private val packetHandlers = mapOf(
        Packet.Type.ACK to PacketHandler(this::handleAck),
        Packet.Type.HELLO to PacketHandler(this::handleHello),
        Packet.Type.KEEP_ALIVE to PacketHandler(this::handleKeepAlive),
        Packet.Type.MESSAGE to PacketHandler(this::handleMessage),
    )

    init {
        val socket = DatagramSocket(ApplicationProperties.udpSocketPort)
        sender = UdpSocketSender(serializer, socket)
    }

    fun interrupt() {
        interrupted.set(true)
    }

    fun sendMessage(message: String) {
        val packet = Packet(
            type = Packet.Type.MESSAGE,
            userName = ApplicationProperties.userName,
            data = message.toByteArray(ApplicationProperties.charset)
        )

        val neighbourAddresses = model.neighbours.map {
            it.value.address
        }
        sender.broadcastPacket(packet, neighbourAddresses)

        model.unconfirmedMessages[packet.uuid] = UnconfirmedPacket(
            packet,
            neighbourAddresses as MutableList<InetSocketAddress>
        )
    }

    private fun handleAck(packetContext: PacketContext) {
        val unconfirmedPacket = model.unconfirmedMessages[packetContext.packet.uuid]
        unconfirmedPacket?.let {
            it.unconfirmedDestinations.remove(packetContext.address)
            if (it.unconfirmedDestinations.isEmpty()) {
                model.unconfirmedMessages.remove(packetContext.packet.uuid)
            }
        }
    }

    private fun handleHello(packetContext: PacketContext) {
        setAlternateIfNone(packetContext.address)
        val packet = packetContext.packet

        sender.sendAck(packet.uuid, packetContext.address)
        val neighbour = getNeighbourByAddress(packetContext.address)
        neighbour.alternate = packet.data?.let {
            Alternate.Other(serializer.toObject(it))
        } ?: Alternate.Me
    }

    private fun getNeighbourByAddress(address: InetSocketAddress): Neighbour {
        return model.neighbours.values.find {
            it.address == address
        } ?: Neighbour(address).apply {
            model.neighbours[uuid] = this
        }.also { sendHello(address) }
    }

    private fun sendHello(address: InetSocketAddress) = sender.sendPacket(
        Packet(
            type = Packet.Type.HELLO,
            userName = ApplicationProperties.userName,
            data = serializer.toBytes(model.alternate)
        ),
        address
    )

    private fun handleKeepAlive(packetContext: PacketContext) {
        model.neighbours[packetContext.packet.uuid]?.let {
            it.isAlive = true
        }
    }

    private fun handleMessage(packetContext: PacketContext) {
        val packet = packetContext.packet
        sender.sendAck(packet.uuid, packetContext.address)
        if (model.messages.containsKey(packet.uuid)) {
            return
        }
        model.messages[packet.uuid] = packet
    }

    private fun handlePackets() = runBlocking {
        launch {
            while (!interrupted.get()) {
                val packetContext: PacketContext
                withContext(Dispatchers.IO) {
                    packetContext = packetQueue.take()
                }

                packetHandlers[packetContext.packet.type]!!.accept(packetContext)
            }
        }
    }

    private fun setAlternateIfNone(address: InetSocketAddress) {
        if (model.alternate == null) {
            model.alternate = Alternate.Other(address)
        }
    }
}
