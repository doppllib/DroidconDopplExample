//
//  JWCastController.h
//  JWPlayer-iOS-SDK
//
//  Created by Karim Mourra on 12/3/15.
//  Copyright © 2015 JWPlayer. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JWCastingDelegate.h"
#import "JWCastingDevice.h"

@class JWPlayerController;

/*!
 @class JWCastController
 A class that enables casting using the JWPlayerController.
 */
@interface JWCastController : NSObject

/*!
 Player instance.
 @discussion the instance of JWPlayerController in use.
 */
@property (nonatomic, weak) JWPlayerController *player;

/*!
 The ChromeCast Application ID of the receiver application to which you wish to cast.
 @discussion Set this property to a receiver's Application ID before scanning for devices in order to restrict the scan to chromeCast devices that support the receiver. Leaving this value nil results in scanning for all ChromeCast devices.
 */
@property (nonatomic) NSString *chromeCastReceiverAppID;

/*!
 The object that acts as the delegate of the JWCastController.
 @discussion The delegate must adopt the JWCastingDelegate protocol. The delegate is not retained.
 */
@property (nonatomic, weak) id<JWCastingDelegate> delegate;

/*!
 The casting device to which you are currently connected. 
 @discussion Will be nil if not connected to any casting devices.
 */
@property (nonatomic, readonly) JWCastingDevice *connectedDevice;

/*!
 The list of casting devices that are currently online.
 @discussion scanForDevices must be called in order to start listening for devices.
 */
@property (nonatomic, readonly) NSArray *availableDevices;

- (instancetype)init __attribute__((unavailable("init not available")));

/*!
 Inits the castController with a player.
 @param player JWPlayerController object currently in use.
 */
- (instancetype)initWithPlayer:(JWPlayerController *)player;

/*!
 Scans for casting devices.
 @see chromeCastReceiverAppID.
 */
- (void)scanForDevices;

/*!
 Connects the castController to a casting device.
 @param device The casting device to which the user would like to cast. A list of available devices can be obtained from the availableDevices property.
 @see availableDevices.
 @see JWCastingDevice.
 */
- (void)connectToDevice:(JWCastingDevice *)device;

/*!
 Disconnects the castController from the connected casting device.
 */
- (void)disconnect;

/*!
 Casts the file being currently reproduced by the linked JWPlayerController instance.
 @discussion You must be connected to a casting device in order to cast.
 */
- (void)cast;

/*!
 Stops the casting and resumes on the JWPlayerController instance when the cast reproduction left off.
 @discussion Calling this method does not disconnect from the casting device.
 */
- (void)stopCasting;

@end
