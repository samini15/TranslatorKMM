//
//  IOSVoiceToTextParser.swift
//  iosApp
//
//  Created by Shayan Amini on 08/04/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared
import Combine
import Speech

class IOSVoiceToTextParser: VoiceToTextParser, ObservableObject {
    
    private let _state = IOSMutableStateFlow(initialValue: VoiceToTextParserState(result: "", error: nil, powerRatio: 0.0, isSpeaking: false))
    var state: CommonStateFlow<VoiceToTextParserState> { _state }
    
    private var microphoneObserver = MicrophonePowerObserver()
    var microphonePowerRatio: Published<Double>.Publisher { microphoneObserver.$microphonePowerRatio }
    private var microphonePowerCancellable: AnyCancellable?
    
    private var recognizer: SFSpeechRecognizer?
    private var audioEngine: AVAudioEngine?
    private var inputNode: AVAudioInputNode?
    private var audioBufferRequest: SFSpeechAudioBufferRecognitionRequest?
    private var recognitionTask: SFSpeechRecognitionTask?
    private var audioSession: AVAudioSession?
    
    func cancel() {
        // Not needed on iOS
    }
    
    func reset() {
        self.stopListening()
        _state.value = VoiceToTextParserState(result: "", error: nil, powerRatio: 0.0, isSpeaking: false)
    }
    
    func startListening(languageCode: String) {
        updateState(error: nil)
        
        let chosenLocale = Locale.init(identifier: languageCode)
        let supportedLocale = SFSpeechRecognizer.supportedLocales().contains(chosenLocale) ? chosenLocale : Locale.init(identifier: "en-US")
        self.recognizer = SFSpeechRecognizer(locale: supportedLocale)
        
        guard recognizer?.isAvailable == true else {
            updateState(error: "Speech recognizer is not available!")
            return
        }
        
        audioSession = AVAudioSession.sharedInstance()
        
        self.requestPermissions { [weak self] in
            self?.audioBufferRequest = SFSpeechAudioBufferRecognitionRequest()
            
            // Nil check
            guard let audioBufferRequest = self?.audioBufferRequest else {
                return
            }
            
            self?.recognitionTask = self?.recognizer?.recognitionTask(with: audioBufferRequest) { [weak self] (result, error) in
                guard let result = result else {
                    self?.updateState(error: error?.localizedDescription)
                    return
                }
                if result.isFinal {
                    self?.updateState(result: result.bestTranscription.formattedString)
                }
            }
            
            self?.audioEngine = AVAudioEngine()
            self?.inputNode = self?.audioEngine?.inputNode
            
            let recordingFormat = self?.inputNode?.outputFormat(forBus: 0)
            self?.inputNode?.installTap(onBus: 0, bufferSize: 1024, format: recordingFormat) { buffer, _ in
                self?.audioBufferRequest?.append(buffer)
            }
            
            self?.audioEngine?.prepare()
            
            do {
                try self?.audioSession?.setCategory(.playAndRecord, mode: .spokenAudio, options: .duckOthers)
                try self?.audioSession?.setActive(true, options: .notifyOthersOnDeactivation)
                
                self?.microphoneObserver.startObserving()
                
                try self?.audioEngine?.start()
                
                self?.updateState(isSpeaking: true)
                
                self?.microphonePowerCancellable = self?.microphonePowerRatio.sink { [weak self] ratio in
                    self?.updateState(powerRatio: ratio)
                }
            } catch {
                self?.updateState(error: error.localizedDescription, isSpeaking: false)
            }
        }
    }
    
    func stopListening() {
        self.updateState(isSpeaking: false)
        microphonePowerCancellable = nil
        microphoneObserver.releaseMemory()
        
        audioBufferRequest?.endAudio()
        audioBufferRequest = nil
        
        audioEngine?.stop()
        audioEngine = nil
        
        inputNode?.removeTap(onBus: 0)
        
        try? audioSession?.setActive(false)
        audioSession = nil
    }
    
    // @escaping -> means that the parameter passed to this closure can escape from (lifetime) this function
    private func requestPermissions(onGranted: @escaping () -> Void) {
        audioSession?.requestRecordPermission { [weak self] isGranted in
            if !isGranted {
                self?.updateState(error: "You need to grant permission to record your voice.")
                self?.stopListening()
                return
            }
            SFSpeechRecognizer.requestAuthorization { [weak self] status in
                // We have to run asynchronously on main thread
                DispatchQueue.main.async {
                    if status != .authorized {
                        self?.updateState(error: "You need to grant permission to transcribe audio.")
                        self?.stopListening()
                        return
                    }
                    onGranted()
                }
            }
        }
    }
    
    private func updateState(result: String? = nil, error: String? = nil, powerRatio: CGFloat? = nil, isSpeaking: Bool? = nil) {
        let currentState = _state.value
        _state.value = VoiceToTextParserState(
            result: result ?? currentState?.result ?? "",
            error: error ?? currentState?.error,
            powerRatio: Float(powerRatio ?? CGFloat(currentState?.powerRatio ?? 0.0)),
            isSpeaking: isSpeaking ?? currentState?.isSpeaking ?? false
        )
    }
}
