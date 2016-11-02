//
//  JWPlayerViewController.h
//  JWPlayer-iOS-SDK
//
//  Created by Max Mikheyenko on 8/14/14.
//  Copyright (c) 2014 JWPlayer. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "JWConfig.h"
#import "JWPlayerDelegate.h"
#import "JWCastController.h"
#import "JWDrmDataSource.h"

#define JWPlayerAllNotification @"JWPlayerAllNotification"
#define JWMetaDataAvailableNotification @"JWMetaDataAvailableNotification"
#define JWPlayerStateChangedNotification @"JWPlayerStateChangedNotification"
#define JWPlaybackPositionChangedNotification @"JWPlaybackPositionChangedNotification"
#define JWFullScreenStateChangedNotification @"JWFullScreenStateChangedNotification"
#define JWAdActivityNotification @"JWAdActivityNotification"
#define JWAdPlaybackProgressNotification @"JWAdPlaybackProgressNotification"
#define JWAdClickNotification @"JWAdClickNotification"
#define JWErrorNotification @"JWErrorNotification"
#define JWCaptionsNotification @"JWCaptionsNotification"
#define JWVideoQualityNotification @"JWVideoQualityNotification"
#define JWPlaylistNotification @"JWPlaylistNotification"
#define JWAudioTrackNotification @"JWAudioTrackNotification"
#define JWPictureInPictureNotification @"JWPictureInPictureNotification"
#define JWRelatedActivityNotification @"JWRelatedActivityNotification"

/*!
 A class that encapsulates JW Player and provides control over the playback as well as holds the state of the player and notifies about status updates.
 */
@interface JWPlayerController : NSObject

/* ========================================*/
/** @name Accessing Player Controller Attributes */

/*!
 Player view.
 @discussion to be added to the application view hierarchy.
 */
@property (nonatomic, retain, readonly) UIView *view;

/*!
The object that acts as the delegate of the jwPlayerController.
 @discussion The delegate must adopt the JWPlayerDelegate protocol. The delegate is not retained.
 @see JWPlayerDelegate
 */
@property (nonatomic, weak) id<JWPlayerDelegate> delegate;

/*!
 The JWDrmDataSource is adopted by an object that mediates the application's data model and key server. The data source provides the JWPlayerController object with the data needed to reproduce encrypted content.
 @discussion The drmDataSource must adopt the JWDrmDataSource protocol. The drmDataSource is not retained.
 @see JWDrmDataSource
 */
@property (nonatomic, weak) id<JWDrmDataSource> drmDataSource;

/*!
 Returns the version of google IMA framework used by the SDK.
 */
@property (nonatomic, retain, readonly) NSString *googleIMAVersion;

/*!
 Returns the version of google ChromeCast framework used by the SDK.
*/
@property (nonatomic, retain, readonly) NSString *googleChromeCastVersion;

/*!
 Returns current state of the player.
 @discussion Can be "idle", "playing", "paused" and "buffering".
 */
@property (nonatomic, retain, readonly) NSString *playerState;

/*!
 Metadata associated with the current video. Usually includes dimensions and duration of the video.
 @discussion becomes available shortly after the video starts playing. There is a notification JWMetaDataAvailableNotification posted right after metadata is available.
 */
@property (nonatomic, retain, readonly) NSDictionary *metadata;

/*!
 Dimensions of the current video. Becomes available shortly after the video starts to play as a part of metadata.
 */
@property (nonatomic, readonly) CGSize naturalSize;


/*!
 JWConfig object that was used to setup the player.
 @discussion Check JWConfig documentation for more info.
 */
@property (nonatomic, retain, readonly) JWConfig *config;


/*!
 Returns the current PlaylistItem's filled buffer, as a percentage (0 to 100) of the total video's length.
 @discussion This only applies to progressive downloads of media (MP4/FLV/WebM and AAC/MP3/Vorbis); streaming media (HLS/RTMP/YouTube/DASH) do not expose this behavior.
 */
@property (nonatomic, readonly) NSInteger buffer;

/*!
 Enable the built-in controls by setting them true, disable the controls by setting them false.
 */
