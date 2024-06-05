package com.zhou03.distribute.controller

import com.zhou03.distribute.dto.message.MessageHistoryDTO
import com.zhou03.distribute.dto.message.MessageReadDTO
import com.zhou03.distribute.dto.message.MessageSendDTO
import com.zhou03.distribute.service.MessageService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/message")
class MessageController {

    @Autowired
    lateinit var messageService: MessageService

    @RequestMapping("/history")
    fun history(
        @RequestBody messageHistoryDTO: MessageHistoryDTO,
        request: HttpServletRequest,
    ) = messageService.history(messageHistoryDTO, request)

    @RequestMapping("/user/send")
    fun userSend(
        @RequestBody messageSendDTO: MessageSendDTO,
        request: HttpServletRequest,
    ) = messageService.userSend(messageSendDTO, request)

    @RequestMapping("/group/send")
    fun groupSend(
        @RequestBody messageSendDTO: MessageSendDTO,
        request: HttpServletRequest,
    ) = messageService.groupSend(messageSendDTO, request)

    @RequestMapping("/key/user/send/{key}")
    fun userSend(
        @PathVariable("key") key: String,
        @RequestBody messageSendDTO: MessageSendDTO,
    ) = messageService.userSend(key, messageSendDTO)

    @RequestMapping("/key/group/send/{key}")
    fun groupSend(
        @PathVariable("key") key: String,
        @RequestBody messageSendDTO: MessageSendDTO,
    ) = messageService.groupSend(key, messageSendDTO)

    @RequestMapping("/read")
    fun read(
        @RequestBody messageReadDTO: MessageReadDTO,
        request: HttpServletRequest,
    ) = messageService.read(messageReadDTO, request)
}