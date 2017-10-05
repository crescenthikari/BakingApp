package net.crescenthikari.bakingapp.features.step.model.step;

/**
 * Created by Muhammad Fiqri Muthohar on 9/20/17.
 */

public class HeaderData implements RecipeStepListData {

    private final String title;

    public HeaderData(String title) {
        this.title = title;
    }

    @Override
    public int getStepType() {
        return HEADER;
    }

    @Override
    public String getStepText() {
        return title;
    }
}
