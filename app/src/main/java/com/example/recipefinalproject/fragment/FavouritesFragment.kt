package com.example.recipefinalproject.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipefinalproject.adapters.RecipeAdapter
import com.example.recipefinalproject.databinding.FragmentFavouritesBinding
import com.example.recipefinalproject.models.Recipe
import com.example.recipefinalproject.room.RecipeRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavouritesFragment : Fragment() {
    var binding: FragmentFavouritesBinding? = null
    var recipeRepository: RecipeRepository? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun loadFavorites() {
        recipeRepository = RecipeRepository(requireActivity().application)
        val favouriteRecipes = recipeRepository!!.allFavourites
        if (favouriteRecipes.isEmpty()) {
            Toast.makeText(requireContext(), "No Favourites", Toast.LENGTH_SHORT).show()
            binding!!.rvFavourites.visibility = View.GONE
        } else {
            binding!!.rvFavourites.layoutManager = GridLayoutManager(requireContext(), 2)
            binding!!.rvFavourites.adapter = RecipeAdapter()
            val recipes: MutableList<Recipe?> = ArrayList()
            val reference = FirebaseDatabase.getInstance().getReference("Recipes")
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        for (dataSnapshot in snapshot.children) {
                            for (favouriteRecipe in favouriteRecipes) {
                                if (dataSnapshot.key == favouriteRecipe.recipeId) {
                                    recipes.add(dataSnapshot.getValue(Recipe::class.java))
                                }
                            }
                        }
                        val adapter = binding!!.rvFavourites.adapter as RecipeAdapter?
                        adapter?.setRecipeList(recipes)
                    } else {
                        binding!!.rvFavourites.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FavouritesFragment", "onCancelled: " + error.message)
                }
            })
        }
    }
}