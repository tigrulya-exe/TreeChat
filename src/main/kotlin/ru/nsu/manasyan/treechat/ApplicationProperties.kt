package ru.nsu.manasyan.treechat

import java.nio.charset.Charset

object ApplicationProperties {
    var charset: Charset = Charsets.UTF_8
    var userName: String = "Steve"
    var messagesBuffSize: Int = 50
    var unconfirmedMessagesBuffSize: Int = 50
    var udpSocketPort: Int = 7777
}
