//
//  NoteTableViewCell.swift
//  ios
//
//  Created by Kachi Nwaobasi on 7/11/17.
//  Copyright © 2017 Kevin Galligan. All rights reserved.
//

import Foundation

class NoteTableViewCell : UITableViewCell {
    
    @IBOutlet weak var noteTitleLabel: UILabel!
    @IBOutlet weak var noteLabel: UILabel!
    @IBOutlet weak var noteImageView: UIImageView!
    
    override func awakeFromNib() {
        // Initialization code
        imageView?.autoresizingMask = UIViewAutoresizing(rawValue: 0)
        imageView?.clipsToBounds = true
    }
}
