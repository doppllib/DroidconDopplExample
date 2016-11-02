//
//  DroidconTabBarViewController.swift
//  ios
//
//  Created by Ramona Harrison on 7/20/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

import UIKit

class DroidconTabBarViewController: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Sets the background color of the selected UITabBarItem
        UITabBar.appearance().selectionIndicatorImage = UIImage().makeImageWithColorAndSize(UIColor(red:(0/255.0), green:(90/255.0), blue:(224/255.0), alpha: 1.0), size:
            CGSize(width: tabBar.frame.width/CGFloat((tabBar.items?.count)!), height: tabBar.frame.height))
        
        // Sets the tab bar text appearance
        UITabBarItem.appearance().setTitleTextAttributes([NSForegroundColorAttributeName: UIColor.white], for:UIControlState())
        UITabBarItem.appearance().setTitleTextAttributes([NSForegroundColorAttributeName: UIColor(red:(93/255.0), green:(253/255.0), blue:(173/255.0), alpha: 1.0)], for:.selected)
        
        // Render the icons in their original white
        for item in self.tabBar.items as [UITabBarItem]! {
            if let image = item.image {
                item.image = image.withRenderingMode(.alwaysOriginal)
            }
        }

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

extension UIImage {
    func makeImageWithColorAndSize(_ color: UIColor, size: CGSize) -> UIImage {
        UIGraphicsBeginImageContextWithOptions(size, false, 0)
        color.setFill()
        UIRectFill(CGRect(x: 0, y: 0, width: size.width, height: size.height))
        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return image!
    }
}
