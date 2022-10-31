package com.sebastianpakiela.githubexplorer.data.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.Instant

class InstantDateDeserializer : JsonDeserializer<Instant> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Instant? {
        return json?.asString?.let {
            Instant.parse(it)
        }
    }
}