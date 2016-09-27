//
//  ShowEventDetailViewController.swift
//  ios
//
//  Created by Sahil Ishar on 3/14/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

import UIKit

@objc class ShowEventDetailViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, DCPEventDetailHost {
    
    // MARK: Properties
    
    var titleString: String?
    var descriptionString: String?
    var dateTime: String?
    var event: DCDEvent!
    var speakers: [DCDEventSpeaker]?
    var eventDetailPresenter: DCPEventDetailPresenter!
    
    @IBOutlet weak var tableView : UITableView!
    @IBOutlet weak var rsvpButton: UIButton!
    @IBOutlet weak var headerImage: UIImageView!
    // MARK: Lifecycle events
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if eventDetailPresenter != nil {
            eventDetailPresenter.unregister()
        }
        
        eventDetailPresenter = DCPEventDetailPresenter(androidContentContext: DCPAppManager.getContext(), withLong: event.getId(), withDCPEventDetailHost: self)
        
        eventDetailPresenter.refreshData()
        
        tableView.rowHeight = UITableViewAutomaticDimension
        tableView.estimatedRowHeight = 800

        let nib = UINib(nibName: "EventTableViewCell", bundle: nil)
        tableView.registerNib(nib, forCellReuseIdentifier: "eventCell")
        
        let nib2 = UINib(nibName: "SpeakerTableViewCell", bundle: nil)
        tableView.registerNib(nib2, forCellReuseIdentifier: "speakerCell")
        
        self.tableView.contentInset = UIEdgeInsetsZero
        self.tableView.separatorStyle = .None
        self.tableView.reloadData()
        
