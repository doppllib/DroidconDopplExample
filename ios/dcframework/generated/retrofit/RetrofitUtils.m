//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: /Users/kgalligan/devel-doppl/retrofit-doppl/retrofit/src/main/java/retrofit/Utils.java
//

#include "IOSClass.h"
#include "IOSObjectArray.h"
#include "IOSPrimitiveArray.h"
#include "J2ObjC_source.h"
#include "RetrofitClientRequest.h"
#include "RetrofitClientResponse.h"
#include "RetrofitMimeTypedByteArray.h"
#include "RetrofitMimeTypedInput.h"
#include "RetrofitMimeTypedOutput.h"
#include "RetrofitUtils.h"
#include "java/io/ByteArrayOutputStream.h"
#include "java/io/IOException.h"
#include "java/io/InputStream.h"
#include "java/lang/IllegalArgumentException.h"
#include "java/lang/Runnable.h"
#include "java/util/List.h"

@interface RetrofitUtils ()

- (instancetype)init;

@end

inline jint RetrofitUtils_get_BUFFER_SIZE();
#define RetrofitUtils_BUFFER_SIZE 4096
J2OBJC_STATIC_FIELD_CONSTANT(RetrofitUtils, BUFFER_SIZE, jint)

__attribute__((unused)) static void RetrofitUtils_init(RetrofitUtils *self);

__attribute__((unused)) static RetrofitUtils *new_RetrofitUtils_init() NS_RETURNS_RETAINED;

__attribute__((unused)) static RetrofitUtils *create_RetrofitUtils_init();

@implementation RetrofitUtils

+ (IOSByteArray *)streamToBytesWithJavaIoInputStream:(JavaIoInputStream *)stream {
  return RetrofitUtils_streamToBytesWithJavaIoInputStream_(stream);
}

+ (RetrofitClientRequest *)readBodyToBytesIfNecessaryWithRetrofitClientRequest:(RetrofitClientRequest *)request {
  return RetrofitUtils_readBodyToBytesIfNecessaryWithRetrofitClientRequest_(request);
}

+ (RetrofitClientResponse *)readBodyToBytesIfNecessaryWithRetrofitClientResponse:(RetrofitClientResponse *)response {
  return RetrofitUtils_readBodyToBytesIfNecessaryWithRetrofitClientResponse_(response);
}

+ (RetrofitClientResponse *)replaceResponseBodyWithRetrofitClientResponse:(RetrofitClientResponse *)response
                                               withRetrofitMimeTypedInput:(id<RetrofitMimeTypedInput>)body {
  return RetrofitUtils_replaceResponseBodyWithRetrofitClientResponse_withRetrofitMimeTypedInput_(response, body);
}

+ (void)validateServiceClassWithIOSClass:(IOSClass *)service {
  RetrofitUtils_validateServiceClassWithIOSClass_(service);
}

J2OBJC_IGNORE_DESIGNATED_BEGIN
- (instancetype)init {
  RetrofitUtils_init(self);
  return self;
}
J2OBJC_IGNORE_DESIGNATED_END

