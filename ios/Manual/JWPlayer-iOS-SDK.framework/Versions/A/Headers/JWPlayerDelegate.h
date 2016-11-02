//
//  JWPlayerDelegate.h
//  JWPlayer-iOS-SDK
//
//  Created by Karim Mourra on 5/1/15.
//  Copyright (c) 2015 JWPlayer. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JWPlaylistItem.h"
#import "JWRelatedConfig.h"
#import "JWAdConfig.h"

/*!
 @protocol JWPlayerDelegate
 @discussion The JWPlayerDelegate protocol defines methods that a delegate of a JWPlayerController object can optionally implement to intervene when player callbacks are captured.
 */
@protocol JWPlayerDelegate <NSObject>

@optional

/*!
 onAll(callback)
 @discussion This singular API call can be used to gather all information from the player's API. Note: This will output a large amount of information and may degrade performance if it is used for an extended period of time.
 */
- (void)onAll;

/* ========================================*/
/** @name Playback */

/*!
 onPlayAttempt(callback)
 @discussion Useful for QOE tracking - Triggered the instant a user attempts to play a file. This event fires before both the onPlay and onBeforePlay events.
 */
- (void)onPlayAttempt;

/*!
 onBeforePlay(callback)
 @discussion Fired just before the player begins playing. Unlike the onPlay event, the player will not have begun playing or buffering when triggered, which makes this the right moment to insert preroll ads using playAd().
 */
- (void)onBeforePlay;

/*!
 onFirstFrame(callback)
 @discussion Useful for QOE tracking - Triggered by a video's first frame event (Or the instant an audio file begins playback). This event pinpoints when content playback begins.
 @param loadTime The amount of time (In milliseconds) it takes for the player to transition from a playAttempt to a firstFrame event. Use this to determine the period of time between a user pressing play and the same user viewing their content.
 */
- (void)onFirstFrame:(NSInteger)loadTime;

/*!
 onPlay(callback)
 @discussion Fired when the player enters the 'playing' state.
 */
- (void)onPlay __attribute((deprecated("Use onPlay: instead")));

/*!
 onPlay(callback)
 @discussion Fired when the player enters the 'playing' state.
 */
- (void)onPlay:(NSString *)oldState;

/*!
 onPause(callback)
 @discussion Fired when the player enters the 'paused' state.
 */
- (void)onPause __attribute((deprecated("Use onPause: instead")));

/*!
 onPause(callback)
 @discussion Fired when the player enters the 'paused' state.
 */
- (void)onPause:(NSString *)oldState;

/*!
 onIdle(callback)
 @discussion Fired when the player enters the 'idle' state.
 */
- (void)onIdle __attribute((deprecated("Use onIdle: instead")));

/*!
 onIdle(callback)
 @discussion Fired when the player enters the 'idle' state.
 */
- (void)onIdle:(NSString *)oldState;

/*!
 onReady(callback)
 @discussion Fired when the player has initialized in either Flash or HTML5 and is ready for playback.
 */
- (void)onReady __attribute((deprecated("Use onReady: instead")));

/*!
 onReady(callback)
 @discussion Fired when the player has initialized in either Flash or HTML5 and is ready for playback.
 */
- (void)onReady:(NSInteger)setupTime;

/*!
 onBeforeComplete(callback)
 @discussion Fired just before the player completes playing. Unlike the onComplete event, the player will not have moved on to either showing the replay screen or advancing to the next playlistItem, which makes this the right moment to insert post-roll ads using playAd().
 */
- (void)onBeforeComplete;

/*!
 onComplete(callback)
 @discussion Fired when an item completes playback.
 */
- (void)onComplete;

/* ========================================*/
/** @name Buffer */

/*!
 onBuffer(callback)
 @discussion Fired when the player enters the 'buffering' state.
 */
- (void)onBuffer __attribute((deprecated("Use onBuffer: newState: reason: instead")));

/*!
 onBuffer(callback)
 @discussion Fired when the player enters the 'buffering' state.
 */
- (void)onBuffer:(NSString *)oldState reason:(NSString *)reason;

/*!
 onBuffer(callback)
 @discussion Fired when the currently playing item loads additional data into its buffer.
 @param buffer Percentage between 0 and 100 of the current media that is buffered.
 */
- (void)onBufferChange:(double)buffer;

