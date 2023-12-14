package com.example.recipefinalproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinalproject.R
import com.example.recipefinalproject.databinding.ItemRecipeBinding
import com.example.recipefinalproject.models.Recipe

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeHolder>() {
    var recipeList2: List<Recipe> = ArrayList()

    fun setRecipeList(recipeList: MutableList<Recipe?>) {
        this.recipeList2 = recipeList.filterNotNull()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        return RecipeHolder(
            ItemRecipeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        val recipe = recipeList2[position]
        holder.onBind(recipe)
    }

    override fun getItemCount(): Int {
        return recipeList2.size
    }

    class RecipeHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(recipe: Recipe) {
            Glide.with(binding.root.context)
                .load(recipe.image)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.bgImgRecipe)

            binding.tvRecipeName.text = recipe.name
        }
    }
}