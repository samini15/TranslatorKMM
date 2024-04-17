//
//  VoiceRecorderButton.swift
//  iosApp
//
//  Created by Shayan Amini on 14/04/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct VoiceRecorderButton: View {
    var displayState: DisplayState
    var onClick: () -> Void
    
    var body: some View {
        Button(action: onClick) {
            ZStack {
                Circle()
                    .foregroundColor(.primaryColor)
                    .padding()
                icon.foregroundColor(.onPrimary)
            }
        }
        .frame(maxWidth: 100.0, maxHeight: 100.0)
    }
    
    var icon: some View {
        switch displayState {
        case .speaking:
            return Image(systemName: "stop.fill")
        case .displayingResult:
            return Image(systemName: "checkmark")
        default:
            return Image(uiImage: UIImage(named: "mic")!)
        }
    }
}

#Preview {
    VoiceRecorderButton(displayState: .speaking) { }
}
