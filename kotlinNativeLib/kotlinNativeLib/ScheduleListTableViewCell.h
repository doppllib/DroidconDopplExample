//
//  ScheduleListTableViewCell.h
//  hellatest
//
//  Created by Kevin Galligan on 10/25/17.
//  Copyright © 2017 doppl. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ScheduleListTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *speakerNamesLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@end
