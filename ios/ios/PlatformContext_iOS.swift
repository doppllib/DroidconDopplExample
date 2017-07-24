//
//  PlatformContext_iOS.swift
//  ios
//
//  Created by Kachi Nwaobasi on 7/11/17.
//  Copyright © 2017 Kevin Galligan. All rights reserved.
//

import Foundation
import JRE

protocol PlatformContext_iOSDelegate : class {
    func reloadTableView()
    func showEventDetailView(with event: DCDEvent, andIndex index: Int)
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
    var hourBlocks: [DCPScheduleBlockHour]!
    var conferenceDays: [DCPConferenceDayHolder]?
    var iOSContext: AndroidContentContext!
    
    weak var reloadDelegate: PlatformContext_iOSDelegate?
    
    
    var appContext: AndroidContentContext {
        return iOSContext
    }
    
    lazy var storageDir: String = {
        let paths = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.documentDirectory, FileManager.SearchPathDomainMask.userDomainMask, true)
        return paths[0]
    }()
    
    
    var hourBlocksArray : [DCPScheduleBlockHour] {
        var array = [DCPScheduleBlockHour]()
        
        let index = self.isDayTwo ? 1 : 0
        if let days = conferenceDays, days.count > index, let objectArray = days[index].getHourHolders() {
            array.append(contentsOf: convertiOSObjectArrayToArray(objArray: objectArray) as! [DCPScheduleBlockHour])
        }
        if dateFormatter == nil {
            dateFormatter = DateFormatter()
            dateFormatter.dateFormat="hh:mma"
        }
        return array
    }
    
    override init() {
        iOSContext = DCPAppManager.getContext()
    }
    
    func getSpeakersArray(from event: DCDEvent) -> [Any] {
        return PlatformContext_iOS.javaList(toList: event.getSpeakerList())
    }
    
    fileprivate func formatSpeakersString(from array: [DCDEventSpeaker]) -> String {
        var speakerNames = [String]()
        for speaker in array {
            speakerNames.append(speaker.getUserAccount().getName())
        }
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
        let eventObj = hourHolder.getScheduleBlock()
        if let event = eventObj as? DCDEvent {
            let speakers = getSpeakersArray(from: event) as! [DCDEventSpeaker]
            cell.titleLabel.text = event.getName().replacingOccurrences(of: "Android", with: "[Sad Puppy]")
            cell.speakerNamesLabel.text = formatSpeakersString(from: speakers)
            cell.timeLabel.text = hourHolder.getStringDisplay()
            //cell.rsvpView.isHidden = event.isRsvped() && !event.isPast()
            
        } else if let event = eventObj as? DCDBlock {
            cell.titleLabel.text = event.getName()
            cell.speakerNamesLabel.text = getEventTime(startTime: event.getStartFormatted() as NSString, andEnd: event.getEndFormatted() as NSString)
            cell.timeLabel.text = ""
            //cell.rsvpView.isHidden = true
        }
        cell.layer.isOpaque = true
        return cell
    }
    
}

extension PlatformContext_iOS : UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: false)
        
        let eventObj = hourBlocks[indexPath.row].getScheduleBlock()
        if let event = eventObj as? DCDEvent {
            reloadDelegate?.showEventDetailView(with: event, andIndex: indexPath.row)
        }
    }
    
}

extension PlatformContext_iOS : DCPConferenceDataHost {
    
    func loadCallback(withDCPConferenceDayHolderArray daySchedules: IOSObjectArray!) {
        hourBlocks = [DCPScheduleBlockHour]()
        conferenceDays = convertiOSObjectArrayToArray(objArray: daySchedules) as! [DCPConferenceDayHolder]
        updateTableData()
        reloadDelegate?.reloadTableView()
    }
    
}

