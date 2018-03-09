require 'rake'
FileList = Rake::FileList

Pod::Spec.new do |s|

  s.name             = 'testdoppllib'
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

    s.source_files = FileList["build/testdoppllib.h"].include("build/dopplBuild/dependencies/out/main/**/*.{h,m,cpp,properites,txt}").include("build/dopplBuild/source/out/main/**/*.{h,m,cpp,properites,txt}").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_lib_androidbase_0_9_0_0/src/**/*.{h,m,cpp,properites,txt}").include("build/dopplBuild/dependencies/exploded/doppl/co_doppl_com_squareup_okio_okio_1_13_0_0/src/**/*.{h,m,cpp,properites,txt}").include("build/dopplBuild/dependencies/out/test/**/*.{h,m,cpp,properites,txt}").include("build/dopplBuild/source/out/test/**/*.{h,m,cpp,properites,txt}").include("build/generated/source/apt/debug/**/*.java").include("build/generated/source/buildConfig/debug/**/*.java").include("build/generated/source/r/debug/**/*.java").include("src/debug/java/**/*.java").include("src/main/java/**/*.java").include("src/main/kotlin/**/*.java").include("build/generated/source/apt/test/debug/**/*.java").include("src/test/java/**/*.java").include("src/testDebug/java/**/*.java").to_ary

    s.public_header_files = FileList["build/testdoppllib.h"].include("build/dopplBuild/dependencies/out/main/**/*.h").include("build/dopplBuild/source/out/main/**/*.h").include("build/dopplBuild/dependencies/out/test/**/*.h").include("build/dopplBuild/source/out/test/**/*.h").exclude(/cpphelp/).exclude(/jni/).to_ary

    s.requires_arc = false
    s.libraries = 'z', 'sqlite3', 'iconv', 'jre_emul'
    s.frameworks = 'UIKit'

    s.pod_target_xcconfig = {
     'HEADER_SEARCH_PATHS' => '/Users/kgalligan/.doppl/j2objc/2.0.6a/include /Users/kgalligan/devel-doppl-supported/DroidconDopplExample/app/build/dopplBuild/dependencies/exploded/doppl/co_doppl_lib_androidbase_0_9_0_0/src /Users/kgalligan/devel-doppl-supported/DroidconDopplExample/app/build/dopplBuild/dependencies/exploded/doppl/co_doppl_com_squareup_okio_okio_1_13_0_0/src','LIBRARY_SEARCH_PATHS' => '/Users/kgalligan/.doppl/j2objc/2.0.6a/lib',
     'OTHER_LDFLAGS' => '-ObjC',
'CLANG_WARN_DOCUMENTATION_COMMENTS' => 'NO',
'GCC_WARN_64_TO_32_BIT_CONVERSION' => 'NO'
    }
    
    s.user_target_xcconfig = {
     'HEADER_SEARCH_PATHS' => '/Users/kgalligan/.doppl/j2objc/2.0.6a/frameworks/JRE.framework/Headers'
    }
    
    
    
end