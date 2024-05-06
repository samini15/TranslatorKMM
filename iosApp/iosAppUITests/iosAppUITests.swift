//
//  iosAppUITests.swift
//  iosAppUITests
//
//  Created by Shayan Amini on 06/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import XCTest

final class iosAppUITests: XCTestCase {
    
    private var app: XCUIApplication!

    override func setUpWithError() throws {
        // Put setup code here. This method is called before the invocation of each test method in the class.

        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false

        // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
        app = XCUIApplication()
        app.launchArguments = ["isUiTesting"]
        app.launch()
    }

    override func tearDownWithError() throws {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testExample() throws {
        // UI tests must launch the application that they test.
        let app = XCUIApplication()
        app.launch()

        // Use XCTAssert and related functions to verify your tests produce the correct results.
    }

    func testLaunchPerformance() throws {
                if #available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 7.0, *) {
            // This measures how long it takes to launch your application.
            measure(metrics: [XCTApplicationLaunchMetric()]) {
                XCUIApplication().launch()
            }
        }
    }
    
    func testRecordAndTranslate() {
        app.buttons["Record audio"].tap()
        
        app.buttons["Voice recorder button"].tap() // Start recording
        app.buttons["Voice recorder button"].tap() // Stop recording
        
        XCTAssert(app.staticTexts["Transcribed voice result"].waitForExistence(timeout: 2))
        
        app.buttons["Voice recorder button"].tap() // Apply result
        
        XCTAssert(app.textViews["Transcribed voice result"].waitForExistence(timeout: 2))
        
        app.buttons["TRANSLATE"].tap()
        
        XCTAssert(app.staticTexts["Transcribed voice result"].waitForExistence(timeout: 2))
        XCTAssert(app.staticTexts["test translation"].waitForExistence(timeout: 2)) // Translated text
    }
}
