# Smart Pantry Manager

An Android app (Java) that suggests recipes you can make **right now** from the ingredients already in your pantry.

**Module:** [Mobile App Development 700]
**Student:** [Thandolwethu Luyanda Tshabalala 402307374]

## Features

- **Pantry list:** view all saved ingredients with quantity, unit and optional expiry date
- **Add, edit and delete** ingredients (full CRUD), with input validation
- **17 built-in recipes**, saved in the database the first time the app runs
- **Strict recipe matching:** a recipe is suggested only if *every* ingredient is in the pantry in at least the required quantity
- **Smart matching:** handles name differences ("Tomatoes" and "tomato") and unit differences (1.5 kg covers 200 g)
- **Suggested Recipes screen** with a clear message when nothing matches
- **Recipe detail screen** with the ingredients and method
- **Settings screen** (show or hide expiry dates), saved with SharedPreferences
- Data **persists** after the app is closed and reopened

## Screens

1. Pantry List (home screen)
2. Add/Edit Ingredient
3. Suggested Recipes
4. Recipe Detail
5. Settings

## Technology

- Java, Android Studio
- SQLite (SQLiteOpenHelper)
- RecyclerView with custom adapters
- Intents for navigation between screens
- SharedPreferences for settings
- JUnit unit tests
- Minimum SDK: [your minSdk, e.g. 24]

## Why SQLite?

The pantry data is small, personal and needed offline, so a local database on the phone is the simplest and most reliable choice. No server, account or internet connection is needed.

## Database design

| Table | Purpose |
|---|---|
| `pantry_item` | the user's ingredients (name, normalized name, quantity, unit, expiry date) |
| `recipe` | recipe name and method |
| `recipe_ingredient` | the ingredients each recipe needs (many rows per recipe, linked by `recipe_id`) |

## How the strict matching works

1. Each ingredient name is cleaned by `IngredientUtils.normalize` (lowercase, trimmed, simple plurals made singular).
2. Pantry quantities are added up per ingredient, with kg counted as grams and litres counted as millilitres.
3. For each recipe, every ingredient must be present in at least the required amount.
4. If even one ingredient is missing or too small, the recipe is left out.
5. Units that cannot be converted (for example grams and pieces) never match.

## Running the app

1. Open the project folder in Android Studio and wait for Gradle to sync.
2. Create or start an emulator (for example Pixel 4, API 34).
3. Click **Run**.

## Tests

`RecipeMatcherTest` contains 10 unit tests for the matching logic. Right-click the file in Android Studio and choose **Run 'RecipeMatcherTest'**.