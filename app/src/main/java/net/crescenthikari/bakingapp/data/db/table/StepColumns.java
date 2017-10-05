package net.crescenthikari.bakingapp.data.db.table;

import net.crescenthikari.bakingapp.data.db.BakingAppDatabase;
import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by Muhammad Fiqri Muthohar on 9/15/17.
 */

public interface StepColumns {

    @DataType(INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(INTEGER)
    @References(table = BakingAppDatabase.RECIPES, column = RecipeColumns.ID)
    String RECIPE_ID = "recipe_id";

    @DataType(INTEGER)
    String ID = "id";

    @DataType(TEXT)
    @NotNull
    String DESCRIPTION = "description";

    @DataType(TEXT)
    @NotNull
    String SHORT_DESCRIPTION = "short_description";

    @DataType(TEXT)
    @NotNull
    String VIDEO_URL = "video_url";

    @DataType(TEXT)
    @NotNull
    String THUMBNAIL = "thumbnail";
}
