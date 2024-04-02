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
                    .padding(.leading)
                    
                    Spacer()
                    
                    SwapLanguageButton {
                        viewModel.onEvent(event: TranslateEvent.SwapLanguages())
                    }
                    
                    Spacer()
                    
                    LanguageDropDownMenu(language: viewModel.state.toLanguage, isOpen: viewModel.state.isChoosingToLanguage) { language in
                        viewModel.onEvent(event: TranslateEvent.ChooseToLanguage(language: language))
                    }
                    .padding(.trailing)
                }
                //.listRowSeparator(.hidden)
                //.listRowBackground(Color.surface)
                
                TranslateTextField(
                    fromText: Binding(get: { viewModel.state.fromText }, set: { value in
                        viewModel.onEvent(event: TranslateEvent.ChangeTranslationText(text: value))
                    }),
                    toText: viewModel.state.toText,
                    isTranslating: viewModel.state.isTranslating,
                    fromLanguage: viewModel.state.fromLanguage,
                    toLanguage: viewModel.state.toLanguage) { translateEvent in
                        viewModel.onEvent(event: translateEvent)
                    }
                
                if !viewModel.state.history.isEmpty {
                    Text("History")
                        .font(.title)
                        .bold()
                        .frame(maxWidth: .infinity, alignment: .leading)
                        //.listRowSeparator(.hidden)
                        //.listRowBackground(Color.surface)
                    
                    ForEach(viewModel.state.history, id: \.self.id) { historyItem in
                        TranslateHistoryItem(item: historyItem, onClick: {
                            viewModel.onEvent(event: TranslateEvent.SelectHistoryItem(historyItem: historyItem))
                        })
                        //.listRowSeparator(.hidden)
                        //.listRowBackground(Color.surface)
                    }
                }
                
                
            }
            .navigationTitle("Translation")
            .listStyle(.plain)
            .buttonStyle(.plain)
            
            VStack {
                Spacer()
                NavigationLink(destination: Text("Voice-to-text screen")) {
                    ZStack {
                        Circle()
                            .foregroundColor(.primaryColor)
                            .padding()
                        Image(uiImage: UIImage(named: "mic")!)
                            .foregroundColor(.onPrimary)
                    }
                    .frame(maxWidth: 100, maxHeight: 100)
                }
            }
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
    }
}
