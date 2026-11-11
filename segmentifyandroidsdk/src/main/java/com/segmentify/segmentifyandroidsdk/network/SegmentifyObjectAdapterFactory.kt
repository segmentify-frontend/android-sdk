package com.segmentify.segmentifyandroidsdk.network

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.segmentify.segmentifyandroidsdk.model.SegmentifyObject

/**
 * Gson reads fields, not getters, so the lazy userId/sessionId of a
 * SegmentifyObject would be missing from the request body. This factory
 * resolves them right before any SegmentifyObject is serialized, so every
 * request path gets them without calling resolveIds() by hand.
 */
internal class SegmentifyObjectAdapterFactory : TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        if (!SegmentifyObject::class.java.isAssignableFrom(type.rawType)) return null
        val delegate = gson.getDelegateAdapter(this, type)

        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T?) {
                (value as? SegmentifyObject)?.resolveIds()
                delegate.write(out, value)
            }

            override fun read(reader: JsonReader): T? = delegate.read(reader)
        }
    }
}
