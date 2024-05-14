package com.zhou03.distribute.vo

import com.zhou03.distribute.domain.Device

data class DeviceVO(
    val id: Int,
    val title: String,
) {
    companion object {
        fun from(device: Device) = DeviceVO(device.id, device.title)
    }
}