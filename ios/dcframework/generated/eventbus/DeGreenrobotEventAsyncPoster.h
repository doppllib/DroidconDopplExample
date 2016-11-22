//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: /Users/kgalligan/devel-doppl/EventBus-doppl/EventBus/src/main/java/de/greenrobot/event/AsyncPoster.java
//

#include "J2ObjC_header.h"

#pragma push_macro("INCLUDE_ALL_DeGreenrobotEventAsyncPoster")
#ifdef RESTRICT_DeGreenrobotEventAsyncPoster
#define INCLUDE_ALL_DeGreenrobotEventAsyncPoster 0
#else
#define INCLUDE_ALL_DeGreenrobotEventAsyncPoster 1
#endif
#undef RESTRICT_DeGreenrobotEventAsyncPoster

#if !defined (DeGreenrobotEventAsyncPoster_) && (INCLUDE_ALL_DeGreenrobotEventAsyncPoster || defined(INCLUDE_DeGreenrobotEventAsyncPoster))
#define DeGreenrobotEventAsyncPoster_

#define RESTRICT_JavaLangRunnable 1
#define INCLUDE_JavaLangRunnable 1
#include "java/lang/Runnable.h"

@class DeGreenrobotEventEventBus;
@class DeGreenrobotEventSubscription;

@interface DeGreenrobotEventAsyncPoster : NSObject < JavaLangRunnable >

#pragma mark Public

- (void)enqueueWithDeGreenrobotEventSubscription:(DeGreenrobotEventSubscription *)subscription
                                          withId:(id)event;

- (void)run;

#pragma mark Package-Private

- (instancetype)initWithDeGreenrobotEventEventBus:(DeGreenrobotEventEventBus *)eventBus;

@end

J2OBJC_EMPTY_STATIC_INIT(DeGreenrobotEventAsyncPoster)

FOUNDATION_EXPORT void DeGreenrobotEventAsyncPoster_initWithDeGreenrobotEventEventBus_(DeGreenrobotEventAsyncPoster *self, DeGreenrobotEventEventBus *eventBus);

FOUNDATION_EXPORT DeGreenrobotEventAsyncPoster *new_DeGreenrobotEventAsyncPoster_initWithDeGreenrobotEventEventBus_(DeGreenrobotEventEventBus *eventBus) NS_RETURNS_RETAINED;

FOUNDATION_EXPORT DeGreenrobotEventAsyncPoster *create_DeGreenrobotEventAsyncPoster_initWithDeGreenrobotEventEventBus_(DeGreenrobotEventEventBus *eventBus);

J2OBJC_TYPE_LITERAL_HEADER(DeGreenrobotEventAsyncPoster)

#endif

#pragma pop_macro("INCLUDE_ALL_DeGreenrobotEventAsyncPoster")