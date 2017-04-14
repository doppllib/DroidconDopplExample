//
//  AppDelegate.m
//  ios
//
//  Created by Kevin Galligan on 2/23/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

#import "AppDelegate.h"
#import "ViewController.h"
#import "DCPAppManager.h"
#import "CoTouchlabDroidconandroidSharedIosIosPlatformClient.h"
#import "CoTouchlabDroidconandroidSharedTasksPersistedRefreshScheduleData.h"
#import "AndroidContentIOSContext.h"
#import "AndroidOsLooper.h"
#import "UIViewController+Utils.h"
//#import "Reachability.h"

@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    
    [AndroidOsLooper prepareMainLooper];
    
    NSString *bundlePath = [[NSBundle mainBundle] pathForResource:@"rebundle" ofType:@"bundle"];
    
    NSLog(@"bundle path %@", bundlePath);
    
//    NSBundle *bundle = [NSBundle bundleWithPath:bundlePath];
    
    DCIosPlatformClient* platformClient = [[DCIosPlatformClient alloc] initWithDCIosFirebase:self];
    
    [DCPAppManager initContextWithAndroidContentContext:[AndroidContentIOSContext new]
                         withDCPPlatformClient:platformClient
                         withDCPAppManager_LoadDataSeed:self];
    
//    Reachability *reachability = [Reachability reachabilityWithHostname:[[NSURL URLWithString:[[DCPAppManager getPlatformClient] baseUrl]] host]];
//    
//    reachability.reachableBlock = ^(Reachability *reachability) {
//        
//    };
//    
//    reachability.unreachableBlock = ^(Reachability *reachability) {
//        
//    };
//    
//    // Start Monitoring
//    [reachability startNotifier];
    
    // Register for remote notifications
    UIUserNotificationType allNotificationTypes =
    (UIUserNotificationTypeSound | UIUserNotificationTypeAlert | UIUserNotificationTypeBadge);
    UIUserNotificationSettings *settings =
    [UIUserNotificationSettings settingsForTypes:allNotificationTypes categories:nil];
    [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
    [[UIApplication sharedApplication] registerForRemoteNotifications];

    return YES;
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    NSString* type = userInfo[@"type"];
    //check the type. event messages just open the app so dont need to the handled here.
    if([type isEqualToString:@"updateSchedule"])
    {
        [CoTouchlabDroidconandroidSharedTasksPersistedRefreshScheduleData callMeWithAndroidContentContext:[AndroidContentIOSContext new]];
        completionHandler(UIBackgroundFetchResultNewData);
    }
    else
    {
        completionHandler(UIBackgroundFetchResultNoData);
    }
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

- (NSString *)dataSeed
{
    NSString *fileName = [[NSBundle mainBundle] pathForResource:@"dataseed"
                                                         ofType:@"json"
                                                    inDirectory:@"dataseeds"];
    
    //check file exists
    if (fileName) {
        //retrieve file content
        NSData *partyData = [[NSData alloc] initWithContentsOfFile:fileName];
        
        NSString *myString = [[NSString alloc] initWithData:partyData encoding:NSUTF8StringEncoding];
        
        return myString;
    }
    else {
        return nil;
    }
}

- (void)logFirebaseNativeWithNSString:(NSString *)s{
    
}

- (void)logPushFirebaseNativeWithNSString:(NSString *)s{
//    CLS_LOG(@"%@", s);
}

- (void)logEventWithNSString:(NSString *)name
           withNSStringArray:(IOSObjectArray *)params{
    
}

@end
