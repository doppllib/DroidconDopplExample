//
//  PlatformContext_iOS.swift
//  ios
//
//  Created by Kachi Nwaobasi on 7/11/17.
//  Copyright © 2017 Kevin Galligan. All rights reserved.
//

import Foundation
import JRE
import UIKit

protocol PlatformContext_iOSDelegate : class {
    func reloadTableView()
    func showEventDetailView(with timeBlock: DDATTimeBlock, andIndex index: Int)
}

class PlatformContext_iOS : NSObject {
    
    
    static func javaList(toList list: JavaUtilList) -> [Any] {
        var array = [Any]()
        for i in 0..<list.size() {
            array.append(list.getWith(i))
        }
        return array
    }
    
    
    var isDayTwo: Bool = false
    var dateFormatter: DateFormatter!
    var timeFormatter: DateFormatter!
    var hourBlocks: [DPRESHourBlock]!
    var conferenceDays: [DPRESDaySchedule]?
    
    weak var reloadDelegate: PlatformContext_iOSDelegate?
    
    
    
    lazy var storageDir: String = {
        let paths = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.documentDirectory, FileManager.SearchPathDomainMask.userDomainMask, true)
        return paths[0]
    }()
    
    //TODO: What is this? Looks like this doesn't need to be here
    var hourBlocksArray : [DPRESHourBlock] {
        var array = [DPRESHourBlock]()
        
        let index = self.isDayTwo ? 1 : 0
        if let days = conferenceDays, days.count > index, let objectArray = days[index].getHourHolders() {
            array.append(contentsOf: convertiOSObjectArrayToArray(objArray: objectArray) as! [DPRESHourBlock])
        }
        if dateFormatter == nil {
            dateFormatter = DateFormatter()
            dateFormatter.dateFormat="hh:mma"
        }
        return array
    }
    
    func getSpeakersArray(from networkEvent: DDATEvent) -> [Any] {
        return PlatformContext_iOS.javaList(toList: networkEvent.getSpeakerList())
    }
    
    fileprivate func formatSpeakersString(from array: [DDATEventSpeaker]) -> String {
        var speakerNames = [String]()
        //TODO: Fix this
//        for speaker in array {
//            speakerNames.append(speaker.getUserAccount().getName())
//        }
        return speakerNames.joined(separator: ", ")
    }
    
    func getEventTime(startTime: NSString, andEnd endTime: NSString) -> String {
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
    
    func updateTableData() {
        hourBlocks.removeAll()
        hourBlocks.append(contentsOf: hourBlocksArray)
    }
    
    fileprivate func convertiOSObjectArrayToArray(objArray: IOSObjectArray) -> [Any] {
        var array = [Any]()
        for i in 0..<objArray.length() {
            array.append(objArray.object(at: UInt(i)))
        }
        return array
    }
    
}

extension PlatformContext_iOS : UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let index = isDayTwo ? 1 : 0
        guard let days = self.conferenceDays, days.count > index else {
            return 0
        }
        let daySchedule = days[index]
        return Int(daySchedule.getHourHolders().length())
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "cellIdentifier", for: indexPath) as! EventListCell
        
        let hourHolder = hourBlocks[indexPath.row]
        let eventObj = hourHolder.getTime()
        
        if let event = eventObj as? DDATEvent {
            cell.titleLabel.text = event.getName().replacingOccurrences(of: "Android", with: "[Sad Puppy]")
            let speakers = getSpeakersArray(from: event) as! [DDATEventSpeaker]
            cell.speakerNamesLabel.text = formatSpeakersString(from: speakers)
        } else if let event = eventObj as? DDATBlock {
            cell.titleLabel.text = event.getName()
            cell.speakerNamesLabel.text = ""
        }
        cell.timeLabel.text = hourHolder.getHourStringDisplay().lowercased()
        cell.startOfBlock = indexPath.row == 0 || hourBlocks[indexPath.row - 1].getTime().getStartLong() != eventObj!.getStartLong()
        cell.layer.isOpaque = true
        return cell
    }
    
}

extension PlatformContext_iOS : UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, didHighlightRowAt indexPath: IndexPath) {
        tableView.cellForRow(at: indexPath)?.isHighlighted = true
    }
    
    func tableView(_ tableView: UITableView, didUnhighlightRowAt indexPath: IndexPath) {
        tableView.cellForRow(at: indexPath)?.isHighlighted = false
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: false)
        
        let eventObj = hourBlocks[indexPath.row].getTime()
        if let networkEvent = eventObj {
            
            reloadDelegate?.showEventDetailView(with: networkEvent, andIndex: indexPath.row)
        }
    }
    
}

extension PlatformContext_iOS : DPRESScheduleDataViewModel_Host {
    func loadCallback(with daySchedules: DPRESDaySchedule!) {
        
    }
    func loadCallback(withDPRESDayScheduleArray daySchedules: IOSObjectArray!) {
        hourBlocks = [DPRESHourBlock]()
        conferenceDays = convertiOSObjectArrayToArray(objArray: daySchedules) as! [DPRESDaySchedule]
        updateTableData()
        reloadDelegate?.reloadTableView()
    }
}

