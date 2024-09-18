package com.zhou03.distribute.dto.message

import org.springframework.web.multipart.MultipartFile

data class MessageFileSendDTO(
    val to: Int,
    val files: List<MultipartFile>,
) {
    fun validate() = this.to == 0 || this.files.isEmpty()
}