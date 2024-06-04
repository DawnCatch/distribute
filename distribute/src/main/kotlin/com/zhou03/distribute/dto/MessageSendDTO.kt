package com.zhou03.distribute.dto

import com.zhou03.distribute.vo.Content


data class MessageSendDTO(
    val to: Int,
    val content: Content,
)
