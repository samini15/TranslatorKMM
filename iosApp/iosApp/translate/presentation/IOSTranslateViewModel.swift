//
//  IOSTranslateViewModel.swift
//  iosApp
//
//  Created by Shayan Amini on 25/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared


extension TranslateView {
    @MainActor class IOSTranslateViewModel: ObservableObject {
        private var historyDataSource: HistoryDataSource
        private var translateUseCase: TranslateUseCase
        
        private let viewModel: TranslateViewModel
        
        @Published var state: TranslateState = TranslateState(
            fromLanguage: UiLanguage(language: .english, imageName: "english"),
            fromText: "",
            isChoosingFromLanguage: false,
            toLanguage: UiLanguage(language: .persian, imageName: "persian"),
            toText: "",
            isChoosingToLanguage: false,
            isTranslating: false,
            error: nil,
            history: []
        )
        private var handle: DisposableHandle?
        
        init(historyDataSource: HistoryDataSource, translateUseCase: TranslateUseCase) {
            self.historyDataSource = historyDataSource
            self.translateUseCase = translateUseCase
            self.viewModel = TranslateViewModel(translateUseCase: translateUseCase, historyDataSource: historyDataSource, coroutineScope: nil)
        }
        
        /// Collect emissions from the flow and update the state
        func startObserving() {
            handle = viewModel.state.colle
        }
    }
}
