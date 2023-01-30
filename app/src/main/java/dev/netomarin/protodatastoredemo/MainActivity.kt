package dev.netomarin.protodatastoredemo

import android.content.Context
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEachIndexed
import androidx.datastore.dataStore
import dev.netomarin.protodatastoredemo.entity.Language
import dev.netomarin.protodatastoredemo.entity.LanguageSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore by dataStore("language-settings.json", LanguageSettingsSerializer)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val scope = CoroutineScope(Dispatchers.IO)

        // Loading data
        scope.launch {
            try {
                val savedLanguage: LanguageSettings = dataStore.data.first()
                radioGroup.forEachIndexed { i, rb ->
                    val language = Language.values()[i]
                    (rb as RadioButton).isChecked = language == savedLanguage.language
                }
            } catch (e: NoSuchElementException) {
                radioGroup.check(R.id.radioButton)
                setLanguage(Language.PORTUGUESE)
            }
        }

        // Listener to save data
        radioGroup.forEachIndexed { i, rb ->
            val language = Language.values()[i]
            rb.setOnClickListener {
                scope.launch { setLanguage(language) }
            }
        }
    }

    private suspend fun setLanguage(language: Language) {
        dataStore.updateData {
            it.copy(language = language)
        }
    }
}