package com.recipes.authoring.repo;

import com.recipes.authoring.dto.SearchRequest;
import com.recipes.authoring.dao.RecipeEntity;

import java.util.List;

public interface RecipeRepositoryCustom {
    List<RecipeEntity> searchRecipes(String instruction,SearchRequest searchRequest);
}
