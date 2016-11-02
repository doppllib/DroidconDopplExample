//
//  JWSource.h
//  JWPlayer-iOS-SDK
//
//  Created by Max Mikheyenko on 12/8/14.
//  Copyright (c) 2014 JWPlayer. All rights reserved.
//

#import <Foundation/Foundation.h>

/*!
 An object providing info about quality levels of a video.
 */
@interface JWSource : NSObject

/* ========================================*/
/** @name Accessing Source Attributes */


/*!
 URL of a file representing video quality
 */
@property (nonatomic, retain) NSString *file;

/*!
 A label to be shown in the quality dropdown for this quality
 */
@property (nonatomic, retain) NSString *label;

/*!
 Determines whether current quality is the default.
 */
@property (nonatomic) BOOL defaultQuality;

/*!
 A dictionary containing asset initialization options.
 */
@property (nonatomic) NSDictionary *assetOptions;


/* ========================================*/
/** @name Creating Source Object */

/*!
 Initializes source with file and label.
 @param file URL of a file representing video quality.
 @param label A label to be shown in the quality dropdown for this quality.
 */
+ (instancetype)sourceWithFile:(NSString *)file label:(NSString *)label;

/*!
 Convenience method that initilizes source with provided file, label and sets it to default.
 @param file URL of a file representing video quality.
 @param label A label to be shown in the quality dropdown for this quality.
 @param defaultQuality Determines whether this quality is the default option.
 */
+ (instancetype)sourceWithFile:(NSString *)file label:(NSString *)label isDefault:(BOOL)defaultQuality;

/* ========================================*/
/** @name Initializing Source Object */


/*!
 Initializes source with file and label. Sets source as default.
 @param file URL of a file representing video quality.
 @param label A label to be shown in the quality dropdown for this quality.
 @param defaultQuality Determines whether this quality is the default option.
 */
- (instancetype)initWithFile:(NSString *)file label:(NSString *)label isDefault:(BOOL)defaultQuality;

@end
