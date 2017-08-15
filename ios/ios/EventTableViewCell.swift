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
    @IBOutlet weak var timeConflictLabel: UILabel!

    var networkEvent: DDATEvent!
    var conflict: jboolean!
    var eventDetailPresenter: DPRESEventDetailViewModel!
    
    func loadInfo(_ title: String, description: String, track: String, time: String, networkEvent: DDATEvent, eventDetailPresenter: DPRESEventDetailViewModel, conflict: jboolean?) {
        
        self.networkEvent = networkEvent
        self.conflict = conflict
        self.eventDetailPresenter = eventDetailPresenter
        
        titleLabel.text = title.replacingOccurrences(of: "Android", with: "[Sad Puppy]")
        timeInfoLabel.text = "Track " + track + ", " + time
        descriptionLabel.text = description.replacingOccurrences(of: "/n/n", with: "/n")

        if networkEvent.isNow() {
            timeConflictLabel.isHidden = false
            timeConflictLabel.text = "This session is happening now"
        } else if networkEvent.isPast() {
            timeConflictLabel.isHidden = false
            timeConflictLabel.text = "This session has already ended"
        } else if (conflict != nil && conflict!) {
            timeConflictLabel.isHidden = false
            timeConflictLabel.text = "This session conflicts with another session in your schedule"
        } else {
            timeConflictLabel.isHidden = true
        }
        
        titleLabel.sizeToFit()
        timeInfoLabel.sizeToFit()
        descriptionLabel.sizeToFit()
        timeConflictLabel.sizeToFit()
    }

    func formatHTMLString(_ htmlString: String) -> NSAttributedString {
        let modifiedFont = NSString(format:"<span style=\"font: -apple-system-body; font-size: 12px\">%@</span>", htmlString) as String

        let attrStr = NSAttributedString(
            string: modifiedFont,
            attributes: [NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType, NSCharacterEncodingDocumentAttribute: String.Encoding.utf8]
        )
        
        return attrStr
    }
}
