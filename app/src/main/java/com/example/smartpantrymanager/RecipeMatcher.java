package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeMatcher {

    public static List<Recipe> findMakeableRecipes(List<Recipe> recipes, List<PantryItem> pantry) {
        Map<String, Double> available = buildAvailableMap(pantry);
        List<Recipe> result = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (canMake(recipe, available)) {
                result.add(recipe);
            }
        }
        return result;
    }

    public static boolean canMake(Recipe recipe, List<PantryItem> pantry) {
        return canMake(recipe, buildAvailableMap(pantry));
    }

    private static boolean canMake(Recipe recipe, Map<String, Double> available) {
        List<RecipeIngredient> needed = recipe.getIngredients();
        if (needed == null || needed.isEmpty()) {
            return false;
        }
        for (RecipeIngredient ing : needed) {
            String key = makeKey(ing.getNormalizedName(), ing.getUnit());
            double have = available.getOrDefault(key, 0.0);
            double need = toBaseQuantity(ing.getQuantity(), ing.getUnit());
            if (have + 1e-9 < need) {
                return false;
            }
        }
        return true;
    }

    private static Map<String, Double> buildAvailableMap(List<PantryItem> pantry) {
        Map<String, Double> map = new HashMap<>();
        for (PantryItem item : pantry) {
            String key = makeKey(item.getNormalizedName(), item.getUnit());
            double qty = toBaseQuantity(item.getQuantity(), item.getUnit());
            map.put(key, map.getOrDefault(key, 0.0) + qty);
        }
        return map;
    }

    private static String makeKey(String name, String unit) {
        return IngredientUtils.normalize(name) + "|" + baseUnit(unit);
    }

    static String baseUnit(String unit) {
        String u = unit == null ? "" : unit.trim().toLowerCase();
        if (u.equals("kg") || u.equals("g")) return "g";
        if (u.equals("l") || u.equals("ml")) return "ml";
        return u;
    }

    static double toBaseQuantity(double quantity, String unit) {
        String u = unit == null ? "" : unit.trim().toLowerCase();
        if (u.equals("kg") || u.equals("l")) {
            return quantity * 1000;
        }
        return quantity;
    }
}
