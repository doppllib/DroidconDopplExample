//
//  ShowEventDetailViewController.swift
//  ios
//
//  Created by Sahil Ishar on 3/14/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

import UIKit
import JRE
import dcframework

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
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if eventDetailPresenter != nil {
            eventDetailPresenter.unregister()
        }
        
        eventDetailPresenter = DCPEventDetailPresenter(androidContentContext: DCPAppManager.getContext(), withLong: event.getId(), with: self)
        
        eventDetailPresenter.refreshData()
        
        tableView.rowHeight = UITableViewAutomaticDimension
        tableView.estimatedRowHeight = 800

        let nib = UINib(nibName: "EventTableViewCell", bundle: nil)
        tableView.register(nib, forCellReuseIdentifier: "eventCell")
        
        let nib2 = UINib(nibName: "SpeakerTableViewCell", bundle: nil)
        tableView.register(nib2, forCellReuseIdentifier: "speakerCell")
        
        self.tableView.contentInset = UIEdgeInsets.zero
        self.tableView.separatorStyle = .none
        self.tableView.reloadData()
        
        styleButton()
    }
    
    deinit {
        eventDetailPresenter.unregister()
    }
    
    // MARK: Data refresh
    
    func dataRefresh() {
        event = eventDetailPresenter.getEventDetailLoadTask().getEvent()
        speakers = PlatformContext_iOS.javaList(toNSArray: eventDetailPresenter.getEventDetailLoadTask().getEvent().getSpeakerList()) as? [DCDEventSpeaker]
        tableView.reloadData()
        updateButton()
        updateHeaderImage()
    }
    
    func videoDataRefresh() {
        tableView.reloadData()
    }
    
    func callStreamActivity(with task: DCTStartWatchVideoTask){
        performSegue(withIdentifier: "LiveStream", sender: self)
    }
    
    func reportError(with error: String){
        let alert = UIAlertView(title: "Video Error", message: error as String, delegate: nil, cancelButtonTitle: "Ok")
        alert.show()
    }
    
    func resetStreamProgress() {
        // TODO
    }
    
    func showTicketOptions(with email: String!, with link: String!, with cover: String!) {
        //1. Create the alert controller.
        //It looks like %1$s isn\'t associated with a streaming-enabled ticket.
        let formatted = String(format: "It looks like %@ isn\'t associated with a streaming-enabled ticket. If you already bought a ticket, enter the associated email address below. Otherwise, you can pick up a ticket now!", email)
        
        let alert = UIAlertController(title: "Whoops!", message: formatted, preferredStyle: .alert)
        
        //2. Add the text field. You can configure it however you need.
        alert.addTextField(configurationHandler: { (textField) -> Void in
            textField.text = ""
        })
        
        //3. Grab the value from the text field, and print it when the user clicks OK.
        alert.addAction(UIAlertAction(title: "Cancel", style: .default,handler: { (action) -> Void in
            
        }))
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (action) -> Void in
            let textField = alert.textFields![0] as UITextField
            self.eventDetailPresenter.setEventbriteEmailWith(textField.text, with: link, with: cover)
        }))
        
        // 4. Present the alert.
        self.present(alert, animated: true, completion: nil)
    }
    
    func openSlack(with slackLink: String!, with slackLinkHttp: String!, withBoolean showSlackDialog: jboolean) {
        // TODO
    }

    
    // MARK: TableView
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0 {
            return 1
        }
        
        return speakers!.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (indexPath as NSIndexPath).section == 0 {
            let cell:EventTableViewCell = tableView.dequeueReusableCell(withIdentifier: "eventCell") as! EventTableViewCell

            cell.loadInfo(titleString!, description: descriptionString!, track: event!.getVenue().getName(), time: dateTime!, event: event, eventDetailPresenter: eventDetailPresenter)
            cell.selectionStyle = UITableViewCellSelectionStyle.none
            
            let videoDetailsTask:DCTEventVideoDetailsTask? = eventDetailPresenter.getEventVideoDetailsTask()
            
            if (videoDetailsTask != nil && videoDetailsTask!.hasStream()) {
                cell.liveStreamButton.addTarget(self, action: #selector(ShowEventDetailViewController.liveStreamTapped(_:)), for: UIControlEvents.touchUpInside)
                cell.liveStreamButton.isHidden = false
                cell.liveStreamIcon.isHidden = false
                if(videoDetailsTask!.isNow()){
                    cell.liveStreamButton.setTitle("LIVE STREAM", for: UIControlState())
                } else {
                    cell.liveStreamButton.setTitle("STREAM ARCHIVE", for: UIControlState())
                }
            } else {
                cell.liveStreamButton.isHidden = true
                cell.liveStreamIcon.isHidden = true
            }
            return cell
        } else {
            let cell:SpeakerTableViewCell = tableView.dequeueReusableCell(withIdentifier: "speakerCell") as! SpeakerTableViewCell
            
            let speaker = speakers![indexPath.row] as DCDEventSpeaker
            if let speakerDescription = (speakers?[indexPath.row].getUserAccount().getProfile()) {
                let userAccount = speaker.getUserAccount()
                let imageUrl = userAccount!.avatarImageUrl() ?? ""
                cell.loadInfo(userAccount!.getName() as! String, info: speakerDescription as! String, imgUrl: imageUrl)
            }
            
            cell.selectionStyle = UITableViewCellSelectionStyle.none
            return cell
        }
    }

    // MARK: Action
    
    func styleButton() {
        rsvpButton.layer.cornerRadius = 24
        rsvpButton.layer.masksToBounds = true
        rsvpButton.layer.shadowColor = UIColor.black.cgColor
        rsvpButton.layer.shadowOffset = CGSize(width: 5, height: 5)
        rsvpButton.layer.shadowRadius = 5
        rsvpButton.layer.shadowOpacity = 1.0
        updateButton()
    }
    
    func updateButton() {
        if (event.isRsvped()) {
            rsvpButton.setImage(UIImage(named: "ic_done"), for: UIControlState())
            rsvpButton.backgroundColor = UIColor.white
        } else {
            rsvpButton.setImage(UIImage(named: "ic_add"), for: UIControlState())
            rsvpButton.backgroundColor = UIColor(red: 93/255.0, green: 253/255.0, blue: 173/255.0, alpha: 1.0)
        }
    }
    
    //TODO Use Track class from shared lib folder.
    func updateHeaderImage() {
        let track : DCDTrack  = (event.getCategory() ?? "").isEmpty ?
            DCDTrack.findByServerName(with: "Design") : // Default to design (Same as Android)
        DCDTrack.findByServerName(with: event.getCategory())
        
        var imageName : String
        
        switch track {
            case DCDTrack.findByServerName(with: "Dev/Design"):
                imageName = "illo_designdevtalk"
                break
            case DCDTrack.findByServerName(with: "Design"):
                imageName = "illo_designtalk"
                break
            case DCDTrack.findByServerName(with: "Design Lab"):
                imageName = "illo_designlab"
                break
            default:
                imageName = "illo_devtalk"
                break
        }
        
        headerImage.image =  UIImage(named:imageName)!
    }

    @IBAction func toggleRsvp(_ sender: UIButton) {
        eventDetailPresenter.toggleRsvp()
    }
    
    func liveStreamTapped(_ sender: UIButton) {
        eventDetailPresenter.callStartVideo(with: eventDetailPresenter.getEventVideoDetailsTask().getMergedStreamLink(), with: "")
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "LiveStream") {
            let liveStreamVC = (segue.destination as! LiveStreamViewController)
            liveStreamVC.titleString = titleString
            liveStreamVC.streamUrl = eventDetailPresenter.getEventVideoDetailsTask().getMergedStreamLink()
            liveStreamVC.coverUrl = ""
        }
    }
}
