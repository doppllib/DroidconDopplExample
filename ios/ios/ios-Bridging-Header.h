//
//  Use this file to import your target's public headers that you would like to expose to Swift.
//
#import "co/touchlab/droidconandroid/presenter/AppManager.h"
#import "co/touchlab/droidconandroid/presenter/EventDetailPresenter.h"
#import "co/touchlab/droidconandroid/presenter/EventDetailHost.h"
#import "co/touchlab/droidconandroid/tasks/EventDetailLoadTask.h"
#import "co/touchlab/droidconandroid/tasks/EventVideoDetailsTask.h"
#import "co/touchlab/droidconandroid/tasks/StartWatchVideoTask.h"

#import "co/touchlab/droidconandroid/data/AppPrefs.h"
#import "android/content/Context.h"
#import "android/content/IOSContext.h"
#import "PlatformContext_iOS.h"
#import "java/util/ArrayList.h"

#import "java/lang/Integer.h"
#import "java/util/List.h"
#import "co/touchlab/droidconandroid/data/Event.h"
#import "co/touchlab/droidconandroid/data/Venue.h"
#import "co/touchlab/droidconandroid/data/Track.h"
#import "co/touchlab/droidconandroid/data/UserAccount.h"
#import "co/touchlab/droidconandroid/data/EventSpeaker.h"
#import <JWPlayer-SDK/JWPlayerController.h>
