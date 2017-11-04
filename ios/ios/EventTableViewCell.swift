//
//  EventTableViewCell.swift
//  ios
//
//  Created by Sahil Ishar on 3/15/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

import UIKit
import doppllib

class EventTableViewCell: UITableViewCell {

    @IBOutlet weak var titleLabel : UILabel!
    @IBOutlet weak var descriptionLabel : UILabel!
    @IBOutlet weak var timeInfoLabel : UILabel!
    @IBOutlet weak var timeConflictLabel: UILabel!

    var networkEvent: DDATEvent!
    var conflict: jboolean!
    var eventDetailPresenter: DVMEventDetailViewModel!
    
    func loadInfo(_ networkEvent: DDATEvent, eventDetailPresenter: DVMEventDetailViewModel, conflict: jboolean?) {
        
        self.networkEvent = networkEvent
        self.conflict = conflict
        self.eventDetailPresenter = eventDetailPresenter
        
        titleLabel.text = networkEvent.getName().replacingOccurrences(of: "Android", with: "Lulu")
        let time = TimeUtils.getEventTime(startTime: networkEvent.getStartFormatted()! as NSString, andEnd: networkEvent.getEndFormatted()! as NSString)
        timeInfoLabel.text = "Track " + networkEvent.getVenue().getName() + ", " + time
        descriptionLabel.text = networkEvent.getDescription().replacingOccurrences(of: "/n/n", with: "/n")

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
