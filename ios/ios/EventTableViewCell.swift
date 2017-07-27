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
    
    var networkEvent: DCDEvent!
    var eventDetailPresenter: DCPEventDetailPresenter!
    
    func loadInfo(_ title: String, description: String, track: String, time: String, networkEvent: DCDEvent, eventDetailPresenter: DCPEventDetailPresenter) {
        
        self.networkEvent = networkEvent
        self.eventDetailPresenter = eventDetailPresenter
        
        titleLabel.text = title.replacingOccurrences(of: "Android", with: "[Sad Puppy]")
        timeInfoLabel.text = "Track " + track + ", " + time
        let asdf = description.replacingOccurrences(of: "Android", with: "[Sad Puppy]")
//        let attribasdf = formatHTMLString(asdf)
        descriptionLabel.text = asdf
        
        titleLabel.sizeToFit()
        timeInfoLabel.sizeToFit()
        descriptionLabel.sizeToFit()
        
        liveStreamIcon.image = liveStreamIcon.image!.withRenderingMode(UIImageRenderingMode.alwaysTemplate)
        liveStreamIcon.tintColor = UIColor(red: 0/255.0, green: 65/255.0, blue: 163/255.0, alpha: 1.0)
    }
   
    func formatHTMLString(_ htmlString: String) -> NSAttributedString {
        let modifiedFont = NSString(format:"<span style=\"font: -apple-system-body; font-size: 12px\">%@</span>", htmlString) as String

        let attrStr = try! NSAttributedString(
            string: modifiedFont,
            attributes: [NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType, NSCharacterEncodingDocumentAttribute: String.Encoding.utf8])
        
        return attrStr
    }
    
    /*
 let modifiedFont = NSString(format:"<span style=\"font: -apple-system-body; font-size: 12px\">%@</span>", htmlString) as String
 
 let adata = modifiedFont.data(using: String.Encoding.unicode, allowLossyConversion: true)!
 let aoptions = [NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType, NSCharacterEncodingDocumentAttribute: String.Encoding.utf8] as [String : Any]
 
 let attrStr = try! NSAttributedString(
 data: adata,
 options: aoptions,
 documentAttributes: nil)
 
 return attrStr
 */

}
