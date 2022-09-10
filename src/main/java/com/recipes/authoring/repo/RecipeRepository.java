package com.recipes.authoring.repo;

import com.recipes.authoring.dao.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<RecipeEntity, String>, RecipeRepositoryCustom {

}
