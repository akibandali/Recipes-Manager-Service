package com.recipes.authoring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import com.recipes.authoring.dao.RecipeEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import com.recipes.authoring.dto.Recipe;
import com.recipes.authoring.repo.RecipeRepository;

@RunWith(MockitoJUnitRunner.class)
public class RecipeEntityServiceTest {

    @Mock
    private RecipeRepository recipeRepository;
    @InjectMocks
    private RecipeService recipeService;
    private static final String RECIPE_ID = "recipe1";

    @Test
    public void testGetRecipeById () {
        RecipeEntity testRecipeEntity = testRecipe();
        doReturn(Optional.of(testRecipeEntity)).when(recipeRepository).findById(RECIPE_ID);
        Recipe actual = recipeService.getRecipeById(RECIPE_ID);
        assertThat(actual).isNotNull();
        assertRecipes(actual, testRecipeEntity);
    }

    @Test
    public void testCreateNewRecipe () {
        Recipe recipeDTO = testRecipeDTO();
        doReturn(RecipeEntity.builder().build()).when(recipeRepository).save(any());
        String actual = recipeService.createNewRecipe(recipeDTO);
        assertThat(actual).isNotNull();
        assertThat(actual).isNotEqualTo(recipeDTO.getId());

    }

    @Test
    public void deleteRecipeById () {
        doNothing().when(recipeRepository).deleteById(RECIPE_ID);
        recipeService.deleteRecipeById(RECIPE_ID);
        Mockito.verify(recipeRepository, times(1)).deleteById(RECIPE_ID);
    }

    @Test
    public void testUpdateRecipe () {
        Recipe recipeDTO = testRecipeDTO();
        recipeDTO.setServings(5);
        recipeDTO.setType("Veg");
        RecipeEntity expected = testRecipe();
        expected.setServings(5);
        expected.setType("Veg");
        doReturn(Optional.of(testRecipe())).when(recipeRepository).findById(RECIPE_ID);
        doReturn(RecipeEntity.builder().build()).when(recipeRepository).save(any());
        recipeService.updateRecipe(recipeDTO.getId(), recipeDTO);
        Mockito.verify(recipeRepository, times(1)).save(expected);
    }

    private void assertRecipes (Recipe actual, RecipeEntity expected) {
        assertThat(actual.getType()).isEqualTo(expected.getType());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getServings()).isEqualTo(expected.getServings());
        assertThat(actual.getChef()).isEqualTo(expected.getChef());
        assertThat(actual.getIngredients()).isEqualTo(expected.getIngredients());
        assertThat(actual.getInstructions()).isEqualTo(expected.getInstructions());

    }

    public static RecipeEntity testRecipe () {
        return RecipeEntity.builder()
            .id(RECIPE_ID)
            .type("Non-Veg")
            .chef("Jordon")
            .name("Biryani")
            .servings(10)
            .ingredients(Arrays.asList("Rice", "chicken", "spices"))
            .instructions(Arrays.asList("Marinate chicken", "Bake chicken in oven"))
            .build();
    }

    public static Recipe testRecipeDTO () {
        return Recipe.builder()
            .id(RECIPE_ID)
            .type("Non-Veg")
            .chef("Jordon")
            .name("Biryani")
            .servings(10)
            .ingredients(Arrays.asList("Rice", "chicken", "spices"))
            .instructions(Arrays.asList("Marinate chicken", "Bake chicken in oven"))
            .build();
    }
}
