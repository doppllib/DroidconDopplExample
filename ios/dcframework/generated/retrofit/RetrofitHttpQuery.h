//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: /Users/kgalligan/devel-doppl/retrofit-doppl/retrofit/src/main/java/retrofit/http/Query.java
//

#include "J2ObjC_header.h"

#pragma push_macro("INCLUDE_ALL_RetrofitHttpQuery")
#ifdef RESTRICT_RetrofitHttpQuery
#define INCLUDE_ALL_RetrofitHttpQuery 0
#else
#define INCLUDE_ALL_RetrofitHttpQuery 1
#endif
#undef RESTRICT_RetrofitHttpQuery

#if !defined (RetrofitHttpQuery_) && (INCLUDE_ALL_RetrofitHttpQuery || defined(INCLUDE_RetrofitHttpQuery))
#define RetrofitHttpQuery_

#define RESTRICT_JavaLangAnnotationAnnotation 1
#define INCLUDE_JavaLangAnnotationAnnotation 1
#include "java/lang/annotation/Annotation.h"

@class IOSClass;

@protocol RetrofitHttpQuery < JavaLangAnnotationAnnotation >

@property (readonly) NSString *value;
@property (readonly) jboolean encodeName;
@property (readonly) jboolean encodeValue;

@end

@interface RetrofitHttpQuery : NSObject < RetrofitHttpQuery > {
 @public
  NSString *value_;
  jboolean encodeName_;
  jboolean encodeValue_;
}

@end

J2OBJC_EMPTY_STATIC_INIT(RetrofitHttpQuery)

FOUNDATION_EXPORT id<RetrofitHttpQuery> create_RetrofitHttpQuery(jboolean encodeName, jboolean encodeValue, NSString *value);

J2OBJC_TYPE_LITERAL_HEADER(RetrofitHttpQuery)

#endif

#pragma pop_macro("INCLUDE_ALL_RetrofitHttpQuery")