package com.example.recipefinalproject.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinalproject.AllRecipesActivity
import com.example.recipefinalproject.R
import com.example.recipefinalproject.databinding.ItemCategoryBinding
import com.example.recipefinalproject.models.Category

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {
    var categoryList1: List<Category> = ArrayList()

    fun setCategoryList(categoryList: MutableList<Category?>) {
        this.categoryList1 = categoryList.filterNotNull()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val category = categoryList1[position]
        holder.onBind(category)
    }

    override fun getItemCount(): Int {
        return categoryList1.size
    }

    class CategoryHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(category: Category) {
            binding.tvName.text = category.name
            Glide.with(binding.root.context)
                .load(category.image)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.imgBgCategory)

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, AllRecipesActivity::class.java)
                intent.putExtra("type", "category")
                intent.putExtra("category", category.name)
                binding.root.context.startActivity(intent)
            }
        }
    }
}