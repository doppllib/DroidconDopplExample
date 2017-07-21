//
//  UIViewController.swift
//  ios
//
//  Created by Kachi Nwaobasi on 7/11/17.
//  Copyright © 2017 Kevin Galligan. All rights reserved.
//

import Foundation

extension UIViewController {

    
    static func currentViewController(vcType: AnyClass) -> UIViewController {
        let viewController = UIApplication.shared.keyWindow!.rootViewController!
        return UIViewController.findBest(viewController: viewController, forType: vcType)
    }
    
    private static func findBest(viewController: UIViewController, forType type: AnyClass) -> UIViewController {
        
        if viewController.isKind(of: type) {
            return viewController
        }
        if let presentedViewController = viewController.presentedViewController {
            // Return presented view controller
            return UIViewController.findBest(viewController: presentedViewController, forType: type)
        } else if viewController.isKind(of: UISplitViewController.self) {
            let splitViewController = viewController as! UISplitViewController
            if splitViewController.viewControllers.count > 0 {
                return UIViewController.findBest(viewController:splitViewController.viewControllers.last!, forType:type)
            } else {
                return viewController
            }
        } else if viewController.isKind(of: UINavigationController.self) {
            
            //Return top view
            let navController = viewController as! UINavigationController
            if navController.viewControllers.count > 0 {
                return UIViewController.findBest(viewController:navController.topViewController!, forType: type)
            } else {
                return viewController
            }
        } else if viewController.isKind(of: UITabBarController.self) {
            
            //Return visible view
            let tabController = viewController as! UITabBarController
            if let viewControllers = tabController.viewControllers, viewControllers.count > 0 {
                return UIViewController.findBest(viewController:tabController.selectedViewController!, forType: type)
            } else {
                return viewController
            }
        } else {
            // Unknown view controller type, return last child view controller
            return viewController
        }
        
    }
    
}
