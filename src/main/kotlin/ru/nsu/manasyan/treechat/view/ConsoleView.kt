package ru.nsu.manasyan.treechat.view

import ru.nsu.manasyan.treechat.controller.ApplicationController
import ru.nsu.manasyan.treechat.model.ApplicationModel
import ru.nsu.manasyan.treechat.util.Observable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.concurrent.atomic.AtomicBoolean

class ConsoleView(
    private val controller: ApplicationController,
    model: ApplicationModel
) {
    val isInterrupted = AtomicBoolean()

    private val dateFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)

    init {
        model.messages.observe { event, message ->
            // TODO: change to logger
            // TODO: get format from file
            if (event != Observable.Event.UPDATE) {
                return@observe
            }
            println("${getCurrentTime()} : <${message.userName}>: ${message.data}")
        }
    }

    private fun getCurrentTime() = LocalDateTime.now().format(dateFormat)

    // TODO: make async
    private fun sendMessage(message: String) {
        while (!isInterrupted.get()){
            // TODO: add validations
            readLine()?.let {
                controller.sendMessage(it)
            } ?: break
        }
    }
}