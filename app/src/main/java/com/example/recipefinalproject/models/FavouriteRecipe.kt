package com.example.recipefinalproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_recipes")
class FavouriteRecipe(@JvmField var recipeId: String) {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}