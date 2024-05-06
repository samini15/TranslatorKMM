import SwiftUI
import shared

struct ContentView: View {

    let appModule: AppModule

	var body: some View {
        TranslateView(historyDataSource: appModule.historyDataSource, translateUseCase: appModule.translateUseCase, parser: appModule.voiceToTextParser)
	}
}

/*struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}*/
