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
import FirebaseMessaging
import doppllib

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey : Any]? = nil) -> Bool {
        DopplRuntime.start()
        
        let platformClient = DCIosPlatformClient(dcIosFirebase: self)
        let application = AndroidContentIOSContext()
        
        let appComponent = DDAGDaggerAppComponent.builder().appModule(with: DDAGAppModule(androidAppApplication: application))
            .networkModule(with: DDAGNetworkModule())
            .build()
        
        DVMAppManager.create(with: AndroidContentIOSContext(), with: platformClient, with: appComponent)
        DVMAppManager.getInstance().seed(with: self);
        
        FirebaseApp.configure()
        Messaging.messaging().delegate = self
        Messaging.messaging().shouldEstablishDirectChannel = true
        registerForNotifications()
        print("Firebase Analytics test \(Analytics.appInstanceID())")
        return true
    }
    
    func registerForNotifications() {
        UNUserNotificationCenter.current().delegate = self
        
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(options: authOptions, completionHandler: { accepted, error in
            if let error = error as NSError? {
                if accepted {
                    print("Push notification permission granted")
                } else if error.code == 3010 {
                    print("Push notifications are not supported in the iOS Simulator.", terminator: "")
                } else {
                    print("RequestAuthorizationWithOptions failed with error: %@", error, terminator: "")
                }
            }
            UIApplication.shared.registerForRemoteNotifications()
        })
    }
    
    // only called if "content_available" is set to true
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        Messaging.messaging().appDidReceiveMessage(userInfo)
    }
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("Unable to register for remote notifications: \(error.localizedDescription)")
    }

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        print("APNs token retrieved: \(deviceToken)")
        Messaging.messaging().apnsToken = deviceToken
        Messaging.messaging().subscribe(toTopic: "/topics/all_2017")
        Messaging.messaging().subscribe(toTopic: "/topics/ios_2017")
    }
}

extension AppDelegate : UNUserNotificationCenterDelegate {
    // called when notification is sent
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        let data = notification.request.content.userInfo
        Messaging.messaging().appDidReceiveMessage(data)
        completionHandler([.alert, .badge, .sound])
    }
    
    // called when notification is clicked on
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        let identifier = response.notification.request.identifier
        let data = response.notification.request.content.userInfo
        Messaging.messaging().appDidReceiveMessage(data)
        
        if identifier == "versionNotification" {
            UIApplication.shared.open(NSURL(string: "itms-apps://itunes.apple.com/us/app/droidcon-nyc-2016/id1155197664?mt=8")! as URL)
        } else if (data["type"] as? String == "event") || identifier == "eventNotification" {
            if let controller = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "EventDetail") as? EventDetailViewController {
                if let window = self.window, let rootViewController = window.rootViewController {
                    var currentController = rootViewController
                    while let presentedController = currentController.presentedViewController {
                        currentController = presentedController
                    }
                    let id = data["eventId"] as! String
                    controller.eventId = jlong.init(id)
                    currentController.present(controller, animated: true, completion: nil)
                }
            }
        }
        
        completionHandler()
    }
}

extension AppDelegate : MessagingDelegate {
    func messaging(_ messaging: Messaging, didRefreshRegistrationToken fcmToken: String) {
        print("Firebase registration token: \(fcmToken)")
    }
   
    // Receive data messages on iOS 10+ directly from FCM (bypassing APNs) when the app is in the foreground.
    func messaging(_ messaging: Messaging, didReceive remoteMessage: MessagingRemoteMessage) {
        print("Received direct data message: \(remoteMessage.appData)")
        if let type = remoteMessage.appData["type"] as? String {
            if (type == "updateSchedule") {
                DVMAppManager.getInstance().getAppComponent().refreshScheduleInteractor().refreshFromServer()
            } else if (type == "version") {
                let newVersion = remoteMessage.appData["versionCode"] as! String
                let currentVersion = Bundle.main.infoDictionary!["CFBundleShortVersionString"] as! String
                if currentVersion < newVersion {
                    sendVersionNotification()
                }
            }
        }
    }
    
    func sendVersionNotification() {
        let content = UNMutableNotificationContent()
        content.title = Bundle.main.infoDictionary![kCFBundleNameKey as String] as! String
        content.body = "A new version of the app is now available!"
        content.sound = UNNotificationSound.default()
        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 2, repeats: false)
        let notificationIdentifier = "versionNotification"
        sendNotification(content: content, trigger: trigger, notificationIdentifier: notificationIdentifier)
    }
    
    func sendAlarmNotification(title: String, date: Date, eventId: String) {
        let content = UNMutableNotificationContent()
        content.title = Bundle.main.infoDictionary![kCFBundleNameKey as String] as! String
        content.body = "The \(title) session is starting soon!"
        content.sound = UNNotificationSound.default()
        content.userInfo = ["eventId": eventId]
        let triggerDate = Calendar.current.dateComponents([.year,.month,.day,.hour,.minute,.second,], from: date)
        let trigger = UNCalendarNotificationTrigger(dateMatching: triggerDate, repeats: false)
        let notificationIdentifier = "eventNotification"
        sendNotification(content: content, trigger: trigger, notificationIdentifier: notificationIdentifier)
    }
    
    func sendNotification(content: UNMutableNotificationContent, trigger: UNNotificationTrigger, notificationIdentifier: String) {
        let request = UNNotificationRequest(identifier: notificationIdentifier, content: content, trigger: trigger)
        UNUserNotificationCenter.current().add(request) { (error) in
            print("Notification error: \(error as Any)")
        }
    }
}

extension AppDelegate : DVMLoadDataSeed {
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
