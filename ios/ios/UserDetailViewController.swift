//
//  UserDetailViewController.swift
//  ios
//
//  Created by Sufei Zhao on 8/21/17.
//  Copyright © 2017 Kevin Galligan. All rights reserved.
//

import UIKit

class UserDetailViewController: UIViewController {

    @IBOutlet weak var speakerImage: UIImageView!
    @IBOutlet weak var speakerName: UILabel!
    @IBOutlet weak var speakerCompany: UILabel!
    @IBOutlet weak var speakerCompany2: UILabel!
    
    @IBOutlet weak var website: UILabel!
    
    @IBOutlet weak var facebook: UILabel!
    
    @IBOutlet weak var twitter: UILabel!
    
    @IBOutlet weak var linkedin: UILabel!
    
    @IBOutlet weak var speakerBio: UILabel!
    
    
    
    
    var speaker: DDATUserAccount!
    private var searchUrl: String = "https://www.google.com/search?q="
    
    override func viewDidLoad() {
        super.viewDidLoad()

        speakerName.text = speaker.getName()
        
        if (!speaker.getCompany().isEmpty) {
            speakerCompany.text = speaker?.getCompany()
            speakerCompany2.text = speaker?.getCompany()
            let companyClick = UITapGestureRecognizer(target: self, action: Selector(("openCompany:")))
            speakerCompany2.addGestureRecognizer(companyClick)
        } else {
            speakerCompany.isHidden = true
            speakerCompany2.isHidden = true
        }
        
        if (!speaker.getWebsite().isEmpty) {
            //        website.text
        } else {
            website.isHidden = true
        }
        
        if (!speaker.getFacebook().isEmpty) {
            //        facebook.text =
        } else {
            facebook.isHidden = true
        }
        
        if (!speaker.getTwitter().isEmpty) {
            //        twitter.text =
        } else {
            twitter.isHidden = true
        }
        
        if (!speaker.getLinkedIn().isEmpty) {
            //        linkedin.text =
        } else {
            linkedin.isHidden = true
        }
        
        if (!speaker.getProfile().isEmpty) {
            //        speakerBio.text =
        } else {
            speakerBio.isHidden = true
        }
        
        
            
        speakerImage.kf.setImage(with: URL(string: (speaker?.avatarImageUrl())!))
        speakerImage.layer.cornerRadius = 24
        speakerImage.layer.masksToBounds = true
        
        speakerCompany.sizeToFit()
        speakerName.sizeToFit()
        speakerImage.sizeToFit()
    }
    
    func openCompany() {
        var search = searchUrl + (speaker?.getCompany())!
        UIApplication.shared.open(URL(string: search)!)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
