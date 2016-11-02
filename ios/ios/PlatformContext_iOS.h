//
//  PlatformContext_iOS.h
//  ios
//
//  Created by Sahil Ishar on 3/10/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "DCPConferenceDataHost.h"
#import "DCDEvent.h"
#import "DCDBlock.h"
#import "DCDEventSpeaker.h"
#import "AndroidContentContext.h"
#import "java/util/List.h"
#import "java/util/ArrayList.h"

@protocol PlatformContext_iOSDelegate <NSObject>

- (void)reloadTableView;
- (void)showEventDetailViewWithEvent:(DCDEvent *)event andIndex:(long)index;
@end

@interface PlatformContext_iOS : NSObject <DCPConferenceDataHost, UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, weak) id <PlatformContext_iOSDelegate> reloadDelegate;

@property (nonatomic, assign) BOOL isDayTwo;
@property (nonatomic, strong) __block NSDateFormatter *dateFormatter;
@property (nonatomic, strong) __block NSDateFormatter *timeFormatter;
@property (nonatomic, strong) NSArray *conferenceDays;
@property (nonatomic, strong) NSMutableArray *hourBlocks;
@property (nonatomic, strong) AndroidContentContext *iosContext;

- (void)updateTableData;
- (NSArray *)getSpeakersArrayFromEvent:(DCDEvent *)event;
- (NSString *)getEventTimeFromStart:(NSString *)startTime andEnd:(NSString *)endTime;
- (NSString *)getFullEventTime:(DCDEvent *)event;
+ (NSArray *)javaListToNSArray:(id<JavaUtilList>)list;

@end
