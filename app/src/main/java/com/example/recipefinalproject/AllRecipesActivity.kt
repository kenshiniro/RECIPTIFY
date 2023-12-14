package com.example.recipefinalproject

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipefinalproject.adapters.RecipeAdapter
import com.example.recipefinalproject.databinding.ActivityAllRecipesBinding
import com.example.recipefinalproject.models.Recipe
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Collections
import java.util.Locale

class AllRecipesActivity : AppCompatActivity() {
    var binding: ActivityAllRecipesBinding? = null
    var reference: DatabaseReference? = null
    var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllRecipesBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        reference = FirebaseDatabase.getInstance().getReference("Recipes")
        binding!!.rvRecipes.layoutManager = GridLayoutManager(this, 2)
        binding!!.rvRecipes.adapter = RecipeAdapter()
        type = intent.getStringExtra("type")
    }

    override fun onResume() {
        super.onResume()
        if (type.equals("category", ignoreCase = true)) {
            filterByCategory()
        } else if (type.equals("search", ignoreCase = true)) {
            loadByRecipes()
        } else {
            loadAllRecipes()
        }
    }

    private fun loadAllRecipes() {
        reference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recipes: MutableList<Recipe?> = ArrayList()
                for (dataSnapshot in snapshot.children) {
                    val recipe = dataSnapshot.getValue(Recipe::class.java)
                    recipes.add(recipe)
                }
                Collections.shuffle(recipes)
                val adapter = binding!!.rvRecipes.adapter as RecipeAdapter?
                adapter?.setRecipeList(recipes)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.message)
            }
        })
    }

    private fun loadByRecipes() {
        val query = intent.getStringExtra("query")
        reference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recipes: MutableList<Recipe?> = ArrayList()
                for (dataSnapshot in snapshot.children) {
                    val recipe = dataSnapshot.getValue(Recipe::class.java)
                    if (recipe!!.name!!.lowercase(Locale.getDefault()).contains(
                            query!!.lowercase(
                                Locale.getDefault()
                            )
                        )
                    ) recipes.add(recipe)
                }
                val adapter = binding!!.rvRecipes.adapter as RecipeAdapter?
                adapter?.setRecipeList(recipes)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.message)
            }
        })
    }

    private fun filterByCategory() {
        val category = intent.getStringExtra("category")
        reference!!.orderByChild("category").equalTo(category)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val recipes: MutableList<Recipe?> = ArrayList()
                    for (dataSnapshot in snapshot.children) {
                        val recipe = dataSnapshot.getValue(Recipe::class.java)
                        recipes.add(recipe)
                    }
                    val adapter = binding!!.rvRecipes.adapter as RecipeAdapter?
                    adapter?.setRecipeList(recipes)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Error", error.message)
                }
            })
    }
}