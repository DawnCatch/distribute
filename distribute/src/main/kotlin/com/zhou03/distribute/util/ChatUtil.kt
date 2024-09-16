package com.zhou03.distribute.util

import com.zhou03.distribute.dao.MessageDao
import com.zhou03.distribute.vo.Content
import com.zhou03.distribute.vo.MessageVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Component
object ChatUtil {

    private val sessionsMap: ConcurrentHashMap<Int, MutableList<WebSocketSession>> = ConcurrentHashMap()

    private fun put(userId: Int, session: WebSocketSession) {
        val sessions = sessionsMap[userId]
        if (sessions == null) {
            sessionsMap[userId] = mutableListOf(session)
        } else {
            sessions += session
        }
    }

    fun put(session: WebSocketSession) {
        val token = session.getToken()
        put(token.userId, session)
    }

    private fun remove(userId: Int, session: WebSocketSession) {
        val sessions = sessionsMap[userId] ?: return
        sessions.remove(session)
    }

    fun remove(session: WebSocketSession) {
        val token = session.getToken()
        remove(token.userId, session)
    }

    private lateinit var messageDao: MessageDao

    @Autowired
    fun setMessageDao(messageDao: MessageDao) {
        this.messageDao = messageDao
    }

    fun notice(content: Content, id: Int) {
        val message = MessageVO(0, false, 0, id, content)
        sessionsMap.forEach { (userId, sessions) ->
            if (userId == id) {
                val it = sessions.iterator()
                while (it.hasNext()) {
                    val session = it.next()
                    try {
                        if (session.isOpen) session.sendMessage(message.to())
                        else it.remove()
                    } catch (e: Exception) {
                        it.remove()
                    }
                }
            }
        }
    }

    fun sendMessage(message: MessageVO) {
        sessionsMap.forEach { (userId, sessions) ->
            if (userId == message.to || userId == message.from) {
                val it = sessions.iterator()
                while (it.hasNext()) {
                    val session = it.next()
                    try {
                        if (session.isOpen) session.sendMessage(message.to())
                        else it.remove()
                    } catch (e: Exception) {
                        it.remove()
                    }
                }
            }
        }
    }

    fun sendMessage(message: MessageVO, targets: List<Int>) {
        targets.forEach { id ->
            val sessions = sessionsMap[id]
            if (sessions.isNullOrEmpty()) return@forEach
            val it = sessions.iterator()
            while (it.hasNext()) {
                val session = it.next()
                try {
                    if (session.isOpen) session.sendMessage(message.to())
                    else it.remove()
                } catch (e: Exception) {
                    it.remove()
                }
            }
        }
    }

    fun sendMessage(message: MessageVO, target: Int) = ChatUtil.sendMessage(message, listOf(target))
}