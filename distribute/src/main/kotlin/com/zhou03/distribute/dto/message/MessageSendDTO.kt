package com.zhou03.distribute.dto.message

import com.zhou03.distribute.vo.Content


data class MessageSendDTO(
    val to: Int,
    val content: Content,
) {
    fun validate() = this.to == 0 || this.content.type == "" || this.content.value == ""
}
