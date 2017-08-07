//
//  EventTableViewCell.swift
//  ios
//
//  Created by Sahil Ishar on 3/15/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

import UIKit

class EventTableViewCell: UITableViewCell {

    @IBOutlet weak var titleLabel : UILabel!
    @IBOutlet weak var descriptionLabel : UILabel!
    @IBOutlet weak var timeInfoLabel : UILabel!
    
    var networkEvent: DDATEvent!
    var eventDetailPresenter: DPRESEventDetailViewModel!
    
    func loadInfo(_ title: String, description: String, track: String, time: String, networkEvent: DDATEvent, eventDetailPresenter: DPRESEventDetailViewModel) {
        
        self.networkEvent = networkEvent
        self.eventDetailPresenter = eventDetailPresenter
        
        titleLabel.text = title.replacingOccurrences(of: "Android", with: "[Sad Puppy]")
        timeInfoLabel.text = "Track " + track + ", " + time
        let description = description.replacingOccurrences(of: "Android", with: "[Sad Puppy]")
        descriptionLabel.text = description
        
        titleLabel.sizeToFit()
        timeInfoLabel.sizeToFit()
        descriptionLabel.sizeToFit()
    }
   
    func formatHTMLString(_ htmlString: String) -> NSAttributedString {
        let modifiedFont = NSString(format:"<span style=\"font: -apple-system-body; font-size: 12px\">%@</span>", htmlString) as String

        let attrStr = try! NSAttributedString(
            string: modifiedFont,
            attributes: [NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType, NSCharacterEncodingDocumentAttribute: String.Encoding.utf8])
        
        return attrStr
    }
}
