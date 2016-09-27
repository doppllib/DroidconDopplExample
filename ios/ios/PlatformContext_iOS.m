//
//  PlatformContext_iOS.m
//  ios
//
//  Created by Sahil Ishar on 3/10/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

#import "PlatformContext_iOS.h"
#import "co/touchlab/droidconandroid/presenter/ConferenceDayHolder.h"
#import "co/touchlab/droidconandroid/presenter/ScheduleBlockHour.h"
#import "co/touchlab/droidconandroid/data/UserAccount.h"
#import "co/touchlab/droidconandroid/presenter/AppManager.h"
#import "ios-Swift.h"

@implementation PlatformContext_iOS


-(id)init {
    if ( self = [super init] ) {
        self.iosContext = [DCPAppManager getContext];
    }
    return self;
}

- (AndroidContentContext *)appContext {
    return self.iosContext;
}

- (NSString *)storageDir
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    
    return documentsDirectory;
}

- (NSMutableArray *)convertIOSObjectArrayToArray:(IOSObjectArray *)objArray
{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    
    for (int i = 0; i < objArray.length; i++) {
        DCPScheduleBlockHour *holder = [objArray objectAtIndex:i];
        [array addObject:holder];
    }

    return array;
}

- (NSMutableArray *)getHourBlocksArray
{
    IOSObjectArray *objArray;
    NSMutableArray *array = [[NSMutableArray alloc] init];
    
    int index = self.isDayTwo ? 1 : 0;
    
    if (self.conferenceDays.count > index)
    {
        DCPConferenceDayHolder *daySchedule = [self.conferenceDays objectAtIndex:index];
        objArray = daySchedule->hourHolders_;
        [array addObjectsFromArray:[self convertIOSObjectArrayToArray:objArray]];
    }
    
    if (self.dateFormatter == nil) {
        self.dateFormatter = [[NSDateFormatter alloc] init];
        [self.dateFormatter setDateFormat:@"hh:mma"];
    }
    
    return array;
}

- (NSArray *)getSpeakersArrayFromEvent:(DCDEvent *)event
{
    return [PlatformContext_iOS javaListToNSArray:event->speakerList_];
}

+ (NSArray *)javaListToNSArray:(id<JavaUtilList>)list
{
    NSMutableArray *array = [[NSMutableArray alloc] init];
    
    for (int i = 0; i < [list size]; i++) {
        [array addObject:[list getWithInt:(jint)i]];
    }
    
    return array;
}

- (NSString *)formatSpeakerStringFromArray:(NSArray *)array
{
    NSMutableArray *speakerNames = [[NSMutableArray alloc] init];
    
    for (DCDEventSpeaker *speaker in array) {
        [speakerNames addObject:speaker->userAccount_->name_];
    }
    
    return [speakerNames componentsJoinedByString:@", "];
}

- (NSString *)getEventTimeFromStart:(NSString *)startTime andEnd:(NSString *)endTime
{
    int startLoc = 7;
    int endLoc = 7;
    int startLocCutoff = 0;
    
    NSString *startFirstDigit = [startTime substringWithRange:NSMakeRange(startTime.length-7, 1)];
    NSString *endFirstDigit = [endTime substringWithRange:NSMakeRange(endTime.length-7, 1)];
    
    if ([startFirstDigit isEqualToString:@"0"]) {
        startLoc = 6;
    }
    if ([endFirstDigit isEqualToString:@"0"]) {
        endLoc = 6;
    }
    
    NSString *startAmPm = [startTime substringWithRange:NSMakeRange(startTime.length-2, 2)];
    NSString *endAmPm = [endTime substringWithRange:NSMakeRange(endTime.length-2, 2)];
    if ([startAmPm isEqualToString:endAmPm]) {
        startLocCutoff = 2;
    }
    
    NSString *startTimeStr = [startTime substringWithRange:NSMakeRange(startTime.length-startLoc, startLoc-startLocCutoff)];
    NSString *endTimeStr = [endTime substringWithRange:NSMakeRange(endTime.length-endLoc, endLoc)];
    
    return [NSString stringWithFormat:@"%@ - %@", startTimeStr, endTimeStr];
}

