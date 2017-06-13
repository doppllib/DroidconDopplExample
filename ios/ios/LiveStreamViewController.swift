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

class LiveStreamViewController: UIViewController {
    
    var titleString: String?
    var streamUrl: String?
    var coverUrl: String?
    
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    override func viewDidLoad() {
        self.edgesForExtendedLayout =  UIRectEdge()
        self.view.backgroundColor = UIColor.black
        self.automaticallyAdjustsScrollViewInsets = false
        
        super.viewDidLoad()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }
}
