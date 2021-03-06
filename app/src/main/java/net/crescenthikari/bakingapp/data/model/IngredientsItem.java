package net.crescenthikari.bakingapp.data.model;

import com.google.gson.annotations.SerializedName;

public class IngredientsItem {

    @SerializedName("quantity")
    private double quantity;

    @SerializedName("measure")
    private String measure;

    @SerializedName("ingredient")
    private String ingredient;

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return
                "IngredientsItem{" +
                        "quantity = '" + quantity + '\'' +
                        ",measure = '" + measure + '\'' +
                        ",ingredient = '" + ingredient + '\'' +
                        "}";
    }
}