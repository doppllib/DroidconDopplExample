//
//  WelcomePageViewController.swift
//  ios
//
//  Created by Ramona Harrison on 8/9/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

import UIKit

class WelcomePageViewController: UIPageViewController {
    
    weak var welcomeDelegate: WelcomePageViewControllerDelegate?

    override func viewDidLoad() {
        super.viewDidLoad()

        dataSource = self
        delegate = self
        
        if let firstViewController = orderedViewControllers.first {
            setViewControllers([firstViewController], direction: .Forward, animated: true, completion: nil)
        }
        
        welcomeDelegate?.welcomePageViewController(self,
                                                     didUpdatePageCount: orderedViewControllers.count)
    }
    
    private(set) lazy var orderedViewControllers: [UIViewController] = {
        return [self.newWelcomeViewController("1"),
                self.newWelcomeViewController("2"),
                self.newWelcomeViewController("3"),
                self.newWelcomeViewController("4")]
    }()
    
    private func newWelcomeViewController(index: String) -> UIViewController {
        return UIStoryboard(name: "Main", bundle: nil) .
            instantiateViewControllerWithIdentifier("Welcome\(index)")
    }
}

// MARK: UIPageViewControllerDataSource

extension WelcomePageViewController: UIPageViewControllerDataSource {
    
    func pageViewController(pageViewController: UIPageViewController,
                            viewControllerBeforeViewController viewController: UIViewController) -> UIViewController? {
        guard let viewControllerIndex = orderedViewControllers.indexOf(viewController) else {
            return nil
        }
        
        let previousIndex = viewControllerIndex - 1
        
        guard previousIndex >= 0 else {
            return nil
        }
        
        guard orderedViewControllers.count > previousIndex else {
            return nil
        }
        
        return orderedViewControllers[previousIndex]

    }
    
    func pageViewController(pageViewController: UIPageViewController,
                            viewControllerAfterViewController viewController: UIViewController) -> UIViewController? {
        guard let viewControllerIndex = orderedViewControllers.indexOf(viewController) else {
            return nil
        }
        
        let nextIndex = viewControllerIndex + 1
        let orderedViewControllersCount = orderedViewControllers.count
        
        guard orderedViewControllersCount != nextIndex else {
            return nil
        }
        
        guard orderedViewControllersCount > nextIndex else {
            return nil
        }
        
        return orderedViewControllers[nextIndex]
    }
    
}

// MARK: UIPageViewControllerDelegate

extension WelcomePageViewController: UIPageViewControllerDelegate {
    
    func pageViewController(pageViewController: UIPageViewController,
                            didFinishAnimating finished: Bool,
                                               previousViewControllers: [UIViewController],
                                               transitionCompleted completed: Bool) {
        if let firstViewController = viewControllers?.first,
            let index = orderedViewControllers.indexOf(firstViewController) {
            welcomeDelegate?.welcomePageViewController(self,
                                                         didUpdatePageIndex: index)
        }
    }
    
}

protocol WelcomePageViewControllerDelegate: class {
    
    func welcomePageViewController(welcomePageViewController: WelcomePageViewController,
                                    didUpdatePageCount count: Int)
    
    func welcomePageViewController(welcomePageViewController: WelcomePageViewController,
                                    didUpdatePageIndex index: Int)
}
