package com.example.recipefinalproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipefinalproject.AllRecipesActivity;
import com.example.recipefinalproject.adapters.HorizontalRecipeAdapter;
import com.example.recipefinalproject.adapters.RecipeAdapter;
import com.example.recipefinalproject.databinding.FragmentHomeBinding;
import com.example.recipefinalproject.models.Recipe;
import com.example.recipefinalproject.ui.home.HomeViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadRecipes();
        binding.etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        binding.tvSeeAllFavorite.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), AllRecipesActivity.class);
            intent.putExtra("type", "favourite");
            startActivity(intent);
        });

        binding.tvSeeAllPopulars.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), AllRecipesActivity.class);
            intent.putExtra("type", "popular");
            startActivity(intent);
        });
    }

    private void performSearch() {
        String query = Objects.requireNonNull(binding.etSearch.getText()).toString().trim();
        Intent intent = new Intent(requireContext(), AllRecipesActivity.class);
        intent.putExtra("type", "search");
        intent.putExtra("query", query);
        startActivity(intent);

    }

    private void loadRecipes(){
        binding.rvPopulars.setAdapter(new HorizontalRecipeAdapter());
        binding.rvFavouriteMeal.setAdapter(new HorizontalRecipeAdapter());
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Recipes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> recipes= new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Recipe recipe= dataSnapshot.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                loadPopularRecipes(recipes);
                loadfavoriteRecipes(recipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error",error.getMessage());
            }
        });
    }
    private void loadPopularRecipes(List <Recipe> recipes) {

        List<Recipe> popularRecipes = new ArrayList<>();
        for (int i =0; i<5; i++) {
            int random = (int) (Math.random() * recipes.size());
            popularRecipes.add(recipes.get(random));
        }
        HorizontalRecipeAdapter adapter = (HorizontalRecipeAdapter) binding.rvPopulars.getAdapter();
        if (adapter != null) {
            adapter.setRecipeList(popularRecipes);
        }

    }

    private void loadfavoriteRecipes(List <Recipe> recipes) {
        List<Recipe> favoriteRecipes = new ArrayList<>();
        for (int i =0; i<5; i++) {
            int random = (int) (Math.random() * recipes.size());
            favoriteRecipes.add(recipes.get(random));
        }
        HorizontalRecipeAdapter adapter = (HorizontalRecipeAdapter) binding.rvFavouriteMeal.getAdapter();
            if (adapter != null) {
                adapter.setRecipeList(favoriteRecipes);

            }


        }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}