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
            CGSizeMake(tabBar.frame.width/CGFloat((tabBar.items?.count)!), tabBar.frame.height))
        
        // Sets the tab bar text appearance
        UITabBarItem.appearance().setTitleTextAttributes([NSForegroundColorAttributeName: UIColor.whiteColor()], forState:.Normal)
        UITabBarItem.appearance().setTitleTextAttributes([NSForegroundColorAttributeName: UIColor(red:(93/255.0), green:(253/255.0), blue:(173/255.0), alpha: 1.0)], forState:.Selected)
        
        // Render the icons in their original white
        for item in self.tabBar.items as [UITabBarItem]! {
            if let image = item.image {
                item.image = image.imageWithRenderingMode(.AlwaysOriginal)
            }
        }

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

extension UIImage {
    func makeImageWithColorAndSize(color: UIColor, size: CGSize) -> UIImage {
        UIGraphicsBeginImageContextWithOptions(size, false, 0)
        color.setFill()
        UIRectFill(CGRectMake(0, 0, size.width, size.height))
        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return image
    }
}