+ (const J2ObjcClassInfo *)__metadata {
  static J2ObjcMethodInfo methods[] = {
    { NULL, "[B", 0x8, 0, 1, 2, -1, -1, -1 },
    { NULL, "LRetrofitClientRequest;", 0x8, 3, 4, 2, -1, -1, -1 },
    { NULL, "LRetrofitClientResponse;", 0x8, 3, 5, 2, -1, -1, -1 },
    { NULL, "LRetrofitClientResponse;", 0x8, 6, 7, -1, -1, -1, -1 },
    { NULL, "V", 0x8, 8, 9, -1, 10, -1, -1 },
    { NULL, NULL, 0x2, -1, -1, -1, -1, -1, -1 },
  };
  #pragma clang diagnostic push
  #pragma clang diagnostic ignored "-Wobjc-multiple-method-names"
  methods[0].selector = @selector(streamToBytesWithJavaIoInputStream:);
  methods[1].selector = @selector(readBodyToBytesIfNecessaryWithRetrofitClientRequest:);
  methods[2].selector = @selector(readBodyToBytesIfNecessaryWithRetrofitClientResponse:);
  methods[3].selector = @selector(replaceResponseBodyWithRetrofitClientResponse:withRetrofitMimeTypedInput:);
  methods[4].selector = @selector(validateServiceClassWithIOSClass:);
  methods[5].selector = @selector(init);
  #pragma clang diagnostic pop
  static const J2ObjcFieldInfo fields[] = {
    { "BUFFER_SIZE", "I", .constantValue.asInt = RetrofitUtils_BUFFER_SIZE, 0x1a, -1, -1, -1, -1 },
  };
  static const void *ptrTable[] = { "streamToBytes", "LJavaIoInputStream;", "LJavaIoIOException;", "readBodyToBytesIfNecessary", "LRetrofitClientRequest;", "LRetrofitClientResponse;", "replaceResponseBody", "LRetrofitClientResponse;LRetrofitMimeTypedInput;", "validateServiceClass", "LIOSClass;", "<T:Ljava/lang/Object;>(Ljava/lang/Class<TT;>;)V", "LRetrofitUtils_SynchronousExecutor;" };
  static const J2ObjcClassInfo _RetrofitUtils = { "Utils", "retrofit", ptrTable, methods, fields, 7, 0x10, 6, 1, -1, 11, -1, -1, -1 };
  return &_RetrofitUtils;
}

@end

IOSByteArray *RetrofitUtils_streamToBytesWithJavaIoInputStream_(JavaIoInputStream *stream) {
  RetrofitUtils_initialize();
  JavaIoByteArrayOutputStream *baos = create_JavaIoByteArrayOutputStream_init();
  if (stream != nil) {
    IOSByteArray *buf = [IOSByteArray arrayWithLength:RetrofitUtils_BUFFER_SIZE];
    jint r;
    while ((r = [stream readWithByteArray:buf]) != -1) {
      [baos writeWithByteArray:buf withInt:0 withInt:r];
    }
  }
  return [baos toByteArray];
}

RetrofitClientRequest *RetrofitUtils_readBodyToBytesIfNecessaryWithRetrofitClientRequest_(RetrofitClientRequest *request) {
  RetrofitUtils_initialize();
  id<RetrofitMimeTypedOutput> body = [((RetrofitClientRequest *) nil_chk(request)) getBody];
  if (body == nil || [body isKindOfClass:[RetrofitMimeTypedByteArray class]]) {
    return request;
  }
  NSString *bodyMime = [body mimeType];
  JavaIoByteArrayOutputStream *baos = create_JavaIoByteArrayOutputStream_init();
  [body writeToWithJavaIoOutputStream:baos];
  body = create_RetrofitMimeTypedByteArray_initWithNSString_withByteArray_(bodyMime, [baos toByteArray]);
  return create_RetrofitClientRequest_initWithNSString_withNSString_withJavaUtilList_withRetrofitMimeTypedOutput_([request getMethod], [request getUrl], [request getHeaders], body);
}

RetrofitClientResponse *RetrofitUtils_readBodyToBytesIfNecessaryWithRetrofitClientResponse_(RetrofitClientResponse *response) {
  RetrofitUtils_initialize();
  id<RetrofitMimeTypedInput> body = [((RetrofitClientResponse *) nil_chk(response)) getBody];
  if (body == nil || [body isKindOfClass:[RetrofitMimeTypedByteArray class]]) {
    return response;
  }
  NSString *bodyMime = [body mimeType];
  JavaIoInputStream *is = [body in];
  @try {
    IOSByteArray *bodyBytes = RetrofitUtils_streamToBytesWithJavaIoInputStream_(is);
    body = create_RetrofitMimeTypedByteArray_initWithNSString_withByteArray_(bodyMime, bodyBytes);
    return RetrofitUtils_replaceResponseBodyWithRetrofitClientResponse_withRetrofitMimeTypedInput_(response, body);
  }
  @finally {
    if (is != nil) {
      @try {
        [is close];
      }
      @catch (JavaIoIOException *ignored) {
      }
    }
  }
}

