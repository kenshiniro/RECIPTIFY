package com.example.recipefinalproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipefinalproject.adapters.HorizontalRecipeAdapter;
import com.example.recipefinalproject.adapters.RecipeAdapter;
import com.example.recipefinalproject.databinding.FragmentHomeBinding;
import com.example.recipefinalproject.models.Recipe;
import com.example.recipefinalproject.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    List<Recipe> favoriteRecipes;
    List<Recipe> popularRecipes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadfavoriteRecipes();
        loadPopularRecipes();
    }

    private void loadPopularRecipes() {
        binding.rvPopulars.setAdapter(new HorizontalRecipeAdapter());
        popularRecipes = new ArrayList<>();
        popularRecipes.add(new Recipe("1","Popular One","recipe1","null","Popular","null", ""));
        popularRecipes.add(new Recipe("2","Popular Two","recipe2","null","Popular","null", ""));
        popularRecipes.add(new Recipe("3","Popular Three","recipe1","null","Popular","null", ""));
        popularRecipes.add(new Recipe("4","Popular Four","recipe2","null","Popular","null", ""));
        HorizontalRecipeAdapter adapter = (HorizontalRecipeAdapter) binding.rvPopulars.getAdapter();
        if (adapter != null) {
            adapter.setRecipeList(favoriteRecipes);
            adapter.notifyDataSetChanged();
        }

    }

    private void loadfavoriteRecipes() {
        favoriteRecipes = new ArrayList<>(); {
        favoriteRecipes.add(new Recipe("1","Favorite One","recipe1","null","Favorite","null", ""));
        favoriteRecipes.add(new Recipe("2","Favorite Two","recipe2","null","Favorite","null", ""));
        favoriteRecipes.add(new Recipe("3","Favorite Three","recipe1","null","Favorite","null",""));
        favoriteRecipes.add(new Recipe("4","Favorite Four","recipe2","null","Favorite","null",""));
        binding.rvFavouriteMeal.setAdapter(new HorizontalRecipeAdapter());
        HorizontalRecipeAdapter adapter = (HorizontalRecipeAdapter) binding.rvFavouriteMeal.getAdapter();
            if (adapter != null) {
                adapter.setRecipeList(favoriteRecipes);
                adapter.notifyDataSetChanged();
            }


        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}