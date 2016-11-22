//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: /Users/kgalligan/devel-doppl/retrofit-doppl/retrofit/src/main/java/retrofit/converter/GsonConverter.java
//

#include "J2ObjC_header.h"

#pragma push_macro("INCLUDE_ALL_RetrofitConverterGsonConverter")
#ifdef RESTRICT_RetrofitConverterGsonConverter
#define INCLUDE_ALL_RetrofitConverterGsonConverter 0
#else
#define INCLUDE_ALL_RetrofitConverterGsonConverter 1
#endif
#undef RESTRICT_RetrofitConverterGsonConverter

#if !defined (RetrofitConverterGsonConverter_) && (INCLUDE_ALL_RetrofitConverterGsonConverter || defined(INCLUDE_RetrofitConverterGsonConverter))
#define RetrofitConverterGsonConverter_

#define RESTRICT_RetrofitConverterConverter 1
#define INCLUDE_RetrofitConverterConverter 1
#include "RetrofitConverterConverter.h"

@class ComGoogleGsonGson;
@protocol JavaLangReflectType;
@protocol RetrofitMimeTypedInput;
@protocol RetrofitMimeTypedOutput;

@interface RetrofitConverterGsonConverter : NSObject < RetrofitConverterConverter >

#pragma mark Public

- (instancetype)initWithComGoogleGsonGson:(ComGoogleGsonGson *)gson;

- (instancetype)initWithComGoogleGsonGson:(ComGoogleGsonGson *)gson
                             withNSString:(NSString *)charset;

- (id)fromBodyWithRetrofitMimeTypedInput:(id<RetrofitMimeTypedInput>)body
                 withJavaLangReflectType:(id<JavaLangReflectType>)type;

- (id<RetrofitMimeTypedOutput>)toBodyWithId:(id)object;

@end

J2OBJC_EMPTY_STATIC_INIT(RetrofitConverterGsonConverter)

FOUNDATION_EXPORT void RetrofitConverterGsonConverter_initWithComGoogleGsonGson_(RetrofitConverterGsonConverter *self, ComGoogleGsonGson *gson);

FOUNDATION_EXPORT RetrofitConverterGsonConverter *new_RetrofitConverterGsonConverter_initWithComGoogleGsonGson_(ComGoogleGsonGson *gson) NS_RETURNS_RETAINED;

FOUNDATION_EXPORT RetrofitConverterGsonConverter *create_RetrofitConverterGsonConverter_initWithComGoogleGsonGson_(ComGoogleGsonGson *gson);

FOUNDATION_EXPORT void RetrofitConverterGsonConverter_initWithComGoogleGsonGson_withNSString_(RetrofitConverterGsonConverter *self, ComGoogleGsonGson *gson, NSString *charset);

FOUNDATION_EXPORT RetrofitConverterGsonConverter *new_RetrofitConverterGsonConverter_initWithComGoogleGsonGson_withNSString_(ComGoogleGsonGson *gson, NSString *charset) NS_RETURNS_RETAINED;

FOUNDATION_EXPORT RetrofitConverterGsonConverter *create_RetrofitConverterGsonConverter_initWithComGoogleGsonGson_withNSString_(ComGoogleGsonGson *gson, NSString *charset);

J2OBJC_TYPE_LITERAL_HEADER(RetrofitConverterGsonConverter)

#endif

#pragma pop_macro("INCLUDE_ALL_RetrofitConverterGsonConverter")