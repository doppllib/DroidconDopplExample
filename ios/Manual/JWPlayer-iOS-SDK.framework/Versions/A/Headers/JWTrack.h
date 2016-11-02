//
//  JWTrack.h
//  JWPlayer-iOS-SDK
//
//  Created by Max Mikheyenko on 10/20/14.
//  Copyright (c) 2014 JWPlayer. All rights reserved.
//

#import <Foundation/Foundation.h>

/*!
 An object providing info about captions. Supports DFXP (TTML), SRT, WebVTT formats.
 */
@interface JWTrack : NSObject


/* ========================================*/
/** @name Accessing Captions Track Attributes */

/*!
 Path to the captions file.
 */
@property (nonatomic, retain) NSString *file;

/*!
 Label to be shown in the player in captions popup.
 @discussion not shown if only 1 captions track provided.
 */
@property (nonatomic, retain) NSString *label;

/*!
 String representing kind of track.
 @discussion only 'captions' currently supported.
 */
@property (nonatomic, retain) NSString *kind;

/*!
 If set to yes the player shows this captions track upon launch.
 */
@property (nonatomic) BOOL defaultValue;

/* ========================================*/
/** @name Creating Captions Track Object */


/*!
 Factory method that creates JWTrack object with provided file and label.
 @param file Path to the captions file.
 @param label Label to be shown in the player in captions popup.
 */
+ (instancetype)trackWithFile:(NSString *)file label:(NSString *)label;

/*!
 Factory method that creates JWTrack object with provided file and label and sets current captions track as default.
 @param file Path to the captions file.
 @param label Label to be shown in the player in captions popup.
 @param def If set to yes the player shows this captions track upon launch.
 */
+ (instancetype)trackWithFile:(NSString *)file label:(NSString *)label isDefault:(BOOL)def;

/* ========================================*/
/** @name Initializing Captions Track Object */


/*!
 Inits JWTrack object with provided file and label.
 @param file Path to the captions file.
 @param label Label to be shown in the player in captions popup.
 */
- (instancetype)initWithFile:(NSString *)file label:(NSString *)label;

/*!
 Inits JWTrack object with provided file and label and sets current captions track as default.
 @param file Path to the captions file.
 @param label Label to be shown in the player in captions popup.
 @param def If set to yes the player shows this captions track upon launch.
 */
- (instancetype)initWithFile:(NSString *)file label:(NSString *)label isDefault:(BOOL)def;

@end
