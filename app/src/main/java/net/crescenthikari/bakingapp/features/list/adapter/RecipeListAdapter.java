package net.crescenthikari.bakingapp.features.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        @BindView(R.id.recipe_image)
        ImageView recipeImageView;

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
            if (!TextUtils.isEmpty(recipe.getImage())) {
                recipeImageView.setVisibility(View.VISIBLE);
                Picasso.with(itemView.getContext())
                        .load(recipe.getImage())
                        .placeholder(R.drawable.ic_insert_photo_black_48dp)
                        .error(R.drawable.ic_broken_image_black_48dp)
                        .into(recipeImageView);
            } else {
                recipeImageView.setVisibility(View.GONE);
            }
        }
    }
}
