package net.crescenthikari.bakingapp.data.db.table;

import net.crescenthikari.bakingapp.data.db.BakingAppDatabase;
import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by Muhammad Fiqri Muthohar on 9/15/17.
 */

public interface IngredientColumns {

    @DataType(INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(INTEGER)
    @References(table = BakingAppDatabase.RECIPES, column = RecipeColumns.ID)
    String RECIPE_ID = "recipe_id";

    @DataType(REAL)
    @NotNull
    String QUANTITY = "quantity";

    @DataType(TEXT)
    @NotNull
    String MEASURE = "measure";

    @DataType(TEXT)
    @NotNull
    String INGREDIENT = "ingredient";
}
