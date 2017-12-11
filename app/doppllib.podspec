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

    s.source_files = FileList["build/doppllib.h"].include("build/dopplBuild/dependencies/out/main/**/*.{h,m,cpp,properites,txt}").include("build/dopplBuild/source/out/main/**/*.{h,m,cpp,properites,txt}").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_lib_androidbase_0_8_8_0/src/**/*.{h,m,cpp,properites,txt}").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_com_squareup_okio_okio_1_13_0_0/src/**/*.{h,m,cpp,properites,txt}").include("build/generated/source/apt/debug/**/*.java").include("build/generated/source/buildConfig/debug/**/*.java").include("build/generated/source/r/debug/**/*.java").include("src/debug/java/**/*.java").include("src/main/java/**/*.java").include("src/main/kotlin/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_android_arch_persistence_room_rxjava2_1_0_0_2_rc1/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_android_arch_persistence_room_runtime_1_0_0_2_rc1/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_android_arch_lifecycle_extensions_1_0_0_2_rc1/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_android_arch_lifecycle_runtime_1_0_0_2_rc1/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_com_squareup_retrofit2_urlsession_converter_gson_2_3_0_10/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_com_squareup_retrofit2_urlsession_adapter_rxjava2_2_3_0_10/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_com_squareup_retrofit2_urlsession_retrofit_2_3_0_10/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_io_reactivex_rxjava2_rxandroid_2_0_1_7/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_io_reactivex_rxjava2_rxjava_2_1_5_2/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_android_arch_persistence_room_common_1_0_0_2_rc1/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_android_arch_persistence_db_framework_1_0_0_2_rc1/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_android_arch_persistence_db_1_0_0_2_rc1/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_android_arch_core_runtime_1_0_0_2_rc1/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_android_arch_core_common_1_0_0_2_rc1/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_android_arch_lifecycle_common_1_0_0_2_rc1/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_lib_androidbase_0_8_8_0/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_com_google_dagger_dagger_2_5_7/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_com_squareup_okhttp3_okhttp_3_4_2_6/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_com_google_code_gson_gson_2_6_2_7/java/**/*.java").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_com_squareup_okio_okio_1_13_0_0/java/**/*.java").to_ary

    s.public_header_files = FileList["build/doppllib.h"].include("build/dopplBuild/dependencies/out/main/**/*.h").include("build/dopplBuild/source/out/main/**/*.h").exclude(/cpphelp/).exclude(/jni/).to_ary

    s.requires_arc = false
    s.libraries = 'z', 'sqlite3', 'iconv', 'javax_inject', 'jre_emul', 'jsr305'
    s.frameworks = 'UIKit'

    s.pod_target_xcconfig = {
     'HEADER_SEARCH_PATHS' => '/Users/kgalligan/devel/j2objc-cleanup/dist/include','LIBRARY_SEARCH_PATHS' => '/Users/kgalligan/devel/j2objc-cleanup/dist/lib',
     'OTHER_LDFLAGS' => '-ObjC'
    }
    
    s.user_target_xcconfig = {
     'HEADER_SEARCH_PATHS' => '/Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/JRE.framework/Headers /Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/JavaxInject.framework/Headers /Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/JSR305.framework/Headers /Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/JUnit.framework/Headers /Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/Mockito.framework/Headers /Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/Xalan.framework/Headers /Users/kgalligan/devel/j2objc-cleanup/dist/frameworks/Guava.framework/Headers'
    }
    
    
    
end