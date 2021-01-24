package ru.nsu.manasyan.treechat.network

import ru.nsu.manasyan.treechat.controller.PacketQueue
import ru.nsu.manasyan.treechat.serialization.PacketSerializer
import java.net.DatagramPacket
import java.net.DatagramSocket

class UdpSocketListener(
    private val deserializer: PacketSerializer,
    private val packetQueue: PacketQueue,
    bufferSize: Int = DEFAULT_BUFF_SIZE
) {
    private val receiveBuffer: ByteArray = ByteArray(bufferSize)

    private companion object {
        private const val DEFAULT_BUFF_SIZE = 8192
    }

    fun listen(port: Int) {
        // TODO: handle exceptions
        val socket = DatagramSocket(port)
        val datagramPacket = DatagramPacket(
            receiveBuffer,
            receiveBuffer.size
        )
        while (true) {
            socket.receive(datagramPacket)
            val packet = deserializer.toPacket(datagramPacket.data)
            packetQueue.add(packet)
        }
    }
}