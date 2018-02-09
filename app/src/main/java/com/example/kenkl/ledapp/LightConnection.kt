package com.example.kenkl.ledapp

import com.google.gson.Gson
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress


class LightConnection(val ip: String, val port: Int) : AnkoLogger {
    val gson = Gson()
    fun send(data: ControlData): Throwable? {
        try {
            val socket = DatagramSocket(port)
            socket.connect(InetSocketAddress(ip, port))

            val output = gson.toJson(data, ControlData::class.java).toByteArray()
            val packet = DatagramPacket(output, output.size)

            socket.send(packet)
            socket.close()
            return null
        } catch(e: Exception) {
            warn { e }
            return e
        }
    }
}