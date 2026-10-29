package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class SuggestedRecipesActivity extends AppCompatActivity
        implements RecipeAdapter.OnRecipeClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.suggestedRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerRecipes);
        TextView emptyView = findViewById(R.id.emptyRecipes);
        Button btnBack = findViewById(R.id.btnBackSuggested);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecipeAdapter adapter = new RecipeAdapter(this);
        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        DatabaseHelper db = new DatabaseHelper(this);
        List<PantryItem> pantry = db.getAllPantryItems();
        List<Recipe> allRecipes = db.getAllRecipes();
        List<Recipe> makeable = RecipeMatcher.findMakeableRecipes(allRecipes, pantry);

        adapter.setRecipes(makeable);

        if (makeable.isEmpty()) {

            if (pantry.isEmpty()) {
                emptyView.setText("Your pantry is empty.\nAdd some ingredients first.");
            } else {
                emptyView.setText("No recipes match your pantry yet.\n"
                        + "Add more ingredients to see suggestions.");
            }
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}
