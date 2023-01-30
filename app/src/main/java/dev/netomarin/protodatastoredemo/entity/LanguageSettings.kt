package dev.netomarin.protodatastoredemo.entity

@kotlinx.serialization.Serializable
data class LanguageSettings(
    val language: Language = Language.PORTUGUESE
)

enum class Language {
    PORTUGUESE, SPANISH, ENGLISH
}
