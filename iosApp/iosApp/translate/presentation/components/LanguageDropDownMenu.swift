//
//  LanguageDropDownMenu.swift
//  iosApp
//
//  Created by Shayan Amini on 25/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct LanguageDropDownMenu: View {
    
    var language: UiLanguage
    var isOpen: Bool
    var selectLanguage: (UiLanguage) -> Void
    
    var body: some View {
        Menu {
            VStack {
                ForEach(UiLanguage.Companion().allLanguages, id: \.self.language.langCode) { language in
                    LanguageDropDownItem(language: language) {
                        selectLanguage(language)
                    }
                }
            }
        } label: {
            HStack {
                SmallLanguageIcon(language: language)
                Text(language.language.langName)
                    .foregroundColor(.lightBlue)
                
                if isOpen {
                    Image(systemName: isOpen ? "chevron.up" : "chevron.down")
                        .foregroundColor(.lightBlue)
                }
            }
        }
    }
}

#Preview {
    LanguageDropDownMenu(language: UiLanguage(language: .persian, imageName: "persian"), isOpen: true, selectLanguage: { language in })
}
