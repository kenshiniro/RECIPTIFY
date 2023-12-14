package com.example.recipefinalproject

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.recipefinalproject.databinding.ActivityAddRecipeBinding
import com.example.recipefinalproject.models.Category
import com.example.recipefinalproject.models.Recipe
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import java.io.ByteArrayOutputStream
import java.util.Objects

class AddRecipeActivity : AppCompatActivity() {
    var binding: ActivityAddRecipeBinding? = null
    private var isImageSelected = false
    private var dialog: ProgressDialog? = null
    var isEdit = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        loadCategories()
        binding!!.btnAddRecipe.setOnClickListener { view: View? -> data }
        binding!!.imgRecipe.setOnClickListener { view: View? -> pickImage() }
        val isEdit = intent.getBooleanExtra("isEdit", false)
        if (isEdit) {
            editRecipe()
        }
    }

    private fun editRecipe() {
        val recipe = intent.getSerializableExtra("recipe") as Recipe?
        isImageSelected = true
        binding!!.etRecipeName.setText(recipe!!.name)
        binding!!.etDescription.setText(recipe.description)
        binding!!.etCookingTime.setText(recipe.time)
        binding!!.etCategory.setText(recipe.category)
        binding!!.etCalories.setText(recipe.calories)
        Glide
            .with(binding!!.root.context)
            .load(recipe.image)
            .centerCrop()
            .placeholder(R.drawable.image_placeholder)
            .into(binding!!.imgRecipe)
        binding!!.btnAddRecipe.text = "Update Recipe"
    }

    private fun loadCategories() {
        val categories: MutableList<String> = ArrayList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
        binding!!.etCategory.setAdapter(adapter)
        val reference = FirebaseDatabase.getInstance().reference.child("Categories")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    for (dataSnapshot in snapshot.children) {
                        categories.add(
                            dataSnapshot.getValue(
                                Category::class.java
                            )!!.name.toString()
                        )
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun pickImage() {
        PickImageDialog.build(PickSetup()).show(this@AddRecipeActivity)
            .setOnPickResult { r: PickResult ->
                Log.e("ProfileFragment", "onPickResult: " + r.uri)
                binding!!.imgRecipe.setImageBitmap(r.bitmap)
                binding!!.imgRecipe.scaleType = ImageView.ScaleType.CENTER_CROP
                isImageSelected = true
            }.setOnPickCancel {
            Toast.makeText(
                this@AddRecipeActivity,
                "Cancelled",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val data: Unit
        private get() {
            val recipeName = Objects.requireNonNull(binding!!.etRecipeName.text).toString()
            val recipeDescription = Objects.requireNonNull(binding!!.etDescription.text).toString()
            val cookingTime = Objects.requireNonNull(binding!!.etCookingTime.text).toString()
            val recipeCategory = binding!!.etCategory.text.toString()
            val calories = Objects.requireNonNull(binding!!.etCalories.text).toString()
            if (recipeName.isEmpty()) {
                binding!!.etRecipeName.error = "Please enter a Recipe Name"
                return
            } else if (recipeDescription.isEmpty()) {
                binding!!.etDescription.error = "Please enter Recipe Description"
            } else if (cookingTime.isEmpty()) {
                binding!!.etCookingTime.error = "Please enter Cooking Time"
            } else if (cookingTime.isEmpty()) {
                binding!!.etCookingTime.error = "Please enter Cooking Time"
            } else if (recipeCategory.isEmpty()) {
                binding!!.etCategory.error = "Please enter Recipe Category"
            } else if (calories.isEmpty()) {
                binding!!.etCalories.error = "Please enter Calories"
            } else if (!isImageSelected) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            } else {
                dialog = ProgressDialog(this)
                dialog!!.setMessage("Uploading Recipe...")
                dialog!!.setCancelable(false)
                dialog!!.show()
                val recipe = Recipe(
                    recipeName,
                    recipeDescription,
                    recipeCategory,
                    calories,
                    cookingTime,
                    "",
                    FirebaseAuth.getInstance().uid
                )
                uploadImage(recipe)
            }
        }

    private fun uploadImage(recipe: Recipe): String {
        val url = arrayOf("")
        binding!!.imgRecipe.isDrawingCacheEnabled = true
        val bitmap = (binding!!.imgRecipe.drawable as BitmapDrawable).bitmap
        binding!!.imgRecipe.isDrawingCacheEnabled = false
        val id = if (isEdit) recipe.id else System.currentTimeMillis().toString() + ""
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("images/" + id + "_recipe.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = storageRef.putBytes(data)
        uploadTask.continueWithTask { task: Task<UploadTask.TaskSnapshot?> ->
            if (!task.isSuccessful) {
                throw Objects.requireNonNull(task.exception)!!
            }
            storageRef.downloadUrl
        }
            .addOnCompleteListener { task: Task<Uri> ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    url[0] = downloadUri.toString()
                    Toast.makeText(
                        this@AddRecipeActivity,
                        "Image Uploaded Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    saveDataInDatabase(recipe, url[0])
                } else {
                    Toast.makeText(
                        this@AddRecipeActivity,
                        "Error in Uploading Image",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog!!.dismiss()
                    Log.e(
                        "ProfileFragment",
                        "onComplete: " + Objects.requireNonNull(task.exception)!!.message
                    )
                }
            }
        return url[0]
    }

    private fun saveDataInDatabase(recipe: Recipe, url: String) {
        recipe.image = url
        val reference = FirebaseDatabase.getInstance().reference.child("Recipes")
        if (isEdit) {
            reference.child(recipe.id.toString()).setValue(recipe).addOnCompleteListener { task: Task<Void?> ->
                dialog!!.dismiss()
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@AddRecipeActivity,
                        "Recipe Updated Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@AddRecipeActivity,
                        "Error in updating recipe",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            val id = reference.push().key
            recipe.id = id
            if (id != null) {
                reference.child(id).setValue(recipe).addOnCompleteListener { task: Task<Void?> ->
                    dialog!!.dismiss()
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@AddRecipeActivity,
                            "Recipe Added Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@AddRecipeActivity,
                            "Error in adding recipe",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}