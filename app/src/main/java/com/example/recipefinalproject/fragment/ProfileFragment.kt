package com.example.recipefinalproject.fragment

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.recipefinalproject.R
import com.example.recipefinalproject.SettingActivity
import com.example.recipefinalproject.adapters.RecipeAdapter
import com.example.recipefinalproject.databinding.FragmentProfileBinding
import com.example.recipefinalproject.models.Recipe
import com.example.recipefinalproject.models.User
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
import com.vansuita.pickimage.listeners.IPickResult
import java.io.ByteArrayOutputStream
import java.util.Objects

class ProfileFragment : Fragment(), IPickResult {
    private var user: User? = null
    private var binding: FragmentProfileBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (FirebaseAuth.getInstance().currentUser == null) {
            AlertDialog.Builder(context)
                .setTitle("Login Required")
                .setMessage("You need to login to view your profile")
                .show()
        } else {
            loadProfile()
            loadUserRecipes()
            init()
        }
    }

    private fun init() {
        binding!!.imageEdit.setOnClickListener { v: View? ->
            PickImageDialog.build(PickSetup()).show(requireActivity())
                .setOnPickResult { r: PickResult ->
                    Log.e("ProfileFragment", "onPickResult: " + r.uri)
                    binding!!.userProfilePic.setImageBitmap(r.bitmap)
                    binding!!.userProfilePic.scaleType = ImageView.ScaleType.CENTER_CROP
                    uploadImage(r.bitmap)
                }.setOnPickCancel {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
        binding!!.imgEditCover.setOnClickListener { View: View? ->
            PickImageDialog.build(PickSetup()).show(requireActivity())
                .setOnPickResult { r: PickResult ->
                    Log.e("ProfileFragment", "onPickResult: " + r.uri)
                    binding!!.imageBanner.setImageBitmap(r.bitmap)
                    binding!!.imageBanner.scaleType = ImageView.ScaleType.CENTER_CROP
                    uploadCoverImage(r.bitmap)
                }.setOnPickCancel {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
        binding!!.btnSetting.setOnClickListener { view1: View? ->
            startActivity(
                Intent(
                    requireContext(),
                    SettingActivity::class.java
                )
            )
        }
    }

    private fun uploadCoverImage(bitmap: Bitmap) {
        val storage = FirebaseStorage.getInstance()
        val storageRef =
            storage.reference.child("images/" + FirebaseAuth.getInstance().uid + "cover.jpg")
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
                    Toast.makeText(
                        requireContext(),
                        "Image Uploaded Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    user!!.cover = Objects.requireNonNull(downloadUri).toString()
                    val reference = FirebaseDatabase.getInstance().reference
                    reference.child("Users")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().uid).toString())
                        .setValue(user)
                } else {
                    Log.e(
                        "ProfileFragment",
                        "onComplete: " + Objects.requireNonNull(task.exception)!!.message
                    )
                }
            }
    }

    private fun uploadImage(bitmap: Bitmap) {
        val storage = FirebaseStorage.getInstance()
        val storageRef =
            storage.reference.child("images/" + FirebaseAuth.getInstance().uid + "image.jpg")
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
                    Toast.makeText(
                        requireContext(),
                        "Image Uploaded Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    user!!.image = Objects.requireNonNull(downloadUri).toString()
                    val reference = FirebaseDatabase.getInstance().reference
                    reference.child("Users")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().uid).toString())
                        .setValue(user)
                } else {
                    Log.e(
                        "ProfileFragment",
                        "onComplete: " + Objects.requireNonNull(task.exception)!!.message
                    )
                }
            }
    }

    override fun onPickResult(r: PickResult) {
        if (r.error == null) {
            Log.e("ProfileFragment", "onPickResult: " + r.uri)
            binding!!.userProfilePic.setImageBitmap(r.bitmap)
        } else {
            Log.e("ProfileFragment", "onPickResult: " + r.error.message)
        }
    }

    private fun loadUserRecipes() {
        binding!!.rvProfile.layoutManager = GridLayoutManager(context, 2)
        binding!!.rvProfile.adapter = RecipeAdapter()
        val reference = FirebaseDatabase.getInstance().reference
        reference.child("Recipes").orderByChild("authorId").equalTo(FirebaseAuth.getInstance().uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val recipes: MutableList<Recipe?> = ArrayList()
                    for (dataSnapshot in snapshot.children) {
                        val recipe = dataSnapshot.getValue(Recipe::class.java)
                        recipes.add(recipe)
                    }
                    (Objects.requireNonNull(binding!!.rvProfile.adapter) as RecipeAdapter).setRecipeList(
                        recipes
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Profile Fragment", "onCancelled:" + error.message)
                }
            })
    }

    private fun loadProfile() {
        val reference = FirebaseDatabase.getInstance().reference
        reference.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().uid).toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        binding!!.txtviewUsername.text = user!!.name
                        binding!!.txtviewEmail.text = user!!.email
                        Glide
                            .with(requireContext())
                            .load(user!!.image)
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher)
                            .into(binding!!.userProfilePic)
                        Glide
                            .with(requireContext())
                            .load(user!!.cover)
                            .centerCrop()
                            .placeholder(R.drawable.bg_default_recipe)
                            .into(binding!!.imageBanner)
                    } else {
                        Log.e("ProfileFragment", "onDataChange: User is non-existent")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ProfileFragment", "onCancelled: " + error.message)
                }
            })
        val user = User()
        user.name = "Joever"
        user.email = "feer@gmail.com"
        binding!!.txtviewUsername.text = user.name
        binding!!.txtviewEmail.text = user.email
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}