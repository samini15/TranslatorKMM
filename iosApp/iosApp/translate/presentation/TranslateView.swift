//
//  TranslateView.swift
//  iosApp
//
//  Created by Shayan Amini on 25/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct TranslateView: View {
    
    private var historyDataSource: HistoryDataSource
    private var translateUseCase: TranslateUseCase
    @ObservedObject var viewModel: IOSTranslateViewModel
    
    init(historyDataSource: HistoryDataSource, translateUseCase: TranslateUseCase) {
        self.historyDataSource = historyDataSource
        self.translateUseCase = translateUseCase
        self.viewModel = IOSTranslateViewModel(historyDataSource: historyDataSource, translateUseCase: translateUseCase)
    }
    
    var body: some View {
        ZStack {
            List {
                HStack(alignment: .center) {
                    LanguageDropDownMenu(language: viewModel.state.fromLanguage, isOpen: viewModel.state.isChoosingFromLanguage) { language in
                        viewModel.onEvent(event: TranslateEvent.ChooseFromLanguage(language: language))
                    }
                    
                    Spacer()
                    
                    SwapLanguageButton {
                        viewModel.onEvent(event: TranslateEvent.SwapLanguages())
                    }
                    
                    Spacer()
                    
                    LanguageDropDownMenu(language: viewModel.state.toLanguage, isOpen: viewModel.state.isChoosingToLanguage) { language in
                        viewModel.onEvent(event: TranslateEvent.ChooseToLanguage(language: language))
                    }
                }
                .listRowSeparator(.hidden)
                //.listRowBackground(Color.background)
            }
            //.listStyle(.plain)
            //.buttonStyle(.plain)
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
    }
}
