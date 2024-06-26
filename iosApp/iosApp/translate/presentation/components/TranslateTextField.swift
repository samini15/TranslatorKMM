//
//  TranslateTextField.swift
//  iosApp
//
//  Created by Shayan Amini on 01/04/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared
import UniformTypeIdentifiers

struct TranslateTextField: View {
    
    @Binding var fromText: String
    let toText: String?
    let isTranslating: Bool
    let fromLanguage: UiLanguage
    let toLanguage: UiLanguage
    let onTranslateEvent: (TranslateEvent) -> Void
    
    var body: some View {
        if toText == nil || isTranslating {
            IdleTextField(fromText: $fromText, isTranslating: isTranslating, onTranslateEvent: onTranslateEvent)
                .gradientSurface()
                .cornerRadius(15)
                .animation(.easeInOut, value: isTranslating)
                .shadow(radius: 4)
        } else {
            TranslatedTextField(fromText: fromText, toText: toText ?? "", fromLanguage: fromLanguage, toLanguage: toLanguage, onTranslateEvent: onTranslateEvent)
                .padding()
                .gradientSurface()
                .cornerRadius(15)
                .animation(.easeInOut, value: isTranslating)
                .shadow(radius: 4)
                .onTapGesture {
                    onTranslateEvent(TranslateEvent.EditTranslation())
                }
        }
    }
}

#Preview {
    TranslateTextField(fromText: Binding(get: { "Text" }, set: { value in }), toText: "Translated text", isTranslating: true, fromLanguage: UiLanguage(language: .persian, imageName: "persian"), toLanguage: UiLanguage(language: .french, imageName: "french"), onTranslateEvent: { event in })
}

private extension TranslateTextField {
    struct IdleTextField: View {
        @Binding var fromText: String
        let isTranslating: Bool
        let onTranslateEvent: (TranslateEvent) -> Void
        
        var body: some View {
            TextEditor(text: $fromText)
                .frame(maxWidth: .infinity, minHeight: 200, alignment: .topLeading)
                .padding()
                .foregroundColor(Color.onSurface)
                .overlay(alignment: .bottomTrailing) {
                    ProgressButton(text: "Translate", isLoading: isTranslating) {
                        onTranslateEvent(TranslateEvent.Translate())
                    }
                    .padding()
                }
                .onAppear {
                    // Transparent color for textEditor background
                    /// iOS 15 and below
                    /// https://stackoverflow.com/questions/62848276/change-background-color-of-texteditor-in-swiftui
                    UITextView.appearance().backgroundColor = .clear
                }
                /// iOS 16 and above
                .scrollContentBackground(.hidden)
        }
    }
    
    struct TranslatedTextField: View {
        let fromText: String
        let toText: String
        let fromLanguage: UiLanguage
        let toLanguage: UiLanguage
        let onTranslateEvent: (TranslateEvent) -> Void
        
        private let textToSpeech = TextToSpeech()
        
        var body: some View {
            VStack(alignment: .leading) {
                LanguageDisplay(language: fromLanguage)
                    .padding(.bottom)
                Text(fromText)
                    .foregroundColor(.onSurface)
                
                HStack {
                    Spacer()
                    Button(action: {
                        UIPasteboard.general.setValue(fromText, forPasteboardType: UTType.plainText.identifier)
                    }, label: {
                        Image(uiImage: UIImage(named: "copy")!)
                            .renderingMode(.template)
                            .foregroundColor(.lightBlue)
                    })
                    
                    Button(action: {
                        onTranslateEvent(TranslateEvent.CloseTranslation())
                    }, label: {
                        Image(systemName: "xmark")
                            .foregroundColor(.lightBlue)
                    })
                }
                
                Divider().padding()
                
                LanguageDisplay(language: toLanguage)
                    .padding(.bottom)
                Text(toText)
                    .foregroundColor(.onSurface)
                
                HStack {
                    Spacer()
                    Button(action: {
                        UIPasteboard.general.setValue(toText, forPasteboardType: UTType.plainText.identifier)
                    }, label: {
                        Image(uiImage: UIImage(named: "copy")!)
                            .renderingMode(.template)
                            .foregroundColor(.lightBlue)
                    })
                    
                    Button(action: {
                        textToSpeech.speak(text: toText, language: toLanguage.language.langCode)
                    }, label: {
                        Image(systemName: "speaker.wave.2")
                            .foregroundColor(.lightBlue)
                    })
                }
            }
        }
    }
}
