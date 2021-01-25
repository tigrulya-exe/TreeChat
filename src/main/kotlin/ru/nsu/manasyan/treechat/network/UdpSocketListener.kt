package ru.nsu.manasyan.treechat.network

import ru.nsu.manasyan.treechat.ApplicationProperties
import ru.nsu.manasyan.treechat.controller.ApplicationController
import ru.nsu.manasyan.treechat.controller.PacketQueue
import ru.nsu.manasyan.treechat.data.Packet
import ru.nsu.manasyan.treechat.data.PacketContext
import ru.nsu.manasyan.treechat.serialization.Serializer
import ru.nsu.manasyan.treechat.serialization.toObject
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress

class UdpSocketListener(
    private val deserializer: Serializer,
    private val packetQueue: PacketQueue,
    bufferSize: Int = DEFAULT_BUFF_SIZE
) {
    private val receiveBuffer: ByteArray = ByteArray(bufferSize)

    private companion object {
        private const val DEFAULT_BUFF_SIZE = 8192
    }

    fun listen(port: Int = ApplicationProperties.udpSocketPort) {
        // TODO: handle exceptions
        val socket = DatagramSocket(port)
        val datagramPacket = DatagramPacket(
            receiveBuffer,
            receiveBuffer.size
        )
        while (true) {
            socket.receive(datagramPacket)
            val packet = deserializer.toObject<Packet>(
                datagramPacket.data
            )
            packetQueue.add(
                PacketContext(
                    packet,
                    datagramPacket.socketAddress as InetSocketAddress
                )
            )
        }
    }
}