/* ========================================*/
/** @name Playback Position */

/*!
 onTime(callback)
 @discussion While the player is playing, this event is fired as the playback position gets updated. This may occur as frequently as 10 times per second.
 @param position Playback position in seconds.
 @param duration Duration of the current item in seconds.
 */
- (void)onTime:(double)position ofDuration:(double)duration;

/*!
 onSeek(callback)
 @discussion Fired after a seek has been requested either by scrubbing the controlbar or through the API.
 @param offset  The user requested position to seek to (in seconds). Note the actual position the player will eventually seek to may differ.
 @param position The position of the player before the player seeks (in seconds).
 */
- (void)onSeek:(double)offset fromPosition:(double)position;

/*!
 onSeeked(callback)
 @discussion Triggered when content playback resumes after seeking. As opposed to onSeek, this API listener will only trigger when playback actually continues.
 */
- (void)onSeeked;

/* ========================================*/
/** @name Metadata */

/*!
 onMeta(callback)
 @discussion Fired when new metadata has been broadcasted by the player.
 @param metaData Object containing the new metadata. This can be metadata hidden in the media (ID3, XMP, keyframes) or metadata broadcasted by the playback provider (bandwidth, quality switches).
 */
- (void)onMeta:(NSDictionary *)metaData;

/* ========================================*/
/** @name Caption */

/*!
 onCaptionsList(callback)
 @discussion Fired when the list of available captions tracks is updated. Happens shortly after a playlist item starts playing.
 @param tracks The full array of caption tracks.
 @param selectedTrack The currently selected caption track.
 */
- (void)onCaptionsList:(NSArray *)tracks with:(NSInteger)selectedTrack;

/*!
 onCaptionsChanged (callback)
 @discussion Fired when the active captions track is changed. Happens in response to e.g. a user clicking the controlbar CC menu or a script calling setCurrentCaptions.
 @param selectedTrack Index of the new active captions track in the tracks array. Note the captions are Off if the selectedTrack is 0.
 */
- (void)onCaptionsChanged:(NSInteger)selectedTrack;

/*!
 onCaptionsChange (callback)
 @discussion Fired when the active captions track is changed. Happens in response to e.g. a user clicking the controlbar CC menu or a script calling setCurrentCaptions.
 @param selectedTrack Index of the new active captions track in the tracks array. Note the captions are Off if the selectedTrack is 0.
 */
- (void)onCaptionsChange:(NSInteger)selectedTrack __attribute((deprecated("Use onCaptionsChanged: instead")));

/* ========================================*/
/** @name Quality */

/*!
 onLevels(callback)
 @discussion Fired when the list of available quality levels is updated. Happens e.g. shortly after a playlist item starts playing.
 @param levels The full array of quality levels.
 */
- (void)onLevels:(NSArray *)levels;

/*!
 onQualityLevels(callback)
 @discussion Fired when the list of available quality levels is updated. Happens e.g. shortly after a playlist item starts playing.
 @param levels The full array of quality levels.
 */
- (void)onQualityLevels:(NSArray *)levels __attribute((deprecated("Use onLevels: instead")));

/*!
 onLevelsChanged(callback)
 @discussion Fired when the active quality level is changed. Happens in response to e.g. a user clicking the controlbar quality menu or a script calling setCurrentLevel.
 @param currentLevel Index of the new quality level in the getQualityLevels() array.
 */
- (void)onLevelsChanged:(NSInteger)currentLevel;

/*!
 onQualityChange(callback)
 @discussion Fired when the active quality level is changed. Happens in response to e.g. a user clicking the controlbar quality menu or a script calling setCurrentQuality.
 @param currentQuality Index of the new quality level in the getQualityLevels() array.
 */
- (void)onQualityChange:(NSInteger)currentQuality __attribute((deprecated("Use onLevelsChanged: instead")));

/*!
 onVisualQuality(callback)
 @discussion Fired when the active quality level is changed for HLS. This is different than onLevelsChanged since this will trigger when adaptive streaming automatically shifts quality. In addition, this will also provide reasons why this change happened, as well as information about how and why this change was triggered.
 @param mode The type of quality selection that has been enabled with the player. This will read auto when a user is relying on our automatic quality determination or manual when a user has selected a static quality.
 @param reason Why the quality was changed. This can be initial choice.
 @param label Information about the quality that was changed. This will display your label, bitrate, index, and resolution.
 */
