package com.vnapnic.media.model

import java.util.*

open class FileInfo {
    var fileCode: String? = null //Numbering
    var recorder: String? = null //Uploader
    var fileExtName: String? = null //Suffix name
    var optCode: String? = null //Business Number
    var fileContent: Array<Byte>? = null //file
    var fileDesc: String? = null //description
    var recordDate: Date? = null //Upload time
    var fileType: String? = null //Upload file type
    var fileName: String? = null //Upload file name
    var needIndex: Int? = null //Whether to index
}