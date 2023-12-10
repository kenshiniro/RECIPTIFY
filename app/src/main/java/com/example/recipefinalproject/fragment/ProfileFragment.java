package com.example.recipefinalproject.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;


import com.bumptech.glide.Glide;
import com.example.recipefinalproject.R;
import com.example.recipefinalproject.adapters.RecipeAdapter;
import com.example.recipefinalproject.databinding.FragmentProfileBinding;
import com.example.recipefinalproject.models.Recipe;
import com.example.recipefinalproject.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileFragment extends Fragment implements IPickResult {

    private User user;
    private FragmentProfileBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

    binding= FragmentProfileBinding.inflate(inflater,container,false);

    return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        loadProfile();
        loadUserRecipes();
        init();
    }

    private void init() {
        binding.imageEdit.setOnClickListener(v -> {
            PickImageDialog.build(new PickSetup()).show(requireActivity()).setOnPickResult(r -> {
                Log.e("ProfileFragment", "onPickResult: " + r.getUri());
                binding.userProfilePic.setImageBitmap(r.getBitmap());
                binding.userProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                uploadImage(r.getBitmap());
            }).setOnPickCancel(() -> Toast.makeText(requireContext(),"Cancelled", Toast.LENGTH_SHORT).show());
        });

        binding.imgEditCover.setOnClickListener(View -> {
            PickImageDialog.build(new PickSetup()).show(requireActivity()).setOnPickResult(r -> {
                Log.e("ProfileFragment", "onPickResult: " + r.getUri());
                binding.imageBanner.setImageBitmap(r.getBitmap());
                binding.imageBanner.setScaleType(ImageView.ScaleType.CENTER_CROP);
                uploadCoverImage(r.getBitmap());
            }).setOnPickCancel(() -> Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show());
        });
            }

    private void uploadCoverImage(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images/" + FirebaseAuth.getInstance().getUid() + "cover.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            return storageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                Toast.makeText(requireContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                user.setCover(Objects.requireNonNull(downloadUri).toString());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(user);

            } else {
                Log.e("ProfileFragment", "onComplete: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }


    private void uploadImage(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images/" + FirebaseAuth.getInstance().getUid() + "image.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            return storageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                Toast.makeText(requireContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                user.setImage(Objects.requireNonNull(downloadUri).toString());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(user);

            } else {
                Log.e("ProfileFragment", "onComplete: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    @Override
    public void onPickResult(PickResult r) {
        if(r.getError() == null) {
            Log.e("ProfileFragment", "onPickResult: " + r.getUri());
            binding.userProfilePic.setImageBitmap(r.getBitmap());
        } else {
            Log.e("ProfileFragment", "onPickResult: " + r.getError().getMessage());
        }
    }

    private void loadUserRecipes(){
        binding.rvProfile.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.rvProfile.setAdapter(new RecipeAdapter());
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("1","Popular One","recipe1","null","Popular","null",""));
        recipes.add(new Recipe("2","Popular Two","recipe2","null","Popular","null",""));
        recipes.add(new Recipe("3","Popular Three","recipe1","null","Popular","null",""));
        recipes.add(new Recipe("4","Popular Four","recipe2","null","Popular","null",""));
        RecipeAdapter adapter = (RecipeAdapter) binding.rvProfile.getAdapter();
        if (adapter != null) {
            adapter.setRecipeList(recipes);
            adapter.notifyDataSetChanged();
        }
    }
    private void loadProfile(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null) {
                    binding.txtviewUsername.setText(user.getName());
                    binding.txtviewEmail.setText(user.getEmail());

                    Glide
                            .with(requireContext())
                            .load(user.getImage())
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher)
                            .into(binding.userProfilePic);

                    Glide
                            .with(requireContext())
                            .load(user.getCover())
                            .centerCrop()
                            .placeholder(R.drawable.bg_default_recipe)
                            .into(binding.imageBanner);
                }else{
                    Log.e("ProfileFragment","onDataChange: User is non-existent" );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProfileFragment","onCancelled: " + error.getMessage());
            }
        });
        User user = new User();
        user.setName("Joever");
        user.setEmail("feer@gmail.com");
        binding.txtviewUsername.setText(user.getName());
        binding.txtviewEmail.setText(user.getEmail());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}