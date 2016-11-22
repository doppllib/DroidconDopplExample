//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: /Users/kgalligan/devel-doppl/core-doppl/androidbase/src/main/java/android/database/MatrixCursor.java
//

#include "J2ObjC_header.h"

#pragma push_macro("INCLUDE_ALL_AndroidDatabaseMatrixCursor")
#ifdef RESTRICT_AndroidDatabaseMatrixCursor
#define INCLUDE_ALL_AndroidDatabaseMatrixCursor 0
#else
#define INCLUDE_ALL_AndroidDatabaseMatrixCursor 1
#endif
#undef RESTRICT_AndroidDatabaseMatrixCursor

#if !defined (AndroidDatabaseMatrixCursor_) && (INCLUDE_ALL_AndroidDatabaseMatrixCursor || defined(INCLUDE_AndroidDatabaseMatrixCursor))
#define AndroidDatabaseMatrixCursor_

#define RESTRICT_AndroidDatabaseAbstractCursor 1
#define INCLUDE_AndroidDatabaseAbstractCursor 1
#include "AndroidDatabaseAbstractCursor.h"

@class AndroidDatabaseMatrixCursor_RowBuilder;
@class IOSByteArray;
@class IOSObjectArray;
@protocol JavaLangIterable;

@interface AndroidDatabaseMatrixCursor : AndroidDatabaseAbstractCursor

#pragma mark Public

- (instancetype)initWithNSStringArray:(IOSObjectArray *)columnNames;

- (instancetype)initWithNSStringArray:(IOSObjectArray *)columnNames
                              withInt:(jint)initialCapacity;

- (void)addRowWithJavaLangIterable:(id<JavaLangIterable>)columnValues;

- (void)addRowWithNSObjectArray:(IOSObjectArray *)columnValues;

- (IOSByteArray *)getBlobWithInt:(jint)column;

- (IOSObjectArray *)getColumnNames;

- (jint)getCount;

- (jdouble)getDoubleWithInt:(jint)column;

- (jfloat)getFloatWithInt:(jint)column;

- (jint)getIntWithInt:(jint)column;

- (jlong)getLongWithInt:(jint)column;

- (jshort)getShortWithInt:(jint)column;

- (NSString *)getStringWithInt:(jint)column;

- (jint)getTypeWithInt:(jint)column;

- (jboolean)isNullWithInt:(jint)column;

- (AndroidDatabaseMatrixCursor_RowBuilder *)newRow OBJC_METHOD_FAMILY_NONE;

@end

J2OBJC_EMPTY_STATIC_INIT(AndroidDatabaseMatrixCursor)

FOUNDATION_EXPORT void AndroidDatabaseMatrixCursor_initWithNSStringArray_withInt_(AndroidDatabaseMatrixCursor *self, IOSObjectArray *columnNames, jint initialCapacity);

FOUNDATION_EXPORT AndroidDatabaseMatrixCursor *new_AndroidDatabaseMatrixCursor_initWithNSStringArray_withInt_(IOSObjectArray *columnNames, jint initialCapacity) NS_RETURNS_RETAINED;

FOUNDATION_EXPORT AndroidDatabaseMatrixCursor *create_AndroidDatabaseMatrixCursor_initWithNSStringArray_withInt_(IOSObjectArray *columnNames, jint initialCapacity);

FOUNDATION_EXPORT void AndroidDatabaseMatrixCursor_initWithNSStringArray_(AndroidDatabaseMatrixCursor *self, IOSObjectArray *columnNames);

FOUNDATION_EXPORT AndroidDatabaseMatrixCursor *new_AndroidDatabaseMatrixCursor_initWithNSStringArray_(IOSObjectArray *columnNames) NS_RETURNS_RETAINED;

FOUNDATION_EXPORT AndroidDatabaseMatrixCursor *create_AndroidDatabaseMatrixCursor_initWithNSStringArray_(IOSObjectArray *columnNames);

J2OBJC_TYPE_LITERAL_HEADER(AndroidDatabaseMatrixCursor)

#endif

#if !defined (AndroidDatabaseMatrixCursor_RowBuilder_) && (INCLUDE_ALL_AndroidDatabaseMatrixCursor || defined(INCLUDE_AndroidDatabaseMatrixCursor_RowBuilder))
#define AndroidDatabaseMatrixCursor_RowBuilder_

@class AndroidDatabaseMatrixCursor;

@interface AndroidDatabaseMatrixCursor_RowBuilder : NSObject

#pragma mark Public

- (AndroidDatabaseMatrixCursor_RowBuilder *)addWithId:(id)columnValue;

- (AndroidDatabaseMatrixCursor_RowBuilder *)addWithNSString:(NSString *)columnName
                                                     withId:(id)value;

#pragma mark Package-Private

- (instancetype)initWithAndroidDatabaseMatrixCursor:(AndroidDatabaseMatrixCursor *)outer$
                                            withInt:(jint)row;

@end

J2OBJC_EMPTY_STATIC_INIT(AndroidDatabaseMatrixCursor_RowBuilder)

FOUNDATION_EXPORT void AndroidDatabaseMatrixCursor_RowBuilder_initWithAndroidDatabaseMatrixCursor_withInt_(AndroidDatabaseMatrixCursor_RowBuilder *self, AndroidDatabaseMatrixCursor *outer$, jint row);

FOUNDATION_EXPORT AndroidDatabaseMatrixCursor_RowBuilder *new_AndroidDatabaseMatrixCursor_RowBuilder_initWithAndroidDatabaseMatrixCursor_withInt_(AndroidDatabaseMatrixCursor *outer$, jint row) NS_RETURNS_RETAINED;

FOUNDATION_EXPORT AndroidDatabaseMatrixCursor_RowBuilder *create_AndroidDatabaseMatrixCursor_RowBuilder_initWithAndroidDatabaseMatrixCursor_withInt_(AndroidDatabaseMatrixCursor *outer$, jint row);

J2OBJC_TYPE_LITERAL_HEADER(AndroidDatabaseMatrixCursor_RowBuilder)

#endif

#pragma pop_macro("INCLUDE_ALL_AndroidDatabaseMatrixCursor")