        styleButton()
    }
    
    deinit {
        eventDetailPresenter.unregister()
    }
    
    // MARK: Data refresh
    
    func dataRefresh() {
        event = eventDetailPresenter.getEventDetailLoadTask().getEvent()
        speakers = PlatformContext_iOS.javaListToNSArray(eventDetailPresenter.getEventDetailLoadTask().getEvent().getSpeakerList()) as? [DCDEventSpeaker]
        tableView.reloadData()
        updateButton()
        updateHeaderImage()
    }
    
    func videoDataRefresh() {
        tableView.reloadData()
    }
    
    func callStreamActivityWithDCTStartWatchVideoTask(task: DCTStartWatchVideoTask){
        performSegueWithIdentifier("LiveStream", sender: self)
    }
    
    func reportErrorWithNSString(error: String){
        let alert = UIAlertView(title: "Video Error", message: error as String, delegate: nil, cancelButtonTitle: "Ok")
        alert.show()
    }
    
    func resetStreamProgress() {
        // TODO
    }
    
    func showTicketOptionsWithNSString(email: String!, withNSString link: String!, withNSString cover: String!) {
        //1. Create the alert controller.
        //It looks like %1$s isn\'t associated with a streaming-enabled ticket.
        let formatted = String(format: "It looks like %@ isn\'t associated with a streaming-enabled ticket. If you already bought a ticket, enter the associated email address below. Otherwise, you can pick up a ticket now!", email)
        
        let alert = UIAlertController(title: "Whoops!", message: formatted, preferredStyle: .Alert)
        
        //2. Add the text field. You can configure it however you need.
        alert.addTextFieldWithConfigurationHandler({ (textField) -> Void in
            textField.text = ""
        })
        
        //3. Grab the value from the text field, and print it when the user clicks OK.
        alert.addAction(UIAlertAction(title: "Cancel", style: .Default,handler: { (action) -> Void in
            
        }))
        alert.addAction(UIAlertAction(title: "OK", style: .Default, handler: { (action) -> Void in
            let textField = alert.textFields![0] as UITextField
            self.eventDetailPresenter.setEventbriteEmailWithNSString(textField.text, withNSString: link, withNSString: cover)
        }))
        
        // 4. Present the alert.
        self.presentViewController(alert, animated: true, completion: nil)
    }
    
    func openSlackWithNSString(slackLink: String!, withNSString slackLinkHttp: String!, withBoolean showSlackDialog: jboolean) {
        // TODO
    }

    
    // MARK: TableView
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0 {
            return 1
        }
        
        return speakers!.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            let cell:EventTableViewCell = tableView.dequeueReusableCellWithIdentifier("eventCell") as! EventTableViewCell

            cell.loadInfo(titleString!, description: descriptionString!, track: event!.getVenue().getName(), time: dateTime!, event: event, eventDetailPresenter: eventDetailPresenter)
            cell.selectionStyle = UITableViewCellSelectionStyle.None
            
            let videoDetailsTask:DCTEventVideoDetailsTask? = eventDetailPresenter.getEventVideoDetailsTask()
            
            if (videoDetailsTask != nil && videoDetailsTask!.hasStream()) {
                cell.liveStreamButton.addTarget(self, action: #selector(ShowEventDetailViewController.liveStreamTapped(_:)), forControlEvents: UIControlEvents.TouchUpInside)
                cell.liveStreamButton.hidden = false
                cell.liveStreamIcon.hidden = false
                if(videoDetailsTask!.isNow()){
                    cell.liveStreamButton.setTitle("LIVE STREAM", forState: UIControlState.Normal)
                } else {
                    cell.liveStreamButton.setTitle("STREAM ARCHIVE", forState: UIControlState.Normal)
                }
            } else {
                cell.liveStreamButton.hidden = true
                cell.liveStreamIcon.hidden = true
            }
            return cell
        } else {
            let cell:SpeakerTableViewCell = tableView.dequeueReusableCellWithIdentifier("speakerCell") as! SpeakerTableViewCell
            
            let speaker = speakers![indexPath.row] as DCDEventSpeaker
            if let speakerDescription = speakers?[indexPath.row].valueForKey("userAccount_")!.valueForKey("profile_") {
                let userAccount = speaker.getUserAccount()
                let imageUrl = userAccount!.avatarImageUrl() ?? ""
                cell.loadInfo(userAccount!.valueForKey("name_") as! String, info: speakerDescription as! String, imgUrl: imageUrl)
            }
            
            cell.selectionStyle = UITableViewCellSelectionStyle.None
            return cell
        }
    }

    // MARK: Action
    
    func styleButton() {
        rsvpButton.layer.cornerRadius = 24
        rsvpButton.layer.masksToBounds = true
        rsvpButton.layer.shadowColor = UIColor.blackColor().CGColor
        rsvpButton.layer.shadowOffset = CGSizeMake(5, 5)
        rsvpButton.layer.shadowRadius = 5
        rsvpButton.layer.shadowOpacity = 1.0
        updateButton()
    }
    
    func updateButton() {
        if (event.isRsvped()) {
            rsvpButton.setImage(UIImage(named: "ic_done"), forState: .Normal)
            rsvpButton.backgroundColor = UIColor.whiteColor()
        } else {
            rsvpButton.setImage(UIImage(named: "ic_add"), forState: .Normal)
            rsvpButton.backgroundColor = UIColor(red: 93/255.0, green: 253/255.0, blue: 173/255.0, alpha: 1.0)
        }
    }
    
    //TODO Use Track class from shared lib folder.
    func updateHeaderImage() {
        let track : DCDTrack  = (event.getCategory() ?? "").isEmpty ?
            DCDTrack.findByServerNameWithNSString("Design") : // Default to design (Same as Android)
        DCDTrack.findByServerNameWithNSString(event.getCategory())
        
        var imageName : String
        
        switch track {
            case DCDTrack.findByServerNameWithNSString("Dev/Design"):
                imageName = "illo_designdevtalk"
                break
            case DCDTrack.findByServerNameWithNSString("Design"):
                imageName = "illo_designtalk"
                break
            case DCDTrack.findByServerNameWithNSString("Design Lab"):
                imageName = "illo_designlab"
                break
            default:
                imageName = "illo_devtalk"
                break
        }
        
        headerImage.image =  UIImage(named:imageName)!
    }

    @IBAction func toggleRsvp(sender: UIButton) {
        eventDetailPresenter.toggleRsvp()
    }
    
    func liveStreamTapped(sender: UIButton) {
        eventDetailPresenter.callStartVideoWithNSString(eventDetailPresenter.getEventVideoDetailsTask().getMergedStreamLink(), withNSString: "")
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if(segue.identifier == "LiveStream") {
            let liveStreamVC = (segue.destinationViewController as! LiveStreamViewController)
            liveStreamVC.titleString = titleString
            liveStreamVC.streamUrl = eventDetailPresenter.getEventVideoDetailsTask().getMergedStreamLink()
            liveStreamVC.coverUrl = ""
        }
    }
}
