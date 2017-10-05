package net.crescenthikari.bakingapp.data.db;

import net.crescenthikari.bakingapp.data.db.table.IngredientColumns;
import net.crescenthikari.bakingapp.data.db.table.RecipeColumns;
import net.crescenthikari.bakingapp.data.db.table.StepColumns;
import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Muhammad Fiqri Muthohar on 9/15/17.
 */

@Database(version = BakingAppDatabase.VERSION)
public class BakingAppDatabase {
    public static final int VERSION = 1;

    @Table(RecipeColumns.class)
    public static final String RECIPES = "recipes";
}
