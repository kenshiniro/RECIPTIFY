package com.example.recipefinalproject.models

class Category {
    var id: String? = null
    var name: String? = null
    var image: String? = null

    constructor()
    constructor(id: String?, name: String?, image: String?) {
        this.id = id
        this.name = name
        this.image = image
    }
}