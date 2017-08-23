//
//  UserTableViewCell.swift
//  ios
//
//  Created by Sufei Zhao on 8/22/17.
//  Copyright © 2017 Kevin Galligan. All rights reserved.
//

import UIKit

class UserTableViewCell: UITableViewCell {

    @IBOutlet weak var imageIcon: UIImageView!
    @IBOutlet weak var infoLabel: UILabel!
    
    func loadInfo(_ speakerInfo: SpeakerInfo) {
        infoLabel.text = speakerInfo.info
        
        switch (speakerInfo.type) {
            case .company:
                imageIcon.image = UIImage(named:"ic_business_white")
                break
            case .website:
                imageIcon.image = UIImage(named:"ic_public_white")
                break
            case .facebook:
                imageIcon.image = UIImage(named:"ic_facebook")
                break
            case .twitter:
                infoLabel.text = "@\(speakerInfo.info)"
                imageIcon.image = UIImage(named:"ic_twitter")
                break
            case .linkedin:
                imageIcon.image = UIImage(named:"ic_linked_in")
                break
            case .gplus:
                imageIcon.image = UIImage(named:"ic_gplus")
                imageIcon.backgroundColor = nil
                break
            case .profile:
                imageIcon.image = UIImage(named:"ic_info_outline_white")
                break
        }
        
        infoLabel.sizeToFit()
        imageIcon.sizeToFit()
    }
    

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
}

struct SpeakerInfo {
    var type: InfoType
    var info: String
}

enum InfoType {
    case company, website, facebook, twitter, linkedin, gplus, profile
}
