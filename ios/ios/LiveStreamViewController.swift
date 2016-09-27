//
//  LivestreamViewController.swift
//  ios
//
//  Created by Ramona Harrison on 8/4/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

import UIKit

import Foundation
import UIKit
import MediaPlayer

class LiveStreamViewController: UIViewController, JWPlayerDelegate {
    
    private var player:JWPlayerController!
    var titleString: String?
    var streamUrl: String?
    var coverUrl: String?
    
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: NSBundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    override func viewDidLoad() {
        self.edgesForExtendedLayout =  UIRectEdge.None
        self.view.backgroundColor = UIColor.blackColor()
        self.automaticallyAdjustsScrollViewInsets = false
        
        super.viewDidLoad()
    }
    
    override func viewDidAppear(animated: Bool) {
        self.createPlayer()
        self.view.addSubview(player.view)
        self.setupCallbacks()
        super.viewDidAppear(animated)
    }
    
    func createPlayer() {
        let config: JWConfig = JWConfig(contentURL: streamUrl)
        if coverUrl != nil {
            config.image = coverUrl
        }
        config.title = titleString
        config.controls = true  //default
        config.`repeat` = false   //default
        config.premiumSkin = JWPremiumSkinRoundster
        
        self.player = JWPlayerController(config: config)
        self.player.delegate = self
        
        var frame: CGRect = self.view.bounds
        frame.size.height /= 2
        frame.size.height -= 44
        frame.origin.y = 150
        
        self.player.view.frame = frame
        self.player.view.autoresizingMask = [UIViewAutoresizing.FlexibleBottomMargin, UIViewAutoresizing.FlexibleHeight, UIViewAutoresizing.FlexibleLeftMargin, UIViewAutoresizing.FlexibleRightMargin, UIViewAutoresizing.FlexibleTopMargin, UIViewAutoresizing.FlexibleWidth];
        
        self.player.forceFullScreenOnLandscape = true
        self.player.forceLandscapeOnFullScreen = true
    }
    
    func setupCallbacks() {
        setupNotifications()
    }
    
    func setupNotifications() {
        let notifications: Array = [JWPlayerStateChangedNotification, JWMetaDataAvailableNotification, JWAdActivityNotification, JWErrorNotification, JWCaptionsNotification, JWVideoQualityNotification, JWPlaybackPositionChangedNotification, JWFullScreenStateChangedNotification, JWAdClickNotification]
        
        let center:  NSNotificationCenter = NSNotificationCenter.defaultCenter()
        
        for(_, notification) in notifications.enumerate() {
            center.addObserver(self, selector: #selector(LiveStreamViewController.handleNotification(_:)), name: notification, object: nil)
        }
    }
    
    func handleNotification(notification: NSNotification) {
        var userInfo: Dictionary = notification.userInfo!
        let callback: String = userInfo["event"] as! String
        
        if(callback == "onTime") {return}
    }
    
    func controlCenter() {
        MPNowPlayingInfoCenter.defaultCenter().nowPlayingInfo = [MPMediaItemPropertyArtist : "Artist",  MPMediaItemPropertyTitle : "Title"]
    }
}