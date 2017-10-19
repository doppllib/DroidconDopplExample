//
//  Extensions.swift
//  ios
//
//  Created by Sufei Zhao on 8/23/17.
//  Copyright © 2017 Kevin Galligan. All rights reserved.
//

import Foundation
import doppllib

extension Optional where Wrapped == String {
    func isNotNilOrEmpty() -> jboolean {
        return (self != nil) && (!self!.isEmpty)
    }
}

extension String {
    func formatUrl() -> String {
        var url = ""
        if (!self.hasPrefix("http://") || !self.hasPrefix("https://")) {
            url = "http://" + self
        } else {
            url = self
        }
        return url
    }
}
