package com.example.recipefinalproject.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipefinalproject.adapters.CategoryAdapter
import com.example.recipefinalproject.databinding.FragmentCategoryBinding
import com.example.recipefinalproject.models.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoriesFragment : Fragment() {
    private var binding: FragmentCategoryBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.rvCategories.adapter = CategoryAdapter()
        loadCategories()
    }

    private fun loadCategories() {
        binding!!.rvCategories.adapter = CategoryAdapter()
        val reference = FirebaseDatabase.getInstance().getReference("Categories")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categories: MutableList<Category?> = ArrayList()
                for (dataSnapshot in snapshot.children) {
                    val category = dataSnapshot.getValue(
                        Category::class.java
                    )
                    categories.add(category)
                }
                val adapter = binding!!.rvCategories.adapter as CategoryAdapter?
                adapter?.setCategoryList(categories)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.message)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}