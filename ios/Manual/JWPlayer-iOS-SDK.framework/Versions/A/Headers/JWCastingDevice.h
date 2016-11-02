//
//  JWCastingDevice.h
//  JWPlayer-iOS-SDK
//
//  Created by Karim Mourra on 12/3/15.
//  Copyright © 2015 JWPlayer. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    googleChromeCast = 1
}JWCastingService;

/*!
 @class JWCastingDevice
 A class that encapsulates a casting device and holds its identifiers and attributes.
 */
@interface JWCastingDevice : NSObject

/*!
 The casting service supported the device. i.e. a chromeCast device returns googleChromeCast as its casting service.
 */
@property (nonatomic, readonly) JWCastingService castingService;

/*!
 The casting device's friendly name; i.e. "Dining Room".
 */
@property (nonatomic, readonly) NSString *name;

/*!
 A unique identifier for the casting device.
 */
@property (nonatomic, readonly) NSString *identifier;

- (instancetype)init __attribute__((unavailable("init not available")));

@end
