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
    }
    
    override func viewWillAppear(_ animated: Bool) {
        // refresh every time it appears so that we see updates, doesn't seem to affect scroll position of list
        loadConferenceSchedule()
        tableView.tableHeaderView = nil
        tableView.tableFooterView = nil
        
        tableView.delegate = platformContext
        tableView.dataSource = platformContext
        
        // will refresh data from server only if it is old
        dataPresenter.refreshFromServer()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ShowEventDetail" {
            let detailViewController = segue.destination as! ShowEventDetailViewController
            let event = sender as! DCDEvent
            let speakers = platformContext.getSpeakersArray(from: event) as! [DCDEventSpeaker]
            detailViewController.titleString = event.getName().replacingOccurrences(of: "Android", with: "[Sad Puppy]")
            detailViewController.descriptionString = event.getDescription().replacingOccurrences(of: "Android", with: "[Sad Puppy]")
            detailViewController.event = event
            detailViewController.speakers = speakers
            detailViewController.dateTime = platformContext.getEventTime(startTime: event.getStartFormatted()! as NSString, andEnd: event.getEndFormatted()! as NSString)
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
    
    func showEventDetailView(with event: DCDEvent, andIndex index: Int) {
        track = index
        performSegue(withIdentifier: "ShowEventDetail", sender: event)
    }
    
    func showBlockDetailView(with block: DCDBlock) {
        performSegue(withIdentifier: "ShowBlockDetail", sender: block)
    }
}
