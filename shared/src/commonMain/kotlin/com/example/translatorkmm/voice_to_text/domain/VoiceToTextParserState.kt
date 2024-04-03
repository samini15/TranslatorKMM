package com.example.translatorkmm.voice_to_text.domain

data class VoiceToTextParserState(
    val result: String = "",
    val error: String? = null,
    // Value between 0 and 1 (100%) determining how loud user speaks
    val powerRatio: Float = 0f,
    val isSpeaking: Boolean = false
)
