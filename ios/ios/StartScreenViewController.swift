//
//  StartScreenViewController.swift
//  ios
//
//  Created by Kevin Galligan on 4/19/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

import UIKit

class StartScreenViewController: UIViewController{
    override func viewDidAppear(_ animated: Bool)
    {
        let viewModel = DPRESConferenceDataViewModel.factory(withBoolean: true).create(with: DPRESConferenceDataViewModel.java_getClass()) as! DPRESConferenceDataViewModel
        let appScreen = viewModel.goToScreen()
        let segueName = appScreen.name()
        performSegue(withIdentifier: segueName!, sender: self)
    }
}
