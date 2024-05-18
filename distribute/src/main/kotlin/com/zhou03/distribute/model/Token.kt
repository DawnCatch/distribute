package com.zhou03.distribute.model

import com.zhou03.distribute.domain.Device
import com.zhou03.distribute.domain.User

data class Token(
    var userId: Int,
    var deviceId: Int? = null,
) {
    companion object {
        fun from(user: User) = Token(user.id)
    }

    fun from(device: Device) = this.apply {
        if (userId != device.userId) return@apply
        deviceId = device.id
    }
}