@property (nonatomic) BOOL controls;

/*!
 When enabled, the user will be able to control playback of the current video (play, pause, and when applicable next/previous) from the device's Lock Screen and some information (title, playback position, duration, poster image) will be presented on the lockscreen. Defaults to YES.
 @discussion In order for the lock screen controls to appear, background audio must be enabled and the audio session must be set to AVAudioSessionCategoryPlayback.
 */
@property (nonatomic) BOOL displayLockScreenControls;

/*!
 Returns the region of the display not used by the controls. You can use this information to ensure your visual assets don't overlap with the controls.
 */
@property (nonatomic, readonly) CGRect safeRegion;

/* ========================================*/
/** @name Managing Video Quality Levels */

/*!
 The index of the object in quality levels list currently used by the player.
 */
@property (nonatomic) NSInteger currentQualityLevel;

/*!
 List of quality levels available for the current media.
 */
@property (nonatomic, retain, readonly) NSArray *levels;

/* ========================================*/
/** @name Managing Closed Captions */

/*!
 The index of the caption object in captions list currently used by the player.
 @discussion index 0 stands for no caption.
 @see captionsList
 */
@property (nonatomic) NSInteger currentCaptions;

/*!
 List of all the captions supplied in the config
 @discussion Use currentCaptions to activate one of the captions programmatically.
 Object at index 0 is "off".
 @see currentCaptions
 */
@property (nonatomic, retain, readonly) NSArray *captionsList;

/* ========================================*/
/** @name Managing Audio Tracks */


/*!
 The index of the currently active audio track.
 */
@property (nonatomic) NSInteger currentAudioTrack;

/*!
 Array with audio tracks from the player.
 */
@property (nonatomic, retain, readonly) NSArray *audioTracks;

/* ========================================*/
/** @name Managing Playlists */

/*!
 The index of the currently active item in the playlist.
 */
@property (nonatomic) NSInteger playlistIndex;


/* ========================================*/
/** @name Initializing Player Controller Object */

/*!
 Inits the player with config object in JWConfig format.
 @param config  JWConfig object that is used to setup the player.
 */
- (instancetype)initWithConfig:(JWConfig *)config;

/*!
 Inits the player with config object in JWConfig format and sets the object that acts as the delegate of the JWPlayerController.
  @param config JWConfig object that is used to setup the player.
 @param delegate The object that acts as the delegate of the jwPlayerController.
 @see JWPlayerDelegate
 */
- (instancetype)initWithConfig:(JWConfig *)config delegate:(id<JWPlayerDelegate>)delegate;

/*!
 Inits the player with a JWConfig object and sets the object that acts as a DRM data source, as well as the delegate of the JWPlayerController.
 @param config JWConfig object that is used to setup the player.
 @param delegate The object that acts as the delegate of the jwPlayerController.
 @param drmDataSource The object that acts as a data source for reproducing drm encrypted content.
 @see JWPlayerDelegate, JWDrmDataSource
 */
- (instancetype)initWithConfig:(JWConfig *)config delegate:(id<JWPlayerDelegate>)delegate drmDataSource:(id<JWDrmDataSource>)drmDataSource;

/* ========================================*/
/** @name Managing Playback */


/*!
 Starts to play video from current position.
 */
- (void)play;

/*!
 Pauses video.
 */
- (void)pause;

/*!
 Stops the player (returning it to the idle state) and unloads the currently playing media file.
 */
- (void)stop;

/*!
 @param position Time in the video to seek to
 @see duration
 */
- (void)seek:(NSUInteger)position;

/*!
 Playback position of the current video.
 @discussion gets updated as the video plays. JWPlaybackProgressNotification is posted every time position changes. KVO compliant.
 */
@property (nonatomic, retain, readonly) NSNumber *playbackPosition;

/*!
 Duration of the current video. Becomes available shortly after the video starts to play as a part of metadata.
 */
@property (nonatomic, readonly) double duration;

