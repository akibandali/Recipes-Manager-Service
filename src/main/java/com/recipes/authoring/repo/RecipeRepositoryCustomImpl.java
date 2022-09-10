package com.recipes.authoring.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import com.recipes.authoring.dto.ConditionType;
import com.recipes.authoring.dto.FilterCondition;
import com.recipes.authoring.dto.SearchRequest;
import com.recipes.authoring.exception.BadRequestException;
import com.recipes.authoring.dao.RecipeEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom {

    public static final String INSTRUCTIONS = "instructions";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RecipeEntity> searchRecipes (String instruction,SearchRequest searchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RecipeEntity> query = criteriaBuilder.createQuery(RecipeEntity.class);
        Root<RecipeEntity> recipeRoot = query.from(RecipeEntity.class);
        List<Predicate> predicates = dynamicallyBuildPredicate(searchRequest, recipeRoot, criteriaBuilder,instruction);
        query.select(recipeRoot).where(criteriaBuilder.and((predicates.toArray(Predicate[]::new))));
        return entityManager.createQuery(query).getResultList();
    }

    private List<Predicate> dynamicallyBuildPredicate (SearchRequest searchRequest,
                                                       Root<RecipeEntity> recipeRoot,
                                                       CriteriaBuilder criteriaBuilder,
                                                       String instruction) {
       List<Predicate> predicates= new ArrayList<>();
        if (!CollectionUtils.isEmpty(searchRequest.getFilterConditions())) {
            predicates.addAll(searchRequest.getFilterConditions()
                                .stream()
                                .map(filterCondition -> buildPredicate(filterCondition, recipeRoot, criteriaBuilder))
                                .collect(Collectors.toList()));
        }
        if(null!=instruction){
            predicates.add(getInstructionsPredicate(instruction,recipeRoot,criteriaBuilder));
        }
        
        return predicates;

    }

    private Predicate getInstructionsPredicate(String instruction, Root<RecipeEntity> recipeRoot, CriteriaBuilder criteriaBuilder) {
        Path<String> field =  recipeRoot.join(INSTRUCTIONS);
        return criteriaBuilder.like(field, "%"+instruction+"%");
    }

    private Predicate buildPredicate (FilterCondition condition, Root<RecipeEntity> recipeRoot, CriteriaBuilder criteriaBuilder) {

        if (null == condition.getField() || null == condition.getValue()) {
            throw new BadRequestException("field amd value both should be populated");
        }

        Path<String> field = recipeRoot.get(condition.getField());
        if (field.getJavaType().getName().equals(List.class.getName())) {
            field = recipeRoot.join(condition.getField(),JoinType.LEFT);
        }

        if (ConditionType.INCLUDE.value().equals(condition.getType().value())) {
            return criteriaBuilder.equal(field, condition.getValue());
        }
        else if (ConditionType.EXCLUDE.value().equals(condition.getType().value())) {
            return criteriaBuilder.notEqual(field, condition.getValue());
        }
        else {
            throw new BadRequestException("field type should be either include or exclude");
        }

    }
}
