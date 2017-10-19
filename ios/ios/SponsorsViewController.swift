//
//  SponsorsCollectionViewController.swift
//
//
//  Created by Wojciech Dziemianczyk on 8/30/16.
//
//

import UIKit
import doppllib

class SponsorsViewController: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout, DVMSponsorsViewModel_Host {
    
    @IBOutlet weak var navBar: UINavigationBar!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var tabs: UISegmentedControl!
    
    fileprivate let sectionInsets = UIEdgeInsets(top: 10.0, left: 10.0, bottom: 10.0, right: 10.0)
    
    fileprivate let sponsorApi = Sponsor()
    fileprivate var totalSpanCount : Int = 12
    fileprivate var itemSpacing : CGFloat = 10
    
    let viewModel = DVMSponsorsViewModel.forIos()
    var sponsorSections : JavaUtilList = JavaUtilArrayList()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        collectionView.dataSource = self
        collectionView.delegate = self
        
        refreshSponsors()
        
        let transparentPixel = UIImage(named: "TransparentPixel")
        navBar.setBackgroundImage(transparentPixel, for: UIBarMetrics.default)
        navBar.shadowImage = UIImage()
        navBar.backgroundColor = UIColor.clear
        navBar.isTranslucent = true
    }
    
    deinit {
        viewModel.unwire()
    }
    
    func onShowSponsors(with sections: JavaUtilList!) {
        sponsorSections = sections
        self.collectionView?.reloadData()
    }
    
    func onSponsorsFound(with result: DNETDSponsorsResult!) {
        
    }
    
    func onError() {
        print("An error?")
    }
    
    @IBAction func indexChanged(_ sender: UISegmentedControl) {
        refreshSponsors()
    }
    
    func refreshSponsors(){
        viewModel.wire(with: self, with: jint(tabs.selectedSegmentIndex))
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
        return Int(sponsorSections.size())
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return Int((findSponsorSection(section)).getSponsors().size())
    }
    
    func findSponsorSection(_ section: Int) -> DVMSponsorsViewModel_SponsorSection {
        return sponsorSections.getWith(jint(section)) as! DVMSponsorsViewModel_SponsorSection
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "SponsorPhotoCell", for: indexPath) as! SponsorPhotoCell
        
        cell.backgroundColor = UIColor.white
        cell.imageView.image = nil
        
        let item = photoForIndexPath(indexPath)
        let url = URL(string: item.getImage())
        
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
    
    func photoForIndexPath(_ indexPath: IndexPath) -> DNETDSponsorsResult_Sponsor {
        return findSponsorSection((indexPath as NSIndexPath).section).getSponsors().getWith(jint((indexPath as NSIndexPath).row)) as! DNETDSponsorsResult_Sponsor
    }
    
    func collectionView(_ collectionView: UICollectionView, shouldSelectItemAt indexPath: IndexPath) -> Bool {
        let item = photoForIndexPath(indexPath)
        UIApplication.shared.open(URL(string: item.getLink())!)
        return false
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let photo = photoForIndexPath(indexPath)
        let paddingPerRow =  CGFloat(itemSpacing)  * (CGFloat(self.totalSpanCount) / CGFloat(photo.getSpanCount()) - 1)
        let collectionSize = collectionView.bounds.size.width - sectionInsets.left - sectionInsets.right - paddingPerRow
        let itemSize = Int(Float(photo.getSpanCount()) / Float(self.totalSpanCount) * Float(collectionSize))
        return CGSize(width: itemSize, height: itemSize)
    }
    
}
