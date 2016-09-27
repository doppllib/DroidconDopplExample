//
//  ViewController.h
//  ios
//
//  Created by Kevin Galligan on 2/23/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ios-Swift.h"
#import "PlatformContext_iOS.h"

@interface ViewController : UIViewController <PlatformContext_iOSDelegate>

@property (nonatomic, weak) IBOutlet UISegmentedControl *dayChooser;
@property (nonatomic, weak) IBOutlet UITableView *tableView;

@end