/*!
 The volume of the JWPlayerController's audio. At 0.0 the player is muted, at 1.0 the player's volume is as loud as the device's volume.
 @discussion This property should be used to control the volume of the player relative to other audio output, not for volume              control by viewers. This property will have no effect when ads are played using Google IMA, or when casting. Viewers can control volume when casting by changing the device's volume.
 */
@property (nonatomic) CGFloat volume;


/* ========================================*/
/** @name Managing Full Screen / Picture in Picture */

/*!
 A Boolean value that determines whether the video is in full screen.
 */
@property (nonatomic, readonly) BOOL isInFullscreen;

/*!
 A Boolean value that determines whether the video should go to full screen mode when the device rotates to landscape.
 @discussion Make sure your app supports landscape to make this property work.
 */
@property (nonatomic) BOOL forceFullScreenOnLandscape;

/*!
 A Boolean value that determines whether the video should rotate to landscape when the fullscreen button is pressed.
 @discussion Make sure your app supports landscape to make this property work.
 */
@property (nonatomic) BOOL forceLandscapeOnFullScreen;

/*!
 A Boolean value that determines whether the video should allow Picture In Picture display. Default value is NO.
 @discussion Picture in Picture is only available on iPad Pro, iPad Air (or later), and iPad mini 2 (or later) running iOS 9.
 */
@property (nonatomic) BOOL pictureInPictureDisabled;

/*!
 Switches the player to full screen mode.
 */
- (void)enterFullScreen;

/*!
 Switches the player to inline mode.
 */
- (void)exitFullScreen;

/*!
 Toggles the player into and out of Picture In Picture display.
 @discussion Picture in Picture is only available on iPad Pro, iPad Air (or later), and iPad mini 2 (or later) running iOS 9.
 */
- (void)togglePictureInPicture;

/* ========================================*/
/** @name Loading New Media */


/*!
 Loads a new file into the player.
 @param file Video URL to play using JW Player.
 */
- (void)load:(NSString *)file;

/*!
 Loads a new JWConfig object into the player.
 @param config COnfiguration object.
 */
- (void)loadConfig:(JWConfig *)config;

/*!
 Loads a new playlist into the player.
 @param playlist An array containing playlist items.
 */
- (void)loadPlaylist:(NSArray *)playlist;

/* ========================================*/
/** @name Injecting Ads */


/*!
 Immediately starts to play an ad using the vastPlugin.
 @param tag Xml file with info about the ad.
 @discussion Usually used to inject an ad in streams where you can't schedule an ad. If you wish to play the ad with the Google IMA Client, please use playAd:onClient: instead and specify 'googima' as your ad client.
 */
- (void)playAd:(NSString *)tag;

/*!
 Immediately starts to play an ad.
 @param tag Xml file with info about the ad.
 @param adClient Set to googima if you wish to use google IMA; set to vastPlugin if not. Setting to nil defaults to vastPlugin. Note: Due to the fact that Google IMA's iOS SDK is still in Beta mode, we suggest using the vastPlugin.
 @discussion Usually used to inject an ad in streams where you can't schedule an ad.
 @see JWAdClient
 */
- (void)playAd:(NSString *)tag onClient:(JWAdClient)adClient;

/*!
 If set to YES will open Safari after the user clicks the ad.
 */
@property (nonatomic) BOOL openSafariOnAdClick;

/* ========================================*/
/** @name Related */

/*!
 Opens the related overlay. This will pause content if it is currently playing.
 */
- (void)openRelatedOverlay;

/*!
 Closes the related plugin overlay. This will resume content.
 */
- (void)closeRelatedOverlay;

/* ========================================*/
/** @name Accessing SDK Info */

/*!
 Version of underlying web player
 */
@property (nonatomic, retain, readonly) NSString *playerVersion;

/*!
 Player edition based on the provided JW License key
 */
@property (nonatomic, retain, readonly) NSString *playerEdition;

/*!
 Version of the iOS SDK
 */
+ (NSString *)SDKVersion;

/*!
 Version of the iOS SDK, truncated. 
 (i.e.: if SDKVersion returns 1.001, SDKVersionToMinor returns 1.).
 */
+ (NSString *)SDKVersionToMinor;

@end
