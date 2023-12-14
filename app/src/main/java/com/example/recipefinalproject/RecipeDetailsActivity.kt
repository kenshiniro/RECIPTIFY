package com.example.recipefinalproject

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.recipefinalproject.databinding.ActivityRecipeDetailsBinding
import com.example.recipefinalproject.models.FavouriteRecipe
import com.example.recipefinalproject.models.Recipe
import com.example.recipefinalproject.room.RecipeRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecipeDetailsActivity : AppCompatActivity() {
    var binding: ActivityRecipeDetailsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailsBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        init()
    }

    private fun init() {
        val recipe = intent.getSerializableExtra("recipe") as Recipe?
        binding!!.tvName.text = recipe!!.name
        binding!!.tvCategory.text = recipe.category
        binding!!.tvDescription.text = recipe.description
        binding!!.tvCalories.text = String.format("%s Calories", recipe.calories)
        Glide
            .with(this@RecipeDetailsActivity)
            .load(recipe.image)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(binding!!.imgRecipe)
        if (recipe.authorId.equals(FirebaseAuth.getInstance().uid, ignoreCase = true)) {
            binding!!.imgEdit.visibility = View.VISIBLE
            binding!!.btnDelete.visibility = View.VISIBLE
        } else {
            binding!!.imgEdit.visibility = View.GONE
            binding!!.btnDelete.visibility = View.GONE
        }
        binding!!.imgEdit.setOnClickListener { view: View? ->
            val intent = Intent(binding!!.root.context, AddRecipeActivity::class.java)
            intent.putExtra("recipe", recipe)
            intent.putExtra("isEdit", true)
            binding!!.root.context.startActivity(intent)
        }
        checkFavorite(recipe)
        binding!!.imgFvrt.setOnClickListener { view: View? -> favouriteRecipe(recipe) }
        binding!!.btnDelete.setOnClickListener { view: View? ->
            AlertDialog.Builder(this)
                .setTitle("Delete Recipe")
                .setMessage("Are you sure you want to delete this recipe?")
                .setPositiveButton("Yes") { dialogInterface: DialogInterface?, i: Int ->
                    val dialog = ProgressDialog(this)
                    dialog.setMessage("Deleting...")
                    dialog.setCancelable(false)
                    dialog.show()
                    val reference = FirebaseDatabase.getInstance().getReference("Recipes")
                    reference.child(recipe.id.toString()).removeValue()
                        .addOnCompleteListener { task: Task<Void?> ->
                            dialog.dismiss()
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Recipe Deleted Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(this, "Failed to delete recipe", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
                .setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
                .show()
        }
        updateDataWithFireBase(recipe.id.toString())
    }

    private fun checkFavorite(recipe: Recipe?) {
        val repository = RecipeRepository(application)
        val isFavourite = repository.isFavourite(recipe!!.id)
        if (isFavourite) {
            binding!!.imgFvrt.setColorFilter(resources.getColor(R.color.accent))
        } else {
            binding!!.imgFvrt.setColorFilter(resources.getColor(R.color.black))
        }
    }

    // Delete this method not working
    // lets try to fix it
    // Solved. Now it's working
    private fun favouriteRecipe(recipe: Recipe?) {
        val repository = RecipeRepository(application)
        val isFavourite = repository.isFavourite(recipe!!.id)
        if (isFavourite) {
            repository.delete(FavouriteRecipe(recipe.id.toString()))
            binding!!.imgFvrt.setColorFilter(resources.getColor(R.color.black))
        } else {
            repository.insert(FavouriteRecipe(recipe.id.toString()))
            binding!!.imgFvrt.setColorFilter(resources.getColor(R.color.accent))
        }
    }

    private fun updateDataWithFireBase(id: String) {
        val reference = FirebaseDatabase.getInstance().getReference("Recipes")
        reference.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recipe = snapshot.getValue(Recipe::class.java)
                binding!!.tvName.text = recipe!!.name
                binding!!.tvCategory.text = recipe.category
                binding!!.tvDescription.text = recipe.description
                binding!!.tvCalories.text = String.format("%s Calories", recipe.calories)
                Glide
                    .with(this@RecipeDetailsActivity)
                    .load(recipe.image)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding!!.imgRecipe)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "onCancelled: ", error.toException())
            }
        })
    }
}