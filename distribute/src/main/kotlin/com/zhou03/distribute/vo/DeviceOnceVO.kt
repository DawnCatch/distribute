package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.Device

data class DeviceOnceVO(
    val id: Int,
    val title: String,
    val code: String,
) {
    companion object {
        fun from(device: Device) = DeviceOnceVO(device.id, device.title, device.code)
    }
}