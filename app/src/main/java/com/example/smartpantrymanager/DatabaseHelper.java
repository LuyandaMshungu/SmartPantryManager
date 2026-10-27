package com.example.smartpantrymanager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PANTRY = "pantry_item";
    public static final String TABLE_RECIPE = "recipe";
    public static final String TABLE_RECIPE_ING = "recipe_ingredient";

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_NORMALIZED = "normalized_name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY = "expiry_date";
    public static final String COL_STEPS = "steps";
    public static final String COL_RECIPE_ID = "recipe_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PANTRY + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_NORMALIZED + " TEXT NOT NULL, "
                + COL_QUANTITY + " REAL NOT NULL, "
                + COL_UNIT + " TEXT NOT NULL, "
                + COL_EXPIRY + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPE + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_STEPS + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPE_ING + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_RECIPE_ID + " INTEGER NOT NULL, "
                + COL_NORMALIZED + " TEXT NOT NULL, "
                + COL_QUANTITY + " REAL NOT NULL, "
                + COL_UNIT + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + COL_RECIPE_ID + ") REFERENCES "
                + TABLE_RECIPE + "(" + COL_ID + ") ON DELETE CASCADE)");
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_ING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    private void addRecipe(SQLiteDatabase db, String name, String steps,
                           Object[][] ingredients) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(COL_NAME, name);
        recipeValues.put(COL_STEPS, steps);
        long recipeId = db.insert(TABLE_RECIPE, null, recipeValues);

        for (Object[] ing : ingredients) {
            ContentValues iv = new ContentValues();
            iv.put(COL_RECIPE_ID, recipeId);
            iv.put(COL_NORMALIZED, (String) ing[0]);
            iv.put(COL_QUANTITY, ((Number) ing[1]).doubleValue());
            iv.put(COL_UNIT, (String) ing[2]);
            db.insert(TABLE_RECIPE_ING, null, iv);
        }
    }

    private void seedRecipes(SQLiteDatabase db) {

        addRecipe(db, "Scrambled Eggs",
                "1. Beat the eggs with the milk.\n"
                        + "2. Melt the butter in a pan on medium heat.\n"
                        + "3. Pour in the eggs and stir gently until just set.",
                new Object[][]{
                        {"egg", 3, "pcs"},
                        {"milk", 30, "ml"},
                        {"butter", 10, "g"}
                });

        addRecipe(db, "Cheese Omelette",
                "1. Beat the eggs.\n"
                        + "2. Melt the butter in a pan and add the eggs.\n"
                        + "3. Sprinkle the cheese on top, fold and serve.",
                new Object[][]{
                        {"egg", 3, "pcs"},
                        {"cheese", 50, "g"},
                        {"butter", 10, "g"}
                });

        addRecipe(db, "Simple Tomato Pasta",
                "1. Boil the pasta until soft, then drain.\n"
                        + "2. Fry the chopped onion and tomatoes until soft.\n"
                        + "3. Mix the sauce through the pasta and season.",
                new Object[][]{
                        {"pasta", 200, "g"},
                        {"tomato", 4, "pcs"},
                        {"onion", 1, "pcs"}
                });

        addRecipe(db, "Buttered Toast",
                "1. Toast the bread.\n"
                        + "2. Spread the butter on while still warm.",
                new Object[][]{
                        {"bread", 2, "pcs"},
                        {"butter", 10, "g"}
                });

        addRecipe(db, "Cheese Toast",
                "1. Butter the bread lightly.\n"
                        + "2. Top with cheese.\n"
                        + "3. Grill until the cheese melts and bubbles.",
                new Object[][]{
                        {"bread", 2, "pcs"},
                        {"cheese", 40, "g"},
                        {"butter", 5, "g"}
                });

        addRecipe(db, "French Toast",
                "1. Whisk the eggs and milk together.\n"
                        + "2. Dip each slice of bread in the mixture.\n"
                        + "3. Fry in butter until golden on both sides.",
                new Object[][]{
                        {"bread", 4, "pcs"},
                        {"egg", 2, "pcs"},
                        {"milk", 100, "ml"},
                        {"butter", 10, "g"}
                });

        addRecipe(db, "Pancakes",
                "1. Mix the flour, egg and milk into a smooth batter.\n"
                        + "2. Melt a little butter in a pan.\n"
                        + "3. Pour in batter and cook until golden, flipping once.",
                new Object[][]{
                        {"flour", 200, "g"},
                        {"egg", 1, "pcs"},
                        {"milk", 300, "ml"},
                        {"butter", 20, "g"}
                });

        addRecipe(db, "Mac and Cheese",
                "1. Boil the pasta and drain.\n"
                        + "2. Melt the butter, add the milk and cheese and stir until smooth.\n"
                        + "3. Mix the sauce through the pasta.",
                new Object[][]{
                        {"pasta", 200, "g"},
                        {"cheese", 100, "g"},
                        {"milk", 200, "ml"},
                        {"butter", 20, "g"}
                });

        addRecipe(db, "Egg Fried Rice",
                "1. Heat the oil and fry the chopped onion.\n"
                        + "2. Add the cooked rice and stir-fry.\n"
                        + "3. Push the rice aside, scramble the eggs, then mix together.",
                new Object[][]{
                        {"rice", 200, "g"},
                        {"egg", 2, "pcs"},
                        {"onion", 1, "pcs"},
                        {"oil", 15, "ml"}
                });

        addRecipe(db, "Tuna Salad",
                "1. Drain the tuna.\n"
                        + "2. Chop the tomatoes and onion.\n"
                        + "3. Mix everything together in a bowl.",
                new Object[][]{
                        {"tuna", 1, "pcs"},
                        {"tomato", 2, "pcs"},
                        {"onion", 1, "pcs"}
                });

        addRecipe(db, "Tomato Soup",
                "1. Fry the chopped onion in butter until soft.\n"
                        + "2. Add the chopped tomatoes and a little water.\n"
                        + "3. Simmer for 20 minutes, then blend until smooth.",
                new Object[][]{
                        {"tomato", 6, "pcs"},
                        {"onion", 1, "pcs"},
                        {"butter", 20, "g"}
                });

        addRecipe(db, "Garlic Bread",
                "1. Mash the garlic into the butter.\n"
                        + "2. Spread it on the bread.\n"
                        + "3. Bake at 180 C for 10 minutes.",
                new Object[][]{
                        {"bread", 4, "pcs"},
                        {"garlic", 2, "pcs"},
                        {"butter", 30, "g"}
                });

        addRecipe(db, "Mashed Potato",
                "1. Peel and boil the potatoes until soft.\n"
                        + "2. Drain and mash with the butter and milk.",
                new Object[][]{
                        {"potato", 4, "pcs"},
                        {"butter", 30, "g"},
                        {"milk", 100, "ml"}
                });

        addRecipe(db, "Banana Smoothie",
                "1. Peel the bananas.\n"
                        + "2. Blend with the milk until smooth.",
                new Object[][]{
                        {"banana", 2, "pcs"},
                        {"milk", 250, "ml"}
                });

        addRecipe(db, "Peanut Butter Sandwich",
                "1. Spread the peanut butter on one slice of bread.\n"
                        + "2. Top with the second slice.",
                new Object[][]{
                        {"bread", 2, "pcs"},
                        {"peanut butter", 30, "g"}
                });

        addRecipe(db, "Rice and Beans",
                "1. Fry the chopped onion until soft.\n"
                        + "2. Add the beans and cook for 10 minutes.\n"
                        + "3. Serve over the cooked rice.",
                new Object[][]{
                        {"rice", 200, "g"},
                        {"bean", 200, "g"},
                        {"onion", 1, "pcs"}
                });

        addRecipe(db, "Chicken Stir-Fry",
                "1. Slice the chicken and onion.\n"
                        + "2. Heat the oil and fry the chicken until cooked through.\n"
                        + "3. Add the onion, cook for 3 minutes and serve with rice.",
                new Object[][]{
                        {"chicken", 300, "g"},
                        {"onion", 1, "pcs"},
                        {"oil", 15, "ml"},
                        {"rice", 150, "g"}
                });

    }

    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = toContentValues(item);
        return db.insert(TABLE_PANTRY, null, values);
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, null, null, null, null,
                COL_NAME + " COLLATE NOCASE ASC");
        try {
            while (c.moveToNext()) {
                list.add(fromCursor(c));
            }
        } finally {
            c.close();
        }
        return list;
    }

    public PantryItem getPantryItem(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, COL_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        try {
            if (c.moveToFirst()) {
                return fromCursor(c);
            }
            return null;
        } finally {
            c.close();
        }
    }


    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        return db.update(TABLE_PANTRY, toContentValues(item),
                COL_ID + " = ?", new String[]{String.valueOf(item.getId())});
    }

    public int deletePantryItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_PANTRY, COL_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    private ContentValues toContentValues(PantryItem item) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, item.getName());
        values.put(COL_NORMALIZED, item.getNormalizedName());
        values.put(COL_QUANTITY, item.getQuantity());
        values.put(COL_UNIT, item.getUnit());
        values.put(COL_EXPIRY, item.getExpiryDate()); // null is allowed
        return values;
    }

    private PantryItem fromCursor(Cursor c) {
        return new PantryItem(
                c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                c.getString(c.getColumnIndexOrThrow(COL_NORMALIZED)),
                c.getDouble(c.getColumnIndexOrThrow(COL_QUANTITY)),
                c.getString(c.getColumnIndexOrThrow(COL_UNIT)),
                c.getString(c.getColumnIndexOrThrow(COL_EXPIRY)));
    }
}
