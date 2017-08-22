//
//  TimeUtils.swift
//  ios
//
//  Created by Sufei Zhao on 8/17/17.
//  Copyright © 2017 Kevin Galligan. All rights reserved.
//

import Foundation

class TimeUtils {
    static func getEventTime(startTime: NSString, andEnd endTime: NSString) -> String {
        var startLoc = 7
        var endLoc = 7
        var startLocCutoff = 0
        
        let startFirstDigit = (startTime as NSString).substring(with: NSMakeRange((startTime as NSString).length - 7, 1))
        let endFirstDigit = (endTime as NSString).substring(with: NSMakeRange((endTime as NSString).length - 7, 1))
        
        if startFirstDigit == "0" {
            startLoc = 6
        }
        if endFirstDigit == "0" {
            endLoc = 6
        }
        
        let startAmPm = startTime.substring(with: NSMakeRange(startTime.length - 2, 2))
        let endAmPm = endTime.substring(with: NSMakeRange(endTime.length - 2, 2))
        if startAmPm == endAmPm {
            startLocCutoff = 2
        }
        
        let startTimeStr = startTime.substring(with: NSMakeRange(startTime.length-startLoc, startLoc-startLocCutoff))
        let endTimeStr = endTime.substring(with: NSMakeRange(endTime.length - endLoc, endLoc))
        
        return "\(startTimeStr) - \(endTimeStr)"
    }
}
