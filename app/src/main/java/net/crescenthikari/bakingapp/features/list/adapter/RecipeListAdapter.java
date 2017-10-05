package net.crescenthikari.bakingapp.features.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.crescenthikari.bakingapp.R;
import net.crescenthikari.bakingapp.data.model.Recipe;
import net.crescenthikari.bakingapp.features.list.contract.OnRecipeItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhammad Fiqri Muthohar on 9/18/17.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private List<Recipe> recipes;
    private OnRecipeItemClickListener itemClickListener;

    public RecipeListAdapter() {
        recipes = new ArrayList<>();
    }

    public RecipeListAdapter(List<Recipe> recipes) {
        setRecipes(recipes);
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.recipe_list_content, null);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder recipeViewHolder, int pos) {
        recipeViewHolder.bind(recipes.get(pos));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        if (recipes != null) {
            this.recipes = recipes;
        } else {
            this.recipes = new ArrayList<>();
        }
    }

    public void setItemClickListener(OnRecipeItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_name)
        TextView recipeNameField;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onRecipeItemClicked(recipes.get(getAdapterPosition()));
                    }
                }
            });
        }

        void bind(Recipe recipe) {
            recipeNameField.setText(recipe.getName());
        }
    }
}
