package com.zhou03.distribute.controller

import com.zhou03.distribute.dto.MessageHistoryDTO
import com.zhou03.distribute.dto.MessageReadDTO
import com.zhou03.distribute.dto.MessageSendDTO
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

    @RequestMapping("/send")
    fun send(
        @RequestBody messageSendDTO: MessageSendDTO,
        request: HttpServletRequest,
    ) = messageService.send(messageSendDTO, request)

    @RequestMapping("/key/send/{key}")
    fun send(
        @PathVariable("key") key: String,
        @RequestBody messageSendDTO: MessageSendDTO,
    ) = messageService.send(key, messageSendDTO)

    @RequestMapping("/read")
    fun read(
        @RequestBody messageReadDTO: MessageReadDTO,
        request: HttpServletRequest,
    ) = messageService.read(messageReadDTO, request)
}