//
//  SpeakerTableViewCell.swift
//  ios
//
//  Created by Sahil Ishar on 3/15/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

import UIKit
import Kingfisher

class SpeakerTableViewCell: UITableViewCell {

    @IBOutlet weak var nameLabel : UILabel!
    @IBOutlet weak var infoLabel : UILabel!
    @IBOutlet weak var speakerImage: UIImageView!
    
    func loadInfo(name: String, info: String, imgUrl: String) {
        nameLabel.text = name
        infoLabel.attributedText = formatHTMLString(info)
        
        nameLabel.sizeToFit()
        infoLabel.sizeToFit()
        
        speakerImage.kf_setImageWithURL(NSURL(string: imgUrl)!)
        speakerImage.layer.cornerRadius = 24
        speakerImage.layer.masksToBounds = true
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
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
