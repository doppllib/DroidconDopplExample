//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: /Users/kgalligan/devel-doppl/MagicThreads-doppl/library/src/main/java/co/touchlab/android/threading/tasks/persisted/BusLog.java
//

#include "CoTouchlabAndroidThreadingTasksPersistedBusLog.h"
#include "J2ObjC_source.h"

@interface CoTouchlabAndroidThreadingTasksPersistedBusLog : NSObject

@end

@implementation CoTouchlabAndroidThreadingTasksPersistedBusLog

+ (const J2ObjcClassInfo *)__metadata {
  static J2ObjcMethodInfo methods[] = {
    { NULL, "I", 0x401, 0, 1, -1, -1, -1, -1 },
    { NULL, "I", 0x401, 0, 2, -1, -1, -1, -1 },
    { NULL, "I", 0x401, 3, 1, -1, -1, -1, -1 },
    { NULL, "I", 0x401, 3, 2, -1, -1, -1, -1 },
    { NULL, "LNSString;", 0x401, 4, 5, -1, -1, -1, -1 },
    { NULL, "I", 0x401, 6, 1, -1, -1, -1, -1 },
    { NULL, "I", 0x401, 6, 2, -1, -1, -1, -1 },
    { NULL, "Z", 0x401, 7, 8, -1, -1, -1, -1 },
    { NULL, "I", 0x401, 9, 10, -1, -1, -1, -1 },
    { NULL, "I", 0x401, 11, 1, -1, -1, -1, -1 },
    { NULL, "I", 0x401, 11, 2, -1, -1, -1, -1 },
    { NULL, "I", 0x401, 12, 13, -1, -1, -1, -1 },
    { NULL, "I", 0x401, 12, 2, -1, -1, -1, -1 },
    { NULL, "I", 0x401, 12, 1, -1, -1, -1, -1 },
    { NULL, "V", 0x401, 14, 2, -1, -1, -1, -1 },
  };
  #pragma clang diagnostic push
  #pragma clang diagnostic ignored "-Wobjc-multiple-method-names"
  methods[0].selector = @selector(dWithNSString:withNSString:);
  methods[1].selector = @selector(dWithNSString:withNSString:withNSException:);
  methods[2].selector = @selector(eWithNSString:withNSString:);
  methods[3].selector = @selector(eWithNSString:withNSString:withNSException:);
  methods[4].selector = @selector(getStackTraceStringWithNSException:);
  methods[5].selector = @selector(iWithNSString:withNSString:);
  methods[6].selector = @selector(iWithNSString:withNSString:withNSException:);
  methods[7].selector = @selector(isLoggableWithNSString:withInt:);
  methods[8].selector = @selector(printlnWithInt:withNSString:withNSString:);
  methods[9].selector = @selector(vWithNSString:withNSString:);
  methods[10].selector = @selector(vWithNSString:withNSString:withNSException:);
  methods[11].selector = @selector(wWithNSString:withNSException:);
  methods[12].selector = @selector(wWithNSString:withNSString:withNSException:);
  methods[13].selector = @selector(wWithNSString:withNSString:);
  methods[14].selector = @selector(logSoftExceptionWithNSString:withNSString:withNSException:);
  #pragma clang diagnostic pop
  static const void *ptrTable[] = { "d", "LNSString;LNSString;", "LNSString;LNSString;LNSException;", "e", "getStackTraceString", "LNSException;", "i", "isLoggable", "LNSString;I", "println", "ILNSString;LNSString;", "v", "w", "LNSString;LNSException;", "logSoftException" };
  static const J2ObjcClassInfo _CoTouchlabAndroidThreadingTasksPersistedBusLog = { "BusLog", "co.touchlab.android.threading.tasks.persisted", ptrTable, methods, NULL, 7, 0x609, 15, 0, -1, -1, -1, -1, -1 };
  return &_CoTouchlabAndroidThreadingTasksPersistedBusLog;
}

@end

J2OBJC_INTERFACE_TYPE_LITERAL_SOURCE(CoTouchlabAndroidThreadingTasksPersistedBusLog)