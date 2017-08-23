//
//  UserDetailViewController.swift
//  ios
//
//  Created by Sufei Zhao on 8/21/17.
//  Copyright © 2017 Kevin Galligan. All rights reserved.
//

import UIKit

class UserDetailViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var speakerImage: UIImageView!
    @IBOutlet weak var speakerName: UILabel!
    @IBOutlet weak var speakerCompany: UILabel!
    @IBOutlet weak var tableView: UITableView!
    
    private let GOOGLE_URL: String = "http://www.google.com/search?q="
    private let TWITTER_URL: String = "http://www.twitter.com/"
    private let GPLUS_URL: String = "http://www.plus.google.com/s/"
    private let LINKEDIN_URL: String = "http://www.linkedin.com/in/"
    private let FACEBOOK_URL: String = "http://www.facebook.com/"
    
    var speaker: DDATUserAccount!
    var speakerInfos: [SpeakerInfo] = []
    
    // MARK: Lifecycle events
    override func viewDidLoad() {
        super.viewDidLoad()
        checkForSpeakerInfo()
        
        if (!speaker.getName().isEmpty) {
            speakerName.text = speaker.getName()
            speakerName.isHidden = false
            speakerName.sizeToFit()
        }
        if (!speaker.getCompany().isEmpty) {
            speakerCompany.text = speaker?.getCompany()
            speakerCompany.isHidden = false
            speakerCompany.sizeToFit()
        }
                
        speakerImage.kf.setImage(with: URL(string: (speaker?.avatarImageUrl())!))
        speakerImage.layer.cornerRadius = 24
        speakerImage.layer.masksToBounds = true
        speakerImage.sizeToFit()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        tableView.delegate = self
        tableView.dataSource = self
        let nib = UINib(nibName: "UserTableViewCell", bundle: nil)
        tableView.register(nib, forCellReuseIdentifier: "userCell")
    }
    
    func checkForSpeakerInfo() {
        let speakerCompany = speaker.getCompany()
        if speakerCompany.isNotNilOrEmpty() {
            speakerInfos.append(SpeakerInfo(type: .company, info: speakerCompany!))
        }
        
        let speakerWebsite = speaker.getWebsite()
        if speakerWebsite.isNotNilOrEmpty() {
            speakerInfos.append(SpeakerInfo(type: .website, info: speakerWebsite!))
        }
        
        let speakerFb = speaker.getFacebook()
        if speakerFb.isNotNilOrEmpty() {
            speakerInfos.append(SpeakerInfo(type: .facebook, info: speakerFb!))
        }
        
        let speakerTwitter = speaker.getTwitter()
        if speakerTwitter.isNotNilOrEmpty() {
            speakerInfos.append(SpeakerInfo(type: .twitter, info: speakerTwitter!))
        }
        
        let speakerLinkedin = speaker.getLinkedIn()
        if speakerLinkedin.isNotNilOrEmpty() {
            speakerInfos.append(SpeakerInfo(type: .linkedin, info: speakerLinkedin!))
        }
        
        let speakerGPlus = speaker.getgPlus()
        if speakerGPlus.isNotNilOrEmpty() {
            speakerInfos.append(SpeakerInfo(type: .gplus, info: speakerGPlus!))
        }
        
        let speakerProfile = speaker.getProfile()
        if speakerProfile.isNotNilOrEmpty() {
            speakerInfos.append(SpeakerInfo(type: .profile, info: speakerProfile!))
        }
    }
    
    func openWebsite(_ selected: SpeakerInfo) {
        var url = ""
        
        if selected.info.contains(".com") {
            url = selected.info.formatUrl()
        } else {
            switch (selected.type) {
            case .company:
                url = GOOGLE_URL + selected.info
                break
            case .website:
                url = selected.info.formatUrl()
                break
            case .facebook:
                url = FACEBOOK_URL + selected.info
                break
            case .twitter:
                url = TWITTER_URL + selected.info
                break
            case .linkedin:
                url = LINKEDIN_URL + selected.info
                break
            case .gplus:
                url = GPLUS_URL + selected.info.replacingOccurrences(of: "+", with: "")
                break
            default:
                break
            }
        }
        
        if (!url.isEmpty) {
            UIApplication.shared.open(URL(string: url)!)
        }
    }
    
    // MARK: TableView
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return speakerInfos.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: UserTableViewCell = tableView.dequeueReusableCell(withIdentifier: "userCell") as! UserTableViewCell
        
        let speakerInfo = speakerInfos[indexPath.row]
        cell.loadInfo(speakerInfo)
        cell.selectionStyle = UITableViewCellSelectionStyle.none
        return cell
    }
    
    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: false)
        
        let selected = speakerInfos[indexPath.row]
        openWebsite(selected)
        
    }
}
