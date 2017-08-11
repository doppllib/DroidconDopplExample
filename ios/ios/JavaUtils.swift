//
//  StringUtils.swift
//  ios
//
//  Created by Sufei Zhao on 8/11/17.
//  Copyright © 2017 Kevin Galligan. All rights reserved.
//

import Foundation

class JavaUtils {
    static func convertiOSObjectArrayToArray(objArray: IOSObjectArray) -> [Any] {
        var array = [Any]()
        for i in 0..<objArray.length() {
            array.append(objArray.object(at: UInt(i)))
        }
        return array
    }
    
    static func javaList(toList list: JavaUtilList) -> [Any] {
        var array = [Any]()
        for i in 0..<list.size() {
            array.append(list.getWith(i))
        }
        return array
    }
}
