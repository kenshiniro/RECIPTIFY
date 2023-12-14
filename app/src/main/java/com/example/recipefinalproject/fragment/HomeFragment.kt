package com.example.recipefinalproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.recipefinalproject.AllRecipesActivity
import com.example.recipefinalproject.adapters.HorizontalRecipeAdapter
import com.example.recipefinalproject.databinding.FragmentHomeBinding
import com.example.recipefinalproject.models.Recipe
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRecipes()

        binding?.etSearch?.setOnEditorActionListener { textView: TextView?, actionId: Int, keyEvent: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@setOnEditorActionListener true
            }
            false
        }

        binding?.tvSeeAllFavorite?.setOnClickListener {
            val intent = Intent(requireContext(), AllRecipesActivity::class.java)
            intent.putExtra("type", "favourite")
            startActivity(intent)
        }

        binding?.tvSeeAllPopulars?.setOnClickListener {
            val intent = Intent(requireContext(), AllRecipesActivity::class.java)
            intent.putExtra("type", "popular")
            startActivity(intent)
        }
    }

    private fun performSearch() {
        val query = binding?.etSearch?.text?.toString()?.trim()
        val intent = Intent(requireContext(), AllRecipesActivity::class.java)
        intent.putExtra("type", "search")
        intent.putExtra("query", query)
        startActivity(intent)
    }

    private fun loadRecipes() {
        binding?.rvPopulars?.adapter = HorizontalRecipeAdapter()
        binding?.rvFavouriteMeal?.adapter = HorizontalRecipeAdapter()
        val reference = FirebaseDatabase.getInstance().getReference("Recipes")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recipes: MutableList<Recipe?> = ArrayList()
                for (dataSnapshot in snapshot.children) {
                    val recipe = dataSnapshot.getValue(Recipe::class.java)
                    recipes.add(recipe)
                }
                loadPopularRecipes(recipes)
                loadFavoriteRecipes(recipes)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.message)
            }
        })
    }

    private fun loadPopularRecipes(recipes: List<Recipe?>) {
        val popularRecipes: MutableList<Recipe?> = ArrayList()
        for (i in 0 until 5) {
            val random = (Math.random() * recipes.size).toInt()
            popularRecipes.add(recipes[random])
        }
        val adapter = binding?.rvPopulars?.adapter as HorizontalRecipeAdapter?
        adapter?.setRecipeList(popularRecipes)
    }

    private fun loadFavoriteRecipes(recipes: List<Recipe?>) {
        val favoriteRecipes: MutableList<Recipe?> = ArrayList()
        for (i in 0 until 5) {
            val random = (Math.random() * recipes.size).toInt()
            favoriteRecipes.add(recipes[random])
        }
        val adapter = binding?.rvFavouriteMeal?.adapter as HorizontalRecipeAdapter?
        adapter?.setRecipeList(favoriteRecipes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}