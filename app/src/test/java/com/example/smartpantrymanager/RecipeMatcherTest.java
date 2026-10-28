package com.example.smartpantrymanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeMatcherTest {


    private PantryItem pantry(String name, double qty, String unit) {
        return new PantryItem(0, name, IngredientUtils.normalize(name), qty, unit, null);
    }

    private Recipe recipe(String name, Object[][] ingredients) {
        Recipe r = new Recipe(0, name, "steps");
        List<RecipeIngredient> list = new ArrayList<>();
        for (Object[] ing : ingredients) {
            list.add(new RecipeIngredient(0, 0, (String) ing[0],
                    ((Number) ing[1]).doubleValue(), (String) ing[2]));
        }
        r.setIngredients(list);
        return r;
    }

    private Recipe omelette() {
        return recipe("Cheese Omelette", new Object[][]{
                {"egg", 3, "pcs"}, {"cheese", 50, "g"}, {"butter", 10, "g"}});
    }


    @Test
    public void allIngredientsPresent_recipeIsSuggested() {
        List<PantryItem> p = Arrays.asList(
                pantry("Eggs", 6, "pcs"), pantry("Cheese", 200, "g"), pantry("Butter", 250, "g"));
        assertTrue(RecipeMatcher.canMake(omelette(), p));
    }

    @Test
    public void oneIngredientMissing_recipeIsRejected() {
        List<PantryItem> p = Arrays.asList(
                pantry("Eggs", 6, "pcs"), pantry("Cheese", 200, "g"));
        assertFalse(RecipeMatcher.canMake(omelette(), p));
    }

    @Test
    public void quantityTooLow_recipeIsRejected() {
        List<PantryItem> p = Arrays.asList(
                pantry("Eggs", 2, "pcs"), pantry("Cheese", 200, "g"), pantry("Butter", 250, "g"));
        assertFalse(RecipeMatcher.canMake(omelette(), p));
    }

    @Test
    public void pluralAndCapitalDifferences_stillMatch() {
        Recipe r = recipe("Tomato Soup", new Object[][]{{"tomato", 4, "pcs"}});
        List<PantryItem> p = Arrays.asList(pantry("TOMATOES", 5, "pcs"));
        assertTrue(RecipeMatcher.canMake(r, p));
    }

    @Test
    public void unitConversion_kgCoversGrams() {
        Recipe r = recipe("Pancakes", new Object[][]{{"flour", 200, "g"}});
        List<PantryItem> p = Arrays.asList(pantry("Flour", 1.5, "kg"));
        assertTrue(RecipeMatcher.canMake(r, p));
    }

    @Test
    public void unitConversion_litresCoverMillilitres() {
        Recipe r = recipe("Smoothie", new Object[][]{{"milk", 250, "ml"}});
        List<PantryItem> p = Arrays.asList(pantry("Milk", 1, "l"));
        assertTrue(RecipeMatcher.canMake(r, p));
    }

    @Test
    public void incompatibleUnits_areRejected() {
        Recipe r = recipe("Scrambled Eggs", new Object[][]{{"egg", 3, "pcs"}});
        List<PantryItem> p = Arrays.asList(pantry("Egg", 100, "g"));
        assertFalse(RecipeMatcher.canMake(r, p));
    }

    @Test
    public void duplicatePantryRows_areAddedTogether() {
        Recipe r = recipe("Tomato Soup", new Object[][]{{"tomato", 4, "pcs"}});
        List<PantryItem> p = Arrays.asList(pantry("Tomato", 2, "pcs"), pantry("Tomatoes", 2, "pcs"));
        assertTrue(RecipeMatcher.canMake(r, p));
    }

    @Test
    public void emptyPantry_noRecipesSuggested() {
        List<Recipe> recipes = Arrays.asList(omelette());
        List<Recipe> result = RecipeMatcher.findMakeableRecipes(recipes, new ArrayList<PantryItem>());
        assertEquals(0, result.size());
    }

    @Test
    public void findMakeableRecipes_returnsOnlyTheMakeableOnes() {
        Recipe toast = recipe("Buttered Toast", new Object[][]{
                {"bread", 2, "pcs"}, {"butter", 10, "g"}});
        List<PantryItem> p = Arrays.asList(
                pantry("Bread", 4, "pcs"), pantry("Butter", 100, "g"));

        List<Recipe> result = RecipeMatcher.findMakeableRecipes(
                Arrays.asList(toast, omelette()), p);

        assertEquals(1, result.size());
        assertEquals("Buttered Toast", result.get(0).getName());
    }
}
