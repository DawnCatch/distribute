package com.zhou03.distribute.dto.group

import org.springframework.web.multipart.MultipartFile

data class GroupUploadAvatarDTO(
    val id: Int,
    val file: MultipartFile,
)