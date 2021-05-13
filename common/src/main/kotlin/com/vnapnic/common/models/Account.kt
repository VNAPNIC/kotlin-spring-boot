package com.vnapnic.common.models


open class Account {
    var id: String? = null
    var socialId: String? = null
    var username: String? = null
    var password: String? = null
    var email: String? = null

    constructor(id: String? = null, socialId: String? = null, username: String? = null, password: String? = null, email: String? = null) {
        this.id = id
        this.socialId = socialId
        this.username = username
        this.password = password
        this.email = email
    }

    constructor()

}