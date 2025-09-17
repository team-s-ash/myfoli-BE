package com.slash.project.myfoli.domain.category.repository;

import com.slash.project.myfoli.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
