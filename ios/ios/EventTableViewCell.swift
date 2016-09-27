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
    @IBOutlet weak var liveStreamButton: UIButton!
    @IBOutlet weak var liveStreamIcon: UIImageView!
    
    var event: DCDEvent!
    var eventDetailPresenter: DCPEventDetailPresenter!
    
    func loadInfo(title: String, description: String, track: String, time: String, event: DCDEvent, eventDetailPresenter: DCPEventDetailPresenter) {
        
        self.event = event
        self.eventDetailPresenter = eventDetailPresenter
        
        titleLabel.text = title.stringByReplacingOccurrencesOfString("Android", withString: "[Sad Puppy]")
        timeInfoLabel.text = "Track " + track + ", " + time
        descriptionLabel.attributedText = formatHTMLString(description.stringByReplacingOccurrencesOfString("Android", withString: "[Sad Puppy]"))
        
        titleLabel.sizeToFit()
        timeInfoLabel.sizeToFit()
        descriptionLabel.sizeToFit()
        
        liveStreamIcon.image = liveStreamIcon.image!.imageWithRenderingMode(UIImageRenderingMode.AlwaysTemplate)
        liveStreamIcon.tintColor = UIColor(red: 0/255.0, green: 65/255.0, blue: 163/255.0, alpha: 1.0)
    }
   
    func formatHTMLString(htmlString: String) -> NSAttributedString {
        let modifiedFont = NSString(format:"<span style=\"font: -apple-system-body; font-size: 12px\">%@</span>", htmlString) as String
        
        let attrStr = try! NSAttributedString(
            data: modifiedFont.dataUsingEncoding(NSUnicodeStringEncoding, allowLossyConversion: true)!,
            options: [NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType, NSCharacterEncodingDocumentAttribute: NSUTF8StringEncoding],
            documentAttributes: nil)
        
        return attrStr
    }

}
