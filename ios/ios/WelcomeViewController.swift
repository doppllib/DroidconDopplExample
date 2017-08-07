//
//  WelcomeViewController.swift
//  ios
//
//  Created by Kevin Galligan on 4/19/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

import UIKit

class WelcomeViewController: UIViewController
{
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var pageControl: UIPageControl!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let welcomePageViewController = segue.destination as? WelcomePageViewController {
            welcomePageViewController.welcomeDelegate = self
        }
    }
}

extension WelcomeViewController: WelcomePageViewControllerDelegate
{
    func welcomePageViewController(_ welcomePageViewController: WelcomePageViewController,
                                    didUpdatePageCount count: Int) {
        pageControl.numberOfPages = count
    }
    
    func welcomePageViewController(_ welcomePageViewController: WelcomePageViewController,
                                    didUpdatePageIndex index: Int) {
        pageControl.currentPage = index
        
        if (pageControl.numberOfPages - 1 == pageControl.currentPage) {
            DPRESAppManager.getInstance().getAppComponent().getPrefs().setHasSeenWelcome()
        }
    }
}
