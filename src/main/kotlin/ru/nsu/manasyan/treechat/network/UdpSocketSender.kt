package ru.nsu.manasyan.treechat.network

import ru.nsu.manasyan.treechat.data.Packet
import ru.nsu.manasyan.treechat.serialization.Serializer
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.util.*

class UdpSocketSender(
    private val serializer: Serializer,
    private val socket: DatagramSocket = DatagramSocket()
) {
    // TODO: make async
    fun sendPacket(packet: Packet, destination: InetSocketAddress) {
        val datagram = serializer.toBytes(packet).let {
            DatagramPacket(it, it.size, destination)
        }
        socket.send(datagram)
    }

    fun sendAck(uuid: UUID, destination: InetSocketAddress) = sendPacket(
        Packet(Packet.Type.ACK, uuid),
        destination
    )

    // TODO: make async
    fun broadcastPacket(
        packet: Packet,
        destinations: List<InetSocketAddress>
    ) {
        destinations.forEach {
            val datagram = serializer.toBytes(packet).let { bytes ->
                DatagramPacket(bytes, bytes.size, it)
            }
            socket.send(datagram)
        }
    }
}
