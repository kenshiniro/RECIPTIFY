package com.example.recipefinalproject.models

import java.io.Serializable

class Recipe : Serializable {
    var id: String? = null
    var name: String? = null
    var image: String? = null
    var description: String? = null
    var category: String? = null
    var calories: String? = null
    var time: String? = null
    var authorId: String? = null

    constructor()
    constructor(
        name: String?,
        description: String?,
        time: String?,
        category: String?,
        calories: String?,
        image: String?,
        authorId: String?
    ) {
        this.name = name
        this.description = description
        this.time = time
        this.category = category
        this.calories = calories
        this.image = image
        this.authorId = authorId
    }
}