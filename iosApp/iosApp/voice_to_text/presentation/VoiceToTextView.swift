//
//  VoiceToTextView.swift
//  iosApp
//
//  Created by Shayan Amini on 14/04/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct VoiceToTextView: View {
    
    @ObservedObject var viewModel: IOSVoiceToTextViewModel
    
    private let parser: any VoiceToTextParser
    private let languageCode: String
    private let onResult: (String) -> Void
    
    @Environment(\.dismiss) var dismiss
    
    init(onResult: @escaping (String) -> Void, parser: any VoiceToTextParser, languageCode: String) {
        self.onResult = onResult
        self.parser = parser
        self.languageCode = languageCode
        self.viewModel = IOSVoiceToTextViewModel(parser: parser, languageCode: languageCode)
    }
    var body: some View {
        VStack {
            Spacer()
            
            mainView
            
            Spacer()
            
            HStack {
                Spacer()
                
                VoiceRecorderButton(displayState: viewModel.state.displayState ?? .waitingToTalk) {
                    if viewModel.state.displayState != .displayingResult {
                        viewModel.onEvent(event: VoiceToTextEvent.ToggleRecording(languageCode: languageCode))
                    } else {
                        onResult(viewModel.state.spokenText)
                        dismiss()
                    }
                }
                if viewModel.state.displayState == .displayingResult {
                    Button(action: {
                        viewModel.onEvent(event: VoiceToTextEvent.ToggleRecording(languageCode: languageCode))
                    }) {
                        Image(systemName: "arrow.clockwise")
                            .foregroundColor(.lightBlue)
                    }
                }
                Spacer()
            }
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
        .background(Color.background)
    }
    
    @ViewBuilder var mainView: some View {
        switch viewModel.state.displayState {
        case .waitingToTalk:
            AnyView(Text("Click record and start talking."))
                .font(.title2)
        case .displayingResult:
            AnyView(Text(viewModel.state.spokenText))
                .font(.title2)
        case .error:
            AnyView(Text(viewModel.state.recordError ?? "Unknown error"))
                .font(.title2)
                .foregroundColor(.red)
        case .speaking:
            AnyView(
                VoiceRecorderDisplay(powerRatios: viewModel.state.powerRatios.map { Double(truncating: $0) })
                    .frame(maxHeight: 100)
                    .padding()
            )
        default: AnyView(EmptyView())
        }
    }
}

//#Preview {
//    VoiceToTextView()
//}
