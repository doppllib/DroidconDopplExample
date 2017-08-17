//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//

#include "J2ObjC_header.h"

#pragma push_macro("INCLUDE_ALL_DINTFindUserInteractorTest")
#ifdef RESTRICT_DINTFindUserInteractorTest
#define INCLUDE_ALL_DINTFindUserInteractorTest 0
#else
#define INCLUDE_ALL_DINTFindUserInteractorTest 1
#endif
#undef RESTRICT_DINTFindUserInteractorTest

#if !defined (DINTFindUserInteractorTest_) && (INCLUDE_ALL_DINTFindUserInteractorTest || defined(INCLUDE_DINTFindUserInteractorTest))
#define DINTFindUserInteractorTest_

@class DDATDatabaseHelper;
@class DINTRxTrampolineSchedulerRule;
@protocol DNETFindUserRequest;

@interface DINTFindUserInteractorTest : NSObject {
 @public
  DINTRxTrampolineSchedulerRule *schedulerRule_;
  DDATDatabaseHelper *helper_;
  id<DNETFindUserRequest> request_;
}

#pragma mark Public

- (instancetype)init;

- (void)setUp;

- (void)whenNetworkAndDatabaseError_ShouldError;

- (void)whenNetworkReturns_ShouldNotGoToDatabase;

- (void)whenNetworkReturnsError_ShouldGetUserFromDatabase;

@end

J2OBJC_EMPTY_STATIC_INIT(DINTFindUserInteractorTest)

J2OBJC_FIELD_SETTER(DINTFindUserInteractorTest, schedulerRule_, DINTRxTrampolineSchedulerRule *)
J2OBJC_FIELD_SETTER(DINTFindUserInteractorTest, helper_, DDATDatabaseHelper *)
J2OBJC_FIELD_SETTER(DINTFindUserInteractorTest, request_, id<DNETFindUserRequest>)

FOUNDATION_EXPORT void DINTFindUserInteractorTest_init(DINTFindUserInteractorTest *self);

FOUNDATION_EXPORT DINTFindUserInteractorTest *new_DINTFindUserInteractorTest_init() NS_RETURNS_RETAINED;

FOUNDATION_EXPORT DINTFindUserInteractorTest *create_DINTFindUserInteractorTest_init();

J2OBJC_TYPE_LITERAL_HEADER(DINTFindUserInteractorTest)

@compatibility_alias CoTouchlabDroidconandroidSharedInteractorsFindUserInteractorTest DINTFindUserInteractorTest;

#endif

#pragma pop_macro("INCLUDE_ALL_DINTFindUserInteractorTest")
