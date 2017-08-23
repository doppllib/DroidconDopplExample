//
//  SponsorsCollectionViewController.swift
//
//
//  Created by Wojciech Dziemianczyk on 8/30/16.
//
//

import UIKit

class SponsorsViewController: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    
    @IBOutlet weak var navBar: UINavigationBar!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var tabs: UISegmentedControl!
    
    fileprivate let sectionInsets = UIEdgeInsets(top: 10.0, left: 10.0, bottom: 10.0, right: 10.0)
    
    fileprivate var items = NSMutableDictionary()
    fileprivate let sponsorApi = Sponsor()
    fileprivate var totalSpanCount : Int = 12
    fileprivate var itemSpacing : CGFloat = 10
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        collectionView.dataSource = self
        collectionView.delegate = self
        
        let transparentPixel = UIImage(named: "TransparentPixel")
        navBar.setBackgroundImage(transparentPixel, for: UIBarMetrics.default)
        navBar.shadowImage = UIImage()
        navBar.backgroundColor = UIColor.clear
        navBar.isTranslucent = true
        
        loadItems(tabs.selectedSegmentIndex)
    }
    
    @IBAction func indexChanged(_ sender: UISegmentedControl) {
        loadItems(sender.selectedSegmentIndex)
    }
    
    func loadItems(_ type: Int) {
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
                    var uncastList = self.items.object(forKey: key) as? NSMutableArray;
                    if (uncastList == nil){
                        uncastList = NSMutableArray()
                        self.items.setValue(uncastList, forKey: key)
                    }
                    
                    uncastList!.add(item)
                }
            
                self.collectionView?.reloadData()
            }
        })
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return sectionInsets
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return itemSpacing
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return itemSpacing
    }
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return items.count
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return (items.allValues[section] as! [SponsorItem]).count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "SponsorPhotoCell", for: indexPath) as! SponsorPhotoCell
        
        cell.backgroundColor = UIColor.white
        cell.imageView.image = nil
        
        let item = photoForIndexPath(indexPath)
        let url = URL(string: item.sponsorImage)
        
        DispatchQueue.global(qos: DispatchQoS.QoSClass.default).async {
            let data = try? Data(contentsOf: url!)
            DispatchQueue.main.async(execute: {
                if (data != nil) {
                    cell.imageView.image = UIImage(data: data!)
                }
            });
        }
                
        return cell
    }
    
    func photoForIndexPath(_ indexPath: IndexPath) -> SponsorItem {
        return (items.allValues[(indexPath as NSIndexPath).section] as! [SponsorItem])[(indexPath as NSIndexPath).row]
    }
    
    func collectionView(_ collectionView: UICollectionView, shouldSelectItemAt indexPath: IndexPath) -> Bool {
        let item = photoForIndexPath(indexPath)
        UIApplication.shared.open(URL(string: item.sponsorLink)!)
        return false
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let photo = photoForIndexPath(indexPath)
        let paddingPerRow =  CGFloat(itemSpacing)  * (CGFloat(self.totalSpanCount) / CGFloat(photo.spanCount) - 1)
        let collectionSize = collectionView.bounds.size.width - sectionInsets.left - sectionInsets.right - paddingPerRow
        let itemSize = Int(Float(photo.spanCount) / Float(self.totalSpanCount) * Float(collectionSize))
        return CGSize(width: itemSize, height: itemSize)
    }
    
}
