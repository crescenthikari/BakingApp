package net.crescenthikari.bakingapp.data.db;

import android.net.Uri;

import net.crescenthikari.bakingapp.data.db.table.RecipeColumns;
import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Muhammad Fiqri Muthohar on 9/15/17.
 */

@ContentProvider(
        authority = BakingAppProvider.AUTHORITY,
        database = BakingAppDatabase.class)
public final class BakingAppProvider {
    public static final String AUTHORITY = "net.crescenthikari.bakingapp.BakingAppProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private BakingAppProvider() {
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    interface Path {
        String RECIPES = "recipes";
    }

    @TableEndpoint(table = BakingAppDatabase.RECIPES)
    public static class Recipes {
        public static final String[] projections = new String[]{
                RecipeColumns.ID,
                RecipeColumns.NAME,
                RecipeColumns.SERVINGS,
                RecipeColumns.IMAGE,
                RecipeColumns.INGREDIENTS,
                RecipeColumns.STEPS
        };

        @ContentUri(
                path = Path.RECIPES,
                type = "vnd.android.cursor.dir/recipes",
                defaultSort = RecipeColumns.ID + " ASC"
        )
        public static final Uri CONTENT_URI = buildUri(Path.RECIPES);

        @InexactContentUri(
                path = Path.RECIPES + "/#",
                name = "RECIPES_ID",
                type = "vnd.android.cursor.dir/recipes",
                whereColumn = RecipeColumns.ID,
                pathSegment = 1
        )
        public static Uri withId(long id) {
            return buildUri(Path.RECIPES, String.valueOf(id));
        }
    }
}
