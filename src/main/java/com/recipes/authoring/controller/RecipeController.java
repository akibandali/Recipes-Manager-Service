package com.recipes.authoring.controller;

import com.recipes.authoring.dto.SearchRequest;
import com.recipes.authoring.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.recipes.authoring.dto.Recipe;
import com.recipes.authoring.service.RecipeService;

import java.util.List;

@RestController
@RequestMapping(path = "v1/authoring/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Recipe> getRecipeById (@PathVariable String id) {
        Recipe recipe = recipeService.getRecipeById(id);
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Recipe>> getAllRecipes () {
        List<Recipe> recipes = recipeService.getAllRecipes();
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createNewRecipe (@RequestBody Recipe recipe) {
        return new ResponseEntity<>(recipeService.createNewRecipe(recipe), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Void> updateRecipe (@PathVariable String id, @RequestBody Recipe recipeDTO) {
        recipeService.updateRecipe(id, recipeDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Void> deleteRecipe (@PathVariable String id) {
        recipeService.deleteRecipeById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping(path = "/search", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<Recipe>> searchRecipes (@RequestParam(name = "instruction",required = false) String instruction, @RequestBody SearchRequest searchRequest) {
        validateSearchRequest(instruction,searchRequest);
        List<Recipe> searchResponse = recipeService.searchRecipes(instruction,searchRequest);
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }

    private void validateSearchRequest(String instruction, SearchRequest searchRequest) {
        if (!CollectionUtils.isEmpty(searchRequest.getFilterConditions()) || null ==instruction ) {
            return;
        }
        throw new BadRequestException("Add at least one search condition or instruction");
    }



}
