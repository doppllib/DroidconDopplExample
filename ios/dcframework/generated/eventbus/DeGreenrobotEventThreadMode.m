//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: /Users/kgalligan/devel-doppl/EventBus-doppl/EventBus/src/main/java/de/greenrobot/event/ThreadMode.java
//

#include "DeGreenrobotEventThreadMode.h"
#include "IOSObjectArray.h"
#include "J2ObjC_source.h"
#include "java/lang/Enum.h"
#include "java/lang/IllegalArgumentException.h"

__attribute__((unused)) static void DeGreenrobotEventThreadMode_initWithNSString_withInt_(DeGreenrobotEventThreadMode *self, NSString *__name, jint __ordinal);

J2OBJC_INITIALIZED_DEFN(DeGreenrobotEventThreadMode)

DeGreenrobotEventThreadMode *DeGreenrobotEventThreadMode_values_[4];

@implementation DeGreenrobotEventThreadMode

+ (IOSObjectArray *)values {
  return DeGreenrobotEventThreadMode_values();
}

+ (DeGreenrobotEventThreadMode *)valueOfWithNSString:(NSString *)name {
  return DeGreenrobotEventThreadMode_valueOfWithNSString_(name);
}

- (id)copyWithZone:(NSZone *)zone {
  return self;
}

+ (const J2ObjcClassInfo *)__metadata {
  static J2ObjcMethodInfo methods[] = {
    { NULL, "[LDeGreenrobotEventThreadMode;", 0x9, -1, -1, -1, -1, -1, -1 },
    { NULL, "LDeGreenrobotEventThreadMode;", 0x9, 0, 1, -1, -1, -1, -1 },
  };
  #pragma clang diagnostic push
  #pragma clang diagnostic ignored "-Wobjc-multiple-method-names"
  methods[0].selector = @selector(values);
  methods[1].selector = @selector(valueOfWithNSString:);
  #pragma clang diagnostic pop
  static const J2ObjcFieldInfo fields[] = {
    { "PostThread", "LDeGreenrobotEventThreadMode;", .constantValue.asLong = 0, 0x4019, -1, 2, -1, -1 },
    { "MainThread", "LDeGreenrobotEventThreadMode;", .constantValue.asLong = 0, 0x4019, -1, 3, -1, -1 },
    { "BackgroundThread", "LDeGreenrobotEventThreadMode;", .constantValue.asLong = 0, 0x4019, -1, 4, -1, -1 },
    { "Async", "LDeGreenrobotEventThreadMode;", .constantValue.asLong = 0, 0x4019, -1, 5, -1, -1 },
  };
  static const void *ptrTable[] = { "valueOf", "LNSString;", &JreEnum(DeGreenrobotEventThreadMode, PostThread), &JreEnum(DeGreenrobotEventThreadMode, MainThread), &JreEnum(DeGreenrobotEventThreadMode, BackgroundThread), &JreEnum(DeGreenrobotEventThreadMode, Async), "Ljava/lang/Enum<Lde/greenrobot/event/ThreadMode;>;" };
  static const J2ObjcClassInfo _DeGreenrobotEventThreadMode = { "ThreadMode", "de.greenrobot.event", ptrTable, methods, fields, 7, 0x4011, 2, 4, -1, -1, -1, 6, -1 };
  return &_DeGreenrobotEventThreadMode;
}

+ (void)initialize {
  if (self == [DeGreenrobotEventThreadMode class]) {
    size_t objSize = class_getInstanceSize(self);
    size_t allocSize = 4 * objSize;
    uintptr_t ptr = (uintptr_t)calloc(allocSize, 1);
    id e;
    id names[] = {
      @"PostThread", @"MainThread", @"BackgroundThread", @"Async",
    };
    for (jint i = 0; i < 4; i++) {
      (DeGreenrobotEventThreadMode_values_[i] = e = objc_constructInstance(self, (void *)ptr), ptr += objSize);
      DeGreenrobotEventThreadMode_initWithNSString_withInt_(e, names[i], i);
    }
    J2OBJC_SET_INITIALIZED(DeGreenrobotEventThreadMode)
  }
}

@end

void DeGreenrobotEventThreadMode_initWithNSString_withInt_(DeGreenrobotEventThreadMode *self, NSString *__name, jint __ordinal) {
  JavaLangEnum_initWithNSString_withInt_(self, __name, __ordinal);
}

IOSObjectArray *DeGreenrobotEventThreadMode_values() {
  DeGreenrobotEventThreadMode_initialize();
  return [IOSObjectArray arrayWithObjects:DeGreenrobotEventThreadMode_values_ count:4 type:DeGreenrobotEventThreadMode_class_()];
}

DeGreenrobotEventThreadMode *DeGreenrobotEventThreadMode_valueOfWithNSString_(NSString *name) {
  DeGreenrobotEventThreadMode_initialize();
  for (int i = 0; i < 4; i++) {
    DeGreenrobotEventThreadMode *e = DeGreenrobotEventThreadMode_values_[i];
    if ([name isEqual:[e name]]) {
      return e;
    }
  }
  @throw create_JavaLangIllegalArgumentException_initWithNSString_(name);
  return nil;
}

DeGreenrobotEventThreadMode *DeGreenrobotEventThreadMode_fromOrdinal(NSUInteger ordinal) {
  DeGreenrobotEventThreadMode_initialize();
  if (ordinal >= 4) {
    return nil;
  }
  return DeGreenrobotEventThreadMode_values_[ordinal];
}

J2OBJC_CLASS_TYPE_LITERAL_SOURCE(DeGreenrobotEventThreadMode)