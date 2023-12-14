package com.example.recipefinalproject.models

class User {
    var id: String? = null
    var name: String? = null
    var email: String? = null
    var image: String? = null
    var cover: String? = null

    constructor()
    constructor(id: String?, name: String?, email: String?, image: String?, cover: String?) {
        this.id = id
        this.name = name
        this.email = email
        this.image = image
        this.cover = cover
    }
}