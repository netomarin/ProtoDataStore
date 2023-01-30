package dev.netomarin.protodatastoredemo

import androidx.datastore.core.Serializer
import dev.netomarin.protodatastoredemo.entity.LanguageSettings
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object LanguageSettingsSerializer : Serializer<LanguageSettings> {
    override val defaultValue: LanguageSettings
        get() = LanguageSettings()

    override suspend fun readFrom(input: InputStream): LanguageSettings {
        return try {
            Json.decodeFromString(
                deserializer = LanguageSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: LanguageSettings, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = LanguageSettings.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}