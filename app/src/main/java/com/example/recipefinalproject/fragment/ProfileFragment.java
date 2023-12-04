package com.example.recipefinalproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;


import com.example.recipefinalproject.adapters.RecipeAdapter;
import com.example.recipefinalproject.databinding.FragmentProfileBinding;
import com.example.recipefinalproject.models.Recipe;
import com.example.recipefinalproject.models.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

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
    }
    private void loadUserRecipes(){
        binding.rvProfile.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.rvProfile.setAdapter(new RecipeAdapter());
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("1","Popular One","recipe1","null","Popular","null","","","",""));
        recipes.add(new Recipe("2","Popular Two","recipe2","null","Popular","null","","","",""));
        recipes.add(new Recipe("3","Popular Three","recipe1","null","Popular","null","","","",""));
        recipes.add(new Recipe("4","Popular Four","recipe2","null","Popular","null","","","",""));
        RecipeAdapter adapter = (RecipeAdapter) binding.rvProfile.getAdapter();
        if (adapter != null) {
            adapter.setRecipeList(recipes);
            adapter.notifyDataSetChanged();
        }
    }
    private void loadProfile(){
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