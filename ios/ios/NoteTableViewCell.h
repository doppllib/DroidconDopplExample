//
//  NoteTableViewCell.h
//  ios
//
//  Created by Sahil Ishar on 3/11/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NoteTableViewCell : UITableViewCell

@property (nonatomic, weak) IBOutlet UILabel *noteTitleLabel;
@property (nonatomic, weak) IBOutlet UILabel *noteLabel;
@property (nonatomic, weak) IBOutlet UIImageView *noteImageView;

@end
