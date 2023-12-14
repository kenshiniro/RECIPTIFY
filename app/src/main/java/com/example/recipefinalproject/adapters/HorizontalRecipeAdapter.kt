package com.example.recipefinalproject.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinalproject.R
import com.example.recipefinalproject.RecipeDetailsActivity
import com.example.recipefinalproject.databinding.ItemRecipeHorizontalBinding
import com.example.recipefinalproject.models.Recipe

class HorizontalRecipeAdapter : RecyclerView.Adapter<HorizontalRecipeAdapter.RecipeHolder>() {
    var recipeList1: MutableList<Recipe> = ArrayList()

    fun setRecipeList(recipeList: MutableList<Recipe?>) {
        this.recipeList1.clear()
        this.recipeList1.addAll(recipeList.filterNotNull())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        return RecipeHolder(
            ItemRecipeHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        val recipe = recipeList1[position]
        holder.onBind(recipe)
    }

    override fun getItemCount(): Int {
        return recipeList1.size
    }

    class RecipeHolder(private val binding: ItemRecipeHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(recipe: Recipe) {
            Glide.with(binding.root.context)
                .load(recipe.image)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.bgImgRecipe)

            binding.tvRecipeName.text = recipe.name

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, RecipeDetailsActivity::class.java)
                intent.putExtra("recipe", recipe)
                binding.root.context.startActivity(intent)
            }
        }
    }
}
