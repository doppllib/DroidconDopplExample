//
//  Welcome4ViewController.swift
//  ios
//
//  Created by Ramona Harrison on 8/10/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

import UIKit

class Welcome4ViewController: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {

    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var arrow: UIImageView!
    
    private let reuseIdentifier = "OnboardingPhotoCell"
    private let sectionInsets = UIEdgeInsets(top: 10.0, left: 10.0, bottom: 10.0, right: 10.0)
    
    private var items = NSMutableDictionary()
    private let sponsorApi = Sponsor()
    private var totalSpanCount : Int = 12
    private var itemSpacing : CGFloat = 10
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        collectionView.dataSource = self
        collectionView.delegate = self
    
        arrow.image = arrow.image!.imageWithRenderingMode(UIImageRenderingMode.AlwaysTemplate)
        arrow.tintColor = UIColor(red: 93/255.0, green: 253/255.0, blue: 173/255.0, alpha: 1.0)
        
        loadItems(Sponsor.allSponsors())
    }
    
    func loadItems(type: Int) {
        self.items.removeAllObjects()
        self.collectionView?.reloadData()
        
        sponsorApi.getSponsorResults(type, completion: {
            (results, error) in
            
            // Check for error
            if error != nil {
                return
            }
            
            // Post results
            if results != nil {
                self.totalSpanCount = (results?.totalSpanCount)!
                
                // Loop through all items and group them in as "sections"
                for item : SponsorItem in (results?.searchResults)! {
                    let key = String(item.spanCount)
                    var uncastList = self.items.objectForKey(key) as? NSMutableArray;
                    if (uncastList == nil){
                        uncastList = NSMutableArray()
                        self.items.setValue(uncastList, forKey: key)
                    }
                    
                    uncastList!.addObject(item)
                }
                
                self.collectionView?.reloadData()
            }
            
        })
        
    }
    
    func collectionView(collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, atIndexPath indexPath: NSIndexPath) -> UICollectionReusableView {
        let headerView = collectionView.dequeueReusableSupplementaryViewOfKind(kind, withReuseIdentifier: "OnboardingHeaderView", forIndexPath: indexPath)
        return headerView
    }
    
    func collectionView(collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                               insetForSectionAtIndex section: Int) -> UIEdgeInsets {
        return sectionInsets
    }
    
    func collectionView(collectionView: UICollectionView, layout collectionViewLayout:
        UICollectionViewLayout, minimumInteritemSpacingForSectionAtIndex section: Int) -> CGFloat {
        return itemSpacing
    }
    
    func collectionView(collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAtIndex section: Int) -> CGFloat {
        return itemSpacing
    }
    
    func numberOfSectionsInCollectionView(collectionView: UICollectionView) -> Int {
        return items.count
    }
    
    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return (items.allValues[section] as! [SponsorItem]).count
    }
    
    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier(reuseIdentifier, forIndexPath: indexPath) as! OnboardingPhotoCell
        
        cell.backgroundColor = UIColor.whiteColor()
        cell.imageView.image = nil
        
        let item = photoForIndexPath(indexPath)
        let url = NSURL(string: item.sponsorImage)
        
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
            let data = NSData(contentsOfURL: url!)
            if(data != nil){
                dispatch_async(dispatch_get_main_queue(), {
                    cell.imageView.image = UIImage(data: data!)
                });
            }
        }
        
        return cell
    }
    
    func photoForIndexPath(indexPath: NSIndexPath) -> SponsorItem {
        return (items.allValues[indexPath.section] as! [SponsorItem])[indexPath.row]
    }
    
    func collectionView(collectionView: UICollectionView, shouldSelectItemAtIndexPath indexPath: NSIndexPath) -> Bool {
        let item = photoForIndexPath(indexPath)
        UIApplication.sharedApplication().openURL(NSURL(string: item.sponsorLink)!)
        return false
    }
    
    func collectionView(collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                               sizeForItemAtIndexPath indexPath: NSIndexPath) -> CGSize {
        let photo = photoForIndexPath(indexPath)
        let paddingPerRow =  CGFloat(itemSpacing)  * (CGFloat(self.totalSpanCount) / CGFloat(photo.spanCount) - 1)
        let collectionSize = collectionView.bounds.size.width - sectionInsets.left - sectionInsets.right - paddingPerRow
        let itemSize = Int(Float(photo.spanCount) / Float(self.totalSpanCount) * Float(collectionSize))
        return CGSize(width: itemSize, height: itemSize)
    }


}
