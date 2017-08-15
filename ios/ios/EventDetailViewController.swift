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

@objc class EventDetailViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, DPRESEventDetailHost {
    
    @IBOutlet weak var tableView : UITableView!
    @IBOutlet weak var rsvpButton: UIButton!
    @IBOutlet weak var headerImage: UIImageView!
    
    // MARK: Properties
    var titleString: String?
    var descriptionString: String?
    var dateTime: String?
    var event: DDATEvent!
    var conflict: jboolean!
    var speakers: [DDATUserAccount]?
    var eventDetailPresenter: DPRESEventDetailViewModel!
    
    // MARK: Lifecycle events
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if eventDetailPresenter != nil {
            eventDetailPresenter.unregister()
        }
        
        eventDetailPresenter = DPRESEventDetailViewModel.forIos()
        eventDetailPresenter.register__(with: self)
        eventDetailPresenter.getDetailsWithLong(event.getId())
        
        tableView.estimatedRowHeight = tableView.rowHeight
        tableView.rowHeight = UITableViewAutomaticDimension

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
    func dataRefresh(with eventInfo: DDATEventInfo!) {
        event = eventInfo.getEvent()
        conflict = eventInfo.getConflict()
        speakers = JavaUtils.javaList(toList: eventInfo.getSpeakers()) as? [DDATUserAccount]
        updateAllUi()
    }
    
    func reportError(with error: String){
        let alert = UIAlertController(title: "Error", message: error as String, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Ok", style: .default) { _ in })
        self.present(alert, animated: true) {}
    }
    
    func updateRsvp(with event: DDATEvent!) {
        self.event = event
        updateAllUi()
    }
    
    func updateAllUi() {
        updateButton()
        updateHeaderImage()
        tableView.reloadData()
    }

    // MARK: TableView
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0 {
            return 1
        } else if speakers == nil {
            return 0
        }
        
        return speakers!.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (indexPath as NSIndexPath).section == 0 {
            let cell:EventTableViewCell = tableView.dequeueReusableCell(withIdentifier: "eventCell") as! EventTableViewCell
            cell.descriptionLabel.numberOfLines = 0
            cell.loadInfo(titleString!, description: descriptionString!, track: event!.getVenue().getName(), time: dateTime!, networkEvent: event, eventDetailPresenter: eventDetailPresenter, conflict: conflict)
            cell.selectionStyle = UITableViewCellSelectionStyle.none
            return cell
        } else {
            let cell:SpeakerTableViewCell = tableView.dequeueReusableCell(withIdentifier: "speakerCell") as! SpeakerTableViewCell
            
            let speaker = speakers![indexPath.row] as DDATUserAccount
            if let speakerDescription = (speaker.getProfile()) {
                let imageUrl = speaker.avatarImageUrl() ?? ""
                cell.loadInfo(speaker.getName()!, info: speakerDescription, imgUrl: imageUrl)
            }
            
            cell.selectionStyle = UITableViewCellSelectionStyle.none
            return cell
        }
    }

    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }

    // MARK: Action
    func styleButton() {
        rsvpButton.layer.cornerRadius = 24
        rsvpButton.layer.masksToBounds = true
        rsvpButton.layer.shadowColor = UIColor.black.cgColor
        rsvpButton.layer.shadowOffset = CGSize(width: 5, height: 5)
        rsvpButton.layer.shadowRadius = 5
        rsvpButton.layer.shadowOpacity = 1.0
        rsvpButton.isHidden = true
        updateButton()
    }
    
    func updateButton() {
        if event.isPast() {
            rsvpButton.isHidden = true
        } else {
            rsvpButton.isHidden = false
            if (event.isRsvped()) {
                rsvpButton.setImage(UIImage(named: "ic_done"), for: UIControlState())
                rsvpButton.backgroundColor = UIColor.white
            } else {
                rsvpButton.setImage(UIImage(named: "ic_add"), for: UIControlState())
                rsvpButton.backgroundColor = UIColor(red: 93/255.0, green: 253/255.0, blue: 173/255.0, alpha: 1.0)
            }
        }
    }

    func updateHeaderImage() {
        let track : DDATTrack  = (event.getCategory() ?? "").isEmpty ?
            DDATTrack.findByServerName(with: "Design") : // Default to design (Same as Android)
            DDATTrack.findByServerName(with: event.getCategory())
        
        var imageName : String
        
        switch track {
        case DDATTrack.findByServerName(with: "Dev/Design"):
            imageName = "illo_designdevtalk"
            break
        case DDATTrack.findByServerName(with: "Design"):
            imageName = "illo_designtalk"
            break
        case DDATTrack.findByServerName(with: "Design Lab"):
            imageName = "illo_designlab"
            break
        default:
            imageName = "illo_devtalk"
            break
        }
        
        headerImage.image =  UIImage(named:imageName)!
    }

    @IBAction func toggleRsvp(_ sender: UIButton) {
        eventDetailPresenter.setRsvpWithBoolean(!event.isRsvped(), withLong: event.getId())
    }
}
