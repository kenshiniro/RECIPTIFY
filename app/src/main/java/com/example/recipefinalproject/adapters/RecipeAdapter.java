package com.example.recipefinalproject.adapters;

import static android.view.View.inflate;

import static com.example.recipefinalproject.databinding.ItemRecipeBinding.inflate;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.recipefinalproject.databinding.ItemRecipeBinding;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder>{
    @NonNull
    @Override
    public RecipeAdapter.RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        return new RecipeHolder(inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeHolder holder,int position){
        holder.onBind();
    }

    @Override
    public int getItemCount(){
        return 4;
    }

    public static class RecipeHolder extends RecyclerView.ViewHolder {
        ItemRecipeBinding binding;
        public RecipeHolder(@NonNull ItemRecipeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
        public void onBind(){

        }
    }
}
