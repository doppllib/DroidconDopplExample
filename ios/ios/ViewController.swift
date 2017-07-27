//
//  ViewController.swift
//  ios
//
//  Created by Kachi Nwaobasi on 7/11/17.
//  Copyright © 2017 Kevin Galligan. All rights reserved.
//

import Foundation
import JRE


class ViewController : UIViewController {

    var track: Int!
    var notesArray: [String]!
    var imagesArray: [UIImageView]!
    var platformContext: PlatformContext_iOS!
    var dataPresenter: DCPConferenceDataPresenter!
    var notes: JavaUtilArrayList!
    var allEvents = false
    @IBOutlet weak var dayChooser: UISegmentedControl!
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        allEvents = self.tabBarController?.selectedIndex == 0
        navigationItem.title = allEvents ? "Droidcon NYC" : "My Agenda"
        
        // Hide the nav bar shadow
        navigationController?.navigationBar.shadowImage = UIImage()
        navigationController?.navigationBar.setBackgroundImage(UIImage(), for: UIBarMetrics.default)
        navigationController?.navigationBar.isTranslucent = false
        
        tableView.estimatedRowHeight = 44
        tableView.rowHeight = UITableViewAutomaticDimension
    }
    
    override func viewWillAppear(_ animated: Bool) {
        // refresh every time it appears so that we see updates, doesn't seem to affect scroll position of list
        loadConferenceSchedule()
        
        tableView.delegate = platformContext
        tableView.dataSource = platformContext
        
        // will refresh data from server only if it is old
        dataPresenter.refreshFromServer()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ShowEventDetail" {
            let detailViewController = segue.destination as! ShowEventDetailViewController
            let networkEvent = sender as! DCDEvent
            let speakers = platformContext.getSpeakersArray(from: networkEvent) as! [DCDEventSpeaker]
            detailViewController.titleString = networkEvent.getName().replacingOccurrences(of: "Android", with: "[Sad Puppy]")
            detailViewController.descriptionString = networkEvent.getDescription().replacingOccurrences(of: "Android", with: "[Sad Puppy]")
            detailViewController.networkEvent = networkEvent
            detailViewController.speakers = speakers
            detailViewController.dateTime = platformContext.getEventTime(startTime: networkEvent.getStartFormatted()! as NSString, andEnd: networkEvent.getEndFormatted()! as NSString)
        }
    }
    
    private func createSDASimple() {
        if platformContext == nil {
            platformContext = PlatformContext_iOS()
            platformContext.reloadDelegate = self
            dataPresenter = DCPConferenceDataPresenter(androidContentContext: DCPAppManager.getContext(), with: platformContext, withBoolean: allEvents)
        } else {
            dataPresenter.refreshConferenceData()
        }
    }
    
    @IBAction func updateTable(_ sender: AnyObject) {
        platformContext.isDayTwo = dayChooser.selectedSegmentIndex > 0
        platformContext.updateTableData()
        tableView.reloadData()
    }
    
    func loadImage(with imagePath: String) {
        let paths = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.documentDirectory, FileManager.SearchPathDomainMask.userDomainMask, true)
        let documentsDirectory = URL(fileURLWithPath: paths[0])
        let path = documentsDirectory.appendingPathComponent(imagePath)
        let image = UIImage(contentsOfFile: path.absoluteString)
        let imageView = UIImageView(image: image)
        imagesArray.append(imageView)
    }
    
    private func loadConferenceSchedule() {
        createSDASimple()
    }
    
}

//MARK: PlatformContext_iOSDelegate
extension ViewController : PlatformContext_iOSDelegate {
    
    func reloadTableView() {
        tableView.reloadData()
    }
    
    func showEventDetailView(with networkEvent: DCDEvent, andIndex index: Int) {
        track = index
        performSegue(withIdentifier: "ShowEventDetail", sender: networkEvent)
    }
    
    func showBlockDetailView(with networkBlock: DCDBlock) {
        performSegue(withIdentifier: "ShowBlockDetail", sender: networkBlock)
    }
}
