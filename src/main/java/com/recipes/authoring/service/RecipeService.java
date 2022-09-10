package com.recipes.authoring.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.recipes.authoring.dto.SearchRequest;
import com.recipes.authoring.dao.RecipeEntity;
import com.recipes.authoring.util.RecipeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.recipes.authoring.dto.Recipe;
import com.recipes.authoring.exception.NotFoundException;
import com.recipes.authoring.repo.RecipeRepository;
import static com.recipes.authoring.util.RecipeMapper.mapRecipeEntityToRecipe;
import static com.recipes.authoring.util.RecipeMapper.mapRecipeToRecipeEntity;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe getRecipeById (String id) {
        RecipeEntity recipeEntity = findById(id);
        return mapRecipeEntityToRecipe(recipeEntity);

    }

    public String createNewRecipe (Recipe recipe) {
        RecipeEntity recipeEntity = mapRecipeToRecipeEntity(recipe);
        recipeRepository.save(recipeEntity);
        return recipeEntity.getId();

    }

    private RecipeEntity findById (String id) {
        Optional<RecipeEntity> recipe = recipeRepository.findById(id);
        if (recipe.isEmpty()) {
            throw new NotFoundException(id);
        }
        return recipe.get();
    }

    public void deleteRecipeById (String id) {
        recipeRepository.deleteById(id);
    }

    public void updateRecipe (String id, Recipe recipeDTO) {
        RecipeEntity recipeEntity = findById(id);
        recipeEntity.setChef(recipeDTO.getChef());
        recipeEntity.setServings(recipeDTO.getServings());
        recipeEntity.setName(recipeDTO.getName());
        recipeEntity.setType(recipeDTO.getType());
        if (recipeDTO.getIngredients() != null) {
            recipeEntity.setIngredients(recipeDTO.getIngredients());
        }
        if (recipeDTO.getInstructions() != null) {
            recipeEntity.setInstructions(recipeDTO.getInstructions());
        }
        recipeRepository.save(recipeEntity);
    }

    public List<Recipe> searchRecipes (String instruction, SearchRequest searchRequest) {
        List<RecipeEntity> recipeEntities = recipeRepository.searchRecipes(instruction, searchRequest);
        return recipeEntities.stream().map(RecipeMapper::mapRecipeEntityToRecipe).collect(Collectors.toList());
    }

    public List<Recipe> getAllRecipes () {
        List<RecipeEntity> recipeEntities = recipeRepository.findAll();
        return recipeEntities.stream().map(RecipeMapper::mapRecipeEntityToRecipe).collect(Collectors.toList());

    }
}
