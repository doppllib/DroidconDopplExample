//
//  JWConfigObject.h
//  JWPlayer-iOS-SDK
//
//  Created by Max Mikheyenko on 8/25/14.
//  Copyright (c) 2014 JWPlayer. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "JWAdConfig.h"
#import "JWAdBreak.h"
#import "JWTrack.h"
#import "JWSource.h"
#import "JWPlaylistItem.h"
#import "JWCaptionStyling.h"
#import "JWRelatedConfig.h"

typedef enum {
    JWPremiumSkinSeven = 0,
    JWPremiumSkinBeelden,
    JWPremiumSkinBekle,
    JWPremiumSkinFive,
    JWPremiumSkinGlow,
    JWPremiumSkinRoundster,
    JWPremiumSkinStormtrooper,
    JWPremiumSkinVapor,
    JWPremiumSkinSix
}JWPremiumSkin;

typedef enum {
    JWStretchUniform = 0,
    JWStretchExactFit,
    JWStretchFill,
    JWStretchNone
}JWStretching;

@class JWAdConfig;

/*!
 Configuration object used to create JW Player instance.
 */
@interface JWConfig : NSObject

/* ========================================*/
/** @name Accessing Config Attributes */


/*!
 Configuration object used to customize the captions.
 */
@property (nonatomic, retain) JWCaptionStyling *captionStyling;

/*!
 Video URL to play using JW Player.
 */
@property (nonatomic, retain) NSString *file;

/*!
 An array of JWSource objects representing multiple quality levels of a video.
 @see JWSource
 */
@property (nonatomic, retain) NSArray *sources;

/*!
 An array of JWPlaylistItem objects containing information about different video items to be reproduced in a sequence.
 @see JWPlaylistItem
 */
@property (nonatomic, retain) NSArray *playlist;

/*!
 Title of the video
 @discussion Shown in the play button container in the center of the screen, before the video starts to play.
 */
@property (nonatomic, retain) NSString *title;

/*!
 The URL of the thumbnail image.
 */
@property (nonatomic, retain) NSString *image;

/*!
 A dictionary containing asset initialization options.
 */
@property (nonatomic) NSDictionary *assetOptions;

/*!
 The image you wish to display if the user gets disconnected from the internet. If this is nil, your thumbnail image will be displayed.
 */
@property (nonatomic, retain) UIImage *offlinePoster;

/*!
 The message you wish to display if the user gets disconnected from the internet. If this is nil, "Internet Lost" will be displayed.
 */
@property (nonatomic, retain) NSString *offlineMessage;

/*!
 Player view size.
 */
@property (nonatomic) CGSize size;

/*!
 adConfig object providing info about ad handling.
 @see JWAdConfig
 */
@property (nonatomic, retain) JWAdConfig *adConfig;

/*!
 Config object containing related settings.
 @see JWRelatedConfig
 */
@property (nonatomic) JWRelatedConfig *relatedConfig;

/*!
 A boolean value that determines whether player controls are shown.
 */
@property (nonatomic) BOOL controls;

/*!
 A boolean value that determines whether video should repeat after it's done playing.
 */
@property (nonatomic) BOOL repeat;

/*!
 A boolean value that determines whether video should start automatically after loading.
 */
@property (nonatomic) BOOL autostart;

/*!
 An array of JWAdBreak objects that proivides info about ad breaks.
 @discussion this property is ignored if adVmap not nil.
 @see JWAdBreak
 */
@property (nonatomic, retain) NSArray *adSchedule;

/*!
 Vast vmap file to use for ad breaks.
 @discussion adSchedule is ignored if this property is not nil.
 */
@property (nonatomic, retain) NSString *adVmap;

/*!
 Sets a premium JW Player skin to use with the player.
 */
@property (nonatomic) JWPremiumSkin premiumSkin;

/*!
 A url to a CSS file that contains a skin to be used during player setup.
 */
@property (nonatomic, retain) NSString *cssSkin;

/*!
 An array of JWTrack objects providing captions for different languages.
 @see JWTrack
 */
@property (nonatomic, retain) NSArray *tracks;

/*!
 Provides an option to stretch the video.
 @discussion JWStretchingUniform (default) - Will fit JW Player dimensions while maintaining original aspect ratio (Black bars)
 @discussion JWStretchingExactFit - Will fit JW Player dimensions without maintaining aspect ratio
 @discussion JWStretchingFill - Will stretch and zoom video to fill dimensions, while maintaining aspect ratio
 @discussion JWStretchingNone - Displays the actual size of the video file. (Black borders)
 */
@property (nonatomic) JWStretching stretch;

/* ========================================*/
/** @name Creating Config Object */

/*!
 Inits a JWConfig object with provided video url.
 @param contentUrl URL of the video content.
 */
+ (instancetype)configWithContentURL:(NSString *)contentUrl;

/* ========================================*/
/** @name Initializing Config Object */

/*!
 Factory method that creates a JWConfig object with url of video content.
 @param contentUrl URL of the video content.
 */
- (instancetype)initWithContentUrl:(NSString *)contentUrl;

@end
