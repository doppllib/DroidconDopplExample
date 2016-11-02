//
//  JWRelatedConfig.h
//  JWPlayer-iOS-SDK
//
//  Created by Rik Heijdens on 9/1/16.
//  Copyright © 2016 JWPlayer. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    show = 0,
    hide,
    autoplay
}JWRelatedCompleteAction;

typedef enum {
    api = 0,
    complete,
    click
}JWRelatedOpenMethod;

/*!
 An object providing information about the way related videos are handled by the player.
 */
@interface JWRelatedConfig : NSObject

/* ========================================*/
/** @name Configuring the related overlay */

/*!
 (Required) Location of an RSS or JSON file containing a feed of related videos.
 @discussion Must be an url to a RSS or JSON file containing a feed of related videos. See: https://support.jwplayer.com/customer/portal/articles/1483102#implementing-related
*/
@property (nonatomic) NSString *file;

/*!
 What to do when the user clicks a thumbnail.
 */
// Disabled: always play for SDKs
//@property (nonatomic) NSString *onClick;

/*!
 The behavior of our related videos overlay when a single video or playlist is completed.
 @discussion show Display the related overlay (default).
 @discussion hide Replay button and related icon will appear.
 @discussion autoplay Automatically play the next video in your related feed after 10 seconds.
 */
@property (nonatomic) JWRelatedCompleteAction onComplete;

/*!
 Single line heading displayed above the grid with related videos. Generally contains a short call-to-action.
 Default: "Next up"
 */
@property (nonatomic) NSString *heading;

/*!
 The number of seconds to wait before playing the next related video in your content list.
 Set to 0 to have your next related content to play immediately.
 Default: 10
 */
@property (nonatomic) NSInteger autoplayTimer;

/*!
 A custom message that appears during autoplay.
 Note: xx will be replaced by the countdown timer
 Note: __title__ will be replaced by the next title in the related feed.
 Default: "Next up in xx seconds"
 */
@property (nonatomic) NSString *autoplayMessage;

@end
