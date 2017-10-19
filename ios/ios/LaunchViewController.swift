//
//  StartScreenViewController.swift
//  ios
//
//  Created by Kevin Galligan on 4/19/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

import UIKit
import doppllib

class LaunchViewController: UIViewController{
    override func viewDidAppear(_ animated: Bool)
    {
        let viewModel = DVMConferenceDataViewModel.forIosWithBoolean(true)
        let appScreen = viewModel.goToScreen()
        let segueName = appScreen.name()
        performSegue(withIdentifier: segueName!, sender: self)
    }
}
