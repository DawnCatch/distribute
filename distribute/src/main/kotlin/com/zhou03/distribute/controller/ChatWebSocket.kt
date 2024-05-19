package com.zhou03.distribute.controller

import com.zhou03.distribute.util.ChatUtil
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession


class ChatWebSocket : WebSocketHandler {
    override fun afterConnectionEstablished(session: WebSocketSession) {
        ChatUtil.put(session)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        ChatUtil.sendMessage(message)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        if (session.isOpen) {
            session.close();
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        ChatUtil.remove(session)
    }

    override fun supportsPartialMessages(): Boolean {
        return false
    }


}