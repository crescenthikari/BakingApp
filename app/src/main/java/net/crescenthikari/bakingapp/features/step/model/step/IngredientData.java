package net.crescenthikari.bakingapp.features.step.model.step;

import net.crescenthikari.bakingapp.data.model.IngredientsItem;

import java.util.List;

/**
 * Created by Muhammad Fiqri Muthohar on 9/20/17.
 */

public class IngredientData implements RecipeStepListData {

    String ingredients;

    public IngredientData(List<IngredientsItem> ingredientsItemList) {
        StringBuilder sb = new StringBuilder();
        for (IngredientsItem item : ingredientsItemList) {
            sb.append("<p><b>").append(String.valueOf(item.getQuantity()))
                    .append(" ").append(item.getMeasure()).append("</b> ")
                    .append(item.getIngredient()).append("</p>");
        }
        ingredients = sb.toString();
    }

    @Override
    public int getStepType() {
        return INGREDIENT;
    }

    @Override
    public String getStepText() {
        return ingredients;
    }
}
