package net.crescenthikari.bakingapp.features.step.model.step;

import net.crescenthikari.bakingapp.data.model.StepsItem;

/**
 * Created by Muhammad Fiqri Muthohar on 9/20/17.
 */

public class StepData implements RecipeStepListData {

    StepsItem stepsItem;

    public StepData(StepsItem item) {
        stepsItem = item;
    }

    @Override
    public int getStepType() {
        return STEP;
    }

    @Override
    public String getStepText() {
        return stepsItem.getShortDescription();
    }

    public int getStepId() {
        return stepsItem.getId();
    }

    public String getStepFullDesc() {
        return stepsItem.getDescription();
    }

    public boolean haveImage() {
        return stepsItem.getThumbnailURL() != null && !"".equals(stepsItem.getThumbnailURL());
    }

    public String getImageURL() {
        return stepsItem.getThumbnailURL();
    }

    public boolean haveVideo() {
        return stepsItem.getVideoURL() != null && !"".equals(stepsItem.getVideoURL());
    }

    public String getVideoURL() {
        return stepsItem.getVideoURL();
    }
}
