//
//  NoteTableViewCell.m
//  ios
//
//  Created by Sahil Ishar on 3/11/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

#import "NoteTableViewCell.h"

@implementation NoteTableViewCell

- (void)awakeFromNib {
    // Initialization code
    
    [self.imageView setAutoresizingMask:UIViewAutoresizingNone];
    [self.imageView setClipsToBounds:YES];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
