package com.example.translatorkmm.voiceToText.data

import com.example.translatorkmm.core.domain.util.CommonStateFlow
import com.example.translatorkmm.core.domain.util.toCommonStateFlow
import com.example.translatorkmm.voice_to_text.domain.VoiceToTextParser
import com.example.translatorkmm.voice_to_text.domain.VoiceToTextParserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeVoiceToTextParser: VoiceToTextParser {

    private val _state = MutableStateFlow(VoiceToTextParserState())
    override val state: CommonStateFlow<VoiceToTextParserState>
        get() = _state.toCommonStateFlow()

    val transcribedResult = "Transcribed voice result"
    override fun startListening(languageCode: String) {
        _state.update {
            it.copy(
                result = "",
                isSpeaking = true
            )
        }
    }

    override fun stopListening() {
        _state.update {
            it.copy(
                result = transcribedResult,
                isSpeaking = false
            )
        }
    }

    override fun cancel() = Unit

    override fun reset() = _state.update { VoiceToTextParserState() }
}