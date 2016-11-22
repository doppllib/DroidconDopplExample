//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: /Users/kgalligan/devel-doppl/Squeaky-doppl/core/src/main/java/co/touchlab/squeaky/field/types/LongStringType.java
//

#include "CoTouchlabSqueakyFieldSqlType.h"
#include "CoTouchlabSqueakyFieldTypesLongStringType.h"
#include "CoTouchlabSqueakyFieldTypesStringType.h"
#include "IOSObjectArray.h"
#include "J2ObjC_source.h"

@interface CoTouchlabSqueakyFieldTypesLongStringType ()

- (instancetype)init;

@end

inline CoTouchlabSqueakyFieldTypesLongStringType *CoTouchlabSqueakyFieldTypesLongStringType_get_singleTon();
static CoTouchlabSqueakyFieldTypesLongStringType *CoTouchlabSqueakyFieldTypesLongStringType_singleTon;
J2OBJC_STATIC_FIELD_OBJ_FINAL(CoTouchlabSqueakyFieldTypesLongStringType, singleTon, CoTouchlabSqueakyFieldTypesLongStringType *)

__attribute__((unused)) static void CoTouchlabSqueakyFieldTypesLongStringType_init(CoTouchlabSqueakyFieldTypesLongStringType *self);

__attribute__((unused)) static CoTouchlabSqueakyFieldTypesLongStringType *new_CoTouchlabSqueakyFieldTypesLongStringType_init() NS_RETURNS_RETAINED;

__attribute__((unused)) static CoTouchlabSqueakyFieldTypesLongStringType *create_CoTouchlabSqueakyFieldTypesLongStringType_init();

J2OBJC_INITIALIZED_DEFN(CoTouchlabSqueakyFieldTypesLongStringType)

@implementation CoTouchlabSqueakyFieldTypesLongStringType

+ (CoTouchlabSqueakyFieldTypesLongStringType *)getSingleton {
  return CoTouchlabSqueakyFieldTypesLongStringType_getSingleton();
}

J2OBJC_IGNORE_DESIGNATED_BEGIN
- (instancetype)init {
  CoTouchlabSqueakyFieldTypesLongStringType_init(self);
  return self;
}
J2OBJC_IGNORE_DESIGNATED_END

- (instancetype)initWithCoTouchlabSqueakyFieldSqlType:(CoTouchlabSqueakyFieldSqlType *)sqlType
                                    withIOSClassArray:(IOSObjectArray *)classes {
  CoTouchlabSqueakyFieldTypesLongStringType_initWithCoTouchlabSqueakyFieldSqlType_withIOSClassArray_(self, sqlType, classes);
  return self;
}

+ (const J2ObjcClassInfo *)__metadata {
  static J2ObjcMethodInfo methods[] = {
    { NULL, "LCoTouchlabSqueakyFieldTypesLongStringType;", 0x9, -1, -1, -1, -1, -1, -1 },
    { NULL, NULL, 0x2, -1, -1, -1, -1, -1, -1 },
    { NULL, NULL, 0x4, -1, 0, -1, -1, -1, -1 },
  };
  #pragma clang diagnostic push
  #pragma clang diagnostic ignored "-Wobjc-multiple-method-names"
  methods[0].selector = @selector(getSingleton);
  methods[1].selector = @selector(init);
  methods[2].selector = @selector(initWithCoTouchlabSqueakyFieldSqlType:withIOSClassArray:);
  #pragma clang diagnostic pop
  static const J2ObjcFieldInfo fields[] = {
    { "singleTon", "LCoTouchlabSqueakyFieldTypesLongStringType;", .constantValue.asLong = 0, 0x1a, -1, 1, -1, -1 },
  };
  static const void *ptrTable[] = { "LCoTouchlabSqueakyFieldSqlType;[LIOSClass;", &CoTouchlabSqueakyFieldTypesLongStringType_singleTon };
  static const J2ObjcClassInfo _CoTouchlabSqueakyFieldTypesLongStringType = { "LongStringType", "co.touchlab.squeaky.field.types", ptrTable, methods, fields, 7, 0x1, 3, 1, -1, -1, -1, -1, -1 };
  return &_CoTouchlabSqueakyFieldTypesLongStringType;
}

+ (void)initialize {
  if (self == [CoTouchlabSqueakyFieldTypesLongStringType class]) {
    JreStrongAssignAndConsume(&CoTouchlabSqueakyFieldTypesLongStringType_singleTon, new_CoTouchlabSqueakyFieldTypesLongStringType_init());
    J2OBJC_SET_INITIALIZED(CoTouchlabSqueakyFieldTypesLongStringType)
  }
}

@end

CoTouchlabSqueakyFieldTypesLongStringType *CoTouchlabSqueakyFieldTypesLongStringType_getSingleton() {
  CoTouchlabSqueakyFieldTypesLongStringType_initialize();
  return CoTouchlabSqueakyFieldTypesLongStringType_singleTon;
}

void CoTouchlabSqueakyFieldTypesLongStringType_init(CoTouchlabSqueakyFieldTypesLongStringType *self) {
  CoTouchlabSqueakyFieldTypesStringType_initWithCoTouchlabSqueakyFieldSqlType_(self, JreLoadEnum(CoTouchlabSqueakyFieldSqlType, LONG_STRING));
}

CoTouchlabSqueakyFieldTypesLongStringType *new_CoTouchlabSqueakyFieldTypesLongStringType_init() {
  J2OBJC_NEW_IMPL(CoTouchlabSqueakyFieldTypesLongStringType, init)
}

CoTouchlabSqueakyFieldTypesLongStringType *create_CoTouchlabSqueakyFieldTypesLongStringType_init() {
  J2OBJC_CREATE_IMPL(CoTouchlabSqueakyFieldTypesLongStringType, init)
}

void CoTouchlabSqueakyFieldTypesLongStringType_initWithCoTouchlabSqueakyFieldSqlType_withIOSClassArray_(CoTouchlabSqueakyFieldTypesLongStringType *self, CoTouchlabSqueakyFieldSqlType *sqlType, IOSObjectArray *classes) {
  CoTouchlabSqueakyFieldTypesStringType_initWithCoTouchlabSqueakyFieldSqlType_withIOSClassArray_(self, sqlType, classes);
}

CoTouchlabSqueakyFieldTypesLongStringType *new_CoTouchlabSqueakyFieldTypesLongStringType_initWithCoTouchlabSqueakyFieldSqlType_withIOSClassArray_(CoTouchlabSqueakyFieldSqlType *sqlType, IOSObjectArray *classes) {
  J2OBJC_NEW_IMPL(CoTouchlabSqueakyFieldTypesLongStringType, initWithCoTouchlabSqueakyFieldSqlType_withIOSClassArray_, sqlType, classes)
}

CoTouchlabSqueakyFieldTypesLongStringType *create_CoTouchlabSqueakyFieldTypesLongStringType_initWithCoTouchlabSqueakyFieldSqlType_withIOSClassArray_(CoTouchlabSqueakyFieldSqlType *sqlType, IOSObjectArray *classes) {
  J2OBJC_CREATE_IMPL(CoTouchlabSqueakyFieldTypesLongStringType, initWithCoTouchlabSqueakyFieldSqlType_withIOSClassArray_, sqlType, classes)
}

J2OBJC_CLASS_TYPE_LITERAL_SOURCE(CoTouchlabSqueakyFieldTypesLongStringType)