- (void)onVisualQuality:(NSString *)mode reason:(NSString *)reason label:(NSString *)label;

/* ========================================*/
/** @name Audio Track */

/*!
 onAudioTracks (callback)
 @discussion Fired when the list of available audio tracks is updated. Happens e.g. shortly after a playlist item starts playing.
 @param audioTracks The full array with audio tracks.
 */
- (void)onAudioTracks:(NSArray *)audioTracks;

/*!
 onAudioTrackChanged (callback)
 @discussion Fired when the active audio track is changed. Happens in repsponse to e.g. a user clicking the audio tracks menu or setting the currentAudioTrack JWPlayerController property.
 @param currentAudioTrack Index of the newly selected audio track in the JWPlayerController's audioTracks property.
 */
- (void)onAudioTrackChanged:(NSInteger)currentAudioTrack;

/* ========================================*/
/** @name Playlist */

/*!
 onPlaylist(callback)
 @discussion Fired when a new playlist has been loaded into the player. Note this event is not fired as part of the initial playlist load (playlist is loaded when onReady is called).
 @param playlist The new playlist; an array of playlist items.
 */
- (void)onPlaylist:(NSArray *)playlist;

/*!
 onPlaylistItem(callback)
 @discussion Fired when the playlist index changes to a new playlist item. This event occurs before the player begins playing the new playlist item.
 @param index Zero-based index into the playlist array (e.g. 0 is the first item).
 */
- (void)onPlaylistItem:(NSInteger)index;

/*!
 onPlaylistComplete(callback)
 @discussion Fired when the player is done playing all items in the playlist. However, if the repeat option is set true, this is never fired.
 */
- (void)onPlaylistComplete;

/* ========================================*/
/** @name Resize */

/*!
 onFullscreen
 @discussion Fired when the player toggles to/from fullscreen.
 @param status Whether or not video is in fullscreen mode.
 */
- (void)onFullscreen:(BOOL)status;

/*!
 onPictureInPicture
 @discussion Fired when the player enters/exits picture in picture mode. Picture in Picture is only available on iPad Pro, iPad Air (or later), and iPad mini 2 (or later) running iOS 9.
 @param status Whether or not player is displayed in Picture in Picture.
 */
- (void)onPictureInPicture:(BOOL)status;

/* ========================================*/
/** @name Controls */

/*!
 onControls
 @discussion Fired when controls are enabled or disabled by setting the JWPlayerController controls property to a boolean.
 @param status New state of the controls. Is true when the controls were just enabled.
 */
- (void)onControls:(BOOL)status;

/*!
 onDisplayClick(callback)
 @discussion Fired when a user clicks the video display. Especially useful for wiring your own controls when the built-in ones are disabled. Note the default click action (toggling play/pause) will still occur if controls are enabled.
 */
- (void)onDisplayClick;

/* ========================================*/
/** @name Advertising */

/*!
 onAdRequest(callback)
 @discussion VAST and IMA. Fired whenever an ad is requested by the player.
 @param tag The ad tag that is being requested.
 @param adPosition Whether an ad is in a pre, mid, or post position.
 */
- (void)onAdRequest:(NSString *)tag forPosition:(NSString *)adPosition __attribute((deprecated("Use onAdRequest: forPosition: client: creativeType: instead")));

/*!
 onAdRequest(callback)
 @discussion VAST and IMA. Fired whenever an ad is requested by the player.
 @param tag The ad tag that is being requested.
 @param adPosition Whether an ad is in a pre, mid, or post position.
 @param client The client that is currently being used, vast or googima.
 @param creativeType The type of ad that is being played, linear or nonlinear.
 */
- (void)onAdRequest:(NSString *)tag forPosition:(NSString *)adPosition client:(JWAdClient)client creativeType:(NSString *)creativeType;

/*!
 onAdSkipped(callback)
 @discussion VAST and IMA. Fired whenever an ad has been skipped.
 @param tag The ad tag that was skipped.
 */
- (void)onAdSkipped:(NSString *)tag __attribute((deprecated("Use onAdSkipped: client: creativeType: instead")));

/*!
 onAdSkipped(callback)
 @discussion VAST and IMA. Fired whenever an ad has been skipped.
 @param tag The ad tag that was skipped.
 @param client The client that is currently being used, vast or googima.
 @param creativeType The type of ad that is being played, linear or nonlinear.
 */
- (void)onAdSkipped:(NSString *)tag client:(JWAdClient)client creativeType:(NSString *)creativeType;

/*!
 onAdComplete(callback)
 @discussion VAST and IMA. Fired whenever an ad has completed playback.
 @param tag The ad tag that is currently playing.
 */
- (void)onAdComplete:(NSString *)tag __attribute((deprecated("Use onAdComplete: client: creativeType: instead")));

/*!
 onAdComplete(callback)
 @discussion VAST and IMA. Fired whenever an ad has completed playback.
 @param tag The ad tag that is currently playing.
 @param client The client that is currently being used, vast or googima.
 @param creativeType The type of ad that is being played, linear or nonlinear.
 */
- (void)onAdComplete:(NSString *)tag client:(JWAdClient)client creativeType:(NSString *)creativeType;

/*!
 onAdClick(callback)
 @discussion VAST and IMA. Fired whenever a user clicks an ad to be redirected to its landing page.
 @param tag The ad tag that is currently playing.
 */
- (void)onAdClick:(NSString *)tag __attribute((deprecated("Use onAdClick: client: creativeType: instead")));

/*!
 onAdClick(callback)
 @discussion VAST and IMA. Fired whenever a user clicks an ad to be redirected to its landing page.
 @param tag The ad tag that is currently playing.
 @param client The client that is currently being used, vast or googima.
 @param creativeType The type of ad that is being played, linear or nonlinear.
 */
- (void)onAdClick:(NSString *)tag client:(JWAdClient)client creativeType:(NSString *)creativeType;

/*!
 onAdImpression(callback)
 @discussion VAST and IMA. Fired whenever an ad starts playing back. At this point, the VAST tag is loaded and the creative selected.
 @param tag The ad tag that is currently playing.
 */
- (void)onAdImpression:(NSString *)tag __attribute((deprecated("Use onAdImpression: adPosition: adSystem: adTitle: client: creativeType: vastVersion: wrapper: instead")));

/*!
 onAdImpression(callback)
 @discussion VAST and IMA. Fired whenever an ad starts playing back. At this point, the VAST tag is loaded and the creative selected.
 @param tag The ad tag that is currently playing.
 @param adPosition An ad's position.
 @param adSystem AdSystem referenced inside of the VAST XML.
 @param adTitle AdTitle referenced inside of the VAST XML.
 @param client The client that's currently being used, vast or googima.
 @param creativeType The type of ad that is being played.
 */
- (void)onAdImpression:(NSString *)tag adPosition:(NSString *)position
              adSystem:(NSString *)adSystem adTitle:(NSString *)adTitle client:(JWAdClient)client
          creativeType:(NSString *)creativeType vastVersion:(double)vastVersion wrapper:(NSArray *)wrapper;

/*!
 onAdPlay(callback)
 @discussion VAST and IMA. Fired whenever an ad starts playing. Will fire after an ad is unpaused.
 @param tag The ad tag that is currently playing.
 */
- (void)onAdPlay:(NSString *)tag __attribute((deprecated("Use onAdPlay: creativeType: newstate: oldState: instead")));

/*!
 onAdPlay(callback)
 @discussion VAST and IMA. Fired whenever an ad starts playing. Will fire after an ad is unpaused.
 @param tag The ad tag that is currently playing.
 @param creativeType The type of ad that is being played.
 @param newState The new state of the player.
 @param oldState the old state of the player.
 */
- (void)onAdPlay:(NSString *)tag creativeType:(NSString *)creativeType newState:(NSString *)newState oldState:(NSString *)oldState;

/*!
 onAdPause(callback)
 @discussion VAST and IMA. Fired whenever an ad is paused.
 @param tag The ad tag that is currently playing.
 */
- (void)onAdPause:(NSString *)tag __attribute((deprecated("Use onAdPause: creativeType: newState: oldState: instead")));

/*!
 onAdPause(callback)
 @discussion VAST and IMA. Fired whenever an ad is paused.
 @param tag The ad tag that is currently playing.
 @param creativeType The type of ad that is being played.
 @param newState The new state of the player.
 @param oldState the old state of the player.
 */
