//
//  dcframeworkgenTests.m
//  ios
//
//  Created by touchlab on 8/17/17.
//  Copyright © 2017 Kevin Galligan. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "DINTOneTest.h"

@interface dcframeworkgenTests : XCTestCase

@end

@implementation dcframeworkgenTests

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testExample {
    // This is an example of a functional test case.
    // Use XCTAssert and related functions to verify your tests produce the correct results.
    [DINTOneTest runDopplResourcesWithNSString:@"dopplTests.h"];
}

- (void)testPerformanceExample {
    // This is an example of a performance test case.
    [self measureBlock:^{
        // Put the code you want to measure the time of here.
    }];
}

@end
