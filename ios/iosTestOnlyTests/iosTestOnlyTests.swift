//
//  iosTestOnlyTests.swift
//  iosTestOnlyTests
//
//  Created by Kevin Galligan on 3/27/18.
//  Copyright © 2018 Kevin Galligan. All rights reserved.
//

import XCTest
@testable import iosTestOnly
import testj2objclib

class iosTestOnlyTests: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testJ2objcCode() {
        XCTAssertEqual(CoTouchlabDopplTestingDopplJunitTestHelper.runResource(with: "j2objcTests.txt"), 0)
    }
    
}