- (NSString *)getFullEventTime:(DCDEvent *)event
{
    NSString *day = [[event getStartFormatted] substringWithRange:NSMakeRange(0, 10)];

    if (self.timeFormatter == nil) {
        self.timeFormatter = [[NSDateFormatter alloc] init];
    }
    
    [self.timeFormatter setDateFormat:@"MM/dd/yyyy"];
    NSDate* myDate = [self.timeFormatter dateFromString:day];
    [self.timeFormatter setDateFormat:@"MMMM dd"];
    NSString *dayString = [self.timeFormatter stringFromDate:myDate];
    
    return [NSString stringWithFormat:@"%@, %@", dayString, [self getEventTimeFromStart:[event getStartFormatted] andEnd:[event getEndFormatted]]];
}

- (void)updateTableData
{
    [self.hourBlocks removeAllObjects];
    [self.hourBlocks addObjectsFromArray:[self getHourBlocksArray]];
}

- (void)loadCallbackWithDCPConferenceDayHolderArray:(IOSObjectArray *)conferenceDayHolders
{
    self.hourBlocks = [[NSMutableArray alloc] init];
    
    self.conferenceDays = (NSArray *)conferenceDayHolders;
    [self updateTableData];
    [self.reloadDelegate reloadTableView];
    
}

#pragma Table View - Delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    int index = self.isDayTwo ? 1 : 0;
    
    if(self.conferenceDays == nil || self.conferenceDays.count <= index)
        return 0;

    DCPConferenceDayHolder *daySchedule = [self.conferenceDays objectAtIndex:index];
    return daySchedule->hourHolders_->size_;

}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier = @"cellIdentifier";
    
    EventListCell *cell = (EventListCell*)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    
    DCPScheduleBlockHour *hourHolder = [self.hourBlocks objectAtIndex:[indexPath row]];
//    NSArray *scheduleBlocks = (NSArray *)hourHolder->scheduleBlocks_;
    
    NSObject *eventObj = hourHolder->scheduleBlock_;
    if ([eventObj isKindOfClass:[DCDEvent class]]) {
        DCDEvent *event = (DCDEvent *)eventObj;
        NSArray *speakers = [self getSpeakersArrayFromEvent:event];
        [cell.titleLabel setText: [event->name_ stringByReplacingOccurrencesOfString:@"Android" withString:@"[Sad Puppy]"]];
        [cell.speakerNamesLabel setText:[self formatSpeakerStringFromArray:speakers]];
        [cell.timeLabel setText:hourHolder->hourStringDisplay_];
        
        if([event isRsvped] && ![event isPast])
        {
            cell.rsvpView.hidden = true;
        }
        else{
            cell.rsvpView.hidden = false;
        }
//        [cell.textLabel setTextColor:[UIColor blackColor]];
//        [cell.detailTextLabel setTextColor:[UIColor colorWithRed:(151/255.0) green:(151/255.0) blue:(151/255.0) alpha:1.0]];
//        [cell setBackgroundColor:[UIColor whiteColor]];
//        [cell setUserInteractionEnabled:YES];
    } else {
        DCDBlock *event = (DCDBlock *)eventObj;
        
        [cell.titleLabel setText:event->name_];
        [cell.speakerNamesLabel setText:[self getEventTimeFromStart:[event getStartFormatted] andEnd:[event getEndFormatted]]];
        [cell.timeLabel setText:@" "];
        cell.rsvpView.hidden = true;
                
//        [cell.textLabel setTextColor:[UIColor colorWithRed:(87/255.0) green:(125/255.0) blue:(140/255.0) alpha:1.0]];
//        [cell.detailTextLabel setTextColor:[UIColor colorWithRed:(87/255.0) green:(125/255.0) blue:(140/255.0) alpha:1.0]];
//        [cell setBackgroundColor:[UIColor lightGrayColor]];
//        [cell setUserInteractionEnabled:NO];
    }
    
    cell.layer.opaque = YES;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
    
//    DCPConferenceHourHolder *hourHolder = [self.hourBlocks objectAtIndex:[indexPath section]];
//    NSArray *scheduleBlocks = (NSArray *)hourHolder->scheduleBlocks_;
    
    NSObject *eventObj = ((DCPScheduleBlockHour*)[self.hourBlocks objectAtIndex:[indexPath row]])->scheduleBlock_;
    if ([eventObj isKindOfClass:[DCDEvent class]]) {
        DCDEvent *event = (DCDEvent *)eventObj;
        [self.reloadDelegate showEventDetailViewWithEvent:event andIndex:[indexPath row]];
    }
}

@end