- (void)onAdPause:(NSString *)tag creativeType:(NSString *)creativeType newState:(NSString *)newState oldState:(NSString *)oldState;

/*!
 onAdTime(callback)
 @discussion VAST and IMA. Fired while ad playback is in progress.
 @param position The current playback position in the ad creative.
 @param duration The total duration of the ad creative.
 @param tag The ad tag that is currently playing.
 @param sequence Returns the sequence number the ad is a part of.
 */
- (void)onAdTime:(double)position ofDuration:(double)duration tag:(NSString *)tag index:(NSInteger)sequence
    __attribute((deprecated("Use onAdTime: ofDuration: tag: index: client: creativeType: instead")));

/*!
 onAdTime(callback)
 @discussion VAST and IMA. Fired while ad playback is in progress.
 @param position The current playback position in the ad creative.
 @param duration The total duration of the ad creative.
 @param tag The ad tag that is currently playing.
 @param sequence Returns the sequence number the ad is a part of.
 */
- (void)onAdTime:(double)position ofDuration:(double)duration tag:(NSString *)tag index:(NSInteger)sequence client:(JWAdClient)client creativeType:(NSString *)creativeType;

/*!
 onAdError(callback)
 @discussion VAST and IMA. Fired whenever an error prevents the ad from playing.
 @param error Object containing the error message under property localizedDescription. The following error messages are possible:
 -invalid ad tag (e.g. invalid XML, broken VAST)
 -ad tag empty (e.g. no ad available after chasing the wrappers)
 -no compatible creatives (e.g. only FLV video in HTML5)
 -error playing creative (e.g. a 404 on the MP4 video)
 -error loading ad tag (for all else)
 When applicable, the userInfo (NSDictionary) property of error will contain the ad tag that is currently playing (key: tag), and/or the vmap (key: vmap). If Google IMA is being used as the ad Client, the imaErrorType will be included (key: imaErrorType) and not the vmap.
 */
- (void)onAdError:(NSError *)error;

/*!
 onAdStarted(callback)
 @discussion VPAID-only. Will trigger when a VPAID ad creative signals to our player that it is starting. This differs from adImpression since the advertisement may not yet be visible.
 @param tag The URL of the ad tag that was started.
 @param creativeType The type of VPAID ad that is being played.
 */
- (void)onAdStarted:(NSString *)tag creativeType:(NSString *)creativeType;

/* ========================================*/
/** @name Related */

/*!
 onOpenRelatedOverlay(callback)
 @discussion Triggers when the related overlay is opened.
 @param method The method used to open the overlay. (api, complete, or interaction)
 @param relatedFile The URL of the feed that was loaded into the player.
 @param items An object of all objects that have been loaded into the related plugin.
 */
- (void)onOpenRelatedOverlay:(JWRelatedOpenMethod)method relatedFile:(NSString *)relatedFile items:(NSArray<JWPlaylistItem *> *)items;

/*!
 onCloseRelatedOverlay(callback)
 @discussion Triggers when the related overlay is closed.
 @param method The method used to close the overlay. (api, complete, or click)
 */
- (void)onCloseRelatedOverlay:(NSString *)method;

/*!
 onRelatedPlay(callback)
 @discussion Triggers when a user selects an object in a related feed.
 @param item Metadata for the chosen item specified in the feed.
 @param auto Returns true if started via autoplay; false if manually started.
 @param position Ordinal position of the related video that has been chosen.
 */
- (void)onRelatedPlay:(JWPlaylistItem *)item auto:(BOOL)autoplay position:(NSInteger)position;

/* ========================================*/
/** @name Error */

/*!
 onError(callback)
 @discussion Fired when a media error has occurred, causing the player to stop playback and go into 'idle' mode.
 @param error Object containing the reason for the error in property localizedDescription. See "Common error messages" on http://support.jwplayer.com/customer/portal/articles/1403682-troubleshooting-your-setup for a list of possible media errors.
 */
- (void)onError:(NSError *)error;

/*!
 onSetupError(callback)
 @discussion Fired when neither the Flash nor HTML5 player could be setup.
 @param error Object containing the error message that describes why the player could not be setup. Error message can be accessed in property localizedDescription.
 */
- (void)onSetupError:(NSError *)error;

@end

