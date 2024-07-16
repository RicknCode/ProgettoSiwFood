package com.uniroma3.prog.repository;

import com.uniroma3.prog.model.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    public Optional<Recipe> findByName(String name);

    public List<Recipe> findAll(Pageable pageable);

    public List<Recipe> findByNameContainingIgnoreCase(String key1);

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    public List<Recipe> findByCategoryNameContainingIgnoreCase(@Param("keyword") String keyword);
}