RetrofitClientResponse *RetrofitUtils_replaceResponseBodyWithRetrofitClientResponse_withRetrofitMimeTypedInput_(RetrofitClientResponse *response, id<RetrofitMimeTypedInput> body) {
  RetrofitUtils_initialize();
  return create_RetrofitClientResponse_initWithNSString_withInt_withNSString_withJavaUtilList_withRetrofitMimeTypedInput_([((RetrofitClientResponse *) nil_chk(response)) getUrl], [response getStatus], [response getReason], [response getHeaders], body);
}

void RetrofitUtils_validateServiceClassWithIOSClass_(IOSClass *service) {
  RetrofitUtils_initialize();
  if (![((IOSClass *) nil_chk(service)) isInterface]) {
    @throw create_JavaLangIllegalArgumentException_initWithNSString_(@"Only interface endpoint definitions are supported.");
  }
  if (((IOSObjectArray *) nil_chk([service getInterfaces]))->size_ > 0) {
    @throw create_JavaLangIllegalArgumentException_initWithNSString_(@"Interface definitions must not extend other interfaces.");
  }
}

void RetrofitUtils_init(RetrofitUtils *self) {
  NSObject_init(self);
}

RetrofitUtils *new_RetrofitUtils_init() {
  J2OBJC_NEW_IMPL(RetrofitUtils, init)
}

RetrofitUtils *create_RetrofitUtils_init() {
  J2OBJC_CREATE_IMPL(RetrofitUtils, init)
}

J2OBJC_CLASS_TYPE_LITERAL_SOURCE(RetrofitUtils)

@implementation RetrofitUtils_SynchronousExecutor

- (void)executeWithJavaLangRunnable:(id<JavaLangRunnable>)runnable {
  [((id<JavaLangRunnable>) nil_chk(runnable)) run];
}

J2OBJC_IGNORE_DESIGNATED_BEGIN
- (instancetype)init {
  RetrofitUtils_SynchronousExecutor_init(self);
  return self;
}
J2OBJC_IGNORE_DESIGNATED_END

+ (const J2ObjcClassInfo *)__metadata {
  static J2ObjcMethodInfo methods[] = {
    { NULL, "V", 0x1, 0, 1, -1, -1, -1, -1 },
    { NULL, NULL, 0x0, -1, -1, -1, -1, -1, -1 },
  };
  #pragma clang diagnostic push
  #pragma clang diagnostic ignored "-Wobjc-multiple-method-names"
  methods[0].selector = @selector(executeWithJavaLangRunnable:);
  methods[1].selector = @selector(init);
  #pragma clang diagnostic pop
  static const void *ptrTable[] = { "execute", "LJavaLangRunnable;", "LRetrofitUtils;" };
  static const J2ObjcClassInfo _RetrofitUtils_SynchronousExecutor = { "SynchronousExecutor", "retrofit", ptrTable, methods, NULL, 7, 0x8, 2, 0, 2, -1, -1, -1, -1 };
  return &_RetrofitUtils_SynchronousExecutor;
}

@end

void RetrofitUtils_SynchronousExecutor_init(RetrofitUtils_SynchronousExecutor *self) {
  NSObject_init(self);
}

RetrofitUtils_SynchronousExecutor *new_RetrofitUtils_SynchronousExecutor_init() {
  J2OBJC_NEW_IMPL(RetrofitUtils_SynchronousExecutor, init)
}

RetrofitUtils_SynchronousExecutor *create_RetrofitUtils_SynchronousExecutor_init() {
  J2OBJC_CREATE_IMPL(RetrofitUtils_SynchronousExecutor, init)
}

J2OBJC_CLASS_TYPE_LITERAL_SOURCE(RetrofitUtils_SynchronousExecutor)