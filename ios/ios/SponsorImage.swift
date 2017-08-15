//
//  SponsorImage.swift
//  ios
//
//  Created by Wojciech Dziemianczyk on 8/30/16.
//  Copyright © 2016 Kevin Galligan. All rights reserved.
//

import Foundation
import UIKit

struct SponsorResults {
    let totalSpanCount : Int
    let searchResults : [SponsorItem]
}

class SponsorItem : Equatable {
    
    var image : UIImage?
    let spanCount : Int
    let sponsorName : String
    let sponsorImage : String
    let sponsorLink : String
    
    init (count:Int, name:String, image:String, link:String) {
        self.spanCount = count
        self.sponsorName = name
        self.sponsorImage = image
        self.sponsorLink = link
    }
    
    func loadImage(_ completion: @escaping (_ photo:SponsorItem, _ error: NSError?) -> Void) {
        
        let sponsorImageUrl = URL(string: sponsorImage)
        let loadRequest = URLRequest(url:sponsorImageUrl!)
        let task = URLSession.shared.dataTask(with: loadRequest) { data, request, error in
            if error != nil {
                completion(self, error as NSError?)
                return
            }
            
            if data != nil {
                let returnedImage = UIImage(data: data!)
                self.image = returnedImage
                completion(self, nil)
                return
            }
            
            completion(self, nil)
        }
        task.resume()
    }
    
    func sizeToFillWidthOfSize(_ size:CGSize) -> CGSize {
        if image == nil {
            return size
        }
        
        let imageSize = image!.size
        var returnSize = size
        
        let aspectRatio = imageSize.width / imageSize.height
        
        returnSize.height = returnSize.width / aspectRatio
        
        if returnSize.height > size.height {
            returnSize.height = size.height
            returnSize.width = size.height * aspectRatio
        }
        
        return returnSize
    }
}


func == (lhs: SponsorItem, rhs: SponsorItem) -> Bool {
    return lhs.spanCount == rhs.spanCount
}


class Sponsor {
    
    static let SPONSOR_GENERAL   = 0
    static let SPONSOR_PARTY     = 1
    static let SPONSOR_ALL       = 2
    
    class func allSponsors() -> Int
    {
        return Sponsor.SPONSOR_ALL
    }
    
    let processingQueue = OperationQueue()
    
    func getSponsorResults(_ sponsorType: Int, completion : @escaping (_ results: SponsorResults?, _ error : NSError?) -> Void){
        let searchURL = getSearchUrlForSponsorType(sponsorType)
        let searchRequest = URLRequest(url: searchURL)
        
        let task = URLSession.shared.dataTask(with: searchRequest) { data, request, error in
            if error != nil {
                completion(nil,error as NSError?)
                return
            }
            
            do {
                let json = try JSONSerialization.jsonObject(with: data!, options: []) as! NSDictionary
                let totalSpanCount = (json["totalSpanCount"] as? NSNumber)?.intValue
                var sponsorItems = [SponsorItem]()
                
                if let sponsorArray = json["sponsors"] as? [[String: AnyObject]] {
                    for sponsorJson in sponsorArray {
                        
                        let spanCount = sponsorJson["spanCount"] as? Int
                        let sponsorName = sponsorJson["sponsorName"] as? String
                        let sponsorImage = sponsorJson["sponsorImage"] as? String
                        let sponsorLink = sponsorJson["sponsorLink"] as? String
                        
                        let item = SponsorItem(
                            count: spanCount!,
                            name: sponsorName!,
                            image: sponsorImage!,
                            link: sponsorLink!)
                        
                        sponsorItems += [item]
                    }
                }
                
                DispatchQueue.main.async(execute: {
                    completion(SponsorResults(totalSpanCount: totalSpanCount!, searchResults: sponsorItems), nil)
                })
                
            } catch let error as NSError {
                completion(nil, error)
                return
            }
        }
        task.resume()
    }
    
    fileprivate func getSearchUrlForSponsorType(_ sponsorType:Int) -> URL {
        
        var photoUrl = "https://s3.amazonaws.com/droidconsponsers/"
        switch sponsorType {
            case Sponsor.SPONSOR_PARTY:
                photoUrl.append("sponsors_party.json")
            case Sponsor.SPONSOR_ALL:
                photoUrl.append("sponsors_all.json")
            default:
                photoUrl.append("sponsors_general.json")
        }
        return URL(string: photoUrl)!;
    }
}
