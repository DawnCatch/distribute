package com.zhou03.distribute.configuration

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.springframework.boot.jackson.JsonComponent
import java.text.SimpleDateFormat
import java.time.Instant


@JsonComponent
class CustomeJackSon {
    class Serialize : JsonDeserializer<Instant?>() {
        override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Instant? {
            val pattern = "yyyy-MM-dd:HH:mm"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val date = simpleDateFormat.parse(p?.valueAsString)
            return date.toInstant()
        }
    }
}