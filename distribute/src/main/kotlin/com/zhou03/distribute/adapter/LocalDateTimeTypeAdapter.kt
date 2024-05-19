package com.zhou03.distribute.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LocalDateTimeTypeAdapter : TypeAdapter<LocalDateTime>() {
    override fun write(out: JsonWriter, value: LocalDateTime?) {
        if (value == null) out.value(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
        else out.value(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(value))
    }

    override fun read(`in`: JsonReader): LocalDateTime {
        return LocalDateTime.parse(`in`.nextString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
}