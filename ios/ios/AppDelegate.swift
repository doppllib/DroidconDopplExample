//
//  AppDelegate.swift
//  ios
//
//  Created by Kachi Nwaobasi on 7/11/17.
//  Copyright © 2017 Kevin Galligan. All rights reserved.
//

import Foundation
import UserNotifications
import JRE
import UIKit
import FirebaseAnalytics

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
 
    func applicationDidFinishLaunching(_ application: UIApplication) {
        DopplRuntime.start()

       // let bundlePath = Bundle.main.path(forResource: "rebundle", ofType: "bundle")!
        //print("bundle path %@", bundlePath)
        
        let platformClient = DCIosPlatformClient(dcIosFirebase: self)
        let application = AndroidContentIOSContext()

        let appComponent = DDAGDaggerAppComponent.builder().appModule(with: DDAGAppModule(androidAppApplication: application))
        .databaseModule(with: DDAGDatabaseModule())
        .networkModule(with: DDAGNetworkModule())
            .build()

        DPRESAppManager.create(with: AndroidContentIOSContext(), with: platformClient, with: appComponent)

        DPRESAppManager.getInstance().seed(with: self);

        //TODO currently no notifications, so hiding this
        //registerForNotifications()
        print("Firebase Analytics test \(Analytics.appInstanceID())")
    }
    
    
    func registerForNotifications() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.badge, .sound, .alert]) {
            accepted, error in
            if let error = error as NSError? {
                if error.code == 3010 {
                    print("Push notifications are not supported in the iOS Simulator.", terminator: "")
                } else {
                    print("requestAuthorizationWithOptions failed with error: %@", error, terminator: "")
                }
            }
            UIApplication.shared.registerForRemoteNotifications()
        }
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        let type = userInfo["type"] as! String
        //check the type. networkEvent messages just open the app so dont need to the handled here.
        if type == "updateSchedule" {
            //CoTouchlabDroidconandroidSharedTasksPersistedRefreshScheduleData.callMe(with: AndroidContentIOSContext(()))
            completionHandler(.newData)
        } else {
            completionHandler(.noData)
        }
    }
}


extension AppDelegate : DPRESLoadDataSeed {
    func dataSeed() -> String! {
        if let fileUrl = Bundle.main.url(forResource: "dataseed", withExtension: "json", subdirectory: "dataseeds"), let partyData = try? Data(contentsOf: fileUrl) {
            return String(data: partyData, encoding: String.Encoding.utf8)
        }
        
        return nil
        
        
    }
}

extension AppDelegate : DCIosFirebase {
    func logFirebaseNative(with s: String!) {
        
    }
    
    func logPushNative(with s: String!) {
        
    }
    
    func logEvent(with name: String!, withNSStringArray params: IOSObjectArray!) {
        
    }
}
