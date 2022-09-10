package com.recipes.authoring.util;

import com.recipes.authoring.dto.Recipe;
import com.recipes.authoring.dao.RecipeEntity;

import java.util.UUID;

public class RecipeMapper {
    public static Recipe mapRecipeEntityToRecipe (RecipeEntity recipeEntity) {
        return Recipe.builder()
                     .id(recipeEntity.getId())
                     .type(recipeEntity.getType())
                     .chef(recipeEntity.getChef())
                     .name(recipeEntity.getName())
                     .servings(recipeEntity.getServings())
                     .ingredients(recipeEntity.getIngredients())
                     .instructions(recipeEntity.getInstructions())
                     .build();
    }

    public static RecipeEntity mapRecipeToRecipeEntity (Recipe recipe) {
        return RecipeEntity.builder()
                           .id(UUID.randomUUID().toString())
                           .type(recipe.getType())
                           .chef(recipe.getChef())
                           .name(recipe.getName())
                           .servings(recipe.getServings())
                           .ingredients(recipe.getIngredients())
                           .instructions(recipe.getInstructions())
                           .build();

    }
}
