//
//  MicrophonePowerObserver.swift
//  iosApp
//
//  Created by Shayan Amini on 06/04/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared
import Combine
import Speech

class MicrophonePowerObserver: ObservableObject {
    private var cancellable: AnyCancellable? = nil
    private var audioRecorder: AVAudioRecorder? = nil
    
    @Published private(set) var microphonePowerRatio = 0.0
    
    private let powerRatioEmissionsPerSecond = 20.0
    
    func startObserving() {
        do {
            let recorderSettings: [String: Any] = [AVFormatIDKey: NSNumber(value: kAudioFormatAppleLossless), AVNumberOfChannelsKey: 1]
            let recorder = try AVAudioRecorder(url: URL(fileURLWithPath: "/dev/null", isDirectory: true), settings: recorderSettings)
            recorder.isMeteringEnabled = true
            recorder.record()
            self.audioRecorder = recorder
            
            self.cancellable = Timer.publish(
                every: 1.0 / powerRatioEmissionsPerSecond,
                tolerance: 1.0 / powerRatioEmissionsPerSecond,
                on: .main,
                in: .common
            )
            .autoconnect()
            .sink(receiveValue: { [weak self] _ in // weak self: To avoid memory leak -> self object will be set to nil as soon as parent class (here Observer) is destroyed
                recorder.updateMeters()
                
                let powerOffset = recorder.averagePower(forChannel: 0)
                if powerOffset < -50 {
                    self?.microphonePowerRatio = 0.0
                } else {
                    let normalizedOffset = CGFloat(50 * powerOffset) / 50
                    self?.microphonePowerRatio = normalizedOffset
                }
            })
        } catch {
            print("An error occured when observing microphone power: \(error.localizedDescription)")
        }
    }
    
    func releaseMemory() {
        cancellable = nil
        audioRecorder?.stop()
        audioRecorder = nil
        microphonePowerRatio = 0.0
    }
}
