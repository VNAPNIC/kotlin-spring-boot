package com.vnapnic.common.db.files

import java.util.*

open class MediasInfo {
    var recorder: String? = null //Uploader
    var deviceId: String? = null //device uploaded
    var recordDate: Date? = null //Upload time
    var path: String? = null
    var fileName: String? = null //Upload file name
    var fileExtName: String? = null //Suffix name
    var contentType: String? = null //Upload file type
    var fileDesc: String? = null //description
}