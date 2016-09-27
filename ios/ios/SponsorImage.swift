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
    
    func loadImage(completion: (photo:SponsorItem, error: NSError?) -> Void) {
        
        let sponsorImageUrl = NSURL(string: sponsorImage)
        let loadRequest = NSURLRequest(URL:sponsorImageUrl!)
        NSURLConnection.sendAsynchronousRequest(loadRequest,
                                                queue: NSOperationQueue.mainQueue()) {
                                                    response, data, error in
                                                    
                                                    if error != nil {
                                                        completion(photo: self, error: error)
                                                        return
                                                    }
                                                    
                                                    if data != nil {
                                                        let returnedImage = UIImage(data: data!)
                                                        self.image = returnedImage
                                                        completion(photo: self, error: nil)
                                                        return
                                                    }
                                                    
                                                    completion(photo: self, error: nil)
        }
    }
    
    func sizeToFillWidthOfSize(size:CGSize) -> CGSize {
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
    static let SPONSOR_STREAMING = 1
    static let SPONSOR_PARTY     = 2
    static let SPONSOR_ALL       = 3
    
    class func allSponsors() -> Int
    {
        return Sponsor.SPONSOR_ALL
    }
    
    let processingQueue = NSOperationQueue()
    
    func getSponsorResults(sponsorType: Int, completion : (results: SponsorResults?, error : NSError?) -> Void){
        let searchURL = getSearchUrlForSponsorType(sponsorType)
        let searchRequest = NSURLRequest(URL: searchURL)
        
        NSURLConnection.sendAsynchronousRequest(searchRequest, queue: processingQueue) {response, data, error in
            if error != nil {
                completion(results: nil,error: error)
                return
            }
            
            do {
                let json = try NSJSONSerialization.JSONObjectWithData(data!, options: []) as! NSDictionary
                let totalSpanCount = json["totalSpanCount"]?.integerValue
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
    
                dispatch_async(dispatch_get_main_queue(), {
                    completion(results:SponsorResults(totalSpanCount: totalSpanCount!, searchResults: sponsorItems), error: nil)
                })
                
            } catch let error as NSError {
                completion(results: nil, error: error)
                return
            }
        }
    }
    
    private func getSearchUrlForSponsorType(sponsorType:Int) -> NSURL {
        
        var photoUrl = "https://s3.amazonaws.com/droidconsponsers/"
        switch sponsorType {
            case Sponsor.SPONSOR_STREAMING:
                photoUrl.appendContentsOf("sponsors_stream.json")
            case Sponsor.SPONSOR_PARTY:
                photoUrl.appendContentsOf("sponsors_party.json")
            case Sponsor.SPONSOR_ALL:
                photoUrl.appendContentsOf("sponsors_all.json")
            default:
                photoUrl.appendContentsOf("sponsors_general.json")
        }
        return NSURL(string: photoUrl)!;
    }
}
