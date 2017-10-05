package net.crescenthikari.bakingapp.features.step.adapter;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.crescenthikari.bakingapp.R;
import net.crescenthikari.bakingapp.features.step.contract.OnStepClickListener;
import net.crescenthikari.bakingapp.features.step.model.step.RecipeStepListData;
import net.crescenthikari.bakingapp.features.step.model.step.StepData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhammad Fiqri Muthohar on 9/20/17.
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.StepItemHolder> {

    private List<RecipeStepListData> recipeStepListDatas;

    private OnStepClickListener stepClickListener;

    public RecipeStepAdapter() {
        recipeStepListDatas = new ArrayList<>();
    }

    @Override
    public StepItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        StepItemHolder holder = null;
        if (viewType == RecipeStepListData.HEADER) {
            View view = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.step_header_content, null);
            holder = new HeaderHolder(view);
        } else if (viewType == RecipeStepListData.INGREDIENT) {
            View view = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.ingredients_item_content, null);
            holder = new IngredientsViewHolder(view);
        } else if (viewType == RecipeStepListData.STEP) {
            View view = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.step_item_content, null);
            holder = new StepViewHolder(view);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return recipeStepListDatas.get(position).getStepType();
    }

    @Override
    public void onBindViewHolder(StepItemHolder viewHolder, int position) {
        viewHolder.bind(recipeStepListDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return recipeStepListDatas.size();
    }

    public void setRecipeStepListDatas(List<RecipeStepListData> listDatas) {
        recipeStepListDatas.clear();
        recipeStepListDatas.addAll(listDatas);
        notifyDataSetChanged();
    }

    public void setOnStepClickListener(OnStepClickListener listener) {
        stepClickListener = listener;
    }

    abstract class StepItemHolder extends RecyclerView.ViewHolder {
        StepItemHolder(View itemView) {
            super(itemView);
        }

        public abstract void bind(RecipeStepListData data);
    }

    class HeaderHolder extends StepItemHolder {

        @BindView(R.id.step_header_field)
        TextView headerField;

        HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(RecipeStepListData data) {
            headerField.setText(data.getStepText());
        }
    }

    class StepViewHolder extends StepItemHolder {

        @BindView(R.id.step_short_description_field)
        TextView shortDescriptionField;

        @BindView(R.id.step_image)
        ImageView stepImageView;

        StepViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stepClickListener != null) {
                        stepClickListener.onStepClick(((StepData) recipeStepListDatas.get(getAdapterPosition())));
                    }
                }
            });
        }

        @Override
        public void bind(RecipeStepListData data) {
            shortDescriptionField.setText(data.getStepText());
            final StepData stepData = ((StepData) data);
            if (stepData.haveImage()) {
                setupImageFromUrl(stepData.getImageURL());
            } else if (stepData.haveVideo()) {
                setupImageFromUrl(stepData.getVideoURL());
            } else {
                stepImageView.setVisibility(View.GONE);
            }
        }

        private void setupImageFromUrl(String mediaURL) {
            stepImageView.setVisibility(View.VISIBLE);
            Picasso.with(itemView.getContext())
                    .load(mediaURL)
                    .into(stepImageView);
        }
    }

    class IngredientsViewHolder extends StepItemHolder {

        @BindView(R.id.ingredients_field)
        TextView ingredientsField;

        IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(RecipeStepListData data) {
            ingredientsField.setText(getSpannedText(data.getStepText()));
        }

        Spanned getSpannedText(String text) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
            } else {
                return Html.fromHtml(text);
            }
        }
    }
}
