//
//  JWAdConfig.h
//  JWPlayer-iOS-SDK
//
//  Created by Max Mikheyenko on 10/3/14.
//  Copyright (c) 2014 JWPlayer. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    vastPlugin = 1,
    googIMA = 2
}JWAdClient;

/*!
 An object providing information about the way ads are handled by the player. Describes adMessage, skipMessage, skipText and skipOffset.
 @discussion In current implementation adConfig object can be added to config and propagates to all adBreaks.
 */
@interface JWAdConfig : NSObject

/* ========================================*/
/** @name Accessing Ad Config Attributes */


/*!
 A message to be shown to the user in place of a seekbar while the ad is playing.
 @discussion 'xx' in the message is replaced with countdown timer until the end of the ad.
 */
@property (nonatomic, retain) NSString *adMessage;

/*!
 A message to be shown on the skip button during countdown to skip availablilty.
 @discussion 'xx' in the message is replaced with countdown timer until the moment skip becomes available.
 @see skipText
 */
@property (nonatomic, retain) NSString *skipMessage;

/*!
 A message to be shown on the skip button when the skip option becomes avilable.
 */
@property (nonatomic, retain) NSString *skipText;

/*!
 An integer representing the number of seconds before the ad can be skept.
 */
@property (nonatomic) NSInteger skipOffset;

/*!
 Set to googima if you wish to use google IMA; set to vastPlugin if not. Setting to nil defaults to vastPlugin.
 @discussion Due to the fact that Google IMA's iOS SDK is still in Beta mode, we suggest using the vastPlugin.
 */
@property (nonatomic) JWAdClient adClient;

@end