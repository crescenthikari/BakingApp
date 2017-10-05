package net.crescenthikari.bakingapp.data.db.table;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by Muhammad Fiqri Muthohar on 9/15/17.
 */

public interface RecipeColumns {

    @DataType(INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(INTEGER)
    String ID = "id";

    @DataType(TEXT)
    @NotNull
    String NAME = "name";

    @DataType(INTEGER)
    @NotNull
    String SERVINGS = "servings";

    @DataType(TEXT)
    @NotNull
    String IMAGE = "image";

    @DataType(TEXT)
    @NotNull
    String INGREDIENTS = "ingredients";

    @DataType(TEXT)
    @NotNull
    String STEPS = "steps";
}
