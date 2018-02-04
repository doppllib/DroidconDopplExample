require 'rake'
FileList = Rake::FileList

Pod::Spec.new do |s|

  s.name             = 'doppllib'
    s.version          = '0.1.0'
    s.summary          = 'Doppl code framework'

    s.description      = <<-DESC
  TODO: Add long description of the pod here.
                         DESC

    s.homepage         = 'http://doppl.co/'
    s.license          = { :type => 'Apache 2.0' }
    s.authors           = { 'Filler Person' => 'filler@example.com' }
    s.source           = { :git => 'https://github.com/doppllib/doppl-gradle.git'}

    s.ios.deployment_target = '8.0'

    s.source_files = FileList["build/doppllib.h"].include("build/dopplBuild/dependencies/out/main/**/*.{h,m,cpp,properites,txt}").include("build/dopplBuild/source/out/main/**/*.{h,m,cpp,properites,txt}").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_lib_androidbase_0_8_10_0/src/**/*.{h,m,cpp,properites,txt}").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_com_squareup_okio_okio_1_13_0_0/src/**/*.{h,m,cpp,properites,txt}").include("build/generated/source/apt/debug/**/*.java").include("build/generated/source/buildConfig/debug/**/*.java").include("build/generated/source/r/debug/**/*.java").include("src/debug/java/**/*.java").include("src/main/java/**/*.java").include("src/main/kotlin/**/*.java").to_ary

    s.public_header_files = FileList["build/doppllib.h"].include("build/dopplBuild/dependencies/out/main/**/*.h").include("build/dopplBuild/source/out/main/**/*.h").exclude(/cpphelp/).exclude(/jni/).to_ary

    s.requires_arc = false
    s.libraries = 'z', 'sqlite3', 'iconv', 'javax_inject', 'jre_emul', 'jsr305'
    s.frameworks = 'UIKit'

    s.pod_target_xcconfig = {
     'HEADER_SEARCH_PATHS' => '/Users/kgalligan/devel/j2objc-cleanup/dist/include','LIBRARY_SEARCH_PATHS' => '/Users/kgalligan/devel/j2objc-cleanup/dist/lib',
     'OTHER_LDFLAGS' => '-ObjC',
'CLANG_WARN_DOCUMENTATION_COMMENTS' => 'NO',
'GCC_WARN_64_TO_32_BIT_CONVERSION' => 'NO'
    }
    
    s.user_target_xcconfig = {
     'HEADER_SEARCH_PATHS' => '/Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/JRE.framework/Headers /Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/JavaxInject.framework/Headers /Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/JSR305.framework/Headers /Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/JUnit.framework/Headers /Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/Mockito.framework/Headers /Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/Xalan.framework/Headers /Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/Guava.framework/Headers'
    }
    
    
    
end