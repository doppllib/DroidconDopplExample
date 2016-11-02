//
//  JWCaptionConfig.h
//  JWPlayer-iOS-SDK
//
//  Created by Karim Mourra on 5/4/15.
//  Copyright (c) 2015 JWPlayer. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef enum {
    none,
    dropshadow,
    raised,
    depressed,
    uniform
}JWEdgeStyle;

/*!
 Configuration object used to customize the captions. Must be set to the JWConfig object used to create the JWPlayerController.
 */
@interface JWCaptionStyling : NSObject

/* ========================================*/
/** @name Accessing Captions Styling Attributes */

/*!
 Overrides the default font color of the captions, including its opacity.
 */
@property (nonatomic, retain) UIColor *fontColor;

/*!
 Changes the background color and the opacity of the overall window the captions reside in.
 */
@property (nonatomic, retain) UIColor *windowColor;

/*!
 Changes the highlight color and highlight opacity of the text.
 */
@property (nonatomic, retain) UIColor *backgroundColor;

/*!
 Overrides the default font style and font size.
 */
@property (nonatomic, retain) UIFont *font;

/*!
 The edge style is an option to put emphasis around text. The available options are: none, dropshadow, raised, depressed, and uniform.
 */
@property (nonatomic) JWEdgeStyle edgeStyle;

@end
