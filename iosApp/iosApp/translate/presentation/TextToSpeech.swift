//
//  TextToSpeech.swift
//  iosApp
//
//  Created by Shayan Amini on 02/04/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import AVFoundation

struct TextToSpeech {
    private let synthetizer = AVSpeechSynthesizer()
    
    func speak(text: String, language: String) {
        let utterance = AVSpeechUtterance(string: text)
        utterance.voice = AVSpeechSynthesisVoice(language: language)
        utterance.volume = 1
        synthetizer.speak(utterance)
    